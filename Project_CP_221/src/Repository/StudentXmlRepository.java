package Repository;
import Domain.HasId;
import Domain.Student;
import Utils.Groups;
import Utils.Names;
import Utils.Surname;

import Utils.Professors;
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
import java.util.Optional;
import java.util.Random;

public class StudentXmlRepository extends AbstractRepository<Integer, Student> {
    private String xmlFile;
    public StudentXmlRepository(Validator<Student> val,String xml) {
        super(val);
        xmlFile=xml;
        //populateXml();
        parseXmlResource();
       // parseXmlResourceNbyN(0,10);
    }

    private void parseXmlNode(Node currentNode)
    {
        if(currentNode.getNodeType()==Node.ELEMENT_NODE)
        {
            Element el=(Element)currentNode;
            int id=Integer.parseInt(el.getElementsByTagName("id").item(0).getTextContent());
            try {
                super.save(new Student(Integer.parseInt(el.getElementsByTagName("id").item(0).getTextContent())
                        ,el.getElementsByTagName("name").item(0).getTextContent(),el.getElementsByTagName("email").item(0).getTextContent(),
                        el.getElementsByTagName("group").item(0).getTextContent(),el.getElementsByTagName("professor").item(0).getTextContent(),
                        Integer.parseInt(el.getElementsByTagName("grade").item(0).getTextContent()), Integer.parseInt(el.getElementsByTagName("numberOfGrades").item(0).getTextContent())));
                } catch (ValidatorException e) {
                e.printStackTrace();
            }
            super.findOne(id).setHomeworkOnTime(Byte.parseByte(el.getElementsByTagName("homeworkOnTime").item(0).getTextContent()));
        }
    }

    public void parseXmlResourceNbyN(int fromIndex,int toIndex) {

        File fileToParse=new File(xmlFile);
        DocumentBuilderFactory documentBuilderFactory=DocumentBuilderFactory.newInstance();
        try {
            DocumentBuilder documentBuilder=documentBuilderFactory.newDocumentBuilder();
            Document document=documentBuilder.parse(fileToParse);
            NodeList studentsNodeList=document.getElementsByTagName("student");
            for(int i=fromIndex;i<toIndex;i++)
            {
                Node currentNode=studentsNodeList.item(i);
                parseXmlNode(currentNode);
            }
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
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
            NodeList studentsNodeList=document.getElementsByTagName("student");
            for(int i=0;i<studentsNodeList.getLength();i++)
            {
                Node currentNode=studentsNodeList.item(i);
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



    private void populateXml()
    {
        DocumentBuilderFactory documentBuilderFactory=DocumentBuilderFactory.newInstance();
        DocumentBuilder documentBuilder= null;
        Random generator=new Random();
        try {
            documentBuilder = documentBuilderFactory.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }

        Document document=documentBuilder.newDocument();
        Element rootElement=document.createElement("Students");
        document.appendChild(rootElement);

        for(int i=1;i<=30;i++)
        {
            String name1= Names.values()[generator.nextInt(Names.values().length)].toString();
            String name2= Surname.values()[generator.nextInt(Surname.values().length)].toString();
            String emaill=name1+"."+name2+"@yahoo.com";
            String prof= Professors.values()[generator.nextInt(Professors.values().length)].toString();
            String grp= Groups.values()[generator.nextInt(Groups.values().length)].toString();

            Element student=document.createElement("student");
            rootElement.appendChild(student);


            Node id=document.createElement("id");
            id.setTextContent(String.valueOf(i));
            student.appendChild(id);

            Node name=document.createElement("name");
            name.setTextContent(name1+" "+name2);
            student.appendChild(name);

            Node email=document.createElement("email");
            email.setTextContent(emaill);
            student.appendChild(email);

            Node group=document.createElement("group");
            group.setTextContent(grp);
            student.appendChild(group);

            Node professor=document.createElement("professor");
            professor.setTextContent(prof);
            student.appendChild(professor);

            Node grade=document.createElement("grade");
            grade.setTextContent(String.valueOf(0));
            student.appendChild(grade);

            Node numberGrades=document.createElement("numberOfGrades");
            numberGrades.setTextContent(String.valueOf(0));
            student.appendChild(numberGrades);

            Node homeworkOnTime=document.createElement("homeworkOnTime");
            homeworkOnTime.setTextContent("1");
            student.appendChild(homeworkOnTime);

        }
        document.getDocumentElement().normalize();
        TransformerFactory transformerFactory=TransformerFactory.newInstance();
        try {
            Transformer transformer=transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
            DOMSource source=new DOMSource(document);
            StreamResult result=new StreamResult(new File("students.xml"));
            transformer.transform(source,result);
        } catch (TransformerConfigurationException e) {
            e.printStackTrace();
        } catch (TransformerException e) {
            e.printStackTrace();
        }

    }


    @Override
    public void save(Student student) throws ValidatorException {

        File fileToParse=new File(xmlFile);
        DocumentBuilderFactory documentBuilderFactory=DocumentBuilderFactory.newInstance();
        DocumentBuilder documentBuilder= null;
        try {
            super.save(student);
            documentBuilder = documentBuilderFactory.newDocumentBuilder();
            Document document=documentBuilder.parse(fileToParse);

            Node stud=document.createElement("student");
            Node id=document.createElement("id");
            id.setTextContent(String.valueOf(student.getId()));
            stud.appendChild(id);

            Node name=document.createElement("name");
            name.setTextContent(student.getName());
            stud.appendChild(name);

            Node email=document.createElement("email");
            email.setTextContent(student.getEmail());
            stud.appendChild(email);

            Node group=document.createElement("group");
            group.setTextContent(student.getGroup());
            stud.appendChild(group);

            Node professor=document.createElement("professor");
            professor.setTextContent(student.getProfessor());
            stud.appendChild(professor);

            Node grade=document.createElement("grade");
            grade.setTextContent(String.valueOf(0));
            stud.appendChild(grade);

            Node numberGrades=document.createElement("numberOfGrades");
            numberGrades.setTextContent(String.valueOf(0));
            stud.appendChild(numberGrades);

            Node hwOnTime=document.createElement("homeworkOnTime");
            hwOnTime.setTextContent(String.valueOf(1));
            stud.appendChild(hwOnTime);

            System.out.println("sss");

            document.getFirstChild().appendChild(stud);
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


    private Element findXmlElement(Document document,Integer integer)
    {

        NodeList all=document.getElementsByTagName("student");
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
    public void delete(Integer integer) {
        DocumentBuilderFactory documentBuilderFactory=DocumentBuilderFactory.newInstance();
        DocumentBuilder documentBuilder= null;
        try
        {
            super.delete(integer);
            documentBuilder = documentBuilderFactory.newDocumentBuilder();
            Document document=documentBuilder.parse(new File(xmlFile));

            Element elementToDelete=findXmlElement(document,integer);
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

    @Override
    public void update(Integer integer, Student student) {
        DocumentBuilderFactory documentBuilderFactory=DocumentBuilderFactory.newInstance();
        DocumentBuilder documentBuilder= null;
        try
        {
            super.update(integer, student);
            documentBuilder = documentBuilderFactory.newDocumentBuilder();
            Document document=documentBuilder.parse(new File(xmlFile));
            Element elementToUpdate=findXmlElement(document,integer);
            elementToUpdate.getElementsByTagName("name").item(0).setTextContent(student.getName());
            elementToUpdate.getElementsByTagName("email").item(0).setTextContent(student.getEmail());
            elementToUpdate.getElementsByTagName("group").item(0).setTextContent(student.getGroup());
            elementToUpdate.getElementsByTagName("professor").item(0).setTextContent(student.getProfessor());

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
   @Override
   public int size()
   {
       DocumentBuilderFactory documentBuilderFactory=DocumentBuilderFactory.newInstance();
       try {
           DocumentBuilder  documentBuilder = documentBuilderFactory.newDocumentBuilder();
           Document document=documentBuilder.parse(new File(xmlFile));
           NodeList studentsNodeList=document.getElementsByTagName("student");
           return studentsNodeList.getLength();
       } catch (ParserConfigurationException e) {
           e.printStackTrace();
       } catch (SAXException e) {
           e.printStackTrace();
       } catch (IOException e) {
           e.printStackTrace();
       }
       return super.size();
   }

   public void updateHomeworkOnTime(int id)
   {
       DocumentBuilderFactory documentBuilderFactory=DocumentBuilderFactory.newInstance();
       DocumentBuilder documentBuilder= null;
       try {
           documentBuilder = documentBuilderFactory.newDocumentBuilder();
           Document document=documentBuilder.parse(new File(xmlFile));
           Element elementToUpdate=findXmlElement(document,id);
           elementToUpdate.getElementsByTagName("homeworkOnTime").item(0).setTextContent(String.valueOf(super.findOne(id).getHomeworkOnTime()));

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

}
