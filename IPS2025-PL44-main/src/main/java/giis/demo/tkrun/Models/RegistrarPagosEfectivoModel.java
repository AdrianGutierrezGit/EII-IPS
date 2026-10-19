package giis.demo.tkrun.Models;

import java.util.List;

import giis.demo.tkrun.DTO.AlumnoDTO;
import giis.demo.tkrun.DTO.CuotaDTO;
import giis.demo.tkrun.DTO.CursoDTO;
import giis.demo.tkrun.DTO.InscripcionDTO;
import giis.demo.tkrun.DTO.PagoDTO;
import giis.demo.util.Database;

public class RegistrarPagosEfectivoModel {
	private Database dataBase;
	private String cursos = "select * from curso";
	private String inscripciones_del_curso = "SELECT i.* " + "FROM inscripcion i "
			+ "JOIN cuota_curso cc ON i.id_cuota = cc.id_cuota_curso " + "WHERE i.id_curso = ? "
			+ "AND (i.cantidad_pagada = 0 OR i.cantidad_pagada = cc.precio) " + "ORDER BY i.fecha_solicitud ASC";

	private String alumno_por_id = "Select * from alumno a, persona p where a.id_alumno = ? and a.id_alumno = p.id";
	private String cancelar_inscripcion = "update inscripcion set estado = 'CANCELADO' where id_inscripcion = ?";
	private String aprobar_inscripcion = "update inscripcion set estado = 'PAGADO' where id_inscripcion = ?";
	private String registrar_pago = "insert into pago (cantidad_pagada,fecha_pago,id_inscripcion) values (?,?,?)";
	private String obtener_plazas_curso = "select * from curso where id_curso = ?";
	private String obtener_plazas_curso_ocupadas = "select * from inscripcion i where i.id_curso = ? "
			+ "and i.id_inscripcion != ? and ( " + "i.estado = 'PAGADO' or (i.estado = 'PENDIENTE' "
			+ "and cast(JULIANDAY(?) - JULIANDAY(i.fecha_solicitud) as integer) <= 2))";
	private String obtener_curso_por_id = "select * from curso where id_curso = ?";
	private String get_cantidad_pagada_inscripcion = "select * from pago where id_inscripcion = ?";
	private String pagos_de_una_inscripcion = "select * from pago where id_inscripcion = ?";
	private String actualizar_cantidad_pagada_en_inscripcion = "update inscripcion set cantidad_pagada = cantidad_pagada + ? where id_inscripcion = ?";

	private String inscripcion_en_48horas = "select * from inscripcion where id_inscripcion = ? and cast(JULIANDAY(?) - JULIANDAY(fecha_solicitud) as integer) <= 2";
	private String pagos_inscripcion = "select * from pago where id_inscripcion = ?";
	private String cuota_inscripcion = "Select * from cuota_curso cc where cc.id_cuota_curso = ?";

	public RegistrarPagosEfectivoModel() {
		dataBase = new Database();

	}

	public List<CursoDTO> getCursos() {
		return dataBase.executeQueryPojo(CursoDTO.class, cursos);
	}

	public List<InscripcionDTO> getInscripcionesParaCurso(int id_curso) {
		return dataBase.executeQueryPojo(InscripcionDTO.class, inscripciones_del_curso, id_curso);
	}

	public List<AlumnoDTO> getAlumnoForId(int id) {
		return dataBase.executeQueryPojo(AlumnoDTO.class, alumno_por_id, id);
	}

	public void cancelarInscripcion(int id) {
		dataBase.executeUpdate(cancelar_inscripcion, id);

	}

	public void aprobarInscripcion(int id) {
		dataBase.executeUpdate(aprobar_inscripcion, id);
	}

	public void registrarPago(int pagoActual, String fecha, int id) {
		dataBase.executeUpdate(registrar_pago, pagoActual, fecha, id);

	}

	public int obtenerPlazasCurso(int id_curso) {
		List<CursoDTO> plazas = dataBase.executeQueryPojo(CursoDTO.class, obtener_plazas_curso, id_curso);
		return plazas.get(0).getNum_plazas();
	}

	public int obtenerPlazasCursoOcupadas(int id_curso, int id_inscripcion, String fechaActual) {
		List<InscripcionDTO> plazas_ocupadas = dataBase.executeQueryPojo(InscripcionDTO.class,
				obtener_plazas_curso_ocupadas, id_curso, id_inscripcion, fechaActual);
		return plazas_ocupadas.size();
	}

	public CursoDTO getCursoPorId(int id_curso) {
		return dataBase.executeQueryPojo(CursoDTO.class, obtener_curso_por_id, id_curso).get(0);
	}

	public int getCantidadPagadaEnInscripcion(int id_inscripcion) {
		List<PagoDTO> pagosInscripcion = dataBase.executeQueryPojo(PagoDTO.class, get_cantidad_pagada_inscripcion,
				id_inscripcion);
		int cantidadADevolver = 0;
		for (PagoDTO pago : pagosInscripcion) {
			cantidadADevolver += pago.getCantidad_pagada();
		}
		return cantidadADevolver;
	}

	public int obtenerCantidadPagadaEnLaInscripcion(int id_inscripcion) {
		List<PagoDTO> pagosInscripcion = dataBase.executeQueryPojo(PagoDTO.class, pagos_de_una_inscripcion,
				id_inscripcion);
		int cantidadPagada = 0;
		for (PagoDTO p : pagosInscripcion) {
			cantidadPagada += p.getCantidad_pagada();
		}
		return cantidadPagada;
	}

	public void actualizarCantidadPagadaEnInscripcion(int pagoActual, int id_inscripcion) {
		dataBase.executeUpdate(actualizar_cantidad_pagada_en_inscripcion, pagoActual, id_inscripcion);
	}

	public boolean inscripcionDentro48Horas(int id_inscripcion, String fechaActual) {
		List<InscripcionDTO> inscripcion = dataBase.executeQueryPojo(InscripcionDTO.class, inscripcion_en_48horas,
				id_inscripcion, fechaActual);
		if (inscripcion.isEmpty()) {
			return false;
		}
		return true;
	}

	public List<PagoDTO> getPagosInscripcion(int id_inscripcion) {
		return dataBase.executeQueryPojo(PagoDTO.class, pagos_inscripcion, id_inscripcion);
	}

	public int getCuotaInscripcion(int id_cuota) {
		List<CuotaDTO> cuota = dataBase.executeQueryPojo(CuotaDTO.class, cuota_inscripcion, id_cuota);
		return cuota.get(0).getPrecio();
	}

}
