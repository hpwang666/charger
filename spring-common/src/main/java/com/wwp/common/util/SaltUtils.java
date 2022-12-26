package com.wwp.common.util;

import java.util.Random;

public class SaltUtils {
    /**
     * 生成salt的静态方法
     */
    public static String getSalt(int n){
        char[] chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz~!@#$%^&*()_+".toCharArray();
        int length = chars.length;
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < n; i++){
            char achar = chars[new Random().nextInt(length)];
            sb.append(achar);
        }
        return sb.toString();
    }

   /* public static void main(String[] args) {
        System.out.println(getSalt(8));
    }*/
}

