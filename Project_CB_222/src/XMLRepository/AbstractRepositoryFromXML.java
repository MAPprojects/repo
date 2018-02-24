package XMLRepository;

import Domain.HasID;
import Domain.Student;
import Repository.AbstractRepository;
import Repository.Validator;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.*;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class AbstractRepositoryFromXML <Id,E extends HasID<Id>> extends AbstractRepository<Id,E> {

    private String fileName;

    public AbstractRepositoryFromXML(Validator validator, String fileName) {
        super(validator);
        this.fileName = fileName;
    }

    public DocumentBuilder createDocumentBuilder(){
        try {
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            return dBuilder;
        }
        catch (Exception e){
            e.printStackTrace();
            return null;
        }

    }

    public Document loadDocument() {
        try {
            DocumentBuilder dBuilder = createDocumentBuilder();
            File fXmlFile = new File(fileName);

            Document doc = dBuilder.parse(fXmlFile);

            doc.getDocumentElement().normalize();

            return doc;

        }
        catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    public void saveXML(Document doc){
        try {
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(new File(fileName));

            transformer.transform(source, result);

            System.out.println("File saved!");
        }
        catch (Exception e){
            e.printStackTrace();
        }

    }

}
