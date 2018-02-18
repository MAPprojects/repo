package Repository;

import Domain.Nota;
import Domain.Student;
import Domain.Tema;
import ValidatorsAndExceptions.Validator;
import javafx.util.Pair;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

public class NotaRepositoryXML extends AbstractRepositoryXML<Nota,Pair<Integer,Integer>>{

    public NotaRepositoryXML(String filename, Validator< Nota > validator) {
        super(filename, validator);
    }

    @Override
    public Nota createInstance(Element element) {
        String idStudent=element.getElementsByTagName("idStudent").item(0).getTextContent();
        String idTema=element.getElementsByTagName("idTema").item(0).getTextContent();
        String value=element.getElementsByTagName("value").item(0).getTextContent();
        String saptPredare=element.getElementsByTagName("saptPredare").item(0).getTextContent();
        Student student=new Student(Integer.parseInt(idStudent));
        Tema tema=new Tema(Integer.parseInt(idTema));
        return new Nota(student,tema,Integer.parseInt(value),Integer.parseInt(saptPredare));

    }

    @Override
    public void writeInstace(Document doc, String tagName, String textNode, Element notaElement) {
        Element element=doc.createElement(tagName);
        element.appendChild(doc.createTextNode(textNode));
        notaElement.appendChild(element);
    }

    @Override
    public void writeToFile() {
        try {
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

            Document doc = docBuilder.newDocument();
            Element rootElement = doc.createElement("note");
            doc.appendChild(rootElement);
            getAll().forEach(x->{
                Element notaElement = doc.createElement("nota");
                writeInstace(doc,"idStudent",x.getStudent().getID().toString(),notaElement);
                writeInstace(doc,"idTema",x.getTema().getID().toString(),notaElement);
                writeInstace(doc,"value",x.getValoare().toString(),notaElement);
                writeInstace(doc,"saptPredare",x.getSaptPredare().toString(),notaElement);
                rootElement.appendChild(notaElement);
            });
            super.saveDocument(doc);
        } catch (ParserConfigurationException pce) {
            pce.printStackTrace();
        }
    }
}
