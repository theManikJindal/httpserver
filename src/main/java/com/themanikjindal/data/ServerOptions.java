package com.themanikjindal.data;


import joptsimple.OptionParser;
import joptsimple.OptionSet;

public class ServerOptions {
    public final int port;
    public final String bindAddress;

    public final String rootDirectory;

    public final int threads;

    public ServerOptions(String[] args) {
        OptionParser parser = new OptionParser("p:r:t:b:");
        OptionSet options = parser.parse(args);

        if(!(options.has("p") && options.has("b") && options.has("r") && options.has("t"))) {
            throw new IllegalArgumentException("Incorrect Usage, see readme.");
        }

        port = Integer.parseInt((String) options.valueOf("p"));
        bindAddress = (String) options.valueOf("b");
        rootDirectory = (String) options.valueOf("r");
        threads = Integer.parseInt((String) options.valueOf("t"));

        if(threads < 1) {
            throw new IllegalArgumentException("Thread count should be greater than 0");
        }

        System.out.println(
                "Hostname: " + bindAddress + "\n" +
                "Port: " + port + "\n" +
                "Root Directory: " + rootDirectory + "\n" +
                "Threads: " + threads + "\n"
        );
    }
}
