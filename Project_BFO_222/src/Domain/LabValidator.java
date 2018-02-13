package Domain;

public class LabValidator implements Validator<Tema> {
    public void validate(Tema t) {
        if (t.getId() < 0) {
            throw new EntitiesException("Numarul temei este invalid!");
        }
        if (t.getDeadline() > 14) {
            throw new EntitiesException("Acest deadline e" +
                    "+-ste in afara anului universitar!");
        }
    }
}