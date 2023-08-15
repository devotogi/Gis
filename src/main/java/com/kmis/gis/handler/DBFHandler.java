package com.kmis.gis.handler;

import com.kmis.gis.define.DDBFFile;
import com.kmis.gis.define.DDBFHeader;
import com.kmis.gis.repository.MemoryDB;
import org.geotools.data.shapefile.ShapefileDataStore;
import org.geotools.data.shapefile.dbf.DbaseFileHeader;
import org.geotools.data.shapefile.dbf.DbaseFileReader;
import org.geotools.data.shapefile.files.ShpFiles;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class DBFHandler {

    @Autowired
    MemoryDB memoryDB;

    private String _fileRootPath = "src/main/resources/maps/";

    public DDBFFile loadDBFFile(String epsg, String filename) throws IOException {

        String filePath =  _fileRootPath + "/" + filename;

        ShpFiles shpFile = new ShpFiles(filePath);

        URL url = new File(filePath).toURI().toURL();
        ShapefileDataStore ds = new ShapefileDataStore(url);
        SimpleFeatureCollection fc = ds.getFeatureSource(ds.getTypeNames()[0]).getFeatures();

        String encoding = ds.getCharset().toString();
        System.out.println("shp schema encoding : "+encoding);

        DbaseFileReader r = null;
        if (!filename.equals("BML_EMD_AS.dbf"))
            r = new DbaseFileReader(shpFile, false, Charset.forName("MS949"));
        else
            r = new DbaseFileReader(shpFile, false, Charset.forName("utf-8"));

        DbaseFileHeader header = r.getHeader();
        int col = header.getNumFields();
        int row = header.getNumRecords();

        int numFields = header.getNumFields();

        DDBFFile ddbfFile = new DDBFFile();
        ddbfFile.set_rowSize(col);
        ddbfFile.set_rowSize(row);

        for (int iField = 0; iField < numFields; iField++) {
            String fieldName = header.getFieldName(iField);
            ddbfFile.get_headers().add(new DDBFHeader(fieldName,fieldName,fieldName));
        }

        while (r.hasNext()) {
            Object[] values = r.readEntry();
            Map<String, String> tmpMap = new HashMap<>();
            for (int iField = 0; iField < numFields; iField++) {
                try {
                    String value = values[iField].toString();
                    String key = ddbfFile.get_headers().get(iField).getField();

                    tmpMap.put(key, value);
                }
                catch (Exception e) {
                    System.out.printf(e.toString());
                }
            }
            ddbfFile.get_data().add(tmpMap);
        }

        r.close();

        memoryDB.SHPDB.put(epsg+filename, ddbfFile);

        return ddbfFile;
    }
}
