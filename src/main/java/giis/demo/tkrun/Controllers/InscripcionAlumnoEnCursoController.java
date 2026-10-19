package giis.demo.tkrun.Controllers;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;

import giis.demo.tkrun.DTO.AlumnoDTO;
import giis.demo.tkrun.DTO.CuotaDTO;
import giis.demo.tkrun.DTO.CursoDTO;
import giis.demo.tkrun.DTO.InscripcionDTO;
import giis.demo.tkrun.Models.InscripcionAlumnoEnCursoModel;
import giis.demo.tkrun.Views.InscripcionAlumnoEnCursoView;
import giis.demo.util.SwingUtil;
import giis.demo.util.ValidadorDatos;

public class InscripcionAlumnoEnCursoController {

	private List<CursoDTO> Cursos = new ArrayList<>();
	private CursoDTO cursoSeleccionado;

	private InscripcionAlumnoEnCursoModel model;
	private String fechaSistema;
	private AlumnoDTO alumnoPreseleccionado;
	private InscripcionAlumnoEnCursoView vista;

	public InscripcionAlumnoEnCursoController(String diaActual) {
		model = new InscripcionAlumnoEnCursoModel();
		this.fechaSistema = diaActual;
		cargarDatos();
	}

	public InscripcionAlumnoEnCursoController(String diaActual, AlumnoDTO alumnoPreseleccionado) {
		model = new InscripcionAlumnoEnCursoModel();
		this.fechaSistema = diaActual;
		this.alumnoPreseleccionado = alumnoPreseleccionado;
		cargarDatos();
	}

	public void inicializarVista() {
		vista = new InscripcionAlumnoEnCursoView();

		// Poblar combos desde el controlador
		for (CursoDTO c : Cursos) {
			vista.getComboBoxCursosDisponibles().addItem(c.getNombre());
		}

		final boolean tieneAlumnoPre = (alumnoPreseleccionado != null);
		if (tieneAlumnoPre) {
			vista.getTextNombre().setText(alumnoPreseleccionado.getNombre());
			vista.getTextApellido().setText(alumnoPreseleccionado.getApellidos());
			vista.getTextTelefono().setText(String.valueOf(alumnoPreseleccionado.getTelefono()));
			vista.getTextCorreo().setText(alumnoPreseleccionado.getEmail());
			vista.setTextEditable(false);
		} else {
			vista.setTextEditable(true);
		}

		// Evento combo cursos
		vista.getComboBoxCursosDisponibles().addActionListener(e -> {
			CursoDTO cursoSel = model
					.getCursoPorNombre((String) vista.getComboBoxCursosDisponibles().getSelectedItem());
			setCurso(cursoSel);

			// Mostrar curso en la tabla
			vista.mostrarCursoSeleccionado(cursoSel);

			vista.getBtnNewButton().setEnabled(true);
			if (!tieneAlumnoPre) {
				vista.setTextEditable(true);
			}

			List<CuotaDTO> cuotas = model.obtenerCuotasDisponiblesParaCurso(cursoSel.getId_curso());
			vista.setCuotasDisponibles(cuotas);
		});

		vista.getBtnNewButton().addActionListener(e -> {
			if (SwingUtil.exceptionWrapper(() -> {
				String nombre = vista.getTextNombre().getText().trim();
				String apellido = vista.getTextApellido().getText().trim();
				String telefonoStr = vista.getTextTelefono().getText().trim();
				String correo = vista.getTextCorreo().getText().trim();
				ValidadorDatos.isNotBlank(nombre, "El nombre está vacío");
				ValidadorDatos.isNotBlank(apellido, "Los apellidos están vacíos");
				ValidadorDatos.isNotBlank(correo, "El correo está vacío");
				ValidadorDatos.isNotBlank(telefonoStr, "El teléfono está vacío");
				ValidadorDatos.isDigit(telefonoStr, "El teléfono debe contener solo números");
				ValidadorDatos.isTrue(cursoSeleccionado != null, "Curso no seleccionado.");
			}))
				return;

			AlumnoDTO alumno = new AlumnoDTO();
			int id_alumno = tieneAlumnoPre ? alumnoPreseleccionado.getId_alumno() : generarIDPago(1, 1000000);
			alumno.setId_alumno(id_alumno);
			alumno.setNombre(vista.getTextNombre().getText().trim());
			alumno.setApellidos(vista.getTextApellido().getText().trim());
			alumno.setEmail(vista.getTextCorreo().getText().trim());
			alumno.setTelefono(vista.getTextTelefono().getText().trim());

			InscripcionDTO inscripcion = new InscripcionDTO();
			inscripcion.setId_curso(cursoSeleccionado.getId_curso());

			if (vista.getCuotaSeleccionada().getPrecio() == 0) {
				inscripcion.setEstado("PAGADO");
			} else {
				inscripcion.setEstado("PENDIENTE");
			}

			inscripcion.setCantidad_pagada(0);
			inscripcion.setId_cuota(vista.getCuotaSeleccionada().getId_cuota_curso());
			inscripcion.setBalance_inscripcion(-model.getCuotaInscripcion(inscripcion.getId_cuota()));

			inscribir(alumno, inscripcion);
		});

		vista.setVisible(true);
	}

	private void cargarDatos() {
		Cursos = model.getCursos();
		model.getAlumnos();
	}

	public void inscribir(AlumnoDTO al, InscripcionDTO inscripcion) {

		// Inserción de la persona si no existe
		if (!model.existePersona(al.getId_alumno())) {
			model.insertarPersona(al);
		}

		// Inserción del alumno si no existe
		if (!model.existeAlumno(al.getId_alumno())) {
			model.insertarAlumno(al.getId_alumno());
		}

		// Comprobar si ya existe inscripción
		InscripcionDTO inscExistente = model.getInscripcionPorAlumnoYCursos(al.getId_alumno(),
				inscripcion.getId_curso());

		if (inscExistente != null && !inscExistente.getEstado().equals("CANCELADO")) {
			SwingUtil.exceptionWrapper(
					() -> ValidadorDatos.isTrue(false, "La inscripción ya existe y no está cancelada"));
			return;
		}
		// Validaciones de plazo de inscripción
		Date hoy = Date.valueOf(fechaSistema);
		if (hoy.before(cursoSeleccionado.getIncioInscripcion()) || hoy.after(cursoSeleccionado.getFinInscripcion())) {
			SwingUtil.exceptionWrapper(() -> ValidadorDatos.isTrue(false, "Fuera del plazo de inscripción"));
			return;
		}

		// 4. Validar plazas disponibles y Lista de Espera
		int considerados = model.contarInscripcionesConsideradas(inscripcion.getId_curso(), hoy.toString());
		int numEnEspera = model.obtenerInscripcionesEnEspera(inscripcion.getId_curso());
		if (considerados >= cursoSeleccionado.getNum_plazas() || numEnEspera > 0) {
			int entrar_en_espera = vista.entrarEnListaDeEspera(obtenerMensajeListaDeEspera(inscripcion));

			if (entrar_en_espera == JOptionPane.YES_OPTION) {
				inscripcion.setEstado(InscripcionDTO.INSCRIPCION_LISTA_DE_ESPERA);
				inscripcion.setFecha_solicitud(hoy.toString());
				inscripcion.setCantidad_pagada(0);
				inscripcion.setBalance_inscripcion(0);
				model.insertarInscripcion(inscripcion, al.getId_alumno());
				vista.mensajeConfirmacionAñadidoAListaDeEspera();
				vista.getBtnNewButton().setEnabled(false);
			}
			return;
		}

		// Si existía pero estaba cancelada
		if (inscExistente != null && inscExistente.getEstado().equals("CANCELADO")) {
			inscExistente.setEstado("PENDIENTE");
			inscExistente.setCantidad_pagada(0);
			inscExistente.setId_cuota(inscripcion.getId_cuota());
			inscExistente.setBalance_inscripcion(-model.getCuotaInscripcion(inscExistente.getId_cuota()));
			inscExistente.setFecha_solicitud(Date.valueOf(fechaSistema).toString());
			model.insertarInscripcion(inscExistente, al.getId_alumno());
			// simularEnvioEmail(al, cursoSeleccionado);
			JOptionPane.showMessageDialog(null, "Inscripción realizada correctamente.");
			vista.getBtnNewButton().setEnabled(false);
			return;
		}

		// Insertar inscripción nueva
		inscripcion.setFecha_solicitud(hoy.toString());
		model.insertarInscripcion(inscripcion, al.getId_alumno());
		// simularEnvioEmail(al, cursoSeleccionado);

		JOptionPane.showMessageDialog(null, "Inscripción realizada correctamente.");
		vista.getBtnNewButton().setEnabled(false);
	}

	private String obtenerMensajeListaDeEspera(InscripcionDTO inscripcion) {
		StringBuilder sb = new StringBuilder();
		sb.append("No quedan plazas disponibles en el curso\n");
		int numInscripcionesEnEspera = model.obtenerInscripcionesEnEspera(inscripcion.getId_curso());
		sb.append("Hay " + numInscripcionesEnEspera + " inscripciones en espera para este curso\n");
		sb.append("Desea entrar en la lista de espera?");
		return sb.toString();
	}

	public void setCurso(CursoDTO curso) {
		this.cursoSeleccionado = curso;
	}

	public int generarIDPago(int min, int max) {
		int codigo;
		do {
			codigo = inRange(min, max);
		} while (model.existeIDPago(codigo));
		return codigo;
	}

	private int inRange(int min, int max) {
		return (int) (Math.random() * (max - min) + min);
	}

	// private void simularEnvioEmail(AlumnoDTO al, CursoDTO curso) {
	// String carpeta = "src/main/resources/emails";
	// new File(carpeta).mkdirs();
	//
	// String nombreArchivo = carpeta + "/email_inscripcion_"
	// + al.getId_alumno() + ".txt";
	//
	// try (FileWriter writer = new FileWriter(nombreArchivo)) {
	// writer.write("Estimado/a " + al.getNombre() + " "
	// + al.getApellidos() + ",\n\n");
	// writer.write("Gracias por inscribirse en el curso: "
	// + curso.getNombre() + "\n");
	// writer.write("Fecha del curso: " + curso.getFecha_inicio() + "\n");
	// writer.write("Lugar: " + curso.getLugar() + "\n");
	// writer.write("Hora: " + curso.getFecha_inicio() + " - "
	// + curso.getFecha_fin() + "\n\n");
	// writer.write(
	// "Le confirmamos que su solicitud ha sido registrada correctamente.\n");
	// writer.write(
	// "Recuerde que la plaza se asignará una vez se registre el pago.\n\n");
	// writer.write("Saludos cordiales,\nEquipo de Formación");
	// } catch (IOException e) {
	// throw new RuntimeException(
	// "Error al generar el archivo de simulación de email", e);
	// }
	// }
}