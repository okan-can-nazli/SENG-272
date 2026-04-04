package model;


 // keep user profile info that collected in Step 1

 
public class Profile {
    private String username;
    private String school;
    private String sessionName;

    public Profile() {}

    public String getUsername(){
        return username;
    }
    
    public void setUsername(String u){ 
        this.username = u;
    }

    public String getSchool(){
        return school;
    }


    public void setSchool(String s){
        this.school = s;
    }

    public String getSessionName(){
        return sessionName;
    
    }

    public void setSessionName(String sn){
        this.sessionName = sn;
    }
}
