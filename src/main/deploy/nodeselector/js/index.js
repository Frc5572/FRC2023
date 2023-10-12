// Copyright (c) 2023 FRC 6328
// http://github.com/Mechanical-Advantage
//
// Use of this source code is governed by an MIT-style
// license that can be found in the LICENSE file at
// the root directory of this project.

import { NT4_Client } from "./NT4.js";

const nodeTopic = "/nodeselector/node_target";
const nodeStatusTopic = "/nodeselector/node_status";
const coneTippeddTopic = "/nodeselector/cone_tipped";
const matchTimeTopic = "/nodeselector/match_time";
const isAutoTopic = "/nodeselector/is_auto";
const autoChooserOptions = "/Shuffleboard/Auto/Auto Chooser/options";
const autoChooserActive = "/Shuffleboard/Auto/Auto Chooser/active";
const autoChooserSelected = "/Shuffleboard/Auto/Auto Chooser/selected";


let active = null;
let tipped = false;
let matchTime = 0;
let isAuto = false;
let nodeStatus = Array(27).fill(false);

function rowIdFromId(row_num) {
  let a = ["#row-low", "#row-med", "#row-high"];
  return a[row_num];
}

function displayActive(index) {
  if (index !== null) {
    active = index;
    $(".node.active").removeClass('active');
    let row = rowIdFromId(index[0]);
    $(row).find("div").eq(index[1]).addClass("active");
  }
  console.log(active);
}

function sendTarget(row, column) {
  // alert(row + '  ' + column);
  if ([row, column] !== active) {
    // if (row !== active[0] && column !== active[1]) {
    displayActive([row, column]);
    client.addSample(nodeTopic, [row, column]);
  }
}

function displayTipped(newTipped) {
  // if (newTipped != tipped) {
  tipped = newTipped;
  if (tipped) {
    $(".cone-orientation").addClass("tipped");
  } else {
    $(".cone-orientation").removeClass("tipped");
  }
  // }
}

function displayTime(time, isAuto) {
  let element = $("#time");
  if (isAuto) {
    element.addClass("auto");
    element.removeClass("teleop-1 teleop-2 teleop-3");
  } else if (time > 30 || time == 0) {
    element.addClass("teleop-1");
    element.removeClass("auto teleop-2 teleop-3");
  } else if (time > 15) {
    element.addClass("teleop-2");
    element.removeClass("teleop-1 auto teleop-3");
  } else {
    element.addClass("teleop-3");
    element.removeClass("teleop-1 teleop-2 auto");
  }
  let secondsString = (time % 60).toString();
  if (secondsString.length == 1) {
    secondsString = "0" + secondsString;
  }
  element.text(Math.floor(time / 60).toString() + ":" + secondsString);
}

function toggleTipped() {
  tipped = !tipped;
  displayTipped(tipped);
  client.addSample(coneTippeddTopic, tipped);
}

let client = new NT4_Client(
  window.location.hostname,
  "NodeSelector",
  (topic) => {
    // Topic announce
  },
  (topic) => {
    // Topic unannounce
  },
  (topic, timestamp, value) => {
    // New data
    if (topic.name === nodeTopic) {
      document.body.style.backgroundColor = "white";
      displayActive(value);
    } else if (topic.name == nodeStatusTopic) {
      nodeStatus = value;
      console.log(value);
      for (let i = 0; i < value.length; i++) {
        let row = Math.floor(i / 9);
        let col = i - (row * 9);
        let rowId = rowIdFromId(row);
        let colId = $(rowId).find("div").eq(col);
        if (value[i]) {
          colId.addClass("confirmed");
        } else {
          colId.removeClass("confirmed");
        }
      }
    } else if (topic.name === coneTippeddTopic) {
      console.log(value);
      displayTipped(value);
    } else if (topic.name === matchTimeTopic) {
      matchTime = value;
      displayTime(matchTime, isAuto);
      console.log(matchTime);
    } else if (topic.name === isAutoTopic) {
      isAuto = value;
      displayTime(matchTime, isAuto);
    } else if (topic.name == autoChooserOptions) {
      // console.log("Auto Chooser Options - " + value);
      // let values = value.split(",");
      $("#autoChooser").children("option").remove();
      for (let i = 0; i < value.length; i++) {
        $("#autoChooser").append(new Option(value[i], value[i]));
      }
    } else if (topic.name == autoChooserActive) {
      // console.log("Auto Chooser Active - " + value);
      $("#autoChooser").val(value);
    }
  },
  () => {
    // Connected
    document.body.style.backgroundColor = "blue";
  },
  () => {
    // Disconnected
    document.body.style.backgroundColor = "red";
    displayActive(null);
    displayTipped(false);
    displayTime(0, false);
  }
);

window.addEventListener("load", () => {
  // Start NT connection
  client.subscribe(
    [
      nodeTopic,
      nodeStatusTopic,
      coneTippeddTopic,
      matchTimeTopic,
      isAutoTopic,
      autoChooserActive,
      autoChooserOptions,
      autoChooserSelected
    ],
    false,
    false,
    0.02
  );
  client.publishTopic(nodeTopic, "int[]");
  client.publishTopic(nodeStatusTopic, "boolean[]");
  client.publishTopic(coneTippeddTopic, "boolean");
  // client.publishTopic(autoChooserActive, "string");
  client.publishTopic(autoChooserSelected, "string");
  client.connect();

  // Add node click listeners
  $(".node.low").click(function () {
    sendTarget(0, $(this).index());
  });
  $(".node.med").click(function () {
    sendTarget(1, $(this).index());
  });
  $(".node.high").click(function () {
    sendTarget(2, $(this).index());
  });
  $("#confirm-node").click(function () {
    let row = active[0];
    let column = active[1];
    let rowId = rowIdFromId(row);
    let colId = $(rowId).find("div").eq(column);
    let index = row * 9 + column;
    nodeStatus[index] = !nodeStatus[index];
    if (nodeStatus[index]) {
      colId.addClass("confirmed");
    } else {
      colId.removeClass("confirmed");
    }
    client.addSample(nodeStatusTopic, nodeStatus);
    console.log(nodeStatus);

  });

  $("#autoChooser").change(function () {
    let value = $("#autoChooser").val();
    client.addSample(autoChooserSelected, value);
  })
  // each((cell, index) => {
  //   cell.addEventListener("click", () => {
  //     sendActive(index);
  //     console.log("click node " + JSON.stringify(cell) + ' ' + index)
  //   });
  //   cell.addEventListener("contextmenu", (event) => {
  //     event.preventDefault();
  //     sendActive(index);
  //   });
  // });

  // Add node touch listeners
  // [("touchstart", "touchmove")].forEach((eventString) => {
  //   document.body.addEventListener(eventString, (event) => {
  //     if (event.touches.length > 0) {
  //       let x = event.touches[0].clientX;
  //       let y = event.touches[0].clientY;
  //       Array.from(document.getElementsByClassName("node")).forEach(
  //         (cell, index) => {
  //           let rect = cell.getBoundingClientRect();
  //           if (
  //             x >= rect.left &&
  //             x <= rect.right &&
  //             y >= rect.top &&
  //             y <= rect.bottom
  //           ) {
  //             sendActive(index);
  //           }
  //         }
  //       );
  //     }
  //   });
  // });

  // Add cone orientation listeners
  const coneOrientationDiv = $(".cone-orientation");
  coneOrientationDiv.click(function () {
    toggleTipped();
  });
  // coneOrientationDiv.addEventListener("contextmenu", (event) => {
  //   event.preventDefault();
  //   toggleTipped();
  // });
  // coneOrientationDiv.addEventListener("touchstart", () => {
  //   event.preventDefault();
  //   toggleTipped();
  // });
});
