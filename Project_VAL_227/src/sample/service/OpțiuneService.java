package sample.service;
import sample.domain.*;
import sample.repository.OpțiuneFileRepository;
import sample.repository.OpțiuneRepository;
import sample.repository.RepositoryException;
import sample.ui.adminUI.OpțiuneController_Candidat;
import sample.ui.adminUI.OpțiuneController_Secție;
import sample.utils.EventType;
import sample.utils.Observable;
import sample.utils.Observer;
import sample.utils.OpțiuneEvent;
import sample.validator.OpțiuneValidator;

import java.util.Objects;
import java.util.Vector;

public class OpțiuneService extends AbstractService<String, Opțiune, OpțiuneRepository, OpțiuneValidator> implements Observable<OpțiuneEvent> {
    // variabile
    private Vector<Observer<OpțiuneEvent>> observers = new Vector<>();
    private Vector<OpțiuneController_Candidat> opțiuneControlleriCandidat;
    private Vector<OpțiuneController_Secție> opțiuneControlleriSecție;

    // metode
    public OpțiuneService(){
        super(new OpțiuneRepository(), new OpțiuneValidator());
        opțiuneControlleriCandidat = new Vector<>();
        opțiuneControlleriSecție = new Vector<>();
    }

    public OpțiuneService(String filename, String xmlName, String path, String logFileName){
        super(new OpțiuneFileRepository(filename, path, xmlName, logFileName), new OpțiuneValidator());
        opțiuneControlleriCandidat = new Vector<>();
        opțiuneControlleriSecție = new Vector<>();
    }

    public OpțiuneService(OpțiuneRepository repository, OpțiuneValidator validator) {
        super(repository, validator);
    }

    public void addOpțiuneControllerCandidat(OpțiuneController_Candidat opțiuneControllerCandidat) {
        opțiuneControlleriCandidat.add(opțiuneControllerCandidat);
    }
    public void addOpțiuneControllerSecție(OpțiuneController_Secție opțiuneControllerSecție) {
        opțiuneControlleriSecție.add(opțiuneControllerSecție);
    }

    /**
     * Se primește o opțiune nouă a candidatului, care are 1 singură secție.
     * Dacă candidatul are deja opțiuni alese, atunci secția tocmai primită i se adaugă în lista de opțiuni.
     * @param opțiunea cu secția nouă
     * @return opțiunile candidatului
     * @throws Exception dacă candidatul s-a înscris deja la secția dorită
     */
    @Override
    public Opțiune add(Opțiune opțiune) throws Exception {
        String idSecțieNouă = opțiune.getIdPrimaSecție();
        // verificăm dacă candidatul are deja opțiuni
        try {
            opțiune = repository.findOne(opțiune.getID());
            opțiune.addSecție(idSecțieNouă);
            // salvăm opțiunea
            repository.update(opțiune);
        }
        catch (RepositoryException E) {
            // salvăm opțiunea
            repository.add(opțiune);
        }

        // anunțăm observatorii că s-a produs o modificare
        notifyObservers(new OpțiuneEvent(opțiune, EventType.ADD));

        // returnăm opțiunea
        return opțiune;
    }

    /**
     * Se primește opțiunea pe care candidatul dorește să o șteargă. Opțiunea trebuie să aibă 1 singură secție.
     * @param opțiune
     * @return opțiunea ștearsă
     * @throws Exception dacă candidatul nu e înscris la secția pe care vrea să o șteargă
     */
    public Opțiune remove(Opțiune opțiune) throws Exception {
        String idSecție = opțiune.getIdPrimaSecție();
        // verificăm dacă candidatul are opțiunea
        try {
            opțiune = repository.findOne(opțiune.getID());
            if (opțiune.getIdSecții().size() == 1)
                // candidatul e înscris doar la această secție => ștergem întreaga opțiune
                repository.delete(opțiune.getID());
            else {
                // ștergem secția din opțiune
                opțiune.removeSecție(idSecție);
                repository.update(opțiune);
            }

            // anunțăm observatorii că s-a produs o modificare
            notifyObservers(new OpțiuneEvent(opțiune, EventType.DELETE));

            // returnăm opțiunea
            return opțiune;
        }
        catch (Exception e) {
            throw e;
        }
    }

    @Override
    public Opțiune update(Opțiune opțiune) throws Exception {
        // actualizăm opțiunea
        Opțiune opțiuneNouă = super.update(opțiune);

        // anunțăm observatorii
        notifyObservers(new OpțiuneEvent(opțiune, EventType.UPDATE));

        // returnăm opțiunea
        return opțiune;
    }

    /**
     * Primim ID-ul candidatului, ID-ul secției nemaidorite și ID-ul secției nou dorite.
     * Dacă secția nou dorită a fost optată deja, atunci aceasta se va șterge din listă și va înlocui secția veche.
     * @return opțiunea modificată
     * @throws Exception dacă candidatul nu a optat pentru idSecție
     */
    public Opțiune update(String idCandidat, String idSecție, String idSecțieNouă) throws Exception {
        // verificăm dacă candidatul are opțiunea
        try {
            Opțiune opțiune = repository.findOne(idCandidat);
            // verificăm dacă candidatul a optat deja pentru secția nouă
            if (opțiune.getIdSecții().contains(idSecțieNouă))
                opțiune.removeSecție(idSecțieNouă);
            // actualizăm secția
            opțiune.updateSecție(idSecție, idSecțieNouă);
            repository.update(opțiune);

            // anunțăm observatorii că s-a produs o modificare
            notifyObservers(new OpțiuneEvent(opțiune, EventType.UPDATE));

            // returnăm opțiunea
            return opțiune;
        }
        catch (Exception e) {
            throw e;
        }
    }

    public Opțiune findOne(String idCandidat, String idSecție) throws Exception {
        Opțiune opțiune = repository.findOne(idCandidat);
        Vector<String> idSecții = opțiune.getIdSecții();
        if (idSecții.contains(idSecție))
            return new Opțiune(idCandidat, idSecție);
        else
            throw new RepositoryException("Candidatul nu s-a înscris la această secție.");
    }


    /**
     * Se scrie în log ce s-a adăugat/șters/modificat.
     * Parametrul secțieVeche este opțional.
     * @param candidat - candidatul ce se înscrie/elimină la o anumită secție
     * @param secție - secția optată de candidat (la care s-a înscris / renunțat)
     * @param status - poate fi „adăugat”, „eliminat” sau „actualizat”
     * @param secțieVeche - dacă status = „actualizat”, atunci în log se va scrie la ce secție a renunțat candidatul (secțieVeche). Altfel, dați un new Secție();
     */
    public void addLog(Candidat candidat, Secție secție, Status status, Secție secțieVeche) {
        OpțiuneFileRepository.addLog(candidat, secție, status, secțieVeche);
    }
    public void addLog(Candidat candidat, Vector<Secție> secții, Status status, Secție secțieVeche) {
        for (Secție secție : secții)
            OpțiuneFileRepository.addLog(candidat, secție, status, secțieVeche);
    }

    /**
     * Returnează prioritatea secției pe care a ales-o candidatul (cu idOpțiune = idCandidat).
     * Cea mai mare prioritate este 1. Apoi 2, 3 ș.a.m.d.
     * @param idOpțiune = idCandidat
     * @param idSecție = secția pentru care a optat candidatul
     * @return prioritatea
     * @throws RepositoryException dacă opțiunea nu există
     * @throws OpțiuneException dacă candidatul nu a optat pentru secția dată
     */
    public Integer getPrioritate(String idOpțiune, String idSecție) throws Exception {
        Opțiune opțiune = findOne(idOpțiune);
        return opțiune.getIdSecții().indexOf(idSecție) + 1;
    }

    /**
     * @param idSecție - secția
     * @return True dacă secția a fost aleasă de cel puțin un candidat și False în caz contrar.
     */
    public Boolean aFostOptată(String idSecție) {
        Vector<Opțiune> opțiuni = (Vector<Opțiune>) findAll();
        for (Opțiune opțiune : opțiuni)
            if (opțiune.getIdSecții().contains(idSecție))
                return Boolean.TRUE;

        return Boolean.FALSE;
    }

    /**
     * @param idSecție - secția
     * @return Numărul de optări pentru secția dată.
     */
    public Integer nrOptări(String idSecție) {
        Vector<Opțiune> opțiuni = (Vector<Opțiune>) findAll();
        Integer nrOptări = 0;

        for (Opțiune opțiune :opțiuni)
            if (opțiune.getIdSecții().contains(idSecție))
                nrOptări++;

        return nrOptări;
    }

    /**
     * @param idSecție - secția
     * @param prioritate - pe ce poziție se va căuta secția în lista de opțiuni a fiecărui candidat
     *                   - prioritatea începe de la 1
     * @return Numărul de apariții ale secției din lista de opțiuni a fiecărui candidat de pe poziția „prioritate”
     */
    public Integer nrOptăriPrioritate(String idSecție, Integer prioritate) {
        if (prioritate < 1)
            prioritate = 1;

        Vector<Opțiune> opțiuni = (Vector<Opțiune>) findAll();
        Integer nrOptări = 0;

        for (Opțiune opțiune : opțiuni) {
            if (opțiune.getIdSecții().size() >= prioritate)
                if (Objects.equals(opțiune.getIdSecții().elementAt(prioritate - 1), idSecție))
                    nrOptări++;
        }

        return nrOptări;
    }

    /**
     * @param idSecție - secția
     * @return De câte ori a fost optată secția dată
     */
    public Integer getNrOptări(String idSecție) {
        Vector<Opțiune> opțiuni = (Vector<Opțiune>) findAll();
        Integer nrOptări = 0;

        for (Opțiune opțiune : opțiuni)
            if (opțiune.getIdSecții().contains(idSecție))
                nrOptări++;

        return nrOptări;
    }


    @Override
    public void addObserver(Observer event) {
        observers.add(event);
    }

    @Override
    public void removeObserver(Observer event) {
        observers.remove(event);
    }

    @Override
    public void notifyObservers(OpțiuneEvent event) {
        observers.forEach(observer -> observer.notifyOnEvent(event));

        for (OpțiuneController_Secție controller : opțiuneControlleriSecție)
            if (controller != null)
                controller.notifyOnEvent();
    }


}
