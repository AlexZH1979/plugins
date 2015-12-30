package com.epam.zhmyd;

import com.epam.zhmyd.plugin.Plugin;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class Performer {

    private static final String PROPERTIES_FILE = "info.properties";
    public static final String PLUGIN_INFO = "plugin.info";
    public static final String PLUGIN_CLASS = "plugin.class";
    public static final String JAR_POSTFIX = ".jar";
    public static final String CLASS_POSTFIX=".class";
    public static final String DOT=".";
    public static final String SLASH="/";

    private Map<String, Plugin> plugins = new HashMap<>();


    public Performer(String name) throws NoSuchMethodException, IOException, URISyntaxException {

        init(name);
    }

    public void showPlugins() throws IOException {
        for (String key : plugins.keySet()) {
            System.out.println("Plugin: " + key);
        }
    }

    public void runPlugin(String id) {
        Plugin plugin = plugins.get(id);
        if(plugin != null){
            plugin.init();
            plugin.apply();
        }
    }

    private void init(String path) throws NoSuchMethodException, IOException, URISyntaxException {
        File file = new File(path);
        if (file.isDirectory()) {
            for (File child : file.listFiles()) {
                parseFile(child);
            }
        } else {
            parseFile(file);
        }
    }

    private void parseFile(File file) throws NoSuchMethodException, IOException, URISyntaxException {
        if (file.isFile() && file.getName().endsWith(JAR_POSTFIX)) {

            Properties properties = new Properties();
            URLClassLoader classLoader = new URLClassLoader(new URL[]{file.toURL()});

            PluginLoader pluginLoader = new PluginLoader();
            InputStream url = classLoader.getResourceAsStream(PROPERTIES_FILE);
            if (url != null) {
                properties.load(url);

                String info = properties.getProperty(PLUGIN_INFO);
                String pluginPath = properties.getProperty(PLUGIN_CLASS).replace(DOT, SLASH) + CLASS_POSTFIX;

                URL is = classLoader.getResource(pluginPath);

                File pluginClass = new File(is.toURI());

                Plugin plugin = pluginLoader.loadPlugin(pluginClass);

                plugins.put(info, plugin);
            }
        }
    }
}
