package com.CronoEdu.auth.infraestructure.security;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EncrypterGatewayImplTest {

    private final EncrypterGatewayImpl encrypter = new EncrypterGatewayImpl();

    @Test
    void encrypt_DeberiaEncriptarPassword() {
        String encrypted = encrypter.encrypt("password123");

        assertNotNull(encrypted);
        assertNotEquals("password123", encrypted);
        assertTrue(encrypted.startsWith("$2a$"));
    }

    @Test
    void checkPass_DeberiaRetornarTrue_CuandoCoincide() {
        String encrypted = encrypter.encrypt("password123");

        assertTrue(encrypter.checkPass("password123", encrypted));
    }

    @Test
    void checkPass_DeberiaRetornarFalse_CuandoNoCoincide() {
        String encrypted = encrypter.encrypt("password123");

        assertFalse(encrypter.checkPass("wrongpass", encrypted));
    }

    @Test
    void checkPass_DeberiaRetornarFalse_CuandoPassBDTieneOtroFormato() {
        assertFalse(encrypter.checkPass("cualquier", "formatoInvalido"));
    }
}
