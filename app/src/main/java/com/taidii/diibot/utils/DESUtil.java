package com.taidii.diibot.utils;

import android.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class DESUtil {

    public static String decryptDES(String decryptString,String iv,String desKey){
        String CHAR_SET = "UTF-8";
        try {
            //先使用Base64解密
            byte[] byteMi = Base64.decode(decryptString,Base64.DEFAULT);

            //实例化IvParameterSpec对象使用指定的初始化向量
            IvParameterSpec zeroIv=new IvParameterSpec(iv.getBytes(CHAR_SET));

            //实例化SecretKeySpec，根据传入的密钥获得字节数组来构造SecretKeySpec,
            SecretKeySpec key =new SecretKeySpec(desKey.getBytes(),"DES");

            //创建密码器
            Cipher cipher=Cipher.getInstance("DES/CBC/PKCS5Padding");

            //用密钥初始化Cipher对象,上面是加密，这是解密模式
            cipher.init(Cipher.DECRYPT_MODE,key,zeroIv);

            //获取解密后的数据
            byte [] decryptedData=cipher.doFinal(byteMi);
            return new String(decryptedData);
        } catch (Exception e){

        }
        return null;
    }
}
