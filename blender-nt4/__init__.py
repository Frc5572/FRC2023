bl_info = {
    "name": "Blender Nt4",
    "description": "NetworkTables in Blender",
    "author": "Wilson Watson",
    "version": (0, 0, 1),
    "blender": (3, 5, 0),
    "location": "View3D",
    "warning": "This addon is still in development.",
    "wiki_url": "",
    "category": "Object"
}

import sys
import subprocess
import os
import importlib.util

try:
    import websocket
    import msgpack
except:
    subprocess.run([sys.executable, "-m", "ensurepip"])
    subprocess.run([sys.executable, "-m", "pip", "install", "websocket-client", "msgpack"])

import bpy
from bpy.props import CollectionProperty, BoolProperty, StringProperty, EnumProperty, IntProperty

spec = importlib.util.spec_from_file_location("nt4", os.path.join(os.path.dirname(__file__), "nt4.py"))
nt4 = importlib.util.module_from_spec(spec)
sys.modules["nt4"] = nt4
spec.loader.exec_module(nt4)

client = None

class TopicProperty(bpy.types.PropertyGroup):
    name: StringProperty(name="name", default="")
    checked: BoolProperty(name="checked", default=False)
    id: IntProperty(name="id", default=0)

class Nt4Panel(bpy.types.Panel):
    bl_label = "NetworkTables"
    bl_idname = "NT4_PT_PANEL"
    bl_space_type = 'VIEW_3D'
    bl_region_type = 'UI'

    def draw(self, context):
        layout = self.layout
        wm = context.scene

        for new_topic in client.all_new_topics():
            topic_components = new_topic['name'].split('/')[1:]
            print(topic_components)

        layout.prop(wm, "conn_ty")
        if wm.conn_ty == 'IP':
            layout.prop(wm, "conn_ip")
        else:
            layout.prop(wm, "conn_team")



        pass

def update_ip(self, context):
    global client
    client.set_ip(self.conn_ip)

def update_team_no(self, context):
    global client
    client.set_team(self.conn_team)

def update_selection(self, context):
    if self.conn_ty == 'IP':
        update_ip(self, context)
    else:
        update_team_no(self, context)

def register():
    global client
    client = nt4.Nt4Client()
    bpy.utils.register_class(TopicProperty)
    from bpy.types import Scene
    Scene.topics = CollectionProperty(type=TopicProperty)
    Scene.conn_ty = EnumProperty(
        name="Connection Type",
        description="Method for determining how to connect to NetworkTables",
        items=(
            ('TEAM_NO', "Team Number", "Use Team Number to connect"),
            ('IP', "IP Address", "Use IP Address to connect"),
        ),
        default='IP',
        update=update_selection
    )
    Scene.conn_ip = StringProperty(name="IP Address", default="127.0.0.1", update=update_ip)
    Scene.conn_team = IntProperty(name="Team Number", default=5572, update=update_team_no)

    bpy.utils.register_class(Nt4Panel)

def unregister():
    global client
    del client
    bpy.utils.unregister_class(Nt4Panel)
    bpy.utils.unregister_class(TopicProperty)

if __name__ == '__main__':
    register()
