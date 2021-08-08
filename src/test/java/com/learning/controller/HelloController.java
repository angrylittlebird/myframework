package com.learning.controller;

import com.learning.bean.PeopleService;
import com.learning.core.annotation.Autowired;
import com.learning.core.annotation.Controller;
import lombok.Getter;

/**
 * @Author: ZHANG
 * @Date: 2021/8/8
 * @Description:
 */
@Controller
@Getter
public class HelloController {
    @Autowired("PeopeImpl")
    private PeopleService peopleService;
}
