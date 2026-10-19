package giis.demo.tkrun.Views;

import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Toolkit;
import java.text.ParseException;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.text.MaskFormatter;

import net.miginfocom.swing.MigLayout;

public class RetrasarCursoView extends JFrame {

	private static final long serialVersionUID = 1L;

	private final Font fontFechas = new Font("Tahoma", Font.PLAIN, 15);

	private JPanel contentPane;

	// Labels con fechas actuales
	private JLabel lbInscripcionIni;
	private JLabel lbInscripcionFin;
	private JLabel lbFormacionIni;
	private JLabel lbFormacionFin;

	// Campos editables
	private JFormattedTextField tfNuevaInscripcionInicio; // NUEVO
	private JFormattedTextField tfNuevaInscripcionFin;
	private JFormattedTextField tfNuevaFormacionInicio;
	private JFormattedTextField tfNuevaFormacionFin;

	private JButton btnActualizar;

	private JComboBox<String> cbCursos;

	public static void main(String[] args) {
		EventQueue.invokeLater(() -> {
			try {
				RetrasarCursoView frame = new RetrasarCursoView();
				frame.setVisible(true);
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
	}

	public RetrasarCursoView() {
		setIconImage(
				Toolkit.getDefaultToolkit().getImage(RetrasarCursoView.class.getResource("/imagenes/logo_COIIPA.png")));
		setTitle("COIIPA: Retrasar Curso");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 900, 465);

		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(10, 10, 10, 10));
		setContentPane(contentPane);

		contentPane.setLayout(new MigLayout("", "[grow]", "[][][][][][][][][][]"));

		cbCursos = new JComboBox<>();
		cbCursos.setFont(new Font("Tahoma", Font.PLAIN, 15));
		JLabel label_8 = new JLabel("Seleccione Curso:");
		label_8.setFont(new Font("Tahoma", Font.PLAIN, 15));
		contentPane.add(label_8, "cell 0 1,alignx left");
		contentPane.add(cbCursos, "cell 0 2,growx");

		// Panel de fechas actuales
		JPanel panelActuales = new JPanel(new MigLayout("", "[grow][grow]", "[][][][]"));
		panelActuales.setBorder(new TitledBorder("Fechas actuales"));

		JLabel label = new JLabel("Inscripción inicio:");
		label.setFont(new Font("Tahoma", Font.PLAIN, 15));
		panelActuales.add(label, "cell 0 0");
		panelActuales.add(getLbInscripcionIni(), "cell 1 0");

		JLabel label_1 = new JLabel("Inscripción fin:");
		label_1.setFont(new Font("Tahoma", Font.PLAIN, 15));
		panelActuales.add(label_1, "cell 0 1");
		panelActuales.add(getLbInscripcionFin(), "cell 1 1");

		JLabel label_2 = new JLabel("Formación inicio:");
		label_2.setFont(new Font("Tahoma", Font.PLAIN, 15));
		panelActuales.add(label_2, "cell 0 2");
		panelActuales.add(getLbFormacionIni(), "cell 1 2");

		JLabel label_3 = new JLabel("Formación fin:");
		label_3.setFont(new Font("Tahoma", Font.PLAIN, 15));
		panelActuales.add(label_3, "cell 0 3");
		panelActuales.add(getLbFormacionFin(), "cell 1 3");

		contentPane.add(panelActuales, "cell 0 3,growx");

		// Panel de nuevas fechas
		JPanel panelNuevas = new JPanel(new MigLayout("", "[grow][grow]", "[][][][]"));
		panelNuevas.setBorder(new TitledBorder("Nuevas fechas"));

		// Nueva inscripción inicio
		JLabel labelNuevaInscripcionIni = new JLabel("Inscripción inicio:");
		labelNuevaInscripcionIni.setFont(new Font("Tahoma", Font.PLAIN, 15));
		panelNuevas.add(labelNuevaInscripcionIni, "cell 0 0");
		panelNuevas.add(getTfNuevaInscripcionInicio(), "cell 1 0,growx");

		// Inscripción fin
		JLabel label_5 = new JLabel("Inscripción fin:");
		label_5.setFont(new Font("Tahoma", Font.PLAIN, 15));
		panelNuevas.add(label_5, "cell 0 1");
		panelNuevas.add(getTfNuevaInscripcionFin(), "cell 1 1,growx");

		// Formación inicio
		JLabel label_6 = new JLabel("Formación inicio:");
		label_6.setFont(new Font("Tahoma", Font.PLAIN, 15));
		panelNuevas.add(label_6, "cell 0 2");
		panelNuevas.add(getTfNuevaFormacionInicio(), "cell 1 2,growx");

		// Formación fin
		JLabel label_7 = new JLabel("Formación fin:");
		label_7.setFont(new Font("Tahoma", Font.PLAIN, 15));
		panelNuevas.add(label_7, "cell 0 3");
		panelNuevas.add(getTfNuevaFormacionFin(), "cell 1 3,growx");

		contentPane.add(panelNuevas, "cell 0 4,growx");

		contentPane.add(getBtnActualizar(), "cell 0 5,alignx right");
	}

	// ================== GETTERS ==================

	private JLabel getLbInscripcionIni() {
		if (lbInscripcionIni == null) {
			lbInscripcionIni = new JLabel("____-__-__");
			lbInscripcionIni.setFont(fontFechas);
		}
		return lbInscripcionIni;
	}

	private JLabel getLbInscripcionFin() {
		if (lbInscripcionFin == null) {
			lbInscripcionFin = new JLabel("____-__-__");
			lbInscripcionFin.setFont(fontFechas);
		}
		return lbInscripcionFin;
	}

	private JLabel getLbFormacionIni() {
		if (lbFormacionIni == null) {
			lbFormacionIni = new JLabel("____-__-__");
			lbFormacionIni.setFont(fontFechas);
		}
		return lbFormacionIni;
	}

	private JLabel getLbFormacionFin() {
		if (lbFormacionFin == null) {
			lbFormacionFin = new JLabel("____-__-__");
			lbFormacionFin.setFont(fontFechas);
		}
		return lbFormacionFin;
	}

	private JFormattedTextField createFechaField() {
		try {
			MaskFormatter mf = new MaskFormatter("####-##-##");
			mf.setPlaceholderCharacter('_');
			return new JFormattedTextField(mf);
		} catch (ParseException e) {
			return new JFormattedTextField();
		}
	}

	// ================== Getters de campos editables ==================
	public JFormattedTextField getTfNuevaInscripcionInicio() {
		if (tfNuevaInscripcionInicio == null)
			tfNuevaInscripcionInicio = createFechaField();
		return tfNuevaInscripcionInicio;
	}

	public JFormattedTextField getTfNuevaInscripcionFin() {
		if (tfNuevaInscripcionFin == null)
			tfNuevaInscripcionFin = createFechaField();
		return tfNuevaInscripcionFin;
	}

	public JFormattedTextField getTfNuevaFormacionInicio() {
		if (tfNuevaFormacionInicio == null)
			tfNuevaFormacionInicio = createFechaField();
		return tfNuevaFormacionInicio;
	}

	public JFormattedTextField getTfNuevaFormacionFin() {
		if (tfNuevaFormacionFin == null)
			tfNuevaFormacionFin = createFechaField();
		return tfNuevaFormacionFin;
	}

	private JButton getBtnActualizar() {
		if (btnActualizar == null) {
			btnActualizar = new JButton("Actualizar");
			btnActualizar.setFont(new Font("Arial", Font.BOLD, 18));
			btnActualizar.setSize(120, 35);
		}
		return btnActualizar;
	}

	// ================== Getters públicos ==================
	public JLabel getLbInscripcionIniPublic() {
		return lbInscripcionIni;
	}

	public JLabel getLbInscripcionFinPublic() {
		return lbInscripcionFin;
	}

	public JLabel getLbFormacionIniPublic() {
		return lbFormacionIni;
	}

	public JLabel getLbFormacionFinPublic() {
		return lbFormacionFin;
	}

	public JButton getBtnActualizarPublic() {
		return btnActualizar;
	}

	public JComboBox<String> getCbCursos() {
		return cbCursos;
	}

	public JFormattedTextField getTfNuevaInscripcionInicioPublic() {
		return tfNuevaInscripcionInicio;
	}

	public JFormattedTextField getTfNuevaInscripcionFinPublic() {
		return tfNuevaInscripcionFin;
	}

	public JFormattedTextField getTfNuevaFormacionInicioPublic() {
		return tfNuevaFormacionInicio;
	}

	public JFormattedTextField getTfNuevaFormacionFinPublic() {
		return tfNuevaFormacionFin;
	}
}
