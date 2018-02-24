package UserInterface;

import Domain.Command;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

public class MenuCommand implements Command {

    private Map<String, Command> dict = new TreeMap<>();
    private String numeMeniu;

    public MenuCommand(String numeMeniu) {
        this.numeMeniu = numeMeniu;
    }

    @Override
    public void execute() {

        dict.keySet().forEach(x -> System.out.println(x));

    }

    public void addCommand(String descriere, Command c){
        dict.put(descriere, c);
    }

    public int size(){
        return dict.size();
    }

    public List<Command> getAllCommands(){
        return dict.values().stream().collect(Collectors.toList());
    }

    public String getMenuNume(){

        return numeMeniu;
    }
}
