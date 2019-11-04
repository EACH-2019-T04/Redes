package criptografia;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class CifraDeBloco3 {
	public static char [] encripta(char [] textoPlano, char [] iv, char [] chave) throws Exception {
		char [] esquerda = Arrays.copyOf(textoPlano, textoPlano.length/2);
		char [] direira = Arrays.copyOfRange(textoPlano, textoPlano.length/2, textoPlano.length);
		
		int i = 0;
		int i_ch = 0;
		for (int interacoes = 0; interacoes < 16; interacoes++) {
			iv = MD5.getHash(new String(iv)).toCharArray();
			chave = MD5.getHash(new String(chave)).toCharArray();
			
			for (i = 0, i_ch = 0; i < esquerda.length; i++, i_ch++) {
				if(i_ch == 32) i_ch = 0;
				
				esquerda[i] = (char) (esquerda[i] ^ iv[i_ch] ^ chave[i_ch]);
			}
			
			for (i = 0, i_ch = 0; i < direira.length; i++, i_ch++) {
				if(i_ch == 32) i_ch = 0;
				
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
		
		// embaralha a ordem
		int soma = 0;
		for (char c : textoEncriptado) {
			soma += c;
		}
		Random random = new Random(soma);
		List<Integer> posicoes = new ArrayList<>();
		while(posicoes.size() < textoEncriptado.length) {
			int sorteio = random.nextInt(textoEncriptado.length);
			if(!posicoes.contains(sorteio)) posicoes.add(sorteio);
		}
		
		char[] salva = textoEncriptado;
		textoEncriptado = new char[salva.length];
		for (int j = 0; j < textoEncriptado.length; j++) {
			textoEncriptado[j] = salva[posicoes.get(j)];
		}
		
		return textoEncriptado;
	}
	
	public static char [] decripta(char [] textoEncriptado, char [] iv, char [] chave) throws Exception {
		// embaralha a ordem
		{
			int soma = 0;
			for (char c : textoEncriptado) {
				soma += c;
			}
			Random random = new Random(soma);
			List<Integer> posicoes = new ArrayList<>();
			while(posicoes.size() < textoEncriptado.length) {
				int sorteio = random.nextInt(textoEncriptado.length);
				if(!posicoes.contains(sorteio)) posicoes.add(sorteio);
			}
			
			char[] salva = textoEncriptado;
			textoEncriptado = new char[salva.length];
			for (int j = 0; j < textoEncriptado.length; j++) {
				textoEncriptado[posicoes.get(j)] = salva[j];
			}
		}
		char [] esquerda = Arrays.copyOf(textoEncriptado, textoEncriptado.length/2);
		char [] direira = Arrays.copyOfRange(textoEncriptado, textoEncriptado.length/2, textoEncriptado.length);
		
		List<char []> ivList = new ArrayList<>();
		List<char []> chaveList = new ArrayList<>();
		for (int interacoes = 0; interacoes < 16; interacoes++) {
			iv = MD5.getHash(new String(iv)).toCharArray();
			chave = MD5.getHash(new String(chave)).toCharArray();
			
			ivList.add(Arrays.copyOf(iv, textoEncriptado.length));
			chaveList.add(Arrays.copyOf(chave, textoEncriptado.length));
		}
		Collections.reverse(ivList);
		Collections.reverse(chaveList);
		
		int i = 0;
		int i_ch = 0;
		for (int interacoes = 0; interacoes < 16; interacoes++) {
			iv = ivList.get(interacoes);
			chave = chaveList.get(interacoes);
			
			// invertendo
			char [] salva = esquerda;
			esquerda = direira;
			direira = salva;
			
			for (i = 0, i_ch = 0; i < direira.length; i++, i_ch++) {
				if(i_ch == 32) i_ch = 0;
				
				direira[i] = (char) (direira[i] ^ esquerda[i] ^ chave[i_ch]);
			}
			
			for (i = 0, i_ch = 0; i < esquerda.length; i++, i_ch++) {
				if(i_ch == 32) i_ch = 0;
				
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
	
	public static void main(String[] args) throws Exception {
		String texto = "0123456789abcdf011112222333344445556";
		System.out.println(Arrays.toString(texto.toCharArray()));
		
		char [] textoEncriptado = encripta(texto.toCharArray(), "aaaaaaaa".toCharArray(), "bbbbbbbb".toCharArray());
		System.out.println();
		char [] textoPlano = decripta(textoEncriptado, "aaaaaaaa".toCharArray(), "bbbbbbbb".toCharArray());
		
		
		System.out.println(Arrays.toString(textoEncriptado));
		System.out.println();
		System.out.println(Arrays.toString(textoPlano));
	}
}
