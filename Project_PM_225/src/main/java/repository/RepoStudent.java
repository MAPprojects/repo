package repository;

import domain.Student;
import domain.StudentValidator;
import org.w3c.dom.*;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.stream.Stream;

public class RepoStudent extends AbstractRepo<Student> {
    private MockRepo defaultlist = new MockRepo();
    private StudentValidator studentValidator = new StudentValidator();


    public RepoStudent() throws Exception {
        super();
        setValidator(Optional.of(studentValidator));
    }

    public void update(Integer id, Student student) throws Exception {
        studentValidator.valideaza(student);
        for (Integer i = 0; i < lista.size(); i++) {
            Student s = lista.get(i);
            if (id == s.getId()) {
                s.setId(student.getId());
                s.setNume(student.getNume());
                s.setGrupa(student.getGrupa());
                s.setEmail(student.getEmail());
                s.setIndrumator(student.getIndrumator());
                updateFile();

            }
        }
    }

    public void getFromFile() throws Exception {
        try {
            File inputFile = new File("./src/main/resources/Student.xml");
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(inputFile);
            doc.getDocumentElement().normalize();
            NodeList nList = doc.getElementsByTagName("student");

            for (int temp = 0; temp < nList.getLength(); temp++) {
                Node nNode = nList.item(temp);

                if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element eElement = (Element) nNode;
                    Student student = new Student();
                    student.setId(Integer.parseInt(eElement.getAttribute("id")));

                    student.setNume(eElement.getElementsByTagName("nume").item(0).getTextContent());
                    student.setGrupa(Integer.parseInt(eElement.getElementsByTagName("grupa").item(0).getTextContent()));
                    student.setEmail(eElement.getElementsByTagName("email").item(0).getTextContent());
                    student.setIndrumator(eElement.getElementsByTagName("indrumator").item(0).getTextContent());

                    lista.add(student);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


    }


    public void updateFile() {
        try {
            DocumentBuilderFactory dbFactory =
                    DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.newDocument();

            // root element
            Element rootElement = doc.createElement("studenti");
            doc.appendChild(rootElement);
            for (Student s : lista) {
                // student element
                Element student = doc.createElement("student");
                rootElement.appendChild(student);

                // setting attribute to element
                Attr attr = doc.createAttribute("id");
                attr.setValue(s.getId().toString());
                student.setAttributeNode(attr);

                // nume element
                Element nume = doc.createElement("nume");
                nume.appendChild(doc.createTextNode(s.getNume()));
                student.appendChild(nume);

                Element grupa = doc.createElement("grupa");
                grupa.appendChild(doc.createTextNode(s.getGrupa().toString()));
                student.appendChild(grupa);

                Element email = doc.createElement("email");
                email.appendChild(doc.createTextNode(s.getEmail()));
                student.appendChild(email);

                Element indrumator = doc.createElement("indrumator");
                indrumator.appendChild(doc.createTextNode(s.getIndrumator()));
                student.appendChild(indrumator);


                // write the content into xml file
            }
            TransformerFactory transformerFactory = TransformerFactory.newInstance();

            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(new File("./src/main/resources/Student.xml"));
            transformer.transform(source, result);

            // Output to console for testing
            StreamResult consoleResult = new StreamResult(System.out);
            transformer.transform(source, consoleResult);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
