package exceptii;

import java.util.ArrayList;

public class ValidationException extends Exception{

    private ArrayList<String> listaMesaje;

    /**
     * contructor
     * @param message String
     */
    public ValidationException(String message, ArrayList<String> mesaje){
        super(message);
        listaMesaje=mesaje;
    }

    /**
     * Returns the list of the error messages
     * @return ArrayList<String>
     */
    public ArrayList<String> getMesaje(){
        return listaMesaje;
    }

    @Override
    public String toString() {
        return "exceptii.ValidationException{" +
                "listaMesaje=" + listaMesaje +
                '}';
    }
}
