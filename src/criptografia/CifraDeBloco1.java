package criptografia;
import java.util.Arrays;

public class CifraDeBloco1 {
	public static char [] encripta(char [] textoPlano, char [] iv, char [] chave) {
		char [] esquerda = Arrays.copyOf(textoPlano, textoPlano.length/2);
		char [] direira = Arrays.copyOfRange(textoPlano, textoPlano.length/2, textoPlano.length);
		
		int i = 0;
		int i_ch = 0;
		for (int interacoes = 0; interacoes < 1; interacoes++) {
			for (i = 0, i_ch = 0; i < esquerda.length; i++, i_ch++) {
				if(i_ch == 8) i_ch = 0;
				
				esquerda[i] = (char) (esquerda[i] ^ iv[i_ch] ^ chave[i_ch]);
			}
			
			for (i = 0, i_ch = 0; i < direira.length; i++, i_ch++) {
				if(i_ch == 8) i_ch = 0;
				
				direira[i] = (char) (direira[i] ^ esquerda[i] ^ chave[i_ch]);
			}
			
			// invertendo
			char [] salva = esquerda;
			esquerda = direira;
			direira = salva;
		}
		
		char [] textoEncriptado = new char [textoPlano.length];
		int metade = textoEncriptado.length/2;
		for (i = 0; i < metade; i++) {
			textoEncriptado[i] = esquerda[i];
		}
		
		for (i = 0; i < metade; i++) {
			textoEncriptado[i+metade] = direira[i];
		}
		
		return textoEncriptado;
	}
	
	public static char [] decripta(char [] textoEncriptado, char [] iv, char [] chave) {
		char [] esquerda = Arrays.copyOf(textoEncriptado, textoEncriptado.length/2);
		char [] direira = Arrays.copyOfRange(textoEncriptado, textoEncriptado.length/2, textoEncriptado.length);
		
		int i = 0;
		int i_ch = 0;
		for (int interacoes = 0; interacoes < 1; interacoes++) {
			// invertendo
			char [] salva = esquerda;
			esquerda = direira;
			direira = salva;
			
			for (i = 0, i_ch = 0; i < direira.length; i++, i_ch++) {
				if(i_ch == 8) i_ch = 0;
				
				direira[i] = (char) (direira[i] ^ esquerda[i] ^ chave[i_ch]);
			}
			
			for (i = 0, i_ch = 0; i < esquerda.length; i++, i_ch++) {
				if(i_ch == 8) i_ch = 0;
				
				esquerda[i] = (char) (esquerda[i] ^ iv[i_ch] ^ chave[i_ch]);
			}
		}
		
		char [] textoPlano = new char [textoEncriptado.length];
		int metade = textoPlano.length/2;
		for (i = 0; i < metade; i++) {
			textoPlano[i] = esquerda[i];
		}
		
		for (i = 0; i < metade; i++) {
			textoPlano[i+metade] = direira[i];
		}
		
		return textoPlano;
	}
	
	public static void main(String[] args) {
		String texto = "0123456789abcdf01111222233334445";
		System.out.println(Arrays.toString(texto.toCharArray()));
		
		char [] textoEncriptado = encripta(texto.toCharArray(), "aaaaaaaa".toCharArray(), "bbbbbbbb".toCharArray());
		char [] textoPlano = decripta(textoEncriptado, "aaaaaaaa".toCharArray(), "bbbbbbbb".toCharArray());
		
		System.out.println(Arrays.toString(textoEncriptado));
		System.out.println(Arrays.toString(textoPlano));
	}
}
