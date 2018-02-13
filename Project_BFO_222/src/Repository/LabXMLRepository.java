package Repository;

import Domain.Tema;
import Domain.Validator;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamWriter;

public class LabXMLRepository extends AbstractXMLRepository<Tema,Integer> {
    public LabXMLRepository(String filename, Validator<Tema> val){
        super(filename,val);
    }

    @Override
    protected void readFromXml(XMLStreamReader reader) throws XMLStreamException {
        Tema t=null;
        while(reader.hasNext()){
            int event=reader.next();
            switch (event) {
                case XMLStreamReader.START_ELEMENT:
                    if (reader.getLocalName().equals("tema")) {
                        t = createEntity(reader);
                        entities.put(t.getId(),t);
                    }
                    break;
            }
        }
    }

    @Override
    protected Tema createEntity(XMLStreamReader reader) throws XMLStreamException {
        String numar=reader.getAttributeValue(null,"numar");
        Tema t=new Tema();
        t.setId(Integer.parseInt(numar));
        String currentPropertyValue="";
        while(reader.hasNext()){
            int event=reader.next();
            switch (event){
                case XMLStreamReader.END_ELEMENT:
                    if(reader.getLocalName().equals("tema")){
                        return t;
                    }
                    else{
                        if(reader.getLocalName().equals("deadline")){
                            t.setDeadline(Integer.parseInt(currentPropertyValue));
                        }
                        if(reader.getLocalName().equals("descriere")){
                            t.setDescriere(currentPropertyValue);
                        }
                        currentPropertyValue="";
                    }
                    break;
                case XMLStreamReader.CHARACTERS:
                    currentPropertyValue=reader.getText().trim();
                    break;
            }
        }
        throw new XMLStreamException("Nu s-a citit tema");
    }

    @Override
    protected void writeTitle(XMLStreamWriter writer) throws XMLStreamException {
        writer.writeStartElement("teme");
        writer.writeCharacters("\n");
    }

    @Override
    protected void writeEntity(Tema x, XMLStreamWriter writer) throws XMLStreamException {
        writer.writeCharacters("\t");
        writer.writeStartElement("tema");
        writer.writeAttribute("numar",x.getId().toString());

        writer.writeCharacters("\n\t\t");

        writer.writeStartElement("deadline");
        writer.writeCharacters(x.getDeadline().toString());
        writer.writeEndElement();

        writer.writeCharacters("\n\t\t");

        writer.writeStartElement("descriere");
        writer.writeCharacters(x.getDescriere());
        writer.writeEndElement();

        writer.writeCharacters("\n\t");
        writer.writeEndElement();
        writer.writeCharacters("\n");
    }
}
