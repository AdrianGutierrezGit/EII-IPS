package giis.demo.tkrun.Controllers;

import java.awt.event.ActionListener;
import java.util.List;
import java.util.Map;

import giis.demo.tkrun.DTO.CursoDTO;
import giis.demo.tkrun.DTO.FacturaDTO;
import giis.demo.tkrun.DTO.ProfesorDTO;
import giis.demo.tkrun.Models.RegistroPagoProfesorModel;
import giis.demo.tkrun.Views.RegistroPagoProfesorView;

public class RegistroPagoProfesorController {

	private final RegistroPagoProfesorModel model;
	private final RegistroPagoProfesorView vista;

	private List<CursoDTO> cursos;
	private CursoDTO cursoActual;
	private ProfesorDTO profe;
	private final String fechaSistema;

	public RegistroPagoProfesorController(String fechaActual) {
		this.model = new RegistroPagoProfesorModel();
		this.vista = new RegistroPagoProfesorView();
		this.fechaSistema = fechaActual;
		cargarDatosIniciales();
	}

	private void cargarDatosIniciales() {
		cursos = model.getCursos();
		cursoActual = null;
	}

	public void iniciarVista() {

		// Limpiar listeners previos solo por seguridad
		for (ActionListener al : vista.getComboCursos().getActionListeners())
			vista.getComboCursos().removeActionListener(al);

		vista.getComboCursos().removeAllItems();
		for (CursoDTO curso : cursos) {
			vista.getComboCursos().addItem(curso);
		}

		vista.getTxtFechaTransferencia().setText(fechaSistema);

		// Listener seguro
		vista.getComboCursos().addActionListener(e -> onCursoSeleccionado());

		vista.getBtnGuardar().addActionListener(e -> guardarPago());
		vista.getBtnVolver().addActionListener(e -> vista.dispose());

		vista.setVisible(true);
	}

	private void onCursoSeleccionado() {

		int idx = vista.getComboCursos().getSelectedIndex();
		if (idx < 0 || idx >= cursos.size()) {
			cursoActual = null;
			vista.getComboProfesores().removeAllItems();
			vista.mostrarDatosProfesor(null);
			return;
		}

		cursoActual = cursos.get(idx);

		List<ProfesorDTO> profesores = model.getProfesor(cursoActual.getId_curso());

		// **IMPORTANTE: eliminar listeners previos**
		for (ActionListener al : vista.getComboProfesores().getActionListeners())
			vista.getComboProfesores().removeActionListener(al);

		vista.getComboProfesores().removeAllItems();

		for (ProfesorDTO p : profesores)
			vista.getComboProfesores().addItem(p);

		// Nuevo listener limpio
		vista.getComboProfesores().addActionListener(e -> {
			profe = (ProfesorDTO) vista.getComboProfesores().getSelectedItem();
			vista.mostrarDatosProfesor(profe);
			actualizarEstadoPago();
		});

		if (!profesores.isEmpty()) {
			vista.getComboProfesores().setSelectedIndex(0);
		} else {
			vista.mostrarDatosProfesor(null);
		}
	}

	private FacturaDTO crearFacturaDesdeVista() {
		Map<String, String> datos = vista.getDatosIntroducidos();

		if (datos.get("numeroFactura").isEmpty()) {
			vista.mostrarError("El número de factura no puede estar vacío");
			return null;
		}

		int numeroFactura;
		try {
			numeroFactura = Integer.parseInt(datos.get("numeroFactura"));
			if (numeroFactura <= 0)
				throw new NumberFormatException();
		} catch (Exception e) {
			vista.mostrarError("Número de factura inválido");
			return null;
		}

		if (datos.get("fechaEmision").isEmpty()) {
			vista.mostrarError("La fecha de emisión no puede estar vacía");
			return null;
		}

		if (datos.get("cantidadFactura").isEmpty()) {
			vista.mostrarError("La cantidad de la factura no puede estar vacía");
			return null;
		}

		int cantidadFactura;
		try {
			cantidadFactura = Integer.parseInt(datos.get("cantidadFactura"));
			if (cantidadFactura <= 0)
				throw new NumberFormatException();
		} catch (Exception e) {
			vista.mostrarError("Cantidad de factura inválida");
			return null;
		}

		if (datos.get("direccionEmisor").isEmpty()) {
			vista.mostrarError("La dirección del emisor no puede estar vacía");
			return null;
		}

		ProfesorDTO profesorSeleccionado = (ProfesorDTO) vista.getComboProfesores().getSelectedItem();

		if (profesorSeleccionado == null) {
			vista.mostrarError("Debe seleccionar un profesor válido");
			return null;
		}

		FacturaDTO factura = new FacturaDTO();
		factura.setId_Factura(numeroFactura);
		factura.setFecha_Emision(datos.get("fechaEmision"));
		factura.setCantidad_Facturada(cantidadFactura);
		factura.setDireccion_Emisor(datos.get("direccionEmisor"));

		Integer idActividad = model.getActividadProfesor(cursoActual.getId_curso(), profesorSeleccionado.getId());

		factura.setId_actividad_profesor(idActividad);
		factura.setNombre_Emisor(profesorSeleccionado.getNombre());

		return factura;
	}

	private void guardarPago() {

		if (cursoActual == null) {
			vista.mostrarError("Seleccione un curso");
			return;
		}

		FacturaDTO factura = crearFacturaDesdeVista();
		if (factura == null)
			return;

		int idFactura = factura.getId_Factura();
		int idProfesor = profe.getId();
		int idCurso = cursoActual.getId_curso();

		boolean existeGlobal = model.existeFacturaGlobal(idFactura);
		boolean conflicto = model.existeFacturaEnOtroSitio(idFactura, idProfesor, idCurso);

		// Caso conflictivo: la factura existe pero pertenece a otro profe o
		// curso
		if (existeGlobal && conflicto) {
			vista.mostrarError("La factura ya existe y pertenece a otro profesor o curso.");
			return;
		}

		String cantidadStr = vista.getDatosIntroducidos().get("cantidadTransferida");

		if (cantidadStr.isEmpty()) {
			vista.mostrarError("La cantidad transferida no puede estar vacía");
			return;
		}

		int cantidadTransferida;

		try {
			cantidadTransferida = Integer.parseInt(cantidadStr);
			if (vista.getBtnCobra().isSelected())
				cantidadTransferida = -cantidadTransferida;
		} catch (Exception e) {
			vista.mostrarError("Cantidad transferida inválida");
			return;
		}

		// Si la factura es nueva, se guarda
		if (!existeGlobal) {
			model.guardarFactura(factura);
		}

		model.guardarPagoProfesor(vista.getTxtFechaTransferencia().getText(), cantidadTransferida,
				factura.getId_Factura());

		vista.mostrarPagoExitosoYCerrar();
		actualizarEstadoPago();
	}

	private void actualizarEstadoPago() {
		if (cursoActual == null || profe == null) {
			vista.getLblEstadoPago().setText("Pendiente");
			return;
		}

		String estado = model.estadoPagoProfesor(profe.getId(), cursoActual.getId_curso());
		vista.getLblEstadoPago().setText(estado);
		if (estado.equals("Pagada")) {
			model.setEstado("PAGADO", profe.getId(), cursoActual.getId_curso());
		}

	}

	public RegistroPagoProfesorView getVista() {
		return vista;
	}
}