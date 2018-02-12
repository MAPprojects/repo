package entities;

import org.w3c.dom.events.Event;

import javax.persistence.*;

@Entity
@Table(name = "SYSTEM_USER")
public class SystemUser implements HasId<String> {
    @Id
    @Column(name = "EMAIL")
    private String email;
    @Column(name = "PASSWORD")
    private byte[] password;
    @Column(name = "ROL")
    private Role rol;

    public SystemUser(String email, byte[] password, Role rol) {
        this.email = email;
        this.password = password;
        this.rol = rol;
    }

    public SystemUser() {
    }

    public byte[] getPassword() {
        return password;
    }

    public void setPassword(byte[] password) {
        this.password = password;
    }

    public Role getRol() {
        return rol;
    }

    public void setRol(Role rol) {
        this.rol = rol;
    }

    @Override
    public String getId() {
        return this.email;
    }

    @Override
    public void setId(String id) {
        this.email = email;
    }
}
