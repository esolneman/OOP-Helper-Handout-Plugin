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
    private SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
    private static Integer DAYS_TILL_QUESTIONNAIRE = 3;

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
        FileHandleController.createNewFile(projectCreationDateFile);
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
        String lastSavedDateString = "";
        //Getting current date
        Calendar lastSavedDateCal = Calendar.getInstance();
        Calendar currentDateCalender = Calendar.getInstance();
        Calendar dateForQuestionnaireCal;
        Date currentDate = currentDateCalender.getTime();
        try {
            //https://www.geeksforgeeks.org/different-ways-reading-text-file-java/
            lastSavedDateString = new String(Files.readAllBytes(Paths.get(projectCreationDateFile.getAbsolutePath())));

            //https://beginnersbook.com/2017/10/java-add-days-to-date/
            try {
                lastSavedDateCal.setTime(dateFormat.parse(lastSavedDateString));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            Date lastSavedDate = dateFormat.parse(lastSavedDateString);

            dateForQuestionnaireCal = lastSavedDateCal;
            dateForQuestionnaireCal.add(Calendar.DAY_OF_MONTH, DAYS_TILL_QUESTIONNAIRE);
            Date dateForQuestionnaire = lastSavedDateCal.getTime();
            String dateForQuestionnaireString = dateFormat.format(dateForQuestionnaire);
            Date dateForQuestionnaireShortVersion = dateFormat.parse(dateForQuestionnaireString);

            //https://www.mkyong.com/java/how-to-compare-dates-in-java/
            if (currentDate.after(dateForQuestionnaireShortVersion) || currentDate.equals(dateForQuestionnaireShortVersion)) {
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
