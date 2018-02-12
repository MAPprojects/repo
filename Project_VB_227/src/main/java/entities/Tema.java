package entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "TEMA")
public class Tema implements HasId<String> {
    @Id
    @Column(name = "ID_TEMA")
    private String idTema;
    @Column(name = "CERINTA")
    private String cerinta;
    @Column(name = "TERMEN_PREDARE")
    private Integer termenPredare;

    public Tema() {
    }

    /**
     * Constructor al clasei Tema
     * @param idTema Integer
     * @param cerinta String
     * @param termenPredare int -> Saptamana din semestru in care trebuie predata(1-14)
     */
    public Tema(String idTema, String cerinta, int termenPredare) {

        this.idTema = idTema;
        this.cerinta = cerinta;
        this.termenPredare = termenPredare;
    }

    /**
     * Getter
     * @return Integer -> id-ul temei
     */
    public String getId() {
        return this.idTema;
    }

    /**
     * Setter
     * @param id Integer -> id-ul temei
     */
    public void setId(String id) {
        this.idTema = id;
    }

    /**
     * Getter
     * @return String -> cerina Temei
     */
    public String getCerinta() {
        return cerinta;
    }

    /**
     * Setter
     * @param cerinta String -> cerinta problemei
     */
    public void setCerinta(String cerinta) {
        this.cerinta = cerinta;
    }

    public static int compareToId(Tema t1, Tema t2) {
        return t1.getId().compareTo(t2.getId());
    }

    /**
     * Getter
     * @return int -> Sapatamana in care trebuie predata tema
     */
    public Integer getTermenPredare() {
        return termenPredare;
    }

    @Override
    public String toString() {
        return "Cerinta: " + cerinta;
    }

    /**
     * Setter
     *
     * @param termenPredare int -> Sapatamana in care trebuie predata tema
     */
    public void setTermenPredare(Integer termenPredare) {
        this.termenPredare = termenPredare;
    }
}
