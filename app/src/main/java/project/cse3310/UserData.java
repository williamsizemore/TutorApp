package project.cse3310;

import android.os.Parcel;
import android.os.Parcelable;

public class UserData implements Parcelable{

    private String email, name, userType, category, zip, state, address, phone, dateOfBirth, days, hours;
    private float rating;
    public UserData (){

    }

    /* student user - only required fields */
    UserData (String email, String name, String userType, String category, String zip, String dateOfBirth){
        this.email = email;
        this.name = name;
        this.userType = userType;
        this.category = category;
        this.zip = zip;
        this.dateOfBirth = dateOfBirth;
    }

    /* student user - optional fields */
    UserData (String email, String name, String userType, String category, String zip,
              String dateOfBirth, String address, String state, String phone){
        this.email = email;
        this.name = name;
        this.userType = userType;
        this.category = category;
        this.zip = zip;
        this.state = state;
        this.address = address;
        this.phone = phone;
        this.dateOfBirth = dateOfBirth;
    }
    /* tutor type user */
    UserData (String email, String name, String userType, String category, String zip,
              String dateOfBirth, String address, String state, String phone, String days ,String hours, float rating){
        this.email = email;
        this.name = name;
        this.userType = userType;
        this.category = category;
        this.zip = zip;
        this.dateOfBirth = dateOfBirth;
        this.address = address;
        this.state = state;
        this.phone = phone;
        this.days = days;
        this.hours = hours;
        this.rating = rating;
    }

    protected UserData(Parcel in) {
        email = in.readString();
        name = in.readString();
        userType = in.readString();
        category = in.readString();
        zip = in.readString();
        state = in.readString();
        address = in.readString();
        phone = in.readString();
        dateOfBirth = in.readString();
        days = in.readString();
        hours = in.readString();
        rating = in.readFloat();
    }

    public static final Creator<UserData> CREATOR = new Creator<UserData>() {
        @Override
        public UserData createFromParcel(Parcel in) {
            return new UserData(in);
        }

        @Override
        public UserData[] newArray(int size) {
            return new UserData[size];
        }
    };

    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getUserType() {
        return userType;
    }
    public void setUserType(String userType) {
        this.userType = userType;
    }
    public String getCategory() {
        return category;
    }
    public void setCategory(String category) {
        this.category = category;
    }
    public String getZip() {
        return zip;
    }
    public void setZip(String zip) {
        this.zip = zip;
    }
    public String getState() {
        return state;
    }
    public void setState(String state) {
        this.state = state;
    }
    public String getAddress() {
        return address;
    }
    public void setAddress(String address) {
        this.address = address;
    }
    public String getPhone() {
        return phone;
    }
    public void setPhone(String phone) {
        this.phone = phone;
    }
    public String getDateOfBirth() {
        return dateOfBirth;
    }
    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }
    public String getDays() {
        return days;
    }
    public void setDays(String days) {
        this.days = days;
    }
    public String getHours() {
        return hours;
    }
    public void setHours(String hours) {
        this.hours = hours;
    }
    public float getRating() {
        return rating;
    }
    public void setRating(float rating) {
        this.rating = rating;
    }

    @Override
    public String toString() {
        return "UserData{" +
                "email='" + email + '\'' +
                ", name='" + name + '\'' +
                ", userType='" + userType + '\'' +
                ", category='" + category + '\'' +
                ", zip='" + zip + '\'' +
                ", state='" + state + '\'' +
                ", address='" + address + '\'' +
                ", phone='" + phone + '\'' +
                ", dateOfBirth='" + dateOfBirth + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(email);
        parcel.writeString(name);
        parcel.writeString(userType);
        parcel.writeString(category);
        parcel.writeString(zip);
        parcel.writeString(state);
        parcel.writeString(address);
        parcel.writeString(phone);
        parcel.writeString(dateOfBirth);
        parcel.writeString(days);
        parcel.writeString(hours);
        parcel.writeFloat(rating);
    }
}
