package com.company.repository;

import com.company.domain.IValidator;
import com.company.domain.Nota;
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
import java.util.ArrayList;
import java.util.stream.Stream;

public class RepositoryNota extends AbstractCRUDRepository<Integer, Nota> {

    public RepositoryNota(IValidator<Nota> validator) {
        super(validator);
        //loadData();
        //loadStream();
        loadDataXML();
    }


    /*
    public void loadData() {
        Nota n1 = new Nota(1, 1, 1, 8, "");
        Nota n2 = new Nota(2, 1, 2, 10, "Nimic. ");
        super.entities.put(n1.getId(), n1);
        super.entities.put(n2.getId(), n2);
    }*/


    @Override
    public void loadStream(){
        try{
            Stream<String> s= Files.lines(Paths.get("Catalog"));
            s.forEach(x->{if(parseCatalog(x)!=null) try {
                super.save(parseCatalog(x));
            } catch (Exception e) {
                e.printStackTrace();
            }
            });
        }catch(IOException e){throw new RepositoryException("Nu s-a putut citi din fisier");}
    }
    private static Nota parseCatalog(String line){
        String[] val=line.split("[;]");
        try {
            int id = Integer.parseInt(val[0]);
            Nota c=new Nota(Integer.parseInt(val[0]), Integer.parseInt(val[1]), Integer.parseInt(val[2]), Integer.parseInt(val[3]), val[4]);
            return c;
        } catch (NumberFormatException e) {
            System.err.println("Id-ul tre sa fie numar");
        }
        return null;
    }

    @Override
    public void saveStream(){
        try(BufferedWriter br= Files.newBufferedWriter(Paths.get("Catalog"))){
            for(Nota c:super.findAll()){
                br.write(c.getId()+";"+c.getIdStudent()+";"+c.getIdTema()+";"+c.getValoare()+";"+c.getObservatie());
                br.newLine();
            }
        }catch(IOException e){throw new RepositoryException("Nu am putut citi ");}
    }


    @Override
    public void loadData(){
        BufferedReader br = null;

        try {
            br = new BufferedReader(new FileReader("Catalog"));
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
                Nota n = new Nota(Integer.parseInt(fields[0]), Integer.parseInt(fields[1]), Integer.parseInt(fields[2]), Integer.parseInt(fields[3]), fields[4]);

                try {
                    super.save(n);
                } catch (RepositoryException e) {
                    System.out.println(e.getMessage());
                }
                catch (ValidationException e) {
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
            bw = new BufferedWriter(new FileWriter("Catalog"));

            for (Nota n: super.findAll()){
                String line = "";
                line += n.getId() + ";";
                line += n.getIdStudent() + ";";
                line += n.getIdTema() + ";";
                line += n.getValoare() + ";";
                line += n.getObservatie();
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
            File fXmlFile = new File("Catalog.xml");
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(fXmlFile);

            doc.getDocumentElement().normalize();

            NodeList nList = doc.getElementsByTagName("nota");

            for (int temp = 0; temp < nList.getLength(); temp++) {

                Node nNode = nList.item(temp);

                if (nNode.getNodeType() == Node.ELEMENT_NODE) {

                    org.w3c.dom.Element eElement = (org.w3c.dom.Element) nNode;
                    int idNota = Integer.parseInt(eElement.getAttribute("idNota"));
                    int idStudent = Integer.parseInt(eElement.getElementsByTagName("idStudent").item(0).getTextContent());
                    int idTema = Integer.parseInt(eElement.getElementsByTagName("idTema").item(0).getTextContent());
                    int valoare = Integer.parseInt(eElement.getElementsByTagName("valoare").item(0).getTextContent());
                    String observatie = eElement.getElementsByTagName("observatie").item(0).getTextContent();

                    Nota n = new Nota(idNota, idStudent, idTema, valoare, observatie);
                    super.entities.put(n.getId(), n);
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

            for (Nota n:findAll()){

                // staff elements
                Element staff = doc.createElement("nota");
                rootElement.appendChild(staff);

                // set attribute to staff element
                Attr attr = doc.createAttribute("idNota");
                attr.setValue(String.valueOf(n.getId()));
                staff.setAttributeNode(attr);


                // nume elements
                Element nume = doc.createElement("idStudent");
                nume.appendChild(doc.createTextNode(String.valueOf(n.getIdStudent())));
                staff.appendChild(nume);

                // grupa elements
                Element grupa = doc.createElement("idTema");
                grupa.appendChild(doc.createTextNode(String.valueOf(n.getIdTema())));
                staff.appendChild(grupa);

                // email elements
                Element email = doc.createElement("valoare");
                email.appendChild(doc.createTextNode(String.valueOf(n.getValoare())));
                staff.appendChild(email);

                // cadru_didactic elements
                Element cadru_didactic = doc.createElement("observatie");
                cadru_didactic.appendChild(doc.createTextNode(n.getObservatie()));
                staff.appendChild(cadru_didactic);
            }

            // write the content into xml file
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(new File("Catalog.xml"));

            transformer.transform(source, result);

        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (TransformerConfigurationException e) {
            e.printStackTrace();
        } catch (TransformerException e) {
            e.printStackTrace();
        }
    }




    public Nota findNota(int idStudent, int idTema) {
        Iterable<Nota> notas = findAll();
        for (Nota n : notas) {
            if (n.getIdStudent() == idStudent && n.getIdTema() == idTema)
                return n;
        }
        return null;
    }

    public int findNotaForStudent(int idStudent){
        Iterable<Nota> notas = findAll();
        for (Nota n:notas)
            if(n.getIdStudent() == idStudent)
                return n.getId();
        return 0;
    }

    public int getIdNota() {
        Iterable<Nota> notas = findAll();
        final int[] maxim = {0};
        notas.forEach((nota -> {if(nota.getId()> maxim[0]) maxim[0] = nota.getId();}));
        return maxim[0]+1;
    }

}
