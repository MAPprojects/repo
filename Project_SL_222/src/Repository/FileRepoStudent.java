package Repository;

import Domain.Nota;
import Domain.NotaID;
import Domain.Student;
import Validator.Validator;
import org.w3c.dom.*;
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
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class FileRepoStudent extends AbstractRepository<Integer, Student> {
    private String fileName;

    public FileRepoStudent(Validator<Student> validator,String fileName){
        super(validator);
        this.fileName=fileName;
        loadData();
        //loading();
    }

    private void loading(){
        Path path = Paths.get(fileName);
        Stream<String> lines;
        try {
            lines = Files.lines(path);
            lines.forEach(linie -> {
                String[] s = linie.split("[|]");
                if (s.length != 5) {
                    System.out.println("Linie invalida" + linie);
                }
                try {
                    int idStudent = Integer.parseInt(s[0]);
                    int grupa=Integer.parseInt(s[2]);
                    Student stud=new Student(idStudent,s[1],grupa,s[3],s[4]);
                    super.save(stud);
                }
                catch(NumberFormatException e){
                    System.err.println(e);
                }
            } );
        }
        catch (IOException e) { System.err.println(e); }
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
                Student stud = createStudent(element);
                super.save(stud);
            }
        }
    }
    private Student createStudent(Element element) {
        Integer id=Integer.parseInt(element.getAttributeNode("id").getValue());
        String name=element.getElementsByTagName("name").item(0).getTextContent();
        Integer group = Integer.parseInt(element.getElementsByTagName("group").item(0).getTextContent());
        String email=element.getElementsByTagName("email").item(0).getTextContent();
        String prof=element.getElementsByTagName("teacher").item(0).getTextContent();
        return new Student(id,name,group,email,prof);
    }


    private void writeToFile(){
        try(PrintWriter pw=new PrintWriter(fileName)){
            for(Student stud:getAll()){
                String l=""+stud.getID()+"|"+stud.getNume()+"|"+stud.getGrupa()+"|"+stud.getEmail()+"|"+stud.getProfesor();
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
            Element rootElement = doc.createElement("students");
            doc.appendChild(rootElement);
            getAll().forEach(x->{
                Element studentElement = doc.createElement("student");
                studentElement.setAttribute("id", x.getID().toString());
                appendStudentElement(doc,"name",x.getNume(),studentElement);
                appendStudentElement(doc,"group",x.getGrupa().toString(),studentElement);
                appendStudentElement(doc,"email",x.getEmail(),studentElement);
                appendStudentElement(doc,"teacher",x.getProfesor(),studentElement);
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
    public void save(Student stud){
        super.save(stud);
        writeToXMLFile();
        //writeToFile();
    }

    @Override
    public void update(Student stud){
        super.update(stud);
        //writeToFile();
        writeToXMLFile();
    }

    @Override
    public Student delete(Integer id){
        Student oldStud=super.delete(id);
        //writeToFile();
        writeToXMLFile();
        return oldStud;
    }
}
