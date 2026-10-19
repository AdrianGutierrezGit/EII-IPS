package giis.demo.tkrun.Views;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.MaskFormatter;

import giis.demo.tkrun.DTO.CuotaDTO;
import giis.demo.tkrun.DTO.ProfesorDTO;
import net.miginfocom.swing.MigLayout;

public class PlanificacionCursoView extends JFrame {
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JLabel lbNombre;
	private JTextField tfNombre;
	private JLabel lbProfesores;
	private JComboBox<ProfesorDTO> cbProfesores;
	private JLabel lbRemuneracion;
	private JLabel lbObjetivos;
	private JLabel lbCOntenidos;
	private JTextArea taObjetivos;
	private JTextArea taContenidos;
	private JSpinner spRemuneracion;
	private JLabel lbFecha;
	private JLabel lbEspacio;
	private JFormattedTextField tfFecha;
	private JTextField txEspacio;
	private JLabel lbAperturaInsripcion;
	private JLabel lbCierreInsripcion;
	private JFormattedTextField tfAperturaInscripcion;
	private JFormattedTextField tfCierreInscripcion;

	// Controles antiguos (se mantienen ocultos por compatibilidad si quieres)
	private JLabel lbCuota;
	private JCheckBox chbGratuito;
	private JSpinner spCuota;

	private JPanel panel;
	private JButton btGuardar;
	private JButton btCandelar;
	private JLabel lbPlazas;
	private JSpinner sPlazas;
	private JCheckBox chUnDia;
	private JFormattedTextField tfFechaFin;
	private JLabel lbFechaFin;
	private JScrollPane scPnProfesores;
	private JLabel lbProfesor;
	private JButton btAdd;
	private DefaultTableModel modeloTablaProfesores;

	// === NUEVOS ELEMENTOS PARA GESTIÓN DE CUOTAS ===
	private JScrollPane scCuotas;

	private JPanel pnCuotas; // Panel contenedor de las filas
	private JPanel pnAddCuota; // Panel para añadir nueva
	private JTextField tfNuevaCuotaNombre;
	private JSpinner spNuevaCuotaPrecio;
	private JButton btAddNuevaCuota;
	// ... otros atributos existentes ...
	private JCheckBox chbEmpresa;
	private JPanel pnEmpresa;
	private JTextField tfCifEmpresa;
	private JTextField tfNombreEmpresa;
	private JSpinner spCosteEmpresa;
	// ...
	// Lista para guardar las referencias a las filas visuales
	private final List<RowCuota> listaFilasCuotas = new ArrayList<>();

	private JTable tableProfesores;

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					PlanificacionCursoView frame = new PlanificacionCursoView();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public PlanificacionCursoView() {
		setResizable(false);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setTitle("COIIPA: Planificar cursos");
		setIconImage(Toolkit.getDefaultToolkit()
				.getImage(PlanificacionCursoView.class.getResource("/imagenes/logo_COIIPA.png")));
		setBounds(100, 100, 1363, 1042); // Aumentado un poco el alto

		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		// Ajustado MigLayout para dar espacio a la lista
		contentPane.setLayout(new MigLayout("", "[162.00,grow][198.00,grow][206.00,grow]",
				"[20px][54.00][][][][][][][][][][-10.00][117.00,grow,fill][65.00][12.00]"));

		contentPane.add(getLbNombre(), "cell 0 0,alignx left,growy");
		contentPane.add(getLbProfesores(), "flowx,cell 2 0,alignx left");
		contentPane.add(getTfNombre(), "cell 0 1,growx,aligny top");
		contentPane.add(getScPnProfesores(), "cell 2 1 1 3,grow");
		contentPane.add(getLbObjetivos(), "cell 0 2,alignx left");
		contentPane.add(getLbCOntenidos(), "cell 1 2");
		contentPane.add(getTaObjetivos(), "cell 0 3,grow");
		contentPane.add(getTaContenidos(), "cell 1 3,grow");
		contentPane.add(getLbPlazas(), "flowx,cell 1 4");
		contentPane.add(getLbFecha(), "flowx,cell 0 5,growx");
		contentPane.add(getTfFecha(), "flowx,cell 0 6,growx");
		contentPane.add(getChUnDia(), "cell 1 6");
		contentPane.add(getLbProfesor(), "flowx,cell 2 6");
		contentPane.add(getLbEspacio(), "cell 0 7");
		contentPane.add(getLbRemuneracion(), "flowx,cell 2 7,aligny center");
		contentPane.add(getTxEspacio(), "cell 0 8,growx");
		contentPane.add(getBtAddProf(), "cell 2 8,alignx center");
		contentPane.add(getLbAperturaInsripcion(), "cell 0 9");
		contentPane.add(getLbCierreInsripcion(), "cell 1 9");
		contentPane.add(getTfAperturaInscripcion(), "cell 0 10,growx");
		contentPane.add(getTfCierreInscripcion(), "cell 1 10,growx");

		// ELEMENTOS OCULTOS (Legacy)
		contentPane.add(getLbCuota(), "flowx,cell 0 11");
		contentPane.add(getChbGratuito(), "cell 1 11");
		contentPane.add(getSpCuota(), "cell 0 11,growx");
		getChbGratuito().setVisible(false);
		getSpCuota().setVisible(false);
		getLbCuota().setVisible(false);

		// === NUEVA SECCIÓN DE CUOTAS ===
		// Usamos cell 0 12 ocupando 2 columnas (span 2) y creciendo (grow)
		contentPane.add(getScCuotas(), "cell 0 12 2 1,grow");
		contentPane.add(getPnAddCuota(), "cell 0 13 2 1,growx");

		contentPane.add(getPanel(), "cell 1 14,grow");
		contentPane.add(getSPlazas(), "cell 1 4,growx");
		contentPane.add(getTfFechaFin(), "cell 0 6,growx");
		contentPane.add(getLbFechaFin(), "cell 0 5,growx");
		contentPane.add(getSpRemuneracion(), "cell 2 7,growx");
		contentPane.add(getCbProfesores(), "cell 2 6,growx,aligny center");

		// EMPRESA

		contentPane.add(getChbEmpresa(), "cell 2 9 2 1");

		// En tu constructor, al añadir el panel:
		contentPane.add(getPnEmpresa(), "cell 2 10 3 1, growx, hidemode 0");

		// --------------

		getTaObjetivos().setEditable(true);
		getTaContenidos().setEditable(true);

		getChUnDia().addActionListener(e -> {
			boolean variosDias = getChUnDia().isSelected();
			getTfFechaFin().setEnabled(variosDias);
			getLbFechaFin().setForeground(variosDias ? Color.BLACK : Color.GRAY);
		});
		setLocationRelativeTo(null);
	}

	public JCheckBox getChbEmpresa() {
		if (chbEmpresa == null) {
			chbEmpresa = new JCheckBox("Contratación externa (Empresa)");
			chbEmpresa.addActionListener(e -> {
				boolean esEmpresa = chbEmpresa.isSelected();

				// 1. Mostrar/Ocultar el panel
				getPnEmpresa().setVisible(esEmpresa);

				// 2. Gestión de la remuneración (lógica de negocio visual)
				getSpRemuneracion().setEnabled(!esEmpresa);
				if (esEmpresa) {
					getSpRemuneracion().setValue(0);
				}

				// 3. ACTUALIZACIÓN VISUAL SIN REDIMENSIONAR VENTANA
				// Eliminamos this.pack();
				contentPane.revalidate(); // Recalcula el layout interno
											// (MigLayout mueve las cosas)
				contentPane.repaint(); // Dibuja los cambios en pantalla
			});
		}
		return chbEmpresa;
	}

	public JPanel getPnEmpresa() {
		if (pnEmpresa == null) {
			pnEmpresa = new JPanel();
			pnEmpresa.setVisible(false); // Oculto por defecto
			pnEmpresa.setBorder(new TitledBorder("Datos de la Empresa Contratada"));
			pnEmpresa.setLayout(new MigLayout("insets 5, fillx", "[][grow][][grow]", "[]"));

			pnEmpresa.add(new JLabel("Nombre:"), "cell 0 0, alignx trailing");
			pnEmpresa.add(getTfNombreEmpresa(), "cell 1 0, growx");

			pnEmpresa.add(new JLabel("CIF:"), "cell 0 1, alignx trailing");
			pnEmpresa.add(getTfCifEmpresa(), "cell 1 1, growx");

			pnEmpresa.add(new JLabel("Coste Total (€):"), "cell 0 2, alignx trailing");
			pnEmpresa.add(getSpCosteEmpresa(), "cell 1 2, growx");
		}
		return pnEmpresa;
	}

	public JTextField getTfCifEmpresa() {
		if (tfCifEmpresa == null) {
			tfCifEmpresa = new JTextField();
		}
		return tfCifEmpresa;
	}

	public JTextField getTfNombreEmpresa() {
		if (tfNombreEmpresa == null) {
			tfNombreEmpresa = new JTextField();
		}
		return tfNombreEmpresa;
	}

	public JSpinner getSpCosteEmpresa() {
		if (spCosteEmpresa == null) {
			spCosteEmpresa = new JSpinner(new SpinnerNumberModel(0, 0, Integer.MAX_VALUE, 100));
		}
		return spCosteEmpresa;
	}

	// === MÉTODOS DE LA UI ORIGINAL ===
	private JLabel getLbNombre() {
		if (lbNombre == null)
			lbNombre = new JLabel("Nombre:");
		return lbNombre;
	}

	public JTextField getTfNombre() {
		if (tfNombre == null) {
			tfNombre = new JTextField();
			tfNombre.setColumns(10);
		}
		return tfNombre;
	}

	private JLabel getLbProfesores() {
		if (lbProfesores == null)
			lbProfesores = new JLabel("Profesores:");
		return lbProfesores;
	}

	public JComboBox<ProfesorDTO> getCbProfesores() {
		if (cbProfesores == null) {
			cbProfesores = new JComboBox<>();
			cbProfesores.setModel(new DefaultComboBoxModel<>());
		}
		return cbProfesores;
	}

	private JLabel getLbRemuneracion() {
		if (lbRemuneracion == null)
			lbRemuneracion = new JLabel("Remuneración:");
		return lbRemuneracion;
	}

	private JLabel getLbObjetivos() {
		if (lbObjetivos == null)
			lbObjetivos = new JLabel("Objetivos:");
		return lbObjetivos;
	}

	private JLabel getLbCOntenidos() {
		if (lbCOntenidos == null)
			lbCOntenidos = new JLabel("Contenidos:");
		return lbCOntenidos;
	}

	public JTextArea getTaObjetivos() {
		if (taObjetivos == null)
			taObjetivos = new JTextArea();
		return taObjetivos;
	}

	public JTextArea getTaContenidos() {
		if (taContenidos == null)
			taContenidos = new JTextArea();
		return taContenidos;
	}

	public JSpinner getSpRemuneracion() {
		if (spRemuneracion == null) {
			spRemuneracion = new JSpinner(new SpinnerNumberModel(0, 0, Integer.MAX_VALUE, 25));
		}
		return spRemuneracion;
	}

	public JLabel getLbFecha() {
		if (lbFecha == null)
			lbFecha = new JLabel("Fecha curso:");
		return lbFecha;
	}

	private JLabel getLbEspacio() {
		if (lbEspacio == null)
			lbEspacio = new JLabel("Espacio:");
		return lbEspacio;
	}

	public JFormattedTextField getTfFecha() {
		if (tfFecha == null) {
			try {
				MaskFormatter mf = new MaskFormatter("####-##-##");
				mf.setPlaceholderCharacter('_');
				tfFecha = new JFormattedTextField(mf);
				return tfFecha;
			} catch (Exception ex) {
				return new JFormattedTextField();
			}
		}
		return tfFecha;
	}

	public JTextField getTxEspacio() {
		if (txEspacio == null) {
			txEspacio = new JTextField();
			txEspacio.setColumns(10);
		}
		return txEspacio;
	}

	private JLabel getLbAperturaInsripcion() {
		if (lbAperturaInsripcion == null)
			lbAperturaInsripcion = new JLabel("Apertura inscripción:");
		return lbAperturaInsripcion;
	}

	private JLabel getLbCierreInsripcion() {
		if (lbCierreInsripcion == null)
			lbCierreInsripcion = new JLabel("Cierre inscripción:");
		return lbCierreInsripcion;
	}

	public JFormattedTextField getTfAperturaInscripcion() {
		if (tfAperturaInscripcion == null) {
			try {
				MaskFormatter mf = new MaskFormatter("####-##-##");
				mf.setPlaceholderCharacter('_');
				tfAperturaInscripcion = new JFormattedTextField(mf);
				return tfAperturaInscripcion;
			} catch (Exception ex) {
				return new JFormattedTextField();
			}
		}
		return tfAperturaInscripcion;
	}

	public JFormattedTextField getTfCierreInscripcion() {
		if (tfCierreInscripcion == null) {
			try {
				MaskFormatter mf = new MaskFormatter("####-##-##");
				mf.setPlaceholderCharacter('_');
				tfCierreInscripcion = new JFormattedTextField(mf);
				return tfCierreInscripcion;
			} catch (Exception ex) {
				return new JFormattedTextField();
			}
		}
		return tfCierreInscripcion;
	}

	private JLabel getLbCuota() {
		if (lbCuota == null)
			lbCuota = new JLabel("Cuota:");
		return lbCuota;
	}

	public JCheckBox getChbGratuito() {
		if (chbGratuito == null)
			chbGratuito = new JCheckBox("Gratuito");
		return chbGratuito;
	}

	public JSpinner getSpCuota() {
		if (spCuota == null)
			spCuota = new JSpinner(new SpinnerNumberModel(1, 1, Integer.MAX_VALUE, 10));
		return spCuota;
	}

	// === NUEVOS MÉTODOS VISUALES PARA CUOTAS ===

	private JScrollPane getScCuotas() {
		if (scCuotas == null) {
			scCuotas = new JScrollPane();
			scCuotas.setViewportView(getPnCuotas());
			scCuotas.setBorder(new TitledBorder("Listado de Cuotas"));
		}
		return scCuotas;
	}

	private JPanel getPnCuotas() {
		if (pnCuotas == null) {
			pnCuotas = new JPanel();
			pnCuotas.setBackground(Color.WHITE);
			// Layout vertical para ir apilando filas
			pnCuotas.setLayout(new MigLayout("ins 5, fillx, wrap 1", "[grow]", "[14.00]"));
		}
		return pnCuotas;
	}

	// Panel inferior para añadir manualmente
	private JPanel getPnAddCuota() {
		if (pnAddCuota == null) {
			pnAddCuota = new JPanel();
			pnAddCuota.setLayout(new MigLayout("ins 0, fillx", "[grow][80px][40px]", "[]"));
			pnAddCuota.setBorder(new TitledBorder("Añadir nueva cuota"));

			tfNuevaCuotaNombre = new JTextField();
			spNuevaCuotaPrecio = new JSpinner(new SpinnerNumberModel(0, 0, 99999, 5));
			btAddNuevaCuota = new JButton("+");
			btAddNuevaCuota.setBackground(new Color(50, 205, 50));
			btAddNuevaCuota.setForeground(Color.WHITE);

			// Lógica visual del botón +
			btAddNuevaCuota.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					accionAnadirManual();
				}
			});

			pnAddCuota.add(tfNuevaCuotaNombre, "growx");
			pnAddCuota.add(spNuevaCuotaPrecio, "growx");
			pnAddCuota.add(btAddNuevaCuota, "growx");
		}
		return pnAddCuota;
	}

	/** Llama a esto desde el controlador para cargar las de la BD */
	public void setCuotas(List<CuotaDTO> cuotas) {
		getPnCuotas().removeAll();
		listaFilasCuotas.clear();

		if (cuotas != null) {
			for (CuotaDTO c : cuotas) {
				RowCuota row = new RowCuota(c.getId_tipo_cuota(), c.getNombre(), c.getPrecio(), false);
				listaFilasCuotas.add(row);
				getPnCuotas().add(row.panelRow, "growx");
			}
		}
		getPnCuotas().revalidate();
		getPnCuotas().repaint();
	}

	/** Llama a esto para recoger todo antes de guardar */
	public List<CuotaDTO> getCuotasConfiguradas() {
		List<CuotaDTO> res = new ArrayList<>();
		for (RowCuota r : listaFilasCuotas) {
			// Solo guardamos las que tienen el check marcado
			if (r.check.isSelected()) {
				CuotaDTO dto = new CuotaDTO();
				dto.setId_tipo_cuota(r.idTipoCuota); // Será -1 si es nueva
				dto.setNombre(r.check.getText());
				dto.setAplicado(true);
				dto.setPrecio(((Number) r.spinner.getValue()).intValue());
				res.add(dto);
			}
		}
		return res;
	}

	private void accionAnadirManual() {
		String nombre = tfNuevaCuotaNombre.getText().trim();
		if (nombre.isEmpty())
			return;
		int precio = (Integer) spNuevaCuotaPrecio.getValue();

		// Creamos la fila (ID -1 porque es nueva)
		RowCuota row = new RowCuota(-1, nombre, precio, true);
		listaFilasCuotas.add(row);

		// Añadimos visualmente
		getPnCuotas().add(row.panelRow, "growx");

		// Limpiar inputs
		tfNuevaCuotaNombre.setText("");
		spNuevaCuotaPrecio.setValue(0);

		getPnCuotas().revalidate();
		getPnCuotas().repaint();
	}

	/** Clase interna simple para manejar cada fila */
	private static class RowCuota {
		final int idTipoCuota;
		final JCheckBox check;
		final JSpinner spinner;
		final JPanel panelRow;

		RowCuota(int id, String nombre, int precio, boolean aplicado) {
			this.idTipoCuota = id;
			this.panelRow = new JPanel(new MigLayout("ins 0, fillx", "[grow][80px]", "[]"));
			this.panelRow.setBackground(Color.WHITE);

			this.check = new JCheckBox(nombre);
			this.check.setSelected(aplicado);
			this.check.setBackground(Color.WHITE);

			this.spinner = new JSpinner(new SpinnerNumberModel(Math.max(0, precio), 0, Integer.MAX_VALUE, 10));
			this.spinner.setEnabled(aplicado);

			this.check.addActionListener(e -> this.spinner.setEnabled(this.check.isSelected()));

			this.panelRow.add(check, "growx");
			this.panelRow.add(spinner, "growx");
		}
	}

	// === FIN NUEVOS MÉTODOS ===

	private JPanel getPanel() {
		if (panel == null) {
			panel = new JPanel();
			panel.add(getBtGuardar());
			panel.add(getBtCandelar());
		}
		return panel;
	}

	public JButton getBtGuardar() {
		if (btGuardar == null)
			btGuardar = new JButton("Guardar");
		return btGuardar;
	}

	private JButton getBtCandelar() {
		if (btCandelar == null) {
			btCandelar = new JButton("Cancelar");
			btCandelar.addActionListener(e -> dispose());
		}
		return btCandelar;
	}

	public JLabel getLbPlazas() {
		if (lbPlazas == null)
			lbPlazas = new JLabel("Plazas disponibles:");
		return lbPlazas;
	}

	public JSpinner getSPlazas() {
		if (sPlazas == null) {
			SpinnerNumberModel modelo = new SpinnerNumberModel(0, 0, Integer.MAX_VALUE, 1);
			sPlazas = new JSpinner(modelo);
		}
		return sPlazas;
	}

	public JCheckBox getChUnDia() {
		if (chUnDia == null)
			chUnDia = new JCheckBox("Curso de varios días");
		return chUnDia;
	}

	public JFormattedTextField getTfFechaFin() {
		if (tfFechaFin == null) {
			try {
				MaskFormatter mf = new MaskFormatter("####-##-##");
				mf.setPlaceholderCharacter('_');
				tfFechaFin = new JFormattedTextField(mf);
				tfFechaFin.setEnabled(false);
				return tfFechaFin;
			} catch (Exception ex) {
				return new JFormattedTextField();
			}
		}
		return tfFechaFin;
	}

	public JLabel getLbFechaFin() {
		if (lbFechaFin == null) {
			lbFechaFin = new JLabel("Fecha fin curso:");
			lbFechaFin.setForeground(Color.GRAY);
		}
		return lbFechaFin;
	}

	private JScrollPane getScPnProfesores() {
		if (scPnProfesores == null) {
			scPnProfesores = new JScrollPane();
			scPnProfesores.setViewportView(getTableProfesores());
		}
		return scPnProfesores;
	}

	private JLabel getLbProfesor() {
		if (lbProfesor == null)
			lbProfesor = new JLabel("Profesor:");
		return lbProfesor;
	}

	public JButton getBtAddProf() {
		if (btAdd == null)
			btAdd = new JButton("Añadir profesor");
		return btAdd;
	}

	public DefaultTableModel getModeloTablaProfesores() {
		return modeloTablaProfesores;
	}

	public void mostrarMensajeConfirmacion() {
		JOptionPane.showMessageDialog(null, "El curso ha sido planificado con éxito", "Curso Planificado",
				JOptionPane.INFORMATION_MESSAGE);
	}

	private JTable getTableProfesores() {
		if (tableProfesores == null) {
			modeloTablaProfesores = new DefaultTableModel(new String[] { "Profesor", "Remuneración" }, 0);
			tableProfesores = new JTable(modeloTablaProfesores);
			tableProfesores.setDefaultEditor(Object.class, null);
		}
		return tableProfesores;
	}
}