let stompClient = null;

const CANVAS_BORDER_COLOUR = 'black';
const CANVAS_BACKGROUND_COLOUR = "white";
let SNAKE_COLOUR = 'lightgreen';
const SNAKE_BORDER_COLOUR = 'darkgreen';
// Get the canvas element
let ctx;

let playerId = "";


function setConnected(connected) {
    $("#connect").prop("disabled", connected);
    $("#disconnect").prop("disabled", !connected);
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


    ctx.strokeRect(0, 0, gameCanvas.width, gameCanvas.height);

    playerId = '_' + Math.random().toString(36).substr(2, 9);


    var socket = new SockJS('/gs-guide-websocket');
    stompClient = Stomp.over(socket);
    stompClient.debug = null; //uncomment this to get rid of debugging from stomp
    stompClient.connect({}, function (frame) {
        setConnected(true);
        console.log('Connected: ' + frame);
        addPlayer();
        getSnakeDetails();
        moveSnake();

        stompClient.subscribe('/snake/snakeDetails', (status) =>{
            let jsonReturn = JSON.parse(status.body);//run every time server is sent a message on this channel
            let snakesJSON = jsonReturn.snakes;
            let pickupsJSON = jsonReturn.pickups;
            let display = '';
            for(let k = 0; k<jsonReturn.gameMessages.length; k++ ){
                display += '\n ' + jsonReturn.gameMessages[k];
            }
            document.getElementById('textArea').value = display;
            ctx.fillStyle = CANVAS_BACKGROUND_COLOUR;
            ctx.clearRect(0, 0, gameCanvas.width, gameCanvas.height);
            ctx.fillRect(0, 0, gameCanvas.width, gameCanvas.height);
            ctx.lineWidth = 20;
            ctx.strokeRect(0, 0, gameCanvas.width, gameCanvas.height);
            ctx.lineWidth = 1;
            for(let j = 0; j < pickupsJSON.length; j++){
                    displayPickups(pickupsJSON[j].x, pickupsJSON[j].y, pickupsJSON[j].colour);
            }
            displaySnakes(snakesJSON);
        });
    });
}

function disconnect() {
    deletePlayer();
    if (stompClient !== null) {
        stompClient.disconnect();
    }
    setConnected(false);
    console.log("Disconnected");
}

function deletePlayer(){
    stompClient.send("/app/" + playerId + "/removeSnake");
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
                break;
            case 87:
            case 38:
                changeD = "up";
                sendKeyCode = true;
                break;
            case 68:
            case 39:
                changeD = "right";
                sendKeyCode = true;
                break;
            case 83:
            case 40:
                changeD = "down";
                sendKeyCode = true;
                break;
            default:
                console.log("UNRECOGNISED KEYCODE: " + keyCode);
                break;
        }
        if(sendKeyCode){
            stompClient.send("/app/" + playerId + "/changeDirection", {}, changeD); //needs to send through the direction as a field (make a JSON/class for multiple fields)
        }
    }
}

function getSnakeDetails() {
   stompClient.send("/app/snakeDetails");
    setTimeout(getSnakeDetails, 10);
}

function displayBodyPart(x,y, colour){
    ctx.fillStyle = colour;
    // Set the border colour of the snake part
    ctx.strokestyle = SNAKE_BORDER_COLOUR;
    // Draw a "filled" rectangle to represent the snake part at the coordinates
    // the part is located
    ctx.fillRect(x, y, 10, 10);
    // Draw a border around the snake part
    ctx.strokeRect(x, y, 10, 10);
}

function displaySnakes(snakesJSON){
    ctx.font = "30px Arial";
    for(let i = 0; i < snakesJSON.length; i ++) {
        ctx.strokeText(snakesJSON.name, snakesJSON[i].x, snakesJSON[i].y +10);
        for (let j = 0; j < snakesJSON[i].snakeSegments.length; j++) {
            displayBodyPart(snakesJSON[i].snakeSegments[j].x, snakesJSON[i].snakeSegments[j].y, snakesJSON[i].playerColour);
        }
    }
}

function displayPickups(x,y, colour){
    ctx.fillStyle = colour;
    // Set the border colour of the snake part
    ctx.strokestyle = SNAKE_BORDER_COLOUR;
    // Draw a "filled" rectangle to represent the snake part at the coordinates
    // the part is located
    ctx.fillRect(x, y, 10, 10);
    // Draw a border around the snake part
    ctx.strokeRect(x, y, 10, 10);
}


function addPlayer(){
    const info = {
        colour: setColour(),
        name: setName()
    }
    stompClient.send("/app/newPlayer/" + playerId, {}, JSON.stringify(info)); //TODO IMPORTANT. ALL INFO THAT SHOULD HAPPEN WHEN A NEW PLAYER JOIN SHOULD HAPPEN HERE. MODEL NEEDS TO BE MADE EVENTUALLY FOR THESE INPUTS
}

$(function () {
    $("form").on('submit', function (e) {
        e.preventDefault();
    });
    $( "#connect" ).click(function() { connect(); });
    $( "#disconnect" ).click(function() { disconnect(); });
});

function isColor(strColor){
    if(strColor === ""){
        return false;
    }
    var s = new Option().style;
    s.color = strColor;
    return s.color == strColor;
}

function setColour(){
    let color = document.getElementById("colorText").value;
    if(!isColor(color)) {
        const letters = '0123456789ABCDEF';
        color = '#';
        for (let i = 0; i < 6; i++) {
            color += letters[Math.floor(Math.random() * 16)];
        }
    }
    return color;
}

function setName(){
    names = ['noodles', 'William Snakespeare', 'Danger Noodle','Severus Snake', 'Nope Rope', 'Mr. Wiggles', 'Spaghetti' ,'Monty Python']
    const name =  document.getElementById('nameText').value
    if(name === '') {
        return get_random(names)
    }else {
        return name;
    }
}


get_random = function (list) {
    return list[Math.floor((Math.random()*list.length))];
}