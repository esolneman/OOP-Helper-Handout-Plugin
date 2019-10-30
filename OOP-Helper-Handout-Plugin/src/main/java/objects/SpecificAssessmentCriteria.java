package objects;

import java.util.ArrayList;

public class SpecificAssessmentCriteria {
    String[]  headline;
    ArrayList<String[]> criteria;


    public SpecificAssessmentCriteria(String[] headLine, ArrayList<String[]> criteria){
        setHeadline(headLine);
        setCriteria(criteria);
    }

    private void setHeadline(String[] headLine){
        this.headline = headLine;
    }

    public String[] getHeadline(){
        return this.headline;
    }

    private void setCriteria(ArrayList<String[]> criteria){
        //this.criteria = new Criteria[explanation, score];
        this.criteria = criteria;
    }

    public ArrayList<String[]> getCriteria(){
        return criteria;
    }

    
    public class Criteria {
        String[] explanation;
        String[] score;
    }

    public class Condition {
        String condition;
        Integer score;
    }
}
