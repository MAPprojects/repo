package Utils;


import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;

/**
 * Used to send a fake email(an email sored into a file)
 */

public class EmailSender {

    private static  final String location = "C:\\Users\\Onu Edy\\Desktop\\225_Onu_Eduard_Gabriel\\src\\Emails\\";
    private static  EmailSender instance = new EmailSender();

    /**
     *
     * @param which the file in which the info will be saved
     * @param what  what info it will be saved into that file
     * @throws IOException  if the file not exists it will automatically create one ,this exception is thrown when the writer could not write from that file
     */

    private void writeIntoFile(String which,String what) throws  IOException{

        Path p = Paths.get(which);

        try{ Files.lines(p); }catch (IOException e){ Files.createFile(p); }

        BufferedWriter cout = Files.newBufferedWriter(p, StandardOpenOption.APPEND);

        cout.write(what);

        cout.close();
    }

    /**
     *
     * @param which the file in which the info will be saved
     * @throws IOException  if the file not exists it will automatically create one ,this exception is thrown when the writer could not write from that file
     */

    private List < String > readFromFile(String which) throws  IOException{
        List < String > l = new ArrayList<>();
        Path p = Paths.get(which);
        Files.lines(p).forEach(s->l.add(s + '\n'));
        return l;
    }

    /**
     *
     * @param addr write into file with this name
     * @param content   this information
     * @throws IOException  and throws this exception if an error occures
     */

    public void sendTo(String addr,String content) throws IOException{
        writeIntoFile(location + addr,content);
    }

    /**
     *
     * @param addr the addres from which to read informations(the file with that name)
     * @return  a list formed from all lines from that file
     * @throws IOException
     */

    public List< String > getAll(String addr) throws  IOException{
        return readFromFile(location + addr);
    }

    /**
     *
     * @return due to fact that EmailSender is a singleton class it needs a function to get an instance
     */

    public  static EmailSender getInstance(){
        return instance;
    }
}
