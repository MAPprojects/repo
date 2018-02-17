package Repository;

import Domain.Student;
import Exceptions.ValidationException;
import Validators.Validator;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamWriter;

public class XMLStudentRepository extends AbstractXMLRepository<Integer, Student> {

    public XMLStudentRepository(String fileName, Validator<Student> validator) {
        super(fileName, validator);
    }


    @Override
    protected Student readEntityFromXML(XMLStreamReader reader) throws XMLStreamException {
        int id = Integer.parseInt(reader.getAttributeValue(null, "id"));
        Student s = new Student();
        s.setId(id);
        String currentPropertyValue = "";
        while (reader.hasNext()) {
            int event = reader.next();
            switch (event) {
                case XMLStreamReader.CHARACTERS:

                    currentPropertyValue = reader.getText().trim();
                    //System.out.println("property: "+ currentPropertyValue);
                    break;

                case XMLStreamReader.END_ELEMENT:
                    if (reader.getLocalName().equals("student")) {
                        return s;
                    } else {
                        switch (reader.getLocalName()) {
                            case "name":
                                s.setNume(currentPropertyValue);
                                break;
                            case "group":
                                s.setGrupa(Integer.parseInt(currentPropertyValue));
                                break;
                            case "email":
                                s.setEmail(currentPropertyValue);
                                break;
                            case "professor":
                                s.setProfesor(currentPropertyValue);
                                break;

                        }
                        currentPropertyValue = "";
                    }
                    break;


            }
        }
        throw new XMLStreamException("student could not be read from file");
    }

    @Override
    protected String getStartElement() {
        return "students";
    }

    @Override
    protected String getLocalName() {
        return "student";
    }

    @Override
    protected void writeEntityToFile(Student student, XMLStreamWriter writer) throws XMLStreamException {

        writer.writeStartElement("student");
        writer.writeAttribute("id", String.valueOf(student.getId()));
        writer.writeDTD("\n");
        writer.writeDTD("\t\t");
        writer.writeStartElement("name");
        writer.writeDTD(student.getNume());
        //writer.writeAttribute("value", student.getNume());
        writer.writeEndElement();


        writer.writeDTD("\n");
        writer.writeDTD("\t\t");

        writer.writeStartElement("group");
        writer.writeDTD(String.valueOf(student.getGrupa()));
        //writer.writeAttribute("value", String.valueOf(student.getGrupa()));
        writer.writeEndElement();

//
        writer.writeDTD("\n");
        writer.writeDTD("\t\t");
        writer.writeStartElement("email");
        writer.writeDTD(student.getEmail());
//        //writer.writeAttribute("value", student.getEmail());
        writer.writeEndElement();


        writer.writeDTD("\n");
        writer.writeDTD("\t\t");
        writer.writeStartElement("professor");
        writer.writeDTD(student.getProfesor());
        //writer.writeAttribute("value", student.getProfesor());
        writer.writeEndElement();
        writer.writeDTD("\n");
        writer.writeDTD("\t");
        writer.writeEndElement();
    }

}
