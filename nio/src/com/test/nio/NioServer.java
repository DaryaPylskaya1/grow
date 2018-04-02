package com.test.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.Set;

public class NioServer {
    static final int PORT = 9090;
    static final String ADDSESS = "localhost";
    private ByteBuffer buffer;

    public NioServer() throws IOException {
        ServerSocketChannel channel = ServerSocketChannel.open();
        channel.configureBlocking(false);
        InetSocketAddress address = new InetSocketAddress(ADDSESS, PORT);
        channel.socket().bind(address);
        Selector selector = Selector.open();
        channel.register(selector, SelectionKey.OP_ACCEPT);
        new Thread(() -> {
            while (true) {
                try {
                    int readyChannels = selector.select();
                    if (readyChannels == 0) continue;
                    Set<SelectionKey> selectedKeys = selector.selectedKeys();
                    Iterator<SelectionKey> keyIterator = selectedKeys.iterator();
                    while (keyIterator.hasNext()) {

                        SelectionKey key = keyIterator.next();
                        if (key.isAcceptable()) {
                            SocketChannel client = channel.accept();
                            client.configureBlocking(false);
                            client.register(selector, SelectionKey.OP_READ);
                            System.out.println("accepted");
                        } else if (key.isConnectable()) {
                            // a connection was established with a remote server.

                        } else if (key.isReadable()) {
                            buffer = ByteBuffer.allocate(50);
                            SocketChannel client = (SocketChannel) key.channel();
                            int count = client.read(buffer);
                            if (count > 0) {
                                System.out.println("read:" + new String(buffer.array(), Charset.defaultCharset()));
                                key.interestOps(SelectionKey.OP_WRITE);
                            } else {
                                channel.close();
                                System.exit(0);
                            }
                        } else if (key.isWritable()) {
                            SocketChannel client = (SocketChannel) key.channel();
                            buffer.flip();
                            client.write(buffer);
                            System.out.println("sent back: " + new String(buffer.array(), Charset.defaultCharset()));
                            key.interestOps(SelectionKey.OP_READ);
                        }

                        keyIterator.remove();
                    }
                } catch (Exception e) {
                    System.err.println(e);
                }
            }
        }).start();
    }

    public static void main(String[] args) throws IOException {
        new NioServer();
    }
}
