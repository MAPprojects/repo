package sample.repository;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import sample.domain.Candidat;
import sample.domain.Opțiune;
import sample.domain.Secție;
import sample.domain.Status;

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
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;
import java.util.stream.Stream;

public class OpțiuneFileRepository extends AbstractFileRepository<String, Opțiune> {
    private static String logFileName;
    private Path fullPath;

    public OpțiuneFileRepository(String fileName, String path, String xmlName, String logFileName) {
        super(fileName, xmlName, path);
        OpțiuneFileRepository.logFileName = logFileName;
        this.fullPath = Paths.get(path + fileName);
    }


    @Override
    protected Map<String, Opțiune> readFromFile(String fileName, String path) {
        Path fullPath = Paths.get(path + fileName);
        Map opțiuni = new HashMap<Integer, Opțiune>();

        Stream<String> linii;
        try {
            linii = Files.lines(fullPath);

            linii.forEach(linie -> {
                // identificăm câmpurile opțiunii
                String[] câmpuri = linie.split("█");
                // idCandidat
                String cnpCandidat = câmpuri[0];
                // idSecții
                String[] câmpuriIDsecții = câmpuri[1].split(" ");
                Vector<String> idSecții = new Vector<>();
                for (String idSecție : câmpuriIDsecții)
                    idSecții.add(idSecție);

                // creăm opțiunea
                Opțiune opțiune = new Opțiune(cnpCandidat, idSecții);

                // salvăm opțiunea în dicționar
                opțiuni.put(opțiune.getID(), opțiune);
            });

        } catch (IOException e) {
            //e.printStackTrace();
        }

        // returnăm dicționarul cu secțiile citite
        return opțiuni;
    }

    @Override
    protected void saveToFile(String fileName, String path, Map<String, Opțiune> opțiuni) {
        Path fullPath = Paths.get(path + fileName);
        try(BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(fullPath.toFile(),false))) {

            // parcurgem opțiunile
            for (Opțiune opțiune : opțiuni.values()) {

                // facem rost de lista secțiilor optate de candidat
                String idSecții = "";
                for(String idSecție : opțiune.getIdSecții()) {
                    idSecții += idSecție + " ";
                }
                idSecții = idSecții.substring(0, idSecții.length() - 1);

                // scriem în fișier
                bufferedWriter.write(opțiune.getID() + "█" + idSecții);
                bufferedWriter.newLine();
            }
        }
        catch (IOException e) {
            //e.printStackTrace();
        }
    }

    protected static void addToFile(String fileName, String text){
        try(BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(fileName,true))) {
            bufferedWriter.write(text);
            bufferedWriter.newLine();
        }
        catch (IOException e) {
            //e.printStackTrace();
        }
    }

    /**
     * Se primește un candidat, o secție și verbul ce descrie ce s-a întâmplat.
     * @param candidat
     * @param secție
     * @param status - poate fi „adăugat” sau „eliminat”
     */
    public static void addLog(Candidat candidat, Secție secție, Status status, Secție secțieVeche) {
        // Notăm data și ora la care s-a făcut actualizarea
        Date data = new Date( );
        SimpleDateFormat format = new SimpleDateFormat ("yyyy.MM.dd 'at' hh:mm:ss");
        OpțiuneFileRepository.addToFile(logFileName, format.format(data));

        // Notăm ce schimbări s-au făcut (pe rândul următor)
        String de = " de";
        if (status.equals(Status.adăugat) || status.equals(Status.mutat))
            de = "";

        String textActualizare = "";
        if (status.equals(Status.mutat))
            textActualizare = " după ce a renunțat la secția " + secțieVeche.getID() + " " + secțieVeche.getNume();

        String text = "Candidatul " + candidat.getID() + " " + candidat.getNume() + "   a fost " + status + de
                + " la secția " + secție.getID() + " " + secție.getNume() + textActualizare + ".";


        OpțiuneFileRepository.addToFile(logFileName, text);

        // Inserăm un rând nou
        OpțiuneFileRepository.addToFile(logFileName, "");
    }



    /** XML */

    @Override
    protected Map<String, Opțiune> readFromXML(String xmlName, String path) {
        Path fullPath = Paths.get(path + xmlName);
        return loadData(xmlName, fullPath);
    }

    private Map<String, Opțiune> loadData(String xmlName, Path path) {
        Document document = loadDocument(xmlName, path);
        assert document != null;
        Node root = document.getDocumentElement();
        NodeList nodeList = root.getChildNodes();
        Map<String, Opțiune> opțiuneMap = new HashMap<String, Opțiune>();
        for (int i = 0; i < nodeList.getLength(); i++) {
            Node node = nodeList.item(i);
            if (node.getNodeType() == Node.ELEMENT_NODE) {  //node instance of Element
                Element element = (Element) node;
                Opțiune opțiune = createOpțiune(element);
                opțiuneMap.put(opțiune.getID(), opțiune);
            }
        }
        return opțiuneMap;
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

    private Opțiune createOpțiune(Element element) {
        String id = element.getAttributeNode("id").getValue();

        Node node = element.getLastChild();
        NodeList nodeList = node.getChildNodes();
        Vector<String> idSecții = new Vector<>();
        for (int i = 0; i < nodeList.getLength(); i++) {
            Node n = nodeList.item(i);
            if (n.getNodeType() == Node.ELEMENT_NODE) {  //node instance of Element
                Element e = (Element) n;
                String idSecție = createIdSecție(e);
                idSecții.add(idSecție);
            }
        }

//        String[] secții = element.getAttributeNode("idSectii").getValue().split("█");
//        for (String idSecție : secții)
//            idSecții.add(Integer.valueOf(idSecție));

        return new Opțiune(id, idSecții);
    }

    private String createIdSecție(Element element) {
        String idSecție = element.getAttributeNode("idS").getValue();
        return idSecție;
    }


    @Override
    protected void saveToXML(String fileName, String path, Map<String, Opțiune> opțiuni) {
        Vector<Opțiune> opțiuneVector = new Vector<>();
        opțiuneVector.addAll(opțiuni.values());
        writeToFile(fileName, path, opțiuneVector);
    }

    private void writeToFile(String xmlName, String path, Vector<Opțiune> opțiuni) {
        try {
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
            // root element
            Document doc = docBuilder.newDocument();
            Element rootElement = doc.createElement("optiuni");
            doc.appendChild(rootElement);
            opțiuni.forEach(x->{
                Element opțiuneElement = doc.createElement("optiune");
                opțiuneElement.setAttribute("id", x.getID());

//                String idSecții = new String();
//                for (Integer idSecție : x.getIdSecții())
//                    idSecții += idSecție.toString() + "█";
//                opțiuneElement.setAttribute("idSectii", idSecții);

                Element idSecțiiElement = doc.createElement("idSectii");
                opțiuneElement.appendChild(idSecțiiElement);
                x.getIdSecții().forEach(y->{
                    Element idElement = doc.createElement("idSectie");
                    idElement.setAttribute("idS", y);
                    idSecțiiElement.appendChild(idElement);
                });

                rootElement.appendChild(opțiuneElement);
            });

            Path fullPath = Paths.get(path + xmlName);
            saveDocument(doc, xmlName, fullPath);
        } catch (ParserConfigurationException pce) {
            pce.printStackTrace();
        }
    }

    private static void appendOpțiuneElement(Document doc, String tagName, String textNode, Element opțiuneElem) {
        Element element=doc.createElement(tagName);
        element.appendChild(doc.createTextNode(textNode));
        opțiuneElem.appendChild(element);
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
