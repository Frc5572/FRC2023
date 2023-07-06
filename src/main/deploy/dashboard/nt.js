

async function run(name, ip) {
    const { _nt4_start } = wasm_bindgen;
    await wasm_bindgen('./nt4_wasm_bg.wasm');
    await _nt4_start(name, ip);
}

var items = {};

run("webwoo", "{ROBOT_IP}").then(() => {
    const { sub_topic_list, addEventListener, publish, unpublish, subscribe, unsubscribe } = wasm_bindgen;
    let tabl = document.getElementById("tabl");
    addEventListener("announce", (name, type) => {
        console.log(name);
        let tr = document.createElement("tr");
        let td1 = document.createElement("td");
        let td2 = document.createElement("td");
        let td3 = document.createElement("td");
        let input = document.createElement("input");
        input.setAttribute("type", "checkbox");
        input.id = "name";
        let span = document.createElement("span");
        input.addEventListener("click", () => {
            if (input.checked) {
                items[name] = subscribe(name, (value) => {
                    console.log(name + " updated");
                    span.innerText = String(value);
                });
            } else {
                unsubscribe(items[name]);
            }
        });
        td1.appendChild(input);
        td2.appendChild(new Text(name));
        td3.appendChild(span);
        tr.appendChild(td1);
        tr.appendChild(td2);
        tr.appendChild(td3);
        tabl.appendChild(tr);
    });
    sub_topic_list("/");
});