package objects;

public class SpecificAssessmentCriteria {
    Headline headLine;
    Criteria[] criteria;
    
    private class Headline {
        String criteriaHeadlineString;
        String conditionLowScoreString;
        String conditionMiddleScoreString;
        String conditionHighestScoreString;
    }
    
    private class Criteria {
        String criteriaString;
        Condition[] condition;
    }

    private class Condition {
        String condition;
        Integer score;
    }
    
}
