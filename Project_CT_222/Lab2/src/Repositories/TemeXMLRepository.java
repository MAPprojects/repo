package Repositories;

import Domain.Studenti;
import Domain.Teme;
import Validators.IValidator;
import Validators.ValidatorException;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamWriter;
import java.util.Calendar;

public class TemeXMLRepository extends AbstractXMLRepository<Teme,Integer>{
    public TemeXMLRepository(String fileName, IValidator<Teme> _iv){
        super(fileName,_iv);
    }

    protected String getLoadForFXMLReader(){
        return "tema";
    }
    protected String getLoadForFXMLWriter(){
        return "teme";
    }
    protected Teme citesteObiect(XMLStreamReader reader) throws XMLStreamException {
        String id = reader.getAttributeValue(null, "id");
        Teme s = new Teme();
        s.setId(Integer.parseInt(id));
        String currentPropertyValue = "";
        while (reader.hasNext()) {
            int event = reader.next();
            switch (event) {
                case XMLStreamReader.END_ELEMENT:
                    if (reader.getLocalName().equals("tema")) {
                        return s;
                    }
                    else {
                        if (reader.getLocalName().equals("cerinta")) {
                            s.setCerinta(currentPropertyValue);
                        }
                        if (reader.getLocalName().equals("deadline")) {
                            s.setDeadline(Integer.parseInt(currentPropertyValue));
                        }
                        currentPropertyValue = "";
                    }
                    break;
                case XMLStreamReader.CHARACTERS:
                    currentPropertyValue = reader.getText().trim();
                    break;
            }
        } throw new XMLStreamException("nu s-a citit tema");
    }

    public void writeInFile(Teme x, XMLStreamWriter writer) throws XMLStreamException{
        writer.writeStartElement("tema");
        writer.writeAttribute("id",Integer.toString(x.getId()));
        writer.writeDTD("\n     ");
        writer.writeStartElement("cerinta");
        writer.writeDTD(x.getCerinta());

        writer.writeEndElement();
        writer.writeDTD("\n     ");
        writer.writeStartElement("deadline");
        writer.writeDTD(Integer.toString(x.getDeadline()));
        writer.writeEndElement();
        writer.writeDTD("\n   ");
        writer.writeEndElement();
    }


    public void update(Integer id,String filtru,String valoare){
        if(filtru.equals("cerinta")){
            findOne(id).setCerinta(valoare);
        }
        else if(filtru.equals("deadline")){
            if(Integer.parseInt(valoare)<2||Integer.parseInt(valoare)>14){
                throw new ValidatorException("Tema deadline "+Integer.parseInt(valoare)+" trebuie sa fie cuprinsa intre 2 si 14");
            }
            Teme tema=findOne(id);
            Calendar calendar= Calendar.getInstance();

            if(tema.getDeadline()<=(calendar.get(calendar.WEEK_OF_YEAR)-40))
                throw new RepositoryException("S-a depasit termenul admis: "+tema.getDeadline());
            else if(tema.getDeadline()>Integer.parseInt(valoare))
                throw new RepositoryException("Nu se poate modifica deadlineul");
            else {
                tema.setDeadline(Integer.parseInt(valoare));
            }
        }
        else
            throw new RepositoryException("Acest atribut al temei nu exista");
        super.update(id,filtru,valoare);
    }

}