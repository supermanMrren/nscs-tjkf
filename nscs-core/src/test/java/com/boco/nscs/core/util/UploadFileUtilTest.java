package com.boco.nscs.core.util;

import org.junit.Test;

import static org.junit.Assert.*;

public class UploadFileUtilTest {
    @Test
    public void getBasePath() throws Exception {
        String path = UploadFileUtil.getBasePath();
        System.out.println(path);
    }

}