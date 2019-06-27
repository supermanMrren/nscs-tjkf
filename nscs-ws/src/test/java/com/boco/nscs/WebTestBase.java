package com.boco.nscs;


import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.net.URL;
/**
 * Created by CC on 2017/6/6.
 * 网站测试配置
 */
@AutoConfigureMockMvc
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(webEnvironment= SpringBootTest.WebEnvironment.RANDOM_PORT)
public abstract class WebTestBase {
    @LocalServerPort
    private int port;
    protected URL base;

    @Before
    public void setUp() throws Exception {
        this.base = new URL("http://localhost:"+port+"/");

        System.out.println("url:"+this.base.toString());
    }
}

