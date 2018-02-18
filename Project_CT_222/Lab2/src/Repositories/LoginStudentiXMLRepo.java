package Repositories;

import Domain.LoginObject;
import Domain.Studenti;
import Validators.IValidator;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamWriter;

public class LoginStudentiXMLRepo extends AbstractXMLRepository<LoginObject,String> {
    public LoginStudentiXMLRepo (String fileName, IValidator<LoginObject> _iv){
        super(fileName,_iv);
    }

    protected String getLoadForFXMLReader(){
        return "Buser";
    }
    protected String getLoadForFXMLWriter(){
        return "Busers";
    }
    protected LoginObject citesteObiect(XMLStreamReader reader) throws XMLStreamException {
        String email = reader.getAttributeValue(null, "email");
        LoginObject s = new LoginObject();
        s.setId(email);
        String currentPropertyValue = "";
        while (reader.hasNext()) {
            int event = reader.next();
            switch (event) {
                case XMLStreamReader.END_ELEMENT:
                    if (reader.getLocalName().equals("Buser")) {
                        return s;
                    }
                    else {
                        if (reader.getLocalName().equals("user")) {
                            s.setUser(currentPropertyValue);
                        }
                        if (reader.getLocalName().equals("password")) {
                            s.setPassword(currentPropertyValue);
                        }
                        if(reader.getLocalName().equals("priority")){
                            s.setPriority(Integer.parseInt(currentPropertyValue));
                        }
                        currentPropertyValue = "";
                    }
                    break;
                case XMLStreamReader.CHARACTERS:
                    currentPropertyValue = reader.getText().trim();
                    break;
            }
        } throw new XMLStreamException("nu s-a citit student");
    }

    public void writeInFile(LoginObject x, XMLStreamWriter writer) throws XMLStreamException{
        writer.writeStartElement("Buser");
        writer.writeAttribute("email",x.getId());
        writer.writeDTD("\n     ");
        writer.writeStartElement("user");
        writer.writeDTD(x.getUser());

        writer.writeEndElement();
        writer.writeDTD("\n     ");
        writer.writeStartElement("password");
        writer.writeDTD(x.getPassword());
        writer.writeEndElement();
        writer.writeDTD("\n     ");
        writer.writeStartElement("priority");
        writer.writeDTD(Integer.toString(x.getPriority()));
        writer.writeEndElement();
        writer.writeDTD("\n   ");
        writer.writeEndElement();
    }


    public void update(Integer id,String filtru,String valoare){

    }
}
