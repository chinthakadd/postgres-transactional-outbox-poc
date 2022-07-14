package com.yc.eda.event.decoder;

import lombok.Getter;

import java.util.Arrays;

/**
 * Different Message Format Types of pg_output Plugin.
 * https://www.postgresql.org/docs/current/protocol-logicalrep-message-formats.html
 */
@Getter
public enum PgOutputMessageType {

    RELATION('R'),
    BEGIN('B'),
    COMMIT('C'),
    INSERT('I'),
    UPDATE('U'),
    DELETE('D'),
    TYPE('Y'),
    ORIGIN('O'),
    TRUNCATE('T'),
    LOGICAL_DECODING_MESSAGE('M');

    private char pgChar;

    PgOutputMessageType(char pgChar) {
        this.pgChar = pgChar;
    }

    public static PgOutputMessageType forType(char type) {
        return Arrays.stream(PgOutputMessageType.values())
                .filter(t -> t.pgChar == type).findFirst().orElseThrow(
                        () -> new RuntimeException("Unsupported Type: " + type)
                );
    }
}