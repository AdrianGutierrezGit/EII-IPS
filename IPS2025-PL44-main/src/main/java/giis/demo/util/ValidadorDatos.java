package giis.demo.util;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

import giis.demo.tkrun.DTO.CursoDTO;

public class ValidadorDatos {

	public static void isNotBlank(String str, String msg) {
		isTrue(str != null && !str.isBlank(), msg);
	}

	public static void isTrue(boolean condition, String msg) {
		if (!condition)
			throw new ApplicationException(msg);
	}

	public static void isNotNull(Object o) {
		if (o == null)
			throw new ApplicationException("Cannot be null.");
	}

	public static void isDigit(String str, String msg) {
		for (char caracter : str.toCharArray()) {
			if (!Character.isDigit(caracter)) {
				throw new ApplicationException(msg);
			}
		}
	}

	public static void isDate(String str, String msg) {
		if (!str.matches("\\d{4}-\\d{2}-\\d{2}")) {
			throw new ApplicationException(msg);
		}

	}

	public static boolean fechaPagoSuperaLimite(LocalDate fechaPago, String fecha_solicitud) {
		if (ChronoUnit.DAYS.between(LocalDate.parse(fecha_solicitud), fechaPago) > 2) {
			return true;
		}
		return false;

	}

	public static void comprobarPlazas(int plazasAsignadasAlCurso, int plazasCurso, String msg) {
		if (plazasCurso < plazasAsignadasAlCurso + 1) {
			throw new ApplicationException(msg);
		}

	}

	public static void comprobarFecha(CursoDTO cursoDTO, String fechaActual, String msg) {
		if (LocalDate.parse(fechaActual).isBefore(LocalDate.parse(cursoDTO.getFecha_fin()))) {
			throw new ApplicationException(msg);
		}

	}

	public static void comprobarCursoCerrado(String estado_curso, String msg) {
		if (estado_curso.trim().equals(CursoDTO.ESTADO_CERRADO)) {
			throw new ApplicationException(msg);
		}

	}

	public static void estaYaPagada(String estado, boolean pagada, String msg) {
		if (estado.equals("PAGADO") && pagada) {
			throw new ApplicationException(msg);
		}

	}

	public static void comprobarFechaPagoSuperiorAFechaInscripcion(String fechaActual, String fechaPago, String msg) {
		if (LocalDate.parse(fechaPago).isBefore(LocalDate.parse(fechaActual))) {
			throw new ApplicationException(msg);
		}

	}

	public static void comprobarCursoCancelado(String estado_curso, String msg) {
		if (estado_curso.trim().equals(CursoDTO.ESTADO_CANCELADO)) {
			throw new ApplicationException(msg);
		}

	}

}
