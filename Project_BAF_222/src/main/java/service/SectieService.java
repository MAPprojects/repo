package service;

import entities.Sectie;
import repository.Repository;


public class SectieService extends AbstractService<Sectie,Integer> {

    public SectieService(Repository<Integer, Sectie> sectieRepository){
        super(sectieRepository);
    }
}
