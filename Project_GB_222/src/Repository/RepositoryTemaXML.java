package Repository;

import Domain.Student;
import Domain.Tema;
import ExceptionsAndValidators.IValidator;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

public class RepositoryTemaXML extends AbstractXMLRepository<Tema,Integer>
{
    public RepositoryTemaXML(String filename, IValidator<Tema> validator) {
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
            Element rootElement = doc.createElement("teme");
            doc.appendChild(rootElement);
            getAll().forEach(x->{
                Element studentElement = doc.createElement("tema");
                studentElement.setAttribute("nrTema", x.getID().toString());
                writeObject(doc,"descriere",x.getDescriere(),studentElement);
                writeObject(doc,"deadline",x.getDeadline().toString(),studentElement);
                rootElement.appendChild(studentElement);
            });
            super.saveDocument(doc);
        } catch (ParserConfigurationException pce) {
            pce.printStackTrace();
        }
    }

    @Override
    public Tema createObject(Element element) {

        String id=element.getAttributeNode("nrTema").getValue();
        String desc=element.getElementsByTagName("descriere").item(0).getTextContent();
        String deadline=element.getElementsByTagName("deadline").item(0).getTextContent();
        return new Tema(new Integer(id),desc,new Integer(deadline));

    }

    @Override
    public void writeObject(Document doc, String tagName, String textNode, Element studElem) {
        Element element=doc.createElement(tagName);
        element.appendChild(doc.createTextNode(textNode));
        studElem.appendChild(element);

    }
}
