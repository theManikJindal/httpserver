package com.themanikjindal.server;

import rx.subjects.PublishSubject;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.Executors;


public class SocketManager {

    private static final int BACKLOG_CONSTANT = 50;

    private final ServerSocket serverSocket;
    private final PublishSubject<Socket> incomingRequests = PublishSubject.create();

    public SocketManager(int port, InetAddress bindAddress) throws IOException {
        this.serverSocket = new ServerSocket(port, BACKLOG_CONSTANT, bindAddress);
    }

    public PublishSubject<Socket> getIncomingRequests() {
        return incomingRequests;
    }

    public void startListening() {
        Executors.newSingleThreadExecutor().submit(() -> {
            while(true) {
                try {
                    Socket socket = serverSocket.accept();
                    System.out.println("Accepted connection from port: " + socket.getPort());
                    incomingRequests.onNext(socket);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

}
