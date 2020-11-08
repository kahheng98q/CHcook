package com.example.chcook.Domain;

public class Staff {
    String staffEmail,staffName,StaffPassword,staffStatus;
    Boolean IsAdmin;
    public Staff(){
    }
    public Staff(String staffEmail,String staffName, String StaffPassword, Boolean IsAdmin, String staffStatus){
        this.staffEmail = staffEmail;
        this.staffName = staffName;
        this.StaffPassword = StaffPassword;
        this.IsAdmin = IsAdmin;
        this.staffStatus=staffStatus;
    }

    public String getStaffEmail() {
        return staffEmail;
    }

    public void setStaffEmail(String staffEmail) {
        this.staffEmail = staffEmail;
    }

    public String getStaffName() {
        return staffName;
    }

    public void setStaffName(String staffName) {
        this.staffName = staffName;
    }

    public String getStaffPassword() {
        return StaffPassword;
    }

    public void setStaffPassword(String staffPassword) {
        StaffPassword = staffPassword;
    }

    public String getStaffStatus() {
        return staffStatus;
    }

    public void setStaffStatus(String staffStatus) {
        this.staffStatus = staffStatus;
    }

    public Boolean getAdmin() {
        return IsAdmin;
    }

    public void setAdmin(Boolean admin) {
        IsAdmin = admin;
    }
}
