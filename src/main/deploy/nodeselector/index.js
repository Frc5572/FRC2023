// Copyright (c) 2023 FRC 6328
// http://github.com/Mechanical-Advantage
//
// Use of this source code is governed by an MIT-style
// license that can be found in the LICENSE file at
// the root directory of this project.

import { NT4_Client } from "./NT4.js";

const nodeTopic = "/nodeselector/node_target";
const coneTippedRobotToDashboardTopic = "/nodeselector/cone_tipped_robot_to_dashboard";
const coneTippedDashboardToRobotTopic = "/nodeselector/cone_tipped_dashboard_to_robot";
const matchTimeTopic = "/nodeselector/match_time";
const isAutoTopic = "/nodeselector/is_auto";

let active = null;
let tipped = false;
let matchTime = 0;
let isAuto = false;

function rowIdFromId(row_num) {
  let a = ["#row-low", "#row-med", "#row-high"];
  return a[row_num];
}

function displayActive(index) {
  if (index !== null) {
    active = index;
    $(".node.active").removeClass('active');
    let row = rowIdFromId(index[0]);
    $(row).find("td").eq(index[1]).addClass("active");
  }
}

function sendTarget(row, column) {
  // alert(row + '  ' + column);
  if ([row, column] !== active) {
    displayActive([row, column]);
    client.addSample(nodeTopic, [row, column]);
  }
}

function displayTipped(newTipped) {
  // if (newTipped != tipped) {
  // tipped = newTipped;
  $(".cone-orientation").toggleClass("tipped");
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
  // client.addSample(coneTippedDashboardToRobotTopic, tipped);
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
    } else if (topic.name === coneTippedRobotToDashboardTopic) {
      displayTipped(value);
    } else if (topic.name === matchTimeTopic) {
      matchTime = value;
      displayTime(matchTime, isAuto);
      console.log(matchTime);
    } else if (topic.name === isAutoTopic) {
      isAuto = value;
      displayTime(matchTime, isAuto);
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
      coneTippedRobotToDashboardTopic,
      matchTimeTopic,
      isAutoTopic,
    ],
    false,
    false,
    0.02
  );
  client.publishTopic(nodeTopic, "int[]");
  client.publishTopic(coneTippedDashboardToRobotTopic, "boolean");
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
