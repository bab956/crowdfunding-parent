package xyz.newtouch.crowdfunding.utils;

import java.security.MessageDigest;

/**
 * MD5算法 哈希算法 MD5算法具有以下特点：
 * 1、压缩性：任意长度的数据，算出的MD5值长度都是固定的。
 * 2、容易计算：从原数据计算出MD5值很容易。
 * 3、抗修改性：对原数据进行任何改动，哪怕只修改1个字节，所得到的MD5值都有很大区别。
 * 4、强抗碰撞：已知原数据和其MD5值，想找到一个具有相同MD5值的数据（即伪造数据）是非常困难的。
 *
 * @author weibing
 */
public class MD5Util {
    public static String digest16(String inStr) {
        return digest(inStr, 16);
    }

    public static String digest(String inStr) {
        return digest(inStr, 32);
    }

    /**
     * 进行MD5加密的方法
     * @param inStr 需要加密的字符串
     * @param rang 加密后的长度
     * @return 加密结果
     */
    private static String digest(String inStr, int rang) {
        MessageDigest md5;
        if (StringUtil.isEmpty(inStr)) {
            return "";
        }

        try {
            // 获得MD5的加密对象(实例)
            md5 = MessageDigest.getInstance("MD5");
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }

        char[] charArray = inStr.toCharArray();
        byte[] byteArray = new byte[charArray.length];

        for (int i = 0; i < charArray.length; i++) {
            byteArray[i] = (byte) charArray[i];
        }

        // 使用MD5对字节数组中的内容进行加密
        byte[] md5Bytes = md5.digest(byteArray);

        StringBuilder hexValue = new StringBuilder();

        for (byte md5Byte : md5Bytes) {
            // 对每个内容进行16进制按位与运算
            int val = ((int) md5Byte) & 0xff;
            if (val < 16) {
                hexValue.append("0");
            }
            // 转为16进制
            hexValue.append(Integer.toHexString(val));
        }
        if (rang == 32) {
            // 返回32位加密内容
            return hexValue.toString();
        } else {
            // 返回16位加密内容
            return hexValue.substring(8, 24);
        }
    }

    public static void main(String[] args) {
        String s = "123";
        System.out.println(digest(s));
    }
}