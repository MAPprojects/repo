package Repository;

import Domain.Student;
import ValidatorsAndExceptions.Validator;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;


public class StudentRepositoryXML  extends AbstractRepositoryXML<Student,Integer>{



    public StudentRepositoryXML(String filename, Validator < Student > validator) {
        super(filename, validator);
    }

    @Override
    public Student createInstance(Element element) {
        String id=element.getAttributeNode("id").getValue();
        String nume=element.getElementsByTagName("nume").item(0).getTextContent();
        String grupa=element.getElementsByTagName("grupa").item(0).getTextContent();
        String email=element.getElementsByTagName("email").item(0).getTextContent();
        String cadruDidactic=element.getElementsByTagName("cadruDidactic").item(0).getTextContent();
        return new Student(Integer.parseInt(id),nume,Integer.parseInt(grupa),email,cadruDidactic);

    }

    @Override
    public void writeInstace(Document doc, String tagName, String textNode, Element studentElement) {
        Element element=doc.createElement(tagName);
        element.appendChild(doc.createTextNode(textNode));
        studentElement.appendChild(element);
    }

    @Override
    public void writeToFile() {
        try {
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

            Document doc = docBuilder.newDocument();
            Element rootElement = doc.createElement("students");
            doc.appendChild(rootElement);
            getAll().forEach(x->{
                Element studentElement = doc.createElement("student");
                studentElement.setAttribute("id", x.getID().toString());
                writeInstace(doc,"nume",x.getNume(),studentElement);
                writeInstace(doc,"grupa",x.getGrupa().toString(),studentElement);
                writeInstace(doc,"email",x.getEmail(),studentElement);
                writeInstace(doc,"cadruDidactic",x.getCadruDidactic(),studentElement);
                rootElement.appendChild(studentElement);
            });
            super.saveDocument(doc);
        } catch (ParserConfigurationException pce) {
            pce.printStackTrace();
        }


    }


}
