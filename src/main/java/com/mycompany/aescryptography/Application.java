/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.aescryptography;
 
import  com.mycompany.aescryptography.AESCryptography;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
//import unilever.gprs.controller.GPRSCarrier; 
/**
 *
 * @author Esraa.Hefny
 */

@SpringBootApplication 
@ComponentScan
public class Application {

     
    public static void main(String[] args) throws Exception {
        SpringApplication.run(Application.class, args);
        AESCryptography.updateSecretKey();
        String e=AESCryptography.encryptString("{\"salesmanno\":\"1161001\",\"password\":\"xyz\",\"adminPassword\":\"123\"}");
        String d=AESCryptography.decryptString(e);
    }
    
   
  
}
