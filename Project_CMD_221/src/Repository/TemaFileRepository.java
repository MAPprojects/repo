package Repository;

import Validators.Validator;
import Entities.Teme;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.swing.text.TabExpander;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class TemaFileRepository extends TemeRepository {
    private String filename;
    public TemaFileRepository(String f, Validator<Teme> v) {
        super(v);
        filename = f;
        load();
    }

    private void load() {
        /*try {
            BufferedReader b = new BufferedReader(new FileReader(filename));
            String line;
            while((line = b.readLine()) != null) {
                String[] values = line.split("[-]");
                if(values.length != 3) {
                    System.err.println("Date incorecte");
                    continue;
                }
                try {
                    int id = Integer.parseInt(values[0]);
                    int deadline = Integer.parseInt(values[1]);
                    Teme tema = new Teme(id, deadline, values[2]);
                    super.save(tema);
                }
                catch(NumberFormatException e) {
                    System.err.println("Nu poate fi incarcata entitatea");
                }
            }
        }
        catch(IOException e) {
            System.err.println("Eroare la fisier");
        }*/
        List<Teme> list = loadFromXML();
        list.forEach(x->super.save(x));
    }

    private Teme toTema(String line) {
        String[] values = line.split("[-]");
        if(values.length != 4) {
            System.err.println("Date incorecte");
        }
        int id = Integer.parseInt(values[0]);
        int deadline = Integer.parseInt(values[1]);
        int dif = Integer.parseInt(values[3]);
        Teme tema = new Teme(id, deadline, values[2], dif);
        return tema;
    }

    private List<Teme> read() {
        try(Stream<String> s = Files.lines(Paths.get(filename))) {
            List<Teme> tema = s.map(x ->toTema(x)).collect(Collectors.toList());
            return tema;
        }
        catch (IOException e) {
            System.out.println(e);
            return null;
        }
    }

    private void writeToFile() {
        try(PrintWriter pr = new PrintWriter(filename)) {
            for(Teme t : getAll()) {
                String s = "" + t.getID() + '-' + t.getDeadline() + '-' + t.getDescriere() + '-' + t.getDif();
                pr.println(s);
            }
        }
        catch(IOException e) {
            throw new RepositoryException("Nu s-a putut salva entitatea");
        }
    }

    public List<Teme> loadFromXML() {
        File xmlFile = new File(filename);
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder;
        try {
            dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(xmlFile);
            doc.getDocumentElement().normalize();
            NodeList nodeList = doc.getElementsByTagName("Tema");
            List<Teme> temaList = new ArrayList<Teme>();
            for (int i = 0; i < nodeList.getLength(); i++) {
                temaList.add(getTema(nodeList.item(i)));
            }
            return temaList;
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
            Element rootElem = dom.createElement("Teme");
            for(Teme t : getAll()) {
                // use factory to get an instance of document builder
                // create the root element
                // create data elements and place them under root
                Element rootEle = dom.createElement("Tema");
                e = dom.createElement("id");
                e.appendChild(dom.createTextNode(String.valueOf(t.getIDTema())));
                rootEle.appendChild(e);

                e = dom.createElement("deadline");
                e.appendChild(dom.createTextNode(String.valueOf(t.getDeadline())));
                rootEle.appendChild(e);

                e = dom.createElement("desc");
                e.appendChild(dom.createTextNode(t.getDescriere()));
                rootEle.appendChild(e);

                e = dom.createElement("dif");
                e.appendChild(dom.createTextNode(String.valueOf(t.getDif())));
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

    private Teme getTema(Node node) {
        if (node.getNodeType() == Node.ELEMENT_NODE) {
            Element element = (Element) node;
            int id = Integer.parseInt(getTagValue("id", element));
            int deadline = Integer.parseInt(getTagValue("deadline", element));
            String desc = getTagValue("desc", element);
            int dif = Integer.parseInt(getTagValue("dif", element));
            Teme tema = new Teme(id, deadline, desc, dif);
            return tema;
        }
        return null;
    }

    private String getTagValue(String tag, Element element) {
        NodeList nodeList = element.getElementsByTagName(tag).item(0).getChildNodes();
        Node node = (Node) nodeList.item(0);
        return node.getNodeValue();
    }

    public void save(Teme tema) {
        super.save(tema);
        writeToXML();
    }

    public void update(Integer id, Teme tema) {
        super.update(id, tema);
        writeToXML();
    }

    public void delete(Integer id) {
        super.delete(id);
        writeToXML();
    }
}
