import { NT4_Client } from "./NT4.js";

let _ = new NT4_Client();
let currentTargetRow = 0;
let currentTargetColumn = 0;
let posList = Array(3);
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
    target.classList.add("selected");
    let row = target.getAttribute("row");
    currentTargetRow = row;
    let column = target.getAttribute("col");
    currentTargetColumn = column;
    console.log([row, column])
    // console.log([currentTargetRow, currentTargetColumn])
});

$("#gridConfirm").on("click", () => {
    posList[currentTargetRow][currentTargetColumn] = true;
    $(`div[row=${currentTargetRow}][col=${currentTargetColumn}]`).removeClass("selected").addClass("done");
})