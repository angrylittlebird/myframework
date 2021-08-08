package com.learning.util;

import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.HashSet;
import java.util.Set;

/**
 * @Author: ZHANG
 * @Date: 2021/8/7
 * @Description:
 */
@Slf4j
public class ClassUtil {

    private static final String FILE_PROTOCAL = "file";

    public static Set<Class<?>> extractPackageClass(String packageName) {
        ClassLoader contextClassLoder = getClassLoader();
        URL url = contextClassLoder.getResource(packageName.replace(".", File.separator));
        Set<Class<?>> classSet = new HashSet<>();

        if (FILE_PROTOCAL.equalsIgnoreCase(url.getProtocol())) {
            //  /E:/myworkspace/simpleframework/target/classes/com/learning
            String path = null;
            try {
                path = url.toURI().getPath();
            } catch (URISyntaxException e) {
                log.warn("can not get path from:" + packageName);
                throw new RuntimeException(e);
            }
            File file = new File(path);

            extractClassHelper(classSet, packageName, file);
        }

        return classSet;
    }


    public static void setFieldValue(Object target, Object value, Field field, boolean accessible) {
        try {
            field.setAccessible(accessible);
            field.set(target, value);
        } catch (IllegalAccessException e) {
            log.warn("Failed to set value to field.", e);
            throw new RuntimeException(e);
        }

    }

    private static void extractClassHelper(Set<Class<?>> emptyClass, String packageName, File curFile) {
        if (curFile.isFile() && curFile.getName().endsWith(".class")) {
            try {
                emptyClass.add(extracFileClass(packageName, curFile));
            } catch (ClassNotFoundException e) {
                log.error("class path not right.", e);
            }
            return;
        }

        File[] files = curFile.listFiles();
        for (File f : files) {
            extractClassHelper(emptyClass, packageName, f);
        }

        return;
    }

    private static Class<?> extracFileClass(String packageName, File curFile) throws ClassNotFoundException {
        String absolutePath = curFile.getAbsolutePath().replace(File.separator, ".");
        return Class.forName(absolutePath.substring(absolutePath.indexOf(packageName), absolutePath.lastIndexOf(".")));
    }

    public static ClassLoader getClassLoader() {
        return Thread.currentThread().getContextClassLoader();
    }

    public static void main(String[] args) throws ClassNotFoundException {
        ClassLoader classLoader = ClassUtil.getClassLoader();
        URL resource = classLoader.getResource("com/learning");
        String path = resource.getPath();

        Class<?> aClass = Class.forName("com.learning.util.ClassUtil");
        System.out.println(aClass);

        ClassUtil.extractPackageClass("com.learning.util");
    }

    public static <T> T newInstance(Class<?> clazz, boolean accessible) {
        try {
            Constructor constructor = clazz.getDeclaredConstructor();
            constructor.setAccessible(accessible);
            return (T) constructor.newInstance();
        } catch (Exception e) {
            log.warn("new instance error", e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
    }
}
