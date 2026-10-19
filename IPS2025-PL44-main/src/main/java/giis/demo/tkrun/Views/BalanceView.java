package giis.demo.tkrun.Views;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Toolkit;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.MaskFormatter;

import giis.demo.tkrun.DTO.CursoDTO;

@SuppressWarnings("serial")
public class BalanceView extends JFrame {

	// raíz
	private JPanel contentPane;

	// encabezado
	private JPanel pnlHeader;
	private JLabel lblTitulo;
	private JLabel lblSub;

	// filtros
	private JPanel pnlFiltros;
	private JLabel lblFechaInicio;
	private JFormattedTextField txtFechaInicio;
	private JLabel lblFechaFin;
	private JFormattedTextField txtFechaFin;
	private JLabel lblEstado;
	private JComboBox<String> cboEstado;
	private JPanel pnlBotones;
	private JButton btnConsultar;
	private JLabel lblNota;

	// resumen
	private JPanel pnlResumen;
	private JLabel lblCursos;
	private JLabel lblIng;
	private JLabel lblGas;
	private JLabel lblBal;
	private JLabel valCursos;
	private JLabel valIng;
	private JLabel valGas;
	private JLabel valBal;

	// resultados
	private JPanel pnlResultados;
	private JLabel lblCerrados;
	private JScrollPane spCerrados;
	private JTable tblCerrados;
	private JLabel lblNoCerrados;
	private JScrollPane spNoCerrados;
	private JTable tblNoCerrados;

	public static void main(String[] args) {
		EventQueue.invokeLater(() -> {
			BalanceView frame = new BalanceView();
			frame.setVisible(true);
		});
	}

	public BalanceView() {
		setTitle("COIIPA: Informe de Ingresos y Gastos de Cursos");
		setIconImage(Toolkit.getDefaultToolkit().getImage(BalanceView.class.getResource("/imagenes/logo_COIIPA.png")));
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 1154, 761);
		setContentPane(getContentPaneLazy());

		contentPane.setLayout(new BorderLayout(0, 10));
		contentPane.add(getPnlHeader(), BorderLayout.NORTH);

		JPanel center = new JPanel();
		center.setLayout(new GridBagLayout());
		contentPane.add(center, BorderLayout.CENTER);

		GridBagConstraints gbcCenter = new GridBagConstraints();
		gbcCenter.gridx = 0;
		gbcCenter.gridy = 0;
		gbcCenter.insets = new Insets(0, 0, 10, 0);
		gbcCenter.fill = GridBagConstraints.HORIZONTAL;
		gbcCenter.weightx = 1;
		center.add(getPnlFiltros(), gbcCenter);

		GridBagConstraints gbcCenter2 = new GridBagConstraints();
		gbcCenter2.gridx = 0;
		gbcCenter2.gridy = 1;
		gbcCenter2.insets = new Insets(0, 0, 10, 0);
		gbcCenter2.fill = GridBagConstraints.HORIZONTAL;
		center.add(getPnlResumen(), gbcCenter2);

		GridBagConstraints gbcCenter3 = new GridBagConstraints();
		gbcCenter3.gridx = 0;
		gbcCenter3.gridy = 2;
		gbcCenter3.fill = GridBagConstraints.BOTH;
		gbcCenter3.weightx = 1;
		gbcCenter3.weighty = 1;
		center.add(getPnlResultados(), gbcCenter3);
	}

	/* ---------- lazy ---------- */

	private JPanel getContentPaneLazy() {
		if (contentPane == null) {
			contentPane = new JPanel();
			contentPane.setBorder(new EmptyBorder(10, 10, 10, 10));
		}
		return contentPane;
	}

	private JPanel getPnlHeader() {
		if (pnlHeader == null) {
			pnlHeader = new JPanel();
			pnlHeader.setLayout(new GridBagLayout());

			lblTitulo = new JLabel("Informe de Ingresos y Gastos de Cursos");
			lblTitulo.setFont(lblTitulo.getFont().deriveFont(Font.BOLD, 18f));
			lblSub = new JLabel("Seleccione parámetros y consulte el balance. Todo en una sola ventana.");

			GridBagConstraints gbc1 = new GridBagConstraints();
			gbc1.gridx = 0;
			gbc1.gridy = 0;
			gbc1.anchor = GridBagConstraints.WEST;
			pnlHeader.add(lblTitulo, gbc1);

			GridBagConstraints gbc2 = new GridBagConstraints();
			gbc2.gridx = 0;
			gbc2.gridy = 1;
			gbc2.anchor = GridBagConstraints.WEST;
			gbc2.insets = new Insets(3, 0, 0, 0);
			pnlHeader.add(lblSub, gbc2);
		}
		return pnlHeader;
	}

	private JPanel getPnlFiltros() {
		if (pnlFiltros == null) {
			pnlFiltros = new JPanel();
			pnlFiltros.setBorder(new TitledBorder("Filtros"));
			pnlFiltros.setLayout(new GridBagLayout());

			lblFechaInicio = new JLabel("Fecha inicio");
			lblFechaFin = new JLabel("Fecha fin");
			lblEstado = new JLabel("Estado del curso");

			txtFechaInicio = buildDateField();
			txtFechaFin = buildDateField();

			cboEstado = new JComboBox<>();
			cboEstado.setModel(new DefaultComboBoxModel<>(
					new String[] { "todos", CursoDTO.ESTADO_PLANIFICADO, CursoDTO.ESTADO_PERIODO_INSCRIPCION,
							CursoDTO.ESTADO_INSCRIPCION_CERRADA, CursoDTO.ESTADO_CERRADO, CursoDTO.ESTADO_CANCELADO }));

			pnlBotones = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
			btnConsultar = new JButton("Consultar");
			pnlBotones.add(btnConsultar);

			lblNota = new JLabel("<html><small>Regla de negocio. Cursos cerrados se muestran con totales reales. "
					+ "Cursos no cerrados se muestran aparte con importes estimados y confirmados.</small></html>");

			Insets in = new Insets(2, 2, 2, 8);

			GridBagConstraints g0 = new GridBagConstraints();
			g0.insets = in;
			g0.gridx = 0;
			g0.gridy = 0;
			g0.anchor = GridBagConstraints.WEST;
			pnlFiltros.add(lblFechaInicio, g0);

			GridBagConstraints g1 = new GridBagConstraints();
			g1.insets = in;
			g1.gridx = 1;
			g1.gridy = 0;
			g1.anchor = GridBagConstraints.WEST;
			pnlFiltros.add(lblFechaFin, g1);

			GridBagConstraints g2 = new GridBagConstraints();
			g2.insets = in;
			g2.gridx = 2;
			g2.gridy = 0;
			g2.anchor = GridBagConstraints.WEST;
			pnlFiltros.add(lblEstado, g2);

			GridBagConstraints g3 = new GridBagConstraints();
			g3.insets = in;
			g3.gridx = 0;
			g3.gridy = 1;
			g3.fill = GridBagConstraints.HORIZONTAL;
			g3.weightx = 0.25;
			pnlFiltros.add(txtFechaInicio, g3);

			GridBagConstraints g4 = new GridBagConstraints();
			g4.insets = in;
			g4.gridx = 1;
			g4.gridy = 1;
			g4.fill = GridBagConstraints.HORIZONTAL;
			g4.weightx = 0.25;
			pnlFiltros.add(txtFechaFin, g4);

			GridBagConstraints g5 = new GridBagConstraints();
			g5.insets = in;
			g5.gridx = 2;
			g5.gridy = 1;
			g5.fill = GridBagConstraints.HORIZONTAL;
			g5.weightx = 0.5;
			pnlFiltros.add(cboEstado, g5);

			GridBagConstraints g6 = new GridBagConstraints();
			g6.insets = in;
			g6.gridx = 3;
			g6.gridy = 1;
			g6.anchor = GridBagConstraints.WEST;
			pnlFiltros.add(pnlBotones, g6);

			GridBagConstraints g7 = new GridBagConstraints();
			g7.gridx = 0;
			g7.gridy = 2;
			g7.gridwidth = 4;
			g7.insets = new Insets(6, 2, 2, 2);
			g7.fill = GridBagConstraints.HORIZONTAL;
			pnlFiltros.add(lblNota, g7);

		}
		return pnlFiltros;
	}

	private JPanel getPnlResumen() {
		if (pnlResumen == null) {
			pnlResumen = new JPanel();
			pnlResumen.setBorder(new TitledBorder("Resumen"));
			pnlResumen.setLayout(new GridBagLayout());

			lblCursos = new JLabel("Cursos");
			valCursos = bold("0");
			lblIng = new JLabel("Ingresos");
			valIng = bold("0 €");
			lblGas = new JLabel("Gastos");
			valGas = bold("0 €");
			lblBal = new JLabel("Balance");
			valBal = bold("0 €");

			Insets m = new Insets(2, 12, 2, 12);

			GridBagConstraints r0 = new GridBagConstraints();
			r0.gridx = 0;
			r0.gridy = 0;
			r0.insets = m;
			pnlResumen.add(lblCursos, r0);

			GridBagConstraints r1 = new GridBagConstraints();
			r1.gridx = 1;
			r1.gridy = 0;
			r1.insets = m;
			pnlResumen.add(lblIng, r1);

			GridBagConstraints r2 = new GridBagConstraints();
			r2.gridx = 2;
			r2.gridy = 0;
			r2.insets = m;
			pnlResumen.add(lblGas, r2);

			GridBagConstraints r3 = new GridBagConstraints();
			r3.gridx = 3;
			r3.gridy = 0;
			r3.insets = m;
			pnlResumen.add(lblBal, r3);

			GridBagConstraints r4 = new GridBagConstraints();
			r4.gridx = 0;
			r4.gridy = 1;
			r4.insets = m;
			pnlResumen.add(valCursos, r4);

			GridBagConstraints r5 = new GridBagConstraints();
			r5.gridx = 1;
			r5.gridy = 1;
			r5.insets = m;
			pnlResumen.add(valIng, r5);

			GridBagConstraints r6 = new GridBagConstraints();
			r6.gridx = 2;
			r6.gridy = 1;
			r6.insets = m;
			pnlResumen.add(valGas, r6);

			GridBagConstraints r7 = new GridBagConstraints();
			r7.gridx = 3;
			r7.gridy = 1;
			r7.insets = m;
			pnlResumen.add(valBal, r7);
		}
		return pnlResumen;
	}

	private JPanel getPnlResultados() {
		if (pnlResultados == null) {
			pnlResultados = new JPanel();
			pnlResultados.setBorder(new TitledBorder("Resultados"));
			pnlResultados.setLayout(new GridBagLayout());

			lblCerrados = new JLabel("Cursos cerrados");
			tblCerrados = new JTable(new DefaultTableModel(new Object[][] {},
					new String[] { "Fecha", "Nombre", "Estado", "Total ingresos", "Total gastos", "Balance" }) {
				public boolean isCellEditable(int r, int c) {
					return false;
				}
			});
			spCerrados = new JScrollPane(tblCerrados);

			lblNoCerrados = new JLabel("Cursos no cerrados");
			tblNoCerrados = new JTable(new DefaultTableModel(new Object[][] {},
					new String[] { "Fecha", "Nombre", "Estado", "Ingresos estimados", "Ingresos confirmados",
							"Gastos estimados", "Gastos confirmados", "Balance estimado", "Balance confirmado" }) {
				public boolean isCellEditable(int r, int c) {
					return false;
				}
			});
			spNoCerrados = new JScrollPane(tblNoCerrados);

			GridBagConstraints s0 = new GridBagConstraints();
			s0.gridx = 0;
			s0.gridy = 0;
			s0.anchor = GridBagConstraints.WEST;
			s0.insets = new Insets(2, 2, 6, 2);
			pnlResultados.add(lblCerrados, s0);

			GridBagConstraints s1 = new GridBagConstraints();
			s1.gridx = 0;
			s1.gridy = 1;
			s1.fill = GridBagConstraints.BOTH;
			s1.weightx = 1;
			s1.weighty = 0.5;
			s1.insets = new Insets(2, 2, 2, 2);
			pnlResultados.add(spCerrados, s1);

			GridBagConstraints s2 = new GridBagConstraints();
			s2.gridx = 0;
			s2.gridy = 2;
			s2.anchor = GridBagConstraints.WEST;
			s2.insets = new Insets(10, 2, 6, 2);
			pnlResultados.add(lblNoCerrados, s2);

			GridBagConstraints s3 = new GridBagConstraints();
			s3.gridx = 0;
			s3.gridy = 3;
			s3.fill = GridBagConstraints.BOTH;
			s3.weightx = 1;
			s3.weighty = 0.5;
			s3.insets = new Insets(2, 2, 2, 2);
			pnlResultados.add(spNoCerrados, s3);
		}
		return pnlResultados;
	}

	/* ---------- utilidades ---------- */

	private static JFormattedTextField buildDateField() {
		try {
			MaskFormatter mf = new MaskFormatter("####-##-##");
			mf.setPlaceholderCharacter('_');
			return new JFormattedTextField(mf);
		} catch (Exception ex) {
			return new JFormattedTextField();
		}
	}

	private static JLabel bold(String text) {
		JLabel l = new JLabel(text);
		l.setFont(l.getFont().deriveFont(Font.BOLD, 16f));
		return l;
	}

	/* ---------- getters públicos ---------- */

	public JFormattedTextField getTxtFechaInicio() {
		return txtFechaInicio;
	}

	public JFormattedTextField getTxtFechaFin() {
		return txtFechaFin;
	}

	public JComboBox<String> getCboEstado() {
		return cboEstado;
	}

	public JButton getBtnConsultar() {
		return btnConsultar;
	}

	public JLabel getValCursos() {
		return valCursos;
	}

	public JLabel getValIng() {
		return valIng;
	}

	public JLabel getValGas() {
		return valGas;
	}

	public JLabel getValBal() {
		return valBal;
	}

	public JTable getTblCerrados() {
		return tblCerrados;
	}

	public JTable getTblNoCerrados() {
		return tblNoCerrados;
	}
}
