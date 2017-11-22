package project.cse3310;

import java.util.Calendar;

public class TutoringSession {
    private Calendar date;
    private String category;

    public TutoringSession(){}

    TutoringSession(String category,Calendar date){
        this.category = category;
        this.date = date;
    }

    public Calendar getDate(){
        return date;
    }

    public String getCategory(){
        return category;
    }
}
