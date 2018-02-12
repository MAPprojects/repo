package Repository;

import Domain.LabHomework;
import Validator.Validator;
import Validator.ValidatorException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.IOException;

public class LabHomeworkXmlRepository extends AbstractRepository<Integer, LabHomework>{

    private String xmlFile;

    public LabHomeworkXmlRepository(Validator<LabHomework> val,String xml) {
        super(val);
        xmlFile=xml;
        parseXmlResource();
    }

    private void parseXmlResource()
    {
        File fileToParse=new File(xmlFile);
        DocumentBuilderFactory documentBuilderFactory=DocumentBuilderFactory.newInstance();
        try {
            DocumentBuilder documentBuilder=documentBuilderFactory.newDocumentBuilder();
            Document document=documentBuilder.parse(fileToParse);
            document.getDocumentElement().normalize();
            NodeList hwsNodeList=document.getElementsByTagName("homework");

            for(int i=0;i<hwsNodeList.getLength();i++)
            {
                Node currentNode=hwsNodeList.item(i);
                if(currentNode.getNodeType()==Node.ELEMENT_NODE)
                {
                    Element el=(Element)currentNode;
                    int id=Integer.parseInt(el.getElementsByTagName("id").item(0).getTextContent());
                    int deadline=Integer.parseInt(el.getElementsByTagName("deadline").item(0).getTextContent());
                    int delays=Integer.parseInt(el.getElementsByTagName("delays").item(0).getTextContent());
                    super.save(new LabHomework(id,deadline,el.getElementsByTagName("description").item(0).getTextContent(),delays));
                }
            }
        }
        catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ValidatorException e) {
            e.printStackTrace();
        }

    }
    @Override
    public void save(LabHomework homework) throws ValidatorException {

        File fileToParse=new File(xmlFile);
        DocumentBuilderFactory documentBuilderFactory=DocumentBuilderFactory.newInstance();
        DocumentBuilder documentBuilder= null;
        try {
            super.save(homework);
            documentBuilder = documentBuilderFactory.newDocumentBuilder();
            Document document=documentBuilder.parse(fileToParse);

            Node hw=document.createElement("homework");

            Node id=document.createElement("id");
            id.setTextContent(String.valueOf(homework.getId()));
            hw.appendChild(id);

            Node deadline=document.createElement("deadline");
            deadline.setTextContent(String.valueOf(homework.getDeadline()));
            hw.appendChild(deadline);

            Node desc=document.createElement("description");
            desc.setTextContent(homework.getDescription());
            hw.appendChild(desc);

            Node delays=document.createElement("delays");
            delays.setTextContent(String.valueOf(homework.getDelayNumber()));
            hw.appendChild(delays);


            document.getFirstChild().appendChild(hw);
            TransformerFactory transformerFactory=TransformerFactory.newInstance();
            Transformer transformer=transformerFactory.newTransformer();

            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");

            DOMSource source=new DOMSource(document);
            StreamResult result=new StreamResult(new File(xmlFile));
            transformer.transform(source,result);
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (TransformerConfigurationException e) {
            e.printStackTrace();
        } catch (TransformerException e) {
            e.printStackTrace();
        }
    }


    private Element findXmlElement(Document document,Integer integer)
    {

        NodeList all=document.getElementsByTagName("homework");
        for(int i=0;i<all.getLength();i++)
        {
            Element currentEl=(Element)all.item(i);
            if(currentEl.getElementsByTagName("id").item(0).getTextContent().equals(String.valueOf(integer)))
            {
                return currentEl;
            }
        }
        return null;
    }


    @Override
    public void update(Integer integer, LabHomework homework) {
        DocumentBuilderFactory documentBuilderFactory=DocumentBuilderFactory.newInstance();
        DocumentBuilder documentBuilder= null;
        try
        {
            super.update(integer, homework);
            documentBuilder = documentBuilderFactory.newDocumentBuilder();
            Document document=documentBuilder.parse(new File(xmlFile));
            Element elementToUpdate=findXmlElement(document,integer);
            elementToUpdate.getElementsByTagName("deadline").item(0).setTextContent(String.valueOf(homework.getDeadline()));

            TransformerFactory transformerFactory=TransformerFactory.newInstance();
            Transformer transformer=transformerFactory.newTransformer();


            DOMSource source=new DOMSource(document);
            StreamResult result=new StreamResult(new File(xmlFile));
            transformer.transform(source,result);
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (TransformerConfigurationException e) {
            e.printStackTrace();
        } catch (TransformerException e) {
            e.printStackTrace();
        }
    }

    public void updateDelays(int integer)
    {
        DocumentBuilderFactory documentBuilderFactory=DocumentBuilderFactory.newInstance();
        try {
            DocumentBuilder documentBuilder= documentBuilderFactory.newDocumentBuilder();
            Document document=documentBuilder.parse(new File(xmlFile));
            Element elementToUpdate=findXmlElement(document,integer);
            int newDelay=Integer.parseInt(elementToUpdate.getElementsByTagName("delays").item(0).getTextContent())+1;
            elementToUpdate.getElementsByTagName("delays").item(0).setTextContent(String.valueOf(newDelay));

            TransformerFactory transformerFactory=TransformerFactory.newInstance();
            Transformer transformer=transformerFactory.newTransformer();


            DOMSource source=new DOMSource(document);
            StreamResult result=new StreamResult(new File(xmlFile));
            transformer.transform(source,result);

        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (TransformerConfigurationException e) {
            e.printStackTrace();
        } catch (TransformerException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        }
    }
}
