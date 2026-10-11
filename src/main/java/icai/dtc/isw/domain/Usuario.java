package icai.dtc.isw.domain;

import java.io.Serial;
import java.io.Serializable;

public class Usuario implements Serializable {
    // Serializable permite convertir una instancia de Usuario en un flujo de bytes
    @Serial
    private static final long serialVersionUID = 1L;

    private int id;
    private String email;
    private String password; // Hash BCrypt

    public Usuario() {}

    // Constructor para registros
    public Usuario(String email, String password) {
        this.email = email;
        this.password = password;
    }

    // Constructor para recuperar los datos o iniciar sesion
    public Usuario(int id, String email, String password) {
        this.id = id;
        this.email = email;
        this.password = password;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
}