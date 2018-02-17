package Repository;

import Entities.Nota;
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
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class NotaFileRepository extends NotaRepository {
    private String filename;
    public NotaFileRepository(String f, Validator<Nota> v) {
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
                if(values.length != 4) {
                    System.err.println("Date incorecte");
                    continue;
                }
                try {
                    int idNota = Integer.parseInt(values[0]);
                    int idStudent = Integer.parseInt(values[1]);
                    int NrTema = Integer.parseInt(values[2]);
                    int valoare = Integer.parseInt(values[3]);
                    Nota nota = new Nota(idNota, idStudent, NrTema, valoare);
                    super.save(nota);
                }
                catch(NumberFormatException e) {
                    System.err.println("Nu poate fi incarcata entitatea");
                }
            }
        }
        catch(IOException e) {
            System.err.println("Eroare la fisier");
        }*/
        List<Nota> list = loadFromXML();
        list.forEach(x->super.save(x));
    }

    private Nota toNota(String line) {
        String[] values = line.split("[-]");
        if(values.length != 3) {
            System.err.println("Date incorecte");
        }
        int idStudent = Integer.parseInt(values[0]);
        String nume = values[1];
        int nrTema = Integer.parseInt(values[2]);
        int val = Integer.parseInt(values[3]);
        Nota nota = new Nota(idStudent, nume, nrTema, val);
        return nota;
    }

    private List<Nota> read() {
        try(Stream<String> s = Files.lines(Paths.get(filename))) {
            List<Nota> nota = s.map(x ->toNota(x)).collect(Collectors.toList());
            return nota;
        }
        catch (IOException e) {
            System.out.println(e);
            return null;
        }
    }

    private void writeToFile() {
        try(PrintWriter pr = new PrintWriter(filename)) {
            for(Nota n : getAll()) {
                String s = "" + n.getIDStudent() + '-' + n.getNrTema() + '-' + n.getValoare();
                pr.println(s);
            }
        }
        catch(IOException e) {
            throw new RepositoryException("Nu s-a putut salva entitatea");
        }
    }

    public List<Nota> loadFromXML() {
        File xmlFile = new File(filename);
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder;
        try {
            dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(xmlFile);
            doc.getDocumentElement().normalize();
            NodeList nodeList = doc.getElementsByTagName("Nota");
            List<Nota> notaList = new ArrayList<Nota>();
            for (int i = 0; i < nodeList.getLength(); i++) {
                notaList.add(getNota(nodeList.item(i)));
            }
            return notaList;
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
            Element rootElem = dom.createElement("Note");
            for(Nota n : getAll()) {
                // use factory to get an instance of document builder
                // create the root element
                // create data elements and place them under root
                Element rootEle = dom.createElement("Nota");
                e = dom.createElement("ids");
                e.appendChild(dom.createTextNode(String.valueOf(n.getIDStudent())));
                rootEle.appendChild(e);

                e = dom.createElement("nume");
                e.appendChild(dom.createTextNode(n.getNume()));
                rootEle.appendChild(e);

                e = dom.createElement("nrt");
                e.appendChild(dom.createTextNode(String.valueOf(n.getNrTema())));
                rootEle.appendChild(e);

                e = dom.createElement("val");
                e.appendChild(dom.createTextNode(String.valueOf(n.getValoare())));
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

    private Nota getNota(Node node) {
        if (node.getNodeType() == Node.ELEMENT_NODE) {
            Element element = (Element) node;
            int ids = Integer.parseInt(getTagValue("ids", element));
            String nume = getTagValue("nume", element);
            int nrt = Integer.parseInt(getTagValue("nrt", element));
            int val = Integer.parseInt(getTagValue("val", element));
            Nota nota = new Nota(ids, nume, nrt, val);
            return nota;
        }
        return null;
    }

    private String getTagValue(String tag, Element element) {
        NodeList nodeList = element.getElementsByTagName(tag).item(0).getChildNodes();
        Node node = (Node) nodeList.item(0);
        return node.getNodeValue();
    }

    public void save(Nota nota) {
        super.save(nota);
        writeToXML();
    }

    public void update(Pair<Integer,Integer> id, Nota nota) {
        super.update(id, nota);
        writeToXML();
    }

    public void delete(Pair<Integer,Integer> id) {
        super.delete(id);
        writeToXML();
    }
}
