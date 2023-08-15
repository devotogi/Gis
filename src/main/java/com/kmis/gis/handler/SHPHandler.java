package com.kmis.gis.handler;

import com.kmis.gis.define.DSHPFile;
import com.kmis.gis.define.DSHPFileHeader;
import com.kmis.gis.repository.MemoryDB;
import com.kmis.gis.utils.CRSUtil;
import org.geotools.data.shapefile.files.ShpFiles;
import org.geotools.data.shapefile.shp.ShapefileReader;
import org.locationtech.jts.geom.*;
import org.locationtech.proj4j.ProjCoordinate;
import org.opengis.referencing.FactoryException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

@Component
public class SHPHandler {

    @Autowired
    MemoryDB memoryDB;

    private String _fileRootPath = "src/main/resources/maps/";

    public DSHPFile loadSHPFile(String epsg, String filename) throws IOException {
        DSHPFile ret = null;

        String filePath =  _fileRootPath + "/" + filename;
        File tmpShpFile = new File(filePath);
        DSHPFileHeader dshpFileHeader = null;

        try {
            dshpFileHeader = new DSHPFileHeader(tmpShpFile);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("shpFileHeader Parser Error");
            throw new RuntimeException(e);
        }

        System.out.println(dshpFileHeader.toString());

        switch (dshpFileHeader.geometryType()) {
            // 점
            case "Point":
                ret = _loadSHPFile_GeoType_Point_Handle(epsg,filename,filePath,dshpFileHeader);
                break;
            // 도형
            case "PolyGon":
                ret = _loadSHPFile_GeoType_PolyGon_Handle(epsg,filename,filePath,dshpFileHeader);
                break;
            // 선
            case "PolyLine":
                ret = _loadSHPFile_GeoType_PolyLine_Handle(epsg,filename,filePath,dshpFileHeader);
                break;
        }

        return ret;
    }

    private DSHPFile _loadSHPFile_GeoType_PolyLine_Handle(String epsg, String filename, String filePath, DSHPFileHeader dshpFileHeader) throws IOException {
        ShpFiles shpFile = null;
        try {
            shpFile = new ShpFiles(filePath);
        } catch (MalformedURLException e) {
            e.printStackTrace();
            System.out.println("_loadSHPFile_GeoTypePoint_Handle SHPFile Load Error");
            throw new RuntimeException(e);
        }

        GeometryFactory geometryFactory = new GeometryFactory();
        ShapefileReader shapefileReader = new ShapefileReader(shpFile, true, false, geometryFactory);
        CRSUtil crsUtil = null;
        try {
            crsUtil = new CRSUtil(epsg, null);
        } catch (FactoryException e) {
            throw new RuntimeException(e);
        }

        List<List<Double[]>> dLineList = new ArrayList<>();

        while (shapefileReader.hasNext()) {
            ShapefileReader.Record record = shapefileReader.nextRecord();
            MultiLineString lineString = (MultiLineString) record.shape();
            List<Double[]> line = new ArrayList<>();
            for (var p : lineString.getCoordinates()) {
                ProjCoordinate afterTransPoint = crsUtil.transPoint(p);
                Double[] tmp = new Double[2];
                tmp[0] = afterTransPoint.x;
                tmp[1] = afterTransPoint.y;
                line.add(tmp);
            }
            dLineList.add(line);
        }

        shapefileReader.close();
        shpFile.dispose();

        DSHPFile dshpFile = new DSHPFile(dshpFileHeader, null, dLineList,null);
        memoryDB.SHPDB.put(epsg + filename, dshpFile);

        return dshpFile;
    }

    private DSHPFile _loadSHPFile_GeoType_PolyGon_Handle(String epsg, String filename, String filePath, DSHPFileHeader dshpFileHeader) throws IOException {
        ShpFiles shpFile = null;
        try {
            shpFile = new ShpFiles(filePath);
        } catch (MalformedURLException e) {
            e.printStackTrace();
            System.out.println("_loadSHPFile_GeoTypePoint_Handle SHPFile Load Error");
            throw new RuntimeException(e);
        }

        GeometryFactory geometryFactory = new GeometryFactory();
        ShapefileReader shapefileReader = new ShapefileReader(shpFile, true, false, geometryFactory);
        CRSUtil crsUtil = null;
        try {
            crsUtil = new CRSUtil(epsg, null);
        } catch (FactoryException e) {
            throw new RuntimeException(e);
        }

        List<List<List<List<Double[]>>>> multiPolygonList = new ArrayList<>();

        while (shapefileReader.hasNext()) {
            ShapefileReader.Record record = shapefileReader.nextRecord();
            MultiPolygon multiPolygon = (MultiPolygon) record.shape();

            List<List<Double[]>> lineStringList = new ArrayList<>();

            for (int polygonN = 0; polygonN <multiPolygon.getNumGeometries(); polygonN++) {

                Polygon polygon = (Polygon) multiPolygon.getGeometryN(polygonN);
                LineString exteriorRing = polygon.getExteriorRing();

                List<Double[]> line = new ArrayList<>();

                for (var point : exteriorRing.getCoordinates()) {
                    ProjCoordinate afterTransPoint = crsUtil.transPoint(point);
                    Double[] tmp = new Double[2];
                    tmp[0] = afterTransPoint.x;
                    tmp[1] = afterTransPoint.y;
                    line.add(tmp);
                }

                lineStringList.add(line);
            }

            List<List<List<Double[]>>> polygonWrapper = new ArrayList<>();
            polygonWrapper.add(lineStringList);

            multiPolygonList.add(polygonWrapper);
        }

        shapefileReader.close();
        shpFile.dispose();

        DSHPFile dshpFile = new DSHPFile(dshpFileHeader, null, null,multiPolygonList);
        memoryDB.SHPDB.put(epsg + filename, dshpFile);

        return dshpFile;
    }

    private DSHPFile _loadSHPFile_GeoType_Point_Handle(String epsg, String filename, String filePath, DSHPFileHeader dshpFileHeader) throws IOException {
        ShpFiles shpFile = null;
        try {
            shpFile = new ShpFiles(filePath);
        } catch (MalformedURLException e) {
            e.printStackTrace();
            System.out.println("_loadSHPFile_GeoTypePoint_Handle SHPFile Load Error");
            throw new RuntimeException(e);
        }

        List<Double[]> dPointList = new ArrayList<>();

        GeometryFactory geometryFactory = new GeometryFactory();
        ShapefileReader shapefileReader = new ShapefileReader(shpFile, true, false, geometryFactory);
        CRSUtil crsUtil = null;
        try {
            crsUtil = new CRSUtil(epsg, null);
        } catch (FactoryException e) {
            throw new RuntimeException(e);
        }

        while (shapefileReader.hasNext()) {
            ShapefileReader.Record record = shapefileReader.nextRecord();
            Point point = (Point) record.shape();

            Coordinate coordinate = new Coordinate(point.getX(),point.getY());

            ProjCoordinate afterTransPoint = crsUtil.transPoint(coordinate);

            Double[] tmp = new Double[2];
            tmp[0] = afterTransPoint.x;
            tmp[1] = afterTransPoint.y;
            dPointList.add(tmp);
        }

        shapefileReader.close();
        shpFile.dispose();

        DSHPFile dshpFile = new DSHPFile(dshpFileHeader, dPointList, null, null);
        memoryDB.SHPDB.put(epsg + filename, dshpFile);

        return dshpFile;
    }
}
