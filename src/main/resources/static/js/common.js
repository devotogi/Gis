window.addEventListener('DOMContentLoaded', event => {
    // Toggle the side navigation
    const sidebarToggle = document.body.querySelector('#sidebarToggle');
    if (sidebarToggle) {
        // Uncomment Below to persist sidebar toggle between refreshes
        // if (localStorage.getItem('sb|sidebar-toggle') === 'true') {
        //     document.body.classList.toggle('sb-sidenav-toggled');
        // }
        sidebarToggle.addEventListener('click', event => {
            event.preventDefault();
            document.body.classList.toggle('sb-sidenav-toggled');
            localStorage.setItem('sb|sidebar-toggle', document.body.classList.contains('sb-sidenav-toggled'));
        });
    }

});


async function PostJSON(url, data) {
    try {
        const response = await fetch(url, {
            method: "POST", // or 'PUT'
            headers: {
                "Content-Type": "application/json",
            },
            body: JSON.stringify(data),
        });

        const result = await response.json();
        console.log("Success:", result);
        return result;
    } catch (error) {
        console.error("Error:", error);
        return null;
    }
}

async function PostJSONString(url, data) {
    try {
        const response = await fetch(url, {
            method: "POST", // or 'PUT'
            headers: {
                "Content-Type": "application/json",
            },
            body: JSON.stringify(data),
        });

        const result = await response.text();
        console.log("Success:", result);
        return result;
    } catch (error) {
        console.error("Error:", error);
        return null;
    }
}

async function GetJSON(url) {
    try {
        const response = await fetch(url, {
            method: "GET", // or 'PUT'
            headers: {
                "Content-Type": "application/json",
            }
        });

        const result = await response.json();
        console.log("Success:", result);
        return result;
    } catch (error) {
        console.error("Error:", error);
        return null;
    }
}

function WrapMaskLayer(){
    //화면의 높이와 너비를 구한다.
    var maskHeight = $(document).height();
    var maskWidth = $(window).width();

    //마스크의 높이와 너비를 화면 것으로 만들어 전체 화면을 채운다.
    $('#maskLayer').css({'width':maskWidth,'height':maskHeight});
    $('#maskLayer').show();
    $('#LoadingBar').show();
}

function UnWrapMaskLayer(){
    $('#maskLayer').hide();
    $('#LoadingBar').hide();
}

function resizeLayout() {
    const windowOuterHeight = $(document).outerHeight()
    const windowOuterWidth = $(window).width()
    const toolbarHeight = $("#toolbar").outerHeight()

    const mapHeight = 650;
    const gridHeight = windowOuterHeight - mapHeight - toolbarHeight;

    $("#wrap").outerHeight(windowOuterHeight);
    $("#map").outerHeight(mapHeight);
    $("#gridBox").outerHeight(gridHeight);

    const leftWidth = windowOuterWidth / 2;
    const divisionWidth = 100;
    const rightWidth = windowOuterWidth - leftWidth - divisionWidth;
    $("#dbfAllGrid").outerWidth(leftWidth);
    $("#division").outerWidth(100);
    $("#dbfDetailGrid").outerWidth(rightWidth);
}