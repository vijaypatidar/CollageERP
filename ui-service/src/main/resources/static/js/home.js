function loadPageInContentDiv(url) {
    $("#content").load(window.location.origin + "/" + url);
}

function checkForm(id) {
    $(id).addClass('was-validated')
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

function loadBranchListOptions(courseId, callback) {
    let msg = "<option value=''>Select branch</option>";
    if (callback)callback(msg);
    loadBranchList(courseId, function (branches) {
        let options = msg;
        branches.forEach(function (value) {
            options += "<option value='" + value.id + "'>" + value.name + "</option>"
        });
        if (callback) callback(options);
    });
}

function loadSessionList(callback) {
    const http = new XMLHttpRequest();
    http.open("GET", "/api/course/session");
    http.onload = function () {
        if (callback) callback(JSON.parse(http.responseText));
    }
    http.send();
}


function loadSessionListOptions(callback) {
    let msg = "<option value=''>Select session</option>";
    if (callback)callback(msg);
    loadSessionList(function (sessions) {
        let options = msg;
        sessions.forEach(function (value) {
            options += "<option value='" + value.id + "'>" + value.name + "</option>"
        });
        if (callback) callback(options);
    });
}

function loadSubjectList(courseId, callback) {
    const http = new XMLHttpRequest();
    http.open("GET", "/api/course/subject?courseId=" + courseId);
    http.onload = function () {
        if (callback) callback(JSON.parse(http.responseText));
    }
    http.send();
}

function loadSubjectListOptions(courseId, callback) {
    let msg = "<option value=''>Select subject</option>";
    if (callback)callback(msg);
    loadSubjectList(courseId, function (sessions) {
        let options = msg;
        sessions.forEach(function (value) {
            options += "<option value='" + value.id + "'>" + `${value.name}(${value.id})` + "</option>"
        });
        if (callback) callback(options);
    });
}

function defaultResponseHandler(data, status) {
    console.log(status)
    if (status === 403) {
        alert("Access denied")
    } else if (status === 503) {
        $("#successAlert").hide();
        $("#errorAlert").show();
        document.getElementById("errorMessage").innerText = data.message;
    } else {
        if (data.status) {
            $("#successAlert").show();
            $("#errorAlert").hide();
            document.getElementById("successMessage").innerText = data.message;
        } else {
            $("#successAlert").hide();
            $("#errorAlert").show();
            document.getElementById("errorMessage").innerText = data.message;
        }
    }
}