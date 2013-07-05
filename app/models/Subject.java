package models;

import com.avaje.ebean.annotation.CreatedTimestamp;
import play.db.ebean.Model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Version;
import java.util.Date;

@Entity
public class Subject extends Model {

    @Id
    public String subject_code;

    public String subject_name;

    @CreatedTimestamp
    public Date createDate;

    @Version
    public Date updateDate;
}
