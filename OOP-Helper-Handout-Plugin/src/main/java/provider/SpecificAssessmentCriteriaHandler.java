package provider;

import objects.SpecificAssessmentCriteria;

import java.io.File;
import java.util.ArrayList;

public class SpecificAssessmentCriteriaHandler {

    private static SpecificAssessmentCriteria specificAssessmentCriteria;

    public static SpecificAssessmentCriteria getSpecificAssessmentCriteria() {
        specificAssessmentCriteria = null;
        File file = RepoLocalStorageDataProvider.getSpecificAssessmentCriteriaFile();
        String[] headline = new String[] {"Kriterium", "test", "test1", "test2"};
        specificAssessmentCriteria = new SpecificAssessmentCriteria(headline);
        System.out.println(file.getName());
        return specificAssessmentCriteria;
    }

}
