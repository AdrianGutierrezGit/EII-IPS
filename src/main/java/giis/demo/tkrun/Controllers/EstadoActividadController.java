package giis.demo.tkrun.Controllers;

import java.util.List;
import java.util.Map;

import giis.demo.tkrun.DTO.CuotaDTO;
import giis.demo.tkrun.DTO.CursoDTO;
import giis.demo.tkrun.Models.EstadoActividadModel;
import giis.demo.tkrun.Views.EstadoActividadView;

public class EstadoActividadController {
	private EstadoActividadModel modelo;
	private EstadoActividadView vista;
	private String fechaActual;

	public EstadoActividadController(String fechaActual) {
		modelo = new EstadoActividadModel();
		vista = new EstadoActividadView();
		this.fechaActual = fechaActual;
		cargaCursos();

		vista.getComboBox().addActionListener(e -> actualizarDatosCurso());
		// actualizarDatosCurso();
	}

	private void cargaCursos() {
		List<String> nombresCursos = modelo.getNombresCursos();
		vista.setNombresCursos(nombresCursos);

	}

	private void actualizarDatosCurso() {
		String nombreCursoSeleccionado = (String) vista.getComboBox().getSelectedItem();

		CursoDTO cdto = modelo.getCursoDto(nombreCursoSeleccionado);

		// Actualizar fechas y componentes
		actualizarDatosCurso(nombreCursoSeleccionado, cdto);

		// Estado
		actualizarEstado(cdto);

		// Inscritos
		listaInscritos(cdto);

		// Ingresos y Gastos
		calcularIngresosYGastos(cdto);

		// lista de cuotas aplicadas con nombre y precio para ese curso
		mostrarCuotaCurso(cdto.getId_curso());

	}

	private void mostrarCuotaCurso(int idCurso) {

		List<CuotaDTO> cuotasDelCurso = modelo.getCuotasCurso(idCurso);
		// lista de cuotas aplicadas con nombre y precio para ese curso
		if (cuotasDelCurso.isEmpty()) {
			CuotaDTO gratis = new CuotaDTO();
			gratis.setPrecio(0);
			gratis.setNombre("Todo el público");
			gratis.setId_tipo_cuota(-1);
			cuotasDelCurso.add(gratis);
		}
		vista.setCuotas(cuotasDelCurso);

	}

	private void actualizarDatosCurso(String nombreCursoSeleccionado, CursoDTO cdto) {
		vista.getTfNombreCurso().setText(nombreCursoSeleccionado);
		;
		vista.getTfNPlazas().setText("" + cdto.getNum_plazas());
		vista.getTfFechaInscripcion().setText(
				cdto.getInicio_plazo_inscripcion().toString() + " a " + cdto.getFin_plazo_inscripcion().toString());
		if (cdto.getFecha_fin().equals(cdto.getFecha_inicio())) {
			vista.getTfFechaCelebracion().setText(cdto.getFecha_inicio().toString());
		} else {
			vista.getTfFechaCelebracion()
					.setText(cdto.getFecha_inicio().toString() + " - " + cdto.getFecha_fin().toString());
		}

		// Plazas disponibles
		int n = modelo.obtenerPlazasCursoOcupadas(cdto.getId_curso(), fechaActual);
		int plazas = cdto.getNum_plazas() - n;
		// if (plazas < 0)
		// plazas = 0;
		vista.getTfPlazasDisponibles().setText("" + (plazas));

	}

	private void actualizarEstado(CursoDTO cdto) {
		String e = "";
		switch (cdto.getEstado_curso()) {
		case "planificado":
			e = "Planificado";
			break;
		case "periodo_inscripcion":
			e = "Periodo de inscripción";
			break;
		case "inscripcion_cerrada":
			e = "Inscripción cerrada";
			break;
		case "curso_cerrado":
			e = "Curso cerrado";
			break;
		case "cancelado":
			e = "Cancelado";
			break;
		case "en_celebracion":
			e = "En celebración";
			break;
		case "celebrado":
			e = "Celebrado";
			break;
		}
		vista.getTfEstadoCurso().setText(e);
	}

	private void listaInscritos(CursoDTO cdto) {
		// Nombre - Fecha - Estado
		List<Map<String, Object>> inscritos = modelo.getInscritos(cdto.getId_curso());
		vista.getModelTablaInscritos().setRowCount(0);

		for (Map<String, Object> map : inscritos) {
			vista.getModelTablaInscritos().addRow(new Object[] { map.get("nombre") + " " + map.get("apellidos"),
					map.get("fecha_solicitud"), map.get("estado") });
		}
	}

	private void calcularIngresosYGastos(CursoDTO cdto) {
		int ingresosEstimados = modelo.getSumaCuotasInscritos(cdto.getId_curso(), fechaActual)
				+ modelo.getSumaCuotasRestantes(cdto, fechaActual)
				+ modelo.getSumaGananciasCancelaciones(cdto.getId_curso());
		int ingresosConfirmados = modelo.ingresosConfirmados(cdto.getId_curso());
		int gastosEstimados = modelo.gastosEstimados(cdto.getId_curso());
		int gastosConfirmados = modelo.gastosConfirmados(cdto.getId_curso());

		vista.getTfIngEstimados().setText("" + ingresosEstimados);
		vista.getTfIngrConfirmados().setText("" + ingresosConfirmados);
		vista.getTfGastEstimados().setText("" + gastosEstimados);
		vista.getTfGastConfirmados().setText("" + gastosConfirmados);
	}

	public void iniciarVista() {
		vista.setVisible(true);
	}

}
