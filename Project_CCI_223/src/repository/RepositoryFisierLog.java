package repository;

import domain.DetaliiLog;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class RepositoryFisierLog {

    public RepositoryFisierLog() {
    }

    public List<DetaliiLog> incarcaFisierLog(Integer idStudent) throws FileNotFoundException {
        List<DetaliiLog> list=new ArrayList<>();
        String fisier="D:\\MAP\\GestiuneStudentiLaboratareNote\\src\\repository\\"+idStudent+".txt";
        try(BufferedReader in=new BufferedReader(new FileReader(fisier))){
            String line;
            while ((line=in.readLine())!=null){
                String[] fields=line.split(";");
                DetaliiLog entity=buildEntity(fields);
                list.add(entity);
            }
        } catch (FileNotFoundException e) {
            System.out.println("Nu s-a gasit fisierul de log pentru studentul dat");
            throw e;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return list;
    }

    private DetaliiLog buildEntity(String[] fields) {
        DetaliiLog detaliiLog=new DetaliiLog(fields[0],Integer.parseInt(fields[1]),Float.parseFloat(fields[2]),Integer.parseInt(fields[3]),Integer.parseInt(fields[4]),"DA","DA");
        if (fields[5].equals("fara intarziere")) detaliiLog.setIntarzieri("NU");
        if (fields[6].equals("fara greseli")) detaliiLog.setGreseli("NU");
        return detaliiLog;
    }
}
