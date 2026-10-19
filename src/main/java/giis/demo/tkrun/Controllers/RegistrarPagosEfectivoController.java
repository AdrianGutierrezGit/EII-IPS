package giis.demo.tkrun.Controllers;

import java.io.File;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;

import giis.demo.tkrun.DTO.AlumnoDTO;
import giis.demo.tkrun.DTO.CursoDTO;
import giis.demo.tkrun.DTO.InscripcionDTO;
import giis.demo.tkrun.DTO.PagoDTO;
import giis.demo.tkrun.Models.RegistrarPagosEfectivoModel;
import giis.demo.tkrun.Views.RegistrarPagosEfectivoView;
import giis.demo.util.SwingUtil;
import giis.demo.util.ValidadorDatos;

public class RegistrarPagosEfectivoController {

	private RegistrarPagosEfectivoModel modelo;
	private RegistrarPagosEfectivoView vista;
	private String fechaActual;
	private DefaultTableModel modeloTablaCursos = new DefaultTableModel(
			new String[] { "Nombre", "Inicio de inscripciones", "Fin de inscripciones", "Inicio del curso",
					"Fin del curso", "Estado del curso" },
			0);
	private DefaultTableModel modeloTablaInscripcionesCurso = new DefaultTableModel(
			new String[] { "Alumno", "Fecha de inscripción", "Cantidad pagada", "Cantidad faltante",
					"Cantidad a devolver", "Estado de la inscripción" },
			0);
	private DefaultTableModel modeloTablaPagosInscripcion = new DefaultTableModel(
			new String[] { "Remitente", "Cantidad del pago", "Fecha del pago" }, 0);

	private List<InscripcionDTO> inscripcionesCurso = new ArrayList<>();
	private List<CursoDTO> cursos = new ArrayList<>();
	private List<PagoDTO> pagosInscripcion = new ArrayList<>();
	private ListSelectionListener cursoSeleccionListener;

	public RegistrarPagosEfectivoController(String fechaHoy) {
		modelo = new RegistrarPagosEfectivoModel();
		vista = new RegistrarPagosEfectivoView();
		this.fechaActual = fechaHoy;
	}

	public void iniciarVista() {
		añadirCursos();
		vista.setVisible(true);
		cursoSeleccionListener = e -> cursoSeleccionado();
		vista.getTablaCursos().getSelectionModel().addListSelectionListener(cursoSeleccionListener);
		vista.getTablaInscripcionesCurso().getSelectionModel().addListSelectionListener(e -> tablaEstaSeleccionada());
		vista.getTablaInscripcionesCurso().getSelectionModel()
				.addListSelectionListener(e -> pagosInscripcionSeleccionada());
		vista.getBtnRegistrarPago().addActionListener(e -> registrarPago());

	}

	private void tablaEstaSeleccionada() {
		if (vista.getTablaInscripcionesCurso().getSelectedRow() != -1) {
			vista.getBtnRegistrarPago().setEnabled(true);
		}
	}

	private void registrarPago() {
		int filaSeleccionada = vista.getTablaInscripcionesCurso().getSelectedRow();
		if (SwingUtil.exceptionWrapper(() -> {
			validarDatosHanSidoIntroducidos();
			comprobarFormato();
			comprobarFechaPagoSuperiorAFechaInscripcion(filaSeleccionada);
		})) {
			return;
		}

		InscripcionDTO inscripcion = inscripcionesCurso.get(filaSeleccionada);
		int pagoActual = comprobarTipoDePago();
		int cuotaInscripcion = modelo.getCuotaInscripcion(inscripcion.getId_cuota());
		int totalPagado = obtenerCantidadPagadaTotal(inscripcion.getCantidad_pagada()) + pagoActual;
		int pagado = obtenerCantidadPagadaTotal(inscripcion.getCantidad_pagada());
		if (pagado == cuotaInscripcion) {
			JOptionPane.showMessageDialog(vista, "Cuota ya pagada", "Aviso", JOptionPane.WARNING_MESSAGE);
			return;
		}
		if (totalPagado != cuotaInscripcion) {
			JOptionPane.showMessageDialog(vista, "Cantidad incorrecta. La cuota es de " + cuotaInscripcion + "€",
					"Pago excesivo", JOptionPane.WARNING_MESSAGE);
			return;
		}

		registrarPago(filaSeleccionada);
	}

	private void registrarPago(int filaSeleccionada) {
		InscripcionDTO inscripcion = inscripcionesCurso.get(filaSeleccionada);
		CursoDTO curso = modelo.getCursoPorId(inscripcion.getId_curso());
		int pagoActual = comprobarTipoDePago();
		int cantidadPagadaTotal = obtenerCantidadPagadaTotal(inscripcion.getCantidad_pagada()) + pagoActual;

		restaurarEstadoInicial();
		int opcion = JOptionPane.showConfirmDialog(vista,
				"El pago ha sido registrado correctamente, \n ¿Desea imprimir un recibo del pago?",
				"Confirmacion del pago", JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE);

		actualizarInscripcion(inscripcion, curso, pagoActual, cantidadPagadaTotal);

		if (opcion == JOptionPane.YES_OPTION) {
			JOptionPane.showMessageDialog(null, "El recibo ha sido impreso correctamemte.", "Recibo",
					JOptionPane.INFORMATION_MESSAGE);
			generarReciboTXT(inscripcion, curso, pagoActual, vista.getTxFechaPago().getText());
		}

	}

	private int obtenerCantidadPagadaTotal(int cantidadPagadaTotal) {
		return cantidadPagadaTotal;
	}

	private int obtenerCantidadFaltaPorPagar(InscripcionDTO i) {
		int cantidadPagada = i.getCantidad_pagada();
		int cuota = modelo.getCuotaInscripcion(i.getId_cuota());

		if (i.getEstado().equals("CANCELADO")) {
			if (i.getBalance_inscripcion() < 0) {
				return Math.abs(i.getBalance_inscripcion());
			} else {
				return 0;
			}
		}

		if (!i.getEstado().equals("CANCELADO") && cuota > cantidadPagada) {
			return cuota - cantidadPagada;
		} else if (cantidadPagada < 0) {
			return Math.abs(cantidadPagada);
		}
		return 0;
	}

	private int obtenerCantidadADevolver(InscripcionDTO inscripcion) {
		if (inscripcion.getBalance_inscripcion() > 0) {
			return inscripcion.getBalance_inscripcion();
		}
		return 0;
	}

	private int comprobarTipoDePago() {
		return Integer.parseInt(vista.getTxCantidadPagada().getText());

	}

	private void actualizarInscripcion(InscripcionDTO inscripcion, CursoDTO curso, int pagoActual,
			int cantidadPagadaTotal) {
		int cuotaInscripcion = modelo.getCuotaInscripcion(inscripcion.getId_cuota());
		modelo.registrarPago(pagoActual, vista.getTxFechaPago().getText(), inscripcion.getId_inscripcion());
		modelo.actualizarCantidadPagadaEnInscripcion(pagoActual, inscripcion.getId_inscripcion());
		if (inscripcion.getEstado().equals("CANCELADO") || pagoActual < 0) {
			return;
		}

		int plazasTotales = modelo.obtenerPlazasCurso(curso.getId_curso());
		int plazasOcupadas = modelo.obtenerPlazasCursoOcupadas(inscripcion.getId_curso(),
				inscripcion.getId_inscripcion(), fechaActual);

		if (plazasTotales > plazasOcupadas) {
			if (!inscripcion.getEstado().equals("PAGADO")) {
				if (cantidadPagadaTotal >= cuotaInscripcion) {
					inscripcion.setEstado("PAGADO");
					modelo.aprobarInscripcion(inscripcion.getId_inscripcion());
				}
			}
		}
	}

	private void comprobarFechaPagoSuperiorAFechaInscripcion(int filaSeleccionada) {
		ValidadorDatos.comprobarFechaPagoSuperiorAFechaInscripcion(
				inscripcionesCurso.get(filaSeleccionada).getFecha_solicitud(), vista.getTxFechaPago().getText(),
				"La fecha introducida del pago es anterior a la fecha en la que se hizo la inscripción");

	}

	private void restaurarEstadoInicial() {
		vista.getBtnRegistrarPago().setEnabled(false);
		vista.getTablaCursos().getSelectionModel().removeListSelectionListener(cursoSeleccionListener);
		vista.getTablaCursos().clearSelection();
		vista.getTablaCursos().getSelectionModel().addListSelectionListener(cursoSeleccionListener);
		modeloTablaInscripcionesCurso.setRowCount(0);
		modeloTablaPagosInscripcion.setRowCount(0);
		vista.actualizarTablaPagosInscripcion(modeloTablaPagosInscripcion);
		vista.actualizarTablaInscripcionesCurso(modeloTablaInscripcionesCurso);
		vista.getTxCantidadPagada().setText("");
		vista.getTxFechaPago().setText("");
		vista.getTxCuotaInscripcion().setText("");
	}

	private void comprobarFormato() {
		ValidadorDatos.isDigit(vista.getTxCantidadPagada().getText(),
				"Formato de la cantidad pagada incorrecta, introduzca un dígito");
		ValidadorDatos.isDate(vista.getTxFechaPago().getText(),
				"Formato de la fecha incorrecto, use el formato YYYY-MM-DD");

	}

	private void validarDatosHanSidoIntroducidos() {
		ValidadorDatos.isNotBlank(vista.getTxCantidadPagada().getText(), "Rellene la cantidad pagada");
		ValidadorDatos.isNotBlank(vista.getTxFechaPago().getText().trim(), "Rellene la fecha del pago");
	}

	private void cursoSeleccionado() {
		int filaCursoSeleccionado = vista.getTablaCursos().getSelectedRow();
		modeloTablaInscripcionesCurso.setRowCount(0);
		inscripcionesCurso = modelo.getInscripcionesParaCurso(cursos.get(filaCursoSeleccionado).getId_curso());
		for (InscripcionDTO i : inscripcionesCurso) {
			AlumnoDTO alumno = modelo.getAlumnoForId(i.getId_alumno()).get(0);
			modeloTablaInscripcionesCurso.addRow(new Object[] { alumno.getNombre() + " " + alumno.getApellidos(),
					i.getFecha_solicitud(), obtenerCantidadPagadaTotal(i.getCantidad_pagada()),
					obtenerCantidadFaltaPorPagar(i), obtenerCantidadADevolver(i), i.getEstado() });
		}
		vista.actualizarTablaInscripcionesCurso(modeloTablaInscripcionesCurso);
	}

	private void mostrarCuota(int filaCursoSeleccionado) {
		vista.getTxCuotaInscripcion().setText(String
				.valueOf(modelo.getCuotaInscripcion(inscripcionesCurso.get(filaCursoSeleccionado).getId_cuota())));
	}

	private void pagosInscripcionSeleccionada() {
		int filaSeleccionada = vista.getTablaInscripcionesCurso().getSelectedRow();
		if (filaSeleccionada != -1) {
			modeloTablaPagosInscripcion.setRowCount(0);
			pagosInscripcion = modelo.getPagosInscripcion(inscripcionesCurso.get(filaSeleccionada).getId_inscripcion());
			for (PagoDTO p : pagosInscripcion) {
				modeloTablaPagosInscripcion.addRow(
						new Object[] { obtenerRemitente(p), Math.abs(p.getCantidad_pagada()), p.getFecha_pago() });
			}
			vista.actualizarTablaPagosInscripcion(modeloTablaPagosInscripcion);
			mostrarCuota(filaSeleccionada);
		}
	}

	private String obtenerRemitente(PagoDTO p) {
		if (p.getCantidad_pagada() < 0) {
			return "COIIPA";
		}
		return "Inscrito";
	}

	private void añadirCursos() {
		for (CursoDTO curso : modelo.getCursos()) {
			if (!curso.getEstado_curso().trim().equals(CursoDTO.ESTADO_CERRADO)) {
				cursos.add(curso);
				modeloTablaCursos.addRow(new Object[] { curso.getNombre(), curso.getInicio_plazo_inscripcion(),
						curso.getFin_plazo_inscripcion(), curso.getFecha_inicio(), curso.getFecha_fin(),
						curso.getEstado_curso() });
			}
		}
		vista.actualizarTablaCursos(modeloTablaCursos);

	}

	private void generarReciboTXT(InscripcionDTO inscripcion, CursoDTO curso, int pagoActual, String fechaPago) {
		try {
			String ruta = "src/main/resources/impresora";

			AlumnoDTO alumno = modelo.getAlumnoForId(inscripcion.getId_alumno()).get(0);

			String nombreArchivo = "recibo_" + inscripcion.getId_inscripcion() + "_" + System.currentTimeMillis()
					+ ".txt";

			File carpeta = new File(ruta);
			if (!carpeta.exists()) {
				carpeta.mkdirs();
			}

			File archivo = new File(carpeta, nombreArchivo);
			PrintWriter writer = new PrintWriter(archivo, "UTF-8");

			writer.println("====================================================");
			writer.println("                    RECIBO DE PAGO");
			writer.println("====================================================");
			writer.println();
			writer.println("IDENTIFICADOR DEL RECIBO: " + inscripcion.getId_inscripcion());
			writer.println("FECHA DE EMISIÓN: " + vista.getTxFechaPago().getText().toString());
			writer.println();
			writer.println("----------------------------------------------------");
			writer.println("DATOS DEL EMISOR");
			writer.println("Entidad: COIIPA");
			writer.println("Dirección: Oviedo, Asturias");
			writer.println("Contacto: info@coiipa.org");
			writer.println();
			writer.println("----------------------------------------------------");
			writer.println("DATOS DEL ALUMNO");
			writer.println("Nombre: " + alumno.getNombre() + " " + alumno.getApellidos());
			writer.println("ID Alumno: " + alumno.getId_alumno());
			writer.println();
			writer.println("----------------------------------------------------");
			writer.println("INFORMACIÓN DEL CURSO");
			writer.println("Curso: " + curso.getNombre());
			writer.println("Fecha inicio: " + curso.getFecha_inicio());
			writer.println("Fecha fin: " + curso.getFecha_fin());
			writer.println();
			writer.println("----------------------------------------------------");
			writer.println("DETALLES DEL PAGO");
			writer.println("Fecha del pago: " + fechaPago);
			writer.println("Cantidad: " + Math.abs(pagoActual) + " €");
			writer.println("Método: Efectivo");
			writer.println("Estado inscripción: " + inscripcion.getEstado());
			writer.println();
			writer.println("----------------------------------------------------");
			writer.println("OBSERVACIONES");
			writer.println("Este documento certifica que el pago indicado ha sido");
			writer.println("registrado correctamente por COIIPA.");
			writer.println();
			writer.println("Firma:");
			writer.println("COIIPA - Colegio Oficial de Ingenieros Informáticos");
			writer.println();
			writer.println("====================================================");
			writer.println("                FIN DEL RECIBO");
			writer.println("====================================================");

			writer.close();

		} catch (Exception ex) {
			ex.printStackTrace();
			JOptionPane.showMessageDialog(vista, "El pago se registró, pero hubo un problema generando el recibo.",
					"Error generando recibo", JOptionPane.ERROR_MESSAGE);
		}
	}

}