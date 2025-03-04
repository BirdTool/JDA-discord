package bot.base;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class ClassScanner {

    public static List<Class<?>> getClassesWithAnnotation(String packageName, Class annotation) {
        List<Class<?>> classes = new ArrayList<>();
        try {
            ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
            String path = packageName.replace('.', '/');
            Enumeration<URL> resources = classLoader.getResources(path);

            while (resources.hasMoreElements()) {
                URL resource = resources.nextElement();
                if (resource.getProtocol().equals("jar")) {
                    // Lidar com recursos dentro de um .jar
                    String jarPath = resource.getPath().substring(5, resource.getPath().indexOf("!"));
                    try (JarFile jar = new JarFile(jarPath)) {
                        Enumeration<JarEntry> entries = jar.entries();
                        while (entries.hasMoreElements()) {
                            JarEntry entry = entries.nextElement();
                            String entryName = entry.getName();
                            if (entryName.startsWith(path) && entryName.endsWith(".class")) {
                                String className = entryName.replace('/', '.').substring(0, entryName.length() - 6);
                                loadClass(className, annotation, classes);
                            }
                        }
                    }
                } else {
                    // Lidar com sistema de arquivos (fora de .jar, como em desenvolvimento)
                    java.io.File directory = new java.io.File(resource.toURI());
                    scanDirectory(packageName, directory, classes, annotation);
                }
            }
        } catch (IOException | java.net.URISyntaxException e) {
            e.printStackTrace();
        }
        return classes;
    }

    private static void scanDirectory(String packageName, java.io.File directory, List<Class<?>> classes, Class annotation) {
        if (!directory.exists()) return;

        java.io.File[] files = directory.listFiles();
        if (files == null) return;

        for (java.io.File file : files) {
            if (file.isDirectory()) {
                scanDirectory(packageName + "." + file.getName(), file, classes, annotation);
            } else if (file.getName().endsWith(".class")) {
                String className = packageName + '.' + file.getName().substring(0, file.getName().length() - 6);
                loadClass(className, annotation, classes);
            }
        }
    }

    private static void loadClass(String className, Class annotation, List<Class<?>> classes) {
        try {
            Class<?> clazz = Class.forName(className);
            if (clazz.isAnnotationPresent(annotation)) {
                classes.add(clazz);
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}