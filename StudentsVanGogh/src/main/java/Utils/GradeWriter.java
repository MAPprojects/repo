package Utils;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class GradeWriter {

    private String filename, event;
    private int IDasg;
    private int value;
    private int deadline;
    private long currentWeek;

    private static GradeWriter instance = new GradeWriter();

    private GradeWriter() {};

    public void setWriter(String filename, int IDasg, int value, int deadline, long currentWeek) {
        this.filename = filename;
        this.IDasg = IDasg;
        this.value = value;
        this.deadline = deadline;
        this.currentWeek = currentWeek;
    }

    public void setEvent(String event) {
        this.event = event;
    }

    public static GradeWriter getInstance() {
        return instance;
    }

    public void write(String notes) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename,true))) {
            writer.write(event + "," + IDasg +"," + value +"," + deadline + "," + currentWeek +"," + notes+"\n");
        }catch (IOException e){
            System.out.println(e.getMessage());
        }
    }
}
