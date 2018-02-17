package Repository;

import Domain.HasId;
import Exceptions.ValidationException;
import Validators.Validator;

import javax.xml.stream.*;
import java.io.*;

public abstract class AbstractXMLRepository <ID, E extends HasId<ID>> extends AbstractRepository<ID, E> {
    protected String fileName;

    public AbstractXMLRepository(String fileName, Validator<E> val) {
        super(val);
        this.fileName = fileName;

        loadFromFile();

    }

    protected void loadFromFile() {
        try (InputStream input = new FileInputStream(fileName)) {
            XMLInputFactory inputFactory = XMLInputFactory.newInstance();
            XMLStreamReader reader = inputFactory.createXMLStreamReader(input);
            readfromXML(reader);
        } catch (IOException | XMLStreamException e) {
            e.printStackTrace();
            System.out.println("eroare");
            //writeToFile();
        }
    }

    protected void readfromXML(XMLStreamReader reader) throws XMLStreamException {
        E elem = null;
        while (reader.hasNext()) {
            int event = reader.next();
            switch (event) {
                case XMLStreamReader.START_ELEMENT:
                    if (reader.getLocalName().equals(getLocalName())) {
                        elem = readEntityFromXML(reader);
                        super.add(elem);
                    }
                    break;
            }
        }
    }

    protected abstract String getLocalName();
    protected abstract String getStartElement();
    protected abstract void writeEntityToFile(E e, XMLStreamWriter writer) throws XMLStreamException;
    protected abstract E readEntityFromXML(XMLStreamReader reader) throws XMLStreamException;

    protected void writeToFile() {
        XMLOutputFactory factory = XMLOutputFactory.newInstance();

        try {
            XMLStreamWriter streamWriter = factory.createXMLStreamWriter(new FileOutputStream(fileName));
            streamWriter.writeStartDocument("UTF-8","1.0");
            streamWriter.writeDTD("\n");
            streamWriter.writeStartElement(getStartElement());
            super.all.values().forEach(x -> {
                try {
                    streamWriter.writeDTD("\n");
                    streamWriter.writeDTD("\t");
                    writeEntityToFile(x, streamWriter);
                } catch (XMLStreamException e) {
                    e.printStackTrace();
                }
            });
            streamWriter.writeDTD("\n");
            streamWriter.writeEndElement();
            streamWriter.writeEndDocument();
        } catch (XMLStreamException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void add(E elem) throws ValidationException {
        super.add(elem);
        //getAll().forEach(System.out::println);
        writeToFile();
    }

    @Override
    public void update(ID id, E replacement) throws ValidationException {
        super.update(id, replacement);
        writeToFile();
    }

    @Override
    public E delete(ID id) {
        E e = super.delete(id);
        writeToFile();
        return e;
    }
}
