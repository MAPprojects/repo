package XMLRepository;

import Domain.Nota;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public interface InterfaceRepositoryXML<Id, E> {

    public void loadData();

    public void writeToXML();

    public void appendToXML(Document doc, Element nList, E nota);

    public E createObject(Element element);
}
