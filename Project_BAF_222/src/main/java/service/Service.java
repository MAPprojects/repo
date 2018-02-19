package service;

import entities.Candidat;
import observer.AbstractObservable;
import repository.Repository;

import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class Service extends AbstractObservable {
    private Repository<Integer, Candidat> candidatRepo;
    //private Repository<Integer,Sectie> sectieRepo;
    //private Repository<CheieOptiune, Optiune> optiuneRepo;
/*
    public Service(Repository<Integer,Candidat> candidatRepo, Repository<Integer,Sectie> sectieRepo, Repository<CheieOptiune, Optiune> optiuneRepo){
        this.candidatRepo=candidatRepo;
        this.sectieRepo=sectieRepo;
        this.optiuneRepo=optiuneRepo;
    }
*/
    public Service(Repository<Integer, Candidat> candidatRepo){
        this.candidatRepo=candidatRepo;
    }

    public void addCandidat(int id,String nume,String telefon, String email){
        Candidat candidat=new Candidat(id,nume,telefon,email);
        candidatRepo.save(candidat);
    }

    public void addCandidat(Candidat candidat) {
        candidatRepo.save(candidat);
        super.notifyObservers();
    }
/*
    public void addOptiune(int idOptiune,int idCandidat,int idSectie){
        candidatRepo.findOne(idCandidat);
        sectieRepo.findOne(idSectie);
        Optiune optiune=new Optiune(idOptiune,idCandidat,idSectie);
        int maxim=0;
        for(Optiune aux:optiuneRepo.getAll()){
            if(aux.getIdCandidat()==optiune.getIdCandidat())
                maxim=max(maxim,aux.getPrioritate());
        }
        optiune.setPrioritate(maxim+1);
        optiuneRepo.save(optiune);
    }

    public void addSectie(int id,String nume,int nrLoc){
        Sectie sectie=new Sectie(id,nume,nrLoc);
        sectieRepo.save(sectie);
    }
*/
    public Candidat deleteCandidat(int id){
        Candidat c=candidatRepo.delete(id);
        super.notifyObservers();
        return c;
    }

    public void updateCandidat(int id,String nume,String telefon, String email){
        Candidat candidat=new Candidat(id,nume,telefon,email);
        candidatRepo.update(id,candidat);
    }

    public void updateCandidat(Candidat c){
        candidatRepo.update(c.getID(),c);
        super.notifyObservers();
    }
/*
    public void updateSectie(int id,String nume,int nrLoc){
        Sectie sectie=new Sectie(id,nume,nrLoc);
        sectieRepo.update(id,sectie);
    }

    public void updateOptiune(int idOptiune,int idCandidat,int idSectie,int prioritate){
        Optiune optiune=new Optiune(idOptiune,idCandidat,idSectie);
        optiune.setPrioritate(prioritate);
        optiuneRepo.update(optiune,optiune);

    }

    public Sectie deleteSectie(int id){
        return sectieRepo.delete(id);
    }

    public Optiune deleteOptiune(int id){

        for(Optiune optiune:optiuneRepo.getAll())
            if(optiune.getIdOptiune()==id) {
                Optiune optiune1=optiuneRepo.delete(optiune);
                int prioritate=optiune1.getPrioritate();
                boolean merge=true;
                while(merge) {
                    merge = false;
                    for (Optiune optiune3 : optiuneRepo.getAll())
                        if (optiune3.getIdCandidat() == optiune1.getIdCandidat() && optiune3.getPrioritate() == prioritate + 1) {
                            updateOptiune(optiune3.getIdOptiune(), optiune3.getIdCandidat(), optiune3.getIdSectie(), prioritate);
                            prioritate = optiune3.getPrioritate();
                            merge=true;
                            break;
                        }
                }
                return optiune1;
            }
        throw new RepositoryException("ID-ul "+id+" nu exista!");
    }
*/
    public Candidat getCandidat(int id){
        return candidatRepo.findOne(id);
    }
/*
    public Sectie getSectie(int id){
        return sectieRepo.findOne(id);
    }



    public Iterable<Sectie> getAllSectii(){
        return sectieRepo.getAll();
    }

    public Iterable<Optiune> getAllOptiuni(){return optiuneRepo.getAll();}

*/

    public Iterable<Candidat> getAllCandidati(){
        return candidatRepo.getAll();
    }


    public <E> List<E> filterAndSorter(Iterable<E> lista, Predicate<E> pred, Comparator<E> comp){
        return  StreamSupport.stream(lista.spliterator(), false).filter(pred).sorted(comp).collect(Collectors.toList());
    }


    public void add3Candidati(){
        addCandidat(1234,"Popescu","0754123544","popescu@mail.com");
        addCandidat(2434,"Grigore","0754353564","grigore@mail.com");
        addCandidat(5641,"Vasile","0346312355","vasile@mail.com");
    }

    public void add3Sectii(){
        //addSectie(1,"Mate-Info",120);
        //addSectie(2,"Info-Romana",210);
        //addSectie(3,"Info-Englezaz",210);
    }

    public void add3Optiuni(){
        //addOptiune(4,545,6);
        //addOptiune(5,545,7);
        //addOptiune(6,24,2);
        //updateOptiune(4,1234,5,1);
        //updateOptiune(1,1234,2,8);
    }


}
