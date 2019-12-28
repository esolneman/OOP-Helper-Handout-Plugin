package controller;

import com.intellij.openapi.application.ApplicationManager;
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
    SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");

    public static QuestionnaireController getInstance() {
        if (single_instance == null) {
            single_instance = new QuestionnaireController();
        }
        return single_instance;
    }

    private QuestionnaireController() {
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
        //Getting current date
        Calendar cal = Calendar.getInstance();
        //https://stackoverflow.com/a/1053475
        try (PrintWriter out = new PrintWriter(projectCreationDateFile)) {
            out.println(dateFormat.format(cal.getTime()));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void compareDates() {
        System.out.println("Date compareDatesg");

        String lastSavedDateString = "";
        //Getting current date
        Calendar lastSavedDateCal = Calendar.getInstance();
        Calendar currentDateCalender = Calendar.getInstance();
        Calendar dateForQuestionnaireCal;
        Date currentDate = currentDateCalender.getTime();
        try {
            //https://www.geeksforgeeks.org/different-ways-reading-text-file-java/
            lastSavedDateString = new String(Files.readAllBytes(Paths.get(projectCreationDateFile.getAbsolutePath())));
            System.out.println("Date lastSavedDateString: " + lastSavedDateString);

            //https://beginnersbook.com/2017/10/java-add-days-to-date/
            try {
                lastSavedDateCal.setTime(dateFormat.parse(lastSavedDateString));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            Date lastSavedDate = dateFormat.parse(lastSavedDateString);

            //TODO MAKE CONSTANT
            dateForQuestionnaireCal = lastSavedDateCal;
            dateForQuestionnaireCal.add(Calendar.DAY_OF_MONTH, 3);
            Date dateForQuestionnaireString = lastSavedDateCal.getTime();
            System.out.println("Date Incremented by three: " + dateForQuestionnaireString);
            System.out.println("Date currentDate: " + currentDate);
            System.out.println("Date lastSavedDate: " + lastSavedDate);

            //https://www.mkyong.com/java/how-to-compare-dates-in-java/
            if (lastSavedDate.after(dateForQuestionnaireString) || lastSavedDate.equals(dateForQuestionnaireString)) {
                System.out.println("Date1 is after Date2");
                ApplicationManager.getApplication().invokeLater(() -> {
                    QuestionnaireDialog.main(null);
                });
                saveDateInFile(dateFormat.format(currentDate));
            }

            //TODO Not After Abgabetermin!!!

        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }

    }

    private void saveDateInFile(String date) {
        //https://stackoverflow.com/a/1053475
        try (PrintWriter out = new PrintWriter(projectCreationDateFile)) {
            out.println(date);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
