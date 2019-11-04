package criptografia;

import java.math.BigInteger;
import java.security.MessageDigest;

public class MD5 {

	public static String getHash(String menssagem) throws Exception {
		MessageDigest messageDigest = MessageDigest.getInstance("MD5");
		messageDigest.update(menssagem.getBytes());
		return new BigInteger(1, messageDigest.digest()).toString(16);
	}

	public static void main(String args[]) throws Exception {
		System.out.println(getHash("Texto de Exemplo"));
		System.out.println(getHash("Texto de Exemplo"));
	}
}
