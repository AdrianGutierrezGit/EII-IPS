package giis.demo.tkrun.DTO;

public class PersonaDTO {
	private int id;
	private String nombre;
	private String apellidos;
	private String email;
	private String telefono;
	private String rol; // alumno, profesor, responsable de formacion,
						// secretaria de formacion

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getApellidos() {
		return apellidos;
	}

	public void setApellidos(String apellidos) {
		this.apellidos = apellidos;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getTelefono() {
		return telefono;
	}

	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}

	public String getRol() {
		return rol;
	}

	public void setRol(String rol) {
		this.rol = rol;
	}

	@Override
	public String toString() {
		String r = rol != null ? rol : "";
		return nombre + " " + apellidos + (r.isEmpty() ? "" : " (" + r + ")");
	}
}