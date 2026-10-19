package giis.demo.tkrun.DTO;

public class InscripcionDTO {

	private int id_inscripcion;
	private String fecha_solicitud;
	private String estado;
	private int cantidad_pagada;
	private int balance_inscripcion;
	private int id_alumno;
	private int id_curso;
	private int id_cuota;

	// Datos toString
	private AlumnoDTO alumno;

	public static final String INSCRIPCION_PAGADA = "PAGADO";
	public static final String INSCRIPCION_PENDIENTE = "PENDIENTE";
	public static final String INSCRIPCION_CANCELADA = "CANCELADO";
	public static final String INSCRIPCION_LISTA_DE_ESPERA = "LISTA_DE_ESPERA";

	public int getId_alumno() {
		return id_alumno;
	}

	public int getBalance_inscripcion() {
		return balance_inscripcion;
	}

	public void setBalance_inscripcion(int balance_inscripcion) {
		this.balance_inscripcion = balance_inscripcion;
	}

	public void setId_alumno(int id_alumno) {
		this.id_alumno = id_alumno;
	}

	public int getId_curso() {
		return id_curso;
	}

	public void setId_curso(int id_curso) {
		this.id_curso = id_curso;
	}

	public int getId_inscripcion() {
		return id_inscripcion;
	}

	public void setId_inscripcion(int id_inscripcion) {
		this.id_inscripcion = id_inscripcion;
	}

	public String getFecha_solicitud() {
		return fecha_solicitud;
	}

	public void setFecha_solicitud(String fecha_solicitud) {
		this.fecha_solicitud = fecha_solicitud;
	}

	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public int getCantidad_pagada() {
		return cantidad_pagada;
	}

	public void setCantidad_pagada(int cantidad_pagada) {
		this.cantidad_pagada = cantidad_pagada;
	}

	public AlumnoDTO getAlumno() {
		return alumno;
	}

	public void setAlumno(AlumnoDTO alumno) {
		this.alumno = alumno;
	}

	public String toString() {
		return " - Nombre: " + alumno.getNombre() + " " + alumno.getApellidos() + "  - Estado inscripción: " + estado
				+ " -  Fecha inscripción: " + fecha_solicitud;
	}

	public int getId_cuota() {
		return id_cuota;
	}

	public void setId_cuota(int id_cuota) {
		this.id_cuota = id_cuota;
	}

}