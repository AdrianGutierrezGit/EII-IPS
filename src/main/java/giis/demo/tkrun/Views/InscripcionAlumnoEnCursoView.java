package giis.demo.tkrun.Views;

import java.awt.Component;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Toolkit;
import java.util.List;

import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;

import giis.demo.tkrun.DTO.CuotaDTO;
import giis.demo.tkrun.DTO.CursoDTO;

public class InscripcionAlumnoEnCursoView extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JPanel panelCursos;
	private JComboBox<String> comboBoxCursosDisponibles;
	private JPanel panelFormulario;
	private JLabel lblNombre;
	private JLabel lblApellidos;
	private JLabel lblTelefono;
	private JLabel lblCorreo;
	private JTextField textNombre;
	private JTextField textApellido;
	private JTextField textTelefono;
	private JTextField textCorreo;
	private JButton btnInscribir;

	// cuotas
	private JLabel lbCuotasDisponibles;
	private JComboBox<CuotaDTO> cbCuotas;

	// tabla curso
	private JTable tableCurso;
	private JScrollPane scrollPaneCurso;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(() -> {
			try {
				InscripcionAlumnoEnCursoView frame = new InscripcionAlumnoEnCursoView();
				frame.setVisible(true);
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
	}

	public InscripcionAlumnoEnCursoView() {
		setTitle("COIIPA: incripción en curso");
		setIconImage(Toolkit.getDefaultToolkit()
				.getImage(InscripcionAlumnoEnCursoView.class.getResource("/imagenes/logo_COIIPA.png")));
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 885, 443);

		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		contentPane.add(getPanelCursos());
		contentPane.add(getPanelFormulario());
		btnInscribir.setEnabled(false);
	}

	private JPanel getPanelCursos() {
		if (panelCursos == null) {
			panelCursos = new JPanel();
			panelCursos.setBorder(new TitledBorder(null, "Cursos", TitledBorder.LEADING, TitledBorder.TOP, null, null));
			panelCursos.setBounds(10, 32, 860, 150);
			panelCursos.setLayout(null);
			panelCursos.add(getComboBoxCursosDisponibles());
			panelCursos.add(getScrollPaneCurso());
		}
		return panelCursos;
	}

	public JComboBox<String> getComboBoxCursosDisponibles() {
		if (comboBoxCursosDisponibles == null) {
			comboBoxCursosDisponibles = new JComboBox<>();
			comboBoxCursosDisponibles.setBounds(10, 26, 840, 30);
		}
		return comboBoxCursosDisponibles;
	}

	private JScrollPane getScrollPaneCurso() {
		if (scrollPaneCurso == null) {
			scrollPaneCurso = new JScrollPane();
			scrollPaneCurso.setBounds(10, 65, 840, 75);
			scrollPaneCurso.setViewportView(getTableCurso());
		}
		return scrollPaneCurso;
	}

	private JTable getTableCurso() {
		if (tableCurso == null) {
			tableCurso = new JTable();
			tableCurso.setModel(
					new DefaultTableModel(new Object[][] {}, new String[] { "Nombre", "Fecha inicio inscripción",
							"Fecha fin inscripción", "Fecha Inicio Curso", "Fecha Fin Curso", "Lugar" }) {
						/**
						                 * 
						                 */
						private static final long serialVersionUID = 1L;
						boolean[] columnEditables = new boolean[] { false, false, false, false, false };

						public boolean isCellEditable(int row, int column) {
							return columnEditables[column];
						}
					});
		}
		return tableCurso;
	}

	private JPanel getPanelFormulario() {
		if (panelFormulario == null) {
			panelFormulario = new JPanel();
			panelFormulario.setBorder(
					new TitledBorder(null, "Formulario", TitledBorder.LEADING, TitledBorder.TOP, null, null));
			panelFormulario.setBounds(10, 193, 860, 203);
			panelFormulario.setLayout(null);
			panelFormulario.add(getLblNombre());
			panelFormulario.add(getLblApellidos());
			panelFormulario.add(getLblTelefono());
			panelFormulario.add(getLblCorreo());
			panelFormulario.add(getTextNombre());
			panelFormulario.add(getTextApellido());
			panelFormulario.add(getTextTelefono());
			panelFormulario.add(getTextCorreo());
			panelFormulario.add(getBtnNewButton());
			panelFormulario.add(getLbCuotasDisponibles());
			panelFormulario.add(getCbCuotas());
		}
		return panelFormulario;
	}

	private JLabel getLblNombre() {
		if (lblNombre == null) {
			lblNombre = new JLabel("Nombre");
			lblNombre.setFont(new Font("Tahoma", Font.PLAIN, 16));
			lblNombre.setBounds(35, 33, 93, 26);
		}
		return lblNombre;
	}

	private JLabel getLblApellidos() {
		if (lblApellidos == null) {
			lblApellidos = new JLabel("Apellidos");
			lblApellidos.setFont(new Font("Tahoma", Font.PLAIN, 16));
			lblApellidos.setBounds(35, 70, 93, 34);
		}
		return lblApellidos;
	}

	private JLabel getLblTelefono() {
		if (lblTelefono == null) {
			lblTelefono = new JLabel("Telefono");
			lblTelefono.setFont(new Font("Tahoma", Font.PLAIN, 16));
			lblTelefono.setBounds(35, 115, 93, 26);
		}
		return lblTelefono;
	}

	private JLabel getLblCorreo() {
		if (lblCorreo == null) {
			lblCorreo = new JLabel("Correo electronico");
			lblCorreo.setFont(new Font("Tahoma", Font.PLAIN, 16));
			lblCorreo.setBounds(35, 152, 142, 26);
		}
		return lblCorreo;
	}

	public JTextField getTextNombre() {
		if (textNombre == null) {
			textNombre = new JTextField();
			textNombre.setEditable(false);
			textNombre.setBounds(180, 36, 313, 25);
			textNombre.setColumns(10);
		}
		return textNombre;
	}

	public JTextField getTextApellido() {
		if (textApellido == null) {
			textApellido = new JTextField();
			textApellido.setEditable(false);
			textApellido.setColumns(10);
			textApellido.setBounds(180, 78, 313, 25);
		}
		return textApellido;
	}

	public JTextField getTextTelefono() {
		if (textTelefono == null) {
			textTelefono = new JTextField();
			textTelefono.setEditable(false);
			textTelefono.setColumns(10);
			textTelefono.setBounds(180, 115, 313, 25);
		}
		return textTelefono;
	}

	public JTextField getTextCorreo() {
		if (textCorreo == null) {
			textCorreo = new JTextField();
			textCorreo.setEditable(false);
			textCorreo.setColumns(10);
			textCorreo.setBounds(180, 152, 313, 25);
		}
		return textCorreo;
	}

	public JButton getBtnNewButton() {
		if (btnInscribir == null) {
			btnInscribir = new JButton("Inscribirse");
			btnInscribir.setFont(new Font("Tahoma", Font.PLAIN, 15));
			btnInscribir.setBounds(746, 161, 104, 34);
		}
		return btnInscribir;
	}

	public void setTextEditable(boolean W) {
		getTextNombre().setEditable(W);
		getTextApellido().setEditable(W);
		getTextCorreo().setEditable(W);
		getTextTelefono().setEditable(W);
	}

	private JLabel getLbCuotasDisponibles() {
		if (lbCuotasDisponibles == null) {
			lbCuotasDisponibles = new JLabel("Cuota");
			lbCuotasDisponibles.setFont(new Font("Tahoma", Font.PLAIN, 16));
			lbCuotasDisponibles.setBounds(640, 33, 80, 26);
		}
		return lbCuotasDisponibles;
	}

	public JComboBox<CuotaDTO> getCbCuotas() {
		if (cbCuotas == null) {
			cbCuotas = new JComboBox<>();
			cbCuotas.setBounds(640, 64, 190, 25);
			cbCuotas.setRenderer(new ComboCuotaRenderer());
			cbCuotas.setEnabled(true);
		}
		return cbCuotas;
	}

	public void setCuotasDisponibles(List<CuotaDTO> cuotas) {
		DefaultComboBoxModel<CuotaDTO> model = new DefaultComboBoxModel<>();
		if (cuotas != null)
			for (CuotaDTO c : cuotas)
				model.addElement(c);
		getCbCuotas().setModel(model);
		getCbCuotas().setSelectedIndex(-1);
	}

	public CuotaDTO getCuotaSeleccionada() {
		return (CuotaDTO) getCbCuotas().getSelectedItem();
	}

	public int getCuotaSeleccionadaId() {
		CuotaDTO c = getCuotaSeleccionada();
		return c == null ? -1 : c.getId_tipo_cuota();
	}

	public void setCuotaEnabled(boolean enabled) {
		getCbCuotas().setEnabled(enabled);
	}

	public void clearCuotaSeleccion() {
		getCbCuotas().setSelectedIndex(-1);
	}

	private static class ComboCuotaRenderer extends DefaultListCellRenderer {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		@Override
		public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected,
				boolean cellHasFocus) {
			super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
			if (value instanceof CuotaDTO) {
				CuotaDTO c = (CuotaDTO) value;
				setText(c.getNombre() + " - " + c.getPrecio() + " €");
			} else if (value == null) {
				setText("");
			}
			return this;
		}
	}

	/** Mostrar datos del curso seleccionado en la tabla */
	public void mostrarCursoSeleccionado(CursoDTO curso) {
		DefaultTableModel model = (DefaultTableModel) getTableCurso().getModel();
		model.setRowCount(0); // limpiar filas
		if (curso != null) {
			model.addRow(new Object[] { curso.getNombre(), curso.getInicio_plazo_inscripcion(),
					curso.getFin_plazo_inscripcion(), curso.getFecha_inicio(), curso.getFecha_fin(),
					curso.getLugar() });
		}
	}

	public int entrarEnListaDeEspera(String msg) {
		return JOptionPane.showConfirmDialog(null, msg, "Inscripción en curso: lista de espera",
				JOptionPane.YES_NO_OPTION);
	}

	public void mensajeConfirmacionAñadidoAListaDeEspera() {
		JOptionPane.showMessageDialog(null, "Ha sido añadido a la lista de espera",
				"Inscripción curso: lista de espera", JOptionPane.INFORMATION_MESSAGE);

	}
}
