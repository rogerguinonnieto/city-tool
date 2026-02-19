const BASE_URL = "http://localhost:8080/citytool/api";

function getStations() {
    var xmlhttp = new XMLHttpRequest();
    xmlhttp.onreadystatechange = function() {
        if (xmlhttp.readyState == 4) {
            if(xmlhttp.status == 200) {
                document.getElementById("result").innerHTML = "<pre>" + JSON.stringify(JSON.parse(xmlhttp.responseText), null, 2) + "</pre>";
            } else {
                document.getElementById("result").innerHTML = "Error fetching stations. Status: " + xmlhttp.status;
            }
        }
    };
    xmlhttp.open("GET", BASE_URL + "/stations", true);
    xmlhttp.send();
}

function subscribeClient() {
    var xmlhttp = new XMLHttpRequest();
    xmlhttp.onreadystatechange = function() {
        if (xmlhttp.readyState == 4) {
            if(xmlhttp.status == 200) {
                document.getElementById("result").innerHTML = "<strong>Success!</strong> Client subscribed to stations.";
            } else {
                document.getElementById("result").innerHTML = "Error subscribing client. Status: " + xmlhttp.status;
            }
        }
    };

    var phoneStr = document.getElementById("phone").value;
    var stationsStr = document.getElementById("stationsIds").value;
    var token = document.getElementById("telegramToken").value;
    var chat = document.getElementById("chatId").value;

    var stationsArray = stationsStr.replace(/\s/g,"").split(",").map(Number);

    var params = {
        "phoneNumber": parseInt(phoneStr),
        "stationIds": stationsArray,
        "botToken": token,
        "chatId": chat
    };

    xmlhttp.open("POST", BASE_URL + "/clients/subscribe", true);
    xmlhttp.setRequestHeader('Content-Type', 'application/json; charset=UTF-8');
    xmlhttp.send(JSON.stringify(params));
}

function getClients() {
    var xmlhttp = new XMLHttpRequest();
    xmlhttp.onreadystatechange = function() {
        if (xmlhttp.readyState == 4) {
            if(xmlhttp.status == 200) {
                document.getElementById("result").innerHTML = "<pre>" + JSON.stringify(JSON.parse(xmlhttp.responseText), null, 2) + "</pre>";
            } else {
                document.getElementById("result").innerHTML = "Error fetching clients. Status: " + xmlhttp.status;
            }
        }
    };
    xmlhttp.open("GET", BASE_URL + "/clients", true);
    xmlhttp.send();
}