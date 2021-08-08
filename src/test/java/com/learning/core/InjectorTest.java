package com.learning.core;

import com.learning.bean.PeopeImpl;
import com.learning.controller.HelloController;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * @Author: ZHANG
 * @Date: 2021/8/8
 * @Description:
 */
class InjectorTest {
    private static BeanContainer container;

    @BeforeAll
    static void init() {
        container = BeanContainer.getSingleInstance();
    }

    @Test
    void doIoc() throws NoSuchFieldException {
        container.loadBean("com.learning");
        HelloController helloController = (HelloController) container.getInstanceByClass(HelloController.class);
        assertNotNull(helloController);
        new Injector().doIoc();
        assertNotNull(helloController.getPeopleService());
        assertNotNull(((PeopeImpl) helloController.getPeopleService()).getChinesePeople());
    }
}