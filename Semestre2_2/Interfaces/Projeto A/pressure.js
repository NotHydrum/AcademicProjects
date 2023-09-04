if (getCookie("darkMode") === "true") {
    document.getElementsByClassName("img")[0].src = "img/pressure.png"
}
else {
    document.getElementsByClassName("img")[0].src = "img/pressureD.png"
}

function getRandomArbitrary(min, max) {
    return Math.floor(Math.random() * (max - min) + min);
}

if (getCookie("tire1") === "") {
    let pressure = getRandomArbitrary(20, 41);
    if (pressure <= 29 || pressure >=36) {
        let string = `${pressure}`.fontcolor("red");
        document.getElementById("tire1").innerHTML = string + " PSI";
    }
    else {
        let string = `${pressure}`;
        document.getElementById("tire1").innerHTML = string + " PSI";
    }
    document.cookie = "tire1=" + `${pressure}` + "; max-age=86400";
}
else {
    let pressure = getCookie("tire1")
    if (pressure <= 29 || pressure >=36) {
        let string = `${pressure}`.fontcolor("red");
        document.getElementById("tire1").innerHTML = string + " PSI";
    }
    else {
        let string = `${pressure}`;
        document.getElementById("tire1").innerHTML = string + " PSI";
    }
}
if (getCookie("tire2") === "") {
    let pressure = getRandomArbitrary(20, 41);
    if (pressure <= 29 || pressure >=36) {
        let string = `${pressure}`.fontcolor("red");
        document.getElementById("tire2").innerHTML = string + " PSI";
    }
    else {
        let string = `${pressure}`;
        document.getElementById("tire2").innerHTML = string + " PSI";
    }
    document.cookie = "tire2=" + `${pressure}` + "; max-age=86400";
}
else {
    let pressure = getCookie("tire2")
    if (pressure <= 29 || pressure >=36) {
        let string = `${pressure}`.fontcolor("red");
        document.getElementById("tire2").innerHTML = string + " PSI";
    }
    else {
        let string = `${pressure}`;
        document.getElementById("tire2").innerHTML = string + " PSI";
    }
}
if (getCookie("tire3") === "") {
    let pressure = getRandomArbitrary(20, 41);
    if (pressure <= 29 || pressure >=36) {
        let string = `${pressure}`.fontcolor("red");
        document.getElementById("tire3").innerHTML = string + " PSI";
    }
    else {
        let string = `${pressure}`;
        document.getElementById("tire3").innerHTML = string + " PSI";
    }
    document.cookie = "tire3=" + `${pressure}` + "; max-age=86400";
}
else {
    let pressure = getCookie("tire3")
    if (pressure <= 29 || pressure >=36) {
        let string = `${pressure}`.fontcolor("red");
        document.getElementById("tire3").innerHTML = string + " PSI";
    }
    else {
        let string = `${pressure}`;
        document.getElementById("tire3").innerHTML = string + " PSI";
    }
}
if (getCookie("tire4") === "") {
    let pressure = getRandomArbitrary(20, 41);
    if (pressure <= 29 || pressure >=36) {
        let string = `${pressure}`.fontcolor("red");
        document.getElementById("tire4").innerHTML = string + " PSI";
    }
    else {
        let string = `${pressure}`;
        document.getElementById("tire4").innerHTML = string + " PSI";
    }
    document.cookie = "tire4=" + `${pressure}` + "; max-age=86400";
}
else {
    let pressure = getCookie("tire4")
    if (pressure <= 29 || pressure >=36) {
        let string = `${pressure}`.fontcolor("red");
        document.getElementById("tire4").innerHTML = string + " PSI";
    }
    else {
        let string = `${pressure}`;
        document.getElementById("tire4").innerHTML = string + " PSI";
    }
}
