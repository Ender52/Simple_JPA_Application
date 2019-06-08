package cz.cvut.fel.stepaego.JPA;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;

//Flight to Pilot Relation
@Entity
public class Flight_pilot {

    @EmbeddedId
    private fpId fpId;

    public cz.cvut.fel.stepaego.JPA.fpId getFpId() {
        return fpId;
    }

    public void setFpId(cz.cvut.fel.stepaego.JPA.fpId fpId) {
        this.fpId = fpId;
    }
}
