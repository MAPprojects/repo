package Repository;
import Domain.Tema;
import Domain.Validator;

import javax.swing.text.html.Option;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class LabFileRepository extends AbstractFileRepository<Tema,Integer> {
    public LabFileRepository(String numeFisier, Validator<Tema> v){
        super(numeFisier,v);
    }

//    @Override
//    protected void loading(){
//        try(BufferedReader br=new BufferedReader(new FileReader(fileName))){
//            String linie;
//            while((linie=br.readLine())!=null) {
//                String[] s= linie.split("[|]");
//                if(s.length!=3) {
//                    System.out.println("Linie invalida "+linie);
//                    continue;
//                }
//                try{
//                    int nr=Integer.parseInt(s[0]);
//                    int dead=Integer.parseInt(s[2]);
//                    Tema t=new Tema(nr,s[1],dead);
//                    save(t);
//                }catch (NumberFormatException e){
//                    System.err.println(e);
//                }catch (RepositoryException re){
//                    System.out.println(re);
//                }
//            }
//        }catch (IOException ioe){
//            System.err.println(ioe);
//        }
//    }

    @Override
    protected void loading() {
        try (Stream<String> s = Files.lines(Paths.get(fileName))) {
            s.forEach(x -> {
                String[] str = x.split("[|]");
                if (str.length != 3) {
                    System.out.println("Linie invalida: " + x);
                } else {
                    try {
                        int nr = Integer.parseInt(str[0]);
                        int dead = Integer.parseInt(str[2]);
                        super.save(new Tema(nr, str[1], dead));
                    } catch (NumberFormatException e) {
                        System.out.println(e);
                    } catch (RepositoryException re) {
                        System.out.println(re);
                    }
                }
            });
        } catch (IOException ioe) {
            System.out.println(ioe);

        }
    }


    @Override
    protected void writeToFile() {
        try (PrintWriter pw = new PrintWriter(fileName)) {
            for (Tema t : findAll()) {
                String tf = "" + t.getId() + "|" + t.getDescriere() + "|" + t.getDeadline();
                pw.println(tf);
            }
        } catch (IOException ioe) {
            System.err.println(ioe);
        }
    }
}
