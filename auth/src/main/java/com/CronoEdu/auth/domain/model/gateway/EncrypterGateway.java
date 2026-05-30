package com.CronoEdu.auth.domain.model.gateway;

public interface EncrypterGateway {

    String encrypt(String password);

    Boolean checkPass(String passUser, String passBD);
}
