package com.yufeng.util;

/**
 * 字符串工具类
 *
 * @author Wensen Ma
 */
public class StringUtil {

    /**
     * 判断是否是空
     *
     * @param str
     * @return
     */
    public static boolean isEmpty(String str) {
		return str == null || "".equals(str.trim());
    }

    /**
     * 判断是否不是空
     *
     * @param str
     * @return
     */
    public static boolean isNotEmpty(String str) {
		return (str != null) && !"".equals(str.trim());
    }


    /**
     * 生成四位编号
     *
     * @param code
     * @return
     */
    public static String formatCode(String code) {
        try {
            int length = code.length();
            Integer num = Integer.valueOf(code.substring(length - 4, length)) + 1;
            String codenum = num.toString();
            int codelength = codenum.length();
            for (int i = 4; i > codelength; i--) {
                codenum = "0" + codenum;
            }
            return codenum;
        } catch (NumberFormatException e) {
            return "0100";
        }
    }

}
