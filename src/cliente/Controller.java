package cliente;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.lang.reflect.Method;
import java.security.PublicKey;

import javax.swing.JOptionPane;

import cliente.criptografia.CriptografiaAssimetrica;

public class Controller {
	private View view;
	private PublicKey chavePublica;
	
	public Controller(View view) {
		this.view = view;
	}

	public void control(String comando){
		try {
			Class<Controller> clazz = Controller.class;
			String[] cmdParse = comando.split("[=]");	
			Method m = clazz.getMethod(cmdParse[0], String.class);
			m.invoke(this, cmdParse[1]);
		}catch(Exception e) {
			erro("comando inválido: "+comando);
			e.printStackTrace();
		}
	}

	public void texto(String texto) {
		view.tfReceber.setText(texto);
	}
	
	public void erro(String texto) {
		JOptionPane.showMessageDialog(view.frame, texto, "Erro", JOptionPane.ERROR_MESSAGE);
	}
	
	public void enviar(String texto) throws Exception {
		if(chavePublica == null) {
			try {
				Conexao conexao = new Conexao();
				conexao.enviar("chavePublica=get".getBytes());
				byte[] reposta = conexao.receber();
				conexao.fechar();
				
				ObjectInputStream inputStream = new ObjectInputStream(new ByteArrayInputStream(reposta));
				chavePublica = (PublicKey) inputStream.readObject();
				inputStream.close();
			} catch (IOException | ClassNotFoundException e) {
				JOptionPane.showMessageDialog(view.frame, "Ao buscar a Chave Publica", "Erro", JOptionPane.ERROR_MESSAGE);
			}
		}
		
		byte[] textoPlano = CriptografiaAssimetrica.encriptar(texto.getBytes(), chavePublica);
		
		Conexao conexao = new Conexao();
		conexao.enviar(textoPlano);
		byte[] reposta = conexao.receber();
		conexao.fechar();
		control(new String(reposta));
	}
}
