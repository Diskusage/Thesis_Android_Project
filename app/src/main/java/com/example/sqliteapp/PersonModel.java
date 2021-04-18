package com.example.sqliteapp;

public class PersonModel {
    private String firstName;
    private String secondName;
    private String IDNP;
    private boolean vaccinated;
    private Vaccines type;
    private String vacc_date;

    //constructor

    public PersonModel(String firstName, String secondName, String IDNP) {
        this.firstName = firstName;
        this.secondName = secondName;
        this.IDNP = IDNP;
        this.vaccinated = false;
        this.type = Vaccines.None;
        this.vacc_date = "None";
    }

    public PersonModel(String firstName, String secondName, String IDNP, String type, String date) throws VaccineDoesntExist{
        this.firstName = firstName;
        this.secondName = secondName;
        this.IDNP = IDNP;
        this.vaccinated = true;
        this.type = sToEn(type);
        this.vacc_date = date;
    }

    public PersonModel() {
    }
    //toString method

    @Override
    public String toString() {
//        return "PersonModel{" +
//                "firstName='" + firstName + '\'' +
//                ", secondName='" + secondName + '\'' +
//                ", IDNP='" + IDNP + '\'' +
//                ", Vaccine='" + type + '\'' +
//                ", Date='" + vacc_date + '\'' +
//                '}';
        return firstName + " " + secondName + " " + type + " " + vacc_date;
    }


    public Vaccines sToEn(String s) throws VaccineDoesntExist{
        s = s.toLowerCase();
        for (Vaccines c: Vaccines.values()){
            if (c.name().toLowerCase().equals(s))
                return c;
        }
        throw new VaccineDoesntExist();
    }
    //getters/setters

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getSecondName() {
        return secondName;
    }

    public void setSecondName(String secondName) {
        this.secondName = secondName;
    }

    public String getIDNP() {
        return IDNP;
    }

    public void setIDNP(String IDNP) {
        this.IDNP = IDNP;
    }

    public boolean isVaccinated() {
        return vaccinated;
    }

    public void setVaccinated(boolean vaccinated) {
        this.vaccinated = vaccinated;
    }

    public Vaccines getType() {
        return type;
    }

    public void setType(Vaccines type) {
        this.type = type;
    }

    public String getVacc_date() {
        return vacc_date;
    }

    public void setVacc_date(String vacc_date) {
        this.vacc_date = vacc_date;
    }
}
