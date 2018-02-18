package Repository;

import Domain.Nota;
import Domain.Student;
import Domain.Tema;
import ExceptionsAndValidators.IValidator;
import javafx.util.Pair;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

public class RepositoryNotaXML extends AbstractXMLRepository<Nota,Pair<Integer,Integer>>
{
    IRepository<Student,Integer> repoStudent;
    IRepository<Tema,Integer> repoTema;

    public RepositoryNotaXML(String filename, IValidator<Nota> validator, IRepository<Student,Integer> repoStudent, IRepository<Tema,Integer> repoTema) {
            super(filename, validator);
            this.repoStudent=repoStudent;
            this.repoTema = repoTema;
            super.loadData();
            }

    @Override
    public void writeToFile() {
            try {
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
    // root element
            Document doc = docBuilder.newDocument();
            Element rootElement = doc.createElement("note");
            doc.appendChild(rootElement);
            getAll().forEach(x->{
            Element studentElement = doc.createElement("nota");
            writeObject(doc,"idStudent",x.getStudent().getID().toString(),studentElement);
            writeObject(doc,"idTema",x.getTema().getID().toString(),studentElement);
            writeObject(doc,"valoare",x.getNota().toString(),studentElement);
            rootElement.appendChild(studentElement);
            });
            super.saveDocument(doc);
            } catch (ParserConfigurationException pce) {
            pce.printStackTrace();
            }
            }

    @Override
    public Nota createObject(Element element) {

            String idStudent=element.getElementsByTagName("idStudent").item(0).getTextContent();
            String idTema=element.getElementsByTagName("idTema").item(0).getTextContent();
            String nota=element.getElementsByTagName("valoare").item(0).getTextContent();
            return new Nota(repoStudent.find(new Integer(idStudent)).get(),repoTema.find(new Integer(idTema)).get(),new Integer(nota));

            }

    @Override
    public void writeObject(Document doc, String tagName, String textNode, Element studElem) {
            Element element=doc.createElement(tagName);
            element.appendChild(doc.createTextNode(textNode));
            studElem.appendChild(element);

            }
}
