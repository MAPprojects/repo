package Repository;

import Entities.User;
import Validators.Validator;
import javafx.util.Pair;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class UserFileRepository extends UserRepository {
    private String filename;
    public UserFileRepository(String f, Validator<User> v) {
        super(v);
        filename = f;
        load();
    }

    private void load() {
        List<User> list = loadFromXML();
        list.forEach(x->super.save(x));
    }

    public List<User> loadFromXML() {
        File xmlFile = new File(filename);
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder;
        try {
            dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(xmlFile);
            doc.getDocumentElement().normalize();
            NodeList nodeList = doc.getElementsByTagName("User");
            List<User> userList = new ArrayList<>();
            for (int i = 0; i < nodeList.getLength(); i++) {
                userList.add(getUser(nodeList.item(i)));
            }
            return userList;
        } catch (SAXException | ParserConfigurationException | IOException e1) {
            e1.printStackTrace();
            return null;
        }
    }

    public void writeToXML() {
        Document dom;
        Element e = null;
        // instance of a DocumentBuilderFactory
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        try {
            DocumentBuilder db = dbf.newDocumentBuilder();
            // create instance of DOM
            dom = db.newDocument();
            Element rootElem = dom.createElement("Users");
            for(User user : getAll()) {
                // use factory to get an instance of document builder
                // create the root element
                // create data elements and place them under root
                Element rootEle = dom.createElement("User");
                e = dom.createElement("username");
                e.appendChild(dom.createTextNode(user.getUsername()));
                rootEle.appendChild(e);

                e = dom.createElement("password");
                e.appendChild(dom.createTextNode(user.getPassword()));
                rootEle.appendChild(e);

                e = dom.createElement("tip1");
                e.appendChild(dom.createTextNode(String.valueOf(user.isOk1())));
                rootEle.appendChild(e);

                e = dom.createElement("tip2");
                e.appendChild(dom.createTextNode(String.valueOf(user.isOk2())));
                rootEle.appendChild(e);

                rootElem.appendChild(rootEle);
            }
            dom.appendChild(rootElem);
            try {
                TransformerFactory transformerFactory = TransformerFactory.newInstance();
                Transformer transformer = transformerFactory.newTransformer();
                //for pretty print
                transformer.setOutputProperty(OutputKeys.INDENT, "yes");
                DOMSource source = new DOMSource(dom);

                //write to file
                StreamResult file = new StreamResult(new File(filename));

                //write data
                transformer.transform(source, file);

            } catch (TransformerException te) {
                System.out.println(te.getMessage());
            }
        } catch (ParserConfigurationException pce) {
            System.out.println("UsersXML: Error trying to instantiate DocumentBuilder " + pce);
        }
    }

    private User getUser(Node node) {
        if (node.getNodeType() == Node.ELEMENT_NODE) {
            Element element = (Element) node;
            String username = getTagValue("username", element);
            String password = getTagValue("password", element);
            int tip1 = Integer.parseInt(getTagValue("tip1", element));
            int tip2 = Integer.parseInt(getTagValue("tip2", element));
            User user = new User(username, password, tip1, tip2);
            return user;
        }
        return null;
    }

    private String getTagValue(String tag, Element element) {
        NodeList nodeList = element.getElementsByTagName(tag).item(0).getChildNodes();
        Node node = (Node) nodeList.item(0);
        return node.getNodeValue();
    }

    public void save(User user) {
        super.save(user);
        writeToXML();
    }

    public void update(Pair<String, String> id, User user) {
        super.update(id, user);
        writeToXML();
    }
}
