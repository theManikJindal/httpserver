package com.themanikjindal.data;


import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.Optional;
import java.util.stream.Stream;

public class FileResponse {

    private final File file;
    private final int statusCode;

    public FileResponse(Optional<File> file) {
        this.file = file.orElse(null);

        if(this.file == null) {
            statusCode = 403;
        } else {
            if(this.file.exists()) {
                if(this.file.isFile()) {
                    statusCode = 200;
                } else {
                    statusCode = 400;
                }
            } else {
                statusCode = 404;
            }
        }
    }

    private String get200Header(String contentType, int contentLength) {
        return "HTTP/1.1 200 OK\r\n" +
                "Content-Type: " + ((contentType != null)? (contentType) : ("text/plain")) +
                "\r\n" +
                "Content-Length: " + contentLength +
                "\r\n" +
                "\r\n";
    }

    private String get400() {
        return "HTTP/1.1 400 Bad Request\r\n" +
                "Content-Type: text/html\r\n" +
                "Content-Length: 20\r\n" +
                "\r\n" +
                "<h1>Bad Request</h1>";
    }

    private String get403() {
        return "HTTP/1.1 403 Forbidden\r\n" +
                "Content-Type: text/html\r\n" +
                "Content-Length: 25\r\n" +
                "\r\n" +
                "<h1>Access Forbidden</h1>";
    }

    private String get404() {
        return "HTTP/1.1 404 Not Found\r\n" +
                "Content-Type: text/html\r\n" +
                "Content-Length: 23\r\n" +
                "\r\n" +
                "<h1>File Not Found</h1>";
    }

    public byte[] getBytes() throws IOException {
        switch (statusCode) {
            case 400:
                return get400().getBytes();

            case 403:
                return get403().getBytes();

            case 404:
                return get404().getBytes();

            case 200:
                byte[] fileBytes = Files.readAllBytes(file.toPath());
                byte[] headerBytes = get200Header( Files.probeContentType(file.toPath()), fileBytes.length).getBytes();
                byte[] responseBytes = Arrays.copyOf(headerBytes, headerBytes.length + fileBytes.length);
                System.arraycopy(fileBytes, 0, responseBytes, headerBytes.length, fileBytes.length);
                return responseBytes;
        }
        throw new IllegalStateException("Will never reach here");
    }
}
