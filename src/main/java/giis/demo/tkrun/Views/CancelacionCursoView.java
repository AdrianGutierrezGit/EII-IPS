package giis.demo.tkrun.Views;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.text.MaskFormatter;
import javax.swing.JTextField;
import java.awt.Color;
import java.awt.Toolkit;
import java.awt.Font;
import javax.swing.SwingConstants;
import javax.swing.JTextArea;
import javax.swing.JFormattedTextField;

public class CancelacionCursoView extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JLabel lbCursos;
	private JComboBox<String> cbCursosInsc;
	private JButton btSiguiente;
	private JButton btCancelar;
	private JLabel lbNombreDelCurso;
	private JTextField tfNombreCurso;
	private JLabel lbFechaCelebracion;
	private JTextField tfFechaCurso;
	private JLabel lbEstadoCurso;
	private JTextField tfEstadoInscripcion;
	private JLabel lbNPlazas;
	private JTextField tfNPlazas;
	private JLabel lbNPlazasDisponibles;
	private JTextField tfPlazasDisponibles;
	private JLabel lbIngrEstimados;
	private JTextField tfIngEstimados;
	private JLabel lbIngresos;
	private JTextField tfIngrConfirmados;
	private JLabel lbIngrConfirmados;
	private JTextField tfGastEstimados;
	private JLabel lbGastEstimados;
	private JLabel lbGastos;
	private JTextField tfGastConfirmados;
	private JLabel lblConfirmados;
	private JLabel lbPorcentaje;
	private JTextArea txtrAyuda;
	private JFormattedTextField tfPorcentaje;

	/**
	 * Create the frame.
	 */
	public CancelacionCursoView() {
		setResizable(false);
		setTitle("COIIPA: Cancelación de curso");
		setIconImage(Toolkit.getDefaultToolkit()
				.getImage(CancelacionCursoView.class.getResource("/imagenes/logo_COIIPA.png")));
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 895, 523);
		contentPane = new JPanel();
		contentPane.setBackground(new Color(255, 255, 255));
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		contentPane.add(getLbCursos());
		contentPane.add(getCbCursosInsc());
		contentPane.add(getBtSiguiente());
		contentPane.add(getBtCancelar());
		contentPane.add(getLbNombreDelCurso());
		contentPane.add(getTfNombreCurso());
		contentPane.add(getLbFechaCelebracion());
		contentPane.add(getTfFechaCurso());
		contentPane.add(getLbEstadoCurso());
		contentPane.add(getTfEstadoInscripcion());
		contentPane.add(getLbNPlazas());
		contentPane.add(getTfNPlazas());
		contentPane.add(getLbNPlazasDisponibles());
		contentPane.add(getTfPlazasDisponibles());
		contentPane.add(getLbIngrEstimados());
		contentPane.add(getTfIngEstimados());
		contentPane.add(getLbIngresos());
		contentPane.add(getTfIngrConfirmados());
		contentPane.add(getLbIngrConfirmados());
		contentPane.add(getTfGastEstimados());
		contentPane.add(getLbGastEstimados());
		contentPane.add(getLbGastos());
		contentPane.add(getTfGastConfirmados());
		contentPane.add(getLblConfirmados());
		contentPane.add(getLbPorcentaje());
		contentPane.add(getTxtrAyuda());
		contentPane.add(getTfPorcentaje());
		setLocationRelativeTo(null);
	}

	private JLabel getLbCursos() {
		if (lbCursos == null) {
			lbCursos = new JLabel("Cursos:");
			lbCursos.setBounds(54, 24, 166, 27);
		}
		return lbCursos;
	}

	public JComboBox<String> getCbCursosInsc() {
		if (cbCursosInsc == null) {
			cbCursosInsc = new JComboBox<String>();
			cbCursosInsc.setBounds(54, 48, 482, 27);
		}
		return cbCursosInsc;
	}

	public JButton getBtSiguiente() {
		if (btSiguiente == null) {
			btSiguiente = new JButton("Cancelar curso");
			btSiguiente.setEnabled(false);
			btSiguiente.setBounds(344, 436, 192, 38);
		}
		return btSiguiente;
	}

	private JButton getBtCancelar() {
		if (btCancelar == null) {
			btCancelar = new JButton("Volver");
			btCancelar.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					dispose();
				}
			});
			btCancelar.setBounds(180, 436, 148, 38);
		}
		return btCancelar;
	}

	public void setCursos(List<String> cursos) {
		getCbCursosInsc().setModel(new DefaultComboBoxModel<String>(cursos.toArray(new String[0])));
		getCbCursosInsc().setSelectedIndex(-1);
	}

	// public int preguntarConfirmacion(int dinero) {
	// if (dinero == -1) {
	// JOptionPane.showMessageDialog(null, "El curso ya se realizó. No es
	// posible anular la inscripción.");
	// return -1;
	// } else {
	// int respuesta = JOptionPane.showConfirmDialog(null,
	// "Se te devolverá " + dinero + "€ si anulas la inscripción.\n¿Quieres
	// continuar?",
	// "Confirmar Anulación", JOptionPane.YES_NO_OPTION);
	//
	// if (respuesta == JOptionPane.YES_OPTION) {
	// JOptionPane.showMessageDialog(null, "Inscripción anulada. Se te
	// devolverán " + dinero + "€.");
	// return 1;
	// } else {
	// JOptionPane.showMessageDialog(null, "Operación cancelada. No se realizó
	// ninguna anulación.");
	// return -1;
	// }
	// }
	// }
	private JLabel getLbNombreDelCurso() {
		if (lbNombreDelCurso == null) {
			lbNombreDelCurso = new JLabel("Nombre del Curso:");
			lbNombreDelCurso.setBounds(41, 132, 163, 29);
		}
		return lbNombreDelCurso;
	}

	public JTextField getTfNombreCurso() {
		if (tfNombreCurso == null) {
			tfNombreCurso = new JTextField();
			tfNombreCurso.setEditable(false);
			tfNombreCurso.setColumns(10);
			tfNombreCurso.setBounds(41, 160, 213, 29);
		}
		return tfNombreCurso;
	}

	private JLabel getLbFechaCelebracion() {
		if (lbFechaCelebracion == null) {
			lbFechaCelebracion = new JLabel("Fecha de Celebración:");
			lbFechaCelebracion.setBounds(323, 132, 177, 29);
		}
		return lbFechaCelebracion;
	}

	public JTextField getTfFechaCurso() {
		if (tfFechaCurso == null) {
			tfFechaCurso = new JTextField();
			tfFechaCurso.setEditable(false);
			tfFechaCurso.setColumns(10);
			tfFechaCurso.setBounds(323, 160, 260, 29);
		}
		return tfFechaCurso;
	}

	private JLabel getLbEstadoCurso() {
		if (lbEstadoCurso == null) {
			lbEstadoCurso = new JLabel("Estado curso:");
			lbEstadoCurso.setBounds(41, 210, 163, 29);
		}
		return lbEstadoCurso;
	}

	public JTextField getTfEstadoInscripcion() {
		if (tfEstadoInscripcion == null) {
			tfEstadoInscripcion = new JTextField();
			tfEstadoInscripcion.setEditable(false);
			tfEstadoInscripcion.setColumns(10);
			tfEstadoInscripcion.setBounds(41, 236, 213, 29);
		}
		return tfEstadoInscripcion;
	}

	public void showMensaje(String string) {
		JOptionPane.showMessageDialog(null, string);
	}

	private JLabel getLbNPlazas() {
		if (lbNPlazas == null) {
			lbNPlazas = new JLabel("Número de Plazas:");
			lbNPlazas.setBounds(323, 210, 122, 29);
		}
		return lbNPlazas;
	}

	public JTextField getTfNPlazas() {
		if (tfNPlazas == null) {
			tfNPlazas = new JTextField();
			tfNPlazas.setEditable(false);
			tfNPlazas.setColumns(10);
			tfNPlazas.setBounds(323, 236, 122, 29);
		}
		return tfNPlazas;
	}

	private JLabel getLbNPlazasDisponibles() {
		if (lbNPlazasDisponibles == null) {
			lbNPlazasDisponibles = new JLabel("Plazas disponibles:");
			lbNPlazasDisponibles.setBounds(461, 210, 122, 29);
		}
		return lbNPlazasDisponibles;
	}

	public JTextField getTfPlazasDisponibles() {
		if (tfPlazasDisponibles == null) {
			tfPlazasDisponibles = new JTextField();
			tfPlazasDisponibles.setEditable(false);
			tfPlazasDisponibles.setColumns(10);
			tfPlazasDisponibles.setBounds(461, 236, 122, 29);
		}
		return tfPlazasDisponibles;
	}

	private JLabel getLbIngrEstimados() {
		if (lbIngrEstimados == null) {
			lbIngrEstimados = new JLabel("Estimados:");
			lbIngrEstimados.setBounds(56, 353, 94, 23);
		}
		return lbIngrEstimados;
	}

	public JTextField getTfIngEstimados() {
		if (tfIngEstimados == null) {
			tfIngEstimados = new JTextField();
			tfIngEstimados.setEditable(false);
			tfIngEstimados.setColumns(10);
			tfIngEstimados.setBounds(54, 375, 96, 18);
		}
		return tfIngEstimados;
	}

	private JLabel getLbIngresos() {
		if (lbIngresos == null) {
			lbIngresos = new JLabel("Ingresos");
			lbIngresos.setFont(new Font("Tahoma", Font.BOLD, 12));
			lbIngresos.setBounds(118, 335, 75, 18);
		}
		return lbIngresos;
	}

	public JTextField getTfIngrConfirmados() {
		if (tfIngrConfirmados == null) {
			tfIngrConfirmados = new JTextField();
			tfIngrConfirmados.setEditable(false);
			tfIngrConfirmados.setColumns(10);
			tfIngrConfirmados.setBounds(184, 375, 96, 18);
		}
		return tfIngrConfirmados;
	}

	private JLabel getLbIngrConfirmados() {
		if (lbIngrConfirmados == null) {
			lbIngrConfirmados = new JLabel("Confirmados:");
			lbIngrConfirmados.setBounds(186, 352, 87, 23);
		}
		return lbIngrConfirmados;
	}

	public JTextField getTfGastEstimados() {
		if (tfGastEstimados == null) {
			tfGastEstimados = new JTextField();
			tfGastEstimados.setEditable(false);
			tfGastEstimados.setColumns(10);
			tfGastEstimados.setBounds(322, 375, 96, 18);
		}
		return tfGastEstimados;
	}

	private JLabel getLbGastEstimados() {
		if (lbGastEstimados == null) {
			lbGastEstimados = new JLabel("Estimados:");
			lbGastEstimados.setBounds(324, 353, 94, 23);
		}
		return lbGastEstimados;
	}

	private JLabel getLbGastos() {
		if (lbGastos == null) {
			lbGastos = new JLabel("Gastos");
			lbGastos.setFont(new Font("Tahoma", Font.BOLD, 12));
			lbGastos.setBounds(389, 335, 75, 18);
		}
		return lbGastos;
	}

	public JTextField getTfGastConfirmados() {
		if (tfGastConfirmados == null) {
			tfGastConfirmados = new JTextField();
			tfGastConfirmados.setEditable(false);
			tfGastConfirmados.setColumns(10);
			tfGastConfirmados.setBounds(448, 375, 96, 18);
		}
		return tfGastConfirmados;
	}

	private JLabel getLblConfirmados() {
		if (lblConfirmados == null) {
			lblConfirmados = new JLabel("Confirmados:");
			lblConfirmados.setBounds(450, 353, 87, 23);
		}
		return lblConfirmados;
	}

	public JLabel getLbPorcentaje() {
		if (lbPorcentaje == null) {
			lbPorcentaje = new JLabel("Porcentaje de celebración:*");
			lbPorcentaje.setVisible(false);
			lbPorcentaje.setHorizontalAlignment(SwingConstants.TRAILING);
			lbPorcentaje.setBounds(631, 132, 177, 29);
		}
		return lbPorcentaje;
	}

	public JTextArea getTxtrAyuda() {
		if (txtrAyuda == null) {
			txtrAyuda = new JTextArea();
			txtrAyuda.setVisible(false);
			txtrAyuda.setFont(new Font("Tahoma", Font.PLAIN, 10));
			txtrAyuda.setText(
					"*Como el curso esta celebrandose, debe introducir el porcentaje de realización del mismo para calcular las devoluciones.");
			txtrAyuda.setBounds(631, 216, 177, 69);
			txtrAyuda.setEditable(false);
			txtrAyuda.setOpaque(false);
			txtrAyuda.setFocusable(false);
			txtrAyuda.setLineWrap(true);
			txtrAyuda.setWrapStyleWord(true);
		}
		return txtrAyuda;
	}

	// tfPorcentaje.setBounds(660, 161, 148, 27);
	public JFormattedTextField getTfPorcentaje() {
		if (tfPorcentaje == null) {
			try {
				MaskFormatter mf = new MaskFormatter("##%");
				mf.setPlaceholderCharacter('_');
				tfPorcentaje = new JFormattedTextField(mf);
				tfPorcentaje.setVisible(false);
				tfPorcentaje.setHorizontalAlignment(SwingConstants.TRAILING);
				tfPorcentaje.setBounds(660, 161, 148, 27);
				return tfPorcentaje;
			} catch (Exception ex) {
				tfPorcentaje = new JFormattedTextField();
				tfPorcentaje.setBounds(660, 161, 148, 27);
				return tfPorcentaje;
			}

		}
		return tfPorcentaje;
	}
}
