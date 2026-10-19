package giis.demo.tkrun.Models;

import java.util.List;
import java.util.Map;

import giis.demo.tkrun.DTO.CuotaDTO;
import giis.demo.tkrun.DTO.CursoDTO;
import giis.demo.tkrun.DTO.InscripcionDTO;
import giis.demo.util.Database;

public class BalanceModel {

	private Database dataBase;

	public BalanceModel() {
		dataBase = new Database();
	}

	private String sqlIngresoAsguradoPorCurso = "SELECT sum(pago.cantidad_pagada) from pago join inscripcion on pago.id_inscripcion = inscripcion.id_inscripcion join curso on	 curso.id_curso=inscripcion.id_curso where curso.id_curso = ?";

	private String sqlCursoPorFecha = "SELECT * FROM curso WHERE fecha_inicio BETWEEN ? AND ?";

	private String sqlGastoAsguradoPorCurso = "select SUM(pf.cantidad_transferida) from pago_profesor pf join factura_profesor fp on pf.id_factura = fp.id_factura join actividad_profesor ap on ap.id_actividad_profesor = fp.id_actividad_profesor join curso c on c.id_curso =ap.id_curso  where c.id_curso = ?";

	private String sqlGastoProfesorado = "select sum(remuneracion) from actividad_profesor where id_curso = ?";

	private String sqlGetMaximaCuota = "SELECT max(precio) precio FROM 'cuota_curso' where id_curso= ?";

	public List<CursoDTO> cargarCursosPorFecha(String fechaInicio, String fechaFin) {

		return dataBase.executeQueryPojo(CursoDTO.class, sqlCursoPorFecha, fechaInicio, fechaFin);
	}

	public List<Object[]> ingresosAseguradosCursos(int i) {
		return dataBase.executeQueryArray(sqlIngresoAsguradoPorCurso, i);
	}

	public List<Object[]> getPagasProfesores(int i) {
		return dataBase.executeQueryArray(sqlGastoAsguradoPorCurso, i);
	}

	public List<Object[]> getGastoProfesorado(int id_Curso) {
		return dataBase.executeQueryArray(sqlGastoProfesorado, id_Curso);
	}

	public int getCuotasInscritos(int id_curso) {
		List<Map<String, Object>> datos = dataBase.executeQueryMap(querySumaCuotasInscritos, id_curso);
		if (datos.size() > 0 && datos.get(0).get("ingr") != null) {
			return Double.valueOf("" + datos.get(0).get("ingr")).intValue();
		}
		return 0;
	}

	private String getCuotasPorCurso = "Select * from cuota_curso where cuota_curso.id_curso = ? ";

	public List<CuotaDTO> getCuotaMasCara(int id_curso) {
		return dataBase.executeQueryPojo(CuotaDTO.class, getCuotasPorCurso, id_curso);
	}

	private String querySumaCuotasInscritos = "select sum(cc.precio) as ingr from cuota_curso cc join inscripcion i on i.id_cuota=cc.id_cuota_curso where i.id_curso =? and (i.estado = 'PAGADO' or (i.estado = 'PENDIENTE'"
			+ " and cast(JULIANDAY(?) - JULIANDAY(i.fecha_solicitud) as integer) <= 2))";

	public int getSumaCuotasInscritos(int id_curso, String fechaActual) {
		List<Map<String, Object>> datos = dataBase.executeQueryMap(querySumaCuotasInscritos, id_curso, fechaActual);
		if (datos.size() > 0 && datos.get(0).get("ingr") != null) {
			return Double.valueOf("" + datos.get(0).get("ingr")).intValue();
		}
		return 0;
	}

	public int getSumaRemuneracionProfesores(int id_Curso) {
		// Reutilizamos la query existente 'sqlGastoProfesorado'
		List<Object[]> resultado = dataBase.executeQueryArray(sqlGastoProfesorado, id_Curso);

		if (resultado.isEmpty() || resultado.get(0) == null || resultado.get(0)[0] == null) {
			return 0;
		}
		return (Integer) resultado.get(0)[0];
	}

	private String obtener_plazas_curso_ocupadas = "select * from inscripcion i where i.id_curso = ? " + " and ( "
			+ "i.estado = 'PAGADO' or (i.estado = 'PENDIENTE' "
			+ "and cast(JULIANDAY(?) - JULIANDAY(i.fecha_solicitud) as integer) <= 2))";

	public int obtenerPlazasCursoOcupadas(int id_curso, String fechaActual) {
		List<InscripcionDTO> plazas_ocupadas = dataBase.executeQueryPojo(InscripcionDTO.class,
				obtener_plazas_curso_ocupadas, id_curso, fechaActual);
		return plazas_ocupadas.size();
	}

	public int getSumaCuotasRestantes(CursoDTO cdto, String fechaActual) {
		int plazasOcupadas = obtenerPlazasCursoOcupadas(cdto.getId_curso(), fechaActual);

		return (cdto.getNum_plazas() - plazasOcupadas) * maximaCuotaIdCurso(cdto.getId_curso());
	}

	public int cuantasPlazasOcupadas(int id_curso, String fecha) {
		List<InscripcionDTO> plazas_ocupadas = dataBase.executeQueryPojo(InscripcionDTO.class, querySumaCuotasInscritos,
				id_curso, fecha);
		return plazas_ocupadas.size();
	}

	public int maximaCuotaIdCurso(int id_curso) {
		List<Map<String, Object>> datos = dataBase.executeQueryMap(sqlGetMaximaCuota, id_curso);
		if (datos.size() > 0 && datos.get(0).get("precio") != null) {
			return Integer.valueOf("" + datos.get(0).get("precio"));
		}
		return 0;
	}

	private String querySumanGananciasCancelaciones = "select  sum(cantidad_pagada - balance_inscripcion) ingr from inscripcion where estado = 'CANCELADO' and id_curso = ?";

	public int getSumaGananciasCancelaciones(int i) {
		List<Map<String, Object>> datos = dataBase.executeQueryMap(querySumanGananciasCancelaciones, i);
		if (datos.size() > 0 && datos.get(0).get("ingr") != null) {
			return Double.valueOf("" + datos.get(0).get("ingr")).intValue();
		}
		return 0;
	}

}
