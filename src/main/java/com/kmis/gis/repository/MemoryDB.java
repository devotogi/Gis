package com.kmis.gis.repository;

import com.kmis.gis.define.DFile;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class MemoryDB {
    public Map<String, DFile> SHPDB = new HashMap<>();


}
