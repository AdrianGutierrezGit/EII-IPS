package giis.demo.tkrun.Models;

import java.util.List;
import java.util.Map;

import giis.demo.tkrun.DTO.CuotaDTO;
import giis.demo.tkrun.DTO.CursoDTO;
import giis.demo.tkrun.DTO.InscripcionDTO;
import giis.demo.util.Database;

public class CancelacionCursoModel {
	private Database dataBase;
	private String sqlGetCursos = "Select * from curso";
	private String sqlGetInscripcion = "SELECT * FROM  inscripcion where id_curso = ?";
	private String sqlUpdateCancelar = "UPDATE curso SET estado_curso = 'cancelado' WHERE id_curso = ? ";
	private String sqlUpdateCancelarIns = "UPDATE inscripcion SET estado = 'CANCELADO' WHERE id_inscripcion = ? ";
	private String sqlInsertDevolucion = "UPDATE inscripcion SET balance_inscripcion = balance_inscripcion + ? WHERE id_inscripcion = ?";
	private String sqlCambiarRemuneracion = "UPDATE actividad_profesor SET remuneracion = CAST(remuneracion * ? AS INTEGER) WHERE id_curso = ?";
	private String sqlGetInscritosFromCurso = "select count(*) num from inscripcion i where i.id_curso = ? "
			+ "and (i.estado = 'PAGADO' or (i.estado = 'PENDIENTE' "
			+ "and cast(JULIANDAY(?) - JULIANDAY(i.fecha_solicitud) as integer) <= 2))";
	private String sqlGetIngresosConfirmados = "select sum(p.cantidad_pagada) as ingr from pago p join inscripcion i on i.id_inscripcion = p.id_inscripcion where i.id_curso = ?";
	private String sqlGetGastosEstimados = "SELECT sum(remuneracion) gast FROM actividad_profesor where id_curso = ?";
	private String sqlGetGastosConfirmados = "SELECT sum(cantidad_transferida) gas FROM pago_profesor p, factura_profesor f,"
			+ " actividad_profesor a where p.id_factura = f.id_factura and f.id_actividad_profesor"
			+ "= a.id_actividad_profesor and a.id_curso = ? ";
	private String cuota_inscripcion = "select precio from cuota_curso where id_cuota_curso =? and id_curso = ? ";
	private String sqlGetMaximaCuota = "SELECT max(precio) precio FROM 'cuota_curso' where id_curso= ?";

	public CancelacionCursoModel() {
		dataBase = new Database();
	}

	public CursoDTO getCursoDTO(String nombreCurso) {
		List<CursoDTO> cDto = getCursos();
		for (CursoDTO dto : cDto) {
			if (dto.getNombre().equals(nombreCurso)) {
				return dto;
			}
		}
		return null;
	}

	public List<CursoDTO> getCursos() {
		return dataBase.executeQueryPojo(CursoDTO.class, sqlGetCursos);
	}

	public List<InscripcionDTO> getInscripcions(int idCurso) {
		return dataBase.executeQueryPojo(InscripcionDTO.class, sqlGetInscripcion, idCurso);
	}

	public void insertarDevolucion(int idInscripcion, double d) {
		dataBase.executeUpdate(sqlInsertDevolucion, d, idInscripcion);
	}

	public void cancelarCurso(CursoDTO curso) {
		dataBase.executeUpdate(sqlUpdateCancelar, curso.getId_curso());
	}

	public void cancelarInscripcion(InscripcionDTO inscripcion) {
		dataBase.executeUpdate(sqlUpdateCancelarIns, inscripcion.getId_inscripcion());
	}

	public int cuantasPlazasOcupadas(int i, String fecha) {
		List<Map<String, Object>> datos = dataBase.executeQueryMap(sqlGetInscritosFromCurso, i, fecha);
		return Integer.valueOf("" + datos.get(0).get("num"));
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

	private String querySumaCuotasInscritos = "select sum(cc.precio) as ingr from cuota_curso cc join inscripcion i on i.id_cuota=cc.id_cuota_curso where i.id_curso =? and (i.estado = 'PAGADO' or (i.estado = 'PENDIENTE'"
			+ " and cast(JULIANDAY(?) - JULIANDAY(i.fecha_solicitud) as integer) <= 2))";

	public int getSumaCuotasInscritos(int id_curso, String fecha) {
		List<Map<String, Object>> datos = dataBase.executeQueryMap(querySumaCuotasInscritos, id_curso, fecha);
		if (datos.size() > 0 && datos.get(0).get("ingr") != null) {
			return Double.valueOf("" + datos.get(0).get("ingr")).intValue();
		}
		return 0;
	}

	public int getPrecioCuota(int id_cuota, int idCurso) {
		List<CuotaDTO> cuota = dataBase.executeQueryPojo(CuotaDTO.class, cuota_inscripcion, id_cuota, idCurso);
		return cuota.get(0).getPrecio();
	}

	public int maximaCuotaIdCurso(int id_curso) {
		List<Map<String, Object>> datos = dataBase.executeQueryMap(sqlGetMaximaCuota, id_curso);
		if (datos.size() > 0 && datos.get(0).get("precio") != null) {
			return Integer.valueOf("" + datos.get(0).get("precio"));
		}
		return 0;
	}

	public int getSumaCuotasRestantes(CursoDTO cdto, String fecha) {
		int plazasOcupadas = cuantasPlazasOcupadas(cdto.getId_curso(), fecha);

		return (cdto.getNum_plazas() - plazasOcupadas) * maximaCuotaIdCurso(cdto.getId_curso());
	}

	public void setRemuneracionesAPorcentaje(int id_curso, int porcentaje) {
		dataBase.executeUpdate(sqlCambiarRemuneracion, (double) porcentaje / 100, id_curso);

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
