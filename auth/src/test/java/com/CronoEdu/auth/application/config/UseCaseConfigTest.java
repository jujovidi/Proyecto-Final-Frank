package com.CronoEdu.auth.application.config;

import com.CronoEdu.auth.domain.model.gateway.EncrypterGateway;
import com.CronoEdu.auth.domain.model.gateway.NotificationGateway;
import com.CronoEdu.auth.domain.model.gateway.UsuarioGateway;
import com.CronoEdu.auth.domain.usecase.UsuarioUseCase;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class UseCaseConfigTest {

    @Mock
    private UsuarioGateway usuarioGateway;

    @Mock
    private EncrypterGateway encrypterGateway;

    @Mock
    private NotificationGateway notificationGateway;

    private final UseCaseConfig config = new UseCaseConfig();

    @Test
    void usuarioUseCase_DeberiaCrearBean() {
        UsuarioUseCase useCase = config.usuarioUseCase(usuarioGateway, encrypterGateway, notificationGateway);
        assertNotNull(useCase);
    }
}
