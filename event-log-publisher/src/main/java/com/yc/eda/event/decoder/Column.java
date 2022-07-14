package com.yc.eda.event.decoder;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.postgresql.core.Oid;

@Data
@AllArgsConstructor
public class Column {

    private final int id;
    private final boolean isKey;
    private final String name;
    private final int type;

    @Override
    public String toString() {
        return "["
                + "Name:"
                + " "
                + this.name
                + ", "
                + "Key:"
                + " "
                + this.isKey
                + ", "
                + "Type:"
                + " "
                + this.type
                + " "
                + "("
                + Oid.toString(type)
                + ")"
                + "]";
    }

}