package com.zhuguang.zhou;

import com.zhuguang.zhou.pojo.ManPersion;
import com.zhuguang.zhou.pojo.Persion;

public class PersionTest {


    public static void main(String[] args) {
            Persion per = new ManPersion();
            per.setName("zhangshan");
            per.setSex("男");
            System.out.println(per.getName());
    }
}
