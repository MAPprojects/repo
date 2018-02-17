package Repository;

import Domain.Task;
import Exceptions.ValidationException;
import Validators.Validator;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamWriter;

public class XMLTaskRepository extends AbstractXMLRepository<Integer, Task> {

    public XMLTaskRepository(String fileName, Validator<Task> validator) {
        super(fileName, validator);
    }

    @Override
    protected String getLocalName() {
        return "task";
    }

    @Override
    protected String getStartElement() {
        return "tasks";
    }

    @Override
    protected void writeEntityToFile(Task task, XMLStreamWriter writer) throws XMLStreamException {
        writer.writeStartElement("task");
        writer.writeAttribute("id", String.valueOf(task.getId()));
        writer.writeDTD("\n");
        writer.writeDTD("\t\t");
        writer.writeStartElement("description");
        writer.writeDTD(task.getDescriere());
        //writer.writeAttribute("value", student.getNume());
        writer.writeEndElement();


        writer.writeDTD("\n");
        writer.writeDTD("\t\t");

        writer.writeStartElement("deadline");
        writer.writeDTD(String.valueOf(task.getDeadline()));
        //writer.writeAttribute("value", String.valueOf(student.getGrupa()));
        writer.writeEndElement();
        writer.writeDTD("\n");
        writer.writeDTD("\t");

        writer.writeEndElement();
    }

    @Override
    protected Task readEntityFromXML(XMLStreamReader reader) throws XMLStreamException {
        int id = Integer.parseInt(reader.getAttributeValue(null, "id"));
        Task task = new Task();
        task.setId(id);
        String currentPropertyValue = "";
        while (reader.hasNext()) {
            int event = reader.next();
            switch (event) {
                case XMLStreamReader.CHARACTERS:

                    currentPropertyValue = reader.getText().trim();
                    //System.out.println("property: "+ currentPropertyValue);
                    break;

                case XMLStreamReader.END_ELEMENT:
                    if (reader.getLocalName().equals("task")) {
                        return task;
                    } else {
                        switch (reader.getLocalName()) {
                            case "description":
                                task.setDescriere(currentPropertyValue);
                                break;
                            case "deadline":
                                task.setDeadline(Integer.parseInt(currentPropertyValue));
                                break;
                        }
                        currentPropertyValue = "";
                    }
                    break;


            }
        }
        throw new XMLStreamException("task could not be read from file");
    }
}
