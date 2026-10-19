package giis.demo.tkrun.Models;

import java.util.List;

import giis.demo.tkrun.DTO.PersonaDTO;
import giis.demo.util.Database;

public class RegistroProfesorModel {

	private Database dataBase;

	// Comprobar persona por datos
	private static final String SQL_CHECK_PERSONA_DATOS = "SELECT COUNT(*) FROM persona WHERE nombre = ? AND apellidos = ? AND email = ? AND telefono = ?";

	// Obtener id real de persona a partir de email
	private static final String SQL_GET_PERSONA_ID = "SELECT id FROM persona WHERE email = ?";

	// Comprobar profesor por id_persona
	private static final String SQL_CHECK_PROFE = "SELECT COUNT(*) FROM profesor WHERE id_profesor = ?";

	private static final String SQL_INSERT_PERSONA = "INSERT INTO persona (id, nombre, apellidos, email, telefono) VALUES (?, ?, ?, ?, ?)";

	private static final String SQL_INSERT_PROFE = "INSERT INTO profesor (id_profesor) VALUES (?)";

	private static final String SQL_MAX_ID = "SELECT MAX(id) FROM persona";
	private static final String SQL_GETID_BY_EMAIL = "SELECT id FROM persona WHERE email = ?";

	public RegistroProfesorModel() {
		dataBase = new Database();
	}

	/**
	 * Generar ID manual (persona no es AUTOINCREMENT)
	 */
	public int generarNuevoIdPersona() {
		List<Object[]> res = dataBase.executeQueryArray(SQL_MAX_ID);

		if (res.isEmpty() || res.get(0)[0] == null)
			return 1;

		return ((Number) res.get(0)[0]).intValue() + 1;
	}

	/**
	 * Comprobar si una persona existe por sus DATOS (no por id)
	 */
	public boolean existePersona(PersonaDTO pr) {
		List<Object[]> res = dataBase.executeQueryArray(SQL_CHECK_PERSONA_DATOS, pr.getNombre(), pr.getApellidos(),
				pr.getEmail(), pr.getTelefono());

		return !res.isEmpty() && ((Number) res.get(0)[0]).intValue() > 0;
	}

	/**
	 * Insertar nueva persona
	 */
	public void insertarPersona(PersonaDTO pr) {
		dataBase.executeUpdate(SQL_INSERT_PERSONA, pr.getId(), pr.getNombre(), pr.getApellidos(), pr.getEmail(),
				pr.getTelefono());
	}

	/**
	 * Comprobar si ya es profesor (por sus DATOS → se obtiene el id)
	 */
	public boolean existeProfesor(PersonaDTO pr) {

		// 1. Obtener id real de la persona por su email
		List<Object[]> resPersona = dataBase.executeQueryArray(SQL_GET_PERSONA_ID, pr.getEmail());

		if (resPersona.isEmpty())
			return false; // no existe persona → imposible que sea profesor

		int idPersona = ((Number) resPersona.get(0)[0]).intValue();

		// 2. Comprobar si está en la tabla profesor
		List<Object[]> resProfe = dataBase.executeQueryArray(SQL_CHECK_PROFE, idPersona);

		return !resProfe.isEmpty() && ((Number) resProfe.get(0)[0]).intValue() > 0;
	}

	/**
	 * Insertar profesor
	 */
	public void insertarProfesor(int idPersona) {
		dataBase.executeUpdate(SQL_INSERT_PROFE, idPersona);
	}

	public int obtenerIdPersonaPorEmail(String email) {
		List<Object[]> res = dataBase.executeQueryArray(SQL_GETID_BY_EMAIL, email);

		if (res.isEmpty() || res.get(0)[0] == null)
			return -1;

		return ((Number) res.get(0)[0]).intValue();
	}
}
