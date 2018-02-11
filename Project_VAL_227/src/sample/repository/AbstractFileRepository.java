package sample.repository;

import sample.domain.HasID;

import java.util.Map;
import java.util.Optional;

public abstract class AbstractFileRepository<ID, E extends HasID<ID>> extends AbstractRepository<ID, E> {
    private String fileName;
    private String xmlName;
    private String path;

    public AbstractFileRepository(String fileName, String xmlName, String path) {
        super();
        this.fileName = fileName;
        this.xmlName = xmlName;
        this.path = path;
//        entities = readFromFile(fileName, path);
//        saveToXML(xmlName, path, entities);
//        entities.clear();
        entities = readFromXML(xmlName, path);
    }

    @Override
    public E add(E entity) throws RepositoryException {
        E savedEntity =  super.add(entity);
        saveToFile(fileName, path, entities);
        saveToXML(xmlName, path, entities);
        return savedEntity;
    }

    @Override
    public E delete(ID id) throws RepositoryException {
        E deletedEntity = super.delete(id);
        saveToFile(fileName, path, entities);
        saveToXML(xmlName, path, entities);
        return deletedEntity;
    }

    @Override
    public Optional<E> deleteOptional(ID id) {
        try{
            return Optional.ofNullable(delete(id));
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    @Override
    public E update(E entity) throws Exception {
        E updatedEntity = super.update(entity);
        saveToFile(fileName, path, entities);
        saveToXML(xmlName, path, entities);
        return updatedEntity;
    }

    /**
     * Citește entitățile din fișierul cu numele dat și le returnează într-un dicționar.
     * Dacă fișierul nu există, atunci se creează unul nou.
     * @param fileName
     * @return dicționarul cu entitățiile citite
     */
    protected abstract Map<ID, E> readFromFile(String fileName, String path);

    /**
     * Salvează entitățiile primite în fișierul cu numele dat.
     * Dacă fișierul există, datele din el se vor pierde.
     * @param fileName - numele fișierului în care vor fi salvate entitățile
     * @param entities - dicționar în care se află entitățile
     */
    protected abstract void saveToFile(String fileName, String path, Map<ID, E> entities);


    /**
     * Citește entitățile din fișierul XML cu numele dat și le returnează într-un dicționar.
     * Dacă fișierul nu există, atunci se creează unul nou.
     * @param xmlName
     * @return dicționarul cu entitățiile citite
     */
    protected abstract Map<ID, E> readFromXML(String xmlName, String path);

    /**
     * Salvează entitățiile primite în fișierul XML cu numele dat.
     * Dacă fișierul există, datele din el se vor pierde.
     * @param xmlName - numele fișierului în care vor fi salvate entitățile
     * @param entities - dicționar în care se află entitățile
     */
    protected abstract void saveToXML(String xmlName, String path, Map<ID, E> entities);
}
