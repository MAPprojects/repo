package Repository;

import Domain.Nota;
import Validators.Validator;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamWriter;
import java.io.File;

public class XMLGradeRepository extends AbstractXMLRepository<Integer, Nota> {

    public XMLGradeRepository(String fileName, Validator<Nota> val) {
        super(fileName, val);
    }

    @Override
    protected String getLocalName() {
        return "grade";
    }

    @Override
    protected String getStartElement() {
        return "grades";
    }

    @Override
    protected void writeEntityToFile(Nota nota, XMLStreamWriter writer) throws XMLStreamException {
        writer.writeStartElement("grade");
        writer.writeAttribute("id", String.valueOf(nota.getId()));
        writer.writeDTD("\n");
        writer.writeDTD("\t\t");
        writer.writeStartElement("idStudent");
        writer.writeDTD(String.valueOf(nota.getIdStudent()));
        //writer.writeAttribute("value", student.getNume());
        writer.writeEndElement();


        writer.writeDTD("\n");
        writer.writeDTD("\t\t");

        writer.writeStartElement("idTask");
        writer.writeDTD(String.valueOf(nota.getNrTema()));
        //writer.writeAttribute("value", String.valueOf(student.getGrupa()));
        writer.writeEndElement();

//
        writer.writeDTD("\n");
        writer.writeDTD("\t\t");
        writer.writeStartElement("value");
        writer.writeDTD(String.valueOf(nota.getValoare()));
//        //writer.writeAttribute("value", student.getEmail());
        writer.writeEndElement();
        writer.writeDTD("\n");
        writer.writeDTD("\t");
        writer.writeEndElement();
    }

    @Override
    protected Nota readEntityFromXML(XMLStreamReader reader) throws XMLStreamException {
        int id = Integer.parseInt(reader.getAttributeValue(null, "id"));
        Nota n = new Nota();
        n.setId(id);
        String currentPropertyValue = "";
        while (reader.hasNext()) {
            int event = reader.next();
            switch (event) {
                case XMLStreamReader.CHARACTERS:

                    currentPropertyValue = reader.getText().trim();
                    //System.out.println("property: "+ currentPropertyValue);
                    break;

                case XMLStreamReader.END_ELEMENT:
                    if (reader.getLocalName().equals("grade")) {
                        return n;
                    } else {
                        switch (reader.getLocalName()) {
                            case "idStudent":
                                n.setIdStudent(Integer.parseInt(currentPropertyValue));
                                break;
                            case "idTask":
                                n.setNrTema(Integer.parseInt(currentPropertyValue));
                                break;
                            case "value":
                                n.setValoare(Integer.parseInt(currentPropertyValue));
                                break;
                        }
                        currentPropertyValue = "";
                    }
                    break;


            }
        }
        throw new XMLStreamException("grade could not be read from file");
    }

    @Override
    public Nota delete(Integer id) {
        Nota n = super.delete(id);
        File file = new File(n.getIdStudent()+".txt");
        file.delete();
        return n;
    }
}
