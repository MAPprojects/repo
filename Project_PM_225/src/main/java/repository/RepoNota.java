package repository;

import domain.Nota;
import org.w3c.dom.*;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.util.Optional;

public class RepoNota extends AbstractRepo<Nota> {

    public RepoNota() throws Exception {
        super();
        setValidator(Optional.empty());
    }

    public void getFromFile() throws Exception {
        try {
            File inputFile = new File("./src/main/resources/Note.xml");
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(inputFile);
            doc.getDocumentElement().normalize();
            NodeList nList = doc.getElementsByTagName("nota");

            for (int temp = 0; temp < nList.getLength(); temp++) {
                Node nNode = nList.item(temp);

                if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element eElement = (Element) nNode;
                    Nota nota = new Nota();
                    nota.setIdStudent(Integer.parseInt(eElement.getAttribute("idStudent")));

                    nota.setValoare(Integer.parseInt(eElement.getElementsByTagName("valoare").item(0).getTextContent()));
                    nota.setNrTema(Integer.parseInt(eElement.getElementsByTagName("numarTema").item(0).getTextContent()));

                    lista.add(nota);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void updateFile() {
        try {
            DocumentBuilderFactory dbFactory =
                    DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.newDocument();

            // root element
            Element rootElement = doc.createElement("note");
            doc.appendChild(rootElement);
            for (Nota n : lista) {
                // nota element
                Element nota = doc.createElement("nota");
                rootElement.appendChild(nota);

                // setting attribute to element
                Attr attr = doc.createAttribute("idStudent");
                attr.setValue(n.getIdStudent().toString());
                nota.setAttributeNode(attr);

                // nume element
                Element valoare = doc.createElement("valoare");
                valoare.appendChild(doc.createTextNode(n.getValoare().toString()));
                nota.appendChild(valoare);

                Element numarTema = doc.createElement("numarTema");
                numarTema.appendChild(doc.createTextNode(n.getNrTema().toString()));
                nota.appendChild(numarTema);



                // write the content into xml file
            }
            TransformerFactory transformerFactory = TransformerFactory.newInstance();

            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(new File("./src/main/resources/Note.xml"));
            transformer.transform(source, result);

            // Output to console for testing
            StreamResult consoleResult = new StreamResult(System.out);
            transformer.transform(source, consoleResult);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void update(Integer id, Nota object) throws Exception {
        for (Nota o : this.lista) {
            if (id == o.getIdStudent() && object.getNrTema() == o.getNrTema() && object.getValoare() > o.getValoare()) {
                o.setValoare(object.getValoare());

            }
        }
        updateFile();

    }

}
