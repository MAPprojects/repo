package repository;

import domain.HasID;
import exceptii.ValidationException;
import validator.Validator;

import javax.xml.stream.*;
import java.io.*;
import java.util.Optional;

public abstract class AbstractXMLFileRepository<E extends HasID<ID>,ID> extends AbstractFileRepository<E,ID> {

    private String nameEntity;
    /**
     * Constructor
     *
     * @param val -validator.Validator<E></E>
     */
    public AbstractXMLFileRepository(Validator<E> val,String file,String entity) {
        super(val,file);
        this.nameEntity=entity;
        loadData();
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
        E entity=null;
        while (reader.hasNext()){
            int event=reader.next();
            switch (event){
                case XMLStreamReader.START_ELEMENT:
                    if (reader.getLocalName().equals(nameEntity)){
                        entity=citesteEntity(reader);
                        validator.validate(entity);
                        entities.put(entity.getId(),entity);
                    }
                    break;
            }
        }
    }

    public abstract E citesteEntity(XMLStreamReader reader) throws XMLStreamException;

    @Override
    public void writeToFile(){
        XMLOutputFactory factory=XMLOutputFactory.newInstance();
        try{
            XMLStreamWriter streamWriter=factory.createXMLStreamWriter(new FileOutputStream(super.file));
            streamWriter.writeStartElement(nameEntity+"s");
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

    protected abstract void writeEntityInFile(E entity, XMLStreamWriter streamWriter) throws XMLStreamException;

    @Override
    public void writeToFile(E entity){

    }

    @Override
    public Optional<E> save(E entity) throws ValidationException {
        Optional<E> aux=super.save(entity);
        if (!aux.isPresent()) writeToFile();
        return aux;
    }
}
