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
        stompClient.subscribe('/snake/snakeDetails', (status) =>{
            console.log(status.body); //run every time server is sent a message on this channel
            //{}
        });
        stompClient.subscribe("/snake/newPlayer", (status) => {
            console.log("SNAKE BOI ADDDED");
        });
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
   stompClient.send("/app/snakeDetails");
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




