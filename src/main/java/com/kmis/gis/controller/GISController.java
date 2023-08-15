package com.kmis.gis.controller;

import com.kmis.gis.service.GISService;
import org.opengis.referencing.FactoryException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.Map;

@RestController
public class GISController {

    @Autowired
    GISService gisService;

    @PostMapping("/map_load")
    public Map<String, Object> mapLoad(@RequestBody Map<String, Object> param) throws IOException, FactoryException {
        return gisService.mapLoad(param);
    }
}
