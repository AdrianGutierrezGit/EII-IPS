package giis.demo.tkrun.DTO;

public class PagoDTO {

	private int id_pago;
	private int cantidad_pagada;
	private String fecha_pago;
	private int id_inscripcion;

	public int getCantidad_pagada() {
		return cantidad_pagada;
	}

	public String getFecha_pago() {
		return fecha_pago;
	}

	public int getId_pago() {
		return id_pago;
	}

	public void setId_pago(int id_pago) {
		this.id_pago = id_pago;
	}

	public int getId_inscripcion() {
		return id_inscripcion;
	}

	public void setId_inscripcion(int id_inscripcion) {
		this.id_inscripcion = id_inscripcion;
	}

	public void setCantidad_pagada(int cantidad_pagada) {
		this.cantidad_pagada = cantidad_pagada;
	}

	public void setFecha_pago(String fecha_pago) {
		this.fecha_pago = fecha_pago;
	}

}