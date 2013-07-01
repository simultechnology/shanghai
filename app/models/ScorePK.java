package models;

import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
public class ScorePK implements Serializable {

    public Long result_id;
    public String subject_code;

    public boolean equals(Object obj) {

        if (obj instanceof ScorePK) {
            ScorePK pk = (ScorePK) obj;
            if (result_id == pk.result_id && subject_code.equals(pk.subject_code)) {
                return true;
            }
        }
        return false;
    }

    public int hashCode () {
        return (int) (result_id + subject_code.hashCode());
    }

}
