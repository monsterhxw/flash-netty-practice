package com.github.monsterhxw.ch02.bio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;

/**
 * @author huangxuewei
 * @since 2023/9/5
 */
public class IOClient {

    private Socket clientSocket;

    public IOClient(String hostname, int port) {
        try {
            InetSocketAddress inetSocketAddress = new InetSocketAddress(hostname, port);
            clientSocket = new Socket();
            clientSocket.connect(inetSocketAddress);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void write() {
        try {
            while (true) {
                clientSocket.getOutputStream().write((System.currentTimeMillis() + ": hello world").getBytes());
                Thread.sleep(2_000L);
            }
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) {
        int size = 1;
        for (int i = 0; i < size; i++) {
            new Thread(() -> new IOClient("127.0.0.1", 8080).write()).start();
        }
    }
}
