package Repository;

import Domain.Nota;
import Domain.NotaID;
import Domain.Student;
import Validator.Validator;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.*;
import java.nio.file.Files;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class FileRepoNote extends AbstractRepository<NotaID, Nota> {
    private String fileName;
    public FileRepoNote(String fileName,Validator<Nota> validator){
        super(validator);
        this.fileName=fileName;
        //loading(fileName);
        //loading();
        loadData();
    }
    private static void appendStudentElement(Document doc, String tagName, String textNode, Element studentElem)
    {
        Element element=doc.createElement(tagName);
        element.appendChild(doc.createTextNode(textNode));
        studentElem.appendChild(element);
    }

    Document loadDocument() {
        try {
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = null;
            Document doc=null;
            docBuilder = docFactory.newDocumentBuilder();
            doc= docBuilder.parse(new FileInputStream(fileName));
            return doc;
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void loadData() {
        Document document =loadDocument();
        Node root = document.getDocumentElement();
        NodeList nodeList = root.getChildNodes();
        //List<Student> students = new ArrayList<>();
        for (int i = 0; i < nodeList.getLength(); i++) {
            Node node = nodeList.item(i);
            if (node.getNodeType() == Node.ELEMENT_NODE) {//node instanceof Element
                Element element = (Element) node;
                Nota nota = createStudent(element);
                super.save(nota);
            }
        }
    }
    private Nota createStudent(Element element) {
        Integer idStudent=Integer.parseInt(element.getAttributeNode("idStudent").getValue());
        Integer nrTema = Integer.parseInt(element.getAttributeNode("nrTema").getValue());
        Double valoare = Double.parseDouble(element.getElementsByTagName("valoare").item(0).getTextContent());
        NotaID idNota=new NotaID(idStudent,nrTema);
        return new Nota(idStudent,nrTema, idNota,valoare);
    }

    private void writeToXMLFile(){
        try {
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
            // root element
            Document doc = docBuilder.newDocument();
            Element rootElement = doc.createElement("grades");
            doc.appendChild(rootElement);
            getAll().forEach(x->{
                Element studentElement = doc.createElement("grade");
                studentElement.setAttribute("idStudent", x.getID().getIdStudent().toString());
                studentElement.setAttribute("nrTema",x.getID().getNrTema().toString());
                appendStudentElement(doc,"valoare",x.getValoare().toString(),studentElement);
                rootElement.appendChild(studentElement);
            });
            saveDocument(doc);
        }
        catch(ParserConfigurationException e){
            e.printStackTrace();
        }
    }

    void saveDocument(Document doc) {
        // write the content into xml file
        DOMSource source = new DOMSource(doc);
        StreamResult result = new StreamResult(new File(fileName));
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer tf = null;
        try {
            tf = TransformerFactory.newInstance().newTransformer();
            tf.setOutputProperty(OutputKeys.INDENT, "yes");
            tf.setOutputProperty(OutputKeys.METHOD, "xml");
            tf.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
        } catch (TransformerConfigurationException e) {
            e.printStackTrace();
        }
        try {
            tf.transform(source, result);
        } catch (TransformerException e) {
            e.printStackTrace();
        }
        //System.out.println("File saved!");
    }

    private void loading(){
        Path path = Paths.get(fileName);
        Stream<String> lines;
        try {
            lines = Files.lines(path);
            lines.forEach(linie -> {
                String[] s = linie.split("[|]");
                if (s.length != 3) {
                    System.out.println("Linie invalida" + linie);
                }
                try {
                    int idStudent = Integer.parseInt(s[0]);
                    int nrTema=Integer.parseInt(s[1]);
                    NotaID idNota=new NotaID(idStudent,nrTema);
                    Double valoare=Double.parseDouble(s[2]);
                    Nota nota=new Nota(idStudent,nrTema,idNota,valoare);
                    super.save(nota);
                }
                catch(NumberFormatException e){
                    System.err.println(e);
                }
            } );
        }
        catch (IOException e) { System.err.println(e); }
    }

    private void writeToFile(){
        try (PrintWriter pw = new PrintWriter(fileName)) {
            for (Nota nota : getAll()) {
                String l = ""+nota.getID().getIdStudent()+"|"+ nota.getID().getNrTema() + "|" + nota.getValoare();
                pw.println(l);
            }
        } catch (FileNotFoundException e) {
            System.err.println("ERR" + e);
        }
    }



    @Override
    public void save(Nota nota){
        super.save(nota);
        //writeToFile();
        writeToXMLFile();
    }

    @Override
    public void update(Nota nota){
        super.update(nota);
        //writeToFile();
        writeToXMLFile();
    }

    @Override
    public Nota delete(NotaID id){
        Nota oldNota=super.delete(id);
        //writeToFile();
        writeToXMLFile();
        return oldNota;
    }


}
