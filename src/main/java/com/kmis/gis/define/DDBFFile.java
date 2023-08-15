package com.kmis.gis.define;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@EqualsAndHashCode(callSuper = true)
@Data
public class DDBFFile extends DFile{
    int _colSize;
    int _rowSize;
    List<DDBFHeader> _headers = new ArrayList<>();
    List<Map<String, String>> _data = new ArrayList<>();
}