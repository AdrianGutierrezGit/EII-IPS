package giis.demo.tkrun;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;

import giis.demo.tkrun.Controllers.BalanceController;
import giis.demo.tkrun.Controllers.CancelacionCursoController;
import giis.demo.tkrun.Controllers.CancelacionInscripcionController;
import giis.demo.tkrun.Controllers.CerrarCursosController;
import giis.demo.tkrun.Controllers.EstadoActividadController;
import giis.demo.tkrun.Controllers.InscripcionAlumnoEnCursoController;
import giis.demo.tkrun.Controllers.PlanificacionCursoController;
import giis.demo.tkrun.Controllers.RegistrarPagosController;
import giis.demo.tkrun.Controllers.RegistrarPagosEfectivoController;
import giis.demo.tkrun.Controllers.RegistroPagoProfesorController;
import giis.demo.tkrun.Controllers.RegistroProfesorController;
import giis.demo.tkrun.Controllers.RetrasarCursoController;
import giis.demo.tkrun.DTO.AlumnoDTO;
import giis.demo.tkrun.DTO.PersonaDTO;
import giis.demo.util.ApplicationException;
import giis.demo.util.Util;

public class AplicacionController {
	private AplicacionModel model;
	private AplicacionView view;

	List<JButton> botonesSecretaria = new ArrayList<JButton>();
	List<JButton> botonesProfesional = new ArrayList<JButton>();
	List<JButton> botonesResponsableFormacion = new ArrayList<JButton>();

	public AplicacionController(AplicacionModel m, AplicacionView v) {
		this.model = m;
		this.view = v;
		// El combo de alumnos ya no existe en la vista actual
		// view.cargarAlumnos(model.getAlumnos());

		// no hay inicializacion especifica del modelo, solo de la vista
		this.initView();
	}

	/**
	 * Inicializacion del controlador: anyade los manejadores de eventos a los
	 * objetos del UI. Cada manejador de eventos se instancia de la misma forma,
	 * para que invoque un metodo privado de este controlador, encerrado en un
	 * manejador de excepciones generico para mostrar ventanas emergentes cuando
	 * ocurra algun problema o excepcion controlada.
	 */
	public void initController() {

		listasBotones();
		view.getBtnInicio().addActionListener(e -> inicioAplicacion());
		view.getComboPersona().addActionListener(e -> logicaBotones());
		view.getACtualizarFecha().addActionListener(e -> {
			model.actualizarEstadosCursos(view.getFechaHoy());
			model.actualizaInscripcionesCursos(view.getFechaHoy());
			cargarComboBox();
		});
	}

	private void listasBotones() {

		botonesSecretaria.add(view.getBtCerrarCursos());
		botonesSecretaria.add(view.getBtRegistrarPagoProfesor());
		botonesSecretaria.add(view.getBtRegistrarPago());
		botonesSecretaria.add(view.getBtEstadoCurso());
		botonesSecretaria.add(view.getBtRegistrarPagosEfectivo());

		botonesProfesional.add(view.getBtInscripcionEnCurso());
		botonesProfesional.add(view.getBtCancelarInsc());

		botonesResponsableFormacion.add(view.getBtCreacionCurso());
		botonesResponsableFormacion.add(view.getBtBalance());
		botonesResponsableFormacion.add(view.getBtRegistrarNuevoProfesor());
		botonesResponsableFormacion.add(view.getBtRetrasarCurso());
		botonesResponsableFormacion.add(view.getBtCancelarCurso());
	}

	private void setBotonesNotVisible() {
		for (JButton boton : botonesProfesional) {
			boton.setVisible(false);
		}
		for (JButton boton : botonesResponsableFormacion) {
			boton.setVisible(false);
		}
		for (JButton boton : botonesSecretaria) {
			boton.setVisible(false);
		}

	}

	private void setVisibleLista(List<JButton> botonesResponsableFormacion2) {
		for (JButton boton : botonesResponsableFormacion2) {
			boton.setVisible(true);
		}
	}

	private void logicaBotones() {
		PersonaDTO persona = null;
		try {
			persona = (PersonaDTO) view.getComboPersona().getSelectedItem();
		} catch (Exception ignore) {
		}
		setBotonesNotVisible();
		if (persona == null) {
			return;
		}
		String rol = persona.getRol();

		if ("responsable de formacion".equalsIgnoreCase(rol)) {
			setVisibleLista(botonesResponsableFormacion);
		} else if ("alumno".equalsIgnoreCase(rol)) {
			setVisibleLista(botonesProfesional);
		} else if ("secretario administrativo".equalsIgnoreCase(rol)) {
			setVisibleLista(botonesSecretaria);
		} else if ("profesor".equalsIgnoreCase(rol)) {
			setBotonesNotVisible();
		} else {
			// Caso "Solo alumno" u otros roles desconocidos: solo mostrar
			// inscripción
			view.getBtInscripcionEnCurso().setVisible(true);
		}
	}

	private void inicioAplicacion() {
		String texto = view.getFechaHoy();
		try {
			Util.isIsoDateValid(texto);
		} catch (ApplicationException e) {
			view.mostrarError(e.getMessage(), "Error de fecha");
		}
	}

	private void cargarComboBox() {
		List<PersonaDTO> personas = model.getPersonasConRol();
		view.getComboPersona().removeAllItems();
		// Opción especial para registrar nuevo alumno (formulario en blanco)
		PersonaDTO opcionNuevo = new PersonaDTO();
		opcionNuevo.setId(-1);
		opcionNuevo.setNombre("Solo alumno");
		opcionNuevo.setApellidos("");
		opcionNuevo.setRol(null); // para que toString muestre solo el texto
		view.getComboPersona().addItem(opcionNuevo);
		view.getBtInscripcionEnCurso().setVisible(true);
		// Resto de personas con rol
		for (PersonaDTO p : personas) {
			view.getComboPersona().addItem(p);
		}
		view.getComboPersona().setSelectedIndex(-1);
	}

	public void initView() {

		view.setFechaHoy(LocalDate.now().toString());

		view.getBtCreacionCurso().addActionListener(e -> crea());
		view.getBtCerrarCursos().addActionListener(e -> cerrarCursos());
		view.getBtEstadoCurso().addActionListener(e -> estadoCursos());
		view.getBtRegistrarPago().addActionListener(E -> registrarPago());
		view.getBtRegistrarPagoProfesor().addActionListener(e -> registrarPagoProfesor());
		view.getBtRegistrarNuevoProfesor().addActionListener(e -> registrarNuevoProfesor());
		view.getBtRetrasarCurso().addActionListener(e -> retrasarCurso());
		view.getBtCancelarCurso().addActionListener(e -> cancelarCurso());
		view.getBtRegistrarPagosEfectivo().addActionListener(e -> registrarPagoEfectivo());

		// Poblar personas en el combo
		try {
			cargarComboBox();
			// Evitar selección por defecto

		} catch (Exception ex) {
			view.mostrarError("Error cargando personas: " + ex.getMessage(), "Error");
		}

		// Abre la ventana (sustituye al main generado por WindowBuilder)
		view.getBtBalance().addActionListener(e -> balanceCursos());
		view.getFrame().setVisible(true);

		view.getBtInscripcionEnCurso().addActionListener(e -> incribirse());

		// El combo de alumnos ya no existe en la vista actual
		// view.getCbAlumnos().addActionListener(e ->
		// view.getBtCancelarInsc().setEnabled(true));

		view.getBtCancelarInsc().addActionListener(e -> cancelarInscripcion());
	}

	private void cancelarInscripcion() {
		// Usar la persona seleccionada si es alumno
		PersonaDTO personaSel = null;
		try {
			personaSel = (PersonaDTO) view.getComboPersona().getSelectedItem();
		} catch (Exception ignore) {
		}
		if (personaSel == null || !"alumno".equalsIgnoreCase(personaSel.getRol())) {
			view.mostrarError("Seleccione un alumno en el combo Persona para cancelar.", "Cancelación");
			return;
		}
		AlumnoDTO alumno = new AlumnoDTO();
		alumno.setId_alumno(personaSel.getId());
		alumno.setNombre(personaSel.getNombre());
		alumno.setApellidos(personaSel.getApellidos());
		alumno.setEmail(personaSel.getEmail());
		alumno.setTelefono(personaSel.getTelefono());

		CancelacionInscripcionController cic = new CancelacionInscripcionController(alumno, view.getFechaHoy());
		cic.iniciarVista();
	}

	private void registrarPago() {
		RegistrarPagosController controlador = new RegistrarPagosController(view.getFechaHoy());
		controlador.iniciarVista();
	}

	private void registrarPagoEfectivo() {
		RegistrarPagosEfectivoController controlador = new RegistrarPagosEfectivoController(view.getFechaHoy());
		controlador.iniciarVista();
	}

	private void registrarPagoProfesor() {
		RegistroPagoProfesorController controlador = new RegistroPagoProfesorController(view.getFechaHoy());
		controlador.iniciarVista();
	}

	private void registrarNuevoProfesor() {
		RegistroProfesorController controlador = new RegistroProfesorController();
		controlador.inicializarVista();
	}

	private void cerrarCursos() {
		CerrarCursosController controlador = new CerrarCursosController(view.getFechaHoy());
		controlador.iniciarVista();
	}

	private void retrasarCurso() {
		RetrasarCursoController controlador = new RetrasarCursoController(view.getFechaHoy());
		controlador.iniciarVista();
	}

	private void cancelarCurso() {
		CancelacionCursoController controlador = new CancelacionCursoController(view.getFechaHoy());
		controlador.iniciarVista();
	}

	private void incribirse() {
		PersonaDTO personaSel = null;
		try {
			personaSel = (PersonaDTO) view.getComboPersona().getSelectedItem();
		} catch (Exception ignore) {
		}
		AlumnoDTO preAlumno = null;
		if (personaSel != null && "alumno".equalsIgnoreCase(personaSel.getRol())) {
			preAlumno = new AlumnoDTO();
			preAlumno.setId_alumno(personaSel.getId());
			preAlumno.setNombre(personaSel.getNombre());
			preAlumno.setApellidos(personaSel.getApellidos());
			preAlumno.setEmail(personaSel.getEmail());
			preAlumno.setTelefono(personaSel.getTelefono());
		}
		// Sin fallback a cbAlumnos: si la selección es "Solo alumno" o no hay
		// selección, el formulario se abrirá vacío y editable.
		InscripcionAlumnoEnCursoController rA = new InscripcionAlumnoEnCursoController(view.getFechaHoy(), preAlumno);
		rA.inicializarVista();
	}

	private void estadoCursos() {
		EstadoActividadController eac = new EstadoActividadController(view.getFechaHoy());
		eac.iniciarVista();
	}

	private void crea() {
		PlanificacionCursoController controlador = new PlanificacionCursoController(view.getFechaHoy());
		controlador.iniciarVista();
	}

	private void balanceCursos() {
		BalanceController controlador = new BalanceController(view.getFechaHoy());
		controlador.iniciarVista();
	}

}