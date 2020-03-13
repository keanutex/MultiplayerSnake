let Snakes = [];
let apple;
let ClientId = "1667030a-b01a-4967-b1b3-b4be2b9012bd";
let message = "";
let UserSnake;
let UserSnakeSegments = []

const user = JSON.parse(window.localStorage.getItem("user"));
// Information needed/wanted from server :
// 1) All current snakes - color, positions travling to(mouseX, mouseY), score/size, ClientId

function setup(){

    //Set size from window of user.
    createCanvas(1000, 1000);

    head = new Segment(50, 50, true, user.color)

    UserSnakeSegments.push(head)

    UserSnake = new Snake("1667030a-b01a-4967-b1b3-b4be2b9012bd", 50, 50, false, user.color, 0, UserSnakeSegments);


    var socket = new SockJS('/server');

    stompClient = Stomp.over(socket);

    stompClient.connect({}, function(frame) {
        
        console.log('Connected: ' + frame);

        stompClient.subscribe('/sync/players', function(message) {

            players = JSON.parse(message.body)

            for (let i = 0; i < players.length; i++){
                Snakes[i] = new Snake(players[i].clientId, players[i].fposition.x, players[i].fposition.y, false, players[i].color, players[i].score, UserSnakeSegments);
            }

        });

        stompClient.subscribe('/sync/player', function(message) {
            message = JSON.parse(message.body);
        });

        stompClient.subscribe('/sync/join', function(message) {
        
            if (ClientId == "1667030a-b01a-4967-b1b3-b4be2b9012bd") 
            {


                ClientId = JSON.parse(message.body);
                UserSnake.clientId = JSON.parse(message.body);
                Snakes.push(UserSnake);

                
            }
            else 
            {
                return;
            }

        });

    });

    //sendMessage();

    //Snakes[1] = new Snake(width/2, height/2, true, 'red');
    //Snakes[2] = new Snake(width/2, height/2, true, 'yellow');
    //Snakes[1] = new Snake(width/2, height/2, true, 'purple');

    //Parse color from Thabiso's landing page
    //let colors = ['crimson','mediumvioletred','steelblue','lightgreen', 'blue'];


    //Get all player snakes's from server through Socket
    //Something like loop through json response ---@1

    apple = new Apple();

    leaderboard = new Leaderboard()

    // ---@1
    // for (let i = 1; i < 2; i++){
    //     Snakes[i] = new Snake(random(width), random(height), true, getColor(colors));
    // }
}

function getColor(colors){
    return colors[Math.floor(Math.random()*colors.length)]
}

function draw() {
    background(51);

    for (let i = 0; i < Snakes.length; i++){
         Snakes[i].run();
    }

    apple.render();

    leaderboard.render();

}

class Leaderboard{

    render(){
        Snakes.sort(function(a, b) {
            return a.score - b.score;
        })

        for (let i = Snakes.length - 1; i >= 0; i--){
            textSize(32);
            fill(Snakes[i].segments[0].color);
            text(Snakes[i].score, 50, (i+1)*50);

        }
    }
}

class Apple{
    constructor(){
        this.position = createVector(random(width) ,random(height));
        this.r =10.0;
    }

    render(){
        fill('green');
        ellipse(this.position.x, this.position.y, this.r, this.r);
    }
}

class Snake {

    emptyList = [];


    constructor(clientId, x=50 , y=50, bot=false, color, score, segments){

        this.segments = segments;
        this.clientId = clientId;
        this.position = createVector(x, y);
        this.score = score;
        this.bot = bot;
        this.color = color;
        
    }

    byApple(){
        let xcheck = (this.segments[0].position.x < (apple.position.x + apple.r + this.segments[0].r/10)) && (this.segments[0].position.x > (apple.position.x - apple.r - this.segments[0].r/10));
        let ycheck = (this.segments[0].position.y < (apple.position.y + apple.r + this.segments[0].r/10)) && (this.segments[0].position.y > (apple.position.y - apple.r - this.segments[0].r/10));

        if(xcheck && ycheck){
            apple = new Apple();

            
            this.addSegment();
            this.score++;

            Server(this, "/app/update");
        }
    }

    addSegment(){
        console.log(this.score);
        //this.segments.push(new Segment(this.segments[this.score].position.x - (this.score*0.005), this.segments[this.score].position.y -(this.score*0.005),false,this.segments[this.score].color))

        this.segments[this.score+1] = new Segment(this.segments[this.score].position.x - (this.score*0.005), this.segments[this.score].position.y -(this.score*0.005),false,this.segments[this.score].color)
    }

    run(){

        // let x =0;
        // let y =0;

        if(this.clientId == UserSnake.clientId){

            this.x = mouseX;
            this.y = mouseY;

            Server(this, "/app/update");

            this.segments[0].moveTo(this.x,this.y);
            this.segments[0].render()
    
            
    
            for(let i = 1; i < this.score; i++){
                this.segments[i].moveTo(this.segments[i-1].position.x - (i*0.005), this.segments[i-1].position.y - (i*0.005));
                this.segments[i].render()
            }

            this.byApple();
    
            //call /update with new UserSanke Obj
        }else{

            //get client position via selectPlayerById

            Server(this, "/app/player");

            this.x = message.x;
            this.y = message.y;
        }

        console.log(this.segments)
        console.log(this.segments[0])


       
        // if(this.Segments.length >= 2){


            
        //     for(let i = 1; i< this.Segments.length - 1; i++){
        //         this.Segments[i].moveTo(this.Segments[i-1].position.x - (i*0.005), this.Segments[i-1].position.y -(i*0.005));
        //         this.Segments[i].render()
        //     }

        // }

    }
}

class Segment{

    constructor(x, y, head=false, color){
        this.velocity = createVector(0 , 0);
        this.acceleration = createVector(0, 0)
        this.position = createVector(x, y)
        this.r =75.0;
        this.isHead = head;
        this.color = color;
    }

    render(){
        fill(this.color);
        stroke(51);
        ellipse(this.position.x, this.position.y, this.r, this.r);

    }

    moveTo(x ,y){

        this.target = createVector(x, y);
        this.target.sub(this.position);


        if(this.isHead){
            this.target.setMag(0.5);
        }
        else{
            this.target.setMag(3);
        }


        this.acceleration = this.target;
        this.velocity.add(this.acceleration);
        this.position.add(this.velocity);
        this.velocity.limit(5);
    }

}

function Server(snake, queue){
    stompClient.send(queue, {}, 
    JSON.stringify({'clientId':snake.clientId, 'color':snake.color, 'fposition':{'x':snake.x, 'y':snake.y}, 'score':snake.score}));
}

function connect() {

    console.log("Finding Players on server...")
    stompClient.send("/app/players", {});

    console.log("Joining server...")
    stompClient.send("/app/join", {},
    JSON.stringify({'clientId':UserSnake.clientId, 'color':UserSnake.color, 'fposition':{'x':UserSnake.x, 'y':UserSnake.y}, 'score':UserSnake.score}));
}

