package ru.netology;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class Main {
    public static void main(String[] args) {
        Server server = new Server();

        server.addHandler("GET", "/app.js", Main::sendFile);
        server.addHandler("GET", "/events.html", Main::sendFile);
        server.addHandler("GET", "/events.js", Main::sendFile);
        server.addHandler("GET", "/forms.html", Main::sendFile);
        server.addHandler("GET", "/index.html", Main::sendFile);
        server.addHandler("GET", "/links.html", Main::sendFile);
        server.addHandler("GET", "/resources.html", Main::sendFile);
        server.addHandler("GET", "/spring.png", Main::sendFile);
        server.addHandler("GET", "/spring.svg", Main::sendFile);
        server.addHandler("GET", "/styles.css", Main::sendFile);

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

    private static void sendFile(Request request, BufferedOutputStream responseStream) {
        try {
            final var filePath = Path.of(".", "public", request.getPath());
            final var mimeType = Files.probeContentType(filePath);
            final var length = Files.size(filePath);
            responseStream.write((
                    "HTTP/1.1 200 OK\r\n" +
                            "Content-Type: " + mimeType + "\r\n" +
                            "Content-Length: " + length + "\r\n" +
                            "Connection: close\r\n" +
                            "\r\n"
            ).getBytes());
            Files.copy(filePath, responseStream);
            responseStream.flush();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}


