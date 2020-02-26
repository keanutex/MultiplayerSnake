var stompClient = null;
let ip;

const CANVAS_BORDER_COLOUR = 'black';
const CANVAS_BACKGROUND_COLOUR = "white";
const SNAKE_COLOUR = 'lightgreen';
const SNAKE_BORDER_COLOUR = 'darkgreen';
// Horizontal velocity
let dx = 10;
// Vertical velocity
let dy = 0;
// Get the canvas element
let ctx;

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
    var gameCanvas = document.getElementById("gameCanvas");
// Return a two dimensional drawing context
    ctx = gameCanvas.getContext("2d");
//  Select the colour to fill the canvas
    ctx.fillStyle = CANVAS_BACKGROUND_COLOUR;
//  Select the colour for the border of the canvas
    ctx.strokestyle = CANVAS_BORDER_COLOUR;
// Draw a "filled" rectangle to cover the entire canvas
    ctx.fillRect(0, 0, gameCanvas.width, gameCanvas.height);
// Draw a "border" around the entire canvas
    ctx.strokeRect(0, 0, gameCanvas.width, gameCanvas.height);

    var socket = new SockJS('/gs-guide-websocket');
    stompClient = Stomp.over(socket);
    stompClient.debug = null; //uncomment this to get rid of debugging from stomp
    stompClient.connect({}, function (frame) {
        setConnected(true);
        console.log('Connected: ' + frame);

        console.log("functions here");
        getSnakeDetails();
        moveSnake();


        stompClient.subscribe('/snake/snakeDetails', (status) =>{
            const jsonReturn = JSON.parse(status.body);//run every time server is sent a message on this channel
            ctx.fillStyle = CANVAS_BACKGROUND_COLOUR;
            ctx.clearRect(0, 0, gameCanvas.width, gameCanvas.height);
            ctx.fillRect(0, 0, gameCanvas.width, gameCanvas.height);
            ctx.strokeRect(0, 0, gameCanvas.width, gameCanvas.height);
            for(let i = 0; i < jsonReturn.length; i ++){
                for(let j = 0; j < jsonReturn[i].snakeSegments.length; j++){
                    displaySnakes(jsonReturn[i].snakeSegments[j].x,jsonReturn[i].snakeSegments[j].y);
                }
            }
        });
        stompClient.subscribe("/snake/newPlayer", (status) => {
            console.log("SNAKE BOI ADDDED");
        });
        stompClient.subscribe("/snake/moveSnake", (status) => {
            console.log("SNAKES MOVED");
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

function moveSnake(){
    stompClient.send("/app/moveSnakes");
}

let interval = setInterval(changeDirection, 5);

function changeDirection() {
    document.onkeydown = function (event) {
        let keyCode, changeD = "Error";
        let sendKeyCode = false;

        if (event) {
            keyCode = event.keyCode;
        } else {
            keyCode = window.event.keyCode;
        }

        switch (keyCode) {
            case 65:
            case 37:
                changeD = "left";
                sendKeyCode = true;
                console.log("LEFT");
                break;
            case 87:
            case 38:
                changeD = "up";
                sendKeyCode = true;
                console.log("UP");
                break;
            case 68:
            case 39:
                changeD = "right";
                sendKeyCode = true;
                console.log("RIGHT");
                break;
            case 83:
            case 40:
                changeD = "down";
                sendKeyCode = true;
                console.log("DOWN");
                break;
            default:
                console.log("UNRECOGNISED KEYCODE: " + keyCode);
                break;
        }
        if(sendKeyCode){
            stompClient.send("/app/changeDirection", {}, changeD); //needs to send through the direction as a field (make a JSON/class for multiple fields)
            //stompClient.send("/app/changeDirection?changeD=" + changeD);

        }
    }
}

function getSnakeDetails() {
   stompClient.send("/app/snakeDetails");
    setTimeout(getSnakeDetails, 500);
}

function displaySnakes(x,y){
    ctx.fillStyle = SNAKE_COLOUR;
    // Set the border colour of the snake part
    ctx.strokestyle = SNAKE_BORDER_COLOUR;
    // Draw a "filled" rectangle to represent the snake part at the coordinates
    // the part is located
    ctx.fillRect(x, y, 10, 10);
    // Draw a border around the snake part
    ctx.strokeRect(x, y, 10, 10);
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
    $( "#addPlayer" ).click(function() { addPlayer(); });
});




