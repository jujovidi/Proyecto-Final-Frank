package com.CronoEdu.horario.infraestructure.mapper;

import com.CronoEdu.horario.domine.model.Horario;
import com.CronoEdu.horario.domine.model.Materia;
import com.CronoEdu.horario.infraestructure.driver_adapters.jpa_repository.HorarioData;
import com.CronoEdu.horario.infraestructure.driver_adapters.jpa_repository.MateriaData;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class MateriaMapper {

    public MateriaData toMateriaData(Materia materia) {
        if (materia == null) {
            return null;
        }
        MateriaData data = new MateriaData();
        data.setId(materia.getId());
        data.setNombre(materia.getNombre());
        data.setDocente(materia.getDocente());
        data.setSalon(materia.getSalon());

        if (materia.getHorarios() != null) {
            List<HorarioData> horariosData = new ArrayList<>();
            for (Horario horario : materia.getHorarios()) {
                HorarioData hd = new HorarioData();
                hd.setId(horario.getId());
                hd.setDiaSemana(horario.getDiaSemana());
                hd.setHoraInicio(horario.getHoraInicio());
                hd.setHoraFin(horario.getHoraFin());
                hd.setMateria(data);
                horariosData.add(hd);
            }
            data.setHorarios(horariosData);
        }

        return data;
    }

    public Materia toMateria(MateriaData materiaData) {
        if (materiaData == null) {
            return null;
        }
        Materia materia = new Materia();
        materia.setId(materiaData.getId());
        materia.setNombre(materiaData.getNombre());
        materia.setDocente(materiaData.getDocente());
        materia.setSalon(materiaData.getSalon());

        if (materiaData.getHorarios() != null) {
            List<Horario> horarios = new ArrayList<>();
            for (HorarioData hd : materiaData.getHorarios()) {
                Horario h = new Horario();
                h.setId(hd.getId());
                h.setDiaSemana(hd.getDiaSemana());
                h.setHoraInicio(hd.getHoraInicio());
                h.setHoraFin(hd.getHoraFin());
                horarios.add(h);
            }
            materia.setHorarios(horarios);
        }

        return materia;
    }

}
