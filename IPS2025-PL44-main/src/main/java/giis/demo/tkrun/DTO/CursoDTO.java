package giis.demo.tkrun.DTO;

import java.sql.Date;

public class CursoDTO {

	private int id_curso;
	private String nombre;
	private String inicio_plazo_inscripcion;
	private String fin_plazo_inscripcion;
	private String fecha_inicio;
	private String fecha_fin;
	private String lugar;
	private String objetivos;
	private String contenidos;

	private int num_plazas;
	private int remuneracion;
	private String estado_curso;

	private boolean retrasado;
	private String fecha_retraso;

	public final static String ESTADO_PLANIFICADO = "planificado";
	public final static String ESTADO_PERIODO_INSCRIPCION = "periodo_inscripcion";
	public final static String ESTADO_INSCRIPCION_CERRADA = "inscripcion_cerrada";
	public final static String ESTADO_CERRADO = "curso_cerrado";
	public final static String ESTADO_CANCELADO = "cancelado";
	public final static String ESTADO_EN_CELEBRACION = "en_celebracion";
	public final static String ESTADO_CELEBRADO = "celebrado";

	public String getFecha_retraso() {
		return fecha_retraso;
	}

	public void setFecha_retraso(String fecha_retraso) {
		this.fecha_retraso = fecha_retraso;
	}

	public String getObjetivos() {
		return objetivos;
	}

	public void setObjetivos(String objetivos) {
		this.objetivos = objetivos;
	}

	public String getContenidos() {
		return contenidos;
	}

	public void setContenidos(String contenidos) {
		this.contenidos = contenidos;
	}

	public String getEstado_curso() {
		return estado_curso;
	}

	public void setEstado_curso(String estado_curso) {
		this.estado_curso = estado_curso;
	}

	private ProfesorDTO profesor;

	public ProfesorDTO getProfesor() {
		return profesor;
	}

	public void setProfesor(ProfesorDTO profesor) {
		this.profesor = profesor;
	}

	public int getRemuneracion() {
		return remuneracion;
	}

	public void setRemuneracion(int remuneracion) {
		this.remuneracion = remuneracion;
	}

	public String getLugar() {
		return lugar;
	}

	public void setLugar(String lugar) {
		this.lugar = lugar;
	}

	public CursoDTO() {

	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public int getId_curso() {
		return id_curso;
	}

	public String getFecha_inicio() {
		return fecha_inicio;
	}

	public void setId_curso(int id_curso) {
		this.id_curso = id_curso;
	}

	public void setFecha_inicio(String fecha_inicio) {
		this.fecha_inicio = fecha_inicio;
	}

	public String getInicio_plazo_inscripcion() {
		return inicio_plazo_inscripcion;
	}

	public String getFecha_fin() {
		return fecha_fin;
	}

	public void setInicio_plazo_inscripcion(String inicio_plazo_inscripcion) {
		this.inicio_plazo_inscripcion = inicio_plazo_inscripcion;
	}

	public void setFecha_fin(String fecha_fin) {
		this.fecha_fin = fecha_fin;
	}

	public String getFin_plazo_inscripcion() {
		return fin_plazo_inscripcion;
	}

	public void setFin_plazo_inscripcion(String fin_plazo_inscripcion) {
		this.fin_plazo_inscripcion = fin_plazo_inscripcion;
	}

	public int getNum_plazas() {
		return num_plazas;
	}

	public void setNum_plazas(int num_plazas) {
		this.num_plazas = num_plazas;
	}

	public Date getIncioInscripcion() {
		return Date.valueOf(getInicio_plazo_inscripcion());
	}

	public Date getFinInscripcion() {
		return Date.valueOf(getFin_plazo_inscripcion());
	}

	public Date getFechaInicioCurso() {
		return Date.valueOf(getFecha_inicio());
	}

	public Date getFechaFinCurso() {
		return Date.valueOf(getFecha_fin());
	}

	public boolean isRetrasado() {
		return retrasado;
	}

	public void setRetrasado(boolean retrasado) {
		this.retrasado = retrasado;
	}

	// En CursoDTO.java añade:
	private Integer id_empresa; // Puede ser null
	private int coste_empresa;

	// Genera sus getters y setters
	public Integer getId_empresa() {
		return id_empresa;
	}

	public void setId_empresa(Integer id_empresa) {
		this.id_empresa = id_empresa;
	}

	public int getCoste_empresa() {
		return coste_empresa;
	}

	public void setCoste_empresa(int coste_empresa) {
		this.coste_empresa = coste_empresa;
	}

	@Override
	public String toString() {
		return nombre;
	}

}
