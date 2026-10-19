package giis.demo.tkrun.Models;

import java.util.List;
import java.util.Map;

import giis.demo.tkrun.DTO.AlumnoDTO;
import giis.demo.tkrun.DTO.CuotaDTO;
import giis.demo.tkrun.DTO.CursoDTO;
import giis.demo.tkrun.DTO.InscripcionDTO;
import giis.demo.util.Database;

public class InscripcionAlumnoEnCursoModel {

	private Database dataBase;

	private static final String SQL_GET_CURSOS = "SELECT * FROM curso where curso.estado_curso <> 'cancelado' and curso.estado_curso <> 'curso_cerrado'";
	private static final String SQL_GET_CURSO_POR_NOMBRE = "SELECT * FROM curso where nombre = ?";
	private static final String SQL_GET_ALUMNOS = "SELECT a.id_alumno, p.nombre, p.apellidos, p.email, p.telefono "
			+ "FROM alumno a JOIN persona p ON a.id_alumno = p.id";

	private static final String SQL_INSERT_PERSONA = "INSERT INTO persona (id, nombre, apellidos, email, telefono) VALUES (?, ?, ?, ?, ?)";
	private static final String SQL_INSERT_ALUMNO = "INSERT INTO alumno (id_alumno) VALUES (?)";
	private static final String SQL_INSERT_INSCRIPCION = "INSERT INTO inscripcion (fecha_solicitud, id_curso, id_alumno, estado, cantidad_pagada, balance_inscripcion,id_cuota) VALUES (?,?,?,?,?,?,?)";

	private static final String SQL_CHECK_PERSONA = "SELECT COUNT(*) FROM persona WHERE id = ?";
	private static final String SQL_CHECK_CORREO = "SELECT COUNT(*) FROM persona WHERE email = ?";
	private static final String SQL_CHECK_ALUMNO = "SELECT COUNT(*) FROM alumno WHERE id_alumno = ?";
	private static final String SQL_CHECK_INSCRIPCION = "SELECT COUNT(*) FROM inscripcion WHERE id_inscripcion = ?";
	private static final String SQL_GET_CURSO = "SELECT * FROM curso WHERE id_curso = ?";
	private static final String SQL_COUNT_INSCRIPCIONES_CURSO = "SELECT COUNT(*) FROM inscripcion WHERE id_curso = ?"
			+ " AND estado = 'PAGADO'";

	private static final String SQL_CHECKID_ALUMNO = "SELECT COUNT(*) FROM inscripcion WHERE id_inscripcion = ?";

	private static final String SQL_CHECKALUMNO_CURSO = "SELECT COUNT(*) " + "FROM persona p "
			+ "JOIN alumno a ON a.id_alumno = p.id " + "JOIN inscripcion i ON i.id_alumno = a.id_alumno "
			+ "JOIN curso c ON c.id_curso = i.id_curso " + "WHERE p.id = ? AND c.id_curso = ?";
	private static final String SQL_COUNT_INSCRIPCIONES_PAGADAS_Y_PENDIENTES_RECENTES = "SELECT COUNT(*) FROM inscripcion WHERE id_curso = ? AND (estado = 'PAGADO' OR (estado = 'PENDIENTE' AND julianday(?) - julianday(fecha_solicitud) <= 2))";

	private static final String SQL_GET_INSCRIPCION_POR_ALUMNO = "SELECT id_inscripcion, fecha_solicitud, id_curso, id_alumno, id_cuota, estado, cantidad_pagada, balance_inscripcion "
			+ "FROM inscripcion " + "WHERE id_alumno = ? AND id_curso = ?";

	private static final String SQL_UPDATE_INSCRIPCION = "UPDATE inscripcion "
			+ "SET fecha_solicitud = ?, estado = ?, cantidad_pagada = ?, balance_inscripcion = ?, id_cuota = ? "
			+ "WHERE id_alumno = ? AND id_curso = ?";

	private static final String SQL_CUOTASDISPONIBLES = "SELECT tc.id_tipo_cuota as id_tipo_cuota , tc.nombre as nombre, cc.precio as precio, cc.id_cuota_curso as id_cuota_curso FROM cuota_curso cc JOIN tipo_cuota tc ON tc.id_tipo_cuota = cc.id_tipo_cuota WHERE cc.id_curso = ? ORDER BY tc.nombre";
	private static final String OBTENER_INSCRIPCIONES_EN_ESPERA_EN_CURSO = "select * from inscripcion where id_curso = ? and estado = 'LISTA_DE_ESPERA'";
	private static final String OBTENER_CUOTA_INSCRIPCION = "select * from cuota_curso c where c.id_cuota_curso = ?";

	public InscripcionAlumnoEnCursoModel() {
		dataBase = new Database();
	}

	public List<CursoDTO> getCursos() {
		return dataBase.executeQueryPojo(CursoDTO.class, SQL_GET_CURSOS);
	}

	public List<AlumnoDTO> getAlumnos() {
		return dataBase.executeQueryPojo(AlumnoDTO.class, SQL_GET_ALUMNOS);
	}

	public boolean existePersona(int id) {
		List<Object[]> res = dataBase.executeQueryArray(SQL_CHECK_PERSONA, id);
		if (res.isEmpty())
			return false;
		Object count = res.get(0)[0];
		return count != null && ((Number) count).intValue() > 0;
	}

	public boolean estaInscritoEnCurso(int idPersona, int idCurso) {
		List<Object[]> res = dataBase.executeQueryArray(SQL_CHECKALUMNO_CURSO, idPersona, idCurso);
		if (res.isEmpty())
			return false;
		Object count = res.get(0)[0];
		return count != null && ((Number) count).intValue() > 0;
	}

	public boolean existeAlumno(int id) {
		List<Object[]> res = dataBase.executeQueryArray(SQL_CHECK_ALUMNO, id);
		if (res.isEmpty())
			return false;
		Object count = res.get(0)[0];
		return count != null && ((Number) count).intValue() > 0;
	}

	public boolean existeInscripcion(int idInscripcion) {
		List<Object[]> res = dataBase.executeQueryArray(SQL_CHECK_INSCRIPCION, idInscripcion);
		if (res.isEmpty())
			return false;
		Object count = res.get(0)[0];
		return count != null && ((Number) count).intValue() > 0;
	}

	public void insertarPersona(AlumnoDTO al) {
		dataBase.executeUpdate(SQL_INSERT_PERSONA, al.getId(), al.getNombre(), al.getApellidos(), al.getEmail(),
				al.getTelefono());
	}

	public void insertarAlumno(int idAlumno) {
		dataBase.executeUpdate(SQL_INSERT_ALUMNO, idAlumno);
	}

	public void insertarInscripcion(InscripcionDTO inscripcion, int idAlumno) {
		dataBase.executeUpdate(SQL_INSERT_INSCRIPCION, inscripcion.getFecha_solicitud(), inscripcion.getId_curso(),
				idAlumno, inscripcion.getEstado(), inscripcion.getCantidad_pagada(),
				inscripcion.getBalance_inscripcion(), inscripcion.getId_cuota());
	}

	public CursoDTO getCursoPorId(int idCurso) {
		List<CursoDTO> cursos = dataBase.executeQueryPojo(CursoDTO.class, SQL_GET_CURSO, idCurso);
		return cursos.isEmpty() ? null : cursos.get(0);
	}

	public int contarInscripcionesCurso(int i) {
		List<Object[]> res = dataBase.executeQueryArray(SQL_COUNT_INSCRIPCIONES_CURSO, i);
		if (res.isEmpty())
			return 0;
		Object count = res.get(0)[0];
		return count != null ? ((Number) count).intValue() : 0;
	}

	public boolean existeIDPago(int idPago) {
		List<Object[]> res = dataBase.executeQueryArray(SQL_CHECKID_ALUMNO, idPago);
		if (res.isEmpty())
			return false;
		Object count = res.get(0)[0];
		return count != null && ((Number) count).intValue() > 0;
	}

	public boolean existeCorreo(String email) {
		List<Object[]> res = dataBase.executeQueryArray(SQL_CHECK_CORREO, email);
		if (res.isEmpty())
			return false;
		Object count = res.get(0)[0];
		return count != null && ((Number) count).intValue() > 0;
	}

	public CursoDTO getCursoPorNombre(String nombre) {
		List<CursoDTO> cursos = dataBase.executeQueryPojo(CursoDTO.class, SQL_GET_CURSO_POR_NOMBRE, nombre);
		return cursos.isEmpty() ? null : cursos.get(0);
	}

	// Añadir este método en la clase RegistroAlumnoModel
	public int contarInscripcionesConsideradas(int idCurso, String hoy) {
		// 'hoy' debe ser una cadena en formato 'YYYY-MM-DD' (Date.toString())
		List<Object[]> res = dataBase.executeQueryArray(SQL_COUNT_INSCRIPCIONES_PAGADAS_Y_PENDIENTES_RECENTES, idCurso,
				hoy);
		if (res.isEmpty())
			return 0;
		Object count = res.get(0)[0];
		return count != null ? ((Number) count).intValue() : 0;
	}

	public List<CuotaDTO> obtenerCuotasDisponiblesParaCurso(int id_curso) {
		return dataBase.executeQueryPojo(CuotaDTO.class, SQL_CUOTASDISPONIBLES, id_curso);
	}

	// Devuelve la inscripción de un alumno en un curso, o null si no existe
	public InscripcionDTO getInscripcionPorAlumnoYCursos(int id_alumno, int id_curso) {

		List<InscripcionDTO> inscripciones = dataBase.executeQueryPojo(InscripcionDTO.class,
				SQL_GET_INSCRIPCION_POR_ALUMNO, id_alumno, id_curso);
		return inscripciones.isEmpty() ? null : inscripciones.get(0);
	}

	// Actualiza la inscripción existente con los datos nuevos
	public void actualizarInscripcion(InscripcionDTO inscExistente, int id_alumno) {

		dataBase.executeUpdate(SQL_UPDATE_INSCRIPCION, inscExistente.getFecha_solicitud(), inscExistente.getEstado(),
				inscExistente.getCantidad_pagada(), inscExistente.getBalance_inscripcion(), inscExistente.getId_cuota(),
				id_alumno, inscExistente.getId_curso());
	}

	private String isCursoGratis = "select sum(precio) as precio from cuota_curso cc where cc.id_curso = ?";

	public boolean isCursoGratis(int id_curso) {
		List<Map<String, Object>> datos = dataBase.executeQueryMap(isCursoGratis, id_curso);
		if (datos.size() > 0 && datos.get(0).get("precio") != null) {
			return Integer.valueOf("" + datos.get(0).get("precio")) == 0;
		}
		return true;
	}

	public int obtenerInscripcionesEnEspera(int id_curso) {
		List<InscripcionDTO> inscripciones = dataBase.executeQueryPojo(InscripcionDTO.class,
				OBTENER_INSCRIPCIONES_EN_ESPERA_EN_CURSO, id_curso);
		return inscripciones.size();
	}

	public int getCuotaInscripcion(int id_cuota) {
		List<CuotaDTO> cuotaInscripcion = dataBase.executeQueryPojo(CuotaDTO.class, OBTENER_CUOTA_INSCRIPCION,
				id_cuota);
		return cuotaInscripcion.get(0).getPrecio();
	}

}
