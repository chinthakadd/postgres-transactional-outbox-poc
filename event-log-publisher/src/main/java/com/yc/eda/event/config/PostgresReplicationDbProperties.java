package com.yc.eda.event.config;

import lombok.Data;
import org.postgresql.PGProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.Properties;

/**
 * postgres-replication:
 * db-url: jdbc:postgresql://localhost:5432/testdb
 * username: postgres
 * password: postgres
 * supported-min-server-version: 9.4
 * replication: database
 * query-mode: simple
 */
@Configuration
@ConfigurationProperties(prefix = "postgres-replication")
@Data
public class PostgresReplicationDbProperties {
    private String dbUrl;
    private String username;
    private String password;
    private String supportedMinServerVersion;
    private String replication;
    private String queryMode;

    public Properties getConfigAsProperties() {
        Properties props = new Properties();
        PGProperty.USER.set(props, username);
        PGProperty.PASSWORD.set(props, password);
        PGProperty.ASSUME_MIN_SERVER_VERSION.set(props, supportedMinServerVersion);
        PGProperty.REPLICATION.set(props, replication);
        PGProperty.PREFER_QUERY_MODE.set(props, queryMode);
        return props;
    }
}
