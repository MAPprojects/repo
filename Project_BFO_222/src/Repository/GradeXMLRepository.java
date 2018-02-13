package Repository;

import Domain.Nota;
import Domain.Validator;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamWriter;

public class GradeXMLRepository extends AbstractXMLRepository<Nota,Integer> {

    public GradeXMLRepository(String filename, Validator<Nota> val){
        super(filename,val);
    }

    @Override
    protected void readFromXml(XMLStreamReader reader) throws XMLStreamException {
        Nota n=null;
        while(reader.hasNext()){
            int event=reader.next();
            switch (event) {
                case XMLStreamReader.START_ELEMENT:
                    if (reader.getLocalName().equals("nota")) {
                        n = createEntity(reader);
                        entities.put(n.getId(),n);
                    }
                    break;
            }
        }
    }

    @Override
    protected Nota createEntity(XMLStreamReader reader) throws XMLStreamException {
        String id=reader.getAttributeValue(null,"id");
        Nota nota=new Nota();
        nota.setId(Integer.parseInt(id));
        String currentPropertyValue="";
        while(reader.hasNext()){
            int event=reader.next();
            switch (event){
                case XMLStreamReader.END_ELEMENT:
                    if(reader.getLocalName().equals("nota")){
                        return nota;
                    }
                    else{
                        if(reader.getLocalName().equals("stud")){
                            nota.setStud(Integer.parseInt(currentPropertyValue));
                        }
                        if(reader.getLocalName().equals("tema")){
                            nota.setTema(Integer.parseInt(currentPropertyValue));
                        }
                        if(reader.getLocalName().equals("grade")){
                            nota.setGrade(Integer.parseInt(currentPropertyValue));
                        }
                        currentPropertyValue="";
                    }
                    break;
                case XMLStreamReader.CHARACTERS:
                    currentPropertyValue=reader.getText().trim();
                    break;
            }
        }
        throw new XMLStreamException("Nu s-a citit nota");
    }

    @Override
    protected void writeTitle(XMLStreamWriter writer) throws XMLStreamException {
        writer.writeStartElement("note");
        writer.writeCharacters("\n");
    }

    @Override
    protected void writeEntity(Nota x, XMLStreamWriter writer) throws XMLStreamException {
        writer.writeCharacters("\t");
        writer.writeStartElement("nota");
        writer.writeAttribute("id",x.getId().toString());

        writer.writeCharacters("\n\t\t");

        writer.writeStartElement("stud");
        writer.writeCharacters(x.getStud().toString());
        writer.writeEndElement();

        writer.writeCharacters("\n\t\t");

        writer.writeStartElement("tema");
        writer.writeCharacters(x.getTema().toString());
        writer.writeEndElement();

        writer.writeCharacters("\n\t\t");

        writer.writeStartElement("grade");
        writer.writeCharacters(x.getGrade().toString());
        writer.writeEndElement();

        writer.writeCharacters("\n\t");
        writer.writeEndElement();
        writer.writeCharacters("\n");
    }
}
