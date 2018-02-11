package sample.repository;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import sample.domain.FormăDeFinanțare;
import sample.domain.Secție;

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

public class SecțieFileRepository extends AbstractFileRepository<String, Secție> {
    private Path fullPath;

    public SecțieFileRepository(String fileName, String xmlName, String path) {
        super(fileName, xmlName, path);
        this.fullPath = Paths.get(path + fileName);
    }

    @Override
    protected Map<String, Secție> readFromFile(String fileName, String path) {
        Path fullPath = Paths.get(path + fileName);
        Map secții = new HashMap<Integer, Secție>();

        Stream<String> linii;
        try {
            linii = Files.lines(fullPath);

            linii.forEach(linie -> {
                // identificăm câmpurile secției
                String[] câmpuri = linie.split("█");
                String id = câmpuri[0];
                String nume = câmpuri[1];
                String limbaDePredare = câmpuri[2];
                String finanțare = câmpuri[3];
                FormăDeFinanțare formăDeFinanțare;
                if (finanțare.equals(FormăDeFinanțare.BUGET.toString()))
                    formăDeFinanțare = FormăDeFinanțare.BUGET;
                else
                    formăDeFinanțare = FormăDeFinanțare.TAXĂ;
                Integer nrLocuri = Integer.valueOf(câmpuri[4]);

                // creăm secția
                Secție secție = new Secție(id, nume, limbaDePredare, formăDeFinanțare, nrLocuri);

                // salvăm secția în dicționar
                secții.put(secție.getID(), secție);
            });

        } catch (IOException e) {
            //e.printStackTrace();
        }

        // returnăm dicționarul cu secțiile citite
        return secții;
    }

    @Override
    protected void saveToFile(String fileName, String path, Map<String, Secție> secții) {
        Path fullPath = Paths.get(path + fileName);
        try(BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(fullPath.toFile(),false))) {
            for (Secție secție : secții.values()) {
                bufferedWriter.write(secție.getID() + "█" + secție.getNume() + "█" + secție.getLimbaDePredare() + "█" + secție.getFormăDeFinanțare() + "█" + secție.getNrLocuri());
                bufferedWriter.newLine();
            }
        }
        catch (IOException e) {
            //e.printStackTrace();
        }
    }




    /** XML */

    @Override
    protected Map<String, Secție> readFromXML(String xmlName, String path) {
        Path fullPath = Paths.get(path + xmlName);
        return loadData(xmlName, fullPath);
    }

    private Map<String, Secție> loadData(String xmlName, Path path) {
        Document document = loadDocument(xmlName, path);
        assert document != null;
        Node root = document.getDocumentElement();
        NodeList nodeList = root.getChildNodes();
        Map<String, Secție> secțieMap = new HashMap<String, Secție>();
        for (int i = 0; i < nodeList.getLength(); i++) {
            Node node = nodeList.item(i);
            if (node.getNodeType() == Node.ELEMENT_NODE) {//node instanceof Element
                Element element = (Element) node;
                Secție secție = createSecție(element);
                secțieMap.put(secție.getID(), secție);
            }
        }
        return secțieMap;
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

    private Secție createSecție(Element element) {
        String id = element.getAttributeNode("id").getValue();
        String nume = element.getElementsByTagName("nume").item(0).getTextContent();
        String limbaDePredare = element.getElementsByTagName("limba").item(0).getTextContent();
        String finanțare = element.getElementsByTagName("finantare").item(0).getTextContent();
        FormăDeFinanțare formăDeFinanțare;
        if (finanțare.equals(FormăDeFinanțare.BUGET.toString()))
            formăDeFinanțare = FormăDeFinanțare.BUGET;
        else
            formăDeFinanțare = FormăDeFinanțare.TAXĂ;
        Integer nrLocuri = Integer.valueOf(element.getElementsByTagName("nrLocuri").item(0).getTextContent());
        return new Secție(id, nume, limbaDePredare, formăDeFinanțare, nrLocuri);
    }





    @Override
    protected void saveToXML(String fileName, String path, Map<String, Secție> secțieMap) {
        Vector<Secție> secțieVector = new Vector<>();
        secțieVector.addAll(secțieMap.values());
        writeToFile(fileName, path, secțieVector);
    }

    private void writeToFile(String xmlName, String path, Vector<Secție> secții) {
        try {
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
            // root element
            Document doc = docBuilder.newDocument();
            Element rootElement = doc.createElement("sectii");
            doc.appendChild(rootElement);
            secții.forEach(x->{
                Element sectieElement = doc.createElement("sectie");
                sectieElement.setAttribute("id", x.getID().toString());
                appendSecțieElement(doc,"nume",x.getNume(),sectieElement);
                appendSecțieElement(doc,"limba",x.getLimbaDePredare(),sectieElement);
                appendSecțieElement(doc,"finantare",x.getFormăDeFinanțare().toString(),sectieElement);
                appendSecțieElement(doc,"nrLocuri",x.getNrLocuri().toString(),sectieElement);
                rootElement.appendChild(sectieElement);
            });

            Path fullPath = Paths.get(path + xmlName);
            saveDocument(doc, xmlName, fullPath);
        } catch (ParserConfigurationException pce) {
            pce.printStackTrace();
        }
    }

    private static void appendSecțieElement(Document doc, String tagName, String textNode, Element secțieElem) {
        Element element = doc.createElement(tagName);
        element.appendChild(doc.createTextNode(textNode));
        secțieElem.appendChild(element);
    }

    private void saveDocument(Document doc, String xmlName, Path path) {
        // write the content into xml file
        DOMSource source = new DOMSource(doc);
        StreamResult result = null;
        try {
            result = new StreamResult(new FileWriter(new File(path.toString())));           /// AICI
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
