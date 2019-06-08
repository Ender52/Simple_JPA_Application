package cz.cvut.fel.stepaego.JPA;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;

//Pilot to Flight Relation
@Entity
public class Pilot_flight {

    @EmbeddedId
    private pfId pfId;

    public cz.cvut.fel.stepaego.JPA.pfId getPfId() {
        return pfId;
    }

    public void setPfId(cz.cvut.fel.stepaego.JPA.pfId pfId) {
        this.pfId = pfId;
    }
}
