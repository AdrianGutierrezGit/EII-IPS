package giis.demo.tkrun.DTO;

import java.util.Objects;

public class CuotaDTO {
	private int id_tipo_cuota;

	private String nombre;
	private int precio; // en euros enteros
	private boolean aplicado;
	public static final int cuotaGeneral = 3;
	private int id_cuota_curso;

	public CuotaDTO() {
	}

	public CuotaDTO(int idTipoCuota, String nombre, int precio, boolean aplicado) {
		this.id_tipo_cuota = idTipoCuota;
		this.nombre = nombre;
		this.precio = precio;
		this.aplicado = aplicado;
	}

	public int getId_tipo_cuota() {
		return id_tipo_cuota;
	}

	public void setId_tipo_cuota(int id_tipo_cuota) {
		this.id_tipo_cuota = id_tipo_cuota;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public int getPrecio() {
		return precio;
	}

	public void setPrecio(int precio) {
		if (precio < 0)
			throw new IllegalArgumentException("El precio no puede ser negativo");
		this.precio = precio;
	}

	public boolean isAplicado() {
		return aplicado;
	}

	public void setAplicado(boolean aplicado) {
		this.aplicado = aplicado;
	}

	@Override
	public String toString() {
		return nombre;
	}

	@Override
	public int hashCode() {
		return Objects.hash(id_tipo_cuota);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!(obj instanceof CuotaDTO))
			return false;
		CuotaDTO other = (CuotaDTO) obj;
		return id_tipo_cuota == other.id_tipo_cuota;
	}

	public int getId_cuota_curso() {
		return id_cuota_curso;
	}

	public void setId_cuota_curso(int id_cuota_curso) {
		this.id_cuota_curso = id_cuota_curso;
	}
}
