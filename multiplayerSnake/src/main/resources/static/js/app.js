"use strict";

//----------------------------------------------------------------------------
// Global context
const user = JSON.parse(window.localStorage.getItem("user"));
const canvas = document.querySelector(".canvas");
const ctx = canvas.getContext("2d");

// ----------------------------------------------------------------------------
// fuctions
const makeStompClient = socket => {
  return Stomp.over(socket);
};

const connect = stompClient =>
  new Promise((resolve, reject) => {
    stompClient.connect(
      {},
      () => {
        resolve(stompClient);
      },
      () => {
        reject(new Error("Could not connect"));
      }
    );
  });

const sendToStompClient = message => client => endpoint => {
  client.send(endpoint, {}, message);
  return client;
};

const sendToStompClient_empty = sendToStompClient();

const subscribe = stompClient => messageHandler => queue => {
  stompClient.subscribe(queue, messageHandler);
  return stompClient;
};

const addPlayer = user => stompClient => {
  return sendToStompClient(user.color)(stompClient)(
    "/app/newPlayer/" + user.username
  );
};

const startGame = stompClient => {
  return sendToStompClient_empty(stompClient)("/app/moveSnakes");
};

const getGameState = stompClient => {
  return sendToStompClient_empty(stompClient)("/app/snakeDetails");
};

const removeStompDebug = stompClient => {
  stompClient.debug = null;
  return stompClient;
};

const makeConnectedStompClient = socketRef => messageHandler => async queue => {
  return await connect(makeStompClient(new SockJS(socketRef)))
    .then(client => removeStompDebug(client))
    .then(client => addPlayer(user)(client))
    .then(client => startGame(client))
    .then(client => subscribe(client)(messageHandler(client))(queue))
    .then(client => getGameState(client))
    .catch(err => {
      console.error(err.message);
    });
};

const handleGameControlsPres = stompClient => controls => {
  document.addEventListener("keydown", e => {
    if (controls[e.key]) controls[e.key]();
  });
};

const socketMessageHandler = updateViewFunc => stompClient => message => {
  updateViewFunc(JSON.parse(message.body));
  stompClient.send("/app/snakeDetails");
};

const updateView = canvas => ctx => data => {
  const BACKGROUND_COLOR = "white";

  // reset background
  ctx.fillStyle = BACKGROUND_COLOR;
  ctx.clearRect(0, 0, canvas.width, canvas.height);
  ctx.fillRect(0, 0, canvas.width, canvas.height);

  // draw pickups
  ctx.lineWidth = 1;
  data.pickups.forEach(pickup => {
    ctx.fillStyle = pickup.colour;
    ctx.fillRect(pickup.x, pickup.y, 10, 10);
    ctx.strokestyle = "black";
    ctx.strokeRect(pickup.x, pickup.y, 10, 10);
  });

  // draw snake
  data.snakes.forEach(snake =>
    snake.snakeSegments.forEach(segment => {
      ctx.fillStyle = snake.playerColour;
      ctx.fillRect(segment.x, segment.y, 10, 10);
      ctx.strokestyle = "black";
      ctx.strokeRect(segment.x, segment.y, 10, 10);
    })
  );
};

const updateGameOnCanvas = updateView(canvas)(ctx);

// ----------------------------------------------------------------
/* GAME */
makeConnectedStompClient("/gs-guide-websocket")(
  socketMessageHandler(updateGameOnCanvas)
)("/snake/snakeDetails")
  .then(client =>
    handleGameControlsPres(client)({
      ArrowUp: () => {
        client.send("/app/" + user.username + "/changeDirection", {}, "up");
      },
      ArrowDown: () => {
        client.send("/app/" + user.username + "/changeDirection", {}, "down");
      },
      ArrowLeft: () => {
        client.send("/app/" + user.username + "/changeDirection", {}, "left");
      },
      ArrowRight: () => {
        client.send("/app/" + user.username + "/changeDirection", {}, "right");
      }
    })
  )
  .catch(err => console.error(err.message));
