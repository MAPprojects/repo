package entities;

public class Cronos {
    private static int saptamanActuala = 8;
    private static Cronos INSTANCE = new Cronos();

    private Cronos() {
    }

    public static Cronos getInstance() {
        return INSTANCE;
    }

    public static void setSaptamanaActuala(int value) {
        saptamanActuala = value;
    }

    public static int getSaptamanActuala() {
        return saptamanActuala;
    }

}
