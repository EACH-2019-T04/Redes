package cliente;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

public class Conexao {

	private Socket socket;
	private OutputStream saida;
	private InputStream entrada;
	
	public Conexao () throws UnknownHostException, IOException{
		socket = new Socket("localhost", 12345);
		saida = socket.getOutputStream();
		entrada = socket.getInputStream();
	}
	
	public void enviar(byte[] texto) throws Exception {
		saida.write(texto);
	}
	
	public byte[] receber() throws IOException {
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

		return buffer;
	}
	
	public void fechar() throws IOException {
		entrada.close();
		saida.close();
		socket.close();
	}
}
