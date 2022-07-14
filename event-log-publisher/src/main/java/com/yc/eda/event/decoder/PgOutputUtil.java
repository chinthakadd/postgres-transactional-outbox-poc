package com.yc.eda.event.decoder;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

public class PgOutputUtil {

    public static String readString(ByteBuffer buffer) {
        StringBuilder sb = new StringBuilder();
        byte b;
        while ((b = buffer.get()) != 0) {
            sb.append((char) b);
        }
        return sb.toString();
    }

    /**
     * TODO: Make this better.
     */
    public static List<String> readTupleData(ByteBuffer msg) {
        var nColumn = msg.getShort();
        List<String> columnData = new ArrayList<>();

        for (int n = 0; n < nColumn; n++) {
            var tupleContentType = new String(new byte[]{msg.get()}, 0, 1);

            var content = "NULL";

            if (tupleContentType.equals("t")) {
                var size = msg.getInt();
                byte[] text = new byte[size];

                for (int z = 0; z < size; z++) {
                    text[z] = msg.get();
                }
                content = new String(text, 0, size);
            }

            if (tupleContentType.equals("u")) {
                content = "TOASTED";
            }

            System.out.printf("Column %s, content type %s, content %s%n",
                    n, tupleContentType, content);
            columnData.add(content);
        }
        return columnData;
    }
}
