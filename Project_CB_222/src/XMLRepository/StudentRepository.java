package XMLRepository;

import Domain.Student;
import Repository.Validator;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import javax.xml.parsers.DocumentBuilder;


public class StudentRepository extends AbstractRepositoryFromXML<Integer, Student> implements InterfaceRepositoryXML<Integer, Student> {

    public StudentRepository(Validator validator, String fileName) {
        super(validator, fileName);
        loadData();
    }

    @Override
    public void save(Student el) {
        super.save(el);
        Document doc = super.loadDocument();
        Element nList = doc.getDocumentElement();
        appendToXML(doc, nList, el);
        super.saveXML(doc);
    }

    @Override
    public void update(Student el) {
        super.update(el);
        writeToXML();
    }

    @Override
    public Student delete(Integer integer) {
        Student student = super.delete(integer);
        writeToXML();
        return student;
    }

    public void loadData(){
        Document doc = super.loadDocument();
        NodeList nList = doc.getElementsByTagName("student");

        for (int temp = 0; temp < nList.getLength(); temp++) {

            Node nNode = nList.item(temp);

            if (nNode.getNodeType() == Node.ELEMENT_NODE) {

                Element eElement = (Element) nNode;

                Student student = createObject(eElement);
                super.save(student);
            }
        }
    }

    public void writeToXML(){
        Iterable<Student> listOfStudent = super.getAll();
        DocumentBuilder docBuilder = super.createDocumentBuilder();

        // root elements
        Document doc = docBuilder.newDocument();
        Element rootElement = doc.createElement("students");
        doc.appendChild(rootElement);


        for(Student student: listOfStudent){
            appendToXML(doc, rootElement, student);
        }
        super.saveXML(doc);
    }


    public void appendToXML(Document doc, Element nList, Student student){
        try{

            Element newStudent = doc.createElement("student");

            Element studentId = doc.createElement("idStudent");
            studentId.appendChild(doc.createTextNode(String.valueOf( student.getId())));
            newStudent.appendChild(studentId);

            Element nume = doc.createElement("nume");
            nume.appendChild(doc.createTextNode(student.getNume()));
            newStudent.appendChild(nume);


            Element grupa = doc.createElement("grupa");
            grupa.appendChild(doc.createTextNode(String.valueOf(student.getGrupa())));
            newStudent.appendChild(grupa);

            Element email = doc.createElement("email");
            email.appendChild(doc.createTextNode(student.getEmail()));
            newStudent.appendChild(email);

            Element cadruDidactic = doc.createElement("cadruDidactic");
            cadruDidactic.appendChild(doc.createTextNode(student.getCadruDidactic()));
            newStudent.appendChild(cadruDidactic);

            nList.appendChild(newStudent);
        }
        catch (Exception e){
            e.printStackTrace();

        }

    }

    @Override
    public Student createObject(Element element) {
        int idStudent=Integer.parseInt(element.getElementsByTagName("idStudent").item(0).getTextContent().trim());
        String nume=element.getElementsByTagName("nume").item(0).getTextContent().trim();
        int grupa=Integer.parseInt( element.getElementsByTagName("grupa").item(0).getTextContent().trim());
        String email=element.getElementsByTagName("email").item(0).getTextContent().trim();
        String carduDidactic=element.getElementsByTagName("cadruDidactic").item(0).getTextContent().trim();
        return new Student(idStudent,nume, grupa, email, carduDidactic);
    }



}
