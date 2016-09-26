package com.themanikjindal.data;


import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.Optional;
import java.util.stream.Stream;

public class FileResponse {

    private static final String[] INDEX_FILES = {
            "index.htm",
            "index.html"
    };

    private final File file;
    private final int statusCode;

    public FileResponse(File file) {

        if(file == null) {
            statusCode = 403;
            this.file = null;
            return;
        }

        if(!file.exists()) {
            statusCode = 404;
            this.file = null;
            return;
        }

        if(file.isFile()) {
            statusCode = 200;
            this.file = file;
            return;
        }

        if (file.isDirectory()) {
            Optional<File> indexFile = Stream.of(INDEX_FILES)
                    .map(s -> file.toPath().resolve(s).toFile())
                    .filter(file1 -> file1.exists() && file1.isFile())
                    .findFirst();

            if (indexFile.isPresent()) {
                statusCode = 302;
                this.file = indexFile.get();
            } else {
                statusCode = 400;
                this.file = null;
            }
        } else {
            statusCode = 400;
            this.file = null;
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

    private String get302(String location) {
        return "HTTP/1.1 302 Found\r\n" +
                "Location: " + location;
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

    public static byte[] getServerInfo() {
        String serverInfo = "HTTP/1.1 200 OK\r\n" +
                "Content-Type: text/html\r\n" +
                "Content-Length: 310\r\n" +
                "\r\n" +
                "<!DOCTYPE html>\n" +
                "<html>\n" +
                "<head>\n" +
                "\t<title>HTTPServer</title>\n" +
                "</head>\n" +
                "<body>\n" +
                "\t<h1><a href=\"https://github.com/theManikJindal/httpserver\">" +
                "Multi-Threaded File based HTTP Server</a></h1>\n" +
                "\t<p>This software is licensed under <strong>The MIT License (MIT) Copyright (c) 2016 theManikJindal" +
                "</strong>.</p>\n" +
                "\t\n" +
                "</body>\n" +
                "</html>";

        return serverInfo.getBytes();
    }
}
