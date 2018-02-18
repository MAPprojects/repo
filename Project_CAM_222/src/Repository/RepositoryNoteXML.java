package Repository;

import Domain.Nota;
import Domain.Student;
import Domain.Tema;
import Domain.Validator;
import org.w3c.dom.*;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.stream.Stream;

public class RepositoryNoteXML extends AbstractFileRepository<String, Nota> {
    private IRepository<Integer, Student> repoStudenti;
    private IRepository<Integer, Tema> repoTeme;

    public RepositoryNoteXML(Validator<Nota> validator,
                             String numeFisier,
                             IRepository<Integer,Student> rs,
                             IRepository<Integer,Tema> rt) {
        super(validator, numeFisier);
        repoStudenti = rs;
        repoTeme = rt;
        incarcareFisier();
    }

    public void incarcareFisier() {
        try {
            File inputFile = new File(numeFisier);
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(inputFile);
            doc.getDocumentElement().normalize();
            NodeList nList = doc.getElementsByTagName("nota");

            for (int temp = 0; temp < nList.getLength(); temp++) {
                Node nNode = nList.item(temp);
                if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element eElement = (Element) nNode;
                    Integer idStudent = Integer.parseInt(eElement.getElementsByTagName("idStudent").item(0).getTextContent());
                    Integer idTema = Integer.parseInt(eElement.getElementsByTagName("idTema").item(0).getTextContent());
                    Integer valoare = Integer.parseInt(eElement.getElementsByTagName("valoare").item(0).getTextContent());

                    Optional<Student> student = repoStudenti.findOne(idStudent);
                    Optional<Tema> tema = repoTeme.findOne(idTema);
                    String id = "" + idStudent + "_" + idTema;
                    if (student.isPresent() && tema.isPresent()) {
                        Nota nota = new Nota(id, student.get(), tema.get(), valoare);
                        super.save(nota);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void salvareFisier() {
        try {
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.newDocument();

            Element rootElement = doc.createElement("class");
            doc.appendChild(rootElement);

            for (Nota n : getAll()) {
                Element notaElement = doc.createElement("nota");
                rootElement.appendChild(notaElement);

                Element idStudent = doc.createElement("idStudent");
                idStudent.appendChild(doc.createTextNode(n.getIdStudent().toString()));
                notaElement.appendChild(idStudent);

                Element idTema = doc.createElement("idTema");
                idTema.appendChild(doc.createTextNode(n.getIdTema().toString()));
                notaElement.appendChild(idTema);

                Element valoare = doc.createElement("valoare");
                valoare.appendChild(doc.createTextNode(n.getValoare().toString()));
                notaElement.appendChild(valoare);
            }

            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty(OutputKeys.METHOD, "xml");
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(new File(numeFisier));
            transformer.transform(source, result);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
