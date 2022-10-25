package ru.netology;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {
    ConcurrentHashMap<String, ConcurrentHashMap<String, Handler>> handlers = new ConcurrentHashMap();

    public void listen(int port) {
        final ExecutorService threadPool = Executors.newFixedThreadPool(64);
        try (final var serverSocket = new ServerSocket(port)) {
            while (true) {
                try {
                    final var socket = serverSocket.accept();
                    this.connection(socket, threadPool);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void connection(Socket socket, ExecutorService threadPool) {

        threadPool.submit(() -> {
            try (
                    final var in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    final var out = new BufferedOutputStream(socket.getOutputStream());
            ) {
                while (true) {
                    final var requestLine = in.readLine();
                    final var parts = requestLine.split(" ");

                    if (parts.length != 3) {
                        badRequest(out);
                        continue;
                    }

                    final var request = new Request(parts[0], parts[1]);

                    if (!handlers.containsKey(request.getMethod())) {
                        notFound(out);
                        return;
                    }

                    var methodHandlers = handlers.get(request.getMethod());

                    if (!methodHandlers.containsKey(request.getPath())) {
                        notFound(out);
                        return;
                    }
                    var handler = methodHandlers.get(request.getPath());

                    if (handler == null) {
                        notFound(out);
                        return;
                    }

                    handler.handle(request, out);

                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });
    }

    private void notFound(BufferedOutputStream out) throws IOException {
        out.write((
                "HTTP/1.1 404 Not Found\r\n" +
                        "Content-Length: 0\r\n" +
                        "Connection: close\r\n" +
                        "\r\n"
        ).getBytes());
        out.flush();
    }

    private void badRequest(BufferedOutputStream out) throws IOException {
        out.write((
                "HTTP/1.1 400 Bad Request\r\n" +
                        "Content-Length: 0\r\n" +
                        "Connection: close\r\n" +
                        "\r\n"
        ).getBytes());
        out.flush();
    }

    public void addHandler(String method, String path, Handler handler) {
        if (!handlers.contains(method))
            handlers.putIfAbsent(method, new ConcurrentHashMap<String, Handler>());
        handlers.get(method).put(path, handler);
    }
}