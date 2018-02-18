package UI;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

public class MenuCommand implements Command
{
    private String menuName;
    private Map<String,Command> map;

    public MenuCommand(String menuName)
    {
        this.menuName=menuName;
        map=new TreeMap<>();
    }
    @Override
    public void execute()
    {
        map.keySet().forEach(comanda->System.out.println(comanda));
    }
    public void addCommand(String descriere,Command comanda)
    {
        map.put(descriere, comanda);
    }
    public List<Command> getCommands()
    {
        //map.values() returneaza o colectie; colectia e pusa in stream si datele sunt puse intr-o lista
        return map.values().stream().collect(Collectors.toList());
    }
    public String getMenuName()
    {
        return menuName;
    }
}
