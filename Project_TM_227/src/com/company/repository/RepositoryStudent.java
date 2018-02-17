package com.company.repository;

import com.company.domain.IValidator;
import com.company.domain.Student;
import com.company.domain.ValidationException;
import org.w3c.dom.*;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;

public class RepositoryStudent extends AbstractCRUDRepository<Integer, Student> {
    public RepositoryStudent(IValidator<Student> validator) {
        super(validator);
        //loadData();
        //loadStream();
        loadDataXML();
    }

   /*
    public void loadData() {
        Student s1= new Student(1, "Popescu Vasile", 227, "popescu@yahoo.com", "Vasilescu Dan");
        Student s2= new Student(2, "Popoviciu Maria", 227, "popo_maria@gmail.com", "Vasilescu Dan");
        Student s3= new Student(3, "Iordache Alina", 213, "iordache_ali17@yahoo.com", "Vasilescu Dan");
        Student s4= new Student(4, "Ancau Anca", 1, "anca_a@yahoo.com", "Apostol Mirela");
        super.entities.put(s1.getId(),s1);
        super.entities.put(s2.getId(),s2);
        super.entities.put(s3.getId(),s3);
        super.entities.put(s4.getId(),s4);
    }*/

    private static Student parseStudent(String line){
        try{
            String[] val=line.split("[;]");
            int id = Integer.parseInt(val[0]);
            Student st=new Student(id,val[1],Integer.parseInt(val[2]),val[3],val[4]);
            return st;}
        catch(NumberFormatException e){System.err.println("id-ul tre sa fie numar si grupa");}
        return null;
    }

    @Override
    public void loadStream(){
        try{
            Stream<String> s= Files.lines(Paths.get("Studenti"));
            s.forEach(x->{if(parseStudent(x)!=null) try {
                super.save(parseStudent(x));
            } catch (Exception e) {
                e.printStackTrace();
            }
            });
        }catch(IOException e){
            throw new RepositoryException("Nu s-a putut citi din fisier");
        }
    }

    @Override
    public void saveStream(){
        try(BufferedWriter br= Files.newBufferedWriter(Paths.get("Studenti"))){
            for(Student st:super.findAll()) {
                br.write(st.getId()+";"+st.getNume()+";"+st.getGrupa()+";"+st.getEmail()+";"+st.getCadru_didactic());
                br.newLine();
            }
        }catch(IOException e){throw new RepositoryException("Nu s-a putut scrie in fisier");}
    }


    @Override
    public void loadData(){
        BufferedReader br = null;

        try {
            br = new BufferedReader(new FileReader("Studenti"));
            String line;

            while((line = br.readLine())!= null)
            {
                String[] fields = line.split(";");

                if(fields.length != 5){
                    try {
                        throw new Exception("Fisier corupt/invalid!");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                Student s = new Student(Integer.parseInt(fields[0]), fields[1], Integer.parseInt(fields[2]), fields[3], fields[4]);

                try {
                    super.save(s);
                } catch (ValidationException e) {
                    System.out.println(e.getMessage());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    System.out.println("Cannot close fileReader");
                }

            }
        }
    }


    @Override
    public void saveData() {
        BufferedWriter bw = null;
        try {
            bw = new BufferedWriter(new FileWriter("Studenti"));

            for (Student s: super.findAll()){
                String line = "";
                line += s.getId() + ";";
                line += s.getNume() + ";";
                line += s.getGrupa() + ";";
                line += s.getEmail() + ";";
                line += s.getCadru_didactic();
                line += "\n";
                bw.write(line);
            }
        } catch (FileNotFoundException e) {
            System.out.println("Can't find the file!");
        } catch (IOException e) {
            System.out.println("Can't read the file!");
        }
        finally {
            if (bw != null) {
                try {
                    bw.close();
                } catch (IOException e) {
                    System.out.println("Cannot close fileReader");
                }
            }
        }
    }


    @Override
    public void loadDataXML()
    {
        try {
            File fXmlFile = new File("Studenti.xml");
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(fXmlFile);

            doc.getDocumentElement().normalize();

            NodeList nList = doc.getElementsByTagName("student");

            for (int temp = 0; temp < nList.getLength(); temp++) {

                Node nNode = nList.item(temp);

                if (nNode.getNodeType() == Node.ELEMENT_NODE) {

                    org.w3c.dom.Element eElement = (org.w3c.dom.Element) nNode;
                    int id = Integer.parseInt(eElement.getAttribute("idStudent"));
                    String nume = eElement.getElementsByTagName("nume").item(0).getTextContent();
                    int grupa = Integer.parseInt(eElement.getElementsByTagName("grupa").item(0).getTextContent());
                    String email = eElement.getElementsByTagName("email").item(0).getTextContent();
                    String cadru = eElement.getElementsByTagName("cadru_didactic").item(0).getTextContent();

                    Student s = new Student(id, nume, grupa, email, cadru);
                    super.entities.put(s.getId(),s);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void saveDataXML(){
        try {

            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

            // root elements
            Document doc = docBuilder.newDocument();
            Element rootElement = doc.createElement("company");
            doc.appendChild(rootElement);

            for (Student s:findAll()){

                // staff elements
                Element staff = doc.createElement("student");
                rootElement.appendChild(staff);

                // set attribute to staff element
                Attr attr = doc.createAttribute("idStudent");
                attr.setValue(String.valueOf(s.getId()));
                staff.setAttributeNode(attr);

                // nume elements
                Element nume = doc.createElement("nume");
                nume.appendChild(doc.createTextNode(s.getNume()));
                staff.appendChild(nume);

                // grupa elements
                Element grupa = doc.createElement("grupa");
                grupa.appendChild(doc.createTextNode(String.valueOf(s.getGrupa())));
                staff.appendChild(grupa);

                // email elements
                Element email = doc.createElement("email");
                email.appendChild(doc.createTextNode(s.getEmail()));
                staff.appendChild(email);

                // cadru_didactic elements
                Element cadru_didactic = doc.createElement("cadru_didactic");
                cadru_didactic.appendChild(doc.createTextNode(s.getCadru_didactic()));
                staff.appendChild(cadru_didactic);
            }

            // write the content into xml file
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(new File("Studenti.xml"));

            transformer.transform(source, result);

        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (TransformerConfigurationException e) {
            e.printStackTrace();
        } catch (TransformerException e) {
            e.printStackTrace();
        }
    }

}
