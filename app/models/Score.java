package models;

import com.avaje.ebean.annotation.CreatedTimestamp;
import play.db.ebean.Model;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Version;
import java.util.Date;

@Entity
public class Score extends Model {

    @EmbeddedId
    public ScorePK scorePK;

    public int correct_number;

    public int mistake_number;

    public int time_over_number;

    public int total_number;

    public long time;

    @CreatedTimestamp
    public Date createDate;

    @Version
    public Date updateDate;
}
