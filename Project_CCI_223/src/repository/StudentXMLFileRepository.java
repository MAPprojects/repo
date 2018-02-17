package repository;

import domain.Student;
import validator.Validator;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamWriter;

public class StudentXMLFileRepository extends AbstractXMLFileRepository<Student,Integer> {
    /**
     * Constructor
     *
     * @param val    -validator.Validator<E></E>
     * @param file
     */
    public StudentXMLFileRepository(Validator<Student> val, String file) {
        super(val, file, "student");
    }

    @Override
    public Student buildEntity(String[] fields) {
        return null;
    }

    @Override
    public Student citesteEntity(XMLStreamReader reader) throws XMLStreamException {
        String id=reader.getAttributeValue(null,"id");
        Student student=new Student();
        student.setId(Integer.parseInt(id));
        String currentPropertyValue="";
        while (reader.hasNext()){
            int event=reader.next();
            switch (event){
                case XMLStreamReader.END_ELEMENT:
                    if (reader.getLocalName().equals("student"))
                        return student;
                    else {
                        if (reader.getLocalName().equals("nume")){
                            student.setNume(currentPropertyValue);
                        }
                        if (reader.getLocalName().equals("grupa")){
                            student.setGrupa(Integer.parseInt(currentPropertyValue));
                        }
                        if (reader.getLocalName().equals("email")){
                            student.setEmail(currentPropertyValue);
                        }
                        if (reader.getLocalName().equals("profesor")){
                            student.setCadru_didactic_indrumator_de_laborator(currentPropertyValue);
                        }
                        currentPropertyValue="";
                    }
                    break;
                case XMLStreamReader.CHARACTERS:
                    currentPropertyValue=reader.getText().trim();
                    break;
            }
        }
        throw new XMLStreamException("nu s-a citit student!!!");
    }

    @Override
    protected void writeEntityInFile(Student entity, XMLStreamWriter streamWriter) throws XMLStreamException {
        streamWriter.writeStartElement("student");
        streamWriter.writeAttribute("id",entity.getId().toString());
        streamWriter.writeCharacters("\n");
        streamWriter.writeStartElement("nume");
        streamWriter.writeCharacters(entity.getNume());
        streamWriter.writeEndElement();
        streamWriter.writeCharacters("\n");
        streamWriter.writeStartElement("grupa");
        Integer grupa=entity.getGrupa();
        streamWriter.writeCharacters(grupa.toString());
        streamWriter.writeEndElement();
        streamWriter.writeCharacters("\n");
        streamWriter.writeStartElement("email");
        streamWriter.writeCharacters(entity.getEmail());
        streamWriter.writeEndElement();
        streamWriter.writeCharacters("\n");
        streamWriter.writeStartElement("profesor");
        streamWriter.writeCharacters(entity.getCadru_didactic_indrumator_de_laborator());
        streamWriter.writeEndElement();
        streamWriter.writeCharacters("\n");
        streamWriter.writeEndElement();
        streamWriter.writeCharacters("\n");
    }

}
