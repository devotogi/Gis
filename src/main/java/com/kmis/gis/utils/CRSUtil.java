package com.kmis.gis.utils;

import org.locationtech.jts.geom.Coordinate;
import org.locationtech.proj4j.BasicCoordinateTransform;
import org.locationtech.proj4j.CRSFactory;
import org.locationtech.proj4j.CoordinateReferenceSystem;
import org.locationtech.proj4j.ProjCoordinate;
import org.opengis.referencing.FactoryException;

import java.util.List;

public class CRSUtil {

    CRSFactory _crsFactory = new CRSFactory();
    CoordinateReferenceSystem _srcCRS;
    CoordinateReferenceSystem _tgtCRS;
    BasicCoordinateTransform _transformer;
    public CRSUtil(String srcCrs, String destCrs) throws FactoryException {
        if (srcCrs == null) srcCrs = "EPSG:5186";
        if (destCrs == null) destCrs = "EPSG:4326";

        _srcCRS = _crsFactory.createFromName(srcCrs);
        _tgtCRS = _crsFactory.createFromName(destCrs);

        _transformer = new BasicCoordinateTransform(_srcCRS, _tgtCRS);
    }

    public ProjCoordinate transPoint(Coordinate point) {
        ProjCoordinate beforeCoord = new ProjCoordinate(point.x, point.y);
        ProjCoordinate afterCoord = new ProjCoordinate();

        return _transformer.transform(beforeCoord, afterCoord);
    }
}
