package com.pedantic.service;

import java.security.Key;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.spec.SecretKeySpec;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.swing.KeyStroke;

import org.apache.shiro.authc.credential.DefaultPasswordService;
import org.apache.shiro.authc.credential.PasswordService;
import org.apache.shiro.codec.CodecSupport;
import org.apache.shiro.codec.Hex;
import org.apache.shiro.crypto.AesCipherService;
import org.apache.shiro.crypto.SecureRandomNumberGenerator;
import org.apache.shiro.crypto.hash.Sha512Hash;
import org.apache.shiro.util.ByteSource;

import com.pedantic.entities.ApplicationUser;

@RequestScoped
public class SecurityUtil {
	private final PasswordService passwordService = new DefaultPasswordService();
	
	@Inject
	QueryService queryService;
	
	public String encryptText(String plainText) {
		return passwordService.encryptPassword(plainText);
	}
	
	public boolean passwordMatch(String dbStoredHashPassword, String saltText, String plainTextPassword) {
		//convert to bytesalt
		ByteSource salt = ByteSource.Util.bytes(Hex.decode(saltText));
		//hash the password sent by the user with the salt
		String hashedPassword = hashAndSaltPassword(plainTextPassword, salt);
		//compare the user sent hashedpassword and the database hashedpassword
		return hashedPassword.equals(dbStoredHashPassword);
	}

	public Key generateKey(String keyString) {
//		AesCipherService cipher = new AesCipherService();
//		return cipher.generateNewKey(256);	
		//use keyspec passing in bytes of keystring then byte length and algorithmand return it
		return new SecretKeySpec(keyString.getBytes(), 0, keyString.getBytes().length, "DES");
	}
	//pass email and plaintext then uses query service to authenticate user
	public boolean authenticateUser(String email, String password) throws Exception{
		return queryService.authenticateUser(email, password);
	}
	
	public Date toDate(LocalDateTime localDateTime) {
		return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
	}
	//takes plaintext password 
	public Map<String , String> hashPassword(String plainTextPassword){
		//generates a salt
		ByteSource salt = getSalt();
		Map<String, String> credMap = new HashMap<>();
		//hashAndSaltPassword(private method) takes text and salt then we put in a map then the salt in a map then return to user
		credMap.put("hashedPassword", hashAndSaltPassword(plainTextPassword, salt));
		credMap.put("salt", salt.toHex());
		return credMap;
	}
	
	private String hashAndSaltPassword(String plainTextPassword, ByteSource salt) {
		//takes sha512 creates new instance pass textpass salt and hash iterations return hex value as string
		return new Sha512Hash(plainTextPassword, salt, 2000000).toHex();
	}
	//use securerandom generator to create byte source
	private ByteSource getSalt() {
		return new SecureRandomNumberGenerator().nextBytes();
	}

}
