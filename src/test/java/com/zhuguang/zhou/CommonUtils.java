package com.zhuguang.zhou;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class CommonUtils {

    public static void add (List<Long> li) {
        try {
            int code = 20;
            code ++;
            for (int i = 0; i < li.size() ; i++) {
                 System.out.println("值" + li);
            }
            TimeUnit.SECONDS.sleep(1);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
