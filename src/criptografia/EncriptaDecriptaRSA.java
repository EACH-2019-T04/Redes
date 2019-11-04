package criptografia;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;

import javax.crypto.Cipher;

public class EncriptaDecriptaRSA {

	public static final String ALGORITHM = "RSA";

	public static byte[] bytesChavePrivava;
	public static byte[] bytesChavePublica;

	public static void geraChaves() throws IOException, NoSuchAlgorithmException {
		final KeyPairGenerator keyGen = KeyPairGenerator.getInstance(ALGORITHM);
		keyGen.initialize(1024);
		final KeyPair key = keyGen.generateKeyPair();

		// Chave Privada
		ByteArrayOutputStream chavePrivadaByteArray = new ByteArrayOutputStream();
		ObjectOutputStream chavePrivadaOS = new ObjectOutputStream(chavePrivadaByteArray);
		chavePrivadaOS.writeObject(key.getPrivate());
		bytesChavePrivava = chavePrivadaByteArray.toByteArray();
		chavePrivadaOS.flush();
		chavePrivadaOS.close();
		
		// Chave Publica
		ByteArrayOutputStream chavePublicaByteArray = new ByteArrayOutputStream();
		ObjectOutputStream chavePublicaOS = new ObjectOutputStream(chavePublicaByteArray);
		chavePublicaOS.writeObject(key.getPublic());
		bytesChavePublica = chavePublicaByteArray.toByteArray();
		chavePublicaOS.flush();
		chavePublicaOS.close();
	}

	public static byte[] criptografa(String textoPlano, PublicKey publicKey) throws Exception {
		final Cipher cipher = Cipher.getInstance(ALGORITHM);
		cipher.init(Cipher.ENCRYPT_MODE, publicKey);
		return cipher.doFinal(textoPlano.getBytes());
	}

	public static String decriptografa(byte[] bytesTextoCifrado, PrivateKey privateKey) throws Exception {
		final Cipher cipher = Cipher.getInstance(ALGORITHM);
		cipher.init(Cipher.DECRYPT_MODE, privateKey);
		return new String(cipher.doFinal(bytesTextoCifrado));
	}

	public static void main(String[] args) throws Exception {
		final String msgOriginal = "Pxemplo de mensagem";
		geraChaves();
		
		// Criptografa
		ObjectInputStream inputStream = new ObjectInputStream(new ByteArrayInputStream(bytesChavePublica));
		final PublicKey chavePublica = (PublicKey) inputStream.readObject();
		final byte[] textoCriptografado = criptografa(msgOriginal, chavePublica);

		// Decriptografa
		inputStream = new ObjectInputStream(new ByteArrayInputStream(bytesChavePrivava));
		final PrivateKey chavePrivada = (PrivateKey) inputStream.readObject();
		final String textoPuro = decriptografa(textoCriptografado, chavePrivada);

		System.out.println("Mensagem Original: " + msgOriginal);
		System.out.println("Mensagem Criptografada: " + new String(textoCriptografado));
		System.out.println("Mensagem Decriptografada: " + textoPuro);
	}
}
