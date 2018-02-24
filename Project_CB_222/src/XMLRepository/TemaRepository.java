package XMLRepository;

import Domain.Tema;
import Repository.Validator;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import javax.xml.parsers.DocumentBuilder;

public class TemaRepository extends AbstractRepositoryFromXML<Integer, Tema> implements InterfaceRepositoryXML<Integer, Tema> {
    public TemaRepository(Validator validator, String fileName) {
        super(validator, fileName);
        loadData();
    }

    @Override
    public void save(Tema el) {
        super.save(el);
        Document doc = super.loadDocument();
        Element nList = doc.getDocumentElement();
        appendToXML(doc, nList, el);
        super.saveXML(doc);
    }

    @Override
    public void update(Tema el) {
        super.update(el);
        writeToXML();
    }

    @Override
    public Tema delete(Integer integer) {
        Tema tema = super.delete(integer);
        writeToXML();
        return tema;
    }

    public void writeToXML(){
        Iterable<Tema> listOfHomeworks = super.getAll();
        DocumentBuilder docBuilder = super.createDocumentBuilder();

        // root elements
        Document doc = docBuilder.newDocument();
        Element rootElement = doc.createElement("homeworks");
        doc.appendChild(rootElement);


        for(Tema student: listOfHomeworks){
            appendToXML(doc, rootElement, student);
        }
        super.saveXML(doc);
    }

    public void loadData(){
        Document doc = super.loadDocument();
        NodeList nList = doc.getElementsByTagName("homework");

        for (int temp = 0; temp < nList.getLength(); temp++) {

            Node nNode = nList.item(temp);

            if (nNode.getNodeType() == Node.ELEMENT_NODE) {

                Element eElement = (Element) nNode;

                Tema tema = createObject(eElement);
                super.save(tema);
            }
        }
    }


    public void appendToXML(Document doc, Element nList, Tema tema){
        try{

            Element newHomework = doc.createElement("homework");

            Element idTema = doc.createElement("idTema");
            idTema.appendChild(doc.createTextNode(String.valueOf( tema.getId())));
            newHomework.appendChild(idTema);

            Element descriere = doc.createElement("descriere");
            descriere.appendChild(doc.createTextNode(tema.getDescriere()));
            newHomework.appendChild(descriere);


            Element deadline = doc.createElement("deadline");
            deadline.appendChild(doc.createTextNode(String.valueOf(tema.getDeadline())));
            newHomework.appendChild(deadline);

            Element titlu = doc.createElement("titlu");
            titlu.appendChild(doc.createTextNode(tema.getTitlu()));
            newHomework.appendChild(titlu);

            nList.appendChild(newHomework);
        }
        catch (Exception e){
            e.printStackTrace();

        }

    }

    @Override
    public Tema createObject(Element element) {
        int idTema=Integer.parseInt(element.getElementsByTagName("idTema").item(0).getTextContent().trim());
        String descriere=element.getElementsByTagName("descriere").item(0).getTextContent().trim();
        int deadline=Integer.parseInt( element.getElementsByTagName("deadline").item(0).getTextContent().trim());
        String titlu=element.getElementsByTagName("titlu").item(0).getTextContent().trim();
        return new Tema(idTema,descriere,deadline,titlu);
    }
}
