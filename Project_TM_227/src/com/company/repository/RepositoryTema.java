package com.company.repository;

import com.company.domain.IValidator;
import com.company.domain.Tema;
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

public class RepositoryTema extends AbstractCRUDRepository<Integer, Tema> {
    public RepositoryTema(IValidator<Tema> validator) {
        super(validator);
        //loadData();
        //loadStream();
        loadDataXML();
    }

    /*
    public void loadData() {
        Tema t1 = new Tema(1, "Cerinta numarul 1", 2);
        Tema t2 = new Tema(2, "Cerinta numarul 2", 5);
        Tema t3 = new Tema(3, "Cerinta numarul 3", 14);
        super.entities.put(t1.getId(), t1);
        super.entities.put(t2.getId(), t2);
        super.entities.put(t3.getId(), t3);
    }*/


    private static Tema parseTema(String line){
        try{
            String[] val=line.split("[;]");
            int id = Integer.parseInt(val[0]);
            Tema tl=new Tema(id,val[1],Integer.parseInt(val[2]));
            return tl;}catch(NumberFormatException e){System.err.println("id-ul si deadline tre sa fie numere");}
        return null;
    }

    @Override
    public void loadStream(){
        try {
            Stream<String> s = Files.lines(Paths.get("Teme"));
            s.forEach(x->{if (parseTema(x)!=null) try {
                super.save(parseTema(x));
            } catch (Exception e) {
                e.printStackTrace();
            }
            });
        }catch(IOException e){throw new RepositoryException("Nu s-a putut citi din fisier");}
    }

    @Override
    public void saveStream(){
        try
                (BufferedWriter br=Files.newBufferedWriter(Paths.get("Teme"))){
                for(Tema tl: super.findAll()){
                    br.write(tl.getId()+";"+tl.getCerinta()+";"+tl.getDeadline());
                    br.newLine();
            }
        }catch(IOException e){
            throw new RepositoryException("Nu s-a putut scrie in fisier ");
        }
    }

    @Override
    public void loadData(){
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader("Teme"));
            String line;

            while((line = br.readLine())!= null)
            {
                String[] fields = line.split(";");

                if(fields.length != 3){
                    try {
                        throw new Exception("Fisier corupt/invalid!");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                Tema t = new Tema(Integer.parseInt(fields[0]), fields[1], Integer.parseInt(fields[2]));

                try {
                    super.save(t);
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
            bw = new BufferedWriter(new FileWriter("Teme"));

            for (Tema t: super.findAll()){
                String line = "";
                line += t.getId() + ";";
                line += t.getCerinta() + ";";
                line += t.getDeadline();
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
            File fXmlFile = new File("Teme.xml");
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(fXmlFile);

            doc.getDocumentElement().normalize();

            NodeList nList = doc.getElementsByTagName("tema");

            for (int temp = 0; temp < nList.getLength(); temp++) {

                Node nNode = nList.item(temp);

                if (nNode.getNodeType() == Node.ELEMENT_NODE) {

                    Element eElement = (Element) nNode;
                    int id = Integer.parseInt(eElement.getAttribute("idTema"));
                    String cerinta = eElement.getElementsByTagName("cerinta").item(0).getTextContent();
                    int deadline = Integer.parseInt(eElement.getElementsByTagName("deadline").item(0).getTextContent());

                    Tema t = new Tema(id, cerinta, deadline);
                    super.entities.put(t.getId(),t);
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

            for (Tema t:findAll()){

                // staff elements
                Element staff = doc.createElement("tema");
                rootElement.appendChild(staff);

                // set attribute to staff element
                Attr attr = doc.createAttribute("idTema");
                attr.setValue(String.valueOf(t.getId()));
                staff.setAttributeNode(attr);

                // nume elements
                Element nume = doc.createElement("cerinta");
                nume.appendChild(doc.createTextNode(t.getCerinta()));
                staff.appendChild(nume);

                // grupa elements
                Element grupa = doc.createElement("deadline");
                grupa.appendChild(doc.createTextNode(String.valueOf(t.getDeadline())));
                staff.appendChild(grupa);
            }

            // write the content into xml file
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(new File("Teme.xml"));

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
