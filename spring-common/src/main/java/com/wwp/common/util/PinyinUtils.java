package com.wwp.common.util;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.HanyuPinyinVCharType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;

public class PinyinUtils {
    /**
     * 将汉字转换为全拼
     * @param src
     * @return String
     */
    public static String getPinYin(String src) {
        char[] t1 = null;
        t1 = src.toCharArray();
        String[] t2 = new String[t1.length];
        // 设置汉字拼音输出的格式
        HanyuPinyinOutputFormat t3 = new HanyuPinyinOutputFormat();
        t3.setCaseType(HanyuPinyinCaseType.LOWERCASE);//小写
        t3.setToneType(HanyuPinyinToneType.WITHOUT_TONE);//不带音标
        t3.setVCharType(HanyuPinyinVCharType.WITH_V);
        StringBuilder t4 = new StringBuilder();
        try {
            for (char c : t1) {
                // 判断是否为汉字字符
                if (Character.toString(c).matches("[\\u4E00-\\u9FA5]+")) {
                    t2 = PinyinHelper.toHanyuPinyinStringArray(c, t3);// 将汉字的几种全拼都存到t2数组中
                    if (t2 != null) {//此处添加判断，因为个别汉字转换拼音时出现异常，转换失败
                        t4.append(t2[0]);// 取出该汉字全拼的第一种读音并连接到字符串t4后
                    } else {
                        throw new IllegalArgumentException("转换拼音异常汉字：" + c);
                    }
                } else {
                    // 如果不是汉字字符，直接取出字符并连接到字符串t4后
                    t4.append(c);
                }
            }
        } catch (BadHanyuPinyinOutputFormatCombination e) {
            e.printStackTrace();
        }
        return t4.toString();
    }

    /**
     * 提取每个汉字的首字母
     *
     * @param str
     * @return String
     */
    public static String getPinYinHeadChar(String str) {
        if(oConvertUtils.isEmpty(str)) return null;
        StringBuilder convert = new StringBuilder();
        for (int j = 0; j < str.length(); j++) {
            char word = str.charAt(j);
            // 提取汉字的首字母
            String[] pinyinArray = PinyinHelper.toHanyuPinyinStringArray(word);
            if (pinyinArray != null) {
                convert.append(pinyinArray[0].charAt(0));
            } else {
                convert.append(word);
            }
        }
        return convert.toString().toLowerCase();
    }

    /**
     * 将字符串转换成ASCII码
     *
     * @param cnStr
     * @return String
     */
    public static String getCnASCII(String cnStr) {
        StringBuilder strBuf = new StringBuilder();
        // 将字符串转换成字节序列
        byte[] bGBK = cnStr.getBytes();
        for (byte b : bGBK) {
            // 将每个字符转换成ASCII码
            strBuf.append(Integer.toHexString(b & 0xff)).append(" ");
        }
        return strBuf.toString();
    }
}
