//package repository.fileRepo;
//
//import entities.Nota;
//import entities.NotaPk;
//import entities.Tema;
//import repository.AbstractCrudRepo;
//
//import java.io.*;
//import java.nio.file.Files;
//import java.nio.file.Path;
//import java.nio.file.Paths;
//import java.util.ArrayList;
//import java.util.Collection;
//import java.util.Optional;
//import java.util.stream.Stream;
//
//public class NotaFileRepo extends AbstractCrudRepo<Nota, NotaPk> {
//
//    private String fileName;
//
//    public NotaFileRepo(String fileName) throws IOException {
//        super(Nota.class);
//        this.fileName = fileName;
//        loadData();
//    }
//
//    @Override
//    public Optional<Nota> save(Nota entity) throws IOException {
//        super.save(entity);
//        saveData();
//        return Optional.ofNullable(entity);
//    }
//
//    @Override
//    public Optional<Nota> delete(String id) throws IOException {
//        Nota n = super.delete(id).get();
//        saveData();
//        return Optional.ofNullable(n);
//    }
//
//    @Override
//    public Optional<Nota> update(String id, Nota entity) throws IOException {
//        super.update(id, entity);
//        saveData();
//        return Optional.ofNullable(entity);
//    }
//
//    @Override
//    public void populate(Collection<Nota> elements) throws IOException {
//        super.populate(elements);
//    }
//
////    @Override
////    public void loadData() throws IOException {
////        Path path = Paths.get(fileName);
////        BufferedReader br = new BufferedReader(new FileReader(this.fileName));
////        Stream<String> lines;
////        lines = Files.lines(path);
////        lines.forEach(line -> {
////            try {
////                Tema[] lineF = line.split(",");
////                super.save(new Nota(lineF[0], lineF[1], Integer.parseInt(lineF[2])));
////            } catch (IOException e) {
////                e.printStackTrace();
////            }
////        });
//////        String line = br.readLine();
//////        while (line != null) {
//////            String[] lineF = line.split(",");
//////            super.save(new Nota(Integer.parseInt(lineF[0]), Integer.parseInt(lineF[1]), Integer.parseInt(lineF[2])));
//////            line = br.readLine();
//////        }
////        br.close();
////    }
//
////    @Override
////    public void saveData() throws IOException {
////        BufferedWriter bw = new BufferedWriter(new FileWriter(this.fileName));
////        bw.flush();
////        ArrayList<Nota> note = new ArrayList<Nota>((Collection<? extends Nota>) super.findAll());
////        for (Nota n : note) {
////            String line = n.getIdStudent() + "," + n.getNrTemei() + "," + n.getValoare() + "\n";
////            bw.write(line);
////        }
////        bw.close();
////    }
//}
