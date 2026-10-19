package giis.demo.tkrun.Views;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.HashMap;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

import giis.demo.tkrun.DTO.ProfesorDTO;

public class RegistroPagoProfesorView extends JFrame {

	private static final long serialVersionUID = 1L;

	private JPanel panelProfesorDatos;

	private JTextField txtFechaEmision, txtNumeroFactura, txtCantidadFactura, txtDireccionEmisor;

	private JTextField txtFechaTransferencia, txtCantidadTransferida;
	private JLabel lblEstadoPago;

	private JComboBox<Object> comboCursos;
	private JComboBox<Object> comboProfesores;

	private JButton btnGuardar;
	private JButton btnVolver;

	private JRadioButton rbPagarProfesor;
	private JRadioButton rbProfesorPaga;

	public RegistroPagoProfesorView() {
		initialize();
	}

	private void initialize() {
		this.setTitle("COIIPA: Registro de Pago a Profesor");
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		this.getContentPane().setBackground(new Color(245, 245, 245));
		this.getContentPane().setLayout(new BorderLayout(10, 10));

		JPanel root = new JPanel(new GridBagLayout());
		root.setBackground(new Color(245, 245, 245));
		root.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.weightx = 1.0;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.insets = new Insets(5, 5, 5, 5);

		JPanel panelCurso = new JPanel(new GridBagLayout());
		panelCurso.setBackground(new Color(245, 245, 245));

		GridBagConstraints gbcCombo = new GridBagConstraints();
		gbcCombo.insets = new Insets(5, 5, 5, 5);
		gbcCombo.gridy = 0;
		gbcCombo.anchor = GridBagConstraints.WEST;

		gbcCombo.gridx = 0;
		panelCurso.add(new JLabel("Curso:"), gbcCombo);
		gbcCombo.gridx = 1;
		gbcCombo.weightx = 1.0;
		gbcCombo.fill = GridBagConstraints.HORIZONTAL;
		comboCursos = new JComboBox<>();
		panelCurso.add(comboCursos, gbcCombo);

		gbcCombo.gridx = 2;
		gbcCombo.weightx = 0;
		gbcCombo.fill = GridBagConstraints.NONE;
		panelCurso.add(new JLabel("Profesor:"), gbcCombo);
		gbcCombo.gridx = 3;
		gbcCombo.weightx = 1.0;
		gbcCombo.fill = GridBagConstraints.HORIZONTAL;
		comboProfesores = new JComboBox<>();
		panelCurso.add(comboProfesores, gbcCombo);

		root.add(panelCurso, gbc);
		gbc.gridy++;

		JPanel panelFactura = crearPanelDatosFacturaEditable();
		root.add(panelFactura, gbc);
		gbc.gridy++;

		JPanel panelPago = crearPanelDatosPago();
		root.add(panelPago, gbc);
		gbc.gridy++;

		JPanel panelBotones = new JPanel();
		btnGuardar = new JButton("Guardar");
		btnVolver = new JButton("Volver");
		panelBotones.add(btnGuardar);
		panelBotones.add(btnVolver);

		root.add(panelBotones, gbc);

		this.getContentPane().add(root, BorderLayout.CENTER);
		this.setMinimumSize(new Dimension(800, 600));
		this.pack();
		this.setLocationRelativeTo(null);
	}

	private JPanel crearPanelDatosFacturaEditable() {
		JPanel panel = new JPanel(new GridBagLayout());
		panel.setBorder(BorderFactory.createTitledBorder("Datos de la factura"));
		panel.setBackground(Color.WHITE);

		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(5, 5, 5, 5);
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.weightx = 1.0;

		int row = 0;

		panelProfesorDatos = new JPanel(new GridBagLayout());
		panelProfesorDatos.setBackground(Color.LIGHT_GRAY);
		panelProfesorDatos.setBorder(BorderFactory.createTitledBorder("Profesor seleccionado"));
		gbc.gridy = row++;
		gbc.gridx = 0;
		panel.add(panelProfesorDatos, gbc);

		txtFechaEmision = new JTextField();
		addLabelField(panel, "Fecha de emisión (YYYY-MM-DD):", txtFechaEmision, row++);

		txtNumeroFactura = new JTextField();
		addLabelField(panel, "Número de factura:", txtNumeroFactura, row++);

		txtCantidadFactura = new JTextField();
		addLabelField(panel, "Cantidad (€):", txtCantidadFactura, row++);

		txtDireccionEmisor = new JTextField();
		addLabelField(panel, "Dirección del emisor:", txtDireccionEmisor, row++);

		return panel;
	}

	private JPanel crearPanelDatosPago() {
		JPanel panel = new JPanel(new GridBagLayout());
		panel.setBorder(BorderFactory.createTitledBorder("Datos del pago"));
		panel.setBackground(Color.WHITE);

		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(5, 5, 5, 5);
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.weightx = 1.0;
		int row = 0;

		// NUEVO → Añadimos selección del tipo de operación
		rbPagarProfesor = new JRadioButton("Pago", true);
		rbProfesorPaga = new JRadioButton("Cobro");
		rbPagarProfesor.setBackground(Color.WHITE);
		rbProfesorPaga.setBackground(Color.WHITE);

		ButtonGroup grupo = new ButtonGroup();
		grupo.add(rbPagarProfesor);
		grupo.add(rbProfesorPaga);

		gbc.gridx = 0;
		gbc.gridy = row++;
		gbc.gridwidth = 2;
		panel.add(rbPagarProfesor, gbc);

		gbc.gridy = row++;
		panel.add(rbProfesorPaga, gbc);

		gbc.gridwidth = 1;

		txtFechaTransferencia = new JTextField();
		addLabelField(panel, "Fecha de transferencia (YYYY-MM-DD):", txtFechaTransferencia, row++);

		txtCantidadTransferida = new JTextField();
		addLabelField(panel, "Cantidad transferida (€):", txtCantidadTransferida, row++);

		lblEstadoPago = new JLabel("Pendiente");
		lblEstadoPago.setFont(new Font("Segoe UI", Font.BOLD, 13));
		addLabelField(panel, "Estado del pago:", lblEstadoPago, row++);

		return panel;
	}

	private void addLabelField(JPanel panel, String label, Component field, int row) {
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(3, 3, 3, 3);
		gbc.gridx = 0;
		gbc.gridy = row;
		gbc.anchor = GridBagConstraints.EAST;
		panel.add(new JLabel(label), gbc);

		gbc.gridx = 1;
		gbc.weightx = 1.0;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		panel.add(field, gbc);
	}

	public void mostrarDatosProfesor(ProfesorDTO profesor) {
		panelProfesorDatos.removeAll();

		if (profesor != null) {
			GridBagConstraints gbc = new GridBagConstraints();
			gbc.gridx = 0;
			gbc.gridy = 0;
			gbc.anchor = GridBagConstraints.WEST;
			gbc.insets = new Insets(2, 5, 2, 5);

			panelProfesorDatos.add(new JLabel("Nombre: " + profesor.getNombre()), gbc);
			gbc.gridy++;
			panelProfesorDatos.add(new JLabel("Apellidos: " + profesor.getApellidos()), gbc);
		}

		panelProfesorDatos.revalidate();
		panelProfesorDatos.repaint();
	}

	public JButton getBtnGuardar() {
		return btnGuardar;
	}

	public JButton getBtnVolver() {
		return btnVolver;
	}

	public JRadioButton getBtnPaga() {
		return rbPagarProfesor;
	}

	public JRadioButton getBtnCobra() {
		return rbProfesorPaga;
	}

	public JComboBox<Object> getComboCursos() {
		return comboCursos;
	}

	public JComboBox<Object> getComboProfesores() {
		return comboProfesores;
	}

	public JTextField getTxtFechaTransferencia() {
		return txtFechaTransferencia;
	}

	public JLabel getLblEstadoPago() {
		return lblEstadoPago;
	}

	public Map<String, String> getDatosIntroducidos() {
		Map<String, String> datos = new HashMap<>();
		datos.put("fechaEmision", txtFechaEmision.getText().trim());
		datos.put("numeroFactura", txtNumeroFactura.getText().trim());
		datos.put("cantidadFactura", txtCantidadFactura.getText().trim());
		datos.put("direccionEmisor", txtDireccionEmisor.getText().trim());
		datos.put("fechaTransferencia", txtFechaTransferencia.getText().trim());
		datos.put("cantidadTransferida", txtCantidadTransferida.getText().trim());
		return datos;
	}

	public void mostrarError(String msg) {
		JOptionPane.showMessageDialog(this, msg, "Error", JOptionPane.ERROR_MESSAGE);
	}

	public void mostrarPagoExitosoYCerrar() {
		JOptionPane.showMessageDialog(this, "Pago registrado correctamente", "Éxito", JOptionPane.INFORMATION_MESSAGE);
	}
}
