package cz.cvut.fel.stepaego.JPA;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

//Flight to Pilot Relation ID
@Embeddable
public class fpId implements Serializable {

    @Column(name = "flight_flightId")
    private int flightFlightId;

    @Column(name = "pilots_pilotId")
    private int pilotsPilotId;

    public int getFlightFlightId() {
        return flightFlightId;
    }

    public void setFlightFlightId(int flightFlightId) {
        this.flightFlightId = flightFlightId;
    }

    public int getPilotsPilotId() {
        return pilotsPilotId;
    }

    public void setPilotsPilotId(int pilotsPilotId) {
        this.pilotsPilotId = pilotsPilotId;
    }
}
