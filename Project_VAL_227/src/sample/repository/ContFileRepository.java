package sample.repository;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import sample.domain.Cont;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;
import java.util.stream.Stream;

public class ContFileRepository extends AbstractFileRepository<String, Cont> {
    private Path fullPath;

    public ContFileRepository(String fileName, String xmlName, String path) {
        super(fileName, xmlName, path);
        this.fullPath = Paths.get(path + fileName);
    }

    @Override
    protected Map<String, Cont> readFromFile(String fileName, String path) {
        Path fullPath = Paths.get(path + fileName);
        Map conturi = new HashMap<String, Cont>();

        Stream<String> linii = null;
        try {
            linii = Files.lines(fullPath);

            linii.forEach(linie -> {
                // identificăm câmpurile contului
                String[] câmpuri = linie.split("█");
                String cnp = câmpuri[0];
                String parolă = câmpuri[1];

                // creăm contul
                Cont cont = new Cont(cnp, parolă);

                // salvăm candidatul în dicționar
                conturi.put(cont.getID(), cont);
            });

        } catch (IOException e) {
            //e.printStackTrace();
        }

        if (linii != null)
            linii.close();

        // returnăm dicționarul cu conturile citite
        return conturi;
    }

    @Override
    protected void saveToFile(String fileName, String path, Map<String, Cont> conturi) {
        Path fullPath = Paths.get(path + fileName);
        try(BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(fullPath.toFile(),false))) {
            for (Cont cont : conturi.values()) {
                bufferedWriter.write(cont.getID() + "█" + cont.getParola());
                bufferedWriter.newLine();
            }
            bufferedWriter.close();
        }
        catch (IOException e) {
            //e.printStackTrace();
        }
    }

    /** XML */

    @Override
    protected Map<String, Cont> readFromXML(String xmlName, String path) {
        Path fullPath = Paths.get(path + xmlName);
        return loadData(xmlName, fullPath);
    }

    private Map<String, Cont> loadData(String xmlName, Path path) {
        Document document = loadDocument(xmlName, path);
        assert document != null;
        Node root = document.getDocumentElement();
        NodeList nodeList = root.getChildNodes();
        Map<String, Cont> contMap = new HashMap<String, Cont>();
        for (int i = 0; i < nodeList.getLength(); i++) {
            Node node = nodeList.item(i);
            if (node.getNodeType() == Node.ELEMENT_NODE) {//node instanceof Element
                Element element = (Element) node;
                Cont cont = createCont(element);
                contMap.put(cont.getID(), cont);
            }
        }
        return contMap;
    }

    private Document loadDocument(String xmlName, Path path) {
        try {
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = null;
            Document doc = null;
            docBuilder = docFactory.newDocumentBuilder();
            doc = docBuilder.parse(new FileInputStream(path.toString()));
            return doc;
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
            e.printStackTrace();
        } catch (IOException | org.xml.sax.SAXException e) {
            e.printStackTrace();
        }
        return null;
    }

    private Cont createCont(Element element) {
        String id = element.getAttributeNode("id").getValue();
        String parola = element.getElementsByTagName("parola").item(0).getTextContent();
        return new Cont(id, parola);
    }





    @Override
    protected void saveToXML(String fileName, String path, Map<String, Cont> conturi) {
        Vector<Cont> contVector = new Vector<>();
        contVector.addAll(conturi.values());
        writeToFile(fileName, path, contVector);
    }

    private void writeToFile(String xmlName, String path, Vector<Cont> conturi) {
        try {
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
            // root element
            Document doc = docBuilder.newDocument();
            Element rootElement = doc.createElement("conturi");
            doc.appendChild(rootElement);
            conturi.forEach(x->{
                Element contElement = doc.createElement("cont");
                contElement.setAttribute("id", x.getID());
                appendContElement(doc,"parola",x.getParola(),contElement);
                rootElement.appendChild(contElement);
            });

            Path fullPath = Paths.get(path + xmlName);
            saveDocument(doc, xmlName, fullPath);
        } catch (ParserConfigurationException pce) {
            pce.printStackTrace();
        }
    }

    private static void appendContElement(Document doc, String tagName, String textNode, Element contElement) {
        Element element = doc.createElement(tagName);
        element.appendChild(doc.createTextNode(textNode));
        contElement.appendChild(element);
    }

    private void saveDocument(Document doc, String xmlName, Path path) {
        // write the content into xml file
        DOMSource source = new DOMSource(doc);
        StreamResult result = null;
        try {
            result = new StreamResult(new FileWriter(new File(path.toString())));
        } catch (IOException e) {
            e.printStackTrace();
        }
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = null;
        try {
            transformer = transformerFactory.newTransformer();
        } catch (TransformerConfigurationException e) {
            e.printStackTrace();
        }
        try {
            assert transformer != null;
            transformer.transform(source, result);
        } catch (TransformerException e) {
            e.printStackTrace();
        }
        //System.out.println("File saved!");
    }
}
