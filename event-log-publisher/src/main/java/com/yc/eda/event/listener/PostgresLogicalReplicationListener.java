package com.yc.eda.event.listener;

import com.yc.eda.event.config.EventPubProperties;
import com.yc.eda.event.config.PostgresReplicationDbProperties;
import com.yc.eda.event.decoder.PgOutputDecoder;
import com.yc.eda.event.decoder.PgOutputMessageType;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.postgresql.PGConnection;
import org.postgresql.replication.PGReplicationStream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.nio.ByteBuffer;
import java.sql.*;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Component
@Slf4j
public class PostgresLogicalReplicationListener {

    private EventPubProperties eventPubProperties;
    private PostgresReplicationDbProperties postgresReplicationDbProperties;
    private PgOutputDecoder decoder;
    private MessagePublisherService messagePublisherService;

    @Autowired
    public PostgresLogicalReplicationListener(EventPubProperties eventPubProperties,
                                              PostgresReplicationDbProperties postgresReplicationDbProperties,
                                              PgOutputDecoder decoder, MessagePublisherService service) {
        this.eventPubProperties = eventPubProperties;
        this.postgresReplicationDbProperties = postgresReplicationDbProperties;
        this.decoder = decoder;
        this.messagePublisherService = service;
    }

    @PostConstruct
    public void listen() throws SQLException, InterruptedException {

        createPublicationIfNotExist();

        Connection pgConnection = DriverManager.getConnection(
                postgresReplicationDbProperties.getDbUrl(),
                postgresReplicationDbProperties.getConfigAsProperties());

        PGReplicationStream stream = openStream(pgConnection);

        while (true) {
            // non blocking receive message
            if (pgConnection.isClosed()) {
                log.warn("Existing connection closed, opening a new one");
                pgConnection = DriverManager.getConnection(
                        postgresReplicationDbProperties.getDbUrl(),
                        postgresReplicationDbProperties.getConfigAsProperties());
            }

            ByteBuffer msg = stream.readPending();

            if (msg == null) {
                TimeUnit.MILLISECONDS.sleep(10L);
                continue;
            }

            PgOutputMessageType type = extractType(msg);
            if (type == PgOutputMessageType.RELATION) {
                decoder.loadTables(msg);
            } else if (type == PgOutputMessageType.INSERT) {
                log.info("Insertion detected");
                Map<String, String> record = decoder.decodeInsertion(msg);
                log.info("Record:{}", record);
                messagePublisherService.publishMessage(record);
            }

            //feedback
            stream.setAppliedLSN(stream.getLastReceiveLSN());
            stream.setFlushedLSN(stream.getLastReceiveLSN());
        }
    }


    @SneakyThrows
    private PGReplicationStream openStream(Connection connection) {
        PGConnection replConnection = connection.unwrap(PGConnection.class);
        createSlot(replConnection, eventPubProperties.getReplicationSlot());
        return replConnection.getReplicationAPI()
                .replicationStream()
                .logical()
                .withSlotName(eventPubProperties.getReplicationSlot())
                .withSlotOption("proto_version", 1)
                .withSlotOption("publication_names", eventPubProperties.getPublication())
                .withStatusInterval(20, TimeUnit.SECONDS)
                .start();
    }


    private void createSlot(PGConnection pgConnection, String slotName) {
        try {
            pgConnection
                    .getReplicationAPI()
                    .createReplicationSlot()
                    .logical()
                    .withSlotName(slotName)
                    .withOutputPlugin("pgoutput")
                    .make();
        } catch (Exception e) {
            log.error("Slot already created possibly, Error: " + e.getMessage());
        }
    }

    private PgOutputMessageType extractType(ByteBuffer buffer) {
        // Cache position as we're going to peak at the first byte to determine message type
        // We need to reprocess all BEGIN/COMMIT messages regardless.
        int position = buffer.position();
        try {
            PgOutputMessageType type = PgOutputMessageType.forType((char) buffer.get());
            log.info("Message Type: {}", type);
            return type;
        } finally {
            // Reset buffer position
            buffer.position(position);
        }
    }

    @SneakyThrows
    private void createPublicationIfNotExist() {
        Connection connection = DriverManager.getConnection(
                postgresReplicationDbProperties.getDbUrl(),
                postgresReplicationDbProperties.getConfigAsProperties());

        try (Statement stmt = connection.createStatement()) {
            ResultSet publicationSet = stmt.executeQuery(
                    String.format("SELECT count(1) from pg_publication where pubname='%s'",
                            eventPubProperties.getPublication()));
            publicationSet.next();
            int count = publicationSet.getInt(1);
            if (count == 0) {
                createPublication(connection, eventPubProperties.getPublication(),
                        eventPubProperties.getEventTable());
            } else {
                log.info("publication {} already exists",
                        eventPubProperties.getPublication());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void createPublication(Connection connection, String publicationName, String eventTable) {
        try (Statement stmt = connection.createStatement()) {
            String sql = String.format("CREATE PUBLICATION %s FOR TABLE %s;", publicationName, eventTable);
            stmt.execute(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
