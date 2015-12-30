package com.epam.zhmyd;

import java.io.IOException;
import java.net.URISyntaxException;

public class App {
    public static void main(String[] args) throws InterruptedException, IOException, ClassNotFoundException, NoSuchMethodException, URISyntaxException {
        String path = args[0];
        if (path != null) {
            Performer performer = new Performer(path);
            performer.showPlugins();
        }



    }
}
