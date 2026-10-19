package giis.demo.tkrun;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

import giis.demo.tkrun.DTO.AlumnoDTO;
import giis.demo.tkrun.DTO.CuotaDTO;
import giis.demo.tkrun.DTO.CursoDTO;
import giis.demo.tkrun.DTO.InscripcionDTO;
import giis.demo.tkrun.DTO.PersonaDTO;
import giis.demo.tkrun.DTO.ProfesorDTO;
import giis.demo.util.Database;

/**
 * Acceso a los datos de carreras e inscripciones, utilizado como modelo para el
 * ejemplo de swing y para las pruebas unitarias y de interfaz de usuario.
 * 
 * <br/>
 * En los metodos de este ejemplo toda la logica de negocio se realiza mediante
 * una unica query sql por lo que siempre se utilizan los metodos de utilidad en
 * la clase Database que usan apache commons-dbutils y controlan la conexion. En
 * caso de que en un mismo metodo se realicen diferentes queries se deberia
 * controlar la conexion desde esta clase (ver como ejemplo la implementacion en
 * Database).
 * 
 * <br/>
 * Si utilizase algún otro framework para manejar la persistencia, la
 * funcionalidad proporcionada por esta clase sería la asignada a los Servicios,
 * Repositorios y DAOs.
 */
public class AplicacionModel {

	private static final String SQL_GET_ALUMNOS = "SELECT a.id_alumno, p.nombre, p.apellidos, p.email, p.telefono "
			+ "FROM alumno a JOIN persona p ON a.id_alumno = p.id";

	private Database db = new Database();

	// Personas por rol
	private static final String SQL_PERSONAS_ALUMNO = "SELECT p.id AS id, p.nombre AS nombre, p.apellidos AS apellidos, p.email AS email, p.telefono AS telefono, 'alumno' AS rol "
			+ "FROM persona p JOIN alumno a ON p.id = a.id_alumno";

	private static final String SQL_PERSONAS_PROFESOR = "SELECT p.id AS id, p.nombre AS nombre, p.apellidos AS apellidos, p.email AS email, p.telefono AS telefono, 'profesor' AS rol "
			+ "FROM persona p JOIN profesor pr ON p.id = pr.id_profesor";

	private static final String SQL_PERSONAS_RESPONSABLE = "SELECT p.id AS id, p.nombre AS nombre, p.apellidos AS apellidos, p.email AS email, p.telefono AS telefono, 'responsable de formacion' AS rol "
			+ "FROM persona p JOIN responsable_formacion rf ON p.id = rf.id_responsable_formacion";
	private static final String SQL_PERSONAS_SECRETARIO = "SELECT p.id AS id, p.nombre AS nombre, p.apellidos AS apellidos, p.email AS email, p.telefono AS telefono, 'secretario administrativo' AS rol "
			+ "FROM persona p JOIN secretario_administrativo sa ON p.id = sa.id_secretario_administrativo";
	private static final String SQL_GET_CURSOS = "SELECT * FROM curso";
	private static final String SQL_UPDATE_STATE = "UPDATE curso SET estado_curso = ? WHERE id_curso = ? ";

	private static final String SQL_GET_PENDIENTES_CURSO = "SELECT * FROM inscripcion WHERE id_curso = ? AND estado = 'PENDIENTE'";
	private static final String SQL_GET_LISTA_ESPERA_CURSO = "SELECT * FROM inscripcion WHERE id_curso = ? AND estado = 'LISTA_DE_ESPERA' ORDER BY fecha_solicitud ASC, id_inscripcion ASC";
	private static final String SQL_MOVER_A_PENDIENTE = "UPDATE inscripcion SET " + "estado = 'PENDIENTE', "
			+ "fecha_solicitud = ?, " + "cantidad_a_devolver = cantidad_a_devolver + ? " + "WHERE id_inscripcion = ?";
	private static final String SQL_OBTENER_CUOTA_INSCRIPCION = "select * from cuota_curso where id_cuota_curso = ?";
	private static final String SQL_COUNT_PAGADOS = "SELECT * FROM inscripcion WHERE id_curso = ? AND estado = 'PAGADO'";

	public List<PersonaDTO> getPersonasConRol() {
		List<PersonaDTO> personas = new ArrayList<PersonaDTO>();
		personas.addAll(db.executeQueryPojo(AlumnoDTO.class, SQL_PERSONAS_ALUMNO));
		personas.addAll(db.executeQueryPojo(ProfesorDTO.class, SQL_PERSONAS_PROFESOR));
		personas.addAll(db.executeQueryPojo(PersonaDTO.class, SQL_PERSONAS_RESPONSABLE));
		personas.addAll(db.executeQueryPojo(PersonaDTO.class, SQL_PERSONAS_SECRETARIO));
		return personas;
	}

	public void actualizarEstadosCursos(String fechaS) {
		LocalDate fecha = LocalDate.parse(fechaS);
		for (CursoDTO cursoDTO : db.executeQueryPojo(CursoDTO.class, SQL_GET_CURSOS)) {
			if (cursoDTO.getEstado_curso().equals("cancelado")) {
				continue;
			}
			if (cursoDTO.getEstado_curso().equals("curso_cerrado")) {
				continue;
			}
			if (fecha.isBefore(cursoDTO.getIncioInscripcion().toLocalDate())) {
				db.executeUpdate(SQL_UPDATE_STATE, "planificado", cursoDTO.getId_curso());
			} else if (fecha.isBefore(cursoDTO.getFinInscripcion().toLocalDate())) {
				db.executeUpdate(SQL_UPDATE_STATE, "periodo_inscripcion", cursoDTO.getId_curso());
			} else if (fecha.isBefore(cursoDTO.getFechaInicioCurso().toLocalDate())) {
				db.executeUpdate(SQL_UPDATE_STATE, "inscripcion_cerrada", cursoDTO.getId_curso());
			} else if (fecha.isBefore(cursoDTO.getFechaFinCurso().toLocalDate())) {
				db.executeUpdate(SQL_UPDATE_STATE, "en_celebracion", cursoDTO.getId_curso());
			} else {
				db.executeUpdate(SQL_UPDATE_STATE, "celebrado", cursoDTO.getId_curso());
			}

		}
	}

	public List<AlumnoDTO> getAlumnos() {
		return db.executeQueryPojo(AlumnoDTO.class, SQL_GET_ALUMNOS);

	}

	public void actualizaInscripcionesCursos(String hoy) {
		LocalDate fechaHoy = LocalDate.parse(hoy);

		List<CursoDTO> cursos = db.executeQueryPojo(CursoDTO.class, SQL_GET_CURSOS);

		for (CursoDTO curso : cursos) {
			int numPagados = db.executeQueryPojo(InscripcionDTO.class, SQL_COUNT_PAGADOS, curso.getId_curso()).size();

			List<InscripcionDTO> pendientes = db.executeQueryPojo(InscripcionDTO.class, SQL_GET_PENDIENTES_CURSO,
					curso.getId_curso());
			int numPendientesValidos = 0;

			for (InscripcionDTO insc : pendientes) {
				LocalDate fechaSolicitud = LocalDate.parse(insc.getFecha_solicitud());
				long diasDiferencia = ChronoUnit.DAYS.between(fechaSolicitud, fechaHoy);

				if (diasDiferencia <= 2) {
					numPendientesValidos++;
				}
			}

			int plazasOcupadas = numPagados + numPendientesValidos;
			int plazasLibres = curso.getNum_plazas() - plazasOcupadas;

			if (plazasLibres > 0) {
				List<InscripcionDTO> listaEspera = db.executeQueryPojo(InscripcionDTO.class, SQL_GET_LISTA_ESPERA_CURSO,
						curso.getId_curso());

				int genteAMover = Math.min(plazasLibres, listaEspera.size());

				for (int i = 0; i < genteAMover; i++) {
					InscripcionDTO afortunado = listaEspera.get(i);

					int precioCuota = 0;
					List<CuotaDTO> cuotas = db.executeQueryPojo(CuotaDTO.class, SQL_OBTENER_CUOTA_INSCRIPCION,
							afortunado.getId_cuota());
					if (!cuotas.isEmpty()) {
						precioCuota = cuotas.get(0).getPrecio();
					}
					db.executeUpdate(SQL_MOVER_A_PENDIENTE, hoy, -precioCuota, afortunado.getId_inscripcion());
				}
			}
		}

	}
}