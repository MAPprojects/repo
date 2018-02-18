package Repository;

import Domain.HasID;
import ValidatorsAndExceptions.Validator;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;


import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public abstract class AbstractRepositoryXML<E extends HasID<ID>,ID> extends AbstractRepository<E,ID>
{

    private String fileName;

    public AbstractRepositoryXML(String filename, Validator<E>validator)
    {
        super(validator);
        this.fileName=filename;
        loadData();
    }

    public abstract E createInstance(Element element);
    public abstract void writeInstace(Document doc, String tagName, String textNode, Element element);
    public abstract void writeToFile();


    Document loadDocument()
    {
        try {
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = null;
            Document doc=null;
            docBuilder = docFactory.newDocumentBuilder();
            doc= docBuilder.parse(new FileInputStream(fileName));
            return doc;
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void loadData() {
        try {
            Document document = loadDocument();
            Node root = document.getDocumentElement();
            NodeList nodeList = root.getChildNodes();
            for (int i = 0; i < nodeList.getLength(); i++) {
                Node node = nodeList.item(i);
                if (node.getNodeType() == Node.ELEMENT_NODE) {//node instanceof Element
                    Element element = (Element) node;
                    E entity = createInstance(element);
                    super.add(entity);
                }
            }
        }catch(Exception ex){}
    }

    void saveDocument(Document doc) {
        // write the content into xml file
        DOMSource source = new DOMSource(doc);
        StreamResult result = new StreamResult(new File(fileName));
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = null;
        try {
            transformer = transformerFactory.newTransformer();
        } catch (TransformerConfigurationException e) {
            e.printStackTrace();
        }
        try {
            transformer.transform(source, result);
        } catch (TransformerException e) {
            e.printStackTrace();
        }
        System.out.println("File saved!");
    }
    @Override
    public void add(E entity)
    {
        super.add(entity);
        writeToFile();
    }
    @Override
    public E delete(ID id)
    {
        E old=super.delete(id);
        writeToFile();
        return old;
    }
    @Override
    public void update(E entity)
    {
        super.update(entity);
        writeToFile();
    }
}
