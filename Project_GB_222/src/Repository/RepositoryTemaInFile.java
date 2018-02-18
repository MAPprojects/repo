package Repository;

import Domain.Tema;
import ExceptionsAndValidators.IValidator;

import java.io.*;

public class RepositoryTemaInFile extends AbstractFileRepository<Tema,Integer>
{
    String fileName;
    public RepositoryTemaInFile(String fileName, IValidator<Tema> validator)
    {
        super(fileName,validator);
//        this.fileName = fileName;
//        load();
    }

    @Override
    public void createObject(String s) {
        String[] valori = s.split("[|]");
        if(valori.length!=3) {
            System.err.println("Linie invalida: " + s);
            return;
        }
        try {
            Integer nrTema = Integer.parseInt(valori[0]);
            String descriere = valori[1];
            int deadline = Integer.parseInt(valori[2]);
            Tema tema = new Tema(nrTema,descriere,deadline);
            super.add(tema);
        }
        catch (NumberFormatException err) {
            System.err.println(err);
        }
    }

    @Override
    public void writeObject(BufferedWriter writer,Tema tema)
    {
        try
        {
            writer.write(""+ tema.getID() + "|" + tema.getDescriere().replace("\n","//newLine//") + "|" + tema.getDeadline() +"\n");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

//    private void load() {
//        try (BufferedReader reader = new BufferedReader(new FileReader(fileName)))
//        {
//            String linie;
//            while((linie=reader.readLine())!=null)
//            {
//                String[] valori = linie.split("[|]");
//                if(valori.length != 3)
//                {
//                    System.err.println("Linie invalida: "+linie);
//                    continue;
//                }
//                try
//                {
//                    Integer nrTema = Integer.parseInt(valori[0]);
//                    String descriere = valori[1];
//                    int deadline = Integer.parseInt(valori[2]);
//                    Tema tema = new Tema(nrTema,descriere,deadline);
//                    super.add(tema);
//
//                }
//                catch (NumberFormatException err)
//                {
//                    System.err.println(err);
//                }
//            }
//        }
//        catch (FileNotFoundException e)
//        {
//            e.printStackTrace();
//        }
//        catch (IOException e)
//        {
//            e.printStackTrace();
//        }
//    }

//    private void writeToFile()
//    {
//        try(BufferedWriter writer = new BufferedWriter(new FileWriter(fileName)))
//        {
//            for(Tema tema : getAll())
//            {
//                writer.write(""+ tema.getID() + "|" + tema.getDescriere() + "|" + tema.getDeadline() +"\n");
//            }
//        }
//        catch (IOException e)
//        {
//            e.printStackTrace();
//        }
//    }

//    @Override
//    public void add(Tema tema)
//    {
//        super.add(tema);
//        writeToFile();
//    }
//
//    @Override
//    public Tema delete(Integer id)
//    {
//        Tema temp = super.delete(id);
//        writeToFile();
//        return temp;
//    }
//
//    @Override
//    public void update(Tema tema)
//    {
//        super.update(tema);
//        writeToFile();
//    }


}
