package com.CronoEdu.horario.application.config;

import com.CronoEdu.horario.domine.model.gateway.MateriaGateway;
import com.CronoEdu.horario.domine.usecase.MateriaUsecase;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class UseCaseConfigTest {

    @Mock
    private MateriaGateway materiaGateway;

    private final UseCaseConfig config = new UseCaseConfig();

    @Test
    void materiaUsecase_DeberiaCrearBean() {
        MateriaUsecase useCase = config.materiaUsecase(materiaGateway);
        assertNotNull(useCase);
    }
}
