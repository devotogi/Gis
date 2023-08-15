package com.kmis.gis.define;

import lombok.Data;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.channels.FileChannel;

@Data
public class DSHPFileHeader {
    private int _fileCode;
    private int _fileLength;
    private int _version;
    private int _shapeType;
    private double _minX;
    private double _minY;
    private double _minZ;
    private double _minM;
    private double _maxX;
    private double _maxY;
    private double _maxZ;
    private double _maxM;
    private String _shapeTypeNm;

    private ByteBuffer buf = ByteBuffer.allocate(100);

    public DSHPFileHeader(File file) throws IOException {
        FileInputStream fileInputStream = new FileInputStream(file);
        FileChannel fileChannel = fileInputStream.getChannel();
        fileChannel.read(buf);
        buf.flip();

        _fileCode = buf.getInt(0);
        _fileLength = buf.getInt(24);

        buf.order(ByteOrder.LITTLE_ENDIAN);
        _version = buf.getInt(28);
        _shapeType = buf.getInt(32);
        geometryType();
        _minX = buf.getDouble(36);
        _minY = buf.getDouble(44);
        _minZ = buf.getDouble(68);
        _minM = buf.getDouble(84);
        _maxX = buf.getDouble(52);
        _maxY = buf.getDouble(60);
        _maxZ = buf.getDouble(76);
        _maxM = buf.getDouble(92);
    }

    public String geometryType() {
        String type = switch (_shapeType) {
            case 0 -> "Null Shape";
            case 1 -> "Point";
            case 3 -> "PolyLine";
            case 5 -> "PolyGon";
            case 8 -> "MultiPoint";
            case 11 -> "PointZ";
            case 13 -> "PolyLineZ";
            case 15 -> "PolyGonZ";
            case 18 -> "MultiPointZ";
            case 21 -> "PointM";
            case 23 -> "PolyLineM";
            case 25 -> "PolyGonM";
            case 28 -> "MultiPointM";
            case 31 -> "MultiPatch";
            default -> "";
        };
        _shapeTypeNm = type;
        return type;
    }

}
