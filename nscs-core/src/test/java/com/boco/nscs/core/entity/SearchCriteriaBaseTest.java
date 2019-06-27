package com.boco.nscs.core.entity;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by CC on 2017/8/11.
 */
public class SearchCriteriaBaseTest {
    @Test
    public void getPageIndex() throws Exception {
        SearchCriteria param = new SearchCriteria();
        EasyUIPager pageInfo1 = new EasyUIPager(1,10);
        param.setPager(pageInfo1);
        assertThat(param).isNotNull();
        assertThat(param.getPageIndex()).isEqualTo(1);
        assertThat(param.getSkip()).isEqualTo(0);

        EasyUIPager pageInfo2 = new EasyUIPager(2,10);

        param.setPager(pageInfo2);
        assertThat(param.getSkip()).isEqualTo(10);
    }

    @Test
    public void getSkip() throws Exception {
        SearchCriteria param = new SearchCriteria();
        DtPager pageinfo1 = new DtPager(0,10);

        param.setPager(pageinfo1);
        assertThat(param.getPageIndex()).isEqualTo(1);

        DtPager pageinfo2 = new DtPager(5,10);
        param.setPager(pageinfo2);
        assertThat(param.getPageIndex()).isEqualTo(1);

        DtPager pageinfo3 = new DtPager(11,10);
        param.setPager(pageinfo3);
        assertThat(param.getPageIndex()).isEqualTo(2);
    }

}