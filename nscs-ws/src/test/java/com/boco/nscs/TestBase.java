package com.boco.nscs;

import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import static org.assertj.core.api.Assertions.*;

/**
 * Created by CC on 2017/6/6.
 * 测试基类
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
//默认回滚,即此类中的方法即使执行成功,数据也并不会真正的修改,方法执行后会回滚。
//因为对数据库的增删改都会回滚,因此便于测试用例的循环利用
//如果想看到数据库中的数据随着测试而发生变化可以去掉这个注解。
//@Rollback
//@Transactional
public abstract class TestBase {
}
