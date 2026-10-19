package giis.demo.tkrun.Controllers;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;

import javax.swing.JOptionPane;

import giis.demo.tkrun.DTO.AlumnoDTO;
import giis.demo.tkrun.DTO.CursoDTO;
import giis.demo.tkrun.DTO.ProfesorDTO;
import giis.demo.tkrun.Models.RetrasarCursoModel;
import giis.demo.tkrun.Views.RetrasarCursoView;

public class RetrasarCursoController {

	private RetrasarCursoModel modelo;
	private RetrasarCursoView vista;
	private List<CursoDTO> cursos;
	private String fecha;

	public RetrasarCursoController(String fecha) {
		this.fecha = fecha;
		modelo = new RetrasarCursoModel();
		vista = new RetrasarCursoView();
	}

	public void iniciarVista() {
		cargarCursos();
		vista.setVisible(true);
		vista.getBtnActualizarPublic().addActionListener(e -> actualizarFechas());
	}

	private void cargarCursos() {
		cursos = modelo.getCursos();
		vista.getCbCursos().removeAllItems();

		for (CursoDTO c : cursos) {
			vista.getCbCursos().addItem(c.getNombre());
		}

		if (!cursos.isEmpty()) {
			vista.getCbCursos().setSelectedIndex(0);
			cursoSeleccionado();
		}

		vista.getCbCursos().addActionListener(e -> cursoSeleccionado());
	}

	private void cursoSeleccionado() {
		String nombreCurso = (String) vista.getCbCursos().getSelectedItem();
		if (nombreCurso == null)
			return;

		CursoDTO c = modelo.getCursoPorNombre(nombreCurso).get(0);

		// Mostrar fechas actuales
		vista.getLbInscripcionIniPublic().setText(c.getInicio_plazo_inscripcion());
		vista.getLbInscripcionFinPublic().setText(c.getFin_plazo_inscripcion());
		vista.getLbFormacionIniPublic().setText(c.getFecha_inicio());
		vista.getLbFormacionFinPublic().setText(c.getFecha_fin());
	}

	private void actualizarFechas() {
		String nombreCurso = (String) vista.getCbCursos().getSelectedItem();
		if (nombreCurso == null)
			return;

		CursoDTO cursoOriginal = modelo.getCursoPorNombre(nombreCurso).get(0);

		// 🔒 Validación inicial: no permitir retrasar cursos ya iniciados o
		// finalizados
		if (cursoIniciadoOCerrado(cursoOriginal))
			return;

		// Obtener fechas actuales
		LocalDate insIniOriginal = LocalDate.parse(cursoOriginal.getInicio_plazo_inscripcion());
		LocalDate insFinOriginal = LocalDate.parse(cursoOriginal.getFin_plazo_inscripcion());
		LocalDate formInicioOriginal = LocalDate.parse(cursoOriginal.getFecha_inicio());
		LocalDate formFinOriginal = LocalDate.parse(cursoOriginal.getFecha_fin());

		// Obtener nuevas fechas desde la vista
		LocalDate insIniNueva, insFinNueva, formInicioNueva, formFinNueva;
		try {
			insIniNueva = LocalDate.parse(vista.getTfNuevaInscripcionInicio().getText().trim());
			insFinNueva = LocalDate.parse(vista.getTfNuevaInscripcionFin().getText().trim());
			formInicioNueva = LocalDate.parse(vista.getTfNuevaFormacionInicio().getText().trim());
			formFinNueva = LocalDate.parse(vista.getTfNuevaFormacionFin().getText().trim());
		} catch (DateTimeParseException e) {
			JOptionPane.showMessageDialog(null, "Formato de fecha inválido. Use YYYY-MM-DD", "Error",
					JOptionPane.ERROR_MESSAGE);
			return;
		}

		// Validar consistencia y progreso de fechas
		if (!fechasValidas(insIniOriginal, insFinOriginal, formInicioOriginal, formFinOriginal, insIniNueva,
				insFinNueva, formInicioNueva, formFinNueva))
			return;

		// Actualizar fechas en el modelo
		modelo.actualizarFechasCurso(nombreCurso, insIniNueva.toString(), insFinNueva.toString(),
				formInicioNueva.toString(), formFinNueva.toString());

		// Marcar curso como retrasado y enviar notificaciones
		CursoDTO cursoActualizado = modelo.getCursoPorNombre(nombreCurso).get(0);
		cursoActualizado.setRetrasado(true);
		notificarAlumnosYProfesor(cursoActualizado);
		cursoActualizado.setFecha_retraso(fecha);

		modelo.actualizar(cursoActualizado);

		cursoSeleccionado();

		JOptionPane.showMessageDialog(null,
				"Fechas actualizadas correctamente. Se notificará al profesorado y a los alumnos", "Información",
				JOptionPane.INFORMATION_MESSAGE);
	}

	// MÉTODOS AUXILIARES

	private boolean cursoIniciadoOCerrado(CursoDTO curso) {
		LocalDate fechaFin = LocalDate.parse(curso.getFecha_fin());
		LocalDate fechaActual = LocalDate.parse(fecha); // fecha del constructor
		if (!fechaActual.isBefore(fechaFin)) {
			JOptionPane.showMessageDialog(null, "No se puede retrasar un curso que ya ha finalizado", "Error",
					JOptionPane.ERROR_MESSAGE);
			return true;
		}
		return false;
	}

	private boolean fechasValidas(LocalDate insIniOriginal, LocalDate insFinOriginal, LocalDate formInicioOriginal,
			LocalDate formFinOriginal, LocalDate insIniNueva, LocalDate insFinNueva, LocalDate formInicioNueva,
			LocalDate formFinNueva) {

		// 1. Nueva inscripción inicio no puede ser anterior a la original
		if (insIniNueva.isBefore(insIniOriginal)) {
			mostrarError("La nueva fecha de inicio de inscripción no puede ser anterior a la actual (" + insIniOriginal
					+ ")");
			return false;
		}

		// 2. Nueva inscripción fin no puede ser anterior a la original
		if (insFinNueva.isBefore(insFinOriginal)) {
			mostrarError(
					"La nueva fecha de fin de inscripción debe ser posterior a la actual (" + insFinOriginal + ")");
			return false;
		}

		// 3. Nueva inscripción fin no puede ser anterior a nueva inscripción
		// inicio
		if (insFinNueva.isBefore(insIniNueva)) {
			mostrarError(
					"La nueva fecha de fin de inscripción no puede ser anterior a la nueva fecha de inicio de inscripción");
			return false;
		}

		// 4. Nueva formación inicio no puede ser anterior a la original
		if (formInicioNueva.isBefore(formInicioOriginal)) {
			mostrarError("La nueva fecha de inicio de formación debe ser el mismo día o posterior a la actual ("
					+ formInicioOriginal + ")");
			return false;
		}

		// 5. Nueva formación fin no puede ser anterior a la original
		if (formFinNueva.isBefore(formFinOriginal)) {
			mostrarError("La nueva fecha de fin de formación debe ser el mismo día o posterior a la actual ("
					+ formFinOriginal + ")");
			return false;
		}

		// 6. Nueva formación inicio no puede ser antes de nueva inscripción fin
		if (formInicioNueva.isBefore(insFinNueva)) {
			mostrarError(
					"La nueva fecha de inicio de formación debe ser el mismo día o posterior a la nueva fecha de fin de inscripción");
			return false;
		}

		// 7. Nueva formación fin no puede ser antes de nueva formación inicio
		if (formFinNueva.isBefore(formInicioNueva)) {
			mostrarError(
					"La nueva fecha de fin de formación no puede ser anterior a la nueva fecha de inicio de formación");
			return false;
		}

		if (!algunaFechaRetrasada(insIniOriginal, insFinOriginal, formInicioOriginal, formFinOriginal, insIniNueva,
				insFinNueva, formInicioNueva, formFinNueva)) {
			mostrarError("No se ha retrasado ninguna fecha");
			return false;
		}

		return true; // todo válido
	}

	private boolean algunaFechaRetrasada(LocalDate insIniOriginal, LocalDate insFinOriginal,
			LocalDate formInicioOriginal, LocalDate formFinOriginal, LocalDate insIniNueva, LocalDate insFinNueva,
			LocalDate formInicioNueva, LocalDate formFinNueva) {

		return insIniNueva.isAfter(insIniOriginal) || insFinNueva.isAfter(insFinOriginal)
				|| formInicioNueva.isAfter(formInicioOriginal) || formFinNueva.isAfter(formFinOriginal);
	}

	private void mostrarError(String msg) {
		JOptionPane.showMessageDialog(null, msg, "Error", JOptionPane.ERROR_MESSAGE);
	}

	private void notificarAlumnosYProfesor(CursoDTO curso) {
		List<AlumnoDTO> alumnos = modelo.getAlumnosPorCurso(curso.getId_curso());
		for (AlumnoDTO al : alumnos)
			simularEnvioEmailRetraso(al, curso);

		ProfesorDTO profesor = curso.getProfesor();
		if (profesor != null)
			simularEnvioEmailProfesor(profesor, curso);
	}

	private void simularEnvioEmailRetraso(AlumnoDTO al, CursoDTO curso) {
		String carpeta = "src/main/resources/emails";
		new File(carpeta).mkdirs();

		String nombreArchivo = carpeta + "/email_retraso_alumno_" + al.getId() + ".txt";

		try (FileWriter writer = new FileWriter(nombreArchivo)) {
			writer.write("Estimado/a " + al.getNombre() + " " + al.getApellidos() + ",\n\n");
			writer.write("Le informamos de que el curso \"" + curso.getNombre() + "\" ha sido reprogramado.\n");
			writer.write("Nueva fecha de inicio: " + curso.getFecha_inicio() + "\n");
			writer.write("Nueva fecha de finalización: " + curso.getFecha_fin() + "\n");
			writer.write("Lugar: " + curso.getLugar() + "\n\n");
			writer.write(
					"Podrá solicitar la devolución del importe en caso de no poder asistir a las nuevas fechas.\n");
			writer.write("Saludos cordiales,\nEquipo de Formación");
		} catch (IOException e) {
			throw new RuntimeException("Error al generar el archivo de simulación de email (alumno)", e);
		}
	}

	private void simularEnvioEmailProfesor(ProfesorDTO profesor, CursoDTO curso) {
		String carpeta = "src/main/resources/emails";
		new File(carpeta).mkdirs();

		String nombreArchivo = carpeta + "/email_retraso_profesor_" + profesor.getId() + ".txt";

		try (FileWriter writer = new FileWriter(nombreArchivo)) {
			writer.write("Estimado/a " + profesor.getNombre() + " " + profesor.getApellidos() + ",\n\n");
			writer.write("Le informamos de que su curso \"" + curso.getNombre() + "\" ha sido reprogramado.\n");
			writer.write("Nuevas fechas: " + curso.getFecha_inicio() + " - " + curso.getFecha_fin() + "\n");
			writer.write("Lugar: " + curso.getLugar() + "\n\n");
			writer.write("Saludos cordiales,\nEquipo de Formación");
		} catch (IOException e) {
			throw new RuntimeException("Error al generar el archivo de simulación de email (profesor)", e);
		}
	}

}
