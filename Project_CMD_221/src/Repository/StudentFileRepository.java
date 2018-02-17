package Repository;

import Validators.Validator;
import Entities.Student;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.Collector;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class StudentFileRepository extends StudentRepository {
    private String filename;
    public StudentFileRepository(String f, Validator<Student> v) {
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
                if(values.length != 5) {
                    System.err.println("Date incorecte");
                    continue;
                }
                try {
                    int id = Integer.parseInt(values[0]);
                    Student student = new Student(id, values[1], values[2], values[3], values[4]);
                    super.save(student);
                }
                catch(NumberFormatException e) {
                    System.err.println("Nu poate fi incarcata entitatea");
                }
            }
        }
        catch(IOException e) {
            System.err.println("Eroare la fisier");
        }*/
        List<Student> list = loadFromXML();
        list.forEach(x->super.save(x));
    }

    private Student toStudent(String line) {
        String[] values = line.split("[-]");
        if(values.length != 10) {
            System.err.println("Date incorecte");
        }
        int id = Integer.parseInt(values[0]);
        int note = Integer.parseInt(values[5]);
        int nr = Integer.parseInt(values[6]);
        double medie = Double.parseDouble(values[7]);
        boolean val = Boolean.parseBoolean(values[8]);
        boolean fint = Boolean.parseBoolean(values[9]);
        Student student = new Student(id, values[1], values[2], values[3], values[4], note, nr, medie, val, fint);
        return student;
    }

    private List<Student> read() {
        try(Stream<String> s = Files.lines(Paths.get(filename))) {
            List<Student> student = s.map(x ->toStudent(x)).collect(Collectors.toList());
            return student;
        }
        catch (IOException e) {
            System.out.println(e);
            return null;
        }
    }

    public void writeToFile() {
        try(PrintWriter pr = new PrintWriter(filename)) {
            for(Student st : getAll()) {
                String s = "" + st.getID() + '-' + st.getNume() + '-' + st.getEmail() + '-' + st.getGrupa() + '-' + st.getProf() + '-' + st.getNote() + '-' + st.getNr() + '-' + st.getMedie() + '-' + st.isVal() + '-' + st.isFint();
                pr.println(s);
            }
        }
        catch(IOException e) {
            throw new RepositoryException("Nu s-a putut salva entitatea");
        }
    }

    public List<Student> loadFromXML() {
        File xmlFile = new File(filename);
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder;
        try {
            dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(xmlFile);
            doc.getDocumentElement().normalize();
            NodeList nodeList = doc.getElementsByTagName("Student");
            List<Student> studentList = new ArrayList<Student>();
            for (int i = 0; i < nodeList.getLength(); i++) {
                studentList.add(getStudent(nodeList.item(i)));
            }
            return studentList;
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
            Element rootElem = dom.createElement("Students");
            for(Student st : getAll()) {
                // use factory to get an instance of document builder
                // create the root element
                // create data elements and place them under root
                Element rootEle = dom.createElement("Student");
                e = dom.createElement("id");
                e.appendChild(dom.createTextNode(String.valueOf(st.getIDStudent())));
                rootEle.appendChild(e);

                e = dom.createElement("nume");
                e.appendChild(dom.createTextNode(st.getNume()));
                rootEle.appendChild(e);

                e = dom.createElement("email");
                e.appendChild(dom.createTextNode(st.getEmail()));
                rootEle.appendChild(e);

                e = dom.createElement("grupa");
                e.appendChild(dom.createTextNode(st.getGrupa()));
                rootEle.appendChild(e);

                e = dom.createElement("prof");
                e.appendChild(dom.createTextNode(st.getProf()));
                rootEle.appendChild(e);

                e = dom.createElement("note");
                e.appendChild(dom.createTextNode(String.valueOf(st.getNote())));
                rootEle.appendChild(e);

                e = dom.createElement("nr");
                e.appendChild(dom.createTextNode(String.valueOf(st.getNr())));
                rootEle.appendChild(e);

                e = dom.createElement("medie");
                e.appendChild(dom.createTextNode(String.valueOf(st.getMedie())));
                rootEle.appendChild(e);

                e = dom.createElement("val");
                e.appendChild(dom.createTextNode(String.valueOf(st.isVal())));
                rootEle.appendChild(e);

                e = dom.createElement("fint");
                e.appendChild(dom.createTextNode(String.valueOf(st.isFint())));
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

    private Student getStudent(Node node) {
        if (node.getNodeType() == Node.ELEMENT_NODE) {
            Element element = (Element) node;
            int id = Integer.parseInt(getTagValue("id", element));
            String nume = getTagValue("nume", element);
            String email = getTagValue("email", element);
            String grupa = getTagValue("grupa", element);
            String prof = getTagValue("prof", element);
            int note = Integer.parseInt(getTagValue("note", element));
            int nr = Integer.parseInt(getTagValue("nr", element));
            double medie = Double.parseDouble(getTagValue("medie", element));
            boolean val = Boolean.parseBoolean(getTagValue("val", element));
            boolean fint = Boolean.parseBoolean(getTagValue("fint", element));
            Student stud = new Student(id, nume, email, grupa, prof, note, nr, medie, val, fint);
            return stud;
        }
        return null;
    }

    private String getTagValue(String tag, Element element) {
        NodeList nodeList = element.getElementsByTagName(tag).item(0).getChildNodes();
        Node node = (Node) nodeList.item(0);
        return node.getNodeValue();
    }

    public void save(Student student) {
        super.save(student);
        writeToXML();
    }

    public void update(Integer id, Student student) {
        super.update(id, student);
        writeToXML();
    }

    public void delete(Integer id) {
        super.delete(id);
        writeToXML();
    }
}
