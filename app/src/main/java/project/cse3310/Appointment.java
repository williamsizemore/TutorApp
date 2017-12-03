package project.cse3310;


import android.os.Parcel;
import android.os.Parcelable;

public class Appointment implements Parcelable {
private String studentUID;
private String tutorName;
private String Day;
private String hour;
private String status;
private String apptID;
    @Override
    public String toString() {
        return "Appointment{" +
                "studentUID='" + studentUID + '\'' +
                ", tutorName='" + tutorName + '\'' +
                ", Day='" + Day + '\'' +
                ", hour='" + hour + '\'' +
                ", status='" + status + '\'' +
                '}';
    }

    public Appointment(){
    }
    public Appointment(String studentUID, String tutorName, String day, String hour, String status) {
        this.studentUID = studentUID;
        this.tutorName = tutorName;
        Day = day;
        this.hour = hour;
        this.status = status;
    }
    public Appointment(String studentUID, String tutorName, String day, String hour, String status, String apptID){
        this.studentUID = studentUID;
        this.tutorName = tutorName;
        Day = day;
        this.hour = hour;
        this.status = status;
        this.apptID = apptID;
    }
    protected Appointment(Parcel in) {
        studentUID = in.readString();
        tutorName = in.readString();
        Day = in.readString();
        hour = in.readString();
        status = in.readString();
    }

    public static final Creator<Appointment> CREATOR = new Creator<Appointment>() {
        @Override
        public Appointment createFromParcel(Parcel in) {
            return new Appointment(in);
        }

        @Override
        public Appointment[] newArray(int size) {
            return new Appointment[size];
        }
    };

    public String getStudentUID() {
        return studentUID;
    }
    public void setStudentUID(String studentUID) {
        this.studentUID = studentUID;
    }
    public String getTutorName() {
        return tutorName;
    }
    public void setTutorName(String tutorName) {
        this.tutorName = tutorName;
    }
    public String getDay() {
        return Day;
    }
    public void setDay(String day) {
        Day = day;
    }
    public String getHour() {
        return hour;
    }
    public void setHour(String hour) {
        this.hour = hour;
    }
    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }
    public String getApptID() {
        return apptID;
    }
    public void setApptID(String apptID) {
        this.apptID = apptID;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(studentUID);
        parcel.writeString(tutorName);
        parcel.writeString(Day);
        parcel.writeString(hour);
        parcel.writeString(status);
    }
}
