package fr.eni.ms2isi9bg3.gfv.config;

/**
 * Application constants.
 */
public final class Constants {

	// Regex for acceptable logins
	public static final String LOGIN_REGEX = "^[_.@A-Za-z0-9-]*$";

	public static final String SYSTEM_ACCOUNT = "system";
	public static final String DEFAULT_LANGUAGE = "en";
	public static final String ANONYMOUS_USER = "anonymousUser";

	public static final String PHONE_NUMBER_REGEX = "(\\+33|0)[0-9]{9}";

	private Constants() {
	}
}
