package pl.izidev.utils;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Not sure if I will use it yet for HTML tag id generation
 */
public class HashingUtils {

	public static String md5(String plain) {
		try {
			byte[] bytes = plain.getBytes();
			MessageDigest md = MessageDigest.getInstance("MD5");
			byte[] digest = md.digest(bytes);
			return new BigInteger(1, digest).toString(16);
		} catch (NoSuchAlgorithmException e) {
			return null;
		}
	}

}
