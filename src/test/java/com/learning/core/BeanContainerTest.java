package com.learning.core;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.net.URISyntaxException;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @Author: ZHANG
 * @Date: 2021/8/8
 * @Description:
 */
class BeanContainerTest {
    private static BeanContainer container;

    @BeforeAll
    static void init() {
        container = BeanContainer.getSingleInstance();
    }

    @Test
    void loadBean() throws URISyntaxException {
        assertFalse(container.isLoaded());
        container.loadBean("com.learning.bean");
        assertTrue(container.isLoaded());
        assertEquals(1, container.getSize());
    }
}