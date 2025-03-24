package com.discord.bot.base.register.commands;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import net.dv8tion.jda.api.interactions.commands.build.CommandData;

public class RegisterCommands {
    
    private static final String FEATURES_PACKAGE = "com.discord.bot.features.commands";
    private static final String METHOD_NAME = "getCommandData";
    
    /**
     * Scans all classes in the features package and subpackages to find those with getCommandData method
     * @return List of CommandData objects from all feature classes
     */
    public static List<CommandData> getAllCommandData() {
        List<CommandData> commandDataList = new ArrayList<>();
        List<Class<?>> classes = findAllClassesWithCommandData();
        
        for (Class<?> clazz : classes) {
            try {
                Method method = clazz.getDeclaredMethod(METHOD_NAME);
                Object instance = clazz.getDeclaredConstructor().newInstance();
                Object result = method.invoke(instance);
                
                if (result instanceof CommandData) {
                    commandDataList.add((CommandData) result);
                    System.out.println("Registered command: " + ((CommandData) result).getName() + " from class: " + clazz.getName());
                } else if (result instanceof List) {
                    @SuppressWarnings("unchecked")
                    List<CommandData> resultList = (List<CommandData>) result;
                    commandDataList.addAll(resultList);
                    System.out.println("Registered " + resultList.size() + " commands from class: " + clazz.getName());
                }
            } catch (Exception e) {
                System.err.println("Error getting command data from class: " + clazz.getName());
                e.printStackTrace();
            }
        }
        
        return commandDataList;
    }
    
    /**
     * Finds all classes in the features package that have the getCommandData method
     * @return List of classes with getCommandData method
     */
    private static List<Class<?>> findAllClassesWithCommandData() {
        List<Class<?>> result = new ArrayList<>();
        String packagePath = FEATURES_PACKAGE.replace('.', '/');
        
        try {
            // Check if we're running from a JAR
            URL resource = RegisterCommands.class.getClassLoader().getResource(packagePath);
            if (resource == null) {
                System.err.println("Could not find features package: " + FEATURES_PACKAGE);
                return result;
            }
            
            if (resource.getProtocol().equals("jar")) {
                // Running from JAR
                String jarPath = resource.getPath().substring(5, resource.getPath().indexOf("!"));
                scanJarForClasses(jarPath, packagePath, result);
            } else {
                // Running from file system
                Path featuresDir = Paths.get(resource.toURI());
                scanDirectoryForClasses(featuresDir, FEATURES_PACKAGE, result);
            }
        } catch (Exception e) {
            System.err.println("Error scanning for command classes");
            e.printStackTrace();
        }
        
        return result;
    }
    
    /**
     * Scans a directory recursively for Java class files
     */
    private static void scanDirectoryForClasses(Path directory, String packageName, List<Class<?>> result) throws IOException {
        try (Stream<Path> paths = Files.walk(directory)) {
            paths.filter(Files::isRegularFile)
                 .filter(path -> path.toString().endsWith(".class"))
                 .forEach(path -> {
                     // Extrair o caminho relativo ao diretório base
                     Path relativePath = directory.relativize(path);
                     String className = packageName;
                     
                     // Construir o nome da classe completo
                     if (relativePath.getNameCount() > 1) {
                         // Se estiver em uma subpasta, adicione o caminho da subpasta
                         for (int i = 0; i < relativePath.getNameCount() - 1; i++) {
                             className += "." + relativePath.getName(i);
                         }
                     }
                     
                     // Adicione o nome do arquivo (sem a extensão .class)
                     String fileName = relativePath.getFileName().toString();
                     className += "." + fileName.substring(0, fileName.length() - 6);
                     
                     tryAddClassWithCommandData(className, result);
                 });
        }
    }
    
    /**
     * Scans a JAR file for classes in the specified package
     */
    private static void scanJarForClasses(String jarPath, String packagePath, List<Class<?>> result) throws IOException {
        try (JarFile jarFile = new JarFile(jarPath)) {
            Enumeration<JarEntry> entries = jarFile.entries();
            
            while (entries.hasMoreElements()) {
                JarEntry entry = entries.nextElement();
                String entryName = entry.getName();
                
                if (entryName.startsWith(packagePath) && entryName.endsWith(".class")) {
                    String className = entryName.substring(0, entryName.length() - 6).replace('/', '.');
                    tryAddClassWithCommandData(className, result);
                }
            }
        }
    }
    
    /**
     * Tries to load a class and check if it has the getCommandData method
     */
    private static void tryAddClassWithCommandData(String className, List<Class<?>> result) {
        try {
            Class<?> clazz = Class.forName(className);
            
            // Check if the class has the getCommandData method
            try {
                clazz.getDeclaredMethod(METHOD_NAME);
                result.add(clazz);
                System.out.println("Found command class: " + className);
            } catch (NoSuchMethodException e) {
                // Class doesn't have the method, skip it
            }
        } catch (ClassNotFoundException e) {
            System.err.println("Could not load class: " + className);
        }
    }
}
