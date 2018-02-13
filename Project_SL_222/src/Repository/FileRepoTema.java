package Repository;

import Domain.Student;
import Domain.Tema;
import Validator.Validator;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

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
import java.util.stream.Stream;

public class FileRepoTema extends AbstractRepository<Integer, Tema> {
    private String fileName;
    public FileRepoTema(Validator<Tema> validator, String fileName){
        super(validator);
        this.fileName=fileName;
        //loading();
        loadData();
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
                Tema tema = createTema(element);
                super.save(tema);
            }
        }
    }
    private Tema createTema(Element element) {
        Integer id=Integer.parseInt(element.getAttributeNode("nr").getValue());
        String description=element.getElementsByTagName("description").item(0).getTextContent();
        Integer deadline = Integer.parseInt(element.getElementsByTagName("deadline").item(0).getTextContent());
        return new Tema(id,description,deadline);
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
                    int idTema = Integer.parseInt(s[0]);
                    int deadline=Integer.parseInt(s[2]);
                    Tema tema=new Tema(idTema,s[1],deadline);
                    super.save(tema);
                }
                catch(NumberFormatException e){
                    System.err.println(e);
                }
            } );
        }
        catch (IOException e) { System.err.println(e); }
    }

    private void writeToFile(){
        try(PrintWriter pw=new PrintWriter(fileName)){
            for(Tema tema:getAll()){
                String l=""+tema.getID()+"|"+tema.getDescriere()+"|"+tema.getDeadline();
                pw.println(l);
            }
        }
        catch (FileNotFoundException e){
            System.err.println("ERR"+e);
        }
    }

    private static void appendStudentElement(Document doc, String tagName, String textNode, Element studentElem)
    {
        Element element=doc.createElement(tagName);
        element.appendChild(doc.createTextNode(textNode));
        studentElem.appendChild(element);
    }

    private void writeToXMLFile(){
        try {
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
            // root element
            Document doc = docBuilder.newDocument();
            Element rootElement = doc.createElement("assignments");
            doc.appendChild(rootElement);
            getAll().forEach(x->{
                Element studentElement = doc.createElement("assignment");
                studentElement.setAttribute("nr", x.getID().toString());
                appendStudentElement(doc,"description",x.getDescriere(),studentElement);
                appendStudentElement(doc,"deadline",x.getDeadline().toString(),studentElement);
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

    @Override
    public void save(Tema tema){
        super.save(tema);
        //writeToFile();
        writeToXMLFile();
    }

    @Override
    public void update(Tema tema){
        super.update(tema);
        //writeToFile();
        writeToXMLFile();
    }

    @Override
    public Tema delete(Integer id){
        Tema oldTema=super.delete(id);
        //writeToFile();
        writeToXMLFile();
        return oldTema;
    }
}
