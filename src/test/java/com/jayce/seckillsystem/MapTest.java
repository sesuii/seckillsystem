package com.jayce.seckillsystem;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Map;

/**
 * TODO
 *
 * @author <a href="mailto:su_1999@126.com">sujian</a>
 * @date 2021-10-23
 */
@SpringBootTest
public class MapTest {
    @Autowired
    private Map<String, Object> map;

    @Test
    public void test() {
        System.out.println(map.getClass());
    }
}
