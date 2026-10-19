package giis.demo.tkrun.Controllers;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.swing.table.DefaultTableModel;

import giis.demo.tkrun.DTO.CursoDTO;
import giis.demo.tkrun.Models.BalanceModel;
import giis.demo.tkrun.Views.BalanceView;
import giis.demo.util.SwingUtil;
import giis.demo.util.Util;
import giis.demo.util.ValidadorDatos;

public class BalanceController {

	private BalanceModel modelo;
	private BalanceView vista;
	private Date fechaActual;
	private String diaActual;

	public BalanceController(String diaActual) {
		modelo = new BalanceModel();
		vista = new BalanceView();
		fechaActual = Util.isoStringToDate(diaActual);
		this.diaActual = diaActual;
		vista.getBtnConsultar().addActionListener(e -> cargarCursos());
	}

	public void iniciarVista() {
		vista.setVisible(true);
		fechasActual();
	}

	private void fechasActual() {
		if (fechaActual == null)
			return;

		Calendar cal = Calendar.getInstance();
		cal.setTime(fechaActual);

		// 1 de enero
		cal.set(Calendar.MONTH, Calendar.JANUARY);
		cal.set(Calendar.DAY_OF_MONTH, 1);
		Date fechaInicio = cal.getTime();

		// 31 de diciembre
		cal.set(Calendar.MONTH, Calendar.DECEMBER);
		cal.set(Calendar.DAY_OF_MONTH, 31);
		Date fechaFin = cal.getTime();

		// Formato ISO
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		vista.getTxtFechaInicio().setText(sdf.format(fechaInicio));
		vista.getTxtFechaFin().setText(sdf.format(fechaFin));
	}

	private void cargarCursos() {
		// 1. Validar fechas antes de nada
		if (comprobacionCamposFechas())
			return;

		int ingresosTotales = 0;
		int gastosTotales = 0;

		String fechaFin = vista.getTxtFechaFin().getText();
		String fechaInicio = vista.getTxtFechaInicio().getText();

		// 2. Cargar datos crudos
		List<CursoDTO> lista = modelo.cargarCursosPorFecha(fechaInicio, fechaFin);
		lista = filtro(lista); // Aplicar filtro de estado

		// Actualizar contador de cursos
		vista.getValCursos().setText(lista.size() + "");

		// Limpiar tablas
		DefaultTableModel tablaCerrados = (DefaultTableModel) vista.getTblCerrados().getModel();
		DefaultTableModel tablaNoCerrados = (DefaultTableModel) vista.getTblNoCerrados().getModel();
		tablaCerrados.setRowCount(0);
		tablaNoCerrados.setRowCount(0);

		for (CursoDTO curso : lista) {
			// === CÁLCULO DE GASTOS ===

			// A) Gasto Confirmado (Pagos reales realizados):
			// Nota: Actualmente solo mira pagos a profesores.
			List<Object[]> pagosProfesores = modelo.getPagasProfesores(curso.getId_curso());

			int gastoConfirmado = 0;
			if (!pagosProfesores.isEmpty() && pagosProfesores.get(0)[0] != null) {
				gastoConfirmado = (Integer) pagosProfesores.get(0)[0];
			}

			// B) Gasto Estimado (Presupuesto):
			// Sumamos lo que cobran los profesores + lo que cobra la empresa
			// (Si es curso de empresa, los profes valen 0 y empresa X. Si es
			// normal, al
			// revés).
			int gastoProfes = modelo.getSumaRemuneracionProfesores(curso.getId_curso());
			int gastoEmpresa = curso.getCoste_empresa();
			int gastoEstimado = gastoProfes + gastoEmpresa;

			// === CÁLCULO DE INGRESOS ===

			// A) Ingreso Confirmado (Dinero real pagado por alumnos):
			List<Object[]> ingresosQuery = modelo.ingresosAseguradosCursos(curso.getId_curso());
			int ingresoConfirmado = 0;
			if (!ingresosQuery.isEmpty() && ingresosQuery.get(0)[0] != null) {
				ingresoConfirmado = (Integer) ingresosQuery.get(0)[0];
			}

			// B) Ingreso Estimado (Potencial):
			// Regla: Suma de cuotas de los ya inscritos + (Plazas libres *
			// Cuota más cara)
			int ingresosEstimados = modelo.getSumaCuotasInscritos(curso.getId_curso(), diaActual)
					+ modelo.getSumaCuotasRestantes(curso, diaActual)
					+ modelo.getSumaGananciasCancelaciones(curso.getId_curso());

			// === RELLENAR TABLAS ===

			if (curso.getEstado_curso().equals(CursoDTO.ESTADO_CERRADO)) {
				// Tabla superior: Cursos Cerrados (Solo datos reales)
				tablaCerrados.addRow(new Object[] { curso.getFecha_inicio(), curso.getNombre(), curso.getEstado_curso(),
						ingresoConfirmado, gastoConfirmado, ingresoConfirmado - gastoConfirmado // Balance Real
				});
			} else {
				// Tabla inferior: Cursos En Curso / Planificados (Estimado vs
				// Real)
				tablaNoCerrados.addRow(new Object[] { curso.getFecha_inicio(), curso.getNombre(),
						curso.getEstado_curso(), ingresosEstimados, // Ingresos
																	// Previstos
						ingresoConfirmado, // Ingresos Reales
						gastoEstimado, // Gastos Previstos (Profes +
										// Empresa)
						gastoConfirmado, // Gastos Reales
						ingresosEstimados - gastoEstimado, // Balance
															// Estimado
						ingresoConfirmado - gastoConfirmado // Balance
															// Real
				});
			}

			// Acumular totales para el resumen de arriba
			ingresosTotales += ingresoConfirmado;
			gastosTotales += gastoConfirmado;
		}

		// Actualizar resumen final en la cabecera
		vista.getValGas().setText(gastosTotales + " €");
		vista.getValIng().setText(ingresosTotales + " €");
		vista.getValBal().setText((ingresosTotales - gastosTotales) + " €");
	}

	private List<CursoDTO> filtro(List<CursoDTO> lista) {
		if (((String) vista.getCboEstado().getSelectedItem()).equals("todos")) {
			return lista;
		}
		List<CursoDTO> listaFiltrada = new ArrayList<CursoDTO>();
		for (CursoDTO curso : lista) {
			if (curso.getEstado_curso().equals((String) vista.getCboEstado().getSelectedItem())) {
				listaFiltrada.add(curso);
			}
		}
		return listaFiltrada;
	}

	private boolean comprobacionCamposFechas() {
		String fechaFin = vista.getTxtFechaFin().getText();
		String fechaInicio = vista.getTxtFechaInicio().getText();

		if (SwingUtil
				.exceptionWrapper(() -> ValidadorDatos.isNotBlank(fechaFin, "La fecha de fin no puede estar vacía")))
			return true;
		if (SwingUtil.exceptionWrapper(
				() -> ValidadorDatos.isNotBlank(fechaInicio, "La fecha de inicio no puede estar vacía")))
			return true;

		if (SwingUtil.exceptionWrapper(() -> Util.isIsoDateValid(fechaFin)))
			return true;
		if (SwingUtil.exceptionWrapper(() -> Util.isIsoDateValid(fechaInicio)))
			return true;

		Date inicio = Util.isoStringToDate(fechaInicio);
		Date fechaFinal = Util.isoStringToDate(fechaFin);
		SwingUtil.exceptionWrapper(() -> ValidadorDatos.isTrue(inicio.before(fechaFinal),
				"La fecha de inicio debe de ser anterior a la de fin"));
		return false;
	}

}
