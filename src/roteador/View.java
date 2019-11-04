package roteador;

import java.awt.EventQueue;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.net.ServerSocket;
import java.util.LinkedList;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;

public class View {
	private ServerSocket serverSocket;
	private AtomicBoolean continua;

	private JFrame frame;
	private JTextArea tfServidor;
	private JTextArea tfCliente;
	private LinkedList<String> historicoServidor;
	private LinkedList<String> historicoCliente;
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					for(javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
						if("Nimbus".equals(info.getName())) {
							javax.swing.UIManager.setLookAndFeel(info.getClassName());
							break;
						}
					}
					View window = new View();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public View() throws IOException{
		this.serverSocket = new ServerSocket(12345);
		this.continua = new AtomicBoolean();
		this.continua.set(true);
		historicoServidor = new LinkedList<>();
		historicoCliente = new LinkedList<>();
		initialize();
		
		View view = this;
		new Thread(new Runnable() {
			@Override
			public void run() {
				while (continua.get()) {
					try {
						Conexao.receber(serverSocket.accept(), view);
					} catch (Exception e) {
						
					}
				}
			}
		}).start();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame("Roteador");
		frame.setBounds(10, 110, 336, 270);
		frame.getContentPane().setLayout(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent evt) {
				try {
					continua.set(false);
					serverSocket.close();
				} catch (IOException e) {
					
				}
			}
		});
		
		tfServidor = new JTextArea();
		tfServidor.setEditable(false);
		tfServidor.setLineWrap(false);
		tfServidor.setWrapStyleWord(true);
		JScrollPane spServidor = new JScrollPane(tfServidor);
		spServidor.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		spServidor.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		spServidor.setBounds(10, 10, 300, 100);
		frame.getContentPane().add(spServidor);
		
		tfCliente = new JTextArea();
		tfCliente.setEditable(false);
		tfCliente.setLineWrap(true);
		tfCliente.setWrapStyleWord (true);
		JScrollPane spCliente = new JScrollPane(tfCliente);
		spCliente.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		spCliente.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		spCliente.setBounds(10, 120, 300, 100);
		frame.getContentPane().add(spCliente);
	}
	
	public void setTextoServidor(String texto) {
		historicoServidor.add(texto);
		if(historicoServidor.size() == 5) historicoServidor.remove();
		
		StringBuilder builder = new StringBuilder();
		builder.append("Servidor\n");
		for (String t : historicoServidor) {
			builder.append(t+"\n");
		}
		
		tfServidor.setText(builder.toString());
	}
	
	public void setTextoCliente(String texto) {
		historicoCliente.add(texto);
		if(historicoCliente.size() == 5) historicoCliente.remove();
		
		StringBuilder builder = new StringBuilder();
		builder.append("Cliente\n");
		for (String t : historicoCliente) {
			builder.append(t+"\n");
		}
		
		tfCliente.setText(builder.toString());
	}
}
