function loadPageInContentDiv(url) {
    $("#content").load(window.location.origin + "/" + url);
}

function loadUserInformation(username, callback) {

    const http = new XMLHttpRequest();
    http.open("GET", "/api/user/profile/" + username, true);
    http.setRequestHeader("Content-type", "application/json");
    http.onload = function () {
        let data = JSON.parse(this.responseText);
        if (callback) callback(data);
    };
    http.send();

}

function loadCourseList(callback) {
    const http = new XMLHttpRequest();
    http.open("GET", "/api/course/getCourseList");
    http.onload = function () {
        if (callback) callback(JSON.parse(http.responseText));
    }
    http.send();
}

function loadBranchList(courseId, callback) {
    const http = new XMLHttpRequest();
    http.open("GET", "/api/course/getBranchList/" + courseId);
    http.onload = function () {
        if (callback) callback(JSON.parse(http.responseText));
    }
    http.send();
}

function loadSessionList( callback) {
    const http = new XMLHttpRequest();
    http.open("GET", "/api/course/session");
    http.onload = function () {
        if (callback) callback(JSON.parse(http.responseText));
    }
    http.send();
}