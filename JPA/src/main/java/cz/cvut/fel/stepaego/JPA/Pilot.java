package cz.cvut.fel.stepaego.JPA;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import java.util.HashSet;
import java.util.Set;

//Pilot Entity
@Entity
public class Pilot {

    @Id
    private int pilotId;
    private int idNumber;
    private String citizenship;
    private String name;
    private String surname;
    private String email;
    private int pilotLicenseNumber;

    @ManyToMany
    private Set<Flight> flights = new HashSet<Flight>();

    public Set<Flight> getFlights() {
        return flights;
    }

    public void setFlights(Set<Flight> flights) {
        this.flights = flights;
    }

    public int getPilotId() {
        return pilotId;
    }

    public void setPilotId(int pilotId) {
        this.pilotId = pilotId;
    }

    public int getIdNumber() {
        return idNumber;
    }

    public void setIdNumber(int idNumber) {
        this.idNumber = idNumber;
    }

    public String getCitizenship() {
        return citizenship;
    }

    public void setCitizenship(String citizenship) {
        this.citizenship = citizenship;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getPilotLicenseNumber() {
        return pilotLicenseNumber;
    }

    public void setPilotLicenseNumber(int pilotLicenseNumber) {
        this.pilotLicenseNumber = pilotLicenseNumber;
    }

    @Override
    public String toString() {
        return "Pilot id=" + pilotId + "\n";
    }
}
