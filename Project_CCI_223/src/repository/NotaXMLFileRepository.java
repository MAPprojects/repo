package repository;

import domain.Nota;
import exceptii.ValidationException;
import validator.Validator;

import javax.xml.stream.*;
import java.io.*;
import java.util.Optional;

public class NotaXMLFileRepository extends NotaFileRepository {
    /**
     * Constructor
     *
     * @param val  -validator.Validator<E></E>
     * @param file String the fileName where is the data
     */
    public NotaXMLFileRepository(Validator<Nota> val, String file) {
        super(val, file);
    }

    @Override
    public void loadData(){
        try(InputStream input=new FileInputStream(super.file)){
            XMLInputFactory inputFactory=XMLInputFactory.newInstance();
            XMLStreamReader reader=inputFactory.createXMLStreamReader(input);
            readFromXML(reader);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (XMLStreamException e) {
            e.printStackTrace();
        } catch (ValidationException e) {
            e.printStackTrace();
        }
    }

    private void readFromXML(XMLStreamReader reader) throws XMLStreamException, ValidationException {
        Nota entity=null;
        while (reader.hasNext()){
            int event=reader.next();
            switch (event){
                case XMLStreamReader.START_ELEMENT:
                    if (reader.getLocalName().equals("nota")){
                        entity=citesteEntity(reader);
                        validator.validate(entity);
                        entities.put(entity.getId(),entity);
                    }
                    break;
            }
        }
    }

    private Nota citesteEntity(XMLStreamReader reader) throws XMLStreamException{
        String id=reader.getAttributeValue(null,"id");
        Nota nota=new Nota();
        nota.setId(Integer.parseInt(id));
        String currentPropertyValue="";
        while (reader.hasNext()){
            int event=reader.next();
            switch (event){
                case XMLStreamReader.END_ELEMENT:
                    if (reader.getLocalName().equals("nota")){
                        return nota;
                    }
                    else{
                        if (reader.getLocalName().equals("idStudent")){
                            nota.setIdStudent(Integer.parseInt(currentPropertyValue));
                        }
                        if (reader.getLocalName().equals("nrTema")){
                            nota.setNrTema(Integer.parseInt(currentPropertyValue));
                        }
                        if (reader.getLocalName().equals("valoare")){
                            nota.setValoare(Float.parseFloat(currentPropertyValue));
                        }
                        currentPropertyValue="";
                    }
                    break;
                case XMLStreamReader.CHARACTERS:
                    currentPropertyValue=reader.getText().trim();
                    break;
            }

        }
        throw new XMLStreamException("nu s-a citit nota");
    }

    @Override
    public void writeToFile(){
        XMLOutputFactory factory=XMLOutputFactory.newInstance();
        try{
            XMLStreamWriter streamWriter=factory.createXMLStreamWriter(new FileOutputStream(super.file));
            streamWriter.writeStartElement("note");
            super.entities.forEach((id,entity)->{
                try{
                    writeEntityInFile(entity,streamWriter);
                }catch (XMLStreamException e){
                    e.printStackTrace();
                }
            });
            streamWriter.writeEndElement();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (XMLStreamException e) {
            e.printStackTrace();
        }
    }

    private void writeEntityInFile(Nota entity, XMLStreamWriter streamWriter) throws XMLStreamException {
        streamWriter.writeStartElement("nota");
        streamWriter.writeAttribute("id",entity.getIdNota().toString());
        streamWriter.writeCharacters("\n");
        streamWriter.writeStartElement("idStudent");
        streamWriter.writeCharacters(entity.getIdStudent().toString());
        streamWriter.writeEndElement();
        streamWriter.writeCharacters("\n");
        streamWriter.writeStartElement("nrTema");
        streamWriter.writeCharacters(entity.getNrTema().toString());
        streamWriter.writeEndElement();
        streamWriter.writeCharacters("\n");
        streamWriter.writeStartElement("valoare");
        streamWriter.writeCharacters(entity.getValoare().toString());
        streamWriter.writeEndElement();
        streamWriter.writeCharacters("\n");
        streamWriter.writeEndElement();
        streamWriter.writeCharacters("\n");
    }

    @Override
    public void writeToFile(Nota entity){ }

    @Override
    public Optional<Nota> save(Nota entity) throws ValidationException {
        Optional<Nota> aux=super.save(entity);
        if (!aux.isPresent()) writeToFile();
        return aux;
    }
}
