package model; // classify the file folder to provide a basic-seperated strucuture 

//this file basically carry data of a obj to other systems


public class AppSession {
    private Profile profile;
    private String qualityType;

    private String mode;
    private Scenario selectedScenario;

    public AppSession() {
        this.profile = new Profile();
    }

    //reset all selections for a new session 
    public void reset() {
        this.profile = new Profile();
        this.qualityType = null;
        this.mode = null;

        this.selectedScenario = null;

    }


    //getters,setters
    public Profile getProfile(){
        return profile; }

    public void setProfile(Profile p){
        this.profile = p; }

    public String getQualityType(){
        return qualityType; }

    public void setQualityType(String qt){
        this.qualityType = qt; }

    public String getMode(){ 
        return mode; }

    public void setMode(String mode){
        this.mode = mode; }

    public Scenario getSelectedScenario(){
        return selectedScenario; }

    public void setSelectedScenario(Scenario scenario){
        this.selectedScenario = scenario; }
}
