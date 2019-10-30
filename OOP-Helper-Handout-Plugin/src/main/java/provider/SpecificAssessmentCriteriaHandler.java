package provider;

import objects.SpecificAssessmentCriteria;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;

public class SpecificAssessmentCriteriaHandler {

    private static SpecificAssessmentCriteria specificAssessmentCriteria;
    private static File file;

    public static SpecificAssessmentCriteria getSpecificAssessmentCriteria() {
        specificAssessmentCriteria = null;
        file = RepoLocalStorageDataProvider.getSpecificAssessmentCriteriaFile();
        parseFileToSpecificAssessmentCriteriaObject();

        System.out.println(file.getName());
        return specificAssessmentCriteria;
    }

    private static void parseFileToSpecificAssessmentCriteriaObject() {
        try {
            String content = Files.readString(file.toPath(), StandardCharsets.UTF_8);
            Document document = Jsoup.parse(content);
            ArrayList<String[][]> criteria = new ArrayList<>();

            String[] headers = document.select("table tr th")
                    .stream()
                    .map(Element::text)
                    .toArray(String[]::new);
            System.out.println("ths: " + Arrays.toString(headers));


            String[][] data = document.select("table tr")
                    .stream()
                    .map(row -> row.select("td")
                            .stream()
                            .map(Element::text)
                            .toArray(String[]::new)
                    ).toArray(String[][]::new);
            for (int i = 0; i < data.length; i++) {
                String[] row = data[i];
                System.out.println("row: " + Arrays.toString(row));
                String[][] criterion = new String[row.length][row.length];
                for (int j = 0; j < row.length; j++) {
                    String currentString = row[j];
                    criterion[j][0] = currentString.split("\\[(.*?)\\]")[0];
                    System.out.println("currentString: " + currentString);
                    System.out.println("score: " + currentString.split("\\[(.*?)\\]")[0]);
                    criterion[1][j] = currentString.split("\\[(.*?)\\]")[0];
                }
                criteria.add(criterion);
                System.out.println("row: " + Arrays.toString(row));
            }
            specificAssessmentCriteria = new SpecificAssessmentCriteria(headers, criteria);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
