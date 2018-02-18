package Repositories;

import Domain.Studenti;
import Validators.IValidator;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamWriter;

public class StudentiXMLRepository extends AbstractXMLRepository<Studenti,Integer>{
    public StudentiXMLRepository(String fileName, IValidator<Studenti> _iv){
        super(fileName,_iv);
    }

    protected String getLoadForFXMLReader(){
        return "student";
    }
    protected String getLoadForFXMLWriter(){
        return "students";
    }
    protected Studenti citesteObiect(XMLStreamReader reader) throws XMLStreamException {
        String id = reader.getAttributeValue(null, "id");
        Studenti s = new Studenti();
        s.setId(Integer.parseInt(id));
        String currentPropertyValue = "";
        while (reader.hasNext()) {
            int event = reader.next();
            switch (event) {
                case XMLStreamReader.END_ELEMENT:
                    if (reader.getLocalName().equals("student")) {
                        return s;
                    }
                    else {
                        if (reader.getLocalName().equals("nume")) {
                            s.setNume(currentPropertyValue);
                        }
                        if (reader.getLocalName().equals("grupa")) {
                            s.setGrupa(Integer.parseInt(currentPropertyValue));
                        }
                        if (reader.getLocalName().equals("email")) {
                            s.setEmail(currentPropertyValue);
                        }
                        if(reader.getLocalName().equals("profesor")){
                            s.setCadruDidactic(currentPropertyValue);
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

    public void writeInFile(Studenti x, XMLStreamWriter writer) throws XMLStreamException{
        writer.writeStartElement("student");
        writer.writeAttribute("id",Integer.toString(x.getId()));
        writer.writeDTD("\n     ");
        writer.writeStartElement("nume");
        writer.writeDTD(x.getNume());

        writer.writeEndElement();
        writer.writeDTD("\n     ");
        writer.writeStartElement("grupa");
        writer.writeDTD(Integer.toString(x.getGrupa()));
        writer.writeEndElement();
        writer.writeDTD("\n     ");
        writer.writeStartElement("email");
        writer.writeDTD(x.getEmail());
        writer.writeEndElement();
        writer.writeDTD("\n     ");
        writer.writeStartElement("profesor");
        writer.writeDTD(x.getCadruDidactic());
        writer.writeEndElement();
        writer.writeDTD("\n   ");
        writer.writeEndElement();
    }


    public void update(Integer id,String filtru,String valoare){
        System.out.println(filtru);
        if(filtru.equals("nume")){
            System.out.println("Intru nume");
            findOne(id).setNume(valoare);
        }
        else if(filtru.equals("grupa")){
            System.out.println("Intru grupa");
            findOne(id).setGrupa(Integer.parseInt(valoare));
        }
        else if(filtru.equals("e-mail")){
            System.out.println("Intru e-mail");
            findOne(id).setEmail(valoare);
        }
        else if(filtru.equals("cadru didactic")){
            System.out.println("Intru cd");
            findOne(id).setCadruDidactic(valoare);
        }
        else
            throw new RepositoryException("Acest atribut al studentului nu exista");

        super.update(id,filtru,valoare);
    }

}

