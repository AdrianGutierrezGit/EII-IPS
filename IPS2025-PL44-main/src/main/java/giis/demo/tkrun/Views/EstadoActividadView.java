package giis.demo.tkrun.Views;

import java.awt.Color;
import java.awt.Font;
import java.awt.Toolkit;
import java.util.List;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import giis.demo.tkrun.DTO.CuotaDTO;

public class EstadoActividadView extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel mainPane;
	private JPanel pnMedio;
	private JPanel pnTop;
	private JLabel lbSeleccionCurso;
	private JComboBox<String> comboBox;
	private JLabel lbNombreDelCurso;
	private JTextField tfNombreCurso;
	private JLabel lbFechaIncripcion;
	private JLabel lbFechaCelebracion;
	private JLabel lbNPlazas;
	private JLabel lbNPlazasDisponibles;
	private JLabel lbEstadoCurso;
	private JTextField tfFechaInscripcion;
	private JTextField tfFechaCelebracion;
	private JTextField tfNPlazas;
	private JTextField tfPlazasDisponibles;
	private JTextField tfEstadoCurso;
	private JPanel pnDown;
	private JLabel lbIngresos;
	private JLabel lbGastos;
	private JLabel lbIngrEstimados;
	private JLabel lbGastEstimados;
	private JLabel lbIngrConfirmados;
	private JLabel lblConfirmados;
	private JTextField tfIngEstimados;
	private JTextField tfIngrConfirmados;
	private JTextField tfGastConfirmados;
	private JTextField tfGastEstimados;
	private JLabel lbInscritos;
	private JScrollPane scPnInscritos;
	private DefaultTableModel modeloTablaInscritos;
	private JButton btVolver;
	private JLabel lbCuota;
	private JTextField txCuota;
	private JScrollPane scCuotas;
	private JPanel pnCuotas;
	private JTable tableInscritos;

	/**
	 * Create the frame.
	 */
	public EstadoActividadView() {
		setTitle("COIIPA: Estado de Actividad");
		setIconImage(Toolkit.getDefaultToolkit()
				.getImage(EstadoActividadView.class.getResource("/imagenes/logo_COIIPA.png")));

		setResizable(false);

		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 909, 543);
		mainPane = new JPanel();
		mainPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(mainPane);
		mainPane.setLayout(null);
		mainPane.add(getPnTop());
		mainPane.add(getPnMedio());
		mainPane.add(getPnDown());
		setLocationRelativeTo(null);

	}

	private JPanel getPnMedio() {
		if (pnMedio == null) {
			pnMedio = new JPanel();
			pnMedio.setBackground(new Color(255, 255, 255));
			pnMedio.setBounds(5, 186, 878, 192);
			pnMedio.setLayout(null);
			pnMedio.add(getLbInscritos());
			pnMedio.add(getScPnInscritos());
		}
		return pnMedio;
	}

	private JPanel getPnTop() {
		if (pnTop == null) {
			pnTop = new JPanel();
			pnTop.setBackground(new Color(255, 255, 255));
			pnTop.setBounds(5, 5, 878, 171);
			pnTop.setLayout(null);
			pnTop.add(getLbSeleccionCurso());
			pnTop.add(getComboBox());
			pnTop.add(getLbNombreDelCurso());
			pnTop.add(getTfNombreCurso());
			pnTop.add(getLbFechaIncripcion());
			pnTop.add(getLbFechaCelebracion());
			pnTop.add(getLbNPlazas());
			pnTop.add(getLbNPlazasDisponibles());
			pnTop.add(getLbEstadoCurso());
			pnTop.add(getTfFechaInscripcion());
			pnTop.add(getTfFechaCelebracion());
			pnTop.add(getTfNPlazas());
			pnTop.add(getTfPlazasDisponibles());
			pnTop.add(getTfEstadoCurso());
			pnTop.add(getLbCuota());
			pnTop.add(getTxCuota());
			pnTop.add(getScCuotas());
			getTxCuota().setVisible(false); // ocultar el campo antiguo
			getLbCuota().setText("Cuotas"); // opcional

		}
		return pnTop;
	}

	private JLabel getLbSeleccionCurso() {
		if (lbSeleccionCurso == null) {
			lbSeleccionCurso = new JLabel("Selecciona curso:");
			lbSeleccionCurso.setBounds(10, 11, 122, 29);
			lbSeleccionCurso.setFont(new Font("Tahoma", Font.PLAIN, 12));
		}
		return lbSeleccionCurso;
	}

	public JComboBox<String> getComboBox() {
		if (comboBox == null) {
			comboBox = new JComboBox<String>();
			comboBox.setBounds(154, 11, 418, 29);
		}
		return comboBox;
	}

	private JLabel getLbNombreDelCurso() {
		if (lbNombreDelCurso == null) {
			lbNombreDelCurso = new JLabel("Nombre del Curso");
			lbNombreDelCurso.setBounds(10, 43, 163, 29);
		}
		return lbNombreDelCurso;
	}

	public JTextField getTfNombreCurso() {
		if (tfNombreCurso == null) {
			tfNombreCurso = new JTextField();
			tfNombreCurso.setBounds(10, 71, 213, 29);
			tfNombreCurso.setEditable(false);
			tfNombreCurso.setColumns(10);
		}
		return tfNombreCurso;
	}

	private JLabel getLbFechaIncripcion() {
		if (lbFechaIncripcion == null) {
			lbFechaIncripcion = new JLabel("Fecha de Inscripción");
			lbFechaIncripcion.setBounds(395, 43, 177, 29);
		}
		return lbFechaIncripcion;
	}

	private JLabel getLbFechaCelebracion() {
		if (lbFechaCelebracion == null) {
			lbFechaCelebracion = new JLabel("Fecha de Celebración");
			lbFechaCelebracion.setBounds(395, 104, 177, 29);
		}
		return lbFechaCelebracion;
	}

	private JLabel getLbNPlazas() {
		if (lbNPlazas == null) {
			lbNPlazas = new JLabel("Número de Plazas");
			lbNPlazas.setBounds(259, 43, 112, 29);
		}
		return lbNPlazas;
	}

	private JLabel getLbNPlazasDisponibles() {
		if (lbNPlazasDisponibles == null) {
			lbNPlazasDisponibles = new JLabel("Plazas disponibles");
			lbNPlazasDisponibles.setBounds(259, 104, 112, 29);
		}
		return lbNPlazasDisponibles;
	}

	private JLabel getLbEstadoCurso() {
		if (lbEstadoCurso == null) {
			lbEstadoCurso = new JLabel("Estado del curso");
			lbEstadoCurso.setBounds(10, 104, 163, 29);
		}
		return lbEstadoCurso;
	}

	public JTextField getTfFechaInscripcion() {
		if (tfFechaInscripcion == null) {
			tfFechaInscripcion = new JTextField();
			tfFechaInscripcion.setBounds(394, 71, 178, 29);
			tfFechaInscripcion.setEditable(false);
			tfFechaInscripcion.setColumns(10);
		}
		return tfFechaInscripcion;
	}

	public JTextField getTfFechaCelebracion() {
		if (tfFechaCelebracion == null) {
			tfFechaCelebracion = new JTextField();
			tfFechaCelebracion.setBounds(395, 132, 177, 29);
			tfFechaCelebracion.setEditable(false);
			tfFechaCelebracion.setColumns(10);
		}
		return tfFechaCelebracion;
	}

	public JTextField getTfNPlazas() {
		if (tfNPlazas == null) {
			tfNPlazas = new JTextField();
			tfNPlazas.setBounds(259, 71, 112, 29);
			tfNPlazas.setEditable(false);
			tfNPlazas.setColumns(10);
		}
		return tfNPlazas;
	}

	public JTextField getTfPlazasDisponibles() {
		if (tfPlazasDisponibles == null) {
			tfPlazasDisponibles = new JTextField();
			tfPlazasDisponibles.setBounds(259, 132, 112, 29);
			tfPlazasDisponibles.setEditable(false);
			tfPlazasDisponibles.setColumns(10);
		}
		return tfPlazasDisponibles;
	}

	public JTextField getTfEstadoCurso() {
		if (tfEstadoCurso == null) {
			tfEstadoCurso = new JTextField();
			tfEstadoCurso.setBounds(10, 132, 213, 29);
			tfEstadoCurso.setEditable(false);
			tfEstadoCurso.setColumns(10);
		}
		return tfEstadoCurso;
	}

	private JPanel getPnDown() {
		if (pnDown == null) {
			pnDown = new JPanel();
			pnDown.setBackground(new Color(255, 255, 255));
			pnDown.setBounds(5, 388, 878, 111);
			pnDown.setLayout(null);
			pnDown.add(getLbIngresos());
			pnDown.add(getLbGastos());
			pnDown.add(getLbIngrEstimados());
			pnDown.add(getLbGastEstimados());
			pnDown.add(getLbIngrConfirmados());
			pnDown.add(getLblConfirmados());
			pnDown.add(getTfIngEstimados());
			pnDown.add(getTfIngrConfirmados());
			pnDown.add(getTfGastConfirmados());
			pnDown.add(getTfGastEstimados());
			pnDown.add(getBtVolver());
		}
		return pnDown;
	}

	private JLabel getLbIngresos() {
		if (lbIngresos == null) {
			lbIngresos = new JLabel("Ingresos");
			lbIngresos.setFont(new Font("Tahoma", Font.BOLD, 12));
			lbIngresos.setBounds(143, 10, 75, 18);
		}
		return lbIngresos;
	}

	private JLabel getLbGastos() {
		if (lbGastos == null) {
			lbGastos = new JLabel("Gastos");
			lbGastos.setFont(new Font("Tahoma", Font.BOLD, 12));
			lbGastos.setBounds(414, 10, 75, 18);
		}
		return lbGastos;
	}

	private JLabel getLbIngrEstimados() {
		if (lbIngrEstimados == null) {
			lbIngrEstimados = new JLabel("Estimados");
			lbIngrEstimados.setBounds(81, 28, 63, 23);
		}
		return lbIngrEstimados;
	}

	private JLabel getLbGastEstimados() {
		if (lbGastEstimados == null) {
			lbGastEstimados = new JLabel("Estimados");
			lbGastEstimados.setBounds(349, 28, 63, 23);
		}
		return lbGastEstimados;
	}

	private JLabel getLbIngrConfirmados() {
		if (lbIngrConfirmados == null) {
			lbIngrConfirmados = new JLabel("Confirmados");
			lbIngrConfirmados.setBounds(211, 27, 87, 23);
		}
		return lbIngrConfirmados;
	}

	private JLabel getLblConfirmados() {
		if (lblConfirmados == null) {
			lblConfirmados = new JLabel("Confirmados");
			lblConfirmados.setBounds(475, 28, 87, 23);
		}
		return lblConfirmados;
	}

	public JTextField getTfIngEstimados() {
		if (tfIngEstimados == null) {
			tfIngEstimados = new JTextField();
			tfIngEstimados.setEditable(false);
			tfIngEstimados.setBounds(79, 50, 96, 18);
			tfIngEstimados.setColumns(10);
		}
		return tfIngEstimados;
	}

	public JTextField getTfIngrConfirmados() {
		if (tfIngrConfirmados == null) {
			tfIngrConfirmados = new JTextField();
			tfIngrConfirmados.setEditable(false);
			tfIngrConfirmados.setColumns(10);
			tfIngrConfirmados.setBounds(209, 50, 96, 18);
		}
		return tfIngrConfirmados;
	}

	public JTextField getTfGastConfirmados() {
		if (tfGastConfirmados == null) {
			tfGastConfirmados = new JTextField();
			tfGastConfirmados.setEditable(false);
			tfGastConfirmados.setColumns(10);
			tfGastConfirmados.setBounds(473, 50, 96, 18);
		}
		return tfGastConfirmados;
	}

	public JTextField getTfGastEstimados() {
		if (tfGastEstimados == null) {
			tfGastEstimados = new JTextField();
			tfGastEstimados.setEditable(false);
			tfGastEstimados.setColumns(10);
			tfGastEstimados.setBounds(347, 50, 96, 18);
		}
		return tfGastEstimados;
	}

	private JLabel getLbInscritos() {
		if (lbInscritos == null) {
			lbInscritos = new JLabel("Inscritos:");
			lbInscritos.setBounds(34, 10, 94, 17);
		}
		return lbInscritos;
	}

	public void setNombresCursos(List<String> nombresCursos) {
		// getComboBox().setModel(new nombresCursos); comboBox.setModel(new
		// DefaultComboBoxModel(new String[] {"hola, adios"}));
		getComboBox().setModel(new DefaultComboBoxModel<String>(nombresCursos.toArray(new String[0])));
		;
		getComboBox().setSelectedIndex(-1);
	}

	private JScrollPane getScPnInscritos() {
		if (scPnInscritos == null) {
			scPnInscritos = new JScrollPane();
			scPnInscritos.setBounds(34, 37, 536, 129);
			scPnInscritos.setViewportView(getTableInscritos());
		}
		return scPnInscritos;
	}

	public void setCuotas(List<CuotaDTO> cuotas) {
		JPanel cont = getPnCuotas();
		cont.removeAll();

		if (cuotas != null) {
			for (CuotaDTO c : cuotas) {
				JPanel fila = new JPanel(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 8, 2));
				fila.setOpaque(false);

				JLabel lbNombre = new JLabel(c.getNombre());
				JTextField tfPrecio = new JTextField(c.getPrecio() + " €");
				tfPrecio.setEditable(false);
				tfPrecio.setColumns(6);

				fila.add(lbNombre);
				fila.add(tfPrecio);
				cont.add(fila);
			}
		}
		cont.revalidate();
		cont.repaint();
	}

	private JScrollPane getScCuotas() {
		if (scCuotas == null) {
			scCuotas = new JScrollPane(getPnCuotas());
			scCuotas.setBounds(600, 71, 250, 90); // ajusta si quieres más
													// alto/ancho
			scCuotas.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		}
		return scCuotas;
	}

	private JPanel getPnCuotas() {
		if (pnCuotas == null) {
			pnCuotas = new JPanel();
			pnCuotas.setBackground(Color.WHITE);
			pnCuotas.setLayout(new javax.swing.BoxLayout(pnCuotas, javax.swing.BoxLayout.Y_AXIS));
		}
		return pnCuotas;
	}

	public DefaultTableModel getModelTablaInscritos() {
		return modeloTablaInscritos;
	}

	private JButton getBtVolver() {
		if (btVolver == null) {
			btVolver = new JButton("Volver");
			btVolver.setBounds(690, 80, 96, 20);
			btVolver.addActionListener(e -> dispose());
		}
		return btVolver;
	}

	private JLabel getLbCuota() {
		if (lbCuota == null) {
			lbCuota = new JLabel("Cuota del curso");
			lbCuota.setBounds(600, 43, 177, 29);
		}
		return lbCuota;
	}

	public JTextField getTxCuota() {
		if (txCuota == null) {
			txCuota = new JTextField();
			txCuota.setEditable(false);
			txCuota.setColumns(10);
			txCuota.setBounds(600, 71, 178, 29);
		}
		return txCuota;
	}

	private JTable getTableInscritos() {
		if (tableInscritos == null) {

			modeloTablaInscritos = new DefaultTableModel(
					new String[] { "Nombre y apellidos", "Fecha de inscripción", "Estado de la inscripción" }, 0);

			tableInscritos = new JTable(modeloTablaInscritos);
			tableInscritos.setDefaultEditor(Object.class, null);
		}
		return tableInscritos;
	}
}
