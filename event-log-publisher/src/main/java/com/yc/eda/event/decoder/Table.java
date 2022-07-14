package com.yc.eda.event.decoder;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Table {

    String namespace;
    String name;
    List<Column> columns;

    public Table(String namespace, String name) {
        this.namespace = namespace;
        this.name = name;
    }
}