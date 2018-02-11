package sample.service;
import sample.domain.*;
import sample.repository.RepositoryException;
import sample.ui.adminUI.OpțiuneController_Candidat;
import sample.validator.CandidatValidator;
import sample.validator.ContValidator;

import javax.xml.bind.ValidationException;
import java.util.*;
import java.util.function.Predicate;


public class GeneralService {
    private CandidatService candidatService;
    private SecțieService secțieService;
    private OpțiuneService opțiuneService;
    private ContService contService;
    private Map<String, Vector<Candidat>> repartiție;


    // Constructor default care folosește InMemoryRepository
    public GeneralService() {
        candidatService = new CandidatService();
        secțieService = new SecțieService();
        opțiuneService = new OpțiuneService();
        contService = new ContService();
    }

    // Constructor default care folosește FileRepository
    public GeneralService(String fișier) {
        candidatService = new CandidatService("candidați.txt", "candidați.xml", "./src/sample/data/");
        secțieService = new SecțieService("secții.txt", "secții.xml", "./src/sample/data/");
        opțiuneService = new OpțiuneService("opțiuni.txt", "opțiuni.xml", "./src/sample/data/", "log_Opțiuni.txt");
        contService = new ContService("conturi.txt", "conturi.xml", "./src/sample/data/");
    }
    public GeneralService(CandidatService candidatService, SecțieService secțieService, OpțiuneService opțiuneService) {
        this.candidatService = candidatService;
        this.secțieService = secțieService;
        this.opțiuneService = opțiuneService;
        contService = new ContService();
    }

    public GeneralService(CandidatService candidatService, SecțieService secțieService, OpțiuneService opțiuneService, ContService contService) {
        this.candidatService = candidatService;
        this.secțieService = secțieService;
        this.opțiuneService = opțiuneService;
        this.contService = contService;
    }

    public GeneralService(OpțiuneService opțiuneService) {
        candidatService = new CandidatService("candidați.txt", "candidați.xml", "./src/sample/data/");
        secțieService = new SecțieService("secții.txt", "secții.xml", "./src/sample/data/");
        this.opțiuneService = opțiuneService;
    }

    private void inițializeazăRepartiția() {
        repartiție = new HashMap<>();
        for (Secție secție : getSecții())
            repartiție.put(secție.getID(), new Vector<>());
    }

    public CandidatService getCandidatService() {
        return candidatService;
    }
    public SecțieService getSecțieService() {
        return secțieService;
    }
    public OpțiuneService getOpțiuneService() {
        return opțiuneService;
    }
    public ContService getContService() {
        return contService;
    }
    public void setCandidatService(CandidatService candidatService) {
        this.candidatService = candidatService;
    }
    public void setSecțieService(SecțieService secțieService) {
        this.secțieService = secțieService;
    }
    public void setOpțiuneService(OpțiuneService opțiuneService) {
        this.opțiuneService = opțiuneService;
    }
    public void setContService(ContService contService) { this.contService = contService; }

    /**
     * Generează automat entități
     */
    public void creeazăEntitățiDefault() throws Exception {
        try {
            // crearea candidaților
            adaugăCandidat("1", "Andrei", "Unu", "0799172805", "andrei_vd2006@yahoo.com");
            adaugăCandidat("2", "Lucian", "Doi","0754477470", "sonicandrei24@gmail.com");
            adaugăCandidat("3", "Vaida", "Trei","0123456789", "vair2213@scs.ubbcluj.ro");
            // crearea secțiilor
            adaugăSecție("IR_B", "Informatică", "Română", FormăDeFinanțare.BUGET, 180);
            adaugăSecție("IE_T", "Informatică", "Engleză", FormăDeFinanțare.TAXĂ, 140);
            // crearea opțiunilor
            adaugăOpțiune("1", "IR");
            adaugăOpțiune("1","IE");
            adaugăOpțiune("2", "IE");
            adaugăOpțiune("2","IR");
            adaugăOpțiune("3","IR");
        }
        catch (Exception e){
            throw e;
        }
    }

    /**** getEntitate */
    public Candidat getCandidat(String cnp) throws RepositoryException {
        return candidatService.findOne(cnp);
    }

    public Secție getSecție(String id) throws Exception {
        return secțieService.findOne(id);
    }

    public Opțiune getOpțiune(String cnpCandidat) throws Exception {
        try {
            return opțiuneService.findOne(cnpCandidat);

        } catch (RepositoryException e) {
            Candidat candidat = getCandidat(cnpCandidat);    // aruncă excepție dacă nu îl găsește
            //return new Opțiune(idCandidat);
            throw e;
        }
    }


    public Opțiune getOpțiune(String idCandidat, String idSecție) throws Exception {
        return opțiuneService.findOne(idCandidat, idSecție);
    }

    /**
     * Returnează prioritatea secției pe care a ales-o candidatul.
     * Cea mai mare prioritate este 1. Apoi 2, 3 ș.a.m.d.
     * @param idCandidat - ID-ul candodatului = ID-ul opțiunii
     * @param idSecție = secția pentru care a optat candidatul
     * @return prioritatea
     * @throws RepositoryException dacă opțiunea nu există
     * @throws OpțiuneException dacă candidatul nu a optat pentru secția dată
     */
    public Integer getPrioritate(String idCandidat, String idSecție) throws Exception {
        return opțiuneService.getPrioritate(idCandidat, idSecție);
    }

    public Integer getNrOptări(String idSecție) {
        return opțiuneService.getNrOptări(idSecție);
    }

    public Integer getNrOptăriPrioritate(String idSecție, Integer prioritate) {
        return opțiuneService.nrOptăriPrioritate(idSecție, prioritate);
    }


    /**** getAll */
    public Vector<Candidat> getCandidați(){
        try {
            return (Vector<Candidat>) candidatService.findAll();
        }
        catch (Exception e){
            return new Vector<Candidat>();
        }
    }

    public Vector<Secție> getSecții(){
        try {
            return (Vector<Secție>) secțieService.findAll();
        }
        catch (Exception e){
            return new Vector<Secție>();
        }
    }

    public Vector<Opțiune> getOpțiuni(){
        try {
            return (Vector<Opțiune>) opțiuneService.findAll();
        }
        catch (Exception e){
            return new Vector<Opțiune>();
        }
    }

    /**** adaugăEntitate */

    public Candidat adaugăCandidat(String cnp, String nume, String prenume, String telefon, String e_mail) throws Exception {
        Candidat candidat = new Candidat(cnp, nume, prenume, telefon, e_mail);
        try{
            candidatService.add(candidat);
            return candidat;
        }
        catch (Exception e){
            throw e;
        }
    }

    public Secție adaugăSecție(String id, String nume, String limbaDePredare, FormăDeFinanțare formăDeFinanțare, Integer nrLocuri) throws Exception {
        Secție secție = new Secție(id, nume, limbaDePredare, formăDeFinanțare, nrLocuri);
        try{
            secțieService.add(secție);
            return secție;
        }
        catch (Exception e){
            throw e;
        }
    }

    public Opțiune adaugăOpțiune(String idCandidat, String idSecție) throws Exception {
        Opțiune opțiune = new Opțiune(idCandidat, idSecție);
        try {
            // verificăm dacă există candidatul și secția dorită
            Candidat candidat = getCandidat(idCandidat);
            Secție secție = getSecție(idSecție);

            // salvăm opțiunea
            opțiuneService.add(opțiune);

            // scriem în log
            opțiuneService.addLog(candidat, secție, Status.adăugat, new Secție());

            // returnăm opțiunea
            return opțiune;
        }
        catch (Exception e){
            throw e;
        }
    }


    /**** ștergeEntitate */

    public Candidat ștergeCandidat(String id) throws Exception {
        try{
            return candidatService.remove(id);
        }
        catch (Exception e){
            throw e;
        }
    }

    public Candidat ștergeCandidatSafe(String id) throws Exception {
        Integer nrOptări = 0;
        try { nrOptări = opțiuneService.findOne(id).getIdSecții().size(); }
        catch (Exception ignored){}
        if (nrOptări > 0)
            throw new Exception("Nu s-a șters candidatul fiindcă e înscris la " + nrOptări + " secții.");

        return ștergeCandidat(id);
    }

    public Secție ștergeSecție(String id) throws Exception {
        try{
            return secțieService.remove(id);
        }
        catch (Exception e){
            throw e;
        }
    }
    public Secție ștergeSecțieSafe(String id) throws Exception {
        Integer nrOptări = opțiuneService.getNrOptări(id);
        if (nrOptări > 0)
            throw new Exception("Secția este optată de " + nrOptări + " candidați.");

        return ștergeSecție(id);
    }

    public Optional<Secție> ștergeSecțieOptional(String id) {
        return secțieService.deleteOptional(id);
    }

    public Opțiune ștergeOpțiune(String idCandidat, String idSecție) throws Exception {
        try {
            // verificăm dacă există candidatul și secția dorită
            Candidat candidat = getCandidat(idCandidat);
            Secție secție = getSecție(idSecție);
            Opțiune opțiune = new Opțiune(idCandidat, idSecție);

            // ștergem opțiunea
            opțiuneService.remove(opțiune);

            // scriem în log
            opțiuneService.addLog(candidat, secție, Status.eliminat, new Secție());

            // returnăm opțiunea
            return opțiune;
        }
        catch (Exception e){
            throw e;
        }
    }

    public Opțiune ștergeOpțiune(String idCandidat) throws Exception {
        try {
            // verificăm dacă există candidatul
            Candidat candidat = getCandidat(idCandidat);
            Opțiune opțiune = getOpțiune(idCandidat);
            Vector<Secție> secții = new Vector<>();
            for (String idSecție : opțiune.getIdSecții())
                try { secții.add(getSecție(idSecție)); }
                catch (RepositoryException e) {}

            // ștergem opțiunea
            opțiuneService.remove(idCandidat);

            // scriem în log
            opțiuneService.addLog(candidat, secții, Status.eliminat, new Secție());

            // returnăm opțiunea
            return opțiune;
        }
        catch (Exception e){
            throw e;
        }
    }


    /**** modificăEntitate */

    public Candidat modificăCandidat(String id, String nume, String prenume, String telefon, String e_mail) throws Exception {
        Candidat candidat = new Candidat(id, nume, prenume, telefon, e_mail);
        try{
            return candidatService.update(candidat);
        }
        catch (Exception e){
            throw e;
        }
    }

    public Secție modificăSecție(String id, String nume, String limbaDePredare, FormăDeFinanțare formăDeFinanțare, Integer nrLocuri) throws Exception {
        Secție secție = new Secție(id, nume, limbaDePredare, formăDeFinanțare, nrLocuri);
        try{
            return secțieService.update(secție);
        }
        catch (Exception e){
            throw e;
        }
    }

    public Opțiune modificăOpțiune(String idCandidat, String idSecție, String idSecțieNouă) throws Exception {
        // verificăm dacă există opțiunea, candidatul, secția actuală și secția nou dorită
        Opțiune opțiune = getOpțiune(idCandidat);
        Candidat candidat = getCandidat(idCandidat);
        Secție secțieVeche = getSecție(idSecție);
        Secție secțieNouă = getSecție(idSecțieNouă);

        // actualizăm opțiunea
        try {
            opțiune = opțiuneService.update(idCandidat, idSecție, idSecțieNouă);

            // scriem în log
            opțiuneService.addLog(candidat, secțieNouă, Status.mutat, secțieVeche);

        } catch (Exception e) {
            throw e;
        }

        // returnăm opțiunea
        return opțiune;
    }

    public Opțiune modificăOpțiune_Prioritate(String idCandidat, String idSecție, Integer prioritateNouă) throws Exception {
        // verificăm dacă există candidatul, opțiunea și secția
        Opțiune opțiune = getOpțiune(idCandidat, idSecție);
        // Candidatul a ales secția, așa că o putem modifica
        opțiune = getOpțiune(idCandidat);
        Candidat candidat = getCandidat(idCandidat);
        Secție secție = getSecție(idSecție);

        // actualizăm opțiunea
        try {
            Opțiune opțiune1 = opțiune.updatePrioritate(idSecție, prioritateNouă);
            Opțiune opțiune2 = opțiuneService.update(opțiune1);

            // scriem în log
            //opțiuneService.addLog(candidat, secțieNouă, Status.mutat, secțieVeche);

        } catch (Exception e) {
            throw e;
        }

        // returnăm opțiunea
        return opțiune;
    }


    /*** filtrări */

    // Candidat
    /**
     * @param nume - numele parțial al candidatului; poate fi și o literă
     * @return Candidații al căror nume încep subșirul de caractere „nume”
     */
    public Vector<Candidat> filtrareCandidați_nume(String nume) {
        return candidatService.filtrare_nume(nume);
    }

    /**
     * @param telefon - telefonul parțial al candidatului; poate fi și o cifră
     * @return Candidații care în al căror număr de telefon există subșirul de caractere „telefon”
     */
    public Vector<Candidat> filtrareCandidați_telefon(String telefon) {
        return candidatService.filtrare_telefon(telefon);
    }

    /**
     * @param domeniu - ex: Yahoo, Gmail, .com, .ro, etc...
     * @return Toți candidații care au adresa de e-mail din domeniul dat
     */
    public Vector<Candidat> filtrareCandidați_email(String domeniu) {
        return candidatService.filtrare_email(domeniu);
    }

    // Secții
    /**
     * @param nume - numele (parțial) al secției
     * @return Secțiile cu numele dat
     */
    public Vector<Secție> filtrareSecții_nume(String nume, Vector<Secție> secții) {
        return secțieService.filtrare_nume(nume, secții);
    }

    /**
     * @param limbaDePredare - limba de predare
     * @return Secțiile cu în care se predă cu limba dată
     */
    public Vector<Secție> filtrareSecții_limbaDePredare(String limbaDePredare, Vector<Secție> secții) {
        return secțieService.filtrare_limbaDePredare(limbaDePredare, secții);
    }

    /**
     * @param formăDeFinanțare - forma de finanțare
     * @return Secțiile cu forma de finanțare dată
     */
    public Vector<Secție> filtrareSecții_formaDeFinanțare(FormăDeFinanțare formăDeFinanțare, Vector<Secție> secții) {
        return secțieService.filtrare_formaDeFinanțare(formăDeFinanțare, secții);
    }

    /**
     * @param nrLocuri - numărul minim de locuri pe care trebuie să îl aibă secția
     * @return Secțiile care au cel puțin nrLocuri
     */
    public Vector<Secție> filtrareSecții_nrMinimLocuri(Integer nrLocuri, Vector<Secție> secții) {
        return secțieService.filtrare_nrMinimLocuri(nrLocuri, secții);
    }

    /**
     * @param nrLocuri - numărul maxim de locuri pe care trebuie să îl aibă secția
     * @return Secțiile care au cel mult nrLocuri
     */
    public Vector<Secție> filtrareSecții_nrMaximLocuri(Integer nrLocuri, Vector<Secție> secții) {
        return secțieService.filtrare_nrMaximLocuri(nrLocuri, secții);
    }

    // Opțiuni
    /**
     * @param idSecție - secția dorită
     * @return Toți candidații care au optat pentru secția dată, în ordinea priorității de optare pentru secție.
     * @throws Exception dacă nu există secția
     */
    public  Vector<Candidat> filtrareOpțiuni_secție(String idSecție) throws Exception {
        // verificăm dacă secția există
        getSecție(idSecție);    // aruncă excepție dacă secția nu există

        // luăm toate opțiunile
        Vector<Opțiune> opțiuni = (Vector<Opțiune>) opțiuneService.findAll();

        // creăm predicatul
        Predicate<Opțiune> predicat = (Opțiune opțiune)-> {
            return opțiune.getIdSecții().contains(idSecție);
        };

        // creăm comparatorul
        Comparator<Opțiune> comparatorOpțiune = (Opțiune o1, Opțiune o2)-> {
            Integer prioritateO1 = o1.getIdSecții().indexOf(idSecție);
            Integer prioritateO2 = o2.getIdSecții().indexOf(idSecție);

            if (Objects.equals(prioritateO1, prioritateO2)) {
                // dacă sunt candidați care au optat secția X pe aceeași poziție, atunci îi sortăm după nume
                try {
                    Candidat candidat1 = candidatService.findOne(o1.getID());
                    Candidat candidat2 = candidatService.findOne(o2.getID());
                    return candidat1.getNume().compareTo(candidat2.getNume());
                }
                catch (RepositoryException e) {
                    return o1.getID().compareTo(o2.getID());
                }
            }
            else
                return prioritateO1.compareTo(prioritateO2);
        };

        // filtrăm și sortăm opțiunile
        Vector<Opțiune> opțiuniFiltrateSortate = AbstractService.filterAndSorter(opțiuni, predicat, comparatorOpțiune);

        // Acum avem opțiunile candidaților sortate, dar noi dorim să returnăm candidații
        Vector<Candidat> candidațiFiltrațiSortați = new Vector<>();
        for (Opțiune opțiune : opțiuniFiltrateSortate) {
            Candidat candidat;
            try { candidat = candidatService.findOne(opțiune.getID()); }
            catch (RepositoryException e) { candidat = new Candidat(); }
            candidațiFiltrațiSortați.add(candidat);
        }

        return candidațiFiltrațiSortați;
    }

    /**
     * @return Toate secțiile optate în ordinea optării candidaților pentru ele.
     */
    public Vector<Secție> getCeleMaiOptateSecții() {
        Vector<Secție> secții = (Vector<Secție>) secțieService.findAll();

        // creăm predicatul
        Predicate<Secție> predicat = (Secție secție)-> {
            return opțiuneService.aFostOptată(secție.getID());
        };

        // creăm comparatorul
        Comparator<Secție> comparatorOpțiune = (Secție s1, Secție s2)-> {
            Integer nrOptăriS1 = opțiuneService.nrOptări(s1.getID());
            Integer nrOptăriS2 = opțiuneService.nrOptări(s2.getID());
            return nrOptăriS2.compareTo(nrOptăriS1);
        };

        // filtrăm și sortăm opțiunile
        return AbstractService.filterAndSorter(secții, predicat, comparatorOpțiune);
    }

    /**
     * @return Secțiile în funcție de câți candidați au optat pentru ele ca prima opțiune (prioritate 1).
     */
    public Vector<Secție> getCeleMaiDoriteNsecții(Integer n) {
        Vector<Secție> secții = new Vector<>();
        Integer prioritate;

        for (prioritate = 1; prioritate <= n; prioritate++) {
            Vector<Secție> secțies = getCeleMaiDoriteSecțiiPrioritate(prioritate);
            if (secțies.size() > 0)
                secții.addAll(secțies);
        }

        return secții;
    }

    public Vector<Secție> getCeleMaiDoriteSecțiiPrioritate(Integer prioritate) {
        Vector<Secție> secții = (Vector<Secție>) secțieService.findAll();

        // creăm predicatul
        Predicate<Secție> predicat = (Secție secție)-> {
            return opțiuneService.nrOptăriPrioritate(secție.getID(), prioritate) > 0;
        };

        // creăm comparatorul
        Comparator<Secție> comparatorOpțiune = (Secție s1, Secție s2)-> {
            Integer nrOptăriS1 = opțiuneService.nrOptăriPrioritate(s1.getID(), prioritate);
            Integer nrOptăriS2 = opțiuneService.nrOptăriPrioritate(s2.getID(), prioritate);
            return nrOptăriS2.compareTo(nrOptăriS1);
        };

        // filtrăm și sortăm opțiunile
        return AbstractService.filterAndSorter(secții, predicat, comparatorOpțiune);
    }


    /**
     * @param idCandidat
     * @return Toate secțiile alese de candidat în ordinea optării lui pentru ele.
     */
    public Vector<Secție> getSecțiiAlese(String idCandidat) {
        Vector<Secție> secțiiAlese = new Vector<>();
        try {
            Opțiune opțiune = getOpțiune(idCandidat);
            for (String idSecție : opțiune.getIdSecții()) {
                try {
                    Secție secție = getSecție(idSecție);
                    secțiiAlese.add(secție);
                } catch (RepositoryException e) {
                    secțiiAlese.add(new Secție(idSecție, "Fără nume", "necunoscută", FormăDeFinanțare.BUGET, 0));
                }
            }
        }
        catch (Exception ignored) {}
        return secțiiAlese;
    }


    public void setOpțiuneControllerCandidat_înCandidatService(OpțiuneController_Candidat opțiuneControllerCandidat) {
        candidatService.addOpțiuneControllerCandidat(opțiuneControllerCandidat);
    }
    public void setOpțiuneControllerCandidat_înSecțieService(OpțiuneController_Candidat opțiuneControllerCandidat) {
        secțieService.addOpțiuneControllerCandidat(opțiuneControllerCandidat);
    }



    public UserType login(String cnp, String parolă) throws Exception {
        // validarea datelor
        if (cnp.length() == 0)
            throw new ValidationException("Introdu CNP-ul.");
        if (parolă.length() == 0)
            throw new ValidationException("Introdu parola.");

        // administrator
        if (cnp.equals("admin") && parolă.equals("admin"))
            return UserType.ADMINISTRATOR;

        // candidat
        Vector<Cont> conturi = (Vector<Cont>) contService.findAll();
        for (Cont cont : conturi) {
            if (cont.getID().equals(cnp)) {
                if (cont.getParola().equals(parolă))
                    return UserType.USER;
                else
                    throw new ValidationException("Parolă incorectă.");
            }
        }

        // contul nu există, dar verificăm dacă există candidatul cu același CNP
        try {
            Candidat candidat = getCandidat(cnp);
            // candidatul există; verificăm dacă userul a introdus parola default
            if (!candidat.getTelefon().equals(parolă))
                throw new ValidationException("Parolă incorectă.");

            Cont cont = new Cont(cnp, parolă);
            contService.addDefault(cont);
            return UserType.USER;
        }
        catch (Exception ignored) {
            // nu există nici cont nici candidat
            throw new ValidationException("Contul nu există.");
        }
    }

    public void adaugăCont(Candidat candidat, String parolă, String confirmareParolă) throws Exception {
        // validarea datelor candidatului
        CandidatValidator candidatValidator = new CandidatValidator();
        candidatValidator.validează(candidat);

        // validarea parolei
        Cont cont = new Cont(candidat.getID(), parolă);
        ContValidator contValidator = new ContValidator();
        contValidator.validează(cont);
        if (!parolă.equals(confirmareParolă))
            throw new ValidationException("Parolele nu coincid.");

        // adăugarea candidatului și a contului
        candidatService.add(candidat);
        contService.add(cont);
    }

    public void modificăParolă(String cnp, String parolăVeche, String parolăNouă, String confirmareParolăNouă) throws Exception {
        Cont cont = contService.findOne(cnp);

        if (!cont.getParola().equals(parolăVeche))
            throw new ValidationException("Parolă incorectă.");

        if (!parolăNouă.equals(confirmareParolăNouă))
            throw new ValidationException("Parolele noi nu coincid.");

        contService.update(new Cont(cnp, parolăNouă));
    }

    /**
     * Dacă parola e corectă, atunci candidatul este eliminat de la toate secțiile la care s-a înscris,
     * apoi e eliminat și apoi i se șterge și contul.
     * @param cnpCandidat
     * @param parola
     */
    public void ștergeContSmart(String cnpCandidat, String parola) throws Exception {
        Cont cont = contService.findOne(cnpCandidat);
        if (!cont.getParola().equals(parola))
            throw new ValidationException("Parolă incorectă.");

        // eliminăm candidatul de la toate secțiile (dacă s-a înscris la vreouna)
        try { opțiuneService.remove(cnpCandidat); }
        catch (Exception ignored) {}
        // ștergem candidatul
        candidatService.remove(cnpCandidat);
        // ștergem contul
        contService.remove(cnpCandidat);
    }


    /** Repartiția candidaților */

    /**
     * Funcția repartizează toți candidații la secții.
     * Rezultatul e salvat în memorie.
     */
    public Map<String, Vector<Candidat>> repartizeazăCadidați() {
        inițializeazăRepartiția();

        Vector<Secție> secții = getSecții();
        Vector<Candidat> candidați = new Vector<>(getCandidați());          // facem copie fiindcă vom elimina din el candidații repartizați
        Vector<Candidat> candidațiNerepartizați = new Vector<>();

        // repartizăm candidații în funcție de prioritate (mai întâi prioritatea 1, apoi 2...)
        for (Integer prioritate = 1; prioritate <= secții.size(); prioritate++) {
            candidațiNerepartizați = new Vector<>(candidați);

            // reparizăm fiecare candidat nerepartizat în prima secție aleasă disponibilă
            for (Candidat candidat : candidați) {
                try {
                    String idSecție = getOpțiune(candidat.getID()).getIdSecții().elementAt(prioritate - 1);
                    Secție secție = getSecție(idSecție);
                    // verificăm dacă mai sunt locuri
                    if (repartiție.get(idSecție).size() < secție.getNrLocuri()) {
                        // sunt locuri => repartizăm candidatul și îl eliminăm din vector
                        repartiție.get(idSecție).add(candidat);
                        candidațiNerepartizați.remove(candidat);
                    }

                }
                // candidatul nu s-a înscris la nici o secție, nu are nicio secție cu prioritatea actuală sau secția nu există
                catch (Exception ignored) {}
            }

            candidați = candidațiNerepartizați;
        }

        return repartiție;
    }

    /**
     * @param secție - secția vizată
     * @return lista candidaților repartizați la această secție
     */
    public Vector<Candidat> getRepartiție(Secție secție) {
        return new Vector<>(repartiție.get(secție.getID()));
    }
}
