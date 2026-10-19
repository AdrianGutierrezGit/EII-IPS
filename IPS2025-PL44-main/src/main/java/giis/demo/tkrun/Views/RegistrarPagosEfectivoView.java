package giis.demo.tkrun.Views;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Toolkit;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.border.EmptyBorder;
import javax.swing.border.EtchedBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;

public class RegistrarPagosEfectivoView extends JFrame {

	private static final long serialVersionUID = 1L;

	// Panels principales
	private JPanel pnRegistrarPagos;
	private JPanel pnCursos;
	private JPanel pnInscripcionesAlCurso;
	private JPanel pnRegistrarPago;
	private JPanel pnPagosInscripcion;

	// Scrolls
	private JScrollPane spCursos;
	private JScrollPane spInscripcionesAlCurso;
	private JScrollPane spPagosInscripcion;

	// Tablas
	private JTable tablaCursos;
	private JTable tablaInscripcionesCurso;
	private JTable tablaPagosInscripcion;

	// Inputs y labels
	private JLabel lbCuotaInscripcion;
	private JTextField txCuotaInscripcion;

	private JLabel lbCantidadPagada;
	private JTextField txCantidadPagada;

	private JLabel lbFechaPago;
	private JTextField txFechaPago;

	private JButton btnRegistrarPago;

	/**
	 * Main
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(() -> {
			try {
				RegistrarPagosEfectivoView frame = new RegistrarPagosEfectivoView();
				frame.setVisible(true);
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
	}

	/**
	 * Constructor
	 */
	public RegistrarPagosEfectivoView() {
		setIconImage(Toolkit.getDefaultToolkit()
				.getImage(RegistrarPagosEfectivoView.class.getResource("/imagenes/logo_COIIPA.png")));
		setTitle("COIIPA: Registrar pagos en efectivo");
		setResizable(false);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

		// Nuevo tamaño más compacto
		setBounds(100, 100, 900, 638);

		pnRegistrarPagos = new JPanel();
		pnRegistrarPagos.setForeground(Color.BLACK);
		pnRegistrarPagos.setBackground(Color.WHITE);
		pnRegistrarPagos.setBorder(new EmptyBorder(10, 10, 10, 10));
		pnRegistrarPagos.setLayout(null);

		setContentPane(pnRegistrarPagos);

		// Añadimos componentes en el nuevo orden
		pnRegistrarPagos.add(getPnCursos());
		pnRegistrarPagos.add(getPnInscripcionesAlCurso());
		pnRegistrarPagos.add(getLbCuotaInscripcion());
		pnRegistrarPagos.add(getTxCuotaInscripcion());
		pnRegistrarPagos.add(getPnRegistrarPago());
		pnRegistrarPagos.add(getPnPagosInscripcion());
		pnRegistrarPagos.add(getBtnRegistrarPago());

		// ===== NUEVA DISTRIBUCIÓN =====

		// Cursos arriba ocupando todo el ancho
		getPnCursos().setBounds(20, 11, 840, 151);

		// Inscripciones debajo ocupando todo el ancho
		getPnInscripcionesAlCurso().setBounds(20, 173, 840, 143);

		// Cuota inscripción debajo de las dos tablas
		getLbCuotaInscripcion().setBounds(30, 337, 120, 30);
		getTxCuotaInscripcion().setBounds(160, 338, 120, 30);

		// Panel de registrar pago (izquierda)
		getPnRegistrarPago().setBounds(20, 389, 350, 150);

		// Panel de pagos de la inscripción (derecha)
		getPnPagosInscripcion().setBounds(390, 389, 470, 150);

		// Botón registrar pago
		getBtnRegistrarPago().setBounds(650, 560, 210, 30);

		setLocationRelativeTo(null);
	}

	// ----------------------------------------------------------- BOTÓN
	// -----------------------------------------------------------

	public JButton getBtnRegistrarPago() {
		if (btnRegistrarPago == null) {
			btnRegistrarPago = new JButton("Registrar Pago");
			btnRegistrarPago.setFont(new Font("Tahoma", Font.PLAIN, 12));
			btnRegistrarPago.setEnabled(false);
		}
		return btnRegistrarPago;
	}

	// ----------------------------------------------------------- PANEL CURSOS
	// -----------------------------------------------------------

	private JPanel getPnCursos() {
		if (pnCursos == null) {
			pnCursos = new JPanel();
			pnCursos.setBackground(Color.WHITE);
			pnCursos.setBorder(
					new TitledBorder(new EtchedBorder(EtchedBorder.LOWERED, Color.WHITE, new Color(160, 160, 160)),
							"Seleccione un curso:", TitledBorder.LEFT, TitledBorder.TOP, null, Color.BLACK));
			pnCursos.setLayout(new GridLayout(1, 0));
			pnCursos.add(getSpCursos());
		}
		return pnCursos;
	}

	private JScrollPane getSpCursos() {
		if (spCursos == null) {
			spCursos = new JScrollPane();
			spCursos.setViewportView(getTablaCursos());
			spCursos.getViewport().setBackground(Color.WHITE);
		}
		return spCursos;
	}

	public JTable getTablaCursos() {
		if (tablaCursos == null) {
			tablaCursos = new JTable();
			tablaCursos.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			tablaCursos.setRowHeight(20);
			tablaCursos.setGridColor(new Color(200, 200, 200)); // líneas suaves
			tablaCursos.setDefaultEditor(Object.class, null);
		}
		return tablaCursos;
	}

	public void actualizarTablaCursos(DefaultTableModel modelo) {
		tablaCursos.setModel(modelo);
		spCursos.revalidate();
		spCursos.repaint();
	}

	// ----------------------------------------------------------- PANEL
	// INSCRIPCIONES -----------------------------------------------------------

	private JPanel getPnInscripcionesAlCurso() {
		if (pnInscripcionesAlCurso == null) {
			pnInscripcionesAlCurso = new JPanel();
			pnInscripcionesAlCurso.setBackground(Color.WHITE);
			pnInscripcionesAlCurso.setBorder(new TitledBorder(
					new EtchedBorder(EtchedBorder.LOWERED, Color.WHITE, new Color(160, 160, 160)),
					"Inscripciones a registrar el pago", TitledBorder.LEADING, TitledBorder.TOP, null, Color.BLACK));
			pnInscripcionesAlCurso.setLayout(new GridLayout(1, 0));
			pnInscripcionesAlCurso.add(getSpInscripcionesAlCurso());
		}
		return pnInscripcionesAlCurso;
	}

	private JScrollPane getSpInscripcionesAlCurso() {
		if (spInscripcionesAlCurso == null) {
			spInscripcionesAlCurso = new JScrollPane();
			spInscripcionesAlCurso.setViewportView(getTablaInscripcionesCurso());
			spInscripcionesAlCurso.getViewport().setBackground(Color.WHITE);
		}
		return spInscripcionesAlCurso;
	}

	public JTable getTablaInscripcionesCurso() {
		if (tablaInscripcionesCurso == null) {
			tablaInscripcionesCurso = new JTable();
			tablaInscripcionesCurso.setRowHeight(20);
			tablaInscripcionesCurso.setGridColor(new Color(200, 200, 200));
			tablaInscripcionesCurso.setDefaultEditor(Object.class, null);
		}
		return tablaInscripcionesCurso;
	}

	public void actualizarTablaInscripcionesCurso(DefaultTableModel modelo) {
		tablaInscripcionesCurso.setModel(modelo);
		spInscripcionesAlCurso.revalidate();
		spInscripcionesAlCurso.repaint();
	}

	// ----------------------------------------------------------- CUOTA
	// INSCRIPCIÓN -----------------------------------------------------------

	private JLabel getLbCuotaInscripcion() {
		if (lbCuotaInscripcion == null) {
			lbCuotaInscripcion = new JLabel("Cuota inscripción:");
			lbCuotaInscripcion.setFont(new Font("Tahoma", Font.BOLD, 12));
		}
		return lbCuotaInscripcion;
	}

	public JTextField getTxCuotaInscripcion() {
		if (txCuotaInscripcion == null) {
			txCuotaInscripcion = new JTextField();
			txCuotaInscripcion.setEditable(false);
			txCuotaInscripcion.setBorder(null);
			txCuotaInscripcion.setBackground(Color.WHITE);
		}
		return txCuotaInscripcion;
	}

	// ----------------------------------------------------------- PANEL
	// REGISTRAR PAGO
	// -----------------------------------------------------------

	private JPanel getPnRegistrarPago() {
		if (pnRegistrarPago == null) {
			pnRegistrarPago = new JPanel();
			pnRegistrarPago.setBackground(Color.WHITE);
			pnRegistrarPago.setBorder(
					new TitledBorder(new EtchedBorder(EtchedBorder.LOWERED, Color.WHITE, new Color(160, 160, 160)),
							"Datos del pago:", TitledBorder.LEADING, TitledBorder.TOP, null, Color.BLACK));
			pnRegistrarPago.setLayout(null);

			pnRegistrarPago.add(getLbCantidadPagada());
			pnRegistrarPago.add(getTxCantidadPagada());
			pnRegistrarPago.add(getLbFechaPago());
			pnRegistrarPago.add(getTxFechaPago());
		}
		return pnRegistrarPago;
	}

	private JLabel getLbCantidadPagada() {
		if (lbCantidadPagada == null) {
			lbCantidadPagada = new JLabel("Cantidad Pagada:");
			lbCantidadPagada.setFont(new Font("Tahoma", Font.PLAIN, 12));
			lbCantidadPagada.setBounds(20, 32, 120, 19);
		}
		return lbCantidadPagada;
	}

	public JTextField getTxCantidadPagada() {
		if (txCantidadPagada == null) {
			txCantidadPagada = new JTextField();
			txCantidadPagada.setBorder(new LineBorder(new Color(171, 173, 179)));
			txCantidadPagada.setBounds(140, 32, 155, 19);
		}
		return txCantidadPagada;
	}

	private JLabel getLbFechaPago() {
		if (lbFechaPago == null) {
			lbFechaPago = new JLabel("Fecha del pago:");
			lbFechaPago.setFont(new Font("Tahoma", Font.PLAIN, 12));
			lbFechaPago.setBounds(20, 95, 120, 19);
		}
		return lbFechaPago;
	}

	public JTextField getTxFechaPago() {
		if (txFechaPago == null) {
			txFechaPago = new JTextField();
			txFechaPago.setBorder(new LineBorder(new Color(171, 173, 179)));
			txFechaPago.setBounds(140, 95, 155, 19);
		}
		return txFechaPago;
	}

	// ----------------------------------------------------------- PANEL PAGOS
	// INSCRIPCIÓN -----------------------------------------------------------

	private JPanel getPnPagosInscripcion() {
		if (pnPagosInscripcion == null) {
			pnPagosInscripcion = new JPanel();
			pnPagosInscripcion.setBackground(Color.WHITE);
			pnPagosInscripcion.setBorder(new TitledBorder(null, "Movimientos de dinero:", TitledBorder.LEADING,
					TitledBorder.TOP, null, null));
			pnPagosInscripcion.setLayout(new GridLayout(1, 0));
			pnPagosInscripcion.add(getSpPagosInscripcion());
		}
		return pnPagosInscripcion;
	}

	private JScrollPane getSpPagosInscripcion() {
		if (spPagosInscripcion == null) {
			spPagosInscripcion = new JScrollPane();
			spPagosInscripcion.setViewportView(getTablaPagosInscripcion());
			spPagosInscripcion.getViewport().setBackground(Color.WHITE);
		}
		return spPagosInscripcion;
	}

	public JTable getTablaPagosInscripcion() {
		if (tablaPagosInscripcion == null) {
			tablaPagosInscripcion = new JTable();
			tablaPagosInscripcion.setRowHeight(20);
			tablaPagosInscripcion.setGridColor(new Color(200, 200, 200));
		}
		return tablaPagosInscripcion;
	}

	public void actualizarTablaPagosInscripcion(DefaultTableModel modelo) {
		tablaPagosInscripcion.setModel(modelo);
		spPagosInscripcion.revalidate();
		spPagosInscripcion.repaint();
	}
}
