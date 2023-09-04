function load() {
    document.getElementById("option11").addEventListener("click", function () {
        selectOption(11)
    })
    document.getElementById("option12").addEventListener("click", function () {
        selectOption(12)
    })
    document.getElementById("option13").addEventListener("click", function () {
        selectOption(13)
    })
    document.getElementById("option21").addEventListener("click", function () {
        selectOption(21)
    })
    document.getElementById("option22").addEventListener("click", function () {
        selectOption(22)
    })
    document.getElementById("option23").addEventListener("click", function () {
        selectOption(23)
    })
    document.getElementById("option31").addEventListener("click", function () {
        selectOption(31)
    })
    document.getElementById("option32").addEventListener("click", function () {
        selectOption(32)
    })
    document.getElementById("option33").addEventListener("click", function () {
        selectOption(33)
    })

    function selectOption(option) {
        document.getElementById("selectedOption").innerHTML = " Opção" + option;
    }

    document.getElementById("addRow").addEventListener("click", addRow);

    function addRow() {
        let productName = document.getElementById("productName").value;
        let productCost = document.getElementById("productCost").value;
        let national = document.getElementById("o1").checked;
        let international = document.getElementById("o2").checked;
        let table = document.getElementsByTagName("table")[0]
        if (productName != null && productName !== "" && productCost != null && productCost !== ""
                && (national || international)) {
            let row = table.insertRow(table.rows.length);
            let cell0 = row.insertCell(0);
            cell0.innerHTML = productName;
            let cell1 = row.insertCell(1);
            if (national) {
                cell1.innerHTML = "National";
            }
            else {
                cell1.innerHTML = "International";
            }
            let cell2 = row.insertCell(2);
            cell2.innerHTML = productCost;
            let warning = document.getElementById("warning");
            if (warning != null) {
                warning.remove();
            }
        }
        else {
            if (document.getElementById("warning") == null) {
                let warning = document.createElement("p");
                warning.id = "warning";
                let text = document.createTextNode("Todos os campos têm de ser preenchidos!");
                warning.appendChild(text);
                warning.style.color = "red";
                let form = document.getElementById("form1");
                form.appendChild(warning);
            }
        }
    }

}