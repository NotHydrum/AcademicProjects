document.getElementById("add-new").getElementsByTagName("input")[1].addEventListener("click", addTask);
document.getElementById("remove-all").getElementsByTagName("input")[0].addEventListener("click", removeFinishedTasks);

function addTask() {
    var li = document.createElement("li");
    var label = document.createElement("label");
    var checkBox = document.createElement("input");
    checkBox.type = "checkbox";
    checkBox.checked = false;
    var text = document.createTextNode(" " + document.getElementById("add-new").getElementsByTagName("input")[0].value);
    label.appendChild(checkBox);
    label.appendChild(text);
    li.appendChild(label);
    var list = document.getElementById("list");
    list.appendChild(li);
}

function removeFinishedTasks() {
    var tasks = document.getElementById("list").getElementsByTagName("li");
    for (let i = 0; i < tasks.length; i++) {
        checkBox = tasks[i].getElementsByTagName("input")[0];
        if (checkBox.checked) {
            tasks[i].remove();
            i--;
        }
    }
}
