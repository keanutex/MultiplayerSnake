var stompClient = null;
let ip;

window.onload = function () {
    ip = window.location.origin;
};

function setConnected(connected) {
    $("#connect").prop("disabled", connected);
    $("#disconnect").prop("disabled", !connected);
    if (connected) {
        $("#conversation").show();
    }
    else {
        $("#conversation").hide();
    }
    $("#greetings").html("");
}

function connect() {
    var socket = new SockJS('/gs-guide-websocket');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function (frame) {
        setConnected(true);
        console.log('Connected: ' + frame);
    });
}

function disconnect() {
    if (stompClient !== null) {
        stompClient.disconnect();
    }
    setConnected(false);
    console.log("Disconnected");
}

function getSnakeDetails() {
    fetch(ip + "/api/snake/snakeDetails", {
        method: 'GET',
        headers: {'Accept': 'application/json', 'Content-Type': 'application/json'}
    }).then(function (ev) {
        console.log(ev);
    }).catch((function (error) {
        console.log("Error: ", error);
    }));
}


function addPlayer(){
    stompClient.send("/app/newPlayer");
}

$(function () {
    $("form").on('submit', function (e) {
        e.preventDefault();
    });
    $( "#connect" ).click(function() { connect(); });
    $( "#disconnect" ).click(function() { disconnect(); });
    $( "#send" ).click(function() { getSnakeDetails(); });
    $( "#addPlayer" ).click(function() { addPlayer(); });
});




