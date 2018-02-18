package mainpackage.ui.menu;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

public class MenuCommand implements Command {

    private String menuName;
    private Map<String, Command> map= new TreeMap<>();

    /**
     * Constructor for a menu
     * @param menuName name to be displayed
     */
    public MenuCommand(String menuName) {
        this.menuName = menuName;
    }

    /**
     * Executes a command
     */
    @Override
    public void execute() {
        map.keySet().forEach(x-> System.out.println(x));
    }

    /**
     * Adds a command
     * @param desc name
     * @param c command
     */
    public void addCommand(String desc, Command c){
        map.put(desc, c);
    }

    /**
     * Returns all commands
     * @return a list of commands
     */
    public List<Command> getCommands(){
        return map.values().stream().collect(Collectors.toList());
    }

    /**
     * @return the menu`s name
     */
    public String getMenuName() {
        return menuName;
    }
}
