package cliente.criptografia;
import java.security.PublicKey;

import javax.crypto.Cipher;

public class CriptografiaAssimetrica {

	private static Cipher cipherEncriptar;

	static  {
		try {
			cipherEncriptar = Cipher.getInstance("RSA");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static byte[] encriptar(byte[] textoPlano, PublicKey chavePublica) throws Exception {
		cipherEncriptar.init(Cipher.ENCRYPT_MODE, chavePublica);
		return cipherEncriptar.doFinal(textoPlano);
	}
}
