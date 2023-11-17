import { NT4_Client } from "./NT4.js";

let _ = new NT4_Client();
let currentTargetRow = -1;
let currentTargetColumn = -1;
let posList = Array(3);
let selected = false;
posList[0] = Array(9).fill(false);
posList[1] = Array(9).fill(false);
posList[2] = Array(9).fill(false);

console.log(posList);

$("#slider").on("change", (x) => {
    console.log(x.target.value)
})

$(".cell").each((_, item) => {
    let col = parseInt($(item).attr("col"));
    let row = parseInt($(item).attr("row"));
    console.log([row, col]);
    if (col == 1 || col == 4 || col == 7 || row == 2) {
        $(item).addClass("cube");
    } else {
        $(item).addClass("cone");
    }
});

$(".cell").on("click", (x) => {
    $(".selected").removeClass("selected");
    let target = x.target;
    while (!target.classList.contains("cell")) {
        target = target.parentNode;
    }
    $("#gridConfirm").removeClass("btn-danger").addClass("btn-success");
    let row = target.getAttribute("row");
    let column = target.getAttribute("col");
    if (currentTargetRow == row && currentTargetColumn == column) {
        target.classList.remove("selected");
        currentTargetRow = -1;
        currentTargetColumn = -1;
    } else {
        target.classList.add("selected");
        currentTargetRow = row;
        currentTargetColumn = column;
    }
    console.log([row, column])
});

$("#gridConfirm").on("click", () => {
    if (posList[currentTargetRow][currentTargetColumn] != true) {
        posList[currentTargetRow][currentTargetColumn] = true;
        $(`div[row=${currentTargetRow}][col=${currentTargetColumn}]`).removeClass("selected").addClass("done");
        $("#gridConfirm").removeClass("btn-success").addClass("btn-danger");
        currentTargetRow = -1;
        currentTargetColumn = -1;
    } else {
        posList[currentTargetRow][currentTargetColumn] = false;
        $(`div[row=${currentTargetRow}][col=${currentTargetColumn}]`).removeClass("done");
    }
})

