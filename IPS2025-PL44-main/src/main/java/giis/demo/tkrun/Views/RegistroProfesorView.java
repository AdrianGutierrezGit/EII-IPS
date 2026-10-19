package giis.demo.tkrun.Views;

import java.awt.Font;
import java.awt.Toolkit;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;

public class RegistroProfesorView extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;

	private JTextField textNombre;
	private JTextField textApellido;
	private JTextField textTelefono;
	private JTextField textCorreo;
	private JButton btnRegistrar;

	public RegistroProfesorView() {
		setTitle("COIIPA: Registro de profesores");
		setIconImage(Toolkit.getDefaultToolkit()
				.getImage(RegistroProfesorView.class.getResource("/imagenes/logo_COIIPA.png")));
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 629, 341);

		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(null);
		setContentPane(contentPane);

		JPanel panelFormulario = new JPanel();
		panelFormulario
				.setBorder(new TitledBorder(null, "Formulario", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panelFormulario.setBounds(53, 32, 514, 240);
		panelFormulario.setLayout(null);
		contentPane.add(panelFormulario);

		JLabel lblNombre = new JLabel("Nombre:");
		lblNombre.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblNombre.setBounds(20, 30, 100, 25);
		panelFormulario.add(lblNombre);

		textNombre = new JTextField();
		textNombre.setBounds(130, 30, 350, 25);
		panelFormulario.add(textNombre);

		JLabel lblApellido = new JLabel("Apellidos:");
		lblApellido.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblApellido.setBounds(20, 65, 100, 25);
		panelFormulario.add(lblApellido);

		textApellido = new JTextField();
		textApellido.setBounds(130, 65, 350, 25);
		panelFormulario.add(textApellido);

		JLabel lblTelefono = new JLabel("Teléfono:");
		lblTelefono.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblTelefono.setBounds(20, 100, 100, 25);
		panelFormulario.add(lblTelefono);

		textTelefono = new JTextField();
		textTelefono.setBounds(130, 100, 350, 25);
		panelFormulario.add(textTelefono);

		JLabel lblCorreo = new JLabel("Correo:");
		lblCorreo.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblCorreo.setBounds(20, 135, 100, 25);
		panelFormulario.add(lblCorreo);

		textCorreo = new JTextField();
		textCorreo.setBounds(130, 135, 350, 25);
		panelFormulario.add(textCorreo);

		btnRegistrar = new JButton("Registrar");
		btnRegistrar.setFont(new Font("Tahoma", Font.PLAIN, 14));
		btnRegistrar.setBounds(380, 180, 100, 30);
		panelFormulario.add(btnRegistrar);
	}

	public JTextField getTextNombre() {
		return textNombre;
	}

	public JTextField getTextApellido() {
		return textApellido;
	}

	public JTextField getTextTelefono() {
		return textTelefono;
	}

	public JTextField getTextCorreo() {
		return textCorreo;
	}

	public JButton getBtnRegistrar() {
		return btnRegistrar;
	}
}
