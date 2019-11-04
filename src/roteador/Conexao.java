package roteador;


import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class Conexao {
	
	public static void receber(Socket socket, View view) throws Exception {
		OutputStream saida = socket.getOutputStream();
		InputStream entrada = socket.getInputStream();

		// para ativar a escuta eu tenho que ler o primeiro byte
		byte primeiroByte = (byte) entrada.read();

		// criando o buffer
		// entrada.available() -> retorna o numero de bytes nao lidos
		// +1 pq eu ja li o primeiro byte
		byte[] buffer = new byte[entrada.available() + 1];

		// colocando o primeiro byte na posicao 1
		buffer[0] = primeiroByte;

		// lendo os bytes restantes e colocando no buffer
		entrada.read(buffer, 1, entrada.available());

		// toda essa logica eh pq o entrada.read(...) so funciona depois que eu li o
		// primeiro byte

		view.setTextoCliente(new String(buffer));
		
		byte[] resposta = enviar(buffer);
		view.setTextoServidor(new String(resposta));
		
		saida.write(resposta);
	}
	
	public static byte[] enviar(byte[] texto) throws Exception {
		Socket socket = new Socket("localhost", 12346);
		OutputStream saida = socket.getOutputStream();
		InputStream entrada = socket.getInputStream();

		saida.write(texto);

		// para ativar a escuta eu tenho que ler o primeiro byte
		byte primeiroByte = (byte) entrada.read();

		// criando o buffer
		// entrada.available() -> retorna o numero de bytes nao lidos
		// +1 pq eu ja li o primeiro byte
		byte[] buffer = new byte[entrada.available() + 1];

		// colocando o primeiro byte na posicao 1
		buffer[0] = primeiroByte;

		// lendo os bytes restantes e colocando no buffer
		entrada.read(buffer, 1, entrada.available());

		// toda essa logica eh pq o entrada.read(...) so funciona depois que eu li o
		// primeiro byte

		entrada.close();
		saida.close();
		socket.close();

		return buffer;
	}
}
