package giis.demo.tkrun.Controllers;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

import giis.demo.tkrun.DTO.CursoDTO;
import giis.demo.tkrun.Models.CerrarCursosModel;
import giis.demo.tkrun.Views.CerrarCursosView;

public class CerrarCursosController {
	private CerrarCursosModel modelo;
	private CerrarCursosView vista;
	private String fechaActual;
	private DefaultTableModel tablaCursos = new DefaultTableModel(
			new String[] { "Nombre", "Fecha inicio", "Fecha Fin", "Estado del curso" }, 0);
	private List<CursoDTO> cursos;

	public CerrarCursosController(String fechaHoy) {
		modelo = new CerrarCursosModel();
		vista = new CerrarCursosView();
		fechaActual = fechaHoy;
	}

	public void iniciarVista() {
		cargarCursos();
		vista.setVisible(true);
		vista.getTablaCursos().getSelectionModel().addListSelectionListener(e -> listaEstaSeleccionada());
		vista.getBtCerrarCurso().addActionListener(e -> cerrarCurso());
	}

	private void listaEstaSeleccionada() {
		if (vista.getTablaCursos().getSelectedRow() != -1) {
			vista.getBtCerrarCurso().setEnabled(true);
		}
	}

	private void cerrarCurso() {
		int filaSeleccionada = vista.getTablaCursos().getSelectedRow();
		CursoDTO cursoSeleccionado = cursos.get(filaSeleccionada);
		List<String> mensajesDeError = validarImpedimentos(cursoSeleccionado);
		if (!mensajesDeError.isEmpty()) {
			vista.mostrarImpedimentosParaCerrarCurso(imprimirMensajesImposibilidadCerrarCurso(mensajesDeError));
			return;
		}

		List<String> mensajesDeAdvertencia = validarAdvertencias(filaSeleccionada);
		boolean procederAlCierre = true;
		if (!mensajesDeAdvertencia.isEmpty()) {
			int eleccionRegistro = vista
					.mostrarAdvertenciasParaCerrarCursos(imprimirMensajeAdvertenciaCerrarCurso(mensajesDeAdvertencia));
			if (eleccionRegistro != JOptionPane.YES_OPTION) {
				procederAlCierre = false;
			}
		}
		if (procederAlCierre) {
			ejecutarCierre(cursos.get(filaSeleccionada));
		}
	}

	private void ejecutarCierre(CursoDTO cursoDTO) {
		modelo.cerrarCurso(cursoDTO);
		vista.mostrarMensajeCursoCerrado();
		vista.getBtCerrarCurso().setEnabled(false);
		cargarCursos();
	}

	private List<String> validarImpedimentos(CursoDTO cursoSeleccionado) {
		List<String> errores = new ArrayList<>();
		LocalDate fechaHoy = LocalDate.parse(fechaActual);
		LocalDate finCurso = LocalDate.parse(cursoSeleccionado.getFecha_fin());
		errores = comprobarCursoTerminado(fechaHoy, finCurso, errores);
		return errores;
	}

	private String imprimirMensajesImposibilidadCerrarCurso(List<String> mensajesDeError) {
		StringBuilder sb = new StringBuilder();
		sb.append("El curso no puede ser cerrado por los siguientes motivos:\n");
		String errores = String.join("\n", mensajesDeError);
		sb.append(errores);
		return sb.toString();
	}

	private String imprimirMensajeAdvertenciaCerrarCurso(List<String> mensajesDeAdvertencia) {
		StringBuilder sb = new StringBuilder();
		sb.append("El curso no debe cerrarse por los siguientes motivos:\n");
		String errores = String.join("\n", mensajesDeAdvertencia);
		sb.append(errores);
		return sb.toString();
	}

	private List<String> validarAdvertencias(int filaSeleccionada) {
		List<String> mensajes = new ArrayList<>();
		CursoDTO cursoSeleccionado = cursos.get(filaSeleccionada);
		mensajes = comprobarGestionesAlumnosHechas(cursoSeleccionado, mensajes);
		mensajes = comprobarGestionesProfesoresHechas(cursoSeleccionado, mensajes);
		return mensajes;
	}

	private List<String> comprobarCursoTerminado(LocalDate fechaHoy, LocalDate fechaFin, List<String> errores) {
		if (!fechaHoy.isAfter(fechaFin)) {
			errores.add("--- El curso aún no ha terminado (Termina el " + String.valueOf(fechaFin) + ")");
		}
		return errores;
	}

	private List<String> comprobarGestionesProfesoresHechas(CursoDTO cursoSeleccionado, List<String> mensajes) {
		int numProfesoresIncorrectos = modelo.getFacturasCursoPendientes(cursoSeleccionado.getId_curso());

		if (numProfesoresIncorrectos > 0) {
			if (numProfesoresIncorrectos == 1) {
				mensajes.add(
						"--- Hay un profesor para el cual la suma de los movimientos bancarios registrados relacionados con sus pagos no coincide con su remuneración para este curso.");
			} else {
				mensajes.add("--- Hay " + numProfesoresIncorrectos
						+ " profesores para los cuales la suma de los movimientos bancarios registrados relacionados con sus pagos no coincide con su remuneración para este curso.");
			}
		}
		return mensajes;
	}

	private List<String> comprobarGestionesAlumnosHechas(CursoDTO cursoSeleccionado, List<String> mensajes) {
		int numPendientes = modelo.getNumInscripcionesPendientes(cursoSeleccionado.getId_curso());

		if (numPendientes > 0) {
			if (numPendientes == 1) {
				mensajes.add("--- Hay una inscripción que sigue en estado PENDIENTE.");
			} else {
				mensajes.add("--- Hay " + numPendientes + " inscripciones que siguen en estado PENDIENTE.");
			}

		}

		int pagadasIncorrectas = modelo.getNumInscripcionesPagadasIncorrectas(cursoSeleccionado.getId_curso());
		if (pagadasIncorrectas > 0) {
			if (pagadasIncorrectas == 1) {
				mensajes.add("--- Hay un alumno inscrito en el que la suma de movimientos bancarios correspondientes a su inscripción no corresponde con la cuota que tiene que pagar para el curso.");
			} else {
				mensajes.add("--- Hay " + pagadasIncorrectas
						+ " alumnos inscritos en los que la suma de movimientos bancarios correspondientes a sus inscripciones no corresponde con la cuota que tienen que pagar para el curso.");
			}

		}
		int canceladasSinDevolucion = modelo.getNumCancelacionesSinDevolucionCompleta(cursoSeleccionado.getId_curso());
		if (canceladasSinDevolucion > 0) {
			if (canceladasSinDevolucion == 1) {
				mensajes.add("--- Hay una inscripción CANCELADA a la que no se le ha devuelto todo el importe abonado o en la que el alumno debe dinero.");
			} else {
				mensajes.add("--- Hay " + canceladasSinDevolucion
						+ " inscripciones CANCELADAS a las que no se les ha devuelto todo el importe abonado o en las que el alumno debe dinero.");
			}

		}
		return mensajes;
	}

	private void cargarCursos() {
		tablaCursos.setRowCount(0);
		cursos = modelo.getCursosNoCerrados();
		for (CursoDTO curso : cursos) {
			tablaCursos.addRow(new Object[] { curso.getNombre(), curso.getFecha_inicio(), curso.getFecha_fin(),
					curso.getEstado_curso() });
		}
		vista.actualizarTablaCursos(tablaCursos);
	}
}