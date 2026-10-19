package giis.demo.util;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import giis.demo.tkrun.DTO.AlumnoDTO;
import giis.demo.tkrun.DTO.CursoDTO;
import giis.demo.tkrun.DTO.ProfesorDTO;

public class GeneradorEmails {
	public static void simularInscripcionCurso(AlumnoDTO al, CursoDTO curso) {

		String carpeta = "src/main/resources/emails";
		new File(carpeta).mkdirs(); // crea la carpeta si no existe

		String nombreArchivo = carpeta + "/email_inscripcion_" + al.getId_alumno() + ".txt";

		try (FileWriter writer = new FileWriter(nombreArchivo)) {
			writer.write("Estimado/a " + al.getNombre() + " " + al.getApellidos() + ",\n\n");
			writer.write("Gracias por inscribirse en el curso: " + curso.getNombre() + "\n");
			writer.write("Fecha del curso: " + curso.getFecha_inicio() + "\n");
			writer.write("Lugar: " + curso.getLugar() + "\n");
			writer.write("Hora: " + curso.getFecha_inicio() + " - " + curso.getFecha_fin() + "\n\n");
			writer.write("Le confirmamos que su solicitud ha sido registrada correctamente.\n");
			writer.write("Recuerde que la plaza se asignará una vez se registre el pago.\n\n");
			writer.write("Saludos cordiales,\nEquipo de Formación");
		} catch (IOException e) {
			throw new RuntimeException("Error al generar el archivo de simulación de email", e);
		}
	}

	public static void informarPagoExcedente(ProfesorDTO profesor, double montoExcedente) {

		String carpeta = "src/main/resources/emails";
		new File(carpeta).mkdirs(); // crea la carpeta si no existe

		String nombreArchivo = carpeta + "/email_error_pago_" + profesor.getId() + ".txt";

		try (FileWriter writer = new FileWriter(nombreArchivo)) {
			writer.write("Estimado/a " + profesor.getNombre() + " " + profesor.getApellidos() + ",\n\n");
			writer.write("Le informamos que durante el proceso de pago se ha producido un error.\n");
			writer.write("El monto procesado supera la cantidad que correspondía.\n\n");
			writer.write("Cantidad pagada en exceso: " + montoExcedente + " EUR\n\n");
			writer.write("Le solicitamos que proceda a devolver dicho importe o contacte con administración\n");
			writer.write("para coordinar la regularización correspondiente.\n\n");
			writer.write("Disculpe las molestias.\n\n");
			writer.write("Saludos cordiales,\n");
			writer.write("Departamento de Administración");
		} catch (IOException e) {
			throw new RuntimeException("Error al generar el archivo de notificación de pago excedente", e);
		}
	}

}
