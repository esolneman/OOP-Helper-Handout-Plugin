package provider;

import objects.SpecificAssessmentCriteria;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Arrays;

public class SpecificAssessmentCriteriaHandler {

    private static SpecificAssessmentCriteria specificAssessmentCriteria;
    private static File file;
    private static String[] headline;

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
            System.out.println(content);
            headline = new String[] {"Kriterium", "test", "test1", "test2"};
            Document document = Jsoup.parse(content);
            System.out.println("document: " + document.body().html());

            String[] headers = document.select("table tr th")
                    .stream()
                    .map(Element::text)
                    .toArray(String[]::new);
            //headline = content.
            System.out.println("ths: " + Arrays.toString(headers));
            specificAssessmentCriteria = new SpecificAssessmentCriteria(headers);
            String[][] data = document.select("table.tablehead tr:gt(1)")
                    .stream()
                    .map(row -> row.select("td")
                            .stream()
                            .map(Element::text)
                            .toArray(String[]::new)
                    ).toArray(String[][]::new);

            System.out.println("----");
            for (String[] row : data){
                System.out.println("row: " + Arrays.toString(row));
            }

            Element table = document.getElementsByTag("table").get(0);
            System.out.println("table: " + table.nodeName());



        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
