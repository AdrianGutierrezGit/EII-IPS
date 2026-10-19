package giis.demo.tkrun.Views;

import java.awt.Color;
import java.awt.Toolkit;
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
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

public class CancelacionInscripcionView extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JLabel lbMisInscrip;
	private JComboBox<String> cbCursosInsc;
	private JButton btSiguiente;
	private JButton btVolver;
	private JLabel lbNombreDelCurso;
	private JTextField tfNombreCurso;
	private JLabel lbFechaCelebracion;
	private JTextField tfFechaCurso;
	private JLabel lbCuota;
	private JTextField tfCuotaCurso;
	private JLabel lbDiasCelebracionCurso;
	private JLabel PorcentDevolucion;
	private JLabel lbDineroDevolver;
	private JTextField tfDiasCelebra;
	private JTextField tfPorcentaje;
	private JTextField tfDineroDevolver;
	private JLabel lbEstadoInscripcion;
	private JTextField tfEstadoInscripcion;

	/**
	 * Create the frame.
	 */
	public CancelacionInscripcionView() {
		setResizable(false);
		setTitle("COIIPA: Cancelación de inscripción");
		setIconImage(Toolkit.getDefaultToolkit()
				.getImage(CancelacionInscripcionView.class.getResource("/imagenes/logo_COIIPA.png")));
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 868, 515);
		contentPane = new JPanel();
		contentPane.setBackground(new Color(255, 255, 255));
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		contentPane.add(getLbMisInscrip());
		contentPane.add(getCbCursosInsc());
		contentPane.add(getBtSiguiente());
		contentPane.add(getBtVolver());
		contentPane.add(getLbNombreDelCurso());
		contentPane.add(getTfNombreCurso());
		contentPane.add(getLbFechaCelebracion());
		contentPane.add(getTfFechaCurso());
		contentPane.add(getLbCuota());
		contentPane.add(getTfCuotaCurso());
		contentPane.add(getLbDiasCelebracionCurso());
		contentPane.add(getPorcentDevolucion());
		contentPane.add(getLbDineroDevolver());
		contentPane.add(getTfDiasCelebra());
		contentPane.add(getTfPorcentaje());
		contentPane.add(getTfDineroDevolver());
		contentPane.add(getLbEstadoInscripcion());
		contentPane.add(getTfEstadoInscripcion());
		setLocationRelativeTo(null);
	}

	private JLabel getLbMisInscrip() {
		if (lbMisInscrip == null) {
			lbMisInscrip = new JLabel("Mis inscripciones:");
			lbMisInscrip.setBounds(54, 24, 166, 27);
		}
		return lbMisInscrip;
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
			btSiguiente = new JButton("Cancelar inscripción");
			btSiguiente.setEnabled(false);
			btSiguiente.setBounds(448, 405, 192, 38);
		}
		return btSiguiente;
	}

	private JButton getBtVolver() {
		if (btVolver == null) {
			btVolver = new JButton("Volver");
			btVolver.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					dispose();
				}
			});
			btVolver.setBounds(290, 405, 148, 38);
		}
		return btVolver;
	}

	public void setCursos(List<String> cursos) {
		getCbCursosInsc().setModel(new DefaultComboBoxModel<String>(cursos.toArray(new String[0])));
		getCbCursosInsc().setSelectedIndex(-1);
	}

	public int preguntarConfirmacion(int dinero) {
		if (dinero == -1) {
			JOptionPane.showMessageDialog(null, "El curso ya se realizó. No es posible anular la inscripción.");
			return -1;
		} else {
			int respuesta = JOptionPane.showConfirmDialog(null,
					"Se te devolverá " + dinero + "€ si anulas la inscripción.\n¿Quieres continuar?",
					"Confirmar Anulación", JOptionPane.YES_NO_OPTION);

			if (respuesta == JOptionPane.YES_OPTION) {
				JOptionPane.showMessageDialog(null, "Inscripción anulada. Se te devolverán " + dinero + "€.");
				return 1;
			} else {
				JOptionPane.showMessageDialog(null, "Operación cancelada. No se realizó ninguna anulación.");
				return -1;
			}
		}
	}

	private JLabel getLbNombreDelCurso() {
		if (lbNombreDelCurso == null) {
			lbNombreDelCurso = new JLabel("Nombre del Curso");
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
			lbFechaCelebracion = new JLabel("Fecha de Celebración");
			lbFechaCelebracion.setBounds(323, 132, 177, 29);
		}
		return lbFechaCelebracion;
	}

	public JTextField getTfFechaCurso() {
		if (tfFechaCurso == null) {
			tfFechaCurso = new JTextField();
			tfFechaCurso.setEditable(false);
			tfFechaCurso.setColumns(10);
			tfFechaCurso.setBounds(323, 160, 177, 29);
		}
		return tfFechaCurso;
	}

	private JLabel getLbCuota() {
		if (lbCuota == null) {
			lbCuota = new JLabel("Cuota del curso");
			lbCuota.setBounds(580, 132, 177, 29);
		}
		return lbCuota;
	}

	public JTextField getTfCuotaCurso() {
		if (tfCuotaCurso == null) {
			tfCuotaCurso = new JTextField();
			tfCuotaCurso.setEditable(false);
			tfCuotaCurso.setColumns(10);
			tfCuotaCurso.setBounds(580, 160, 178, 29);
		}
		return tfCuotaCurso;
	}

	private JLabel getLbDiasCelebracionCurso() {
		if (lbDiasCelebracionCurso == null) {
			lbDiasCelebracionCurso = new JLabel("Días para la celebración");
			lbDiasCelebracionCurso.setBounds(41, 304, 163, 29);
		}
		return lbDiasCelebracionCurso;
	}

	private JLabel getPorcentDevolucion() {
		if (PorcentDevolucion == null) {
			PorcentDevolucion = new JLabel("Porcentaje de devolución");
			PorcentDevolucion.setBounds(323, 304, 177, 29);
		}
		return PorcentDevolucion;
	}

	private JLabel getLbDineroDevolver() {
		if (lbDineroDevolver == null) {
			lbDineroDevolver = new JLabel("Dinero a devolver");
			lbDineroDevolver.setBounds(580, 304, 177, 29);
		}
		return lbDineroDevolver;
	}

	public JTextField getTfDiasCelebra() {
		if (tfDiasCelebra == null) {
			tfDiasCelebra = new JTextField();
			tfDiasCelebra.setEditable(false);
			tfDiasCelebra.setColumns(10);
			tfDiasCelebra.setBounds(41, 330, 213, 29);
		}
		return tfDiasCelebra;
	}

	public JTextField getTfPorcentaje() {
		if (tfPorcentaje == null) {
			tfPorcentaje = new JTextField();
			tfPorcentaje.setEditable(false);
			tfPorcentaje.setColumns(10);
			tfPorcentaje.setBounds(323, 330, 177, 29);
		}
		return tfPorcentaje;
	}

	public JTextField getTfDineroDevolver() {
		if (tfDineroDevolver == null) {
			tfDineroDevolver = new JTextField();
			tfDineroDevolver.setEditable(false);
			tfDineroDevolver.setColumns(10);
			tfDineroDevolver.setBounds(580, 330, 178, 29);
		}
		return tfDineroDevolver;
	}

	private JLabel getLbEstadoInscripcion() {
		if (lbEstadoInscripcion == null) {
			lbEstadoInscripcion = new JLabel("Estado Inscripcion");
			lbEstadoInscripcion.setBounds(41, 218, 163, 29);
		}
		return lbEstadoInscripcion;
	}

	public JTextField getTfEstadoInscripcion() {
		if (tfEstadoInscripcion == null) {
			tfEstadoInscripcion = new JTextField();
			tfEstadoInscripcion.setEditable(false);
			tfEstadoInscripcion.setColumns(10);
			tfEstadoInscripcion.setBounds(41, 244, 213, 29);
		}
		return tfEstadoInscripcion;
	}
}
