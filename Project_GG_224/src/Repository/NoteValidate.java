package Repository;

import Domain.Note;

public class NoteValidate implements Validator<Note> {
    @Override
    public boolean validate(Note entity) throws ValidationException {
        if (entity.getNrTema() > 0 && entity.getIdStudent() > 0 && entity.getValoare() > 0 && entity.getValoare()<11)
            return true;

        return false;
    }
}
