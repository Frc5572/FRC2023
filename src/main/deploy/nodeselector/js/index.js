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
const LevelOptions = "/Shuffleboard/Auto/Level/options";
const LevelActive = "/Shuffleboard/Auto/Level/active";
const LevelSelected = "/Shuffleboard/Auto/Level/selected";
const ColumnOptions = "/Shuffleboard/Auto/Column/options";
const ColumnActive = "/Shuffleboard/Auto/Column/active";
const ColumnSelected = "/Shuffleboard/Auto/Column/selected";
const fieldPos = "/Shuffleboard/Auto/Field Pos/Robot";
const enableDock = "/Shuffleboard/Auto/Enable Dock";
const aprilTagStatus = "/Shuffleboard/Auto/Currently Seeing April Tag";

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

$("#button").on("click", () => {
  console.log("time clicked")
});