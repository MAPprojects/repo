package Service;

import Domain.LabHomework;
import Repository.AbstractRepository;
import Validator.ValidatorException;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.IsoFields;
import java.util.Date;
import java.util.Calendar;

public class LabHomeworkService extends AbstractService<Integer, LabHomework> {


    private static int currentWeek= Calendar.WEEK_OF_YEAR;


    public LabHomeworkService(AbstractRepository<Integer, LabHomework> lhr) {
        super(lhr);
    }

    public static void setCurrentWeek(int currentWeek) {
        LabHomeworkService.currentWeek = currentWeek;
    }

    public void addHomework(int id, int deadline, String description) throws ValidatorException {
        LabHomework lh=new LabHomework(id,deadline,description);
        absRepo.save(lh);
    }

    public void deleteHomework(int id)
    {
        absRepo.delete(id);
    }

    public void updateHomework(int id,int deadline,String description) throws ValidatorException {
        if (deadline<=currentWeek)
            throw new ValidatorException("New deadline must be greater than the current week.");
        if(super.findOne(id).getDeadline()<=currentWeek)
            throw new ValidatorException("The deadline for this homework cannot be changed");
        LabHomework lh=new LabHomework(id,deadline,description);
        absRepo.update(id,lh);

    }
}
