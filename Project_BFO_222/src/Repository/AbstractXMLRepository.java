package Repository;

import Domain.HasID;
import Domain.Student;
import Domain.Validator;
import com.sun.org.apache.xml.internal.serialize.IndentPrinter;

import javax.xml.stream.*;
import java.io.*;

public abstract class AbstractXMLRepository<E extends HasID<ID>,ID> extends AbstractFileRepository<E,ID>{
    public AbstractXMLRepository(String filename, Validator<E> validator ){
        super(filename,validator);
    }
    @Override
    protected void loading(){
        try(InputStream input = new FileInputStream(super.fileName)){
            XMLInputFactory inputFactory=XMLInputFactory.newInstance();
            XMLStreamReader reader=inputFactory.createXMLStreamReader(input);
            readFromXml(reader);
        }catch (IOException | XMLStreamException f){

        }
    }

    protected abstract void readFromXml(XMLStreamReader reader) throws  XMLStreamException;

    protected abstract E createEntity(XMLStreamReader reader) throws XMLStreamException;


    @Override
    protected void writeToFile() {
        XMLOutputFactory factory = XMLOutputFactory.newInstance();
        try {
            XMLStreamWriter streamWriter = factory.createXMLStreamWriter(new FileOutputStream(super.fileName));
            writeTitle(streamWriter);

            entities.forEach((id,x) -> {
                try {
                    writeEntity(x, streamWriter);
                } catch (XMLStreamException e) {
                    e.printStackTrace();
                }
            });
            streamWriter.writeCharacters("\n");
            streamWriter.writeEndElement();
        } catch (XMLStreamException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    protected abstract void writeTitle(XMLStreamWriter writer) throws  XMLStreamException;
    protected abstract void writeEntity(E x, XMLStreamWriter writer) throws XMLStreamException;
}
