package UI;

import Repository.*;
import Service.Service;
import UI.UI;
import Validator.NotaValidator;
import Validator.StudentValidator;
import Validator.TemaValidator;

public class StartApp {
    public static void main(String[] args){
        StudentValidator valid1=new StudentValidator();
        //RepositoryInMemory repoStudent=new RepositoryInMemory(valid1);
        FileRepoStudent repoStudent=new FileRepoStudent(valid1,"Studenti.txt");
        TemaValidator valid2=new TemaValidator();
        //RepositoryInMemoryTema repoTema=new RepositoryInMemoryTema(valid2);
        FileRepoTema repoTema = new FileRepoTema(valid2,"Teme.txt");
        NotaValidator valid3=new NotaValidator();
        FileRepoNote repoNota=new FileRepoNote("Note.txt",valid3);
        Service ctr=new Service(repoStudent,repoTema,repoNota);
        //UI ui=new UI(ctr);
        //ui.testUI();
        UIFinal ui=new UIFinal(ctr);
        ui.showUI();
    }
}
