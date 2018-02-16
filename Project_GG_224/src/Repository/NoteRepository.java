package Repository;

import Domain.Note;

public class NoteRepository extends Repository<Note> {


    protected NoteRepository(Validator<Note> vali){
        super(vali);
    }

}
