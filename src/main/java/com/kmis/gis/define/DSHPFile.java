package com.kmis.gis.define;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;
@Data
public class DSHPFile extends DFile{
    private DSHPFileHeader _dshpFileHeader;
    private List<Double[]> _dPointList = new ArrayList<>();
    private List<List<Double[]>> _dLineList = new ArrayList<>();
    private List<List<List<List<Double[]>>>> _dPolygonsList = new ArrayList<>();
    public DSHPFile(DSHPFileHeader dshpFileHeader, List<Double[]> pointList, List<List<Double[]>> lineList, List<List<List<List<Double[]>>>> polygunList) {
        _dshpFileHeader = dshpFileHeader;
        _dPointList = pointList;
        _dLineList = lineList;
        _dPolygonsList = polygunList;
    }
}