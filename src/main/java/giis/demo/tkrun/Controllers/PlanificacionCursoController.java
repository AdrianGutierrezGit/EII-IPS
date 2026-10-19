package giis.demo.tkrun.Controllers;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import javax.swing.DefaultComboBoxModel;

import giis.demo.tkrun.DTO.CuotaDTO;
import giis.demo.tkrun.DTO.CursoDTO;
import giis.demo.tkrun.DTO.ProfesorDTO;
import giis.demo.tkrun.Models.PlanificacionCursoModel;
import giis.demo.tkrun.Views.PlanificacionCursoView;
import giis.demo.util.ApplicationException;
import giis.demo.util.SwingUtil;
import giis.demo.util.Util;
import giis.demo.util.ValidadorDatos;

public class PlanificacionCursoController {
	private PlanificacionCursoModel modelo;
	private PlanificacionCursoView vista;
	private Date fechaActual;
	private List<ProfesorDTO> profesores;
	private List<Integer> remuneraciones;

	public PlanificacionCursoController(String diaActual) {
		modelo = new PlanificacionCursoModel();
		vista = new PlanificacionCursoView();
		fechaActual = Util.isoStringToDate(diaActual);
		profesores = new ArrayList<ProfesorDTO>();
		remuneraciones = new ArrayList<Integer>();

		cargarProfesores();

	}

	public void iniciarVista() {
		vista.setVisible(true);
		vista.getBtGuardar().addActionListener(e -> guardarCurso());
		vista.getChUnDia().addActionListener(e -> fechaCelebracion());
		// Añadir un profesor
		vista.getBtAddProf().addActionListener(e -> addProfesor());
		vista.getChbEmpresa().addActionListener(e -> alCambiarModoEmpresa());
		cargarCuotas();
	}

	private void cargarCuotas() {
		List<CuotaDTO> cuotas = modelo.cargarCuotas();
		vista.setCuotas(cuotas);
	}

	private void addProfesor() {
		if (SwingUtil.exceptionWrapper(() -> {
			ValidadorDatos.isTrue(vista.getCbProfesores().getSelectedItem() != null, "Debe seleccionar un profesor");
		})) {
			return;
		}

		ProfesorDTO profesorSeleccionado = (ProfesorDTO) vista.getCbProfesores().getSelectedItem();
		int remuneracionNueva = (int) vista.getSpRemuneracion().getValue();

		// Buscamos si el profesor ya está en la lista
		int indice = profesores.indexOf(profesorSeleccionado);

		if (indice != -1) {
			// CASO 1: El profesor YA ESTÁ en la lista -> ACTUALIZAMOS su precio
			remuneraciones.set(indice, remuneracionNueva);
		} else {
			// CASO 2: El profesor NO ESTÁ -> LO AÑADIMOS nuevo
			profesores.add(profesorSeleccionado);
			remuneraciones.add(remuneracionNueva);
		}

		actualizarProfesoresAsignados();
	}

	private void actualizarProfesoresAsignados() {
		vista.getModeloTablaProfesores().setRowCount(0);
		for (int i = 0; i < profesores.size(); i++) {
			vista.getModeloTablaProfesores()
					.addRow(new Object[] { profesores.get(i).getNombre(), remuneraciones.get(i) });
		}

	}

	private void fechaCelebracion() {
		boolean variosDias = vista.getChUnDia().isSelected();
		vista.getLbFecha().setText(variosDias ? "Fecha inicio curso:" : "Fecha curso:" + String.format("%12s", ""));
		vista.getTfFechaFin().setEnabled(variosDias);
		vista.getLbFechaFin().setForeground(variosDias ? Color.DARK_GRAY : Color.GRAY);
	}

	// Requiere: vista, fechaActual ya inicializados
	// Devuelve true si todo es válido. Si hay error, muestra mensaje y devuelve
	// false.
	// Modificamos la firma para aceptar la lista de errores
	private void validarFechasInscripcion(List<String> errores) {
		// 1. Obtener los textos
		String sFechaCurso = vista.getTfFecha().getText().trim();
		String sApertura = vista.getTfAperturaInscripcion().getText().trim();
		String sCierre = vista.getTfCierreInscripcion().getText().trim();
		String sFechaFin = vista.getTfFechaFin().getText().trim();

		// 2. Convertir de forma SEGURA (sin excepciones que paren el programa)
		Date fechaCurso = intentarParsearFecha(sFechaCurso);
		Date apertura = intentarParsearFecha(sApertura);
		Date cierre = intentarParsearFecha(sCierre);

		// Convertimos fecha fin solo si es necesario
		Date fechaFin = null;
		if (vista.getChUnDia().isSelected()) {
			fechaFin = intentarParsearFecha(sFechaFin);
		}

		// 3. Validar Formatos (Si son null, es que el formato estaba mal)
		if (fechaCurso == null)
			errores.add("La fecha del curso es incorrecta (Use formato AAAA-MM-DD).");
		if (apertura == null)
			errores.add("La fecha de apertura es incorrecta (Use formato AAAA-MM-DD).");
		if (cierre == null)
			errores.add("La fecha de cierre es incorrecta (Use formato AAAA-MM-DD).");
		if (vista.getChUnDia().isSelected() && fechaFin == null) {
			errores.add("La fecha de fin de curso es incorrecta (Use formato AAAA-MM-DD).");
		}

		// 4. Validar Lógica de Negocio (Solo si las fechas se pudieron leer
		// correctamente)

		// Comparación Apertura vs Cierre
		if (apertura != null && cierre != null) {
			if (!apertura.before(cierre)) {
				errores.add("La fecha de apertura debe ser anterior al cierre de inscripción.");
			}
		}

		// Comparación Cierre vs Curso
		if (cierre != null && fechaCurso != null) {
			if (!cierre.before(fechaCurso)) {
				errores.add("El cierre de inscripción debe ser anterior al inicio del curso.");
			}
		}

		// Comparación Apertura vs Curso
		if (apertura != null && fechaCurso != null) {
			if (!apertura.before(fechaCurso)) {
				errores.add("La apertura de inscripción debe ser anterior al inicio del curso.");
			}
		}

		// Comparación Curso vs Hoy
		if (fechaCurso != null) {
			if (!fechaActual.before(fechaCurso)) {
				errores.add("La fecha del curso debe ser posterior al día de hoy.");
			}
		}

		// Comparación Curso Inicio vs Fin (Si es multidia)
		if (vista.getChUnDia().isSelected() && fechaCurso != null && fechaFin != null) {
			if (!fechaCurso.before(fechaFin)) {
				errores.add("La fecha de inicio debe ser anterior a la fecha de fin.");
			}
		}
	}

	private void guardarCurso() {

		if (SwingUtil.exceptionWrapper(() -> {
			comprobacionDeFormulario();
		})) {
			return;
		}
		CursoDTO curso = new CursoDTO();
		if (SwingUtil.exceptionWrapper(() -> {
			curso.setEstado_curso(asignarEstado());
		})) {
			return;
		}

		curso.setNombre(vista.getName().toString());

		curso.setId_curso(modelo.maxId() + 1);

		curso.setNombre(vista.getTfNombre().getText());
		curso.setFecha_inicio(vista.getTfFecha().getText().trim());
		if (vista.getChUnDia().isSelected()) {
			curso.setFecha_fin(vista.getTfFechaFin().getText().trim());
		} else {
			// Misma fecha
			curso.setFecha_fin(vista.getTfFecha().getText().trim());
		}
		curso.setFin_plazo_inscripcion(vista.getTfCierreInscripcion().getText().trim());

		curso.setInicio_plazo_inscripcion(vista.getTfAperturaInscripcion().getText().trim());
		curso.setLugar(vista.getTxEspacio().getText());

		curso.setNum_plazas((int) vista.getSPlazas().getValue());
		if (vista.getChbEmpresa().isSelected()) {
			String cif = vista.getTfCifEmpresa().getText().trim();
			String nombreEmpresa = vista.getTfNombreEmpresa().getText().trim();
			int coste = (int) vista.getSpCosteEmpresa().getValue();

			// Buscamos si ya existe para no duplicar (CRÍTICO)
			int idEmpresa = modelo.getEmpresaId(cif);

			if (idEmpresa == -1) {
				// No existe, la creamos
				idEmpresa = modelo.crearEmpresa(cif, nombreEmpresa);
			}

			curso.setId_empresa(idEmpresa);
			curso.setCoste_empresa(coste);
		} else {
			// Modo normal: sin empresa y coste 0
			curso.setId_empresa(null);
			curso.setCoste_empresa(0);
		}
		// ---------------------------------

		for (int i = 0; i < profesores.size(); i++) {
			int remuneracionReal;

			if (vista.getChbEmpresa().isSelected()) {

				remuneracionReal = 0;
			} else {
				remuneracionReal = remuneraciones.get(i);
			}

			modelo.asignarProfesor(curso, profesores.get(i), remuneracionReal);
		}

		modelo.introducirCurso(curso);

		// Guardar cuotas del curso
		// ... dentro de guardarCurso() ...

		// Guardar cuotas del curso
		List<CuotaDTO> cuotas = vista.getCuotasConfiguradas();
		boolean hayCuotasAplicadas = false;

		for (CuotaDTO q : cuotas) {
			// Solo procesamos las que tienen el check marcado (isAplicado)
			if (q.isAplicado()) {
				int idCuota = q.getId_tipo_cuota();

				// === LÓGICA NUEVA ===
				// 1. Si el ID es -1, significa que es una cuota escrita a mano
				if (idCuota == -1) {
					// La insertamos en la tabla de tipos y recuperamos el ID
					// generado
					idCuota = modelo.insertarNuevoTipoCuota(q.getNombre());
				}
				// ====================

				// 2. Insertamos la relación en cuota_curso usando el ID (viejo
				// o nuevo)
				modelo.insertarCuotaCurso(curso.getId_curso(), idCuota, q.getPrecio());
				hayCuotasAplicadas = true;
			}
		}

		// Si no se seleccionó ninguna, se asume cuota general (lógica legacy)
		if (!hayCuotasAplicadas) {
			modelo.insertarCuotaCurso(curso.getId_curso(), CuotaDTO.cuotaGeneral, 0);
		}

		// ... resto del método ...
		// Si no hay cuotas aplicadas, no insertamos nada.
		// La lógica de “curso gratuito” se gestiona en otra capa.

		vista.mostrarMensajeConfirmacion();
		vista.dispose();
		reiniciarVentana();
	}

	private String asignarEstado() {

		final String sFechaInicioCurso = vista.getTfFecha().getText().trim();
		final String sApertura = vista.getTfAperturaInscripcion().getText().trim();
		final String sCierre = vista.getTfCierreInscripcion().getText().trim();

		Date fechaInicioCurso = Util.isoStringToDate(sFechaInicioCurso);
		Date apertura = Util.isoStringToDate(sApertura);
		Date cierre = Util.isoStringToDate(sCierre);

		Date hoy = fechaActual; // ya inicializada

		if (hoy.before(apertura)) {
			return CursoDTO.ESTADO_PLANIFICADO;
		}
		if (!hoy.before(apertura) && !hoy.after(cierre)) {
			return CursoDTO.ESTADO_PERIODO_INSCRIPCION;
		}
		if (hoy.after(cierre) && hoy.before(fechaInicioCurso)) {
			return CursoDTO.ESTADO_INSCRIPCION_CERRADA;
		}
		// hoy es el día del curso o posterior
		return CursoDTO.ESTADO_CERRADO;

	}

	private void alCambiarModoEmpresa() {
		boolean esEmpresa = vista.getChbEmpresa().isSelected();

		if (esEmpresa) {
			// 1. Recorremos las remuneraciones ya guardadas y las ponemos a 0
			for (int i = 0; i < remuneraciones.size(); i++) {
				remuneraciones.set(i, 0);
			}

			// 2. Avisamos visualmente (Opcional pero recomendado)
			// vista.mostrarMensaje("Al pasar a modo empresa, las remuneraciones
			// de los
			// profesores añadidos se han establecido a 0.");
		}

		// 3. Refrescamos la tabla para que el usuario VEA que ahora son 0
		actualizarProfesoresAsignados();
	}

	private void reiniciarVentana() {
		vista.getTxEspacio().setText("");
		vista.getTfAperturaInscripcion().setText("");

		vista.getTfCierreInscripcion().setText("");
		vista.getTfFecha().setText("");
		vista.getTfNombre().setText("");

		// ... lo que ya tenías ...
		vista.getTfCifEmpresa().setText("");
		vista.getTfNombreEmpresa().setText("");
		vista.getSpCosteEmpresa().setValue(0);
		vista.getChbEmpresa().setSelected(false);
		vista.getPnEmpresa().setVisible(false);
		vista.getSpRemuneracion().setEnabled(true);

	}

	private void comprobacionDeFormulario() {
		List<String> errores = new ArrayList<>();

		// --- Validaciones Generales ---
		if (vista.getTfNombre().getText().isBlank()) {
			errores.add("El nombre del curso no puede estar vacío.");
		}

		if (vista.getTfFecha().getText().isBlank()) {
			errores.add("La fecha del curso no puede estar vacía.");
		}

		if (vista.getTfAperturaInscripcion().getText().isBlank()) {
			errores.add("La fecha de apertura de inscripción no puede estar vacía.");
		}

		if ((int) vista.getSPlazas().getValue() <= 0) {
			errores.add("El curso debe tener al menos una plaza disponible.");
		}
		
		if(profesores.isEmpty()) {
			errores.add("Debe asignar al menos un profesor al curso");
		}

		// --- Validaciones de Fechas (Delegamos pasando la lista) ---
		validarFechasInscripcion(errores);

		// --- Validaciones de Empresa (Si aplica) ---
		if (vista.getChbEmpresa().isSelected()) {
			if (vista.getTfCifEmpresa().getText().isBlank()) {
				errores.add("Si contrata una empresa, debe indicar el CIF.");
			}
			if (vista.getTfNombreEmpresa().getText().isBlank()) {
				errores.add("Si contrata una empresa, debe indicar el Nombre.");
			}
			if ((int) vista.getSpCosteEmpresa().getValue() < 0) {
				errores.add("El coste de la empresa no puede ser negativo.");
			}
		}

		// --- Validaciones de Profesores ---
		// (Opcional: Si quieres validar que haya profes, añádelo aquí)
		// if (profesores.isEmpty()) errores.add("Debe asignar al menos un
		// profesor.");

		// === PUNTO CRÍTICO: LANZAMIENTO DE ERRORES ===
		if (!errores.isEmpty()) {
			// Convertimos la lista en un solo string separado por saltos de
			// línea
			String mensajeFinal = "Por favor, corrija los siguientes errores:\n- " + String.join("\n- ", errores);

			// Lanzamos RuntimeException para que SwingUtil.exceptionWrapper lo
			// capture y
			// muestre el popup
			throw new ApplicationException(mensajeFinal);
		}
	}

	public void cargarProfesores() {
		List<ProfesorDTO> lista = modelo.getProfesores();

		ProfesorDTO[] listaProfesores = new ProfesorDTO[lista.size()];

		for (int i = 0; i < lista.size(); i++) {
			listaProfesores[i] = lista.get(i);

		}
		vista.getCbProfesores().setModel(new DefaultComboBoxModel<ProfesorDTO>(listaProfesores));

	}

	private static boolean isBusinessDay(Calendar cal) {
		int dow = cal.get(Calendar.DAY_OF_WEEK);
		return dow != Calendar.SATURDAY && dow != Calendar.SUNDAY;
	}

	public static Date previousBusinessDay(Date date) {
		Objects.requireNonNull(date, "date");
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.DAY_OF_MONTH, -1);
		while (!isBusinessDay(cal)) {
			cal.add(Calendar.DAY_OF_MONTH, -1);
		}
		return cal.getTime();
	}

	/** 'n' días laborales antes de 'date' */
	public static Date minusBusinessDays(Date date, int n) {
		Objects.requireNonNull(date, "date");
		if (n < 0)
			throw new IllegalArgumentException("n must be >= 0");
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		int remaining = n;
		while (remaining > 0) {
			cal.add(Calendar.DAY_OF_MONTH, -1);
			if (isBusinessDay(cal))
				remaining--;
		}
		return cal.getTime();
	}

	/**
	 * Intenta parsear la fecha usando la utilidad. Si falla, devuelve null en lugar
	 * de lanzar una excepción, permitiendo que la validación continúe.
	 */
	private Date intentarParsearFecha(String fecha) {
		try {
			return Util.isoStringToDate(fecha);
		} catch (Exception e) { // Capturamos ApplicationException o
								// ParseException
			return null;
		}
	}

}
