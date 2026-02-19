const BASE_URL = "http://localhost:8080/citytool/api";

function createHtmlTableFromJson(jsonData) {
    if (!jsonData || jsonData.length === 0) {
        return "<p>No data available.</p>";
    }

    if (!Array.isArray(jsonData)) {
        jsonData = [jsonData];
    }

    const headers = Object.keys(jsonData[0]);

    let tableHtml = '<div class="table-container"><table class="pretty-table"><thead><tr>';
    
    // Build Headers
    headers.forEach(header => {
        const cleanHeader = header.charAt(0).toUpperCase() + header.slice(1);
        tableHtml += `<th>${cleanHeader}</th>`;
    });
    tableHtml += '</tr></thead><tbody>';

    // Build Rows
    jsonData.forEach(row => {
        tableHtml += '<tr>';
        headers.forEach(header => {
            let cellValue = row[header];
            
            if (typeof cellValue === 'object' && cellValue !== null) {
                cellValue = Array.isArray(cellValue) ? cellValue.join(', ') : JSON.stringify(cellValue);
            }
            
            tableHtml += `<td>${cellValue !== undefined ? cellValue : ''}</td>`;
        });
        tableHtml += '</tr>';
    });

    tableHtml += '</tbody></table></div>';
    return tableHtml;
}

// --- API Calls ---

function getStations() {
    var xmlhttp = new XMLHttpRequest();
    xmlhttp.onreadystatechange = function() {
        if (xmlhttp.readyState == 4) {
            if(xmlhttp.status == 200) {
                const data = JSON.parse(xmlhttp.responseText);
                document.getElementById("result").innerHTML = createHtmlTableFromJson(data);
            } else {
                document.getElementById("result").innerHTML = `<p class="error">Error fetching stations. Status: ${xmlhttp.status}</p>`;
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
                document.getElementById("result").innerHTML = "<p class='success'><strong>Success!</strong> Client subscribed to stations.</p>";
            } else {
                document.getElementById("result").innerHTML = `<p class="error">Error subscribing client. Status: ${xmlhttp.status}</p>`;
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
                const data = JSON.parse(xmlhttp.responseText);
                document.getElementById("result").innerHTML = createHtmlTableFromJson(data);
            } else {
                document.getElementById("result").innerHTML = `<p class="error">Error fetching clients. Status: ${xmlhttp.status}</p>`;
            }
        }
    };
    xmlhttp.open("GET", BASE_URL + "/clients", true);
    xmlhttp.send();
}