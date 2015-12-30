package com.epam.zhmyd;

import com.epam.zhmyd.plugin.Plugin;

import java.io.File;
import java.io.FileInputStream;
import java.lang.reflect.Method;
import java.net.MalformedURLException;


public class PluginLoader {

    private static final String DEFINE_CLASS = "defineClass";
    private static final String RESOLVE_CLASS = "resolveClass";

    private Method resolve;
    private Method define;

    public PluginLoader() throws NoSuchMethodException {
        Class c = ClassLoader.class;
        define = c.getDeclaredMethod(DEFINE_CLASS, String.class, byte[].class, int.class, int.class);
        resolve = c.getDeclaredMethod(RESOLVE_CLASS, Class.class);
        define.setAccessible(true);
        resolve.setAccessible(true);
    }

    public Plugin loadPlugin(File file) throws MalformedURLException {
        Plugin plugin = null;
        FileInputStream reader;
        Class<?> clazz;
        Object instance;

        try {
            reader = new FileInputStream(file);
            byte[] b = new byte[reader.available()];
            reader.read(b);
            reader.close();
            ClassLoader loader = ClassLoader.getSystemClassLoader();
            clazz = (Class<?>) define.invoke(loader, null, b, 0, b.length);
            resolve.invoke(loader, clazz);
            instance = clazz.newInstance();

            if (instance instanceof Plugin) {
                plugin = (Plugin) instance;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return plugin;
    }
}
