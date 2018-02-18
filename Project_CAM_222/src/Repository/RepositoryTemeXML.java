package Repository;

import Domain.Tema;
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

public class RepositoryTemeXML extends AbstractFileRepository<Integer, Tema> {

    public RepositoryTemeXML(Validator<Tema> validator, String numeFisier) {
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
            NodeList nList = doc.getElementsByTagName("tema");

            for (int temp = 0; temp < nList.getLength(); temp++) {
                Node nNode = nList.item(temp);
                if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element eElement = (Element) nNode;
                    Integer id = Integer.parseInt(eElement.getAttribute("id"));
                    String descriere = eElement.getElementsByTagName("descriere").item(0).getTextContent();
                    Integer deadline = Integer.parseInt(eElement.getElementsByTagName("deadline").item(0).getTextContent());
                    super.save(new Tema(id, descriere, deadline));
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

            for (Tema t : getAll()) {
                Element temaElement = doc.createElement("tema");
                rootElement.appendChild(temaElement);

                Attr attr = doc.createAttribute("id");
                attr.setValue(t.getId().toString());
                temaElement.setAttributeNode(attr);

                Element nume = doc.createElement("descriere");
                nume.appendChild(doc.createTextNode(t.getDescriere()));
                temaElement.appendChild(nume);

                Element grupa = doc.createElement("deadline");
                grupa.appendChild(doc.createTextNode(t.getDeadline().toString()));
                temaElement.appendChild(grupa);
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