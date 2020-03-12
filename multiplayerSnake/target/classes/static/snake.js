const user = JSON.parse(window.localStorage.getItem("user"));

var stompClient = null;

const CANVAS_BORDER_COLOUR = "black";
const CANVAS_BACKGROUND_COLOUR = "white";
let SNAKE_COLOUR = "lightgreen";
const SNAKE_BORDER_COLOUR = "darkgreen";
// Get the canvas element
let ctx;

function setConnected(connected) {
  $("#connect").prop("disabled", connected);
  $("#disconnect").prop("disabled", !connected);
}

document.querySelector("#newsnake").addEventListener("click", () => {
  addPlayer();
});

function connect() {
  const gameCanvas = document.getElementById("gameCanvas");
  // Return a two dimensional drawing context
  ctx = gameCanvas.getContext("2d");
  //  Select the colour to fill the canvas
  ctx.fillStyle = CANVAS_BACKGROUND_COLOUR;
  //  Select the colour for the border of the canvas
  ctx.strokestyle = CANVAS_BORDER_COLOUR;
  // Draw a "filled" rectangle to cover the entire canvas
  ctx.fillRect(0, 0, gameCanvas.width, gameCanvas.height);

  ctx.strokeRect(0, 0, gameCanvas.width, gameCanvas.height);

  const socket = new SockJS("/gs-guide-websocket");
  stompClient = Stomp.over(socket);
  stompClient.debug = null; //uncomment this to get rid of debugging from stomp
  stompClient.connect({}, function(frame) {
    setConnected(true);
    console.log("Connected: " + frame);
    addPlayer();
    stompClient.subscribe("/snake/snakeDetails", status => {
      let jsonReturn = JSON.parse(status.body); //run every time server is sent a message on this channel
      let snakesJSON = jsonReturn.snakes;
      let pickupsJSON = jsonReturn.pickups;
      let bulletsJSON = jsonReturn.bullets;
      let wallsJSON = jsonReturn.walls;
      ctx.fillStyle = CANVAS_BACKGROUND_COLOUR;
      ctx.clearRect(0, 0, gameCanvas.width, gameCanvas.height);
      ctx.fillRect(0, 0, gameCanvas.width, gameCanvas.height);
      ctx.lineWidth = 20;
      ctx.strokeRect(0, 0, gameCanvas.width, gameCanvas.height);
      ctx.lineWidth = 1;
      for (let j = 0; j < pickupsJSON.length; j++) {
        displayEntity(
          pickupsJSON[j].x,
          pickupsJSON[j].y,
          pickupsJSON[j].colour
        );
      }
      for (let i = 0; i < snakesJSON.length; i++) {
        for (let j = 0; j < snakesJSON[i].snakeSegments.length; j++) {
          displayEntity(
            snakesJSON[i].snakeSegments[j].x,
            snakesJSON[i].snakeSegments[j].y,
            snakesJSON[i].playerColour
          );
        }
      }
      for (let i = 0; i < bulletsJSON.length; i++) {
                displayEntity(
                  bulletsJSON[i].x,
                  bulletsJSON[i].y,
                  '#f0697b'
                );
            }
      for (let i = 0; i < wallsJSON.length; i++) {
                      displayWall(
                        wallsJSON[i].x,
                        wallsJSON[i].y,
                        wallsJSON[i].colour
                      );
                  }
    });
    stompClient.subscribe('/logging/loggingSquare',(status)=> {
      const body =JSON.parse(status.body);
      document.getElementById('loggingArea').innerHTML += '<p style=color:' + body.colour + '>' + body.message + '</p>';
    });
    stompClient.subscribe('/messaging/message', (status) => {
      const body =JSON.parse(status.body);
      console.log(body);
      document.getElementById("loggingArea").innerHTML += '<p style=color:' + body.colour + '>' + body.name + ':' + body.message + '</p>';
    });
    stompClient.subscribe("/leaderboard/updateLeaderboard", (status) => {
      
      let leaderboardJSON = JSON.parse(status.body); 
      let index = 0;
      let playerCount = leaderboardJSON.length;
      for (let i = 0; i < leaderboardJSON.length; i++) {      
        for (var key in leaderboardJSON[i]){
          updateLeaderboard(index, key, leaderboardJSON[i][key], playerCount);
          index ++;
        }
      }
   });

    moveSnake();
    getSnakeDetails();
  });
}

function updateLeaderboard(index, name, score, playerCount){
    let rows = document.getElementsByClassName("lbRow").length;
    while (rows != playerCount){
      if (playerCount == 0)
        break;
      else if (rows < playerCount)
        addToLeaderboard();
      else if (rows > playerCount)
        deleteFromLeaderboard(rows);

      rows = document.getElementsByClassName("lbRow").length;
    }
    let row = document.getElementsByClassName("lbRow")[index].getElementsByTagName("td");
    row[0].textContent = name;
    row[1].textContent = score;
}

function addToLeaderboard(){
  $("#leaderboard").append(
    "<tr class=\"lbRow\">" +
      "<td> test1</td>" +
      "<td> 5</td>" +
    "</tr>");
}

function deleteFromLeaderboard(rows){
  var table = document.getElementById("leaderboard");
  table.deleteRow(rows -1);
}



function disconnect() {
  deletePlayer();
  if (stompClient !== null) {
    stompClient.disconnect();
  }
  setConnected(false);
  console.log("Disconnected");
}

function deletePlayer() {
  stompClient.send("/app/" + user.playerId + "/removeSnake");
}

function moveSnake() {
  stompClient.send("/app/moveSnakes");
}

window.onbeforeunload = closingCode;
function closingCode() {
  disconnect();
  return null;
}

let interval = setInterval(keyBoardInput, 5);



function keyBoardInput() {
  document.onkeydown = function(event) {
    let keyCode,
      changeD = "Error";
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
      case 32:
      event.preventDefault();
         stompClient.send(
            "/app/" + user.playerId + "/shoot"
          );
      break;
      default:
        console.log("UNRECOGNISED KEYCODE: " + keyCode);
        break;
    }
    if (sendKeyCode) {
      stompClient.send(
        "/app/" + user.playerId + "/changeDirection",
        {},
        changeD
      ); //needs to send through the direction as a field (make a JSON/class for multiple fields)
    }
  };
}

function getSnakeDetails() {
  stompClient.send("/app/snakeDetails");
  setTimeout(getSnakeDetails, 5);
}

function displayEntity(x, y, colour) {
  ctx.fillStyle = colour;
  // Set the border colour of the snake part
  ctx.strokestyle = SNAKE_BORDER_COLOUR;
  // Draw a "filled" rectangle to represent the snake part at the coordinates
  // the part is located
  ctx.fillRect(x, y, 10, 10);
  // Draw a border around the snake part
  ctx.strokeRect(x, y, 10, 10);
}

function displayWall(x, y, colour){
  ctx.fillStyle = colour;
  // Draw a "filled" rectangle to represent the snake part at the coordinates
  // the part is located
  ctx.fillRect(x, y, 10, 10);
}

function displayPickups(x, y, colour) {
  ctx.fillStyle = colour;
  // Set the border colour of the snake part
  ctx.strokestyle = SNAKE_BORDER_COLOUR;
  // Draw a "filled" rectangle to represent the snake part at the coordinates
  // the part is located
  ctx.fillRect(x, y, 10, 10);
  // Draw a border around the snake part
  ctx.strokeRect(x, y, 10, 10);
}

function addPlayer() {
  stompClient.send("/app/newPlayer/" + user.playerId, {}, JSON.stringify({color: user.color, playerName: user.username})); //TODO IMPORTANT. ALL INFO THAT SHOULD HAPPEN WHEN A NEW PLAYER JOIN SHOULD HAPPEN HERE. MODEL NEEDS TO BE MADE EVENTUALLY FOR THESE INPUTS
}

$(function() {
  $("form").on("submit", function(e) {
    e.preventDefault();
  });
  $("#connect").click(function() {
    connect();
  });
  $("#disconnect").click(function() {
    disconnect();
  });
});

function isColor(strColor) {
  if (strColor === "") {
    return false;
  }
  var s = new Option().style;
  s.color = strColor;
  return s.color == strColor;
}

function setColour() {
  let color = document.getElementById("colorText").value;
  if (!isColor(color)) {
    const letters = "0123456789ABCDEF";
    color = "#";
    for (let i = 0; i < 6; i++) {
      color += letters[Math.floor(Math.random() * 16)];
    }
  }
  return color;
}

connect();
function sendMessage(){
  if(document.getElementById("message").value.length>0) {
      stompClient.send("/app/addMessage", {}, JSON.stringify({
            playerId: user.playerId,
            username: user.username,
            message: document.getElementById("message").value
          }
      ));
    document.getElementById("message").value = "";
  }
}







