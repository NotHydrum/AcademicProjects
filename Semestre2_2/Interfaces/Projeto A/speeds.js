if (getCookie("darkMode") === "true") {
    document.getElementsByClassName("img")[0].src = "img/speeds1.png"
    document.getElementsByClassName("img")[1].src = "img/speeds2.png"
    document.getElementsByClassName("img")[2].src = "img/speeds3.png"
    document.getElementsByClassName("img")[3].src = "img/speeds4.png"
}
else {
    document.getElementsByClassName("img")[0].src = "img/speeds1D.png"
    document.getElementsByClassName("img")[1].src = "img/speeds2D.png"
    document.getElementsByClassName("img")[2].src = "img/speeds3D.png"
    document.getElementsByClassName("img")[3].src = "img/speeds4D.png"
}

if (getCookie("topLeft") === "") {
    document.cookie = "topLeft=50; max-age=86400";
}
if (getCookie("topRight") === "") {
    document.cookie = "topRight=90; max-age=86400";
}
if (getCookie("botLeft") === "") {
    document.cookie = "botLeft=110; max-age=86400";
}
if (getCookie("botRight") === "") {
    document.cookie = "botRight=120; max-age=86400";
}

document.getElementById("top-left-speed").innerHTML = getCookie("topLeft").concat(" Km/h");
document.getElementById("top-right-speed").innerHTML = getCookie("topRight").concat(" Km/h");
document.getElementById("bot-left-speed").innerHTML = getCookie("botLeft").concat(" Km/h");
document.getElementById("bot-right-speed").innerHTML = getCookie("botRight").concat(" Km/h");

function topLeft() {
    let speed = document.getElementById("top-left-speed").innerHTML.split(' ')[0];
    let new_speed = prompt("Please enter new speed:", speed);
    if (new_speed === null || new_speed === "") {
        new_speed = speed;
    }
    document.cookie = "topLeft=" + new_speed + "; max-age=86400";
    document.getElementById("top-left-speed").innerHTML = new_speed.concat(" Km/h");
}

function topRight() {
    let speed = document.getElementById("top-right-speed").innerHTML.split(' ')[0];
    let new_speed = prompt("Please enter new speed:", speed);
    if (new_speed === null || new_speed === "") {
        new_speed = speed;
    }
    document.cookie = "topRight=" + new_speed + "; max-age=86400";
    document.getElementById("top-right-speed").innerHTML = new_speed.concat(" Km/h");
}

function botLeft() {
    let speed = document.getElementById("bot-left-speed").innerHTML.split(' ')[0];
    let new_speed = prompt("Please enter new speed:", speed);
    if (new_speed === null || new_speed === "") {
        new_speed = speed;
    }
    document.cookie = "botLeft=" + new_speed + "; max-age=86400";
    document.getElementById("bot-left-speed").innerHTML = new_speed.concat(" Km/h");
}

function botRight() {
    let speed = document.getElementById("bot-right-speed").innerHTML.split(' ')[0];
    let new_speed = prompt("Please enter new speed:", speed);
    if (new_speed === null || new_speed === "") {
        new_speed = speed;
    }
    document.cookie = "botRight=" + new_speed + "; max-age=86400";
    document.getElementById("bot-right-speed").innerHTML = new_speed.concat(" Km/h");
}
