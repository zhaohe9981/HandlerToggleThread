package com.xiaoniu.handlertest;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtil {
    public static String fortmatTime(long time){
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return format.format(new Date(time));
    }
}
