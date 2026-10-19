package giis.demo.tkrun.Models;

import java.util.List;

import giis.demo.tkrun.DTO.CursoDTO;
import giis.demo.tkrun.DTO.ProfesorDTO;
import giis.demo.util.Database;

public class CreacionCursoModel {

	private Database dataBase;
	private String sqlGetProfesores = "Select * from persona p inner join profesor pr where p.id = pr.id_profesor";
	private String sqlGetCursos = "Select * from curso";
	private String sqlIntroducirCurso = "INSERT INTO curso ( " + "    nombre, " + "    inicio_plazo_inscripcion, "
			+ "    fin_plazo_inscripcion, " + "    fecha_inicio, " + "    fecha_fin, " + "    lugar, "
			+ "    num_plazas, estado_curso" + ")" + "VALUES ( " + "   ?,?,?,?,?,?,?,?,?); ";
	private String sqlAsignarProfesor = "INSERT INTO actividad_profesor (id_curso, profesor_id, remuneracion)"
			+ "VALUES(?,?,?)";

	public CreacionCursoModel() {
		dataBase = new Database();
	}

	public List<ProfesorDTO> getProfesores() {
		return dataBase.executeQueryPojo(ProfesorDTO.class, sqlGetProfesores);
	}

	public List<CursoDTO> getCursos() {
		return dataBase.executeQueryPojo(CursoDTO.class, sqlGetCursos);
	}

	public void introducirCurso(CursoDTO curso) {
		dataBase.executeUpdate(sqlIntroducirCurso, curso.getNombre(), curso.getInicio_plazo_inscripcion(),
				curso.getFin_plazo_inscripcion(), curso.getFecha_inicio(), curso.getFecha_fin(), curso.getLugar(),
				curso.getNum_plazas(), curso.getEstado_curso());

	}

	public void asignarProfesor(CursoDTO curso) {
		dataBase.executeUpdate(sqlAsignarProfesor, curso.getId_curso(), curso.getProfesor().getId(),
				curso.getRemuneracion());
	}

}
