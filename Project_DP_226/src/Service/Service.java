package Service;

import Entites.Candidate;
import Entites.FilterSorter;
import Entites.Option;
import Entites.Section;
import Repository.Repository;
import Utils.*;
import Utils.CandidateEvent;
import Utils.Observable;
import Utils.Observer;
import Validators.ValidationException;

import java.math.BigInteger;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class Service implements Observable<Event> {
    private Repository<Candidate, Integer> candidateRepository;
    private FilterSorter<Candidate> candidateFilterSorter = new FilterSorter<>();
    private List<Observer<Event>> observers = new ArrayList<>();


    Repository<Section, Integer> sectionRepository;
    Repository<Option, Integer> optionRepository;
    FilterSorter<Section> sectionFilterSorter = new FilterSorter<>();
    FilterSorter<Option> optionFilterSorter = new FilterSorter<>();

    public Service(Repository<Candidate, Integer> candidateRepository, Repository<Section, Integer> sectionRepository, Repository<Option, Integer> optionRepository) {
        this.candidateRepository = candidateRepository;
        this.sectionRepository = sectionRepository;
        this.optionRepository = optionRepository;
    }


    /*
    *                              CANDIDATES
     */
    public void addCandidate(Candidate candidate) throws ValidationException {
        candidateRepository.save(candidate);
        notifyObservers(new CandidateEvent(EventType.ADD, candidate));
    }
    public Candidate findCandidate(String name){
        for(Candidate candidate:findAllCandidates()){
            if(candidate.getName().equals(name))
                return candidate;
        }
        return null;
    }
    public void updateCandidate(Integer id, Candidate candidate) throws ValidationException {
        candidateRepository.update(id, candidate);
        notifyObservers(new CandidateEvent(EventType.UPDATE, candidate));

    }

    public Candidate deleteCandidate(Integer id) throws ValidationException {

        Optional<Candidate> candidateOptional = candidateRepository.delete(id);
        if (candidateOptional.isPresent()) {
            Candidate candidate = candidateOptional.get();
            notifyObservers(new CandidateEvent(EventType.DELETE, candidate));
            return candidate;
        } else {
            throw new ValidationException("Id doesn't exist");
        }
    }
    public void alertLoadDataC(){

        candidateRepository.loadData();
    }
    public ArrayList<Candidate> findAllCandidates() {
        return candidateRepository.findAll();
    }

    public long sizeCandidates() {
        return candidateRepository.size();
    }

    public ArrayList<Candidate> filterByName(String name) {
        ArrayList<Candidate> candidates = findAllCandidates();
        //filter
        Predicate<Candidate> predicate = c1 -> c1.getName().contains(name);
        candidates = candidateFilterSorter.filterAndSorter(candidates, predicate, null);
        return candidates;
    }

    public ArrayList<Candidate> filterByPhoneNumber(String phoneNumber) {
        ArrayList<Candidate> candidates = findAllCandidates();
        //filter
        Predicate<Candidate> predicate = c1 -> c1.getPhoneNumber().contains(phoneNumber);
        candidates = candidateFilterSorter.filterAndSorter(candidates, predicate, null);
        return candidates;
    }

    public ArrayList<Candidate> sortByName() {
        ArrayList<Candidate> candidates = findAllCandidates();
        //sort
        Comparator<Candidate> comparator = (c1, c2) -> c1.getName().compareTo(c2.getName());
        candidates = candidateFilterSorter.filterAndSorter(candidates, null, comparator);
        return candidates;
    }

    /*
       *                           SECTIONS
        */
    public void alertLoadDataS(){

        sectionRepository.loadData();
    }
    public void addSection(Section section) throws ValidationException {
        sectionRepository.save(section);
        notifyObservers(new SectionEvent(EventType.ADD, section));
    }
    public Section findSection(String name){
        for(Section section:findAllSections()){
            if(section.getName().equals(name))
                return section;
        }
        return null;
    }

    public Section deleteSection(Integer id) throws ValidationException {
        Optional<Section> sectionOptional = sectionRepository.delete(id);
        if (sectionOptional.isPresent()) {
            Section section = sectionOptional.get();
            notifyObservers(new SectionEvent(EventType.DELETE,section));
            return section;
        } else {
            throw new ValidationException("Id doesn't exist");
        }
    }

    public Section updateFilterSection(Integer id, String filter, String value) throws ValidationException {
        Optional<Section> sectionOptional = sectionRepository.updateFilter(id, filter, value);
        if (sectionOptional.isPresent()) {
            Section section = sectionOptional.get();
            return section;
        } else {
            throw new ValidationException("Id doesn't exist");
        }
    }

    public void updateSection(Integer id, Section section) throws ValidationException {
        sectionRepository.update(id, section);
        notifyObservers(new SectionEvent(EventType.UPDATE,section));

    }

    public ArrayList<Section> findAllSections() {
        return sectionRepository.findAll();
    }
    public Section findSectionbyid(Integer id){
        return sectionRepository.findOne(id);
    }

    public long sizeSection() {
        return sectionRepository.size();
    }

    public ArrayList<Section> filterByNameSection(String name) {
        ArrayList<Section> sections = findAllSections();
        //filter
        Predicate<Section> predicate = s1 -> s1.getName().contains(name);
        sections = candidateFilterSorter.filterAndSorter(sections, predicate, null);
        return sections;
    }

    public ArrayList<Section> filterByNumber(Integer number) {
        ArrayList<Section> sections = findAllSections();
        //filter
        Predicate<Section> predicate = s1 -> s1.getNumber() == number;
        sections = sectionFilterSorter.filterAndSorter(sections, predicate, null);
        return sections;
    }

    public ArrayList<Section> sortByNameSection() {
        ArrayList<Section> sections = findAllSections();
        //sort
        Comparator<Section> comparator = Comparator.comparing(Section::getName);
        sections = sectionFilterSorter.filterAndSorter(sections, null, comparator);
        return sections;
    }

    /*
    *                           OPTIONS
     */
    public void alertLoadData(){

        optionRepository.loadData();
    }

    public void addOption(Option option) throws ValidationException {
        int ok = 0;
        String section1 = "";
        for (Section section : sectionRepository.findAll()) {
            if (section.getID() == option.getIdSection()) {
                ok = 1;
                section1 = section.getName();
            }
        }
        if (ok == 1) {
            for (Candidate candidate : candidateRepository.findAll()) {
                if (candidate.getID() == option.getIdCandidate()) {
                    optionRepository.save(option);
                    new Export(candidate.getName()).export("Add", section1, option.getPriority());
                    ok = 2;
                    notifyObservers(new OptionEvent(EventType.ADD,option));
                    break;
                }
            }
        }
        if (ok != 2)
            throw new ValidationException("Id of section or candidate not exists!");
    }

    public void updateOption(Integer id, Option option) throws ValidationException {
        optionRepository.update(id, option);
        notifyObservers(new OptionEvent(EventType.UPDATE,option));

    }

    public Option deleteOption(Integer id) throws ValidationException {

        Optional<Option> optionOptional = optionRepository.delete(id);
        if (optionOptional.isPresent()) {
            Option option = optionOptional.get();
            notifyObservers(new OptionEvent(EventType.DELETE,option));

            return option;
        } else {
            throw new ValidationException("Id doesn't exist");
        }
    }

    public Option updateFilterOption(Integer id, String filter, String value) throws ValidationException {
        Optional<Option> optionOptional = optionRepository.updateFilter(id, filter, value);
        if (optionOptional.isPresent()) {
            Option option = optionOptional.get();
            Candidate candidate = candidateRepository.findOne(option.getIdCandidate());
            Section section1 = sectionRepository.findOne(option.getIdSection());
            new Export(candidate.getName()).export("Update", section1.getName(), option.getPriority());
            return option;
        } else {
            throw new ValidationException("Id invalid!");
        }
    }


    public ArrayList<Option> findAllOptions() {
        return optionRepository.findAll();
    }


    public long sizeOption() {
        return optionRepository.size();
    }

    public ArrayList<Option> filterByIdCandidate(Integer id) {
        ArrayList<Option> options = findAllOptions();
        //filter
        Predicate<Option> predicate = o1 -> o1.getIdCandidate() == id;
        options = optionFilterSorter.filterAndSorter(options, predicate, null);
        return options;
    }

    public ArrayList<Option> filterByIdSection(Integer id) {
        ArrayList<Option> options = findAllOptions();
        //filter
        Predicate<Option> predicate = o1 -> o1.getIdSection() == id;
        options = optionFilterSorter.filterAndSorter(options, predicate, null);
        return options;
    }

    private int compareNumbers(Integer a, Integer b) {
        if (a.compareTo(b) < 0)
            return -1;
        else if (a.compareTo(b) > 0)
            return 1;
        else
            return 0;
    }

    public ArrayList<Option> sortByIdSection() {
        ArrayList<Option> options = findAllOptions();
        //sort
        Comparator<Option> comparator = (o1, o2) -> {
            return compareNumbers(o1.getIdSection(), o2.getIdSection());
        };
        options = optionFilterSorter.filterAndSorter(options, null, comparator);
        return options;
    }

//    private Map<Integer, ArrayList<Integer>> appearsSections() {
//        /*
//        *   Map with a list for each section in which option appears.
//        * */
//        ArrayList<Section> sections = new ArrayList<>();
//        Map<Integer, Integer> sectionHashMap = new HashMap<>();
//        this.findAllOptions().forEach(x -> {
//            sectionHashMap.put(x.getID(), x.getIdSection());
//        });
//        Map<Integer, ArrayList<Integer>> multiMap = new HashMap<>(
//                sectionHashMap.entrySet().stream()
//                        .collect(Collectors.groupingBy(Map.Entry::getValue)).values().stream()
//                        .collect(Collectors.toMap(
//                                item -> item.get(0).getValue(),
//                                item -> new ArrayList<>(
//                                        item.stream()
//                                                .map(Map.Entry::getKey)
//                                                .collect(Collectors.toList())
//                                ))
//                        ));
//        return multiMap;
//    }
//
//    public ArrayList<Section> topSections() {
//        /*
//        *Sort section by the appearance in options
//        * */
//        Map<Section, Integer> sectionHashMap = new HashMap<>();//list of appears
//        for (int i = 1; i <= appearsSections().size(); i++) {
//            sectionHashMap.put(sectionRepository.findOne(i), appearsSections().get(i).size());
//        }
//        ArrayList<Section> sectionList = new ArrayList<>();
//        findAllSections().forEach(x -> {
//            if (sectionHashMap.containsKey(x)) {
//                sectionList.add(x);
//            }
//        });
//        Collections.sort(sectionList, (o1, o2) -> sectionHashMap.get(o2) - sectionHashMap.get(o1));
//        ArrayList<Section> sectionList2 = new ArrayList<>();
//        for(int i=0;i<3;i++)
//            sectionList2.add(sectionList.get(i));
//        return sectionList2;
//    }
//
    //---Log in stuff
    public static String getMD5Hash(String text){
        try {

            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] messageDigest = md.digest(text.getBytes());
            BigInteger number = new BigInteger(1, messageDigest);
            return number.toString(16);
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;

    }



    @Override
    public void addObserver(Observer<Event> e) {
        observers.add(e);
    }

    @Override
    public void removeObserver(Observer<Event> e) {
        observers.remove(e);
    }

    @Override
    public void notifyObservers(Event t) {
        observers.stream().forEach(x -> x.notifyOnEvent(t));
    }

}