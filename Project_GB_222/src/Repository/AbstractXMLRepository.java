package Repository;

import Domain.HasID;
import ExceptionsAndValidators.IValidator;
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
import java.util.Optional;

public abstract class AbstractXMLRepository<T extends HasID<ID>,ID> extends AbstractRepository<T,ID>
{
    private String filename;

    public AbstractXMLRepository(String filename, IValidator<T> validator)
    {
        super(validator);
        this.filename=filename;
//        loadData();
    }

    Document loadDocument() {
        try {
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = null;
            Document doc=null;
            docBuilder = docFactory.newDocumentBuilder();
            doc= docBuilder.parse(new FileInputStream(filename));
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
        try
        {
        Document document =loadDocument();
        Node root = document.getDocumentElement();
            NodeList nodeList = root.getChildNodes();
            for (int i = 0; i < nodeList.getLength(); i++) {
                Node node = nodeList.item(i);
                if (node.getNodeType() == Node.ELEMENT_NODE) {//node instanceof Element
                    Element element = (Element) node;
                    T elem = createObject(element);
                    super.add(elem);
                }
            }
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
    }

    void saveDocument(Document doc) {
// write the content into xml file
        DOMSource source = new DOMSource(doc);
        StreamResult result = new StreamResult(new File(filename));
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

    public abstract void writeToFile();


    public abstract T createObject(Element element);

    public abstract void writeObject(Document doc, String tagName, String textNode, Element element);

    @Override
    public void add(T type)
    {
        super.add(type);
        writeToFile();
    }

    @Override
    public Optional<T> delete(ID id)
    {
        Optional<T> temp = super.delete(id);
        if(temp.isPresent())
            writeToFile();
        return temp;
    }

    @Override
    public void update(T type)
    {
        super.update(type);
        writeToFile();
    }
}
