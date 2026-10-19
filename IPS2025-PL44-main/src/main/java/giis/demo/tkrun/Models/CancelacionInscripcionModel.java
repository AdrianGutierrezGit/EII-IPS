package giis.demo.tkrun.Models;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import giis.demo.tkrun.DTO.CuotaDTO;
import giis.demo.tkrun.DTO.CursoDTO;
import giis.demo.tkrun.DTO.InscripcionDTO;
import giis.demo.util.Database;

public class CancelacionInscripcionModel {
	private Database dataBase;
	private String sqlGetCursos = "Select * from curso";
	private String sqlGetCursosInscAlumno = "SELECT * FROM curso c, inscripcion i where c.id_curso = i.id_curso and i.id_alumno = ? and estado <> 'CANCELADO'";
	private String sqlGetInscripcion = "SELECT * FROM  inscripcion where id_curso = ? and estado != 'CANCELADO' and id_alumno= ? ";
	private String sqlUpdateCancelar = "UPDATE inscripcion SET estado = 'CANCELADO' WHERE id_inscripcion = ? ";
	private String sqlInsertDevolucion = "UPDATE inscripcion SET balance_inscripcion = balance_inscripcion + ? WHERE id_inscripcion = ?";

	private String cuota_inscripcion = "select precio from cuota_curso where id_cuota_curso =? and id_curso = ? ";
	private String sqlGetDineroPagado = "SELECT sum(cantidad_pagada) gas FROM pago p " + " where p.id_inscripcion = ? ";
	private String primera_lista_de_espera = "select * from inscripcion where id_curso = ? and estado = 'LISTA_DE_ESPERA' order by"
			+ " fecha_solicitud asc, id_inscripcion asc";
	private String inscribir_alumno_de_lista_de_espera = "UPDATE inscripcion SET " + "estado = 'PENDIENTE', "
			+ "fecha_solicitud = ?, " + "balance_inscripcion = balance_inscripcion + ? " + "WHERE id_inscripcion = ?";

	public CancelacionInscripcionModel() {
		dataBase = new Database();
	}

	public List<String> getCursosInscritoAlumno(int id_alumno) {
		List<Map<String, Object>> cursos = dataBase.executeQueryMap(sqlGetCursosInscAlumno, id_alumno);
		List<String> list = new ArrayList<String>();
		for (Map<String, Object> map : cursos) {
			list.add("" + map.get("nombre"));
		}
		return list;
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

	private List<CursoDTO> getCursos() {
		return dataBase.executeQueryPojo(CursoDTO.class, sqlGetCursos);
	}

	public InscripcionDTO getInscripcion(int idCurso, int id_alumno) {
		return dataBase.executeQueryPojo(InscripcionDTO.class, sqlGetInscripcion, idCurso, id_alumno).get(0);
	}

	public void cancelarInscripcion(InscripcionDTO inscripcion) {
		dataBase.executeUpdate(sqlUpdateCancelar, inscripcion.getId_inscripcion());
	}

	public void insertarDevolucion(int idInscripcion, int cantidadDevolucion) {
		dataBase.executeUpdate(sqlInsertDevolucion, cantidadDevolucion, idInscripcion);
	}

	public int getPrecioCuota(int id_cuota, int idCurso) {
		List<CuotaDTO> cuota = dataBase.executeQueryPojo(CuotaDTO.class, cuota_inscripcion, id_cuota, idCurso);
		return cuota.get(0).getPrecio();
	}

	public int getDineroPagado(int id_inscripcion) {
		List<Map<String, Object>> datos = dataBase.executeQueryMap(sqlGetDineroPagado, Integer.valueOf(id_inscripcion));
		if (datos.size() > 0 && datos.get(0).get("gas") != null) {
			return Integer.valueOf("" + datos.get(0).get("gas"));
		}
		return 0;
	}

	public InscripcionDTO getPrimeroEnListaDeEsperta(int id_curso) {
		List<InscripcionDTO> listaDeEspera = dataBase.executeQueryPojo(InscripcionDTO.class, primera_lista_de_espera,
				id_curso);
		if (listaDeEspera.isEmpty()) {
			return null;
		}
		return listaDeEspera.get(0);
	}

	public void inscribirAlumno(String fechaHoy, int id_inscripcion, int precio_cuota) {
		dataBase.executeUpdate(inscribir_alumno_de_lista_de_espera, fechaHoy, precio_cuota, id_inscripcion);

	}

}
