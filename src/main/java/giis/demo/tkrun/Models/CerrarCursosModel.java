package giis.demo.tkrun.Models;

import java.util.List;

import giis.demo.tkrun.DTO.CursoDTO;
import giis.demo.tkrun.DTO.InscripcionDTO;
import giis.demo.util.Database;

public class CerrarCursosModel {
	private Database dataBase;

	private String obtener_cursos_no_cerrados = "select * from curso where estado_curso != 'curso_cerrado'";
	private String cerrar_curso = "update curso set estado_curso = 'curso_cerrado' where id_curso= ?";

	private String num_inscripciones_pendientes = "select * from inscripcion where id_curso = ? and estado = 'PENDIENTE'";

	private final static String profesores_pendientes = "SELECT COUNT(*) FROM (" + "   SELECT ap.id_actividad_profesor "
			+ "   FROM actividad_profesor ap "
			+ "   LEFT JOIN factura_profesor fp ON ap.id_actividad_profesor = fp.id_actividad_profesor "
			+ "   LEFT JOIN pago_profesor pp ON fp.id_factura = pp.id_factura " + "   WHERE ap.id_curso = ? "
			+ "   GROUP BY ap.id_actividad_profesor, ap.remuneracion "
			+ "   HAVING COALESCE(SUM(pp.cantidad_transferida), 0) != ap.remuneracion)";

	private String inscripciones_canceladas_con_pago_incompleto = "select * from inscripcion where id_curso = ? and estado = 'CANCELADO' and balance_inscripcion != 0";
	private String num_inscripciones_pagadas_incorrectas = "select * from inscripcion i, cuota_curso c where i.id_curso = ? and"
			+ " i.id_cuota = c.id_cuota_curso and (i.estado = 'PAGADO' and i.cantidad_pagada != c.precio)";

	public CerrarCursosModel() {
		dataBase = new Database();
	}

	public List<CursoDTO> getCursosNoCerrados() {
		return dataBase.executeQueryPojo(CursoDTO.class, obtener_cursos_no_cerrados);
	}

	public void cerrarCurso(CursoDTO curso) {
		dataBase.executeUpdate(cerrar_curso, curso.getId_curso());
	}

	public int getFacturasCursoPendientes(int id_curso) {
		List<Object[]> result = dataBase.executeQueryArray(profesores_pendientes, id_curso);

		if (result != null && !result.isEmpty()) {
			Object[] row = result.get(0);

			Object valor = row[0];

			if (valor instanceof Number) {
				return ((Number) valor).intValue();
			}
		}

		return 0;
	}

	public int getNumCancelacionesSinDevolucionCompleta(int id_curso) {
		List<InscripcionDTO> inscripcionesCanceladasConDevolucionIncompleta = dataBase
				.executeQueryPojo(InscripcionDTO.class, inscripciones_canceladas_con_pago_incompleto, id_curso);
		return inscripcionesCanceladasConDevolucionIncompleta.size();
	}

	public int getNumInscripcionesPendientes(int id_curso) {
		List<InscripcionDTO> inscripcionesPendientes = dataBase.executeQueryPojo(InscripcionDTO.class,
				num_inscripciones_pendientes, id_curso);
		return inscripcionesPendientes.size();
	}

	public int getNumInscripcionesPagadasIncorrectas(int id_curso) {
		List<InscripcionDTO> inscripcionesPagadasIncorrectas = dataBase.executeQueryPojo(InscripcionDTO.class,
				num_inscripciones_pagadas_incorrectas, id_curso);
		return inscripcionesPagadasIncorrectas.size();
	}
}