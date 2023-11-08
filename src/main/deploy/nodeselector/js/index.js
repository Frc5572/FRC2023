// Copyright (c) 2023 FRC 6328
// http://github.com/Mechanical-Advantage
//
// Use of this source code is governed by an MIT-style
// license that can be found in the LICENSE file at
// the root directory of this project.

import { NT4_Client } from "./NT4.js";

const buttonBool = "/Button/Stuff/pressed";


let bool = false;

let client = new NT4_Client(
  window.location.hostname,
  (topic, timestamp, value) => {
    // New data
    if (topic.name == buttonBool) {
      bool = value;
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

function includeHTML() {
  $("[w3-include-html]").each(function (i, el) {
    // `this` is the div
    $.get($(el).attr("w3-include-html"), function (data) {
      $(el).html(data)
      $(el).removeAttr("w3-include-html")
    });
  });
}



window.addEventListener("load", () => {
  includeHTML()
  // Start NT connection
  client.subscribe(
    [
      buttonBool,
    ],
  );
  client.publishTopic(buttonBool, "boolean");
  client.connect();

  // Add node click listeners
  $("#theButton").click(function () {
    client.addSample(buttonBool, !value);
  })

});
