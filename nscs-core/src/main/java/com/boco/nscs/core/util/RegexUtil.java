package com.boco.nscs.core.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by cyl on 2017/9/21.
 */
public class RegexUtil {
    //
    private static String REGEX_TEL = "\\d{3}-\\d{8}|\\d{4}-\\{7,8}";
    //手机号
    private static String REGEX_PHONE = "0?(13|14|15|18|17)[0-9]{9}";
    //验证时间字符串格式，是否有效日期，从1900开始 yyyy-MM-dd HH:mm:ss
    private static String REGEX_DATE = "(19[0-9]{2}|[2-9]{1}[0-9]{3})-(((0[13578]|1[02])-(0[1-9]|[12][0-9]|3[01]))|((0[469]|11)-(0[1-9]|[12][0-9]|30))|(02-(0[1-9]|[1][0-9]|2[0-8])))\\W(0[1-9]|1[0-9]|2[0-3])(:[0-5][0-9]){2}";

    private static String REGEX_STR = "\\W*";
    private static String REGEX_NUM = "-?(0|[1-9]{1,}0*(.[0-9]{1,})?";

    public static int TYPE_NOT_NULL = 0;//不允许null
    public static int TYPE_NULL = 1;//允许空

    private static boolean match(String value,String regex){
        if(value != null){
            Pattern pattern = Pattern.compile(regex);
            Matcher m = pattern.matcher(value);
            if(m.matches()){
                return true;
            }
        }
        return false;
    }

    public static boolean isTel(String tel,int type){
        if(type == TYPE_NULL && tel == null){
            return true;
        }
        return match(tel,REGEX_TEL);
    }

    public static boolean isPhone(String phone){

        return match(phone,REGEX_PHONE);
    }
    public static boolean isPhone(String phone,int type){
        if(type == TYPE_NULL && phone == null){
            return true;
        }
        return match(phone,REGEX_PHONE);
    }

    public static boolean isDate(String date,int type){
        if(type == TYPE_NULL && date == null){
            return true;
        }
        return match(date,REGEX_DATE);
    }

    public static boolean isNotDate(String date,int type){
        return !isDate(date,type);
    }

    public static boolean isBlank(String str){
        if(str == null || match(str,REGEX_STR)){
            return true;
        }
        return false;
    }

    public static boolean isNotBlank(String str){
        return !isBlank(str);
    }

    public static boolean isNumber(String num,int type){
        if(num == null && type == TYPE_NULL){
            return true;
        }

        return match(num,REGEX_NUM);
    }


}
