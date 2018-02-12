package Repository;

import Domain.Action;
import Domain.Note;
import Utils.Database;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

public class GradesDatabaseRepository extends AbstractRepository<Integer, Note> {
    public GradesDatabaseRepository()
    {
        this.loadData();
    }

    @Override
    public void save(Note entity) {
        try (PreparedStatement cmd = Database.getConnection().prepareStatement("INSERT INTO Note(StudentId, HomeworkId, Note, Week, Observations, [Action]) VALUES (?, ?, ?, ?, ?, ?)"))
        {
            cmd.setInt(1, entity.getStudentID());
            cmd.setInt(2, entity.getHomeworkID());
            cmd.setInt(3, entity.getValue());
            cmd.setInt(4, entity.getSapt_predare());
            cmd.setString(5, entity.getObservatii());
            cmd.setString(6, entity.getAction() == Action.ADD ? "ADD" : "MODIFY");
            cmd.executeUpdate();
            entity.setId(Note.getID(entity.getStudentID(), entity.getHomeworkID()));
        }
        catch (SQLException ignored) {
            System.out.println(ignored.getMessage());
        }
        super.save(entity);
    }

    @Override
    public void loadData() {
        try (PreparedStatement cmd = Database.getConnection().prepareStatement("SELECT N.*, H.Deadline FROM Note N INNER JOIN Homework H ON N.HomeworkId = H.ID");
             ResultSet result = cmd.executeQuery())
        {
            while (result.next())
                super.save(new Note(result.getInt("StudentId"), result.getInt("HomeworkId"),
                        result.getInt("Note"), result.getInt("Deadline"), result.getInt("Week"), result.getString("Observations"),
                        result.getString("Action").equals("ADD") ? Action.ADD : Action.MODIFY));
        }
        catch (SQLException ignored) {
        }
    }

    public Optional<Note> delete(Integer studentID, Integer homeworkID) {
        try (PreparedStatement cmd = Database.getConnection().prepareStatement("DELETE FROM Note WHERE StudentId = ? AND HomeworkId = ?"))
        {
            cmd.setInt(1, studentID);
            cmd.setInt(2, homeworkID);
            cmd.executeUpdate();

        } catch (SQLException ignored) {
        }
        return super.delete(Note.getID(studentID, homeworkID));
    }

    public Optional<Note> update(Note entity) {
        try (PreparedStatement cmd = Database.getConnection().prepareStatement("UPDATE Note SET Note = ?, Week = ?, Observations = ?, [Action] = ? WHERE StudentId = ? AND HomeworkId = ?"))
        {
            cmd.setInt(1, entity.getValue());
            cmd.setInt(2, entity.getSapt_predare());
            cmd.setString(3, entity.getObservatii());
            cmd.setString(4, entity.getAction() == Action.ADD ? "ADD" : "MODIFY");
            cmd.setInt(5, entity.getStudentID());
            cmd.setInt(6, entity.getHomeworkID());
            cmd.executeUpdate();

        } catch (SQLException ignored) {
        }
        return super.update(entity.getId(), entity);
    }
}