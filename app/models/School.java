package models;

import com.avaje.ebean.annotation.CreatedTimestamp;
import play.db.ebean.Model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Version;
import java.util.Date;

@Entity
public class School extends Model {

    @Id
    public long school_id;

    public String school_name;

    @CreatedTimestamp
    public Date createDate;

    @Version
    public Date updateDate;
}
