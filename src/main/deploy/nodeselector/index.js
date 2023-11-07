import { NT4_Client } from "./NT4.js";

const keyDownTopic = "/input/keyboardDown";
const keyUpTopic = "/input/keyboardUp";
const mouseMoveTopic = "/input/mouseDelta";

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

  },
  () => {
    // Connected
    document.body.style.backgroundColor = "blue";
  },
  () => {
    // Disconnected
    document.body.style.backgroundColor = "red";
  }
);

window.addEventListener("load", () => {
  // Start NT connection
  client.publishTopic(keyDownTopic, "string");
  client.publishTopic(keyUpTopic, "string");
  client.publishTopic(mouseMoveTopic, "double[]");
  client.connect();
});

document.addEventListener("keydown", (ev) => {
  client.addSample(keyDownTopic, "");
  client.addSample(keyDownTopic, ev.key.toLowerCase());
});

document.addEventListener("keyup", (ev) => {
  client.addSample(keyUpTopic, "");
  client.addSample(keyUpTopic, ev.key.toLowerCase());
});

let lock_button = document.getElementById("lock");

lock_button.addEventListener("click", () => {
  let locked = lock_button.requestPointerLock({
    unadjustedMovement: true
  });

  if (!locked) {
    locked = lock_button.requestPointerLock();
  }
});

lock_button.addEventListener("mousemove", (ev) => {
  client.addSample(mouseMoveTopic, []);
  client.addSample(mouseMoveTopic, [ev.movementX, ev.movementY]);
});

document.body.addEventListener("mousedown", (ev) => {
  client.addSample(keyDownTopic, "");
  client.addSample(keyDownTopic, "mouse" + ev.button);
});

document.body.addEventListener("mouseup", (ev) => {
  client.addSample(keyUpTopic, "");
  client.addSample(keyUpTopic, "mouse" + ev.button);
});

