package com.learning.core;

import com.learning.core.annotation.Component;
import com.learning.core.annotation.Controller;
import com.learning.util.ClassUtil;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.lang.annotation.Annotation;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author: ZHANG
 * @Date: 2021/8/8
 * @Description:
 */
@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class BeanContainer {
    private static final ConcurrentHashMap<Class<?>, Object> beanMap = new ConcurrentHashMap<>();
    private static final List<Class<? extends Annotation>> BEAN_ANNOTATION = Arrays.asList(Component.class, Controller.class);
    private boolean isLoaded;
    private int size;

    public Object getInstanceByClass(Class<?> clazz) {
        return beanMap.get(clazz);
    }

    public int getSize() {
        return beanMap.size();
    }

    public Set<Class<?>> getSubClass(Class<?> superClass) {
        Objects.nonNull(superClass);

        HashSet<Class<?>> classSet = new HashSet<>();
        for (Class<?> clazz : getAllClasses()) {
            if (superClass.isAssignableFrom(clazz) && !superClass.equals(clazz)) {
                classSet.add(clazz);
            }
        }

        return classSet;
    }

    public boolean isLoaded() {
        return isLoaded;
    }

    public void loadBean(String scanPath) {
        if (isLoaded()) {
            log.info("already loaded bean.");
            return;
        }
        Set<Class<?>> classSet = ClassUtil.extractPackageClass(scanPath);
        if (classSet.isEmpty()) log.warn("extract nothing from path{}", scanPath);

        for (Class<?> clazz : classSet) {
            for (Class annotationClass : BEAN_ANNOTATION) {
                boolean initToBean = clazz.isAnnotationPresent(annotationClass);
                if (initToBean) {
                    beanMap.put(clazz, ClassUtil.newInstance(clazz, true));
                }
            }
        }

        isLoaded = true;
    }

    public Set<Class<?>> getAllClasses() {
        return beanMap.keySet();
    }


    private enum InstanceHolder {
        HOLDER;
        private final BeanContainer instance;

        InstanceHolder() {
            instance = new BeanContainer();
        }
    }

    public static BeanContainer getSingleInstance() {
        return InstanceHolder.HOLDER.instance;
    }


}
