package cz.cvut.fel.stepaego.JPA;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

//Pilot to Flight Relation ID
@Embeddable
public class pfId implements Serializable {

    @Column(name = "pilot_pilotId")
    private int pilotPilotId;

    @Column(name = "flights_flightId")
    private int flightsFlightId;

    public int getPilotPilotId() {
        return pilotPilotId;
    }

    public void setPilotPilotId(int pilotPilotId) {
        this.pilotPilotId = pilotPilotId;
    }

    public int getFlightsFlightId() {
        return flightsFlightId;
    }

    public void setFlightsFlightId(int flightsFlightId) {
        this.flightsFlightId = flightsFlightId;
    }
}
