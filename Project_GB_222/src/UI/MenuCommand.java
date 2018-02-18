package UI;

import java.util.List;
import java.util.TreeMap;
import java.util.stream.Collectors;

public class MenuCommand implements ICommand
{


    String nume;
    TreeMap<String,ICommand> comenzi;

    public MenuCommand(String nume) {
        this.nume = nume;
        this.comenzi = new TreeMap<String,ICommand>();
    }

    @Override
    public void execute()
    {
        comenzi.keySet().forEach(System.out::println);
    }

    public void addCommand(String s,ICommand c)
    {
        comenzi.put(s,c);
    }

    public String getMenuName() {
        return nume;
    }

    public List<ICommand> getCommands()
    {
        return comenzi.values().stream().collect(Collectors.toList());
    }
}
