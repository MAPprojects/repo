package sample.repository;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import sample.domain.Candidat;

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

public class CandidatFileRepository extends AbstractFileRepository<String, Candidat> {
    private Path fullPath;

    public CandidatFileRepository(String fileName, String xmlName, String path) {
        super(fileName, xmlName, path);
        this.fullPath = Paths.get(path + fileName);
    }

    @Override
    protected Map<String, Candidat> readFromFile(String fileName, String path) {
        Path fullPath = Paths.get(path + fileName);
        Map candidați = new HashMap<String, Candidat>();

        Stream<String> linii = null;
        try {
            linii = Files.lines(fullPath);

            linii.forEach(linie -> {
                // identificăm câmpurile candidatului
                String[] câmpuri = linie.split("█");
                String id = câmpuri[0];
                String nume = câmpuri[1];
                String prenume = câmpuri[2];
                String telefon = câmpuri[3];
                String e_mail = câmpuri[4];

                // creăm candidatul
                Candidat candidat = new Candidat(id, nume, prenume, telefon, e_mail);

                // salvăm candidatul în dicționar
                candidați.put(candidat.getID(), candidat);
            });

        } catch (IOException e) {
            //e.printStackTrace();
        }

        if (linii != null)
            linii.close();

        // returnăm dicționarul cu candidații citiți
        return candidați;
    }


    @Override
    protected void saveToFile(String fileName, String path, Map<String, Candidat> candidați) {
        Path fullPath = Paths.get(path + fileName);
            try(BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(fullPath.toFile(),false))) {
                for (Candidat candidat : candidați.values()) {
                    bufferedWriter.write(candidat.getID() + "█" + candidat.getNume() + "█" + candidat.getPrenume() + "█" + candidat.getTelefon() + "█" + candidat.getE_mail());
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
    protected Map<String, Candidat> readFromXML(String xmlName, String path) {
        Path fullPath = Paths.get(path + xmlName);
        return loadData(xmlName, fullPath);
    }

    private Map<String, Candidat> loadData(String xmlName, Path path) {
        Document document = loadDocument(xmlName, path);
        assert document != null;
        Node root = document.getDocumentElement();
        NodeList nodeList = root.getChildNodes();
        Map<String, Candidat> candidatMap = new HashMap<String, Candidat>();
        for (int i = 0; i < nodeList.getLength(); i++) {
            Node node = nodeList.item(i);
            if (node.getNodeType() == Node.ELEMENT_NODE) {//node instanceof Element
                Element element = (Element) node;
                Candidat candidat = createCandidat(element);
                candidatMap.put(candidat.getID(), candidat);
            }
        }
        return candidatMap;
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

    private Candidat createCandidat(Element element) {
        String id = element.getAttributeNode("id").getValue();
        String nume = element.getElementsByTagName("nume").item(0).getTextContent();
        String prenume = element.getElementsByTagName("prenume").item(0).getTextContent();
        String telefon = element.getElementsByTagName("telefon").item(0).getTextContent();
        String email = element.getElementsByTagName("email").item(0).getTextContent();
        return new Candidat(id, nume, prenume, telefon, email);
    }





    @Override
    protected void saveToXML(String fileName, String path, Map<String, Candidat> candidați) {
        Vector<Candidat> candidatVector = new Vector<>();
        candidatVector.addAll(candidați.values());
        writeToFile(fileName, path, candidatVector);
    }

    private void writeToFile(String xmlName, String path, Vector<Candidat> candidați) {
        try {
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
            // root element
            Document doc = docBuilder.newDocument();
            Element rootElement = doc.createElement("candidati");
            doc.appendChild(rootElement);
            candidați.forEach(x->{
                Element candidatElement = doc.createElement("candidat");
                candidatElement.setAttribute("id", x.getID());
                appendCandidatElement(doc,"nume",x.getNume(),candidatElement);
                appendCandidatElement(doc,"prenume",x.getPrenume(),candidatElement);
                appendCandidatElement(doc,"telefon",x.getTelefon(),candidatElement);
                appendCandidatElement(doc,"email",x.getE_mail(),candidatElement);
                rootElement.appendChild(candidatElement);
            });

            Path fullPath = Paths.get(path + xmlName);
            saveDocument(doc, xmlName, fullPath);
        } catch (ParserConfigurationException pce) {
            pce.printStackTrace();
        }
    }

    private static void appendCandidatElement(Document doc, String tagName, String textNode, Element candidatElem) {
        Element element = doc.createElement(tagName);
        element.appendChild(doc.createTextNode(textNode));
        candidatElem.appendChild(element);
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
