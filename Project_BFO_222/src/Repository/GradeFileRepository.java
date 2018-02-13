package Repository;
import Domain.*;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;

public class GradeFileRepository extends AbstractFileRepository<Nota,Integer>{
    private String numeFis;
    public GradeFileRepository(String numeFisier, Validator<Nota> v){
        super(numeFisier,v);
    }

    @Override
    protected void loading(){
        try(Stream<String> s = Files.lines(Paths.get(fileName))){
            s.forEach(x-> {
                String[] str = x.split("[|]");
                if (str.length != 4) {
                    System.out.println("Linie invalida: " + x);
                }
                try {
                    int nr = Integer.parseInt(str[0]);
                    int ids = Integer.parseInt(str[1]);
                    int idl = Integer.parseInt(str[2]);
                    int grade = Integer.parseInt(str[3]);
                    Nota n = new Nota(nr, ids, idl, grade);
                    super.save(n);
                } catch (NumberFormatException n) {
                    System.err.println(n);
                }
            });
        }catch (IOException ioe){
            System.err.println(ioe);
        }
    }

    @Override
    protected void writeToFile(){
        try(PrintWriter pw=new PrintWriter("note.txt")){
            for(Nota grade:findAll()){
                String gr=""+grade.getId()+"|"+grade.getStud()+"|"+grade.getTema()+"|"+grade.getGrade();
                pw.println(gr);
            }
        }catch (IOException ioe){
            System.err.println(ioe);
        }
    }
}
