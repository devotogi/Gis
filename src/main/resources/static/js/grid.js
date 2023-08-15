var dbfAllGrid = null;
var dbfDetailGrid = null;

function createGrid(target,data,columns,options) {
    if (target === "#dbfAllGrid") {
        dbfAllGrid = new Slick.Grid(target, data, columns, options);
    } else {
        dbfDetailGrid = new Slick.Grid(target, data, columns, options);
    }
}

function drawGrid(data) {
    var options = {
        enableColumnReorder: false
    };

    createGrid("#dbfAllGrid",data._data,data._headers,options);
    createGrid("#dbfDetailGrid",[],data._headers,options);
}

function drawGridDetail(data) {
    WrapMaskLayer();
    var options = {
        enableCellNavigation: true,
    };

    if (dbfDetailGrid != null) {
        dbfDetailGrid.destroy();
        dbfDetailGrid = null;
    }

    createGrid("#dbfDetailGrid",data,GData.data2._headers,options);
    UnWrapMaskLayer();
}

function addGridRow(grid, item) {
    var data = grid.getData();
    data.splice(data.length, 0, item);
    grid.invalidateRow(data.length);
    grid.updateRowCount();
    grid.render();
    grid.scrollRowIntoView(data.length-1)
}

function clearGridRow(grid) {
    try{
        var dataView = grid.getData();

        dataView.beginUpdate();
        dataView.setItems([]);
        dataView.endUpdate();

        grid.invalidate();
        grid.render();
    }catch(e){
        grid.setData([]);
    }
}