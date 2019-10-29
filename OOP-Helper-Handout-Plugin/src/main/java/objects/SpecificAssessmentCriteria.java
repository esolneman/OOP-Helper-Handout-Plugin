package objects;

import com.intellij.openapi.vcs.history.VcsRevisionNumber;

import java.util.ArrayList;

public class SpecificAssessmentCriteria {
    String[]  headline;
   // Criteria criteria;

    public SpecificAssessmentCriteria(String[] headLine){
        setHeadline(headLine);
    }

    private void setHeadline(String[] headLine){
        this.headline = headLine;
    }

    public String[] getHeadline(){
        return this.headline;
    }

    private void setCriteria(String explanation, String condition, Integer score){

    }

    public void getCriteria(){

    }

    private static class Headline {
        String criteriaHeadlineString;
        String conditionLowScoreString;
        String conditionMiddleScoreString;
        String conditionHighestScoreString;

        public Headline(String test, String test1, String test2, String test3) {
        }
    }
    
    private class Criteria {
        String explanation;
        Condition[] conditions;
    }

    private class Condition {
        String condition;
        Integer score;
    }
    
}
