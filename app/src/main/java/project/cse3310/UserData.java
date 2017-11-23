package project.cse3310;

/**
 * Created by William-PC on 11/22/2017.
 */

public class UserData {

    public String email, name, userType, category, zip, state, address, phone, dateOfBirth, days, hours;
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
              String dateOfBirth, String address, String state, String phone, String days ,String hours){
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
    }
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
}
