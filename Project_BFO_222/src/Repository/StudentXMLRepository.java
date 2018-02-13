package Repository;

import Domain.Student;
import Domain.Validator;

import javax.xml.stream.*;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class StudentXMLRepository extends AbstractXMLRepository<Student,Integer> {
    public StudentXMLRepository(String numeFisier, Validator<Student> v){
        super(numeFisier,v);
    }

    @Override
    protected void readFromXml(XMLStreamReader reader) throws  XMLStreamException{
        Student st=null;
        while(reader.hasNext()){
            int event=reader.next();
            switch (event) {
                case XMLStreamReader.START_ELEMENT:
                    if (reader.getLocalName().equals("student")) {
                        //citim pana la Start_Element student
                        st = createEntity(reader);
                        entities.put(st.getId(),st);
                    }
                    break;
            }
        }
    }

    @Override
    protected Student createEntity(XMLStreamReader reader) throws  XMLStreamException{
        String id=reader.getAttributeValue(null,"id");
        Student s=new Student();
        s.setId(Integer.parseInt(id));
        String currentPropertyValue="";
        while(reader.hasNext()){
            int event=reader.next();
            switch (event){
                case XMLStreamReader.END_ELEMENT:
                    if(reader.getLocalName().equals("student")){
                        return s;
                    }
                    else{
                        if(reader.getLocalName().equals("nume")){
                            s.setNume(currentPropertyValue);
                        }
                        if(reader.getLocalName().equals("grupa")){
                            s.setGrupa(currentPropertyValue);
                        }
                        if(reader.getLocalName().equals("email")){
                            s.setEmail(currentPropertyValue);
                        }
                        if(reader.getLocalName().equals("profesor")){
                            s.setCadru(currentPropertyValue);
                        }
                        currentPropertyValue="";
                    }
                    break;
                case XMLStreamReader.CHARACTERS:
                    currentPropertyValue=reader.getText().trim();
                    break;
            }
        }
        throw new XMLStreamException("Nu s-a citit studentul");
    }

    @Override
    protected void writeTitle(XMLStreamWriter writer) throws XMLStreamException{
        writer.writeStartElement("students");
        writer.writeCharacters("\n");
}

    @Override
    protected void writeEntity(Student x, XMLStreamWriter writer) throws XMLStreamException {
        writer.writeCharacters("\t");
        writer.writeStartElement("student");
        writer.writeAttribute("id",x.getId().toString());

        writer.writeCharacters("\n\t\t");

        writer.writeStartElement("nume");
        writer.writeCharacters(x.getNume());
        writer.writeEndElement();

        writer.writeCharacters("\n\t\t");

        writer.writeStartElement("grupa");
        writer.writeCharacters(x.getGrupa());
        writer.writeEndElement();

        writer.writeCharacters("\n\t\t");

        writer.writeStartElement("email");
        writer.writeCharacters(x.getEmail());
        writer.writeEndElement();

        writer.writeCharacters("\n\t\t");

        writer.writeStartElement("profesor");
        writer.writeCharacters(x.getCadru());
        writer.writeEndElement();

        writer.writeCharacters("\n\t");
        writer.writeEndElement();
        writer.writeCharacters("\n");
    }

}
