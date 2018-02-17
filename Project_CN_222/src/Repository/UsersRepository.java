package Repository;

import Domain.Category;
import Domain.User;
import Exceptions.ValidationException;
import Validators.Validator;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamWriter;

public class UsersRepository extends AbstractXMLRepository<String, User> {


    public UsersRepository(String fileName, Validator<User> validator) {
        super(fileName, validator);
        //loadFromFile();

    }

    @Override
    protected String getLocalName() {
        return "user";
    }

    @Override
    protected String getStartElement() {
        return "users";
    }

    @Override
    protected void writeEntityToFile(User user, XMLStreamWriter writer) throws XMLStreamException {
        writer.writeStartElement("user");
        writer.writeDTD("\n");
        writer.writeDTD("\t\t");

        writer.writeStartElement("username");
        writer.writeDTD(user.getId());
        writer.writeEndElement();
        writer.writeDTD("\n");
        writer.writeDTD("\t\t");

        writer.writeStartElement("password");
        writer.writeDTD(user.getPassword());
        writer.writeEndElement();
        writer.writeDTD("\n");
        writer.writeDTD("\t\t");

        writer.writeStartElement("category");
        writer.writeDTD(user.getCategory().toString().toLowerCase());
        writer.writeEndElement();
        writer.writeDTD("\n");
        writer.writeDTD("\t");
        writer.writeEndElement();
    }

    @Override
    protected User readEntityFromXML(XMLStreamReader reader) throws XMLStreamException {
        User user = new User();
        String currentPropertyValue = "";
        while (reader.hasNext()) {
            int event = reader.next();
            switch (event) {
                case XMLStreamReader.CHARACTERS:
                    currentPropertyValue = reader.getText().trim();
                    //System.out.println("property: "+ currentPropertyValue);
                    break;

                case XMLStreamReader.END_ELEMENT:
                    if (reader.getLocalName().equals("user")) {
                        return user;
                    } else {
                        switch (reader.getLocalName()) {
                            case "username":
                                user.setId(currentPropertyValue);
                                break;
                            case "password":
                                user.setPassword(currentPropertyValue);
                                break;
                            case "category":
                                user.setCategory(Category.valueOf(currentPropertyValue.toUpperCase()));
                                break;
                        }
                        currentPropertyValue = "";
                    }
                    break;


            }
        }
        throw new XMLStreamException("student could not be read from file");
    }

}
