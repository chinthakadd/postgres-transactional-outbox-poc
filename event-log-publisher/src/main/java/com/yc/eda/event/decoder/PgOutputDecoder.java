package com.yc.eda.event.decoder;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Inspired by: https://github.com/getyourguide/PgOutputDump/blob/main/src/main/java/org/getyourguide/PgOutputDecoder.java
 * <p>
 * Debezium Decoder.
 * https://github.com/debezium/debezium/blob/d81b5578652dbc1d81ea8a7793ee221ab9ec8e57/debezium-connector-postgres/
 * src/main/java/io/debezium/connector/postgresql/connection/pgoutput/PgOutputMessageDecoder.java
 */
@Slf4j
@Component
public class PgOutputDecoder {

    Map<Integer, Table> tables = new HashMap<>();

    public void loadTables(ByteBuffer relationalMessage) {
        var byteOne = relationalMessage.get();
        var type = new String(new byte[]{byteOne}, 0, 1);

        if (type.toCharArray()[0] != PgOutputMessageType.RELATION.getPgChar()) {
            throw new RuntimeException("Not an relational operation");
        }

        int id = relationalMessage.getInt();
        String namespace = PgOutputUtil.readString(relationalMessage);
        String relation = PgOutputUtil.readString(relationalMessage);
        // not used, but still needed
        relationalMessage.get(); //  replicaIdentity
        short nColumns = relationalMessage.getShort();

        var table = new Table(namespace, relation);

        // Read columns information
        List<Column> columnNames = new ArrayList<>();

        for (int i = 0; i < nColumns; i++) {
            var isKey = relationalMessage.get();
            var columnName = PgOutputUtil.readString(relationalMessage);
            var columnTypeId = relationalMessage.getInt();
            relationalMessage.getInt(); //atttypmod
            columnNames.add(new Column(i, isKey == 1, columnName, columnTypeId));
        }

        if (!tables.containsKey(id)) {
            tables.put(id, table);
        }
        table.setColumns(columnNames);
        log.info("Table: {} loaded", table.getName());
    }

    public Map<String, String> decodeInsertion(ByteBuffer msg) {
        var byteOne = msg.get();
        var type = new String(new byte[]{byteOne}, 0, 1);
        if (type.toCharArray()[0] != PgOutputMessageType.INSERT.getPgChar()) {
            throw new RuntimeException("Not an insert operation");
        }
        int id = msg.getInt();
        if (tables.isEmpty() || !tables.containsKey(id)) {
            throw new RuntimeException("Table mapping does not exist");
        }
        Table table = tables.get(id);
        log.info("Insert into {} ({})", table.getName(),
                table.getColumns().stream()
                        .map(Column::getName).collect(Collectors.joining(", ")));

        var byte1 = msg.get();
        var newTuple = new String(new byte[]{byte1}, 0, 1);
        assert newTuple.equals("N");
        List<String> columnData = PgOutputUtil.readTupleData(msg);

        return IntStream.range(0, columnData.size())
                .boxed()
                .collect(Collectors.toMap(
                        i -> table.getColumns().get(i).getName(),
                        columnData::get
                ));
    }

}
