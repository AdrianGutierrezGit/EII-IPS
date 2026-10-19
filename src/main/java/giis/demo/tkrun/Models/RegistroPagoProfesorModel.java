package giis.demo.tkrun.Models;

import java.util.List;

import giis.demo.tkrun.DTO.CursoDTO;
import giis.demo.tkrun.DTO.FacturaDTO;
import giis.demo.tkrun.DTO.ProfesorDTO;
import giis.demo.util.Database;

public class RegistroPagoProfesorModel {

	private Database dataBase;

	private final static String CURSOS = "SELECT * FROM curso ";

	private final static String sqlRemuneracion = "SELECT remuneracion FROM actividad_profesor WHERE profesor_id = ? AND id_curso = ?";

	private final static String INSERT_PAGO_PROFESOR = "INSERT INTO pago_profesor (id_pago_profesor, fecha_transferencia, cantidad_transferida, id_factura) VALUES (?,?,?,?)";

	private final static String NEXT_ID_PAGO_PROFESOR = "SELECT COALESCE(MAX(id_pago_profesor),0)+1 FROM pago_profesor";

	private final static String INSERT_FACTURA = "INSERT INTO factura_profesor (id_factura, fecha_emision, cantidad_facturada, nombre_emisor, direccion_emisor, estado_factura, id_actividad_profesor) VALUES (?, ?, ?, ?, ?, ?, ?)";

	private final static String SQL_FACTURA_GLOBAL = "SELECT COUNT(*) FROM factura_profesor WHERE id_factura = ?";

	private static final String SQL_GET_ACTIVIDAD_PROFESOR = "SELECT id_actividad_profesor FROM actividad_profesor WHERE id_curso = ? AND profesor_id = ?";

	private static final String SQL_GET_PROFESORES_PORCURSO = "SELECT p.id_profesor AS id, per.nombre AS nombre, per.apellidos AS apellidos, per.email AS email, per.telefono AS telefono "
			+ "FROM actividad_profesor ap " + "JOIN profesor p ON ap.profesor_id = p.id_profesor "
			+ "JOIN persona per ON p.id_profesor = per.id " + "WHERE ap.id_curso = ?";

	private final static String sqlSetEstado = "update factura_profesor set estado_factura = ? where id_actividad_profesor in (select id_actividad_profesor from actividad_profesor where profesor_id = ? and id_curso= ?)";
	private final static String SQL_CANTIDAD_PAGADA = "SELECT SUM(pp.cantidad_transferida) " + "FROM pago_profesor pp "
			+ "JOIN factura_profesor fp ON pp.id_factura = fp.id_factura "
			+ "JOIN actividad_profesor ap ON fp.id_actividad_profesor = ap.id_actividad_profesor "
			+ "WHERE ap.profesor_id = ? AND ap.id_curso = ?";

	// Devuelve >0 si la factura existe pero NO pertenece al profe/curso actual
	// (conflicto)
	private static final String SQL_FACTURA_CONFLICTO = "SELECT COUNT(*) " + "FROM factura_profesor fp "
			+ "JOIN actividad_profesor ap ON fp.id_actividad_profesor = ap.id_actividad_profesor "
			+ "WHERE fp.id_factura = ? AND (ap.profesor_id != ? OR ap.id_curso != ?)";

	public RegistroPagoProfesorModel() {
		dataBase = new Database();
	}

	public List<CursoDTO> getCursos() {
		return dataBase.executeQueryPojo(CursoDTO.class, CURSOS);
	}

	// Existe factura en general
	public boolean existeFacturaGlobal(int idFactura) {
		List<Object[]> res = dataBase.executeQueryArray(SQL_FACTURA_GLOBAL, idFactura);
		if (res.isEmpty())
			return false;
		return ((Number) res.get(0)[0]).intValue() > 0;
	}

	// Existe factura pero en OTRO profe/curso
	public boolean existeFacturaEnOtroSitio(int idFactura, int idProfesor, int idCurso) {
		List<Object[]> res = dataBase.executeQueryArray(SQL_FACTURA_CONFLICTO, idFactura, idProfesor, idCurso);
		if (res.isEmpty())
			return false;
		return ((Number) res.get(0)[0]).intValue() > 0;
	}

	public void guardarFactura(FacturaDTO factura) {
		dataBase.executeUpdate(INSERT_FACTURA, factura.getId_Factura(), factura.getFecha_Emision(),
				factura.getCantidad_Facturada(), factura.getNombre_Emisor(), factura.getDireccion_Emisor(), "PENDIENTE",
				factura.getId_actividad_profesor());
	}

	public void guardarPagoProfesor(String fechaTransferencia, int cantidadTransferida, int idFactura) {
		int nextId = getNextPagoProfesorId();
		dataBase.executeUpdate(INSERT_PAGO_PROFESOR, nextId, fechaTransferencia, cantidadTransferida, idFactura);
	}

	private int getNextPagoProfesorId() {
		List<Object[]> res = dataBase.executeQueryArray(NEXT_ID_PAGO_PROFESOR);
		return ((Number) res.get(0)[0]).intValue();
	}

	public Integer getActividadProfesor(int idCurso, int idProfesor) {
		List<Object[]> res = dataBase.executeQueryArray(SQL_GET_ACTIVIDAD_PROFESOR, idCurso, idProfesor);
		if (res.isEmpty())
			return null;
		return ((Number) res.get(0)[0]).intValue();
	}

	public List<ProfesorDTO> getProfesor(int id_curso) {
		return dataBase.executeQueryPojo(ProfesorDTO.class, SQL_GET_PROFESORES_PORCURSO, id_curso);
	}

	public String estadoPagoProfesor(int idProfesor, int idCurso) {

		List<Object[]> res = dataBase.executeQueryArray(sqlRemuneracion, idProfesor, idCurso);
		if (res.isEmpty())
			return "Pendiente (sin actividad)";

		int remuneracion = ((Number) res.get(0)[0]).intValue();

		List<Object[]> pagos = dataBase.executeQueryArray(SQL_CANTIDAD_PAGADA, idProfesor, idCurso);
		int cantidadPagada = (pagos.isEmpty() || pagos.get(0)[0] == null) ? 0 : ((Number) pagos.get(0)[0]).intValue();

		if (cantidadPagada == remuneracion)
			return "Pagada";
		if (cantidadPagada < remuneracion)
			return "Pendiente (faltan " + (remuneracion - cantidadPagada) + " €)";
		return "Pendiente (exceso " + (cantidadPagada - remuneracion) + " €)";
	}

	public void setEstado(String estado, int id_profesor, int id_curso) {
		dataBase.executeUpdate(sqlSetEstado, estado, id_profesor, id_curso);

	}

}
