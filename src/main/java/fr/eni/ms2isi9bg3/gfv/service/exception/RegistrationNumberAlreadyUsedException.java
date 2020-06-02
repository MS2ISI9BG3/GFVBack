package fr.eni.ms2isi9bg3.gfv.service.exception;

public class RegistrationNumberAlreadyUsedException extends RuntimeException {

    public RegistrationNumberAlreadyUsedException(){
        super("Registration Number is already in use!");
    }
}
