package giis.demo.tkrun;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Toolkit;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

import giis.demo.tkrun.DTO.PersonaDTO;
import net.miginfocom.swing.MigLayout;

/**
 * 
 */
public class AplicacionView {

	private JFrame frame;
	private JTextField txtFechaHoy;
	private JButton btIniciarAplicacion;
	private JButton btRegistrarCursos;
	private JButton btRegistrarPagos;
	private JButton btCerrarCursos;
	private JButton btEstadoCurso;
	private JButton btRegistrarPagosEfectivo;

	private JButton btBalance;

	private JButton btIncripcionEnCurso;
	private JButton btCancelarInscripcion;
	private JButton btRegistrarPagoProfesor;
	private JButton btRegistrarNuevoProfesor;
	private JButton btRetrasarCurso;

	// Nuevo selector de persona
	private JLabel lblPersona;
	private JComboBox<PersonaDTO> cbPersona;
	private JButton btCancelarCurso;
	private JButton actualizarFecha;

	/**
	 * Create the application.
	 */
	public AplicacionView() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setIconImage(
				Toolkit.getDefaultToolkit().getImage(AplicacionView.class.getResource("/imagenes/logo_COIIPA.png")));
		frame.setTitle("COIIPA");
		frame.setName("Aplicacion");

		frame.setMinimumSize(new Dimension(450, 450));
		frame.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

		frame.getContentPane().setBackground(new Color(245, 245, 245));

		frame.getContentPane().setLayout(new MigLayout("insets 16", "[grow,fill]",
				"[]10[]10[]10[grow]10[]10[]10[]10[]10[]10[]10[]10[]10[]10[][][][][][]"));

		final JLabel lblSimulacion;
		final JLabel lblFechaHoy;

		lblFechaHoy = new JLabel("Fecha de hoy (formato ISO):");
		frame.getContentPane().add(lblFechaHoy, "flowx,cell 0 1");

		txtFechaHoy = new JTextField();
		txtFechaHoy.setName("txtFechaHoy");
		txtFechaHoy.setToolTipText("Formato ISO: yyyy-MM-dd");
		frame.getContentPane().add(txtFechaHoy, "cell 0 1,growx");
		txtFechaHoy.setColumns(10);

		// Persona selector between date and buttons
		lblPersona = new JLabel("Persona:");
		frame.getContentPane().add(lblPersona, "flowx,cell 0 3");

		cbPersona = new JComboBox<>();
		cbPersona.setName("cbPersona");
		frame.getContentPane().add(cbPersona, "cell 0 3,growx");

		lblSimulacion = new JLabel("Inicio de la aplicación de gestión de cursos informáticos");
		frame.getContentPane().add(lblSimulacion, "flowx,cell 0 6,alignx center");

		btIniciarAplicacion = new JButton("Iniciar aplicación");
		btIniciarAplicacion.setVisible(false);
		frame.getContentPane().add(btIniciarAplicacion, "cell 0 7,growx,sizegroupx btn,sizegroupy btn");

		btRegistrarCursos = new JButton("Planificar cursos");
		btRegistrarCursos.setVisible(false);
		frame.getContentPane().add(btRegistrarCursos, "cell 0 8,growx,sizegroupx btn,sizegroupy btn");

		btRegistrarPagos = new JButton("Registrar pago");
		btRegistrarPagos.setVisible(false);
		frame.getContentPane().add(btRegistrarPagos, "cell 0 9,growx,sizegroupx btn,sizegroupy btn");

		btEstadoCurso = new JButton("Estado de cursos");
		btEstadoCurso.setVisible(false);
		frame.getContentPane().add(btEstadoCurso, "cell 0 10,growx,sizegroupx btn,sizegroupy btn");

		btIncripcionEnCurso = new JButton("Inscripción en un curso");
		frame.getContentPane().add(btIncripcionEnCurso, "cell 0 11,growx,sizegroupx btn,sizegroupy btn");

		btBalance = new JButton("Balance de cursos");
		btBalance.setVisible(false);
		frame.getContentPane().add(btBalance, "cell 0 12,growx,sizegroupx btn,sizegroupy btn");

		btCancelarInscripcion = new JButton("Cancelación de inscripción");
		btCancelarInscripcion.setVisible(false);
		frame.getContentPane().add(btCancelarInscripcion, "flowx,cell 0 13,alignx center");

		btCerrarCursos = new JButton("Cerrar cursos");
		btCerrarCursos.setVisible(false);
		frame.getContentPane().add(btCerrarCursos, "cell 0 14,alignx center");

		btRegistrarPagoProfesor = new JButton("Registrar pago a profesor");
		btRegistrarPagoProfesor.setVisible(false);
		frame.getContentPane().add(btRegistrarPagoProfesor, "cell 0 15,growx,sizegroupx btn,sizegroupy btn");

		btRegistrarNuevoProfesor = new JButton("Registrar nuevo profesor");
		btRegistrarNuevoProfesor.setVisible(false);
		frame.getContentPane().add(btRegistrarNuevoProfesor, "cell 0 16,growx,sizegroupx btn,sizegroupy btn");

		btRetrasarCurso = new JButton("Retrasar curso");
		btRetrasarCurso.setVisible(false);
		frame.getContentPane().add(btRetrasarCurso, "cell 0 17,growx,sizegroupx btn,sizegroupy btn");

		btCancelarCurso = new JButton("Cancelar curso");
		btCancelarCurso.setVisible(false);
		frame.getContentPane().add(btCancelarCurso, "cell 0 18");

		btRegistrarPagosEfectivo = new JButton("Registrar pago en efectivo");
		btRegistrarPagosEfectivo.setVisible(false);
		frame.getContentPane().add(btRegistrarPagosEfectivo, "cell 0 19");

		actualizarFecha = new JButton("Aplicar");

		frame.getContentPane().add(actualizarFecha, "cell 0 1,alignx left");

		// Ajustes finales de ventana
		frame.pack();
		frame.setLocationRelativeTo(null);

	}

	// Getters y Setters anyadidos para acceso desde el controlador
	// (repersentacion compacta)
	public void mostrarError(String mensaje, String tipoError) {
		JOptionPane.showMessageDialog(null, mensaje, tipoError, JOptionPane.ERROR_MESSAGE);
	}

	public JButton getACtualizarFecha() {
		return actualizarFecha;
	}

	public JFrame getFrame() {
		return this.frame;
	}

	public String getFechaHoy() {
		return this.txtFechaHoy.getText();
	}

	public JButton getBtnInicio() {
		return this.btIniciarAplicacion;
	}

	public JButton getBtCerrarCursos() {
		return this.btCerrarCursos;
	}

	public JButton getBtCreacionCurso() {
		return this.btRegistrarCursos;
	}

	public JButton getBtRegistrarPago() {
		return this.btRegistrarPagos;
	}

	public JButton getBtRegistrarPagosEfectivo() {
		return this.btRegistrarPagosEfectivo;
	}

	public JButton getBtEstadoCurso() {
		return this.btEstadoCurso;
	}

	public JButton getBtBalance() {
		return btBalance;
	}

	public void setFechaHoy(String fechaIso) {
		this.txtFechaHoy.setText(fechaIso);
	}

	public JButton getBtInscripcionEnCurso() {
		return this.btIncripcionEnCurso;
	}

	public JButton getBtCancelarInsc() {
		return this.btCancelarInscripcion;
	}

	public JButton getBtRegistrarPagoProfesor() {
		return this.btRegistrarPagoProfesor;
	}

	public JButton getBtRegistrarNuevoProfesor() {
		return this.btRegistrarNuevoProfesor;
	}

	public JButton getBtRetrasarCurso() {
		return this.btRetrasarCurso;
	}

	public JButton getBtCancelarCurso() {
		return this.btCancelarCurso;
	}

	public JComboBox<PersonaDTO> getComboPersona() {
		return this.cbPersona;
	}

}