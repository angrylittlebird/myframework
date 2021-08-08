package com.learning.bean;

import com.learning.core.annotation.Autowired;
import com.learning.core.annotation.Component;
import lombok.Getter;

/**
 * @Author: ZHANG
 * @Date: 2021/8/8
 * @Description:
 */
@Component
@Getter
public class PeopeImpl implements PeopleService {
    @Autowired
    private ChinesePeople chinesePeople;
}
