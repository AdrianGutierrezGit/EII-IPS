/**
 * 
 */
package giis.demo.tkrun.Controllers;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

import giis.demo.tkrun.DTO.AlumnoDTO;
import giis.demo.tkrun.DTO.CursoDTO;
import giis.demo.tkrun.DTO.InscripcionDTO;
import giis.demo.tkrun.Models.CancelacionInscripcionModel;
import giis.demo.tkrun.Views.CancelacionInscripcionView;

/**
 * 
 */
public class CancelacionInscripcionController {
	private CancelacionInscripcionModel modelo;
	private CancelacionInscripcionView vista;
	private AlumnoDTO alumno;
	private String fechaHoy;
	private int dinero;
	private InscripcionDTO inscripcion;
	private int porcentaje;

	public CancelacionInscripcionController(AlumnoDTO alumno, String fecha) {
		modelo = new CancelacionInscripcionModel();
		vista = new CancelacionInscripcionView();
		this.alumno = alumno;
		fechaHoy = fecha;
		cargaCursos();
		vista.getCbCursosInsc().addActionListener(e -> seleccionCurso());
		vista.getBtSiguiente().addActionListener(e -> procederCancelacion());
	}

	private void seleccionCurso() {
		vista.getBtSiguiente().setEnabled(true);

		CursoDTO curso = modelo.getCursoDTO((String) vista.getCbCursosInsc().getSelectedItem());
		if (curso != null)
			inscripcion = modelo.getInscripcion(curso.getId_curso(), alumno.getId());
		if (curso != null) {
			vista.getTfNombreCurso().setText(curso.getNombre());
			vista.getTfFechaCurso().setText(curso.getFecha_inicio().toString());
			vista.getTfCuotaCurso().setText("" + modelo.getPrecioCuota(inscripcion.getId_cuota(), curso.getId_curso()));

			inscripcion = modelo.getInscripcion(curso.getId_curso(), alumno.getId_alumno());
			calcularDevolucion(curso);
			vista.getTfEstadoInscripcion().setText("" + inscripcion.getEstado());

			vista.getTfPorcentaje().setText("" + porcentaje + "%");
			int cantidad = dinero + inscripcion.getBalance_inscripcion();
			if (cantidad < 0) {
				cantidad = 0;
			}
			vista.getTfDineroDevolver().setText("" + (cantidad));
		} else {
			vista.getTfNombreCurso().setText("");
			vista.getTfFechaCurso().setText("");
			vista.getTfCuotaCurso().setText("");
			vista.getTfPorcentaje().setText("");
			vista.getTfDineroDevolver().setText("");
			vista.getTfDiasCelebra().setText("");
			vista.getBtSiguiente().setEnabled(false);
			vista.getTfEstadoInscripcion().setText("");
		}
	}

	private void cargaCursos() {
		List<String> nombresCursos = modelo.getCursosInscritoAlumno(alumno.getId_alumno());
		vista.setCursos(nombresCursos);
	}

	public void iniciarVista() {
		vista.setVisible(true);
	}

	private void calcularDevolucion(CursoDTO curso) {
		// Tenemos inscripcion, alumno y el curso
		int cuota = modelo.getPrecioCuota(inscripcion.getId_cuota(), curso.getId_curso());
		LocalDate fechaCursoDate = LocalDate.parse(curso.getFecha_inicio());
		LocalDate fechaHoyDate = LocalDate.parse(fechaHoy);
		int dias = (int) ChronoUnit.DAYS.between(fechaHoyDate, fechaCursoDate);
		if (curso.isRetrasado()) {
			LocalDate fechaRetraso = LocalDate.parse(curso.getFecha_retraso());
			// ⚠️ Suponemos que el retraso fue registrado hace poco: usamos la
			// diferencia entre la fecha actual y el inicio del nuevo curso (es
			// una forma de estimar el retraso sin guardar un campo extra)
			int diasDesdeRetraso = (int) Math.abs(ChronoUnit.DAYS.between(fechaRetraso, fechaHoyDate));

			if (diasDesdeRetraso <= 2) {
				// Si se cancela dentro de los 2 días del retraso → devolución
				// total
				dinero = cuota;
				porcentaje = 100;
				vista.getTfDiasCelebra().setText("Curso retrasado");
				return;
			}
			// Si ya pasaron más de 2 días desde el retraso → sigue el flujo
			// normal
		}

		if (dias < 0) {
			dinero = 0;
			porcentaje = 0;
			vista.getTfDiasCelebra().setText("Celebrado");
			vista.getBtSiguiente().setEnabled(false);
		} else {
			vista.getTfDiasCelebra().setText("" + dias);
			if (inscripcion.getEstado().equals(InscripcionDTO.INSCRIPCION_LISTA_DE_ESPERA)) {
				dinero = 0;
				porcentaje = 0;
			} else if (dias >= 7) {
				dinero = cuota; // 100%
				porcentaje = 100;
			} else if (dias >= 3) {
				dinero = (int) (cuota * 0.5); // 50%
				porcentaje = 50;
			} else {
				dinero = 0; // 0%
				porcentaje = 0;
			}

		}
	}

	private void procederCancelacion() {
		String estadoOriginal = inscripcion.getEstado();
		int cantidad = dinero + inscripcion.getBalance_inscripcion();
		if (cantidad < 0) {
			cantidad = 0;
		}
		int realizar = vista.preguntarConfirmacion(cantidad);
		if (realizar == 1) {
			modelo.cancelarInscripcion(inscripcion);
			if (dinero >= 0) {
				if (dinero + inscripcion.getBalance_inscripcion() < 0) {
					modelo.insertarDevolucion(inscripcion.getId_inscripcion(), -inscripcion.getBalance_inscripcion());
				} else {
					modelo.insertarDevolucion(inscripcion.getId_inscripcion(), dinero);
				}

			}
			if (!estadoOriginal.equals(InscripcionDTO.INSCRIPCION_LISTA_DE_ESPERA)) {
				InscripcionDTO siguiente = modelo.getPrimeroEnListaDeEsperta(inscripcion.getId_curso());
				if (siguiente != null) {
					modelo.inscribirAlumno(fechaHoy, siguiente.getId_inscripcion(),
							-modelo.getPrecioCuota(inscripcion.getId_cuota(), inscripcion.getId_curso()));
				}
			}
			cargaCursos();
		}
	}
}