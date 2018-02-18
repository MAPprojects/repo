package Domain;

import java.io.Serializable;

public interface HasID<ID> extends Serializable{
    ID getId();
    void setId(ID id);
}
