package controller;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.wm.ToolWindowType;
import gui.QuestionnaireDialog;
import provider.LocalStorageDataProvider;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class QuestionnaireController {

    private static QuestionnaireController single_instance = null;
    private File projectCreationDateFile;

    public static QuestionnaireController getInstance() {
        if (single_instance == null) {
            single_instance = new QuestionnaireController();
        }
        return single_instance;
    }

    private QuestionnaireController(){
        projectCreationDateFile = LocalStorageDataProvider.getProjectCreationDateDirectory();

    }

    public void saveProjectCreationDate() {
        projectCreationDateFile.getParentFile().mkdirs();
        try {
            projectCreationDateFile.createNewFile();
        } catch (IOException e) {
            //TODO CATACH
            System.out.println(e);
        }
        System.out.println("saveProjectCreationDate run");

        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
        //Getting current date
        Calendar cal = Calendar.getInstance();
        String creationDate = sdf.format(cal.getTime());
        System.out.println("creationDate: " + creationDate);

        //TODO ONLY FOR TEST CASES RMEOVE THIS CODE!!!
        try {
            cal.setTime(sdf.parse(creationDate));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        //TODO MALE CONSTANT
        cal.add(Calendar.DAY_OF_MONTH, 3);
        Date dateForQuestionnaireString = cal.getTime();
        System.out.println("Date Incremented by three: "+ dateForQuestionnaireString);
        ApplicationManager.getApplication().invokeLater(() -> {
            QuestionnaireDialog.main(null);
        });

        //https://stackoverflow.com/a/1053475
        try (PrintWriter out = new PrintWriter(projectCreationDateFile)) {
            out.println(creationDate);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void compareDates() {
        String projectCreationDate = "";
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
        //Getting current date
        Calendar cal = Calendar.getInstance();
        Calendar currentDateCalender = Calendar.getInstance();
        Date currentDate = currentDateCalender.getTime();
        try {
            //https://www.geeksforgeeks.org/different-ways-reading-text-file-java/
            projectCreationDate = new String(Files.readAllBytes(Paths.get(projectCreationDateFile.getAbsolutePath())));

            //https://beginnersbook.com/2017/10/java-add-days-to-date/
            try {
                cal.setTime(sdf.parse(projectCreationDate));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            //TODO MALE CONSTANT
            cal.add(Calendar.DAY_OF_MONTH, 3);
            Date dateForQuestionnaireString = cal.getTime();
            System.out.println("Date Incremented by three: "+ dateForQuestionnaireString);
            //https://www.mkyong.com/java/how-to-compare-dates-in-java/
            if (dateForQuestionnaireString.after(currentDate) || dateForQuestionnaireString.equals(currentDate)) {
                System.out.println("Date1 is after Date2");
                QuestionnaireDialog questionnaireDialog = new QuestionnaireDialog();
                saveDateInFile(sdf.format(currentDateCalender.getTime()));
            }

            //TODO Not After Abgabetermin!!!

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void saveDateInFile(String date){
        //https://stackoverflow.com/a/1053475
        try (PrintWriter out = new PrintWriter(projectCreationDateFile)) {
            out.println(date);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
