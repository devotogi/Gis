package com.kmis.gis.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kmis.gis.define.DDBFFile;
import com.kmis.gis.define.DSHPFile;
import com.kmis.gis.handler.DBFHandler;
import com.kmis.gis.handler.SHPHandler;
import com.kmis.gis.repository.MemoryDB;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class GISService {

    @Autowired
    SHPHandler _shpHandler;

    @Autowired
    DBFHandler _dbfHandler;

    @Autowired
    MemoryDB _memoryDB;

    public Map<String, Object> mapLoad(Map<String, Object> param) {
        Map<String, Object> ret = new HashMap<>();
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            String filenm = param.get("filenm").toString();
            String epsg = param.get("epsg").toString();

            DSHPFile dshpFile = (DSHPFile) _memoryDB.SHPDB.get(epsg+filenm + ".shp");
            DDBFFile ddbfFile = (DDBFFile) _memoryDB.SHPDB.get(epsg+filenm + ".dbf");

            if (dshpFile == null)
                dshpFile = _shpHandler.loadSHPFile(epsg,filenm + ".shp");

            if (ddbfFile == null)
                ddbfFile = _dbfHandler.loadDBFFile(epsg,filenm + ".dbf");

            ret.put("data",dshpFile);
            ret.put("data2",ddbfFile);
            ret.put("result", true);
        }
        catch (Exception e) {
            e.printStackTrace();
            ret.put("result", false);
        }

        return ret;
    }
}
