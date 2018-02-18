package Repositories;

import Domain.HasID;
import Domain.Studenti;
import Validators.IValidator;

import javax.swing.text.Document;
import javax.swing.text.Element;
import javax.xml.stream.*;
import java.io.*;
import java.util.Optional;

public abstract class AbstractXMLRepository<E extends HasID<ID>,ID> extends AbstractRepository<E,ID> {
    public String fileName;
    public AbstractXMLRepository(String file, IValidator<E> v) {
        super(v);
        this.fileName=file;
        loadData();
    }

    protected abstract E citesteObiect(XMLStreamReader reader) throws XMLStreamException;
    protected abstract void writeInFile(E x, XMLStreamWriter writer) throws XMLStreamException;
    protected abstract String getLoadForFXMLReader();
    protected abstract String getLoadForFXMLWriter();

    public void loadData() {
        try (InputStream input = new FileInputStream(fileName)) {
            XMLInputFactory inputFactory = XMLInputFactory.newInstance();
            XMLStreamReader reader = inputFactory.createXMLStreamReader(input);
            readFromXml(reader);
        } catch (IOException | XMLStreamException f) {
        }
    }

    private void readFromXml(XMLStreamReader reader) throws XMLStreamException {
        E st = null;
        while (reader.hasNext()) {
            int event = reader.next();
            switch (event) {
                case XMLStreamReader.START_ELEMENT:
                    if (reader.getLocalName().equals(getLoadForFXMLReader())) {
                        st = citesteObiect(reader);
                        super.save(st);
                    }
                    break;
            }
        }
    }

    public void writeToXML() {
        XMLOutputFactory factory = XMLOutputFactory.newInstance();
        try {
            XMLStreamWriter streamWriter =
                    factory.createXMLStreamWriter(new FileOutputStream(fileName));
            streamWriter.writeStartElement(getLoadForFXMLWriter());
            streamWriter.writeDTD("\n   ");
            super.findAll().forEach(x -> {
                try {
                    writeInFile(x, streamWriter);
                    streamWriter.writeDTD("\n");
                } catch (XMLStreamException e) {
                    e.printStackTrace();
                }
            });
            streamWriter.writeEndElement();
        } catch (XMLStreamException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }


    public void save(E elem){
        super.save(elem);
        writeToXML();
    }

    public void update(ID id,String filtru,String valoare){
        writeToXML();
    }
    public Optional<E> delete(ID id){
        Optional<E> e=super.delete(id);
        writeToXML();
        return e;
    }

}
