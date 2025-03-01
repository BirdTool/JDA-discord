package bot.base;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

public class ClassScanner {

    public static List<Class<?>> getClassesWithAnnotation(String packageName, Class annotation) {
        List<Class<?>> classes = new ArrayList<>();
        try {
            // Obtém o ClassLoader atual
            ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
            String path = packageName.replace('.', '/');

            // Obtém todos os recursos no pacote
            Enumeration<URL> resources = classLoader.getResources(path);

            while (resources.hasMoreElements()) {
                URL resource = resources.nextElement();
                File file = new File(resource.toURI());

                // Escaneia o diretório em busca de classes
                scanDirectory(packageName, file, classes, annotation);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return classes;
    }

    private static void scanDirectory(String packageName, File directory, List<Class<?>> classes, Class annotation) {
        if (!directory.exists()) return;

        File[] files = directory.listFiles();
        if (files == null) return;

        for (File file : files) {
            if (file.isDirectory()) {
                // Recursão para subdiretórios
                scanDirectory(packageName + "." + file.getName(), file, classes, annotation);
            } else if (file.getName().endsWith(".class")) {
                // Remove a extensão .class e obtém o nome da classe
                String className = packageName + '.' + file.getName().substring(0, file.getName().length() - 6);

                try {
                    // Carrega a classe
                    Class<?> clazz = Class.forName(className);

                    // Verifica se a classe tem a anotação
                    if (clazz.isAnnotationPresent(annotation)) {
                        classes.add(clazz);
                    }
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}