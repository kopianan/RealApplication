package com.example.realapplication

class Message(var id :String, var fromId :String, var toId :String, var text:String, var time :Long) {
    constructor():this("","","","",-1)
}