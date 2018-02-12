package Utils;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class ResourceManager {
    private static ResourceManager resourceManager = null;
    private final String file = "Files/resources.xml";
    private Map<String, String> values;
    private ResourceManager() throws Exception
    {
        values = new HashMap<>();
        File file = new File(this.file);
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        Document doc = dBuilder.parse(file);

        doc.getDocumentElement().normalize();
        NodeList nList = doc.getElementsByTagName("resource");
        for (int temp = 0; temp < nList.getLength(); temp++) {
            Node nNode = nList.item(temp);
            if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                Element eElement = (Element) nNode;
                String name = eElement.getElementsByTagName("name").item(0).getTextContent();
                String value =  eElement.getElementsByTagName("value").item(0).getTextContent();
                values.put(name, value);
            }
        }
    }

    public static ResourceManager getResourceMangager()
    {
        LoadResources();
        return resourceManager;
    }

    public String getValue(String key)
    {
        if (!values.containsKey(key))
            return null;
        return values.get(key);
    }

    public static void LoadResources()
    {
        if (resourceManager == null) {
            try {
                resourceManager = new ResourceManager();
            } catch (Exception ignored) {
            }
        }
    }
}
