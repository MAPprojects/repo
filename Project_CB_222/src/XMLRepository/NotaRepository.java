package XMLRepository;

import Domain.Nota;
import Repository.InterfaceRepository;
import Repository.Validator;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import javax.xml.parsers.DocumentBuilder;

public class NotaRepository extends AbstractRepositoryFromXML<Integer, Nota> implements InterfaceRepositoryXML<Integer, Nota> {

    public NotaRepository(Validator validator, String fileName) {
        super(validator, fileName);
        loadData();
    }

    @Override
    public void save(Nota el) {
        super.save(el);
        Document doc = super.loadDocument();
        Element nList = doc.getDocumentElement();
        appendToXML(doc, nList, el);
        super.saveXML(doc);
    }

    @Override
    public void update(Nota el) {
        super.update(el);
        writeToXML();
    }

    @Override
    public Nota delete(Integer integer) {
        Nota nota = super.delete(integer);
        writeToXML();
        return nota;
    }

    public void loadData(){
        Document doc = super.loadDocument();
        NodeList nList = doc.getElementsByTagName("grade");

        for (int temp = 0; temp < nList.getLength(); temp++) {

            Node nNode = nList.item(temp);

            if (nNode.getNodeType() == Node.ELEMENT_NODE) {

                Element eElement = (Element) nNode;

                Nota nota = createObject(eElement);
                super.save(nota);
            }
        }
    }

    public void writeToXML(){
        Iterable<Nota> listOfGrades = super.getAll();
        DocumentBuilder docBuilder = super.createDocumentBuilder();

        // root elements
        Document doc = docBuilder.newDocument();
        Element rootElement = doc.createElement("grades");
        doc.appendChild(rootElement);


        for(Nota nota: listOfGrades){
            appendToXML(doc, rootElement, nota);
        }
        super.saveXML(doc);
    }

    public void appendToXML(Document doc, Element nList, Nota nota){
        try{

            Element newNota = doc.createElement("grade");

            Element idNota = doc.createElement("idNota");
            idNota.appendChild(doc.createTextNode(String.valueOf( nota.getId())));
            newNota.appendChild(idNota);

            Element valoareNota = doc.createElement("valoareNota");
            valoareNota.appendChild(doc.createTextNode(String.valueOf(nota.getValoareNota())));
            newNota.appendChild(valoareNota);


            Element idStudent = doc.createElement("idStudent");
            idStudent.appendChild(doc.createTextNode(String.valueOf(String.valueOf(nota.getIdStudent()))));
            newNota.appendChild(idStudent);

            Element idTema = doc.createElement("idTema");
            idTema.appendChild(doc.createTextNode(String.valueOf(nota.getIdTema())));
            newNota.appendChild(idTema);

            Element deadline = doc.createElement("deadline");
            deadline.appendChild(doc.createTextNode(String.valueOf(nota.getDeadline())));
            newNota.appendChild(deadline);

            Element titlu = doc.createElement("titlu");
            titlu.appendChild(doc.createTextNode(nota.getTitlu()));
            newNota.appendChild(titlu);

            Element saptamanaPredarii = doc.createElement("saptamanaPredarii");
            saptamanaPredarii.appendChild(doc.createTextNode(String.valueOf(nota.getSaptamanaPredarii())));
            newNota.appendChild(saptamanaPredarii);

            Element observatii = doc.createElement("observatii");
            observatii.appendChild(doc.createTextNode(nota.getObservatii()));
            newNota.appendChild(observatii);

            nList.appendChild(newNota);
        }
        catch (Exception e){
            e.printStackTrace();

        }

    }

    @Override
    public Nota createObject(Element element) {

        int idNota=Integer.parseInt(element.getElementsByTagName("idNota").item(0).getTextContent().trim());
        int valoareNota = Integer.parseInt(element.getElementsByTagName("valoareNota").item(0).getTextContent().trim());
        int idStudent = Integer.parseInt(element.getElementsByTagName("idStudent").item(0).getTextContent().trim());
        int idTema = Integer.parseInt(element.getElementsByTagName("idTema").item(0).getTextContent().trim());
        int deadline = Integer.parseInt(element.getElementsByTagName("deadline").item(0).getTextContent().trim());
        String titlu = element.getElementsByTagName("titlu").item(0).getTextContent().trim();
        int saptamanaPredarii = Integer.parseInt(element.getElementsByTagName("saptamanaPredarii").item(0).getTextContent().trim());
        String observatii = element.getElementsByTagName("observatii").item(0).getTextContent().trim();

        return new Nota(idNota,valoareNota, idStudent, idTema, deadline, titlu, saptamanaPredarii, observatii);

    }


}
