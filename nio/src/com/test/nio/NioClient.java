package com.test.nio;

import static java.nio.channels.SelectionKey.OP_CONNECT;
import static java.nio.channels.SelectionKey.OP_READ;
import static java.nio.channels.SelectionKey.OP_WRITE;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.Scanner;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class NioClient {
    private static final int PORT = 9090;
    private static final String ADDSESS = "localhost";
    private ByteBuffer buffer;

    private void run() throws IOException {
        SocketChannel channel = SocketChannel.open();
        channel.configureBlocking(false);
        Selector selector = Selector.open();
        channel.register(selector, OP_CONNECT);
        channel.connect(new InetSocketAddress(ADDSESS, PORT));
        BlockingQueue<String> queue = new ArrayBlockingQueue<>(2);
        new Thread(() -> {
            Scanner scanner = new Scanner(System.in);
            while (true) {
                String line = scanner.nextLine();
                if (line.isEmpty()) {
                    try {
                        channel.finishConnect();
                        channel.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    System.exit(0);
                }
                queue.add(line);
                SelectionKey key = channel.keyFor(selector);
                key.interestOps(OP_WRITE);
            }
        }).start();

        while (true) {
            selector.select();
            for (SelectionKey key : selector.selectedKeys()) {
                if (key.isConnectable()) {
                    channel.finishConnect();
                    key.interestOps(OP_WRITE);
                }
                if (key.isReadable()) {
                    buffer = ByteBuffer.allocate(50);
                    channel.read(buffer);
                    System.out.println("Read:" + new String(buffer.array(), Charset.defaultCharset()));
                    key.interestOps(OP_WRITE);
                }
                if (key.isWritable()) {
                    String line = queue.poll();
                    if (line != null) {
                        channel.write(ByteBuffer.wrap(line.getBytes()));
                        key.interestOps(OP_READ);
                    }

                }

            }
        }
    }

    public static void main(String[] args) throws IOException {
        new NioClient().run();
    }
}
