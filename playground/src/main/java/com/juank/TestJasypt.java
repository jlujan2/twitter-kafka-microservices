package com.juank;

import org.jasypt.encryption.pbe.StandardPBEByteEncryptor;
import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.jasypt.iv.RandomIvGenerator;

public class TestJasypt {

    public static void main(String[] args) {

        StandardPBEStringEncryptor standardPBEByteEncryptor = new StandardPBEStringEncryptor();
        standardPBEByteEncryptor.setPassword("Demo_Pwd_2024");
        standardPBEByteEncryptor.setAlgorithm("PBEWITHHMACSHA512ANDAES_256");
        standardPBEByteEncryptor.setIvGenerator(new RandomIvGenerator());
        String result = standardPBEByteEncryptor.encrypt("springCloud_Pwd!");
        System.out.println(result);
        System.out.println(standardPBEByteEncryptor.decrypt(result));
    }

}
