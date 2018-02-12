package services;

import entities.Candidate;
import entities.Department;
import entities.Option;
import repositories.AbstractRepository;
import utils.ListEvent;
import utils.ListEventType;
import utils.Observer;
import validators.IValidator;
import validators.ValidationException;
import java.io.*;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class StaffManager implements utils.Observable<Candidate> {

    private ArrayList<Observer<Candidate>> observersList = new ArrayList<>();
    private AbstractRepository<Candidate, Integer> repoCandidates;
    private AbstractRepository<Department, Integer> repoDepartments;
    private AbstractRepository<Option, String> repoOptions;
    private IValidator<Candidate> valCandidate;
    private IValidator<Department> valDepartment;
    private IValidator<Option> valOption;

    public StaffManager(AbstractRepository<Candidate, Integer> repoCandidates, AbstractRepository<Department, Integer> repoDepartments, AbstractRepository<Option, String> repoOptions, IValidator<Candidate> valCandidate, IValidator<Department> valDepartment, IValidator<Option> valOption) {
        this.repoCandidates = repoCandidates;
        this.repoDepartments = repoDepartments;
        this.repoOptions = repoOptions;
        this.valCandidate = valCandidate;
        this.valDepartment = valDepartment;
        this.valOption = valOption;
    }

    /**
     * Save data in repo (necessary for file repository)
     */
    public void saveData() {
        this.repoCandidates.saveData();
        this.repoDepartments.saveData();
        this.repoOptions.saveData();
    }


    /**
     * Get Number of candidates
     * @return long
     */
    public long ShowSizeCandidates() {
        return this.repoCandidates.size();
    }

    /**
     * Get Number of options
     * @return long
     */
    public long ShowSizeOptions() {
        return this.repoOptions.size();
    }


    /**
     * Get Number of departments
     * @return long
     */
    public long ShowSizeDepartments() {
        return this.repoDepartments.size();
    }


    /**
     * Add a candidate in repository.
     * @param id integer
     * @param name string
     * @param phone string
     * @param email string
     */
    public void addCandidate(Integer id, String name, String phone, String email) {
        Candidate newObj = new Candidate(id, name, phone, email);
        this.valCandidate.validate(newObj);
        this.repoCandidates.save(newObj);

        ListEvent<Candidate> ev = createEvent(ListEventType.ADD, newObj, repoCandidates.findAll());
        notifyObservers(ev);

    }

    /**
     * Delete a candidate from repository based on id.
     * @param id integer
     */
    public void deleteCandidate(Integer id) {
        ArrayList<Option> l = new ArrayList<>();
        this.repoOptions.findAll().forEach(l::add);
        boolean check = false;
        for (Option o : l) {
            if (o.getIdCandidate() == id) {
                check = true;
                break;
            }
        }

        if (!check) {

            Candidate c = this.repoCandidates.delete(id);
            ListEvent<Candidate> ev = createEvent(ListEventType.REMOVE, c, repoCandidates.findAll());
            notifyObservers(ev);
        } else
            throw new ValidationException("Delete options for candidate first");

    }

    /**
     * Update a candidate in repository.
     * @param id integer
     * @param name string
     * @param phone string
     * @param email string
     */
    public void updateCandidate(Integer id, String name, String phone, String email) {
        Candidate newObj = new Candidate(id, name, phone, email);
        this.valCandidate.validate(newObj);
        this.repoCandidates.update(id, newObj);

        ListEvent<Candidate> ev = createEvent(ListEventType.UPDATE, newObj, repoCandidates.findAll());
        notifyObservers(ev);

    }

    /**
     * Find a candidate in repository
     * @param id integer
     * @return Optional(candidate)
     */
    public Optional<Candidate> findCandidate(Integer id) {
        return this.repoCandidates.findOne(id);
    }

    /**
     * Get a list with all candidates;.
     * @return iterable
     */
    public Iterable<Candidate> showCandidates() {
        return this.repoCandidates.findAll();
    }

    /**
     *  Get a list with all candidates from a certain interval.
     * @param pageNumber integer
     * @param limit integer
     * @return iterable
     */
    public Iterable<Candidate> showNextValuesCandidates(int pageNumber, int limit) {
        return this.repoCandidates.nextValues(pageNumber, limit);
    }


    /**
     *  Get a list with all options from a certain interval.
     * @param pageNumber integer
     * @param limit integer
     * @return iterable
     */
    public Iterable<Option> showNextValuesOptions(int pageNumber, int limit) {
        return this.repoOptions.nextValues(pageNumber, limit);
    }


    /**
     *  Get a list with all departments from a certain interval.
     * @param pageNumber integer
     * @param limit integer
     * @return iterable
     */
    public Iterable<Department> showNextValuesDepartments(int pageNumber, int limit) {
        return this.repoDepartments.nextValues(pageNumber, limit);
    }


    /**
     * Add a department in repository.
     * @param idDepartment integer
     * @param name string
     * @param nrLoc integer
     */
    public void addDepartment(Integer idDepartment, String name, Integer nrLoc) {
        Department newObj = new Department(idDepartment, name, nrLoc);
        this.valDepartment.validate(newObj);
        this.repoDepartments.save(newObj);
    }


    /**
     * Delete a department from repository based on id.
     * @param idDepartment integer
     */
    public void deleteDepartment(Integer idDepartment) {
        ArrayList<Option> l = new ArrayList<>();
        this.repoOptions.findAll().forEach(l::add);
        boolean check = false;
        for (Option o : l) {
            if (o.getIdDepartment() == idDepartment) {
                check = true;
                break;
            }
        }
        if (!check) {
            this.repoDepartments.delete(idDepartment);
        } else {
            throw new ValidationException("delete option first!!");
        }
    }

    /**
     *  Update a department in repository.
     * @param idDepartment integer
     * @param name string
     * @param nrLoc integer
     */
    public void updateDepartment(Integer idDepartment, String name, Integer nrLoc) {
        Department newObj = new Department(idDepartment, name, nrLoc);
        this.valDepartment.validate(newObj);
        this.repoDepartments.update(idDepartment, newObj);

    }


    /**
     * Find a department
     * @param id integer
     * @return optional(department)
     */
    public Optional<Department> findDepartment(Integer id) {
        return this.repoDepartments.findOne(id);
    }

    /**
     * Get a list with all departments.
     * @return iterable
     */
    public Iterable<Department> showDepartments() {
        return this.repoDepartments.findAll();
    }


    /**
     *  Add an option in repository
     * @param idC integer
     * @param idD integer
     * @param language string
     * @param priority integer
     */
    public void addOption(Integer idC, Integer idD, String language, Integer priority) {
        if (repoCandidates.findOne(idC) == null)
            throw new ValidationException("Candidate ID does not exist !!");

        if (repoDepartments.findOne(idD) == null)
            throw new ValidationException("Department ID does not exist !!");

        if (this.checkPriority(priority, idC))
            throw new ValidationException("This priority already exists!!");

        Option newObj = new Option(idC, idD, language, priority);
        this.valOption.validate(newObj);
        this.repoOptions.save(newObj);

        FileWriter fw = null;
        try {
            fw = new FileWriter("logs\\" + newObj.getIdCandidate() + ".txt", true);
        } catch (IOException e) {
            e.printStackTrace();
        }
        //BufferedWriter writer give better performance
        BufferedWriter bw = new BufferedWriter(fw);
        try {
            bw.write("Am adaugat optiunea cu id-ul : " + newObj.getId() + "  pentru sectia  " + repoDepartments.findOne(newObj.getIdDepartment()).get().getName() + " , limba :  " + newObj.getLanguage() + " , prioritatea  " + newObj.getPriority() + "\r");
        } catch (IOException e) {
            e.printStackTrace();
        }
        //Closing BufferedWriter Stream
        try {
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * Delete an option from repository.
     * @param idC integer
     * @param idD integer
     * @param language string
     */
    public void deleteOption(Integer idC, Integer idD, String language) {
        Option op = new Option(idC, idD, language, 0);
        Option el = this.repoOptions.delete(op.getId());


        FileWriter fw = null;
        try {
            fw = new FileWriter("logs\\" + el.getIdCandidate() + ".txt", true);
        } catch (IOException e) {
            e.printStackTrace();
        }

        //BufferedWriter writer give better performance
        BufferedWriter bw = new BufferedWriter(fw);
        try {
            bw.write("Am sters optiunea cu id-ul : " + el.getId() + "  pentru sectia  " + repoDepartments.findOne(el.getIdDepartment()).get().getName() + " , limba :  " + el.getLanguage() + " , prioritatea  " + el.getPriority() + "\r");
        } catch (IOException e) {
            e.printStackTrace();
        }
        //Closing BufferedWriter Stream
        try {
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Update an option in repository.
     * @param idCandidate integer
     * @param idDepartment integer
     * @param language string
     * @param priority integer
     */
    public void updateOption(Integer idCandidate, Integer idDepartment, String language, Integer priority) {
        Option newObj = new Option(idCandidate, idDepartment, language, priority);
        this.valOption.validate(newObj);

        if (checkPriority(priority, idCandidate))
            throw new ValidationException("Priority already exists !!");


        this.repoOptions.update(newObj.getId(), newObj);


        FileWriter fw = null;

        try {
            fw = new FileWriter("logs\\" + newObj.getIdCandidate() + ".txt", true);
        } catch (IOException e) {
            e.printStackTrace();
        }

        BufferedWriter bw = new BufferedWriter(fw);
        try {
            bw.write("Am modificat optiunea cu id-ul : " + newObj.getId() + "  pentru sectia  " + repoDepartments.findOne(newObj.getIdDepartment()).get().getName() + " , limba :  " + newObj.getLanguage() + " ,cu noua  prioritate  " + newObj.getPriority() + "\r");
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    /**
     *  Check if an option priority already exists for a candidate
     * @param priority integer
     * @param idc integer
     * @return boolean
     */
    private boolean checkPriority(Integer priority, Integer idc) {
        for (Option o : this.repoOptions.findAll()) {
            if (o.getPriority().equals(priority) && o.getIdCandidate() == idc)
                return true;
        }
        return false;
    }


    /*  Filter Options */

    /**
     * Generic sort and filter method.
     * @param init list
     * @param s predicate
     * @param cmp comparator
     * @param <E> genereic
     * @return list
     */
    private <E> List<E> sortedFilter(List<E> init, Predicate<E> s, Comparator<E> cmp) {
        return init.stream().
                filter(s).
                sorted(cmp).
                collect(Collectors.toList());
    }

    /**
     * Filter options by id of the department.
     * @param idD integer
     * @return list
     */
    public List<Option> filterOptionIDD(Integer idD) {
        ArrayList<Option> l = new ArrayList<>();
        this.showOptions().forEach(l::add);

        Predicate<Option> filter;

        filter = ((x) -> x.getIdDepartment() == idD);

        return this.sortedFilter(l, filter, Comparator.comparing(Option::getPriority));

    }

    /**
     * Filter options by options priority
     * @param priority integer
     * @return list
     */
    public List<Option> filterOptionPriority(Integer priority) {
        ArrayList<Option> l = new ArrayList<>();
        this.showOptions().forEach(l::add);

        Predicate<Option> filter;
        filter = ((x) -> x.getPriority() <= priority);

        return this.sortedFilter(l, filter, Comparator.comparing(Option::getPriority));

    }

    /**
     * Get a list with all options from repository.
     * @return iterable
     */
    public Iterable<Option> showOptions() {
        return this.repoOptions.findAll();
    }

    /**
     * Filter options by id of the  candidate.
     * @param idC integer
     * @return list
     */
    public List<Option> filterOptionIDC(Integer idC) {
        ArrayList<Option> l = new ArrayList<>();
        this.showOptions().forEach(l::add);

        Predicate<Option> filter;
        filter = ((x) -> x.getIdCandidate() == idC);

        return this.sortedFilter(l, filter, Comparator.comparing(Option::getPriority));

    }

    /**
     * Filter options by name of the candidate.
     * @param candidateName string
     * @return list
     */
    public List<Option> filterOptionName(String candidateName) {
        ArrayList<Option> l = new ArrayList<>();
        this.showOptions().forEach(l::add);

        for (int i = 0; i < l.size(); i++) {
            Integer CandidateID = l.get(i).getIdCandidate();
            String name = findCandidate(CandidateID).get().getName();


            Integer DepartmentID = l.get(i).getIdDepartment();
            String department = findDepartment(DepartmentID).get().getName();

            Option item = l.get(i);
            item.setName(name);
            item.setDepartment(department);

            l.set(i, item);
        }

        Predicate<Option> filter;
        filter = ((x) -> x.getName().contains(candidateName));

        return this.sortedFilter(l, filter, Comparator.comparing(Option::getName));

    }


    /**
     * Filter options by name of the department
     * @param departmentName string
     * @return list
     */
    public List<Option> filterOptionDepartment(String departmentName) {
        ArrayList<Option> l = new ArrayList<>();
        this.showOptions().forEach(l::add);

        for (int i = 0; i < l.size(); i++) {
            Integer CandidateID = l.get(i).getIdCandidate();
            String name = findCandidate(CandidateID).get().getName();


            Integer DepartmentID = l.get(i).getIdDepartment();
            String department = findDepartment(DepartmentID).get().getName();

            Option item = l.get(i);
            item.setName(name);
            item.setDepartment(department);

            l.set(i, item);
        }


        Predicate<Option> filter;
        filter = ((x) -> x.getDepartment().contains(departmentName));

        return this.sortedFilter(l, filter, Comparator.comparing(Option::getName));

    }

    /**
     * Filter departments that contains a given substring.
     * @param secv string
     * @return list
     */
    public List<Department> filterDepartments(String secv) {
        ArrayList<Department> l = new ArrayList<>();
        this.showDepartments().forEach(l::add);


        Predicate<Department> filter = (x) -> x.getName().startsWith(secv);

        return this.sortedFilter(l, filter, Comparator.comparing(Department::getName));

    }

    /**
     * Filter candidates that have a certain substring.
     * @param secv string
     * @return list
     */
    public List<Candidate> filterCandidates(String secv) {
        ArrayList<Candidate> l = new ArrayList<>();
        this.showCandidates().forEach(l::add);

        Predicate<Candidate> filter = (x) -> x.getName().contains(secv);

        return this.sortedFilter(l, filter, Candidate.compareName);
    }

    /**
     * Return top 3 departments after number of choices.
     * @return array
     */
    public ArrayList<Object> findTop3Departments() {
        ArrayList<Department> lista = new ArrayList<>();
        this.repoDepartments.findAll().forEach(lista::add);

        ArrayList<Option> opt = new ArrayList<>();
        this.repoOptions.findAll().forEach(opt::add);


        ArrayList<Integer> counts = new ArrayList();
        counts.add(-1);
        counts.add(-1);
        counts.add(-1);

        ArrayList<Department> deps = new ArrayList<>();
        deps.add(null);
        deps.add(null);
        deps.add(null);

        for (Department d : lista) {
            Integer count = 0;
            for (Option o : opt)
                if (o.getIdDepartment() == d.getId())
                    count++;

            if (count > counts.get(0)) {
                counts.set(0, count);
                deps.set(0, d);
            } else if (count > counts.get(1)) {
                counts.set(1, count);
                deps.set(1, d);
            } else if (count > counts.get(2)) {
                counts.set(2, count);
                deps.set(2, d);
            }
        }
        ArrayList<Object> f = new ArrayList<>();


        for (int i = 0; i < counts.size(); i++) {
            if (counts.get(i) != -1) {
                f.add(counts.get(i));
                f.add(deps.get(i));
            }
        }
        return f;

    }


    /**
     * Get top 15 candidates with most choices.
     * @return array
     */
    public ArrayList<Object> findTop15Candidates() {
        ArrayList<Candidate> lista = new ArrayList<>();
        this.repoCandidates.findAll().forEach(lista::add);

        ArrayList<Option> opt = new ArrayList<>();
        this.repoOptions.findAll().forEach(opt::add);


        ArrayList<Integer> counts = new ArrayList();
        for (int i = 1; i <= 15; i++)
            counts.add(Integer.MIN_VALUE);

        ArrayList<Candidate> candidates = new ArrayList<>();
        for (int i = 0; i < 15; i++)
            candidates.add(null);

        for (Candidate c : lista) {
            Integer count = 0;

            for (Option o : opt)
                if (o.getIdCandidate() == c.getId())
                    count++;

            if (!counts.contains(Integer.MIN_VALUE)) {
                Integer pos = getPositionOfMinElement(counts);

                if (counts.get(pos) < count) {
                    counts.set(pos, count);
                    candidates.set(pos, c);
                }

            } else

                for (int i = 0; i < 15; i++) {


                    if (counts.get(i).equals(Integer.MIN_VALUE)) {
                        counts.set(i, count);
                        candidates.set(i, c);
                        break;
                    }

                }

        }
        for (int i = 0; i < 14; i++)
            for (int j = i + 1; j <= 14; j++) {
                if (counts.get(i) < counts.get(j)) {
                    int aux = counts.get(i);
                    counts.set(i, counts.get(j));
                    counts.set(j, aux);

                    Candidate caux = candidates.get(i);
                    candidates.set(i, candidates.get(j));
                    candidates.set(j, caux);
                }
            }

        ArrayList<Object> f = new ArrayList<>();


        for (int i = 0; i < counts.size(); i++) {
            if (counts.get(i) != Integer.MIN_VALUE) {
                f.add(counts.get(i));
                f.add(candidates.get(i));
            }
        }

        return f;

    }


    /**
     * Get a list with top 15 candidates with least choices
     * @return array
     */
    public ArrayList<Object> findTop15CandidatesLeast() {
        ArrayList<Candidate> lista = new ArrayList<>();
        this.repoCandidates.findAll().forEach(lista::add);

        ArrayList<Option> opt = new ArrayList<>();
        this.repoOptions.findAll().forEach(opt::add);


        ArrayList<Integer> counts = new ArrayList();
        for (int i = 1; i <= 15; i++)
            counts.add(Integer.MAX_VALUE);

        ArrayList<Candidate> candidates = new ArrayList<>();
        for (int i = 0; i < 15; i++)
            candidates.add(null);


        for (Candidate c : lista) {
            Integer count = 0;

            for (Option o : opt)
                if (o.getIdCandidate() == c.getId())
                    count++;

            if (!counts.contains(Integer.MAX_VALUE)) {
                Integer pos = getPositionOfMaxElement(counts);

                if (counts.get(pos) > count) {
                    counts.set(pos, count);
                    candidates.set(pos, c);
                }

            } else

                for (int i = 0; i < 15; i++) {


                    if (counts.get(i).equals(Integer.MAX_VALUE)) {
                        counts.set(i, count);
                        candidates.set(i, c);
                        break;
                    }

                }

        }

        for (int i = 0; i < 14; i++)
            for (int j = i + 1; j <= 14; j++) {
                if (counts.get(i) > counts.get(j)) {
                    int aux = counts.get(i);
                    counts.set(i, counts.get(j));
                    counts.set(j, aux);

                    Candidate caux = candidates.get(i);
                    candidates.set(i, candidates.get(j));
                    candidates.set(j, caux);
                }
            }

        ArrayList<Object> f = new ArrayList<>();


        for (int i = 0; i < counts.size(); i++) {
            if (counts.get(i) != Integer.MAX_VALUE) {
                f.add(counts.get(i));
                f.add(candidates.get(i));
            }
        }
        return f;

    }

    /**
     * Get position of maximum element in an array.
     * @param l array
     * @return integer
     */
    public Integer getPositionOfMaxElement(ArrayList<Integer> l) {
        int limit = l.size();
        int max = Integer.MIN_VALUE;
        Integer maxPos = -1;
        for (int i = 0; i < limit; i++) {
            int value = l.get(i);
            if (value > max) {
                max = value;
                maxPos = i;
            }
        }
        return maxPos;
    }

    /**
     * Get position of minimum element in an array.
     * @param l array
     * @return int
     */
    public Integer getPositionOfMinElement(ArrayList<Integer> l) {
        int limit = l.size();
        int max = Integer.MIN_VALUE;
        Integer maxPos = -1;
        for (int i = 0; i < limit; i++) {
            int value = l.get(i);
            if (value < max) {
                max = value;
                maxPos = i;
            }
        }
        return maxPos;
    }

    /**
     * Add observer
     * @param o the observer
     */
    @Override
    public void addObserver(Observer<Candidate> o) {
        observersList.add(o);
    }

    /**
     * Remove observer
     * @param o the observer
     */
    @Override
    public void removeObserver(Observer<Candidate> o) {
        observersList.remove(o);
    }

    /**
     * Notify observers
     * @param event event
     */
    @Override
    public void notifyObservers(ListEvent<Candidate> event) {
        observersList.forEach(x -> x.notifyEvent(event));
    }

    /**
     * Create an event
     * @param type listEventType
     * @param elem E
     * @param l iterable
     * @param <E> generic
     * @return ListEvent
     */
    private <E> ListEvent<E> createEvent(ListEventType type, final E elem, final Iterable<E> l) {
        return new ListEvent<E>(type) {
            @Override
            public Iterable<E> getList() {
                return l;
            }

            @Override
            public E getElement() {
                return elem;
            }
        };
    }
}

