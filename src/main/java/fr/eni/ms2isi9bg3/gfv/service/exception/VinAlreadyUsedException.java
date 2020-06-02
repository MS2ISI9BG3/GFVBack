package fr.eni.ms2isi9bg3.gfv.service.exception;

public class VinAlreadyUsedException extends RuntimeException {

    public VinAlreadyUsedException(){
        super("VIN is already in use!");
    }
}
