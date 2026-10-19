package giis.demo.tkrun.Models;

import java.util.ArrayList;
import java.util.List;

import giis.demo.tkrun.DTO.AlumnoDTO;
import giis.demo.tkrun.DTO.CursoDTO;
import giis.demo.util.Database;

public class RetrasarCursoModel {

	private Database dataBase;

	// Querys ficticias si quieres conectar con la DB
	private static final String SELECT_CURSOS = "SELECT * FROM curso";
	private static final String ACTUALIZAR_FECHAS = "UPDATE curso SET inicio_plazo_inscripcion=?, fin_plazo_inscripcion=?, "
			+ "fecha_inicio=?, fecha_fin=?, retrasado=TRUE WHERE nombre=?";

	private static final String ACTUALIZAR = "UPDATE curso SET fecha_retraso=? WHERE nombre=?";

	private static final String ALUMNO_POR_CURSO = "SELECT p.id, p.nombre, p.apellidos, p.email, p.telefono, p.nif, a.id_alumno "
			+ "FROM alumno a " + "JOIN persona p ON a.id_alumno = p.id "
			+ "JOIN inscripcion i ON a.id_alumno = i.id_alumno " + "WHERE i.id_curso = ? "
			+ "AND i.estado != 'CANCELADO' " + "AND i.cantidad_pagada > 0";
	private List<CursoDTO> cursos;

	public RetrasarCursoModel() {
		dataBase = new Database();
		cursos = new ArrayList<>();
	}

	// Devuelve todos los cursos
	public List<CursoDTO> getCursos() {
		// Si quieres usar DB, descomenta: return
		cursos = dataBase.executeQueryPojo(CursoDTO.class, SELECT_CURSOS);
		return cursos;
	}

	// Devuelve curso por nombre
	public List<CursoDTO> getCursoPorNombre(String nombre) {
		List<CursoDTO> resultado = new ArrayList<>();
		for (CursoDTO c : cursos) {
			if (c.getNombre().equalsIgnoreCase(nombre)) {
				resultado.add(c);
			}
		}
		return resultado;
	}

	// Actualiza las fechas de un curso y marca que ha sido retrasado
	public void actualizarFechasCurso(String nombreCurso, String nuevaInscripcionInicio, String nuevaInscripcionFin,
			String nuevaFormacionInicio, String nuevaFormacionFin) {

		dataBase.executeUpdate(ACTUALIZAR_FECHAS, nuevaInscripcionInicio, nuevaInscripcionFin, nuevaFormacionInicio,
				nuevaFormacionFin, nombreCurso);

		// Refresca la lista local de cursos, si la mantienes en memoria
		cursos = getCursos();
	}

	public List<AlumnoDTO> getAlumnosPorCurso(int id_curso) {

		return dataBase.executeQueryPojo(AlumnoDTO.class, ALUMNO_POR_CURSO, id_curso);
	}

	public void actualizar(CursoDTO cursoActualizado) {
		dataBase.executeUpdate(ACTUALIZAR, cursoActualizado.getFecha_retraso(), cursoActualizado.getNombre());
	}

}
