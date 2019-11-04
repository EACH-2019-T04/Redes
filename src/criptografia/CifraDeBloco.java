package criptografia;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class CifraDeBloco {
	private static char[] desembaralha(char[] textoPlano) {
		int soma = 0;
		for (char c : textoPlano) {
			soma += c;
		}
		Random random = new Random(soma);
		List<Integer> posicoes = new ArrayList<>();
		while(posicoes.size() < textoPlano.length) {
			int sorteio = random.nextInt(textoPlano.length);
			if(!posicoes.contains(sorteio)) posicoes.add(sorteio);
		}
			
		char[] salva = Arrays.copyOf(textoPlano, textoPlano.length);
		textoPlano = new char[salva.length];
		for (int j = 0; j < textoPlano.length; j++) {
			textoPlano[posicoes.get(j)] = salva[j];
		}
		
		return textoPlano;
	}

	private static char[] embaralha(char[] textoEncriptado) {
		char[] salva;
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
		
		salva = Arrays.copyOf(textoEncriptado, textoEncriptado.length);
		textoEncriptado = new char[salva.length];
		for (int j = 0; j < textoEncriptado.length; j++) {
			textoEncriptado[j] = salva[posicoes.get(j)];
		}
		return textoEncriptado;
	}
	
	public static char [] encripta(char [] textoPlano, char [] iv, char [] chave) throws Exception {
		char [] textoEncriptado = Arrays.copyOf(textoPlano, textoPlano.length);
		
		int i = 0;
		int i_ch = 0;
		int metadeDoTamanho = textoEncriptado.length/2;
		for (int interacoes = 0; interacoes < 16; interacoes++) {
			iv = MD5.getHash(new String(iv)).toCharArray();
			chave = MD5.getHash(new String(chave)).toCharArray();
			
			char [] salva = Arrays.copyOf(textoEncriptado, textoEncriptado.length);
			
			for (i = 0, i_ch = 0; i < metadeDoTamanho; i++, i_ch++) {
				if(i_ch == 32) i_ch = 0;
				
				textoEncriptado[i+metadeDoTamanho] = (char) (salva[i] ^ iv[i_ch] ^ chave[i_ch]);
			}
			
			for (i = 0, i_ch = 0; i < metadeDoTamanho; i++, i_ch++) {
				if(i_ch == 32) i_ch = 0;
				
				textoEncriptado[i] = (char) (salva[i+metadeDoTamanho] ^ textoEncriptado[i+metadeDoTamanho] ^ chave[i_ch]);
			}
			
			textoEncriptado = embaralha(textoEncriptado);
		}
		
		return textoEncriptado;
	}
	
	public static char [] decripta(char [] textoEncriptado, char [] iv, char [] chave) throws Exception {
		char [] textoPlano = Arrays.copyOf(textoEncriptado, textoEncriptado.length);
		
		List<char []> ivList = new ArrayList<>();
		List<char []> chaveList = new ArrayList<>();
		int metadeDoTamanho = textoPlano.length/2;
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
			
			textoPlano = desembaralha(textoPlano);
			
			char[] salva = Arrays.copyOf(textoPlano, textoPlano.length);
			
			for (i = 0, i_ch = 0; i < metadeDoTamanho; i++, i_ch++) {
				if(i_ch == 32) i_ch = 0;
				
				textoPlano[i+metadeDoTamanho] = (char) (salva[i] ^ salva[i+metadeDoTamanho] ^ chave[i_ch]);
			}
			
			for (i = 0, i_ch = 0; i < metadeDoTamanho; i++, i_ch++) {
				if(i_ch == 32) i_ch = 0;
				
				textoPlano[i] = (char) (salva[i+metadeDoTamanho] ^ iv[i_ch] ^ chave[i_ch]);
			}
		}
		
		return textoPlano;
	}
	
	public static void main(String[] args) throws Exception {
		String texto = "0123456789abcdf011112222333344445555";
		System.out.println(Arrays.toString(texto.toCharArray()));
		
		char [] textoEncriptado = encripta(texto.toCharArray(), "aaaaaaaa".toCharArray(), "bbbbbbbb".toCharArray());
		System.out.println();
		char [] textoPlano = decripta(textoEncriptado, "aaaaaaaa".toCharArray(), "bbbbbbbb".toCharArray());
		
		
		System.out.println(Arrays.toString(textoEncriptado));
		System.out.println();
		System.out.println(Arrays.toString(textoPlano));
	}
}
