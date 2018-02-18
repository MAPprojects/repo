package Repository;

import Domain.Student;
import Domain.Tema;
import ValidatorsAndExceptions.Validator;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

public class TemaRepositoryXML  extends AbstractRepositoryXML<Tema,Integer>{

    public TemaRepositoryXML(String filename, Validator< Tema > validator) {
        super(filename, validator);
    }

    @Override
    public Tema createInstance(Element element) {
        String nrTema=element.getAttributeNode("nrTema").getValue();
        String descriere=element.getElementsByTagName("descriere").item(0).getTextContent();
        String deadline=element.getElementsByTagName("deadline").item(0).getTextContent();
        return new Tema(Integer.parseInt(nrTema),descriere,Integer.parseInt(deadline));

    }

    @Override
    public void writeInstace(Document doc, String tagName, String textNode, Element temaElement) {
        Element element=doc.createElement(tagName);
        element.appendChild(doc.createTextNode(textNode));
        temaElement.appendChild(element);
    }

    @Override
    public void writeToFile() {
        try {
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

            Document doc = docBuilder.newDocument();
            Element rootElement = doc.createElement("teme");
            doc.appendChild(rootElement);
            getAll().forEach(x->{
                Element temaElement = doc.createElement("tema");
                temaElement.setAttribute("nrTema", x.getID().toString());
                writeInstace(doc,"descriere",x.getDescriere(),temaElement);
                writeInstace(doc,"deadline",x.getDeadline().toString(),temaElement);
                rootElement.appendChild(temaElement);
            });
            super.saveDocument(doc);
        } catch (ParserConfigurationException pce) {
            pce.printStackTrace();
        }
    }
}
