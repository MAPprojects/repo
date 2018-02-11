package sample.service;

import sample.domain.Candidat;
import sample.repository.CandidatFileRepository;
import sample.repository.CandidatRepository;
import sample.repository.RepositoryException;
import sample.ui.adminUI.OpțiuneController_Candidat;
import sample.ui.adminUI.OpțiuneController_Secție;
import sample.utils.CandidatEvent;
import sample.utils.EventType;
import sample.utils.Observable;
import sample.utils.Observer;
import sample.validator.CandidatValidator;

import javax.xml.bind.ValidationException;
import java.util.Comparator;
import java.util.Vector;
import java.util.function.Predicate;


public class CandidatService extends AbstractService<String, Candidat, CandidatRepository, CandidatValidator> implements Observable<CandidatEvent> {
    // variabile
    private Vector<Observer<CandidatEvent>> observers = new Vector<>();
    private Vector<OpțiuneController_Candidat> opțiuneControlleriCandidat;
    private Vector<OpțiuneController_Secție> opțiuneControlleriSecție;

    // metode
    public CandidatService() {
        super(new CandidatRepository(), new CandidatValidator());
        opțiuneControlleriCandidat = new Vector<>();
        opțiuneControlleriSecție = new Vector<>();
        OpțiuneController_Candidat opțiuneControllerCandidat = new OpțiuneController_Candidat();
        opțiuneControlleriCandidat.add(opțiuneControllerCandidat);
    }

    public CandidatService(String fileName, String xmlName, String path) {
        super(new CandidatFileRepository(fileName, xmlName, path), new CandidatValidator());
        opțiuneControlleriCandidat = new Vector<>();
        opțiuneControlleriSecție = new Vector<>();
    }

    public CandidatService(CandidatRepository repository, CandidatValidator validator) {
        super(repository, validator);
    }

    public void addOpțiuneControllerCandidat(OpțiuneController_Candidat opțiuneControllerCandidat) {
        opțiuneControlleriCandidat.add(opțiuneControllerCandidat);
    }
    public void addOpțiuneControllerSecție(OpțiuneController_Secție opțiuneControllerSecție) {
        opțiuneControlleriSecție.add(opțiuneControllerSecție);
    }

    @Override
    public Candidat add(Candidat candidat) throws Exception {
        try{
            // validăm datele
            validator.validează(candidat);

            // verificăm dacă candidatul există
            try{
                repository.findOne(candidat.getID());
                throw new Exception("Candidatul cu CNP-ul " + candidat.getID() + " există deja.");
            }
            catch (RepositoryException e) {
                // salvăm candidatul
                repository.add(candidat);

                // anunțăm observatorii că s-a produs o modificare
                notifyObservers(new CandidatEvent(candidat, EventType.ADD));

                // returnăm candidatul
                return candidat;
            }
        }
        catch (ValidationException e){
            throw e;
        }
    }

    @Override
    public Candidat remove(String cnp) throws Exception {
        try{
            Candidat candidat = repository.delete(cnp);

            // anunțăm observatorii că s-a produs o modificare
            notifyObservers(new CandidatEvent(candidat, EventType.DELETE));

            // returnăm candidatul
            return candidat;
        }
        catch (RepositoryException e){
            throw new RepositoryException("Candidatul cu CNP-ul " + cnp + " nu există.");
        }
    }

    @Override
    public Candidat update(Candidat candidat) throws Exception {
        // validăm datele
        validator.validează(candidat);

        // actualizăm candidatul
        try {
            repository.update(candidat);
        } catch (RepositoryException e){
                throw new RepositoryException("Candidatul cu CNP-ul " + candidat.getID() + " nu există.");
        }

        // anunțăm observatorii că s-a produs o modificare
        notifyObservers(new CandidatEvent(candidat, EventType.UPDATE));

        // returnăm candidatul
        return candidat;
    }

    @Override
    public Candidat findOne(String cnp) throws RepositoryException {
        try{
            return repository.findOne(cnp);
        }
        catch (Exception e){
            throw new RepositoryException("Candidatul cu ID-ul " + cnp + " nu există.");
        }
    }

    @Override
    public Iterable<Candidat> findAll() {
        Vector<Candidat> candidați = (Vector<Candidat>) repository.findAll();

        // sortăm lista
        Comparator<Candidat> comparatorNume = (Candidat c1, Candidat c2)-> {
            return (c1.getNume().toLowerCase().compareTo(c2.getNume().toLowerCase()));
        };
        candidați.sort(comparatorNume);
        return (Iterable<Candidat>) candidați;
    }

    /**
     * @param nume - numele (parțial sau complet) al candidaților
     * @return Candidații al căror nume încep subșirul de caractere „nume”
     */
    public Vector<Candidat> filtrare_nume(String nume) {
        Vector<Candidat> candidați = (Vector<Candidat>) findAll();

        // creăm predicatul
        Predicate<Candidat> predicat = (Candidat candidat)-> {
            return candidat.getNume().toLowerCase().startsWith(nume.toLowerCase());
        };

        // creăm comparatorul
        Comparator<Candidat> comparatorNume = (Candidat c1, Candidat c2)-> {
            return (c1.getNume().toLowerCase().compareTo(c2.getNume().toLowerCase()));
        };

        return filterAndSorter(candidați, predicat, comparatorNume);
    }

    /**
     * @param prenume - prenumele (parțial sau complet) al candidaților
     * @return Candidații al căror prenume încep subșirul de caractere „nume”
     */
    public Vector<Candidat> filtrare_prenume(String prenume) {
        Vector<Candidat> candidați = (Vector<Candidat>) findAll();

        // creăm predicatul
        Predicate<Candidat> predicat = (Candidat candidat)-> {
            return candidat.getPrenume().toLowerCase().startsWith(prenume.toLowerCase());
        };

        // creăm comparatorul
        Comparator<Candidat> comparatorNume = (Candidat c1, Candidat c2)-> {
            return (c1.getNume().toLowerCase().compareTo(c2.getNume().toLowerCase()));
        };

        return filterAndSorter(candidați, predicat, comparatorNume);
    }

    /**
     * @param telefon - numărul de telefon (parțial sau complet) al candidaților
     * @return Candidații care în al căror număr de telefon există subșirul de caractere „telefon”
     */
    public Vector<Candidat> filtrare_telefon(String telefon) {
        Vector<Candidat> candidați = (Vector<Candidat>) findAll();

        // creăm predicatul
        Predicate<Candidat> predicat = (Candidat candidat)-> {
            return candidat.getTelefon().toLowerCase().contains(telefon.toLowerCase());
        };

        // creăm comparatorul
        Comparator<Candidat> comparatorNume = (Candidat c1, Candidat c2)-> {
            return (c1.getTelefon().compareTo(c2.getTelefon()));
        };

        return filterAndSorter(candidați, predicat, comparatorNume);
    }

    /**
     * Returnează candidații care au adresă de e-mail asemănătoare cu cea dată.
     * Aceștia sunt sortați după nume.
     * @param email - poate fi parțial
     * @return un vector cu candidații ce respectă criteriile de mai sus
     */
    public Vector<Candidat> filtrare_email(String email) {
        Vector<Candidat> candidați = (Vector<Candidat>) findAll();

        // creăm predicatul
        Predicate<Candidat> predicat = (Candidat candidat)-> {
            return candidat.getE_mail().toLowerCase().contains(email.toLowerCase());
        };

        // creăm comparatorul
        Comparator<Candidat> comparatorNume = (Candidat c1, Candidat c2)-> {
            return (c1.getE_mail().compareTo(c2.getE_mail()));
        };

        return filterAndSorter(candidați, predicat, comparatorNume);
    }

    /**
     * Returnează candidații care au adresă de e-mail de la compania dată (Yahoo, Google...).
     * Aceștia sunt sortați după nume.
     * @param domeniu - poate fi Yahoo, Google, .com, .ro, etc...
     * @return un vector cu candidații ce respectă criteriile de mai sus
     */
    public Vector<Candidat> filtrare_domeniuEmail(String domeniu) {
        Vector<Candidat> candidați = (Vector<Candidat>) findAll();

        // creăm predicatul
        Predicate<Candidat> predicat = (Candidat candidat)-> {
            Integer pozițieArond = candidat.getE_mail().indexOf('@');
            if (pozițieArond < 0)
                return false;

            String domeniuCandidat = candidat.getE_mail().substring(pozițieArond);
            return domeniuCandidat.toLowerCase().contains(domeniu.toLowerCase());
        };

        // creăm comparatorul
        Comparator<Candidat> comparatorNume = (Candidat c1, Candidat c2)-> {
            return (c1.getE_mail().compareTo(c2.getE_mail()));
        };

        return filterAndSorter(candidați, predicat, comparatorNume);
    }



    // OBSERVABLE
    @Override
    public void addObserver(Observer event) {
        observers.add(event);
    }

    @Override
    public void removeObserver(Observer event) {
        observers.remove(event);
    }

    @Override
    public void notifyObservers(CandidatEvent event) {
        observers.forEach(observer->observer.notifyOnEvent(event));

        for (OpțiuneController_Candidat controller : opțiuneControlleriCandidat)
            if (controller != null)
                controller.notifyOnEvent();

        for (OpțiuneController_Secție controller : opțiuneControlleriSecție)
            if (controller != null)
            controller.notifyOnEvent();
    }

}
