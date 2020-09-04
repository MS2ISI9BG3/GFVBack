package fr.eni.ms2isi9bg3.gfv.domain;

import lombok.Data;

@Data
public class Response {
    private String userLogin;
    private String message;

    public Response(String login) {
        this.userLogin = login;
        // TODO set the msg in message properties
        this.message = "Votre compte a été activé";
    }
}
