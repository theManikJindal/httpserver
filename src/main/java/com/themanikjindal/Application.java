package com.themanikjindal;

import com.themanikjindal.data.ServerOptions;
import com.themanikjindal.server.FileManager;
import com.themanikjindal.server.RequestHandler;
import com.themanikjindal.server.SocketManager;
import com.themanikjindal.utils.SharedRef;
import rx.Observable;
import rx.Scheduler;
import rx.schedulers.Schedulers;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.util.concurrent.Executors;


public class Application {
    public static void main(String[] args) throws IOException {

        ServerOptions serverOptions = new ServerOptions(args);

        FileManager fileManager = new FileManager(new File(serverOptions.rootDirectory));
        RequestHandler requestHandler = new RequestHandler(fileManager);
        SocketManager socketManager = new SocketManager(serverOptions.port,
                InetAddress.getByName(serverOptions.bindAddress));

        Scheduler fixedThreadPoolScheduler = Schedulers.from(Executors.newFixedThreadPool(serverOptions.threads));

        socketManager.getIncomingRequests()
                .map(SharedRef<Socket>::new)
                .forEach(socketSharedRef1 -> Observable.just(socketSharedRef1)
                        .subscribeOn(fixedThreadPoolScheduler)
                        .subscribe(socketSharedRef -> {
                            try {
                                System.out.println("Processing request in thread: " + Thread.currentThread().getName());
                                requestHandler.call(socketSharedRef);
                            } catch (Exception ignored) {}
                        }));

        socketManager.startListening();
    }
}
