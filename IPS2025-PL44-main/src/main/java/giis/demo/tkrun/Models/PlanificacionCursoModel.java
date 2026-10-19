package giis.demo.tkrun.Models;

import java.util.List;

import giis.demo.tkrun.DTO.CuotaDTO;
import giis.demo.tkrun.DTO.CursoDTO;
import giis.demo.tkrun.DTO.ProfesorDTO;
import giis.demo.util.Database;

public class PlanificacionCursoModel {

	private Database dataBase;
	private String sqlGetProfesores = "Select * from persona p inner join profesor pr where p.id = pr.id_profesor";
	private String sqlGetCursos = "Select * from curso";

	private String sqlIntroducirCurso = "INSERT INTO curso (nombre, inicio_plazo_inscripcion, "
			+ "fin_plazo_inscripcion, fecha_inicio, fecha_fin, lugar, num_plazas, "
			+ "estado_curso, objetivos, contenidos, id_empresa, coste_empresa) " + "VALUES (?,?,?,?,?,?,?,?,?,?,?,?);";
	private String sqlAsignarProfesor = "INSERT INTO actividad_profesor (id_curso, profesor_id, remuneracion)"

			+ "VALUES(?,?,?)";

	private String sqlMaxID = "SELECT MAX(id_curso) AS id_maximo FROM curso";

	public PlanificacionCursoModel() {
		dataBase = new Database();
	}

	/**
	 * Crea un nuevo tipo de cuota en la base de datos y devuelve su ID. Calcula el
	 * ID manualmente ya que la tabla tipo_cuota no es autoincremental.
	 */
	public int insertarNuevoTipoCuota(String nombre) {
		// 1. Obtener el último ID existente
		String sqlMaxId = "SELECT MAX(id_tipo_cuota) FROM tipo_cuota";
		List<Object[]> res = dataBase.executeQueryArray(sqlMaxId);

		int newId = 1; // ID por defecto si la tabla está vacía

		// Verificamos si nos devuelve algo y no es null
		if (res != null && !res.isEmpty() && res.get(0)[0] != null) {
			newId = (int) res.get(0)[0] + 1;
		}

		// 2. Insertar el nuevo registro con el ID calculado
		String sqlInsert = "INSERT INTO tipo_cuota (id_tipo_cuota, nombre) VALUES (?, ?)";
		dataBase.executeUpdate(sqlInsert, newId, nombre);

		return newId;
	}

	public List<ProfesorDTO> getProfesores() {
		return dataBase.executeQueryPojo(ProfesorDTO.class, sqlGetProfesores);
	}

	public List<CursoDTO> getCursos() {
		return dataBase.executeQueryPojo(CursoDTO.class, sqlGetCursos);
	}

	public int maxId() {
		return (int) dataBase.executeQueryArray(sqlMaxID).get(0)[0];
	}

	public void introducirCurso(CursoDTO curso) {
		dataBase.executeUpdate(sqlIntroducirCurso, curso.getNombre(), curso.getInicio_plazo_inscripcion(),
				curso.getFin_plazo_inscripcion(), curso.getFecha_inicio(), curso.getFecha_fin(), curso.getLugar(),
				curso.getNum_plazas(), curso.getEstado_curso(), curso.getObjetivos(), curso.getContenidos(),
				// NUEVOS PARÁMETROS:
				curso.getId_empresa(), // Puede ser null, JDBC lo maneja bien
				curso.getCoste_empresa() // int (0 si no es empresa)
		);
	}

	public void asignarProfesor(CursoDTO curso, ProfesorDTO prof, int remuneracion) {
		dataBase.executeUpdate(sqlAsignarProfesor, curso.getId_curso(), prof.getId(), remuneracion);
	}

	private String querySacarCuotas = "SELECT  id_tipo_cuota, nombre FROM tipo_cuota ORDER BY nombre";

	public List<CuotaDTO> cargarCuotas() {
		return dataBase.executeQueryPojo(CuotaDTO.class, querySacarCuotas);
	}

	private String queryIntroducirCuotasCurso = "Insert into cuota_curso (id_curso,id_tipo_cuota,precio) VALUES (?,?,?)";

	public void insertarCuotaCurso(int id_curso, int idTipoCuota, int precio) {
		dataBase.executeUpdate(queryIntroducirCuotasCurso, id_curso, idTipoCuota, precio);
	}

	private String sqlGetEmpresaId = "SELECT id_empresa FROM empresa WHERE cif = ?";
	private String sqlInsertEmpresa = "INSERT INTO empresa (cif, nombre) VALUES (?, ?)";

	/**
	 * Busca una empresa por CIF.
	 * 
	 * @return el ID de la empresa o -1 si no existe.
	 */
	public int getEmpresaId(String cif) {
		List<Object[]> res = dataBase.executeQueryArray(sqlGetEmpresaId, cif);
		if (res != null && !res.isEmpty()) {
			return (int) res.get(0)[0];
		}
		return -1;
	}

	/**
	 * Inserta una empresa nueva y devuelve su ID. Nota: En un entorno real
	 * idealmente usaríamos getGeneratedKeys, pero siguiendo tu patrón de 'maxId'
	 * manual:
	 */
	public int crearEmpresa(String cif, String nombre) {

		dataBase.executeUpdate(sqlInsertEmpresa, cif, nombre);

		return getEmpresaId(cif);
	}

}
