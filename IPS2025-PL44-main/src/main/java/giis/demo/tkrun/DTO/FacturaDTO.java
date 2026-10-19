package giis.demo.tkrun.DTO;

// import java.time.LocalDate; // No usar LocalDate para facilitar el mapeo

public class FacturaDTO {

	private Integer id_Factura;
	private String fecha_Emision;
	private Integer cantidad_Facturada;
	private String nombre_Emisor;
	private String direccion_Emisor;
	private Integer id_actividad_profesor;
	private String estado_factura;
	public static final String PAGADO = "PAGADO";
	public static final String PENDIENTE = "PENDIENTE";
	public static final String CONFLICTO = "CONFLICTO";

	// Constructor vacío
	public FacturaDTO() {
	}

	public FacturaDTO(Integer id_Factura, String fecha_Emision, Integer cantidad_Facturada, String nombre_Emisor,
			String direccion_Emisor, Integer id_actividad_profesor) {
		super();
		this.id_Factura = id_Factura;
		this.fecha_Emision = fecha_Emision;
		this.cantidad_Facturada = cantidad_Facturada;
		this.nombre_Emisor = nombre_Emisor;
		this.direccion_Emisor = direccion_Emisor;
		this.id_actividad_profesor = id_actividad_profesor;
	}

	public Integer getId_Factura() {
		return id_Factura;
	}

	public void setId_Factura(Integer id_Factura) {
		this.id_Factura = id_Factura;
	}

	public String getFecha_Emision() {
		return fecha_Emision;
	}

	public void setFecha_Emision(String fecha_Emision) {
		this.fecha_Emision = fecha_Emision;
	}

	public Integer getCantidad_Facturada() {
		return cantidad_Facturada;
	}

	public void setCantidad_Facturada(Integer cantidad_Facturada) {
		this.cantidad_Facturada = cantidad_Facturada;
	}

	public String getNombre_Emisor() {
		return nombre_Emisor;
	}

	public void setNombre_Emisor(String nombre_Emisor) {
		this.nombre_Emisor = nombre_Emisor;
	}

	public String getDireccion_Emisor() {
		return direccion_Emisor;
	}

	public void setDireccion_Emisor(String direccion_Emisor) {
		this.direccion_Emisor = direccion_Emisor;
	}

	public Integer getId_actividad_profesor() {
		return id_actividad_profesor;
	}

	public void setId_actividad_profesor(Integer id_actividad_profesor) {
		this.id_actividad_profesor = id_actividad_profesor;
	}

	public String getEstado_factura() {
		return estado_factura;
	}

	public void setEstado_factura(String estado_factura) {
		this.estado_factura = estado_factura;
	}

	@Override
	public String toString() {
		return "FacturaDTO{" + "idFactura=" + id_Factura + ", fechaEmision=" + fecha_Emision + ", cantidadFacturada="
				+ cantidad_Facturada + ", nombreEmisor='" + nombre_Emisor + '\'' + '\'' + ", direccionEmisor='"
				+ direccion_Emisor + '}';
	}

}
