package fr.eni.ms2isi9bg3.gfv.web.rest.vm;

import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * View Model object for storing a user's credentials.
 */
@Data
public class LoginVM {

    @NotNull
    @Size(min = 1, max = 50)
    private String login;

    @NotNull
    @Size(min = 4, max = 100)
    private String password;

    @Override
    public String toString() {
        return "LoginVM{" +
            "username='" + login +
            '}';
    }
}
