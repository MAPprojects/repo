package repository;

import domain.Laborator;
import domain.LaboratorValidator;
import org.w3c.dom.*;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.util.Optional;

public class RepoTema extends AbstractRepo<Laborator>{
    private MockRepo defaultlist=new MockRepo();
   //iteratia1/ private List<Laborator> lista =defaultlist.listaLaboratoare();
    private LaboratorValidator laboratorValidator =new LaboratorValidator();

    public RepoTema() throws Exception {
        super();
        setValidator(Optional.of(this.laboratorValidator));
    }

    public void update(Integer nrTema,Laborator laborator) throws Exception{
        laboratorValidator.valideaza(laborator);
        for (Laborator l: lista){
            if (nrTema==l.getNrTema()){
                l.setNrTema(laborator.getNrTema());
                l.setCerinta(laborator.getCerinta());
                l.setDeadline(laborator.getDeadline());
                updateFile();
            }
        }
    }
    public void getFromFile() throws Exception {
//        }
        try {
            File inputFile = new File("./src/main/resources/Teme.xml");
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(inputFile);
            doc.getDocumentElement().normalize();
            NodeList nList = doc.getElementsByTagName("laborator");

            for (int temp = 0; temp < nList.getLength(); temp++) {
                Node nNode = nList.item(temp);

                if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element eElement = (Element) nNode;
                    Laborator laborator = new Laborator();
                    laborator.setNrTema(Integer.parseInt(eElement.getAttribute("nrTema")));

                    laborator.setCerinta(eElement.getElementsByTagName("cerinta").item(0).getTextContent());
                    laborator.setDeadline(Integer.parseInt(eElement.getElementsByTagName("deadline").item(0).getTextContent()));

                    lista.add(laborator);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void updateFile(){
        try {
            DocumentBuilderFactory dbFactory =
                    DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.newDocument();

            // root element
            Element rootElement = doc.createElement("laboratoare");
            doc.appendChild(rootElement);
            for (Laborator l : lista) {
                // nota element
                Element laborator = doc.createElement("laborator");
                rootElement.appendChild(laborator);

                // setting attribute to element
                Attr attr = doc.createAttribute("nrTema");
                attr.setValue(l.getNrTema().toString());
                laborator.setAttributeNode(attr);

                // nume element
                Element valoare = doc.createElement("cerinta");
                valoare.appendChild(doc.createTextNode(l.getCerinta()));
                laborator.appendChild(valoare);

                Element numarTema = doc.createElement("deadline");
                numarTema.appendChild(doc.createTextNode(l.getDeadline().toString()));
                laborator.appendChild(numarTema);



                // write the content into xml file
            }
            TransformerFactory transformerFactory = TransformerFactory.newInstance();

            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(new File("./src/main/resources/Teme.xml"));
            transformer.transform(source, result);

            // Output to console for testing
            StreamResult consoleResult = new StreamResult(System.out);
            transformer.transform(source, consoleResult);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
