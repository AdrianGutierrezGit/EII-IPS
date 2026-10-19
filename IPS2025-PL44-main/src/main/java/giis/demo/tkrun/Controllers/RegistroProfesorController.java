package giis.demo.tkrun.Controllers;

import giis.demo.tkrun.DTO.PersonaDTO;
import giis.demo.tkrun.Models.RegistroProfesorModel;
import giis.demo.tkrun.Views.RegistroProfesorView;
import giis.demo.util.SwingUtil;
import giis.demo.util.ValidadorDatos;

public class RegistroProfesorController {

	private RegistroProfesorModel model;

	public RegistroProfesorController() {
		model = new RegistroProfesorModel();
	}

	public void inicializarVista() {
		RegistroProfesorView ra = new RegistroProfesorView();

		ra.getBtnRegistrar().addActionListener(e -> {
			if (SwingUtil.exceptionWrapper(() -> {
				String nombre = ra.getTextNombre().getText().trim();
				String apellido = ra.getTextApellido().getText().trim();
				String telefonoStr = ra.getTextTelefono().getText().trim();
				String correo = ra.getTextCorreo().getText().trim();
				ValidadorDatos.isNotBlank(nombre, "El nombre está vacío");
				ValidadorDatos.isNotBlank(apellido, "Los apellidos están vacíos");
				ValidadorDatos.isNotBlank(correo, "El correo está vacío");
				ValidadorDatos.isNotBlank(telefonoStr, "El teléfono está vacío");
				ValidadorDatos.isDigit(telefonoStr, "El teléfono debe contener solo números");
			})) {
				return;
			}
			PersonaDTO profe = new PersonaDTO();
			int id = model.generarNuevoIdPersona();
			profe.setId(id);
			profe.setNombre(ra.getTextNombre().getText().trim());
			profe.setApellidos(ra.getTextApellido().getText().trim());
			profe.setEmail(ra.getTextCorreo().getText().trim());
			profe.setTelefono(ra.getTextTelefono().getText().trim());

			inscribir(profe, ra);
		});
		ra.setVisible(true);
	}

	public void inscribir(PersonaDTO pr, RegistroProfesorView ra) {

		int idExistente = model.obtenerIdPersonaPorEmail(pr.getEmail());
		if (idExistente != -1) {
			// ⚠ Ya existe una persona con ese email
			SwingUtil.exceptionWrapper(() -> ValidadorDatos.isTrue(false, "Correo ya existente"));
			return; // salir, no insertamos nada
		}

		// Comprobar persona
		if (!model.existePersona(pr)) {
			model.insertarPersona(pr);
		}

		// Comprobar profe
		if (model.existeProfesor(pr)) {
			SwingUtil.exceptionWrapper(() -> ValidadorDatos.isTrue(false, "Profesor ya existente"));
		} else {
			model.insertarProfesor(pr.getId());
			SwingUtil.exceptionWrapper(() -> ValidadorDatos.isTrue(false, "Profesor registrado correctamente"));
		}
	}
}
