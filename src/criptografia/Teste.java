package criptografia;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class Teste {

	public static void main(String[] args) {
		char[] texto = "abcda".toCharArray();
		
		System.out.println(Arrays.toString(texto));
		
		int soma = 0;
		for (char c : texto) {
			soma += c;
		}
		
		Random random = new Random(soma);
		List<Integer> posicoes = new ArrayList<>();
		while(posicoes.size() < texto.length) {
			int i = random.nextInt(texto.length);
			if(!posicoes.contains(i)) posicoes.add(i);
		}
		
		char[] texto2 = new char[texto.length];
		for (int i = 0; i < texto.length; i++) {
			texto2[i] = texto[posicoes.get(i)];
		}
		
		System.out.println(Arrays.toString(texto2));
		
		char[] texto3 = new char[texto.length];
		for (int i = 0; i < texto.length; i++) {
			texto3[posicoes.get(i)] = texto2[i];
		}
		
		System.out.println(65536*32767);
		
		System.out.println(Arrays.toString(texto3));
	}

}
