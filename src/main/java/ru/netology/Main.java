package ru.netology;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class Main {
    public static void main(String[] args) {
        Server server = new Server();

        server.addHandler("GET", "/messages", new Handler() {
            public void handle(Request request, BufferedOutputStream responseStream) {
                var message = "Hello! GET";
                try {
                    responseStream.write((
                            "HTTP/1.1 200 OK\r\n" +
                                    "Content-Type: " + "text/plain" + "\r\n" +
                                    "Content-Length: " + message.length() + "\r\n" +
                                    "Connection: close\r\n" +
                                    "\r\n" + message
                    ).getBytes());
                    responseStream.flush();

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        server.addHandler("POST", "/messages", new Handler() {
            public void handle(Request request, BufferedOutputStream responseStream) {
                var message = "Hello! POST";
                try {
                    responseStream.write((
                            "HTTP/1.1 200 OK\r\n" +
                                    "Content-Type: " + "text/plain" + "\r\n" +
                                    "Content-Length: " + message.length() + "\r\n" +
                                    "Connection: close\r\n" +
                                    "\r\n" + message
                    ).getBytes());
                    responseStream.flush();

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        server.listen(9999);
    }
}


