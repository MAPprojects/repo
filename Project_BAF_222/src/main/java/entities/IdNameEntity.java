package entities;



public abstract class IdNameEntity<ID> implements HasID<ID> {
    private ID id;
    private String nume;

    public IdNameEntity(ID id,String nume){
        this.id=id;
        this.nume=nume;
    }

    @Override
    public void setID(ID id) {
        this.id=id;
    }

    @Override
    public ID getID() {
        return id;
    }

    public String getNume() {
        return nume;
    }

    public void setNume(String nume) {
        this.nume = nume;
    }

    @Override
    public String toString() {
        return nume;
    }
}
