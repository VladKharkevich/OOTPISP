package pluginUtils;

import java.io.File;
import java.io.FileFilter;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class PlugLoader {

    public ArrayList<Class> getPlugins() {
        ArrayList<Class> result = new ArrayList<>();
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
                    String ext = getFileExtension(name).toLowerCase();
                    if (ext.equals("class")) {
                        String className = name.substring(0, name.length() - 6);
                        className = className.replace('/', '.');
                        Class thisClass = classLoader.loadClass(className);
                        if (thisClass.isInterface()) {
                            continue;
                        }
                        Class<?>[] interfaces = thisClass.getInterfaces();
                        for (Class<?> c : interfaces) {

                            if (c.getName().equals("PlugInterface")) {
                                System.out.println(thisClass.getSimpleName());
                                result.add(thisClass);
                                break;
                            }
                        }
                    }
                }
                jarFile.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    private String getFileExtension(String name) {
        int index = name.lastIndexOf('.') + 1;
        if (index > 0) {
            String extension = name.substring(index);
            return extension;
        }
        return "";
    }
}