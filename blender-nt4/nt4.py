
import time
import threading
import websocket
import struct
import msgpack
import json

# websocket.enableTrace(True)

time_us_offset = 0

def time_us():
    return time.monotonic_ns() // 1000 + time_us_offset

def binary_data_frame(topic, timestamp, data_type, data_value):
    return msgpack.packb((topic, timestamp, data_type, data_value), use_bin_type=True)

class Nt4Client:
    def __init__(self):
        self.thread = threading.Thread(target=self.run, daemon=True)
        self.ip = "127.0.0.1"
        self.lock = threading.Lock()
        self.wsapp = None
        self.running = True
        self.message = bytearray(b'')
        self.message_type = 0
        self.new_topics = []
        self.thread.start()
        pass
    pass

    def set_ip(self, ip):
        with self.lock:
            self.ip = ip

    def set_team(self, team):
        with self.lock:
            self.ip = "10.{}.{}.2".format(team // 100, team % 100)

    def all_new_topics(self):
        with self.lock:
            local_topics = self.new_topics
            self.new_topics = []
        return local_topics

    def run(self):
        while True:
            websocket.setdefaulttimeout(0.5)
            with self.lock:
                if not self.running:
                    return
                print(self.ip)
                self.wsapp = websocket.WebSocketApp("ws://{}:5810/nt/blender".format(self.ip), on_open=self.on_open, on_data=self.on_data, on_close=self.on_close)
            self.wsapp.run_forever()
            time.sleep(0.5)
    
    def on_open(self, ws):
        ws.send(binary_data_frame(-1, 0, 2, time_us()), opcode=websocket.ABNF.OPCODE_BINARY)
    
    def process_binary_message(self, ws):
        topic, timestamp, data_type, data = msgpack.unpackb(bytes(self.message), use_list=False)
        if topic == -1:
            curr_time = time_us()
            rtt = curr_time - data
            server_time = timestamp + rtt // 2
            global time_us_offset
            time_us_offset = server_time - curr_time
            ws.send(json.dumps([{"method": "subscribe", "params": {
                "topics": ["/"],
                "subuid": 1,
                "options": {
                    "all": True,
                    "topicsonly": True,
                    "prefix": True
                }
            }}]))
        else:
            pass
    
    def process_text_message(self, ws):
        data = json.loads(self.message)
        for item in data:
            if not 'method' in item or not 'params' in item:
                print('error')
                continue
            method = item['method']
            params = item['params']
            if method == 'announce':
                with self.lock:
                    self.new_topics.append(params)
                pass
        pass

    def process_message(self, ws):
        if type(self.message) is str:
            self.process_text_message(ws)
        else:
            self.process_binary_message(ws)

    def on_data(self, ws, message, data_type, cont_flag):
        if type(message) is str:
            self.message = message
        else:
            self.message = bytearray(message)
        self.message_type = data_type
        if cont_flag:
            self.process_message(ws)

    def on_cont(self, ws, message, cont_flag):
        if message is str:
            self.message += message
        else:
            self.message.append(message)
        if cont_flag:
            self.process_message(ws)
    
    def on_close(self, ws, close_status_code, close_msg):
        if close_status_code is not None and close_msg is not None:
            print('close')
            print(ws)
            print(close_status_code)
            print(close_msg)

    def __del__(self):
        with self.lock:
            self.running = False
            self.wsapp.close()
