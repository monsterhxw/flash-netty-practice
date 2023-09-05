package com.github.monsterhxw.ch02.nio;

import com.github.monsterhxw.ch02.bio.IOClient;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.Set;

/**
 * @author huangxuewei
 * @since 2023/9/5
 */
public class NIOServer {

    public static void main(String[] args) throws IOException {
        // listen selector
        Selector listenSelector = Selector.open();
        // client selector
        Selector clientSelector = Selector.open();

        // listen socket events
        new Thread(() -> nioServer(listenSelector, clientSelector)).start();

        // handle client read events
        new Thread(() -> nioClient(clientSelector)).start();

        new Thread(() -> {
            // create client connect to server
            try {
                Thread.sleep(1_000L);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            new IOClient("127.0.0.1", 8080).write();
        }).start();
    }

    private static void nioServer(Selector listenSelector, Selector clientSelector) {
        try {
            // create server socket channel
            ServerSocketChannel listenSocketChannel = ServerSocketChannel.open();
            // bind
            InetSocketAddress isa = new InetSocketAddress("127.0.0.1", 8080);
            listenSocketChannel.bind(isa);
            // set non blocking
            listenSocketChannel.configureBlocking(false);
            // register to listen selector
            listenSocketChannel.register(listenSelector, SelectionKey.OP_ACCEPT);

            for (; ; ) {
                int acceptClientConnSize = listenSelector.select(1L);
                if (acceptClientConnSize > 0) {
                    Set<SelectionKey> readySet = listenSelector.selectedKeys();
                    Iterator<SelectionKey> iterator = readySet.iterator();

                    while (iterator.hasNext()) {
                        SelectionKey key = iterator.next();
                        if (key.isAcceptable()) {
                            try {
                                // create client socket channel
                                SocketChannel clientSocketChannel = ((ServerSocketChannel) key.channel()).accept();
                                System.out.println("nio handle client connection: " + clientSocketChannel);
                                // set non blocking
                                clientSocketChannel.configureBlocking(false);
                                // register to client selector
                                clientSocketChannel.register(clientSelector, SelectionKey.OP_READ);
                            } finally {
                                iterator.remove();
                            }
                        }
                    }
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static void nioClient(Selector clientSelector) {
        try {
            for (; ; ) {
                int clientReadReadySize = clientSelector.select(1L);
                if (clientReadReadySize > 0) {
                    Set<SelectionKey> readReadySet = clientSelector.selectedKeys();
                    Iterator<SelectionKey> iterator = readReadySet.iterator();
                    while (iterator.hasNext()) {
                        SelectionKey key = iterator.next();
                        if (key.isReadable()) {
                            try {
                                SocketChannel clientSocketChannel = (SocketChannel) key.channel();
                                ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
                                // 面向 byteBuffer
                                clientSocketChannel.read(byteBuffer);
                                byteBuffer.flip();

                                String dataStr = Charset.defaultCharset().newDecoder().decode(byteBuffer).toString();
                                System.out.println("nio handle receive client data: " + dataStr);
                            } finally {
                                iterator.remove();
                                key.interestOps(SelectionKey.OP_READ);
                            }
                        }
                    }
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
