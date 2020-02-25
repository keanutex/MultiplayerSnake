// 192.168.0.x87 +:8080/index.html
var canvas, ctx, playerNr = -1, maxPlayer = 0;
var pause = false, check; // oldDirection = "r";
let ip = "localhost", ipSecure = "", protocol = "";
let field;
// https wss

// load site and give own name
window.onload = function () {
    field = new Array(6600);

    while (check === '' || check == null) {
        check = prompt('Please set your name, and do not forget your colour ( "Name#HexCode" ) ', '');
    }
    check = check.replace("#", "xHashTagx");

    ip = window.location.origin;
    console.log("IP: "+ ip);
    /// ip = "http://10.62.2.194:8080";
    protocol = window.location.protocol;
    if (protocol === "https:") {
        ipSecure = "s";
    }
};




