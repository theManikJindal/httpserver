package com.themanikjindal.server;


import java.io.File;
import java.util.Optional;

public class FileManager {

    private final File rootDirectory;

    public FileManager(File rootDirectory) {
        this.rootDirectory = rootDirectory.getAbsoluteFile();

        if(!this.rootDirectory.exists() || !this.rootDirectory.isDirectory()) {
            throw new IllegalArgumentException("Cannot find directory: " + this.rootDirectory.getPath());
        }
    }

    public Optional<File> getFile(String requestUri) {
        if (requestUri.contains("../")) {
            return Optional.empty(); // 403
        }
        if (requestUri.indexOf("/") == 0) {
            requestUri = requestUri.substring(1);
        }
        return Optional.of(rootDirectory.toPath().resolve(requestUri).toFile());
    }
}
