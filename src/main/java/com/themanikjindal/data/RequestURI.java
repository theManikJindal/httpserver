package com.themanikjindal.data;


import java.io.*;
import java.util.Optional;
import java.util.StringTokenizer;

public class RequestURI {
    public static Optional<String> get(InputStream in) throws IOException {

        String reqFirstLine = new BufferedReader(new InputStreamReader(in)).readLine();
        if(reqFirstLine == null) {
            // probably browser optimization
            // see this: http://stackoverflow.com/a/5734486/1958893
            return Optional.empty();
        }

        StringTokenizer tokenizer = new StringTokenizer(reqFirstLine, " ");
        tokenizer.nextToken();
        return Optional.of(java.net.URLDecoder.decode(tokenizer.nextToken(), "UTF-8"));
    }
}
