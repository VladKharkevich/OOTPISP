package pluginUtils;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Enumeration;
import java.util.Scanner;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class PluginUtils {

    public static void plugSerialize(String filename, Class cls){
        FileInputStream fin = null;
        try {
            fin = new FileInputStream("temp");

            byte[] buffer = new byte[fin.available()];
            fin.read(buffer, 0, fin.available());
            fin.close();
            File temp = new File("temp");
            temp.delete();

            Method plugMethod = cls.getMethod("code", byte[].class);
            Object obj = cls.newInstance();
            String clsName = cls.getName().substring(cls.getName().lastIndexOf(".") + 1);
            String result = (String) plugMethod.invoke(obj, buffer);

            FileWriter writer = new FileWriter(filename, false);
            writer.write(clsName);
            writer.append('\n');
            writer.write(result);
            writer.flush();
            writer.close();
        } catch (IllegalAccessException | IOException | InvocationTargetException | NoSuchMethodException | InstantiationException e) {
            e.printStackTrace();
        }
    }

    public static void plugDeserialize(String filename, String plug){
        try {
            FileReader fileReader = new FileReader(filename);
            Scanner sc = new Scanner(fileReader);
            sc.nextLine();
            String data = sc.nextLine();
            Class cls = getClassFromPlugins(plug);
            Method method = cls.getMethod("decode", String.class);
            Object obj = cls.newInstance();
            byte[] terminators = (byte[]) method.invoke(obj, data);

            FileOutputStream fos = new FileOutputStream("temp");
            fos.write(terminators, 0, terminators.length);
            fos.close();

        } catch (IOException | InvocationTargetException | NoSuchMethodException | IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }
    }

    public static Class getClassFromPlugins(String pluginName) {
        Class cls = null;
        File pluginDir = new File("plugins");
        File[] jars = pluginDir.listFiles(new FileFilter() {
            public boolean accept(File file) {
                return file.isFile() && file.getName().endsWith(".jar");
            }
        });
        for (File f : jars) {
            try {
                JarFile jarFile = new JarFile(f);
                Enumeration<JarEntry> entries = jarFile.entries();
                URL path = f.toURI().toURL();
                URLClassLoader classLoader = new URLClassLoader(new URL[]{path});
                while (entries.hasMoreElements()) {
                    JarEntry entry = entries.nextElement();
                    if (entry.isDirectory()) {
                        continue;
                    }
                    String name = entry.getName();
                    String ext = "";
                    int index = name.lastIndexOf('.') + 1;
                    if (index > 0) {
                        String extension = name.substring(index);
                        ext = extension;
                    }
                    if (ext.equals("class")) {
                        String className = name.substring(0, name.length() - 6);
                        className = className.replace('/', '.');
                        if (className.equals(pluginName)) {
                            cls = classLoader.loadClass(className);
                            if (cls.isInterface()) {
                                continue;
                            }
                            return cls;
                        }
                    }
                }
                jarFile.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return cls;
    }

    public static String checkPlugin(String filename, String attribute) {
        String plug = null;
        try {
            FileReader fileReader = new FileReader(filename);
            Scanner sc = new Scanner(fileReader);
            String data = sc.nextLine();
            if (data.substring(0, attribute.length()).equals(attribute)){
                return null;
            }
            else{
                plug = data;
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return plug;
    }

    public static String checkPlugin(String filename, byte[] attribute) {
        FileInputStream fin = null;
        String plug = null;
        try {
            fin = new FileInputStream(filename);
            byte[] buffer = new byte[attribute.length];
            fin.read(buffer, 0, attribute.length);
            fin.close();
            Boolean isPlugin = false;
            for (int i = 0; i < attribute.length; i++){
                if (attribute[i] != buffer[i]) {
                    isPlugin = true;
                }
            }
            if (!isPlugin){
                return null;
            }
            FileReader reader = new FileReader(filename);
            Scanner sc = new Scanner(reader);
            plug = sc.nextLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return plug;
    }

}
