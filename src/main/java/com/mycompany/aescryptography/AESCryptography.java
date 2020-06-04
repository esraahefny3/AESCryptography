/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.aescryptography;

import com.mycompany.aescryptography.exceptions.BusinessException;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec; 

/**
 *
 * @author Esraa.Hefny
 */
public class AESCryptography  {

    private static org.apache.log4j.Logger mLog = org.apache.log4j.Logger.getLogger(AESCryptography.class);
    private static final String SECRET_KEY_FILE_PROP_NAME = "SECRET_KEY_FILE";
    private static final String IV_FILE_PROP_Name = "IV_FILE";
    private final static Charset CHAR_ENCODING=StandardCharsets.UTF_8;
    private final static String PROERTY_FILE_NAME ="cryptography.properties";
    private static IvParameterSpec generateIV()  {
        try{
        SecureRandom sRandom = new SecureRandom();
        byte[] iv = new byte[128 / 8];
        sRandom.nextBytes(iv);
        IvParameterSpec ivspec = new IvParameterSpec(iv);
          String fileName=loadFileNameFromPropertyFile(IV_FILE_PROP_Name);
          URL fileURL = AESCryptography.class.getResource(fileName);
       try (FileOutputStream out = new FileOutputStream(fileURL.getFile())) {
            out.write(iv);
        }
        return ivspec;
        }
        catch(Exception e)
        {   
            mLog.error("Error in generateIV() method.",e);
            throw new BusinessException(e.getMessage());
        }
    }

    private static SecretKey generateSecretKey() {
        try{
        KeyGenerator kgen = KeyGenerator.getInstance("AES");
        kgen.init(256);
        SecretKey skey = kgen.generateKey();
        String fileName=loadFileNameFromPropertyFile(SECRET_KEY_FILE_PROP_NAME);
        URL fileURL = AESCryptography.class.getResource(fileName);
        try (FileOutputStream out = new FileOutputStream(fileURL.getFile())) {
            byte[] keyb = skey.getEncoded();
            out.write(keyb);
        }
        return skey;
        }
        catch(Exception e)
        {   
            mLog.error(e);
            throw new BusinessException(e.getMessage());
        }
    }

    public static void updateSecretKey() 
    {
        generateIV();
        generateSecretKey();
    }
    public static IvParameterSpec prepareIVParameterSpec() throws IOException, URISyntaxException {
//        byte[] iv = IV_STRING.getBytes();
//        IvParameterSpec ivspec = new IvParameterSpec(iv);
       try{
       String fileName=loadFileNameFromPropertyFile(IV_FILE_PROP_Name);
        URL fileURL = AESCryptography.class.getResource(fileName);
        byte[] iv = Files.readAllBytes(Paths.get(fileURL.toURI()));
        IvParameterSpec ivspec = new IvParameterSpec(iv);
        return ivspec;
       }
        catch(Exception e)
        {   
            mLog.error(e);
            throw new BusinessException(e.getMessage());
        }
    }

    public static SecretKey prepareSecretKey() {
//        byte[] keyb = SECRET_KEY_STRING.getBytes();
//        SecretKey skey = new SecretKeySpec(keyb, "AES");
     try{
        String fileName=loadFileNameFromPropertyFile(SECRET_KEY_FILE_PROP_NAME);
        URL fileURL = AESCryptography.class.getResource(fileName);
      byte[] keyb = Files.readAllBytes(Paths.get(fileURL.toURI()));
        SecretKeySpec skey = new SecretKeySpec(keyb, "AES");
        return skey;
        }
        catch(Exception e)
        {   
            mLog.error(e);
            throw new BusinessException(e.getMessage());
        }
    }

    public static String encryptString(String plainTextString) {
        try {
            byte[] plainTextBytes = plainTextString.getBytes(CHAR_ENCODING);
            IvParameterSpec ivspec = prepareIVParameterSpec();
            SecretKey skey = prepareSecretKey();
            Cipher ci = Cipher.getInstance("AES/CBC/PKCS5Padding");
            ci.init(Cipher.ENCRYPT_MODE, skey, ivspec);
            byte[] encodedBytes = ci.doFinal(plainTextBytes);
//            String encodedString = new String(encodedBytes,CHAR_ENCODING);
            String encodedString = Base64.getEncoder().encodeToString(encodedBytes);
            return encodedString;
        } 
        catch(Exception e)
        {   
            mLog.error(e);
            throw new BusinessException(e.getMessage());
        }

    }

    public static String decryptString(String encodedText){
        try {
            byte[] encodedTextBytes = Base64.getDecoder().decode(encodedText);
            Cipher ci = Cipher.getInstance("AES/CBC/PKCS5Padding");
            IvParameterSpec ivspec = prepareIVParameterSpec();
            SecretKey skey = prepareSecretKey();
            ci.init(Cipher.DECRYPT_MODE, skey, ivspec);
            String plainTextString = new String(ci.doFinal(encodedTextBytes), CHAR_ENCODING);
            return plainTextString;
       }
        catch(Exception e)
        {   
            mLog.error(e);
            throw new BusinessException(e.getMessage());
        }
    }

    private static String   loadFileNameFromPropertyFile(String propertyName)
    {
        Properties prop = new Properties();
	InputStream input = null;

	try {
            URL fileURL = AESCryptography.class.getResource("/"+PROERTY_FILE_NAME);
//        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            input = new FileInputStream(fileURL.getFile());

		// load a properties file
		prop.load(input);

		// get the property value and print it out
                return prop.getProperty(propertyName);

	}
        catch(Exception e)
        {   
            mLog.error(e);
            throw new BusinessException(e.getMessage());
        } finally {
		if (input != null) {
			try {
				input.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}      
    }
}
