var GData = null;

async function search(index,filename) {
    GData = null;

    if (featureSource != null && featureLayer != null) {
        featureSource.removeFeature(featureLayer);
        featureSource.clear();
    }

    WrapMaskLayer();

    const response = await PostJSON("/map_load",{
        filenm : filename,
        epsg : $("#epsg").val()
    });

    if (response.result === true) {

        GData = response;

        $("#filename").text(filename);
        InitGrid();
        initGlobalVariable();
        dispatch(response.data);
        drawGrid(response.data2);
    }
    else {
        alert("ERROR");
    }

    for (let i = 1; i <= 10; i++) {
        try {
            $("#btn" + i).removeClass("myselected");
        }
        catch (e){

        }
    }

    $("#btn" + index).addClass("myselected");

    UnWrapMaskLayer();
}


function initGlobalVariable() {
    featureSource = new ol.source.Vector({});

    featureLayer = new ol.layer.Vector({
        source: featureSource,
        style: new ol.style.Style({
            image: new ol.style.Circle({
                radius: 4,
                fill: new ol.style.Fill({color: 'blue'})
            }),
            stroke: new ol.style.Stroke({
                color: 'blue',
                width: 4
            }),
            fill: new ol.style.Fill({
                color: 'rgba(0, 0, 255, 0.1)'
            })
        })
    });
}

function dispatch(data) {
    switch (data._dshpFileHeader._shapeTypeNm) {
        case "Point":
            dispatchPointAddLayer(data);
            break;

        case "PolyGon":
            dispatchPolyGunAddLayer(data);
            break;

        case "PolyLine":
            dispatchPolyLineAddLayer(data);
            break;
    }
}

function dispatchPointAddLayer(data) {
    if (data && data._dPointList) {
        data._dPointList.forEach((point, index) => {
            var pointFeature = new ol.Feature({
                geometry: new ol.geom.Point(point)
            });
            pointFeature.setId(index);
            pointFeature.attribute = GData.data2._data[index];
            featureSource.addFeature(pointFeature);
        });
        map.addLayer( featureLayer );
    }
}

function dispatchPolyLineAddLayer(data) {
    if (data && data._dLineList) {
        data._dLineList.forEach((line, index) => {
            var multiPointFeature = new ol.Feature({
                geometry: new ol.geom.LineString(line,'XY')
            });
            multiPointFeature.setId(index);
            multiPointFeature.attribute = GData.data2._data[index];
            featureSource.addFeature(multiPointFeature);
        });
        map.addLayer( featureLayer );
    }
}

function dispatchPolyGunAddLayer(data) {
    if (data && data._dPolygonsList) {
        data._dPolygonsList.forEach((polygon,index) => {
            var polygonFeature = new ol.Feature({
                geometry: new ol.geom.MultiPolygon(polygon)
            });
            polygonFeature.setId(index);
            polygonFeature.attribute = GData.data2._data[index];
            featureSource.addFeature(polygonFeature, 'XY');
        });
        map.addLayer( featureLayer );
    }
}

function InitGrid() {
    if (dbfAllGrid != null)
    {
        dbfAllGrid.destroy();
        dbfAllGrid = null;
    }

    if (dbfDetailGrid != null)
    {
        dbfDetailGrid.destroy();
        dbfDetailGrid = null;
    }
}