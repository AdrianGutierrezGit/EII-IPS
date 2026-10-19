package giis.demo.tkrun.Controllers;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;

import giis.demo.tkrun.DTO.AlumnoDTO;
import giis.demo.tkrun.DTO.CursoDTO;
import giis.demo.tkrun.DTO.InscripcionDTO;
import giis.demo.tkrun.DTO.PagoDTO;
import giis.demo.tkrun.Models.RegistrarPagosModel;
import giis.demo.tkrun.Views.RegistrarPagosView;
import giis.demo.util.SwingUtil;
import giis.demo.util.ValidadorDatos;

public class RegistrarPagosController {
	private RegistrarPagosModel modelo;
	private RegistrarPagosView vista;
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

	public RegistrarPagosController(String fechaHoy) {
		modelo = new RegistrarPagosModel();
		vista = new RegistrarPagosView();
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

		registrarPago(filaSeleccionada);
	}

	private void registrarPago(int filaSeleccionada) {
		InscripcionDTO inscripcion = inscripcionesCurso.get(filaSeleccionada);
		CursoDTO curso = modelo.getCursoPorId(inscripcion.getId_curso());
		int pagoActual = comprobarTipoDePago();
		int cantidadPagadaTotal = obtenerCantidadPagadaTotal(inscripcion.getCantidad_pagada()) + pagoActual;

		List<String> mensajes = obtenerMensajesDeConsecuenciaAlPago(inscripcion, curso, pagoActual,
				cantidadPagadaTotal);

		String datosPago = "Datos del pago:\n- Cantidad: " + Math.abs(pagoActual) + "€\n- Fecha: "
				+ vista.getTxFechaPago().getText() + "\n\n";
		String resumenAcciones = "Consecuencias del registro:\n" + String.join("\n", mensajes) + "\n\n";

		int eleccionRegistro = vista.mostrarEleccionRegistro(datosPago, resumenAcciones);

		if (eleccionRegistro == JOptionPane.YES_OPTION) {
			actualizarInscripcion(inscripcion, curso, pagoActual, cantidadPagadaTotal);
			restaurarEstadoInicial();
			vista.mostrarMensajePagoRegistrado();
		}
	}

	private int obtenerCantidadPagadaTotal(int cantidadPagadaTotal) {
		if (cantidadPagadaTotal < 0) {
			return 0;
		}
		return cantidadPagadaTotal;
	}

	private int obtenerCantidadFaltaPorPagar(InscripcionDTO i) {
		int cantidadPagada = i.getCantidad_pagada();
		int cuota = modelo.getCuotaInscripcion(i.getId_cuota());

		if (i.getEstado().equals(InscripcionDTO.INSCRIPCION_LISTA_DE_ESPERA)) {
			cuota = 0;
		}
		if (i.getEstado().equals(InscripcionDTO.INSCRIPCION_CANCELADA)) {
			if (i.getBalance_inscripcion() < 0) {
				return Math.abs(i.getBalance_inscripcion());
			} else {
				return 0;
			}
		}

		if (!i.getEstado().equals(InscripcionDTO.INSCRIPCION_CANCELADA) && cuota > cantidadPagada) {
			return cuota - cantidadPagada;
		}
		return 0;
	}

	private int obtenerCantidadADevolver(InscripcionDTO inscripcion) {
		if (inscripcion.getBalance_inscripcion() > 0) {
			return inscripcion.getBalance_inscripcion();
		}
		return 0;
	}

	private List<String> obtenerMensajesDeConsecuenciaAlPago(InscripcionDTO inscripcion, CursoDTO curso, int pagoActual,
			int cantidadPagadaTotal) {
		List<String> mensajes = new ArrayList<>();
		int cuotaInscripcion = modelo.getCuotaInscripcion(inscripcion.getId_cuota());

		if (inscripcion.getEstado().equals(InscripcionDTO.INSCRIPCION_CANCELADA) || pagoActual < 0) {
			return gestionBalance(inscripcion, mensajes, pagoActual);
		}

		int plazasTotales = modelo.obtenerPlazasCurso(curso.getId_curso());
		int plazasOcupadas = modelo.obtenerPlazasCursoOcupadas(inscripcion.getId_curso(),
				inscripcion.getId_inscripcion(), fechaActual);
		boolean hayPlazasLibres = plazasTotales > plazasOcupadas;

		if (hayPlazasLibres) {

			if (inscripcion.getEstado().equals(InscripcionDTO.INSCRIPCION_PAGADA)) {
				mensajes.add("- El alumno ya tiene la plaza asignada.");
			} else {
				if (cantidadPagadaTotal >= cuotaInscripcion) {
					if (ValidadorDatos.fechaPagoSuperaLimite(LocalDate.parse(vista.getTxFechaPago().getText()),
							inscripcion.getFecha_solicitud())) {
						mensajes.add(
								"- El pago fue realizado fuera del límite de 48h, pero como hay plazas disponibles la plaza será asignada.");
					} else {
						mensajes.add("- Se confirmará y asignará la plaza al alumno.");
					}
				} else {
					// mirar si poner este mensaje o no
					mensajes.add("- El pago es parcial: La plaza no se asignará hasta completar la cuota.");
				}
			}

			if (cantidadPagadaTotal > cuotaInscripcion) {
				mensajes.add("- Advertencia: La cantidad pagada total por el alumno es superior a la cuota.\n"
						+ "  Se deberán devolverle " + (cantidadPagadaTotal - cuotaInscripcion) + "€");
			} else if (cantidadPagadaTotal < cuotaInscripcion) {
				mensajes.add("- Advertencia: La cantidad pagada total por el alumno es inferior a la cuota.\n"
						+ "  El alumno deberá pagar " + (cuotaInscripcion - cantidadPagadaTotal) + "€");
			} else {
				mensajes.add("- La cantidad pagada es exacta. La inscripción quedará totalmente pagada.");
			}

		} else {
			mensajes.add("- Advertencia: no quedan plazas disponibles.");
			mensajes.add("- La inscripción será cancelada automáticamente.");
			if (cantidadPagadaTotal > 0) {
				mensajes.add("- Se generará un saldo a devolver al alumno de " + cantidadPagadaTotal + "€.");
			}
		}

		return mensajes;
	}

	private List<String> gestionBalance(InscripcionDTO inscripcion, List<String> mensajes, int pagoActual) {
		int balancePendienteActual = inscripcion.getBalance_inscripcion();
		int nuevoBalance = balancePendienteActual + pagoActual;

		if (pagoActual < 0) {
			if (nuevoBalance == 0) {
				mensajes.add("- La cantidad devuelta es correcta.");
			} else if (nuevoBalance > 0) {
				mensajes.add(
						"- Advertencia: La devolución es insuficiente. Faltan por devolver " + nuevoBalance + "€.");
			} else {
				mensajes.add("- Advertencia: Devolución excesiva. El alumno ahora debería pagar "
						+ Math.abs(nuevoBalance) + "€.");
			}
		} else {
			if (balancePendienteActual < 0) {
				if (nuevoBalance == 0) {
					mensajes.add("- El pago es correcto. La deuda ha sido saldada.");
				} else if (nuevoBalance < 0) {
					mensajes.add("- Advertencia: El pago del alumno es insuficiente. El alumno aún debe "
							+ Math.abs(nuevoBalance) + "€.");
				} else {
					mensajes.add("- Advertencia: El pago del alumno es excesivo. Se le deberán devolver " + nuevoBalance
							+ "€.");
				}
			} else {
				mensajes.add("- Advertencia: Se ha recibido pago para una inscripción sin deuda. Se deberán devolver "
						+ nuevoBalance + "€.");
			}
		}
		return mensajes;
	}

	private int comprobarTipoDePago() {
		if (vista.getRbPagoIngreso().isSelected()) {
			return Integer.parseInt(vista.getTxCantidadPagada().getText());
		} else {
			return -Integer.parseInt(vista.getTxCantidadPagada().getText());
		}
	}

	private void actualizarInscripcion(InscripcionDTO inscripcion, CursoDTO curso, int pagoActual,
			int cantidadPagadaTotal) {
		int cuotaInscripcion = modelo.getCuotaInscripcion(inscripcion.getId_cuota());
		modelo.registrarPago(pagoActual, vista.getTxFechaPago().getText(), inscripcion.getId_inscripcion());
		modelo.actualizarCantidadPagadaEnInscripcion(pagoActual, inscripcion.getId_inscripcion());
		if (inscripcion.getEstado().equals(InscripcionDTO.INSCRIPCION_CANCELADA) || pagoActual < 0) {
			modelo.actualizarCantidadADevolverEnInscripcion(inscripcion.getId_inscripcion(), pagoActual);
			return;
		}

		int nuevoBalanceTotal = cantidadPagadaTotal - cuotaInscripcion;
		int balanceAnterior = inscripcion.getBalance_inscripcion();
		int cantidadParaActualizar = nuevoBalanceTotal - balanceAnterior;

		int plazasTotales = modelo.obtenerPlazasCurso(curso.getId_curso());
		int plazasOcupadas = modelo.obtenerPlazasCursoOcupadas(inscripcion.getId_curso(),
				inscripcion.getId_inscripcion(), fechaActual);

		if (plazasTotales > plazasOcupadas) {
			modelo.actualizarCantidadADevolverEnInscripcion(inscripcion.getId_inscripcion(), cantidadParaActualizar);
			if (cantidadPagadaTotal >= cuotaInscripcion) {
				if (!inscripcion.getEstado().equals(InscripcionDTO.INSCRIPCION_PAGADA)) {
					modelo.aprobarInscripcion(inscripcion.getId_inscripcion());
				}
			}
		} else {
			modelo.cancelarInscripcion(inscripcion.getId_inscripcion());
			int nuevoBalanceCorrecto = cantidadPagadaTotal;
			int cantidadParaActualizar_Cancelacion = nuevoBalanceCorrecto - balanceAnterior;
			modelo.actualizarCantidadADevolverEnInscripcion(inscripcion.getId_inscripcion(),
					cantidadParaActualizar_Cancelacion);
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
			AlumnoDTO alumno = modelo.getAlumnoForId(i.getId_alumno());
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
}