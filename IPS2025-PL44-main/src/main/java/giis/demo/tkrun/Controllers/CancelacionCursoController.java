/**
 * 
 */
package giis.demo.tkrun.Controllers;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import giis.demo.tkrun.DTO.CursoDTO;
import giis.demo.tkrun.DTO.InscripcionDTO;
import giis.demo.tkrun.Models.CancelacionCursoModel;
import giis.demo.tkrun.Views.CancelacionCursoView;

/**
 * 
 */
public class CancelacionCursoController {
	private CancelacionCursoModel modelo;
	private CancelacionCursoView vista;
	private String fecha;

	public CancelacionCursoController(String fecha) {
		modelo = new CancelacionCursoModel();
		vista = new CancelacionCursoView();
		this.fecha = fecha;

		cargaCursos();
		vista.getCbCursosInsc().addActionListener(e -> seleccionCurso());
		vista.getBtSiguiente().addActionListener(e -> procederCancelacion());
	}

	public String getFecha() {
		return fecha;
	}

	private void seleccionCurso() {
		CursoDTO curso = modelo.getCursoDTO((String) vista.getCbCursosInsc().getSelectedItem());
		if (curso != null) {
			vista.getTfNombreCurso().setText(curso.getNombre());
			actualizarEstado(curso);
			actualizarFecha(curso);
			actualizarBotonCancelar(curso);
			// Plazas
			vista.getTfNPlazas().setText("" + curso.getNum_plazas());
			int n = modelo.cuantasPlazasOcupadas(curso.getId_curso(), fecha);
			vista.getTfPlazasDisponibles().setText("" + (curso.getNum_plazas() - n));
			calcularIngresosYGastos(curso);
			vista.getLbPorcentaje().setVisible(curso.getEstado_curso().equals("en_celebracion"));
			vista.getTfPorcentaje().setVisible(curso.getEstado_curso().equals("en_celebracion"));
			vista.getTxtrAyuda().setVisible(curso.getEstado_curso().equals("en_celebracion"));
		} else {
			vista.getTfNombreCurso().setText("");
			vista.getTfFechaCurso().setText("");
			vista.getBtSiguiente().setEnabled(false);
			vista.getTfEstadoInscripcion().setText("");
			vista.getTfNPlazas().setText("");
			vista.getTfPlazasDisponibles().setText("");
			vista.getTfIngEstimados().setText("");
			vista.getTfIngrConfirmados().setText("");
			vista.getTfGastEstimados().setText("");
			vista.getTfGastConfirmados().setText("");
			vista.getLbPorcentaje().setVisible(false);
			vista.getTfPorcentaje().setVisible(false);
			vista.getTxtrAyuda().setVisible(false);
		}
	}

	private void actualizarBotonCancelar(CursoDTO curso) {
		if (LocalDate.parse(fecha).isAfter(curso.getFechaFinCurso().toLocalDate())
				|| curso.getEstado_curso().equals("cancelado")) {
			vista.getBtSiguiente().setEnabled(false);
		} else {
			vista.getBtSiguiente().setEnabled(true);
		}

	}

	private void actualizarFecha(CursoDTO curso) {
		if (curso.getFecha_fin().equals(curso.getFecha_inicio())) {
			vista.getTfFechaCurso().setText(curso.getFecha_inicio().toString());
		} else {
			vista.getTfFechaCurso()
					.setText(curso.getFecha_inicio().toString() + " - " + curso.getFecha_fin().toString());
		}
	}

	private void procederCancelacion() {
		CursoDTO curso = modelo.getCursoDTO((String) vista.getCbCursosInsc().getSelectedItem());
		if (curso.getEstado_curso().equals("curso_cerrado") || curso.getEstado_curso().equals("cancelado")
				|| LocalDate.parse(fecha).isAfter(LocalDate.parse(curso.getFecha_fin()))) {
			vista.showMensaje("No se puede cancelar un curso en este estado.");
		} else if (LocalDate.parse(fecha).isAfter(LocalDate.parse(curso.getFecha_inicio()))) {
			try {
				int porcentaje = Integer.parseInt(vista.getTfPorcentaje().getText().trim().replace("%", ""));
				// Cancelacion de una actividad en curso
				cancelacionCurso(curso);
				cancelacionInscripciones(curso, porcentaje);
				modificacionPagosProfesores(curso, porcentaje);
				vista.showMensaje("Curso " + curso.getNombre() + " cancelado.");
				cargaCursos();
			} catch (NumberFormatException e) {
				vista.showMensaje("Debe introducir un porcentaje valido para poder cancelar el curso.");
			}
		} else {
			cancelacionCurso(curso);
			cancelacionInscripciones(curso, 0);
			vista.showMensaje("Curso " + curso.getNombre() + " cancelado.");
			cargaCursos();
		}
	}

	private void modificacionPagosProfesores(CursoDTO curso, int porcentaje) {
		modelo.setRemuneracionesAPorcentaje(curso.getId_curso(), porcentaje);
	}

	private void cancelacionInscripciones(CursoDTO curso, double porcentajeCurso) {
		double portentajeDevolucion = (100 - porcentajeCurso) / 100;
		for (InscripcionDTO i : modelo.getInscripcions(curso.getId_curso())) {
			if (!i.getEstado().equals(InscripcionDTO.INSCRIPCION_CANCELADA)) {
				if (!i.getEstado().equals(InscripcionDTO.INSCRIPCION_LISTA_DE_ESPERA)) {
					modelo.insertarDevolucion(i.getId_inscripcion(),
							(int) (portentajeDevolucion * modelo.getPrecioCuota(i.getId_cuota(), i.getId_curso())));

				}
				modelo.cancelarInscripcion(i);
			}
		}

	}

	private void cancelacionCurso(CursoDTO curso) {
		modelo.cancelarCurso(curso);
	}

	private void actualizarEstado(CursoDTO cdto) {
		String e = "";
		switch (cdto.getEstado_curso()) {
		case "planificado":
			e = "Planificado";
			break;
		case "periodo_inscripcion":
			e = "Periodo de inscripción";
			break;
		case "inscripcion_cerrada":
			e = "Inscripción cerrada";
			break;
		case "curso_cerrado":
			e = "Curso cerrado";
			break;
		case "cancelado":
			e = "Cancelado";
			break;
		case "en_celebracion":
			e = "En celebración";
			break;
		case "celebrado":
			e = "Celebrado";
			break;
		}
		vista.getTfEstadoInscripcion().setText(e);
	}

	private void cargaCursos() {
		List<String> nombresCursos = new ArrayList<String>();
		for (CursoDTO c : modelo.getCursos()) {
			nombresCursos.add(c.getNombre());
		}
		vista.setCursos(nombresCursos);
	}

	public void iniciarVista() {
		vista.setVisible(true);
	}

	private void calcularIngresosYGastos(CursoDTO cdto) {
		int ingresosEstimados = modelo.getSumaCuotasInscritos(cdto.getId_curso(), fecha)
				+ modelo.getSumaCuotasRestantes(cdto, fecha) + modelo.getSumaGananciasCancelaciones(cdto.getId_curso());
		int ingresosConfirmados = modelo.ingresosConfirmados(cdto.getId_curso());
		int gastosEstimados = modelo.gastosEstimados(cdto.getId_curso());
		int gastosConfirmados = modelo.gastosConfirmados(cdto.getId_curso());

		vista.getTfIngEstimados().setText("" + ingresosEstimados);
		vista.getTfIngrConfirmados().setText("" + ingresosConfirmados);
		vista.getTfGastEstimados().setText("" + gastosEstimados);
		vista.getTfGastConfirmados().setText("" + gastosConfirmados);
	}

}
