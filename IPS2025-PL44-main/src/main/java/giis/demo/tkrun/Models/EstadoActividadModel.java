package giis.demo.tkrun.Models;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import giis.demo.tkrun.DTO.CuotaDTO;
import giis.demo.tkrun.DTO.CursoDTO;
import giis.demo.tkrun.DTO.InscripcionDTO;
import giis.demo.util.Database;

public class EstadoActividadModel {

	private Database dataBase;
	private String sqlGetCursos = "Select * from curso";
	private String sqlGetInscritos = "select nombre, apellidos, fecha_solicitud, estado from inscripcion i, persona p where p.id = i.id_alumno and id_curso = ?";
	private String obtener_plazas_curso_ocupadas = "select * from inscripcion i where i.id_curso = ? " + " and ( "
			+ "i.estado = 'PAGADO' or (i.estado = 'PENDIENTE' "
			+ "and cast(JULIANDAY(?) - JULIANDAY(i.fecha_solicitud) as integer) <= 2))";
	private String sqlGetIngresosConfirmados = "select sum(p.cantidad_pagada) as ingr from pago p join inscripcion i on i.id_inscripcion = p.id_inscripcion where i.id_curso = ?";
	private String sqlGetIngresosEstimados = "SELECT SUM(c.cuota) ingr FROM curso c, inscripcion i "
			+ " where c.id_curso = i.id_curso and estado <> 'CANCELADO' and c.id_curso = ?";
	private String sqlGetGastosEstimados = "SELECT sum(remuneracion) gast FROM actividad_profesor where id_curso = ?";
	private String sqlGetGastosConfirmados = "SELECT sum(cantidad_transferida) gas FROM pago_profesor p, factura_profesor f,"
			+ " actividad_profesor a where p.id_factura = f.id_factura and f.id_actividad_profesor"
			+ "= a.id_actividad_profesor and a.id_curso = ? ";
	private String sqlGetMaximaCuota = "SELECT max(precio) precio FROM 'cuota_curso' where id_curso= ?";

	public EstadoActividadModel() {
		dataBase = new Database();
	}

	public List<CursoDTO> getCursos() {
		return dataBase.executeQueryPojo(CursoDTO.class, sqlGetCursos);
	}

	public List<String> getNombresCursos() {
		List<CursoDTO> cDto = getCursos();

		List<String> nombres = new ArrayList<String>();
		for (CursoDTO dto : cDto) {
			nombres.add(dto.getNombre());
		}
		return nombres;
	}

	public int obtenerPlazasCursoOcupadas(int id_curso, String fechaActual) {
		List<InscripcionDTO> plazas_ocupadas = dataBase.executeQueryPojo(InscripcionDTO.class,
				obtener_plazas_curso_ocupadas, id_curso, fechaActual);
		return plazas_ocupadas.size();
	}

	public int ingresosEstimados(int i) {
		List<Map<String, Object>> datos = dataBase.executeQueryMap(sqlGetIngresosEstimados, i);
		if (datos.size() > 0 && datos.get(0).get("ingr") != null) {
			return Double.valueOf("" + datos.get(0).get("ingr")).intValue();
		}
		return 0;
	}

	public int ingresosConfirmados(int id_curso) {
		List<Map<String, Object>> datos = dataBase.executeQueryMap(sqlGetIngresosConfirmados, id_curso);
		if (datos.size() > 0 && datos.get(0).get("ingr") != null) {
			return Double.valueOf("" + datos.get(0).get("ingr")).intValue();
		}
		return 0;
	}

	public int gastosEstimados(int id_curso) {
		List<Map<String, Object>> datos = dataBase.executeQueryMap(sqlGetGastosEstimados, id_curso);
		if (datos.size() > 0 && datos.get(0).get("gast") != null) {
			return Double.valueOf("" + datos.get(0).get("gast")).intValue();
		}
		return 0;
	}

	public int gastosConfirmados(int id_curso) {
		List<Map<String, Object>> datos = dataBase.executeQueryMap(sqlGetGastosConfirmados, Integer.valueOf(id_curso));
		if (datos.size() > 0 && datos.get(0).get("gas") != null) {
			return Double.valueOf("" + datos.get(0).get("gas")).intValue();
		}
		return 0;
	}

	public CursoDTO getCursoDto(String nombreCursoSeleccionado) {
		List<CursoDTO> cDto = getCursos();
		for (CursoDTO dto : cDto) {
			if (dto.getNombre().equals(nombreCursoSeleccionado)) {
				return dto;
			}
		}
		return null;
	}

	public List<Map<String, Object>> getInscritos(int i) {
		return dataBase.executeQueryMap(sqlGetInscritos, i);
	}

	private String getCuotaDeCurso = "select cc.precio as precio , tc.nombre as nombre , tc.id_tipo_cuota as id_tipo_cuota from tipo_cuota tc join cuota_curso cc on cc.id_tipo_cuota = tc.id_tipo_cuota where cc.id_curso =?";

	public List<CuotaDTO> getCuotasCurso(int idCurso) {
		return dataBase.executeQueryPojo(CuotaDTO.class, getCuotaDeCurso, idCurso);

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

	public int maximaCuotaIdCurso(int id_curso) {
		List<Map<String, Object>> datos = dataBase.executeQueryMap(sqlGetMaximaCuota, id_curso);
		if (datos.size() > 0 && datos.get(0).get("precio") != null) {
			return Integer.valueOf("" + datos.get(0).get("precio"));
		}
		return 0;
	}

	public int getSumaCuotasRestantes(CursoDTO cdto, String fechaActual) {
		int plazasOcupadas = obtenerPlazasCursoOcupadas(cdto.getId_curso(), fechaActual);

		return (cdto.getNum_plazas() - plazasOcupadas) * maximaCuotaIdCurso(cdto.getId_curso());
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
