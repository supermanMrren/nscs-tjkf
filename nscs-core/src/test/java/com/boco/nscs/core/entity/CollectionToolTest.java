package com.boco.nscs.core.entity;

import cn.hutool.core.collection.CollectionUtil;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by CC on 2017/9/14.
 */
public class CollectionToolTest {

    List<SelectItem> alllists;

    @Before
    public void setup(){
        alllists = new ArrayList<>();
        alllists.add(new SelectItem("01","name1","test"));
        alllists.add(new SelectItem("02","name2","test"));
        alllists.add(new SelectItem("03","name3","test"));
        alllists.add(new SelectItem("04","name4","test"));
        alllists.add(new SelectItem("05","name5","test"));
        alllists.add(new SelectItem("04","city1","City"));
    }

    @Test
    public void testFindbyKey(){
        String testCode = "05";
        SelectItem item = CollectionUtil.findOneByField(alllists, "code", testCode);
        assertThat(item).isNotNull();
        assertThat(item.getCode()).isEqualTo(testCode);

        String testcode2="04";
        String testType="City";
        SelectItem item1 = CollectionUtil.findOne(alllists, t -> t.getCode().equalsIgnoreCase(testcode2) && t.getType().equalsIgnoreCase(testType));

        assertThat(item1).isNotNull();
        assertThat(item1.getCode()).isEqualTo(testcode2);
        assertThat(item1.getType()).isEqualTo(testType);

    }
}
