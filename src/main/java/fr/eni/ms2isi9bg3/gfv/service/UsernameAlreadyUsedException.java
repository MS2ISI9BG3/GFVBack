package fr.eni.ms2isi9bg3.gfv.service;

public class UsernameAlreadyUsedException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public UsernameAlreadyUsedException() {
		super("Login name already used!");
	}

}
