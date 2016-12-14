package com.dingdong.sys.service.impl;

import java.util.Random;

/**
 * 验证码生成器，默认是生成6位数的验证码
 * 
 * @author niukai
 * 
 */
public class CodeGenerator {

    public static int DEFAULT_NUM = 6;
    private static final Random CODE_GENERATOR = new Random();

    /**
     * 默认6位验证码
     * 
     * @return
     */
    public static String genCode() {
        return genCode(DEFAULT_NUM);
    }

    /**
     * 获取验证码
     * <p>
     * 采用简单算法<br>
     * <pre>{@code
     * char[] codes = new char[num];
     * for (int i = 0; i < num; i++) {
     *      int code = CODE_GENERATOR.nextInt(10) + '0';
     *      codes[i] = (char) code;
     * }
     * return new String(codes);
     * }
     * 
     * @param num 验证码位数
     * @return
     */
    public static String genCode(int num) {
        char[] codes = new char[num];
        for (int i = 0; i < num; i++) {
            int code = CODE_GENERATOR.nextInt(10) + '0';
            codes[i] = (char) code;
        }
        return new String(codes);
    }
}
