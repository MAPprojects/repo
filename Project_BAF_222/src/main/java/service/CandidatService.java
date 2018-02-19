package service;

import entities.Candidat;
import observer.AbstractObservable;
import repository.Repository;

import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class CandidatService extends AbstractService<Candidat,Integer>{

    public CandidatService(Repository<Integer, Candidat> candidatRepo){
        super(candidatRepo);
    }










}
