package giis.demo.tkrun.Views;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.io.IOException;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.border.EmptyBorder;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;

public class RegistrarPagosView extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel pnRegistrarPagos;
	private JButton btnRegistrarPago;
	private JPanel pnCursos;
	private JPanel pnInscripcionesAlCurso;
	private JScrollPane spInscripcionesAlCurso;
	private JLabel lbCuotaInscripcion;
	private JTextField txCuotaInscripcion;
	private JPanel pnRegistrarPago;
	private JTextField txCantidadPagada;
	private JLabel lbCantidadPagada;
	private JLabel lbFechaPago;
	private JTextField txFechaPago;
	private JTable tablaInscripcionesCurso;
	private JPanel pnTipoPago;
	private JRadioButton rbPagoIngreso;
	private JRadioButton rbPagoDevolucion;
	private ButtonGroup tiposPago = new ButtonGroup();
	private JScrollPane spCursos;
	private JTable tablaCursos;
	private JPanel pnPagosIinscripcion;
	private JScrollPane spPagosInscripcion;
	private JTable tablaPagosInscripcion;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					RegistrarPagosView frame = new RegistrarPagosView();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 * 
	 * @throws IOException
	 */
	public RegistrarPagosView() {
		setResizable(false);
		setIconImage(Toolkit.getDefaultToolkit()
				.getImage(RegistrarPagosView.class.getResource("/imagenes/logo_COIIPA.png")));
		setTitle("COIIPA: Registrar pagos");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 1502, 746);

		pnRegistrarPagos = new JPanel();
		pnRegistrarPagos.setForeground(new Color(0, 0, 0));
		pnRegistrarPagos.setBackground(new Color(255, 255, 255));
		pnRegistrarPagos.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(pnRegistrarPagos);
		pnRegistrarPagos.setLayout(null);
		pnRegistrarPagos.add(getBtnRegistrarPago());
		pnRegistrarPagos.add(getPnCursos());
		pnRegistrarPagos.add(getPnInscripcionesAlCurso());
		pnRegistrarPagos.add(getLbCuotaInscripcion());
		pnRegistrarPagos.add(getTxCuotaInscripcion());
		pnRegistrarPagos.add(getPnRegistrarPago());
		pnRegistrarPagos.add(getPnTipoPago());
		tiposPago.add(getRbPagoIngreso());
		tiposPago.add(getRbPagoDevolucion());
		pnRegistrarPagos.add(getPnPagosIinscripcion());
		setLocationRelativeTo(null);
	}

	public JButton getBtnRegistrarPago() {
		if (btnRegistrarPago == null) {
			btnRegistrarPago = new JButton("Registrar Pago");
			btnRegistrarPago.setFont(new Font("Tahoma", Font.PLAIN, 12));
			btnRegistrarPago.setEnabled(false);
			btnRegistrarPago.setBounds(1295, 615, 152, 37);

		}
		return btnRegistrarPago;
	}

	private JPanel getPnCursos() {
		if (pnCursos == null) {
			pnCursos = new JPanel();
			pnCursos.setBackground(new Color(255, 255, 255));
			pnCursos.setBorder(new TitledBorder(
					new EtchedBorder(EtchedBorder.LOWERED, new Color(255, 255, 255), new Color(160, 160, 160)),
					"Seleccione un curso:", TitledBorder.LEFT, TitledBorder.TOP, null, new Color(0, 0, 0)));
			pnCursos.setBounds(42, 25, 767, 311);

			pnCursos.setLayout(new GridLayout(1, 0, 0, 0));
			pnCursos.add(getSpCursos());
		}
		return pnCursos;
	}

	private JPanel getPnInscripcionesAlCurso() {
		if (pnInscripcionesAlCurso == null) {
			pnInscripcionesAlCurso = new JPanel();
			pnInscripcionesAlCurso.setBorder(new TitledBorder(
					new EtchedBorder(EtchedBorder.LOWERED, new Color(255, 255, 255), new Color(160, 160, 160)),
					"Seleccione la inscripci\u00F3n de la cual quiere resgistrar el pago:", TitledBorder.LEADING,
					TitledBorder.TOP, null, new Color(0, 0, 0)));
			pnInscripcionesAlCurso.setBackground(new Color(255, 255, 255));
			pnInscripcionesAlCurso.setBounds(833, 25, 614, 311);

			pnInscripcionesAlCurso.setLayout(new GridLayout(0, 1, 0, 0));
			pnInscripcionesAlCurso.add(getSpInscripcionesAlCurso());
		}
		return pnInscripcionesAlCurso;
	}

	private JScrollPane getSpInscripcionesAlCurso() {
		if (spInscripcionesAlCurso == null) {
			spInscripcionesAlCurso = new JScrollPane();
			spInscripcionesAlCurso.setBackground(new Color(255, 255, 255));
			spInscripcionesAlCurso.setViewportView(getTablaInscripcionesCurso());
			spInscripcionesAlCurso.getViewport().setBackground(Color.WHITE);
		}
		return spInscripcionesAlCurso;
	}

	private JLabel getLbCuotaInscripcion() {
		if (lbCuotaInscripcion == null) {
			lbCuotaInscripcion = new JLabel("Cuota inscripción:");
			lbCuotaInscripcion.setFont(new Font("Tahoma", Font.BOLD, 12));
			lbCuotaInscripcion.setBounds(42, 372, 113, 19);
		}
		return lbCuotaInscripcion;
	}

	public JTextField getTxCuotaInscripcion() {
		if (txCuotaInscripcion == null) {
			txCuotaInscripcion = new JTextField();
			txCuotaInscripcion.setEditable(false);
			txCuotaInscripcion.setBounds(165, 373, 148, 19);
			txCuotaInscripcion.setBackground(Color.WHITE);
			txCuotaInscripcion.setColumns(10);
		}
		return txCuotaInscripcion;
	}

	private JPanel getPnRegistrarPago() {
		if (pnRegistrarPago == null) {
			pnRegistrarPago = new JPanel();
			pnRegistrarPago.setBorder(new TitledBorder(
					new EtchedBorder(EtchedBorder.LOWERED, new Color(255, 255, 255), new Color(160, 160, 160)),
					"Datos del pago:", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
			pnRegistrarPago.setBackground(new Color(255, 255, 255));
			pnRegistrarPago.setBounds(42, 518, 376, 134);
			pnRegistrarPago.setLayout(null);
			pnRegistrarPago.add(getTxCantidadPagada());
			pnRegistrarPago.add(getLbCantidadPagada());
			pnRegistrarPago.add(getLbFechaPago());
			pnRegistrarPago.add(getTxFechaPago());
		}
		return pnRegistrarPago;
	}

	public JTextField getTxCantidadPagada() {
		if (txCantidadPagada == null) {
			txCantidadPagada = new JTextField();
			txCantidadPagada.setText("");
			txCantidadPagada.setBounds(136, 33, 155, 19);
			txCantidadPagada.setColumns(10);
		}
		return txCantidadPagada;
	}

	private JLabel getLbCantidadPagada() {
		if (lbCantidadPagada == null) {
			lbCantidadPagada = new JLabel("Cantidad Pagada:");
			lbCantidadPagada.setFont(new Font("Tahoma", Font.PLAIN, 12));
			lbCantidadPagada.setBounds(23, 32, 103, 19);
		}
		return lbCantidadPagada;
	}

	private JLabel getLbFechaPago() {
		if (lbFechaPago == null) {
			lbFechaPago = new JLabel("Fecha del pago:");
			lbFechaPago.setFont(new Font("Tahoma", Font.PLAIN, 12));
			lbFechaPago.setBounds(23, 81, 103, 16);
		}
		return lbFechaPago;
	}

	public JTextField getTxFechaPago() {
		if (txFechaPago == null) {
			txFechaPago = new JTextField();
			txFechaPago.setBounds(136, 81, 155, 19);
			txFechaPago.setColumns(10);
		}
		return txFechaPago;
	}

	public JTable getTablaInscripcionesCurso() {
		if (tablaInscripcionesCurso == null) {
			tablaInscripcionesCurso = new JTable();
			tablaInscripcionesCurso.setDefaultEditor(Object.class, null);
		}
		return tablaInscripcionesCurso;
	}

	public void actualizarTablaInscripcionesCurso(DefaultTableModel modeloTablaInscripcionesCurso) {
		tablaInscripcionesCurso.setModel(modeloTablaInscripcionesCurso);
		getSpInscripcionesAlCurso().revalidate();
		getSpInscripcionesAlCurso().repaint();

	}

	private JPanel getPnTipoPago() {
		if (pnTipoPago == null) {
			pnTipoPago = new JPanel();
			pnTipoPago.setBorder(new TitledBorder(
					new EtchedBorder(EtchedBorder.LOWERED, new Color(255, 255, 255), new Color(160, 160, 160)),
					"Tipo de Transacci\u00F3n:", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
			pnTipoPago.setBackground(new Color(255, 255, 255));
			pnTipoPago.setBounds(42, 421, 271, 74);
			pnTipoPago.setLayout(null);
			pnTipoPago.add(getRbPagoIngreso());
			pnTipoPago.add(getRbPagoDevolucion());
		}
		return pnTipoPago;
	}

	public JRadioButton getRbPagoIngreso() {
		if (rbPagoIngreso == null) {
			rbPagoIngreso = new JRadioButton("Ingreso");
			rbPagoIngreso.setFont(new Font("Tahoma", Font.PLAIN, 12));
			rbPagoIngreso.setSelected(true);
			rbPagoIngreso.setBackground(new Color(255, 255, 255));
			rbPagoIngreso.setBounds(41, 30, 74, 21);
		}
		return rbPagoIngreso;
	}

	private JRadioButton getRbPagoDevolucion() {
		if (rbPagoDevolucion == null) {
			rbPagoDevolucion = new JRadioButton("Devolución");
			rbPagoDevolucion.setFont(new Font("Tahoma", Font.PLAIN, 12));
			rbPagoDevolucion.setBackground(new Color(255, 255, 255));
			rbPagoDevolucion.setBounds(162, 30, 103, 21);
		}
		return rbPagoDevolucion;
	}

	private JScrollPane getSpCursos() {
		if (spCursos == null) {
			spCursos = new JScrollPane();
			spCursos.setBackground(new Color(255, 255, 255));
			spCursos.setViewportView(getTablaCursos());
			spCursos.getViewport().setBackground(Color.WHITE);
		}
		return spCursos;
	}

	public JTable getTablaCursos() {
		if (tablaCursos == null) {
			tablaCursos = new JTable();
			tablaCursos.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			tablaCursos.setDefaultEditor(Object.class, null);
		}
		return tablaCursos;
	}

	public void actualizarTablaCursos(DefaultTableModel modeloTablaCursos) {
		tablaCursos.setModel(modeloTablaCursos);
		getSpCursos().revalidate();
		getSpCursos().repaint();

	}

	private JPanel getPnPagosIinscripcion() {
		if (pnPagosIinscripcion == null) {
			pnPagosIinscripcion = new JPanel();
			pnPagosIinscripcion.setBackground(new Color(255, 255, 255));
			pnPagosIinscripcion
					.setBorder(new TitledBorder(null, "Movimientos de dinero de la inscripci\u00F3n seleccionada:",
							TitledBorder.LEADING, TitledBorder.TOP, null, null));
			pnPagosIinscripcion.setBounds(833, 376, 614, 185);

			pnPagosIinscripcion.setLayout(new GridLayout(1, 0, 0, 0));
			pnPagosIinscripcion.add(getSpPagosInscripcion());
		}
		return pnPagosIinscripcion;
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
		}
		return tablaPagosInscripcion;
	}

	public void actualizarTablaPagosInscripcion(DefaultTableModel modeloTablaPagosInscripcion) {
		tablaPagosInscripcion.setModel(modeloTablaPagosInscripcion);
		getSpPagosInscripcion().revalidate();
		getSpPagosInscripcion().repaint();

	}

	public int mostrarEleccionRegistro(String datosPago, String resumenAcciones) {
		return JOptionPane.showConfirmDialog(this,
				datosPago + resumenAcciones + "¿Está seguro de registrar el pago con estas condiciones?",
				"Registrar pago: Confirmación de Registro de Pago", JOptionPane.YES_NO_OPTION,
				JOptionPane.INFORMATION_MESSAGE);
	}

	public void mostrarMensajePagoRegistrado() {
		JOptionPane.showMessageDialog(this, "El pago ha sido registrado correctamente",
				"Registrar pago: Operación Exitosa", JOptionPane.INFORMATION_MESSAGE);
	}
}
