package sample.service;

import sample.domain.Cont;
import sample.repository.ContFileRepository;
import sample.repository.ContRepository;
import sample.repository.Repository;
import sample.validator.ContValidator;
import sample.validator.Validator;

import java.util.Vector;

public class ContService extends AbstractService<String, Cont, ContRepository, ContValidator> {
    private Vector<Cont> conturiNoi;        // conturi ce tocmai au fost create - UI Controller ar trebui să verifice dacă contul e nou și să-i afișeze un mesaj utilizatorului
    private Vector<Cont> conturiNoiDefault; // conturi ce au fost create cu parola default - UI Controller ar trebui să verifice și să roage utilizatorul să-și schimbe parola

    public ContService() {
        super(new ContRepository(), new ContValidator());
        conturiNoi = new Vector<>();
        conturiNoiDefault = new Vector<>();
    }

    public ContService(String fileName, String xmlName, String path) {
        super(new ContFileRepository(fileName, xmlName, path), new ContValidator());
        conturiNoi = new Vector<>();
        conturiNoiDefault = new Vector<>();
    }

    public ContService(Repository<String, Cont> repository, Validator<Cont> validator) {
        super(repository, validator);
        conturiNoi = new Vector<>();
        conturiNoiDefault = new Vector<>();
    }

    @Override
    public Cont add(Cont cont) throws Exception {
        cont = super.add(cont);
        conturiNoi.add(cont);
        return cont;
    }

    public Cont addDefault(Cont cont) throws Exception {
        cont = super.add(cont);
        conturiNoi.add(cont);
        conturiNoiDefault.add(cont);
        return cont;
    }

    /**
     * @param cnp - ID-ul candidatului
     * @return true dacă contul tocmai a fost creat și false în caz contrar
     * Această informație (cont nou) se păstrează până la închiderea aplicației !
     */
    public boolean contNou(String cnp) {
        for (Cont cont : conturiNoi)
            if (cont.getID().equals(cnp))
                return true;
        return false;
    }

    /**
     * @param cnp - ID-ul candidatului
     * @return true dacă contul tocmai a fost creat cu parola default și false în caz contrar
     * Această informație (cont nou default) se păstrează până primul apel al funcției pe contul existent cu id-ul dat !
     */
    public boolean contNouDefault(String cnp) {
        for (Cont cont : conturiNoiDefault)
            if (cont.getID().equals(cnp)) {
                conturiNoiDefault.remove(cont);
                return true;
            }
        return false;
    }

}
