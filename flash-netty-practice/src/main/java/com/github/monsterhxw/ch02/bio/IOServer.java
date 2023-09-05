package com.github.monsterhxw.ch02.bio;

import java.io.IOException;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @author huangxuewei
 * @since 2023/9/5
 */
public class IOServer {

    private ServerSocket listenSocket;

    public IOServer(String hostname, int port) {
        try {
            InetSocketAddress inetSocketAddress = new InetSocketAddress(hostname, port);
            this.listenSocket = new ServerSocket();
            listenSocket.bind(inetSocketAddress);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void start() {
        System.out.println("IOServer start with port: " + listenSocket.getLocalPort());
        new Thread(() -> {
            String curName = Thread.currentThread().getName();
            while (true) {
                try {
                    // 阻塞方法获取新客户端连接
                    Socket clientSocket = listenSocket.accept();
                    System.out.println(curName + " accept: " + clientSocket);
                    // 处理客户端请求，Thread Per Connection
                    new Thread(new IOServerHandler(clientSocket)).start();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }).start();
    }

    private static class IOServerHandler implements Runnable {

        private Socket clientSocket;

        public IOServerHandler(Socket clientSocket) {
            this.clientSocket = clientSocket;
        }

        @Override
        public void run() {
            // 读取 client connection 的数据
            int len;
            byte[] data = new byte[1024];
            try {
                InputStream in = clientSocket.getInputStream();
                // 按字节流方式读取 data
                while ((len = in.read(data)) != -1) {
                    System.out.println(new String(data, 0, len));
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * Entry point of the program
     * Starts the server on port 8080
     */
    public static void main(String[] args) {
        // Start the server
        new IOServer("127.0.0.1", 8080).start();
    }
}
