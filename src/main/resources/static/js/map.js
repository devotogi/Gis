var map = null;

var selectedCollection = new ol.Collection();
var snappableCollection = new ol.Collection();

var selectInteraction = new ol.interaction.Select({
    features: selectedCollection,
    multi: true,
});
var modifyInteraction = new ol.interaction.Modify({
    // features: selectedCollection,
    features: selectInteraction.getFeatures(),
});
var snapInteraction = new ol.interaction.Snap({
    features: snappableCollection,
});
var featureSource = null;
var featureLayer = null;

function mapInit() {
    // styles for the vector layers
    map = new ol.Map({
        layers: [new ol.layer.Tile({
            source: new ol.source.OSM()
        })
        ],
        target: 'map',
        view: new ol.View({
            center:[14134806.846373284, 37.49271738383228],
            zoom: 9,
            projection: 'EPSG:4326'
        }),
    });

    // map.on('click', function(evt) {
    //     let coordinate = evt.coordinate;
    // });

    map.addInteraction(selectInteraction);
    map.addInteraction(modifyInteraction);
    map.addInteraction(snapInteraction);

    /* Adding Selected Feature */
    selectedCollection.on('add', ({ element: feature }) => {
        // console.log('================ map.js - selectedFeatures collection - add CALLBACK ================');
        console.log('map.js - selectedFeatures collection - add: ',feature);
        WrapMaskLayer();
        addGridRow(dbfDetailGrid,feature.attribute);
        UnWrapMaskLayer();
        // if (feature.attribute !== undefined || feature.attribute !== null) {
        //     drawGridDetail([feature.attribute]);
        // }

        // console.log('map.js - selectedFeatures collection - add - feature.getId(): ',feature.getId());
        // console.log('map.js - selectedFeatures collection - add - feature.getKeys(): ',feature.getKeys());
        // console.log('map.js - selectedFeatures collection - add - feature.getProperties(): ',feature.getProperties());
        // console.log('map.js - selectedFeatures collection - add - feature.getGeometry(): ',feature.getGeometry());
        // console.log('map.js - selectedFeatures collection - add - feature.getGeometryName(): ',feature.getGeometryName());
        // console.log('map.js - selectedFeatures collection - add - feature.getRevision(): ',feature.getRevision());
    });

    /* Removing Selected Feature */
    selectedCollection.on('remove', ({ element: feature }) => {
        // console.log('================ map.js - selectedFeatures collection - remove CALLBACK ================');
        console.log('map.js - selectedFeatures collection - remove: ', feature);
        WrapMaskLayer();
        clearGridRow(dbfDetailGrid);
        UnWrapMaskLayer();
        // drawGridDetail([]);
        // console.log('map.js - selectedFeatures collection - remove - feature.getId(): ',feature.getId());
        // console.log('map.js - selectedFeatures collection - remove - feature.getKeys(): ',feature.getKeys());
        // console.log('map.js - selectedFeatures collection - remove - feature.getProperties(): ',feature.getProperties());
        // console.log('map.js - selectedFeatures collection - remove - feature.getGeometry(): ',feature.getGeometry());
        // console.log('map.js - selectedFeatures collection - remove - feature.getGeometryName(): ',feature.getGeometryName());
        // console.log('map.js - selectedFeatures collection - remove - feature.getRevision(): ',feature.getRevision());
    });
}