package giis.demo.tkrun.DTO;

public class PagoProfesorDTO {
	private int id_pago_profesor;
	private String fecha_transferencia;
	private int cantidad_transferida;
	private int id_factura;

	public int getId_pago_profesor() {
		return id_pago_profesor;
	}

	public void setId_pago_profesor(int id_pago_profesor) {
		this.id_pago_profesor = id_pago_profesor;
	}

	public String getFecha_transferencia() {
		return fecha_transferencia;
	}

	public void setFecha_transferencia(String fecha_transferencia) {
		this.fecha_transferencia = fecha_transferencia;
	}

	public int getId_factura() {
		return id_factura;
	}

	public void setId_factura(int id_factura) {
		this.id_factura = id_factura;
	}

	public int getCantidad_transferida() {
		return cantidad_transferida;
	}

	public void setCantidad_transferida(int cantidad_transferida) {
		this.cantidad_transferida = cantidad_transferida;
	}

}