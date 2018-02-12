package entities;

import javax.persistence.*;

@Entity
@Table(name = "SYSTEM_CONFIGURATION")
public class SystemConfiguration implements HasId<String> {
    @Id
    @Column(name = "ID")
    private String id = "1";
    @Column(name = "ENABLE_AUTHENTICATION")
    private boolean enableAuthentication;
    @Column(name = "ROLE_OF_CURRENT_USER")
    private Role currentUserRol;

    public static String encryptHash = "35454B055CC325EA1AF2126E27707052";

    public SystemConfiguration() {
    }

    public boolean getEnableAuthentication() {

        return enableAuthentication;
    }

    public void setEnableAuthentication(boolean enableOrDisable) {
        enableAuthentication = enableOrDisable;
    }


    @Override
    public String getId() {
        return id;
    }

    @Override
    public void setId(String id) {
        this.id = id;
    }

    public Role getCurrentUserRol() {
        return currentUserRol;
    }

    public void setCurrentUserRol(Role currentUserRol) {
        this.currentUserRol = currentUserRol;
    }
}
