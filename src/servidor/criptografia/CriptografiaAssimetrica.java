package servidor.criptografia;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PublicKey;

import javax.crypto.Cipher;

public class CriptografiaAssimetrica {

	private static Cipher cipherDecriptar;
	private static byte[] bytesChavePublica;

	static  {
		try {
			final KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
			keyGen.initialize(1024);
			final KeyPair key = keyGen.generateKeyPair();
	
			// Cifra Decriptar
			cipherDecriptar = Cipher.getInstance("RSA");
			cipherDecriptar.init(Cipher.DECRYPT_MODE, key.getPrivate());
			
			// Chave Publica
			ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
			ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
			objectOutputStream.writeObject(key.getPublic());
			bytesChavePublica = byteArrayOutputStream.toByteArray();
			objectOutputStream.flush();
			objectOutputStream.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static byte[] getBytesChavePublica() {
		return bytesChavePublica;
	}
	
	public static byte[] encriptar(String textoPlano, PublicKey publicKey) throws Exception {
		final Cipher cipher = Cipher.getInstance("RSA");
		cipher.init(Cipher.ENCRYPT_MODE, publicKey);
		return cipher.doFinal(textoPlano.getBytes());
	}

	public static byte[] decriptar(byte[] bytesTextoCifrado) throws Exception {
		return cipherDecriptar.doFinal(bytesTextoCifrado);
	}

	public static void main(String[] args) throws Exception {
		final String msgOriginal = "Exemplo de mensagem";
		
		// Criptografa
		ObjectInputStream inputStream = new ObjectInputStream(new ByteArrayInputStream(new String(bytesChavePublica).getBytes()));
		final PublicKey chavePublica = (PublicKey) inputStream.readObject();
		final byte[] textoCriptografado = encriptar(msgOriginal, chavePublica);

		// Decriptografa
		final String textoPuro = new String(decriptar(textoCriptografado));

		System.out.println("Mensagem Original: " + msgOriginal);
		System.out.println("Mensagem Criptografada: " + textoCriptografado.toString());
		System.out.println("Mensagem Decriptografada: " + textoPuro);
		
		System.out.println(new String(CriptografiaAssimetrica.getBytesChavePublica()));
	}
}
