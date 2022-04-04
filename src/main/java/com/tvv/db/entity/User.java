package com.tvv.db.entity;

/**
 * Class User for users in DB
 * login - user login in system
 * password - user password, hide real password with hash function
 * status - user status
 * firstName - first name
 * lastName - last name
 * dayOfBirth - date of birth
 * sex - sex
 * gender - not used, will be developed in the future
 * photo - photo file, use only file name
 * role - user role
 * email - e-mail address
 * local - current user localization
 */
public class User extends EntityID{

    private String login;

    private String password;

    private boolean status;

    private String firstName;

    private String lastName;

    private String dayOfBirth;

    private String sex;

    private String gender;

    private String photo;

    private int role;
    private String email;
    private String local;

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public int getRole() {
        return role;
    }

    public void setRole(int role) {
        this.role = role;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String getDayOfBirth() {
        return dayOfBirth;
    }

    public void setDayOfBirth(String dayOfBirth) {
        this.dayOfBirth = dayOfBirth;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getLocal() {
        return local;
    }

    public void setLocal(String local) {
        this.local = local;
    }

    @Override
    public String toString() {
        return "User{" +
                "login='" + login + '\'' +
                ", password='" + password + '\'' +
                ", status=" + status +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", dayOfBirth='" + dayOfBirth + '\'' +
                ", sex='" + sex + '\'' +
                ", gender='" + gender + '\'' +
                ", photo='" + photo + '\'' +
                ", role=" + role +
                ", email='" + email + '\'' +
                ", local='" + local + '\'' +
                '}';
    }
}