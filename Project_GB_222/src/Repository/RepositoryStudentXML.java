package Repository;

import Domain.Student;
import ExceptionsAndValidators.IValidator;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;


public class RepositoryStudentXML extends AbstractXMLRepository<Student,Integer>
{
    public RepositoryStudentXML(String filename, IValidator<Student> validator) {
        super(filename, validator);
        super.loadData();
    }

    @Override
    public void writeToFile() {
        try {
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
// root element
            Document doc = docBuilder.newDocument();
            Element rootElement = doc.createElement("students");
            doc.appendChild(rootElement);
            getAll().forEach(x->{
                Element studentElement = doc.createElement("student");
                studentElement.setAttribute("idStudent", x.getID().toString());
                writeObject(doc,"nume",x.getNume(),studentElement);
                writeObject(doc,"grupa",x.getGrupa().toString(),studentElement);
                writeObject(doc,"email",x.getEmail(),studentElement);
                writeObject(doc,"cadruDidactic",x.getCadruDidactic(),studentElement);
                rootElement.appendChild(studentElement);
            });
            super.saveDocument(doc);
        } catch (ParserConfigurationException pce) {
            pce.printStackTrace();
        }
    }

    @Override
    public Student createObject(Element element) {

        String id=element.getAttributeNode("idStudent").getValue();
        String nume=element.getElementsByTagName("nume").item(0).getTextContent();
        String grupa=element.getElementsByTagName("grupa").item(0).getTextContent();
        String email=element.getElementsByTagName("email").item(0).getTextContent();
        String cadruDidacitc=element.getElementsByTagName("cadruDidactic").item(0).getTextContent();
        return new Student(new Integer(id),nume,new Integer(grupa),email,cadruDidacitc);

    }

    @Override
    public void writeObject(Document doc, String tagName, String textNode, Element studElem) {
        Element element=doc.createElement(tagName);
        element.appendChild(doc.createTextNode(textNode));
        studElem.appendChild(element);

    }
}
