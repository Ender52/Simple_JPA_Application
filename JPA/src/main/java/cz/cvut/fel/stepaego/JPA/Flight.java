package cz.cvut.fel.stepaego.JPA;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import java.util.HashSet;
import java.util.Set;

//Flight entity
@Entity
public class Flight {

    @Id
    private int flightId;
    private String date;
    private String time;
    private String placeFrom;
    private String placeTo;
    private int amountOfPeople;

    @ManyToMany
    private Set<Pilot> pilots = new HashSet<Pilot>();

    public Set<Pilot> getPilots() {
        return pilots;
    }

    public void setPilots(Set<Pilot> pilots) {
        this.pilots = pilots;
    }

    public int getFlightId() {
        return flightId;
    }

    public void setFlightId(int flightId) {
        this.flightId = flightId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getPlaceFrom() {
        return placeFrom;
    }

    public void setPlaceFrom(String placeFrom) {
        this.placeFrom = placeFrom;
    }

    public String getPlaceTo() {
        return placeTo;
    }

    public void setPlaceTo(String placeTo) {
        this.placeTo = placeTo;
    }

    public int getAmountOfPeople() {
        return amountOfPeople;
    }

    public void setAmountOfPeople(int amountOfPeople) {
        this.amountOfPeople = amountOfPeople;
    }

    @Override
    public String toString() {
        return "Flight id=" + flightId + "\n";
    }
}
