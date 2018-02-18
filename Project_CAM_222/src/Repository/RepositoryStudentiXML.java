package Repository;

import Domain.Student;
import Domain.Validator;
import org.w3c.dom.*;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;

public class RepositoryStudentiXML extends AbstractFileRepository<Integer, Student> {

    public RepositoryStudentiXML(Validator<Student> validator, String numeFisier) {
        super(validator, numeFisier);
        incarcareFisier();
    }

    public void incarcareFisier() {
        try {
            File inputFile = new File(numeFisier);
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(inputFile);
            doc.getDocumentElement().normalize();
            NodeList nList = doc.getElementsByTagName("student");

            for (int temp = 0; temp < nList.getLength(); temp++) {
                Node nNode = nList.item(temp);
                if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element eElement = (Element) nNode;
                    Integer id = Integer.parseInt(eElement.getAttribute("id"));
                    String nume = eElement.getElementsByTagName("nume").item(0).getTextContent();
                    Integer grupa = Integer.parseInt(eElement.getElementsByTagName("grupa").item(0).getTextContent());
                    String email = eElement.getElementsByTagName("email").item(0).getTextContent();
                    String profIndrumator = eElement.getElementsByTagName("profIndrumator").item(0).getTextContent();
                    super.save(new Student(id, nume, grupa, email, profIndrumator));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void salvareFisier() {
        try {
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.newDocument();

            Element rootElement = doc.createElement("class");
            doc.appendChild(rootElement);

            for (Student s : getAll()) {
                Element studentElement = doc.createElement("student");
                rootElement.appendChild(studentElement);

                Attr attr = doc.createAttribute("id");
                attr.setValue(s.getId().toString());
                studentElement.setAttributeNode(attr);

                Element nume = doc.createElement("nume");
                nume.appendChild(doc.createTextNode(s.getNume()));
                studentElement.appendChild(nume);

                Element grupa = doc.createElement("grupa");
                grupa.appendChild(doc.createTextNode(s.getGrupa().toString()));
                studentElement.appendChild(grupa);

                Element email = doc.createElement("email");
                email.appendChild(doc.createTextNode(s.getEmail()));
                studentElement.appendChild(email);

                Element profIndrumator = doc.createElement("profIndrumator");
                profIndrumator.appendChild(doc.createTextNode(s.getProfIndrumator()));
                studentElement.appendChild(profIndrumator);
            }

            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty(OutputKeys.METHOD, "xml");
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(new File(numeFisier));
            transformer.transform(source, result);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
