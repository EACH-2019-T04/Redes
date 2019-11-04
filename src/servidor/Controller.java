package servidor;

import java.io.IOException;
import java.lang.reflect.Method;
import java.net.ServerSocket;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.swing.JOptionPane;

import servidor.criptografia.CriptografiaAssimetrica;

public class Controller {
	private View view;
	
	private ServerSocket serverSocket;
	private AtomicBoolean continua;

	public Controller(View view) throws IOException{
		this.view = view;
		
		this.serverSocket = new ServerSocket(12346);
		this.continua = new AtomicBoolean();
		this.continua.set(true);
		
		new Thread(new Runnable() {
			@Override
			public void run() {
				while (continua.get()) {
					try {
						Conexao conexao = new Conexao(serverSocket.accept());
						
						byte[] requisicao = conexao.receber();
						byte[] reposta = control(requisicao);
						conexao.enviar(reposta);
					} catch (Exception e) {
						
					}
				}
			}
		}).start();
	}

	public byte[] control(byte[] requisicao) {
		
		try {
			Class<Controller> clazz = Controller.class;
			
			if(new String(requisicao).startsWith("chavePublica")) {
				view.tfReceber.setText("chavePublica=enviada para cliente");
				return CriptografiaAssimetrica.getBytesChavePublica();
			} 
				
			requisicao = CriptografiaAssimetrica.decriptar(requisicao);
			String comando = new String(requisicao);
			String[] cmdParse = comando.split("[=]");
			Method m = clazz.getMethod(cmdParse[0], String.class);
			return ((String) m.invoke(this, cmdParse[1])).getBytes();
		} catch (Exception e) {
			return erro("comando inválido: " + new String(requisicao)).getBytes();
		}
	}

	public String texto(String texto) {
		view.tfReceber.setText(texto);

		return "texto="+texto;
	}

	public String erro(String texto) {
		JOptionPane.showMessageDialog(view.frame, texto, "Erro", JOptionPane.ERROR_MESSAGE);

		return null;
	}

	public void fecharSocketServer() throws IOException {
		continua.set(false);
		serverSocket.close();
	}
}
