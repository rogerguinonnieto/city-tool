const BASE_URL = "http://localhost:8080/citytool/api";

// --- Initialization ---

// Set up form submission listener once the DOM is loaded
document.addEventListener("DOMContentLoaded", () => {
    const form = document.getElementById("subscribeForm");
    if (form) {
        form.addEventListener("submit", function(event) {
            event.preventDefault(); // Prevent page reload
            if (this.checkValidity()) {
                subscribeClient();
            } else {
                this.reportValidity(); // Show browser validation bubbles
            }
        });
    }
});

// --- UI Helpers ---

/**
 * Handles clicking a station card to add its ID to the input field
 */
function selectStation(id) {
    const input = document.getElementById("stationsIds");
    let currentVal = input.value.trim();
    
    // Split values and remove whitespace
    let ids = currentVal ? currentVal.split(',').map(s => s.trim()) : [];
    
    // Add if not already present
    if (!ids.includes(id.toString())) {
        ids.push(id);
        input.value = ids.join(', ');
        
        // Visual feedback
        input.style.borderColor = "var(--success)";
        setTimeout(() => { input.style.borderColor = "var(--border)"; }, 500);
    }
}

/**
 * Creates a grid of station cards
 */
function createStationList(stations) {
    if (!stations || stations.length === 0) return "<p>No stations found.</p>";

    let html = '<div class="station-list-container">';
    stations.forEach(s => {
        const id = s.station_id || "N/A";
        const name = s.name || "Unknown Station";
        const lat = s.lat || 0;
        const lon = s.lon || 0;
        const mapsLink = `https://www.google.com/maps/place/${lat},${lon}`;
        const bikes = s.num_bikes_available ?? 0;
        const docks = s.num_docks_available ?? 0;
        const status = s.status || "UNKNOWN";
        const isCharging = s.is_charging_station ? '⚡' : '';

        html += `
            <div class="station-card" onclick="selectStation('${id}')">
                <div class="station-id">STATION #${id}</div>
                <a href="${mapsLink}" target="_blank" rel="noopener noreferrer" class="station-name">${name}</a>
                <div class="station-info">
                    <div class="info-row">
                        <span class="label">Bikes:</span>
                        <span class="value">${bikes}</span>
                    </div>
                    <div class="info-row">
                        <span class="label">Docks:</span>
                        <span class="value">${docks}</span>
                    </div>
                </div>
                <div class="station-status ${status === 'IN_SERVICE' ? 'online' : 'offline'}">
                    ${status.replace('_', ' ')} ${isCharging}
                </div>
            </div>`;
    });
    html += '</div>';
    return html;
}

/**
 * Creates a grid of client cards
 */
function createClientList(clients) {
    if (!clients || clients.length === 0) return "<p>No registered clients.</p>";
    if (!Array.isArray(clients)) clients = [clients];

    let html = '<div class="station-list-container">';
    clients.forEach(c => {
        const phone = c.phoneNumber || "Unknown";
        const stations = Array.isArray(c.stationIds) ? c.stationIds.join(", ") : (c.stationIds || "None");
        
        html += `
            <div class="station-card client-card">
                <div class="station-id">CLIENT: ${phone}</div>
                <div class="station-info">
                    <div class="info-row"><span class="label">Subscriptions:</span></div>
                    <div class="value" style="font-size: 11px; margin-top: 4px; color: var(--accent);">
                        [ ${stations} ]
                    </div>
                </div>
                <div class="station-status online" style="font-size: 8px; margin-top: 10px;">
                    ACTIVE SUBSCRIBER
                </div>
            </div>`;
    });
    html += '</div>';
    return html;
}

// --- API Calls ---

function getStations() {
    var xmlhttp = new XMLHttpRequest();
    xmlhttp.onreadystatechange = function() {
        if (xmlhttp.readyState == 4) {
            if(xmlhttp.status == 200) {
                const response = JSON.parse(xmlhttp.responseText);
                // Drill down to data.stations per your API structure
                const stationsArray = (response.data && response.data.stations) ? response.data.stations : [];
                document.getElementById("result").innerHTML = createStationList(stationsArray);
            } else {
                document.getElementById("result").innerHTML = `<p class="error">Error fetching stations. Status: ${xmlhttp.status}</p>`;
            }
        }
    };
    xmlhttp.open("GET", BASE_URL + "/stations", true);
    xmlhttp.send();
}

async function subscribeClient() {
    const phoneInput = document.getElementById("phone");
    const phoneValue = parseInt(phoneInput.value);

    phoneInput.style.borderColor = "var(--border)";
    // 1. Pre-check: Fetch existing clients
    try {
        const response = await fetch(BASE_URL + "/clients");
        const existingClients = await response.json();
        
        // 2. Look for the phone number
        const alreadyExists = existingClients.some(c => c.phoneNumber === phoneValue);
        
        if (alreadyExists) {
            document.getElementById("result").innerHTML = 
                `<p class="error"><strong>Wait!</strong> Phone number ${phoneValue} is already subscribed.</p>`;
            phoneInput.style.borderColor = "var(--error)";
            return; // Stop the function here
        }
    } catch (err) {
        console.warn("Could not verify existing clients, proceeding anyway...");
    }

    // 3. If it doesn't exist, proceed with the original POST request
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

    const phoneStr = document.getElementById("phone").value;
    const stationsStr = document.getElementById("stationsIds").value;
    const token = document.getElementById("telegramToken").value;
    const chat = document.getElementById("chatId").value;

    const stationsArray = stationsStr.replace(/\s/g,"").split(",").map(Number);

    const params = {
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
                document.getElementById("result").innerHTML = createClientList(data);
            } else {
                document.getElementById("result").innerHTML = `<p class="error">Error fetching clients. Status: ${xmlhttp.status}</p>`;
            }
        }
    };
    xmlhttp.open("GET", BASE_URL + "/clients", true);
    xmlhttp.send();
}