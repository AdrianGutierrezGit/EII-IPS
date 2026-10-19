package giis.demo.tkrun.Views;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Toolkit;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.Font;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

public class CerrarCursosView extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel pnCerrarCurso;
	private JScrollPane scpCursos;
	private JButton btCerrarCurso;
	private JTable tablaCursos;
	private JLabel lbSeleccionarCurso;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					CerrarCursosView frame = new CerrarCursosView();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public CerrarCursosView() {
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setIconImage(
				Toolkit.getDefaultToolkit().getImage(CerrarCursosView.class.getResource("/imagenes/logo_COIIPA.png")));
		setTitle("COIIPA: Cerrar cursos");
		setResizable(false);
		setBounds(100, 100, 660, 399);
		pnCerrarCurso = new JPanel();
		pnCerrarCurso.setBackground(new Color(255, 255, 255));
		pnCerrarCurso.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(pnCerrarCurso);
		pnCerrarCurso.setLayout(null);
		pnCerrarCurso.add(getScpCursos());
		pnCerrarCurso.add(getBtCerrarCurso());
		pnCerrarCurso.add(getLbSeleccionarCurso());

	}

	private JScrollPane getScpCursos() {
		if (scpCursos == null) {
			scpCursos = new JScrollPane();
			scpCursos.setBackground(new Color(255, 255, 255));
			scpCursos.setBorder(new LineBorder(new Color(130, 135, 144)));
			scpCursos.setBounds(24, 42, 585, 223);
			scpCursos.setViewportView(getTablaCursos());
			scpCursos.getViewport().setBackground(new Color(255, 255, 255));
		}
		return scpCursos;
	}

	public void actualizarTablaCursos(DefaultTableModel modeloTablaCursos) {
		tablaCursos.setModel(modeloTablaCursos);
		getScpCursos().revalidate();
		getScpCursos().repaint();

	}

	public JButton getBtCerrarCurso() {
		if (btCerrarCurso == null) {
			btCerrarCurso = new JButton("Cerrar curso");
			btCerrarCurso.setFont(new Font("Tahoma", Font.PLAIN, 12));
			btCerrarCurso.setEnabled(false);
			btCerrarCurso.setBounds(487, 302, 122, 37);
		}
		return btCerrarCurso;
	}

	public JTable getTablaCursos() {
		if (tablaCursos == null) {
			tablaCursos = new JTable();
			tablaCursos.setBackground(new Color(255, 255, 255));
		}
		return tablaCursos;
	}

	private JLabel getLbSeleccionarCurso() {
		if (lbSeleccionarCurso == null) {
			lbSeleccionarCurso = new JLabel("Seleccione el curso que quiere cerrar:");
			lbSeleccionarCurso.setForeground(new Color(0, 0, 0));
			lbSeleccionarCurso.setFont(new Font("Tahoma", Font.BOLD, 12));
			lbSeleccionarCurso.setBounds(24, 19, 239, 13);
		}
		return lbSeleccionarCurso;
	}

	public void mostrarImpedimentosParaCerrarCurso(String msg) {
		JOptionPane.showMessageDialog(this, msg, "Cerrar cursos: errores al cerrar el curso",
				JOptionPane.ERROR_MESSAGE);

	}

	public int mostrarAdvertenciasParaCerrarCursos(String msg) {
		return JOptionPane.showConfirmDialog(this, msg + "\n¿Quiere cerrar el curso de todos modos?",
				"Cerrar cursos: confirmación de cierre de curso", JOptionPane.YES_OPTION, JOptionPane.NO_OPTION);
	}

	public void mostrarMensajeCursoCerrado() {
		JOptionPane.showMessageDialog(null, "Curso cerrado correctamente", "Cerrar cursos: cierre del curso",
				JOptionPane.INFORMATION_MESSAGE);
	}
}