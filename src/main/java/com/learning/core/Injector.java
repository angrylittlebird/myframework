package com.learning.core;

import com.learning.core.annotation.Autowired;
import com.learning.util.ClassUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;

import java.lang.reflect.Field;
import java.util.Set;

/**
 * @Author: ZHANG
 * @Date: 2021/8/8
 * @Description:
 */
@Slf4j
public class Injector {
    private final BeanContainer container;

    public Injector() {
        this.container = BeanContainer.getSingleInstance();
    }

    public void doIoc() {
        Set<Class<?>> classSet = container.getAllClasses();
        for (Class<?> clazz : classSet) {
            Field[] fields = clazz.getDeclaredFields();
            if (fields.length == 0) continue;

            Object curObject = container.getInstanceByClass(clazz);
            for (Field field : fields) {
                if (field.isAnnotationPresent(Autowired.class)) {
                    String qualifier = field.getAnnotation(Autowired.class).value();
                    Object fieldInstance = getFieldInstance(field.getType(), qualifier);
                    ClassUtil.setFieldValue(curObject, fieldInstance, field, true);
                }
            }
        }
    }

    private Object getFieldInstance(Class<?> fieldClass, String qualifier) {
        Object fieldInstance = container.getInstanceByClass(fieldClass);
        if (fieldInstance != null) return fieldInstance;

        //获取子类
        Set<Class<?>> subClassSet = container.getSubClass(fieldClass);
        if (subClassSet.size() == 1) {
            return container.getInstanceByClass(subClassSet.iterator().next());
        } else if (subClassSet.size() > 1) {
            if (StringUtils.isEmpty(qualifier))
                throw new RuntimeException("There are multiple implements of " + fieldClass.getSimpleName());

            for (Class<?> clazz : subClassSet) {
                if (clazz.getSimpleName().equals(qualifier)) {
                    return container.getInstanceByClass(clazz);
                }
            }

            throw new RuntimeException("Can not find implements of " + fieldClass.getSimpleName());

        } else {
            throw new RuntimeException("NO implements of" + fieldClass.getSimpleName());
        }
    }
}
