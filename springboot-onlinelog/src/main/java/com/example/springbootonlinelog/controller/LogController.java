package com.example.springbootonlinelog.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LogController {

    @Value("${server.address:ws://localhost:8080/logWebsocket}")
    private String serverAddress;
    
    @GetMapping("/log")
    public Object getLog() {
        return String.format("<!DOCTYPE HTML><html><head><title>My Log WebSocket</title></head><body " +
                "bgcolor=\"black\"><div " +
                "id=\"message\"style=\"color:white\"><button onclick=\"openLogWebSocket()\">打开日志</button></div></body><script type=\"text/javascript\">var websocket=null;function openLogWebSocket(){if('WebSocket'in window){websocket=new WebSocket(\"%s\");}\n" +
                "else{alert('Not support websocket');}\n" +
                "websocket.onerror=function(){setMessageInnerHTML(\"error\");};websocket.onopen=function(event){setMessageInnerHTML(\"open\");}\n" +
                "websocket.onmessage=function(event){setMessageInnerHTML(event.data);}\n" +
                "websocket.onclose=function(){setMessageInnerHTML(\"close\");}\n" +
                "window.onbeforeunload=function(){websocket.close();}\n" +
                "document.getElementById('message').innerHTML='';}\n" +
                "function setMessageInnerHTML(innerHTML){if(document.getElementById('message').innerHTML.length>1000000){document.getElementById('message').innerHTML='';}document.getElementById('message').innerHTML+=innerHTML+'<br/>';}\n" +
                "function closeWebSocket(){websocket.close();}</script></html>",serverAddress);
    }
}
