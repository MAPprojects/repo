package Repositories;

import Domain.Nota;
import Domain.Studenti;
import Domain.Teme;
import Validators.IValidator;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamWriter;
public class NoteXMLRepository extends AbstractXMLRepository<Nota,Integer>{
    public NoteXMLRepository(String fileName, IValidator<Nota> _iv){
        super(fileName,_iv);
    }

    protected String getLoadForFXMLReader(){
        return "nota";
    }
    protected String getLoadForFXMLWriter(){
        return "note";
    }
    protected Nota citesteObiect(XMLStreamReader reader) throws XMLStreamException {
        String id = reader.getAttributeValue(null, "id");
        Nota s = new Nota();
        s.setId(Integer.parseInt(id));
        String currentPropertyValue = "";
        while (reader.hasNext()) {
            int event = reader.next();
            switch (event) {
                case XMLStreamReader.END_ELEMENT:
                    if (reader.getLocalName().equals("nota")) {
                        return s;
                    }
                    else {
                        if (reader.getLocalName().equals("studentID")) {
                            s.setStudent(Integer.parseInt(currentPropertyValue));
                        }
                        if (reader.getLocalName().equals("temaID")) {
                            s.setTema(Integer.parseInt(currentPropertyValue));
                        }
                        if (reader.getLocalName().equals("valoare")) {
                            s.setValoare(Integer.parseInt(currentPropertyValue));
                        }
                        currentPropertyValue = "";
                    }
                    break;
                case XMLStreamReader.CHARACTERS:
                    currentPropertyValue = reader.getText().trim();
                    break;
            }
        } throw new XMLStreamException("nu s-a citit nota");
    }

    public void writeInFile(Nota x, XMLStreamWriter writer) throws XMLStreamException{
        writer.writeStartElement("nota");
        writer.writeAttribute("id",Integer.toString(x.getId()));
        writer.writeDTD("\n     ");
        writer.writeStartElement("studentID");
        writer.writeDTD(Integer.toString(x.getIdStudent()));

        writer.writeEndElement();
        writer.writeDTD("\n     ");
        writer.writeStartElement("temaID");
        writer.writeDTD(Integer.toString(x.getIdTema()));
        writer.writeEndElement();
        writer.writeDTD("\n     ");
        writer.writeStartElement("valoare");
        writer.writeDTD(Integer.toString(x.getValoare()));
        writer.writeEndElement();
        writer.writeDTD("\n     ");
        writer.writeEndElement();
    }


    public void update(Integer id,String valoareS,String saptamanaPredareS){
        findOne(id).setValoare(Integer.parseInt(valoareS));
        super.update(id,valoareS,saptamanaPredareS);
    }

}
