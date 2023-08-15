package com.kmis.gis.define;

import lombok.Data;

@Data
public class DDBFHeader {
    private String id;
    private String name;
    private String field;

    public DDBFHeader(String id, String name, String field) {
        this.id = id.toLowerCase();
        this.name = name.toLowerCase();
        this.field = field.toLowerCase();
    }
}
