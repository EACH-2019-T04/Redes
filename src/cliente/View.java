package cliente;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JTextField;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class View {
	private Controller controller;

	public JFrame frame;
	public JTextField tfEnviar;
	public JTextField tfReceber;

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
	public View() {
		controller = new Controller(this);
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame("Cliente");
		frame.setBounds(10, 390, 336, 171);
		frame.getContentPane().setLayout(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		tfEnviar = new JTextField();
		tfEnviar.setBounds(10, 10, 300, 30);
		frame.getContentPane().add(tfEnviar);
		
		tfReceber = new JTextField();
		tfReceber.setBounds(10, 50, 300, 30);
		tfReceber.setEditable(false);
		frame.getContentPane().add(tfReceber);
		
		JButton btn = new JButton("Enviar");
		btn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					controller.enviar(tfEnviar.getText());
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
		});
		btn.setBounds(10, 90, 300, 30);
		frame.getContentPane().add(btn);
	}
}
