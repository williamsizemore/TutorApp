package project.cse3310;


import android.widget.RatingBar;

public class Rating {
    private float rating;
    private String message, tutorName;

    public Rating(){

    }
    public Rating(float rating, String message, String tutorName) {
        this.rating = rating;
        this.message = message;
        this.tutorName = tutorName;
    }

    public float getRating() {
        return rating;
    }
    public void setRating(float rating) {
        this.rating = rating;
    }
    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
    }
    public String getTutorName() {
        return tutorName;
    }
    public void setTutorName(String tutorName) {
        this.tutorName = tutorName;
    }

    @Override
    public String toString() {
        return "Rating{" +
                "rating=" + rating +
                ", message='" + message + '\'' +
                ", tutorName='" + tutorName + '\'' +
                '}';
    }
}
