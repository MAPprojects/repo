package repository;

import domain.Intarziere;
import validator.Validator;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamWriter;

public class IntarziereXMLFileRepository extends AbstractXMLFileRepository<Intarziere,Integer> {
    /**
     * Constructor
     *
     * @param val    -validator.Validator<E></E>
     * @param file
     */
    public IntarziereXMLFileRepository(Validator<Intarziere> val, String file) {
        super(val, file, "intarziere");
    }

    @Override
    public Intarziere citesteEntity(XMLStreamReader reader) throws XMLStreamException {
        Intarziere intarziere=new Intarziere();
        String id=reader.getAttributeValue(null,"id");
        intarziere.setIdIntarziere(Integer.parseInt(id));

        String currentPropertyValue="";
        while(reader.hasNext()){
            int event=reader.next();
            switch (event){
                case XMLStreamReader.END_ELEMENT:
                    if (reader.getLocalName().equals("intarziere"))
                        return intarziere;
                    else {
                        if (reader.getLocalName().equals("idNota")){
                            intarziere.setIdNota(Integer.parseInt(currentPropertyValue));
                        }
                        if (reader.getLocalName().equals("idStudent")){
                            intarziere.setIdStudent(Integer.parseInt(currentPropertyValue));
                        }
                        if (reader.getLocalName().equals("nrTema")){
                            intarziere.setNrTema(Integer.parseInt(currentPropertyValue));
                        }
                        if (reader.getLocalName().equals("saptamana_predarii")){
                            intarziere.setSaptamana_predarii(Integer.parseInt(currentPropertyValue));
                        }
                        currentPropertyValue="";
                    }
                    break;
                case XMLStreamReader.CHARACTERS:
                    currentPropertyValue=reader.getText().trim();
                    break;
            }
        }

        throw new XMLStreamException("nu s-a citit intarziere");
    }

    @Override
    protected void writeEntityInFile(Intarziere entity, XMLStreamWriter streamWriter) throws XMLStreamException {
        streamWriter.writeStartElement("intarziere");
        streamWriter.writeAttribute("id",entity.getId().toString());
        streamWriter.writeCharacters("\n");
        streamWriter.writeStartElement("idNota");
        streamWriter.writeCharacters(entity.getIdNota().toString());
        streamWriter.writeEndElement();
        streamWriter.writeCharacters("\n");
        streamWriter.writeStartElement("idStudent");
        Integer grupa=entity.getIdStudent();
        streamWriter.writeCharacters(grupa.toString());
        streamWriter.writeEndElement();
        streamWriter.writeCharacters("\n");
        streamWriter.writeStartElement("nrTema");
        streamWriter.writeCharacters(entity.getNrTema().toString());
        streamWriter.writeEndElement();
        streamWriter.writeCharacters("\n");
        streamWriter.writeStartElement("saptamana_predarii");
        streamWriter.writeCharacters(entity.getSaptamana_predarii().toString());
        streamWriter.writeEndElement();
        streamWriter.writeCharacters("\n");
        streamWriter.writeEndElement();
        streamWriter.writeCharacters("\n");
    }

    @Override
    public Intarziere buildEntity(String[] fields) {
        return null;
    }
}
