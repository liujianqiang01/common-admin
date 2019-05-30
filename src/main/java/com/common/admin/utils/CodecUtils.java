package com.common.admin.utils;


import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.StringUtils;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

/**
 * @author chen.da
 * @date 2017/8/16.
 */
public class CodecUtils {

    private static final String AES_METHOD = "AES";
    private static final String AES_CIPHER = "AES/ECB/PKCS5Padding";

    private CodecUtils() {
    }

    public static String aesEncode(String data, String encryptKey) throws Exception {
        byte[] aesKey = StringUtils.getBytesUtf8(encryptKey);
        SecretKeySpec key = new SecretKeySpec(aesKey, AES_METHOD);
        Cipher cipher = Cipher.getInstance(AES_CIPHER);
        byte[] byteContent = data.getBytes("utf-8");
        cipher.init(Cipher.ENCRYPT_MODE, key);
        byte[] result = cipher.doFinal(byteContent);
        return Base64.encodeBase64String(result);
    }

    public static String aesDecode(String data, String encryptKey) throws Exception {
        byte[] aesKey = StringUtils.getBytesUtf8(encryptKey);
        SecretKeySpec key = new SecretKeySpec(aesKey, AES_METHOD);
        Cipher cipher = Cipher.getInstance(AES_CIPHER);
        cipher.init(Cipher.DECRYPT_MODE, key);
        return StringUtils.newStringUtf8(cipher.doFinal(Base64.decodeBase64(data)));
    }

}
