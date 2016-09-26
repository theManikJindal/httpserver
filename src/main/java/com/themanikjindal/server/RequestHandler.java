package com.themanikjindal.server;

import com.themanikjindal.data.FileResponse;
import com.themanikjindal.data.RequestURI;
import com.themanikjindal.utils.SharedRef;
import rx.functions.Action1;

import java.io.File;
import java.io.IOException;
import java.net.Socket;
import java.util.Optional;

public class RequestHandler implements Action1<SharedRef<Socket>> {

    private final FileManager fileManager;

    public RequestHandler(FileManager fileManager) {
        this.fileManager = fileManager;
    }

    @Override
    public void call(SharedRef<Socket> socketSharedRef) {
        try {
            Socket socket = socketSharedRef.get();
            Optional<String> requestUri = RequestURI.get(socket.getInputStream());

            byte[] responseBytes;
            if (requestUri.isPresent() && requestUri.get().equals("/")) {
                responseBytes = FileResponse.getServerInfo();
            } else {
                Optional<File> file = requestUri.flatMap(fileManager::getFile);
                FileResponse fileResponse = new FileResponse(file.orElse(null));
                responseBytes = fileResponse.getBytes();
            }

            socket.getOutputStream().write(responseBytes);
        } catch (IOException ignored) {
        } finally {
            socketSharedRef.deleteCopyNoThrow();
        }
    }
}


