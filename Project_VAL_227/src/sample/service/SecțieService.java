package sample.service;
import sample.domain.FormăDeFinanțare;
import sample.domain.Secție;
import sample.repository.RepositoryException;
import sample.repository.SecțieFileRepository;
import sample.repository.SecțieRepository;
import sample.ui.adminUI.OpțiuneController_Candidat;
import sample.ui.adminUI.OpțiuneController_Secție;
import sample.ui.userUI.UserController;
import sample.utils.EventType;
import sample.utils.Observable;
import sample.utils.Observer;
import sample.utils.SecțieEvent;
import sample.validator.SecțieValidator;

import javax.xml.bind.ValidationException;
import java.util.Comparator;
import java.util.Objects;
import java.util.Optional;
import java.util.Vector;
import java.util.function.Predicate;

public class SecțieService extends AbstractService<String, Secție, SecțieRepository, SecțieValidator> implements Observable<SecțieEvent> {
    // variabile
    private Vector<Observer<SecțieEvent>> observers = new Vector<>();
    private Vector<OpțiuneController_Candidat> opțiuneControlleriCandidat;
    private Vector<OpțiuneController_Secție> opțiuneControlleriSecție;
    private Vector<UserController> userControllers;

    // metode
    public SecțieService() {
        super(new SecțieRepository(), new SecțieValidator());
        opțiuneControlleriCandidat = new Vector<>();
        opțiuneControlleriSecție = new Vector<>();
        userControllers = new Vector<>();
    }

    public SecțieService(String filename, String xmlName, String path) {
        super(new SecțieFileRepository(filename, xmlName, path), new SecțieValidator());
        opțiuneControlleriCandidat = new Vector<>();
        opțiuneControlleriSecție = new Vector<>();
        userControllers = new Vector<>();
    }

    public SecțieService(SecțieRepository repository, SecțieValidator validator) {
        super(repository, validator);
    }

    public void addOpțiuneControllerCandidat(OpțiuneController_Candidat opțiuneControllerCandidat) {
        opțiuneControlleriCandidat.add(opțiuneControllerCandidat);
    }
    public void addOpțiuneControllerSecție(OpțiuneController_Secție opțiuneControllerSecție) {
        opțiuneControlleriSecție.add(opțiuneControllerSecție);
    }
    public void addUserController(UserController userController) {
        userControllers.add(userController);
    }

    @Override
    public Secție add(Secție secție) throws Exception {
        try{
            // validăm datele
            validator.validează(secție);

            // verificăm dacă secția există
            try{
                repository.findOne(secție.getID());
                throw new Exception("Secția cu ID-ul " + secție.getID() + " există deja.");
            }
            catch (RepositoryException e) {
                // salvăm secția
                repository.add(secție);

                // anunțăm observatorii că s-a produs o modificare
                notifyObservers(new SecțieEvent(secție, EventType.ADD));

                // returnăm secția
                return secție;
            }
        }
        catch (ValidationException e){
            throw e;
        }
    }

    @Override
    public Secție remove(String id) throws Exception {
        try{
            Secție secție = repository.delete(id);

            // anunțăm observatorii că s-a produs o modificare
            notifyObservers(new SecțieEvent(secție, EventType.DELETE));

            // returnăm secția
            return secție;
        }
        catch (RepositoryException e){
            throw new RepositoryException("Secția cu ID-ul " + id + " nu există.");
        }
    }

    @Override
    public Secție update(Secție secție) throws Exception {
        // validăm datele
        validator.validează(secție);

        // actualizăm secția
        try { repository.update(secție); }
        catch (RepositoryException e){ throw new RepositoryException("Secția cu ID-ul " + secție.getID() + " nu există."); }

        // anunțăm observatorii că s-a produs o modificare
        notifyObservers(new SecțieEvent(secție, EventType.UPDATE));

        // returnăm secția
        return secție;
    }

    @Override
    public Iterable<Secție> findAll() {
        Vector<Secție> secții = (Vector<Secție>) super.findAll();
        Comparator<Secție> comparator = (Secție s1, Secție s2)-> {
            return (s1.getID().compareTo(s2.getID()));
        };
        secții.sort(comparator);
        return (Iterable<Secție>) secții;
    }


    public Optional<Secție> deleteOptional(String id) {
        return repository.deleteOptional(id);
    }


    public Vector<Secție> filtrare_id(String id, Vector<Secție> secții) {
        // creăm predicatul
        Predicate<Secție> predicat = (Secție secție)-> {
            return secție.getID().toLowerCase().startsWith(id.toLowerCase());
        };

        // creăm comparatorul
        Comparator<Secție> comparatorNume = (Secție s1, Secție s2)-> {
            return (s1.getNume().compareTo(s2.getNume()));
        };

        return filterAndSorter(secții, predicat, comparatorNume);
    }

    public Vector<Secție> filtrare_nume(String nume, Vector<Secție> secții) {
        // creăm predicatul
        Predicate<Secție> predicat = (Secție secție)-> {
            return secție.getNume().toLowerCase().contains(nume.toLowerCase());
        };

        // creăm comparatorul
        Comparator<Secție> comparatorNume = (Secție s1, Secție s2)-> {
            return (s1.getNume().compareTo(s2.getNume()));
        };

        return filterAndSorter(secții, predicat, comparatorNume);
    }

    public Vector<Secție> filtrare_limbaDePredare(String limbaDePredare, Vector<Secție> secții) {
        // creăm predicatul
        Predicate<Secție> predicat = (Secție secție)-> {
            return secție.getLimbaDePredare().toLowerCase().contains(limbaDePredare.toLowerCase());
        };

        // creăm comparatorul
        Comparator<Secție> comparatorNume = (Secție s1, Secție s2)-> {
            return (s1.getNume().compareTo(s2.getNume()));
        };

        return filterAndSorter(secții, predicat, comparatorNume);
    }

    public Vector<Secție> filtrare_formaDeFinanțare(FormăDeFinanțare formăDeFinanțare, Vector<Secție> secții) {
        // creăm predicatul
        Predicate<Secție> predicat = (Secție secție)-> {
            return secție.getFormăDeFinanțare().equals(formăDeFinanțare);
        };

        // creăm comparatorul
        Comparator<Secție> comparatorNume = (Secție s1, Secție s2)-> {
            return (s1.getNume().compareTo(s2.getNume()));
        };

        return filterAndSorter(secții, predicat, comparatorNume);
    }

    public Vector<Secție> filtrare_nrMinimLocuri(Integer nrLocuri, Vector<Secție> secții) {
        // creăm predicatul
        Predicate<Secție> predicat = (Secție secție)-> {
            return secție.getNrLocuri() >= nrLocuri;
        };

        // creăm comparatorul
        Comparator<Secție> comparatorNume = (Secție s1, Secție s2)-> {
            if (Objects.equals(s1.getNrLocuri(), s2.getNrLocuri()))
                return s1.getNume().compareTo(s2.getNume());
            return s1.getNrLocuri().compareTo(s2.getNrLocuri());
        };

        return filterAndSorter(secții, predicat, comparatorNume);
    }

    public Vector<Secție> filtrare_nrMaximLocuri(Integer nrLocuri, Vector<Secție> secții) {
        // creăm predicatul
        Predicate<Secție> predicat = (Secție secție)-> {
            return secție.getNrLocuri() <= nrLocuri;
        };

        // creăm comparatorul
        Comparator<Secție> comparatorNume = (Secție s1, Secție s2)-> {
            if (Objects.equals(s1.getNrLocuri(), s2.getNrLocuri()))
                return s1.getNume().compareTo(s2.getNume());
            return s2.getNrLocuri().compareTo(s1.getNrLocuri());
        };

        return filterAndSorter(secții, predicat, comparatorNume);
    }

    public Vector<String> getLimbileDePredare() {
        Vector<String> limbiDePredare = new Vector<>();
        for (Secție secție : findAll())
            if (!limbiDePredare.contains(secție.getLimbaDePredare().toLowerCase()))
                limbiDePredare.add(secție.getLimbaDePredare().toLowerCase());

        return limbiDePredare;
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
    public void notifyObservers(SecțieEvent event) {
        observers.forEach(observer->observer.notifyOnEvent(event));

        for (OpțiuneController_Candidat controller : opțiuneControlleriCandidat)
            if (controller != null)
                controller.notifyOnEvent();

        for (OpțiuneController_Secție controller : opțiuneControlleriSecție)
            if (controller != null)
                controller.notifyOnEvent();

        for (UserController controller : userControllers)
            if (controller != null)
                controller.notifyOnEvent();
    }

}
