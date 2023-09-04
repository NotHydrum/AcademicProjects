document.getElementById("square").addEventListener("click", newpage);
document.getElementsByClassName("speed")[0].addEventListener("click", newpage3);
document.getElementsByClassName("radio")[0].addEventListener("click", newpage4);
document.getElementsByClassName("maps")[0].addEventListener("click", newpage5);
const checkbox = document.getElementById('checkbox');

setInterval(displayTime, 1000);

function displayTime() {

    const timeNow = new Date();

    let hoursOfDay = timeNow.getHours();
    let minutes = timeNow.getMinutes();
    let seconds = timeNow.getSeconds();

    if (hoursOfDay > 23) {
        hoursOfDay = 0;
    }

    hoursOfDay = hoursOfDay < 10 ? "0" + hoursOfDay : hoursOfDay;
    minutes = minutes < 10 ? "0" + minutes : minutes;
    seconds = seconds < 10 ? "0" + seconds : seconds;

    let time = hoursOfDay + ":" + minutes + ":" + seconds;

    document.getElementById('Clock').innerHTML = time;

}

displayTime();

function newpage() {
    window.location.replace("pressure.html");
}

function newpage2() {
     window.location.replace("warning.html");
}

function newpage3() {
     window.location.replace("speeds.html");
}

function newpage4() {
     window.location.replace("radio.html");
}

function newpage5() {
     window.location.replace("maps.html");
}

checkbox.addEventListener('change', ()=>{
    document.body.classList.toggle('dark');
    let darkModeCookie = getCookie("darkMode");
    if (darkModeCookie === "true") {
        document.cookie = "darkMode=false; max-age=86400";
    }
    else {
        document.cookie = "darkMode=true; max-age=86400";
    }
})

setInterval(sleepy, 60000);

function sleepy() {
    newpage2();
}
