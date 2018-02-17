package repository;

import domain.TemaLaborator;
import validator.Validator;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamWriter;

public class TemaLaboratorXMLFileRepository extends AbstractXMLFileRepository<TemaLaborator,Integer> {
    /**
     * Constructor
     *
     * @param val    -validator.Validator<E></E>
     * @param file
     */
    public TemaLaboratorXMLFileRepository(Validator<TemaLaborator> val, String file) {
        super(val, file, "tema_laborator");
    }

    @Override
    public TemaLaborator buildEntity(String[] fields) {
        return null;
    }

    @Override
    public TemaLaborator citesteEntity(XMLStreamReader reader) throws XMLStreamException {
        String id=reader.getAttributeValue(null,"id");
        TemaLaborator temaLaborator=new TemaLaborator();
        temaLaborator.setNr_tema_de_laborator(Integer.parseInt(id));
        String currentPropertyValue="";
        while (reader.hasNext()){
            int event=reader.next();
            switch (event){
                case XMLStreamReader.END_ELEMENT:
                    if (reader.getLocalName().equals("tema_laborator")){
                        return temaLaborator;
                    }
                    else{
                        if (reader.getLocalName().equals("cerinta")){
                            temaLaborator.setCerinta(currentPropertyValue);
                        }
                        if (reader.getLocalName().equals("deadline")){
                            temaLaborator.setDeadline(Integer.parseInt(currentPropertyValue));
                        }
                        currentPropertyValue="";
                    }
                    break;
                case XMLStreamReader.CHARACTERS:
                    currentPropertyValue=reader.getText().trim();
                    break;
            }
        }
        throw new XMLStreamException("nu s-a citit tema_laborator");
    }

    @Override
    protected void writeEntityInFile(TemaLaborator entity, XMLStreamWriter streamWriter) throws XMLStreamException {
        streamWriter.writeStartElement("tema_laborator");
        streamWriter.writeAttribute("id",entity.getId().toString());
        streamWriter.writeCharacters("\n");
        streamWriter.writeStartElement("cerinta");
        streamWriter.writeCharacters(entity.getCerinta());
        streamWriter.writeEndElement();
        streamWriter.writeCharacters("\n");
        streamWriter.writeStartElement("deadline");
        Integer deadline=entity.getDeadline();
        streamWriter.writeCharacters(deadline.toString());
        streamWriter.writeEndElement();
        streamWriter.writeCharacters("\n");
        streamWriter.writeEndElement();
        streamWriter.writeCharacters("\n");
    }
}
