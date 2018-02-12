package Repository;

import Domain.Grade;
import Domain.LabHomework;
import Validator.Validator;
import javafx.util.Pair;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import Validator.ValidatorException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.IOException;

public class GradeXmlRepository extends AbstractRepository<Pair<Integer,Integer>,Grade> {

    private String xmlFile;
    private String xmlStsFile;
    public GradeXmlRepository(Validator<Grade> val,String xml,String xmlSts) {
        super(val);
        xmlFile=xml;
        xmlStsFile=xmlSts;
        parseXmlResource();
    }

    private void parseXmlNode(Node currentNode)
    {
        if(currentNode.getNodeType()==Node.ELEMENT_NODE)
        {
            Element el=(Element)currentNode;
            try {
                super.save(new Grade(Integer.parseInt(el.getElementsByTagName("idStudent").item(0).getTextContent()),
                        Integer.parseInt(el.getElementsByTagName("idHomework").item(0).getTextContent()),
                        Integer.parseInt(el.getElementsByTagName("value").item(0).getTextContent()),
                        el.getElementsByTagName("name").item(0).getTextContent())
                        );
                        } catch (ValidatorException e) {
                e.printStackTrace();
            }
        }
    }

    private void parseXmlResource()
    {
        File fileToParse=new File(xmlFile);
        DocumentBuilderFactory documentBuilderFactory=DocumentBuilderFactory.newInstance();
        try {
            DocumentBuilder documentBuilder=documentBuilderFactory.newDocumentBuilder();
            Document document=documentBuilder.parse(fileToParse);
            document.getDocumentElement().normalize();
            NodeList grNodeList=document.getElementsByTagName("grade");
            for(int i=0;i<grNodeList.getLength();i++)
            {
                Node currentNode=grNodeList.item(i);
                    parseXmlNode(currentNode);

            }
        }
        catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void save(Grade grade) throws ValidatorException {

        File fileToParse=new File(xmlFile);
        DocumentBuilderFactory documentBuilderFactory=DocumentBuilderFactory.newInstance();
        DocumentBuilder documentBuilder= null;
        try {
            super.save(grade);
            documentBuilder = documentBuilderFactory.newDocumentBuilder();
            Document document=documentBuilder.parse(fileToParse);

            Node gr=document.createElement("grade");

            Node idS=document.createElement("idStudent");
            idS.setTextContent(String.valueOf(grade.getIdStudent()));
            gr.appendChild(idS);

            Node idH=document.createElement("idHomework");
            idH.setTextContent(String.valueOf(grade.getIdHomework()));
            gr.appendChild(idH);

            Node value=document.createElement("value");
            value.setTextContent(String.valueOf(grade.getValue()));
            gr.appendChild(value);

            Node name=document.createElement("name");
            name.setTextContent(grade.getName());
            gr.appendChild(name);

            document.getFirstChild().appendChild(gr);
            TransformerFactory transformerFactory=TransformerFactory.newInstance();
            Transformer transformer=transformerFactory.newTransformer();

            DOMSource source=new DOMSource(document);
            StreamResult result=new StreamResult(new File(xmlFile));
            transformer.transform(source,result);


            //salvam in student.xml
            documentBuilderFactory=DocumentBuilderFactory.newInstance();
            documentBuilder = documentBuilderFactory.newDocumentBuilder();
            document=documentBuilder.parse(xmlStsFile);
            NodeList allSts=document.getElementsByTagName("student");

            for(int i=0;i<allSts.getLength();i++)
            {
                int ids=grade.getIdStudent();
                Element currentEl=(Element)allSts.item(i);
                if(currentEl.getElementsByTagName("id").item(0).getTextContent().equals(String.valueOf(ids))) {
                    int numberOfGrades = Integer.parseInt(currentEl.getElementsByTagName("numberOfGrades").item(0).getTextContent());
                    int currentGrade = Integer.parseInt(currentEl.getElementsByTagName("grade").item(0).getTextContent());
                    currentEl.getElementsByTagName("numberOfGrades").item(0).setTextContent(String.valueOf(1 + numberOfGrades));
                    currentEl.getElementsByTagName("grade").item(0).setTextContent(String.valueOf(grade.getValue() + currentGrade));

                    source=new DOMSource(document);
                    result=new StreamResult(new File(xmlStsFile));
                    transformer.transform(source,result);
                    break;
                }
            }
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


    @Override
    public void delete(Pair<Integer,Integer> idG) {
        DocumentBuilderFactory documentBuilderFactory=DocumentBuilderFactory.newInstance();
        DocumentBuilder documentBuilder= null;
        try
        {
            super.delete(idG);
            documentBuilder = documentBuilderFactory.newDocumentBuilder();
            Document document=documentBuilder.parse(new File(xmlFile));

            Element elementToDelete=findXmlElement(document,idG);
            elementToDelete.getParentNode().removeChild(elementToDelete);

            TransformerFactory transformerFactory=TransformerFactory.newInstance();
            Transformer transformer=transformerFactory.newTransformer();


            DOMSource source=new DOMSource(document);
            StreamResult result=new StreamResult(new File(xmlFile));
            transformer.transform(source,result);
        }
        catch (ParserConfigurationException e) {
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


    private Element findXmlElement(Document document,Pair<Integer,Integer> id)
    {
        NodeList all=document.getElementsByTagName("grade");
        for(int i=0;i<all.getLength();i++)
        {
            Element currentEl=(Element)all.item(i);
            if(currentEl.getElementsByTagName("idStudent").item(0).getTextContent().equals(String.valueOf(id.getKey())) &&
                    currentEl.getElementsByTagName("idHomework").item(0).getTextContent().equals(String.valueOf(id.getValue()))
                    )
            {
                return currentEl;
            }
        }
        return null;
    }

        @Override
        public void update(Pair<Integer,Integer> id, Grade grade) {
        DocumentBuilderFactory documentBuilderFactory=DocumentBuilderFactory.newInstance();
        DocumentBuilder documentBuilder= null;
        try
        {
            super.update(id,grade);
            documentBuilder = documentBuilderFactory.newDocumentBuilder();
            Document document=documentBuilder.parse(new File(xmlFile));
            Element elementToUpdate=findXmlElement(document,id);
            int oldValue=Integer.parseInt(elementToUpdate.getElementsByTagName("value").item(0).getTextContent());
            elementToUpdate.getElementsByTagName("value").item(0).setTextContent(String.valueOf(grade.getValue()));

            TransformerFactory transformerFactory=TransformerFactory.newInstance();
            Transformer transformer=transformerFactory.newTransformer();


            DOMSource source=new DOMSource(document);
            StreamResult result=new StreamResult(new File(xmlFile));
            transformer.transform(source,result);

            //salvam in student.xml

            documentBuilderFactory=DocumentBuilderFactory.newInstance();
            documentBuilder = documentBuilderFactory.newDocumentBuilder();
            document=documentBuilder.parse(xmlStsFile);
            NodeList allSts=document.getElementsByTagName("student");

            for(int i=0;i<allSts.getLength();i++)
            {
                int ids=grade.getIdStudent();
                Element currentEl=(Element)allSts.item(i);
                if(currentEl.getElementsByTagName("id").item(0).getTextContent().equals(String.valueOf(ids))) {
                    int currentGrade = Integer.parseInt(currentEl.getElementsByTagName("grade").item(0).getTextContent());
                    currentEl.getElementsByTagName("grade").item(0).setTextContent(String.valueOf(currentGrade-oldValue+grade.getValue()));
                    source=new DOMSource(document);
                    result=new StreamResult(new File(xmlStsFile));
                    transformer.transform(source,result);
                    break;
                }
            }


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
}

