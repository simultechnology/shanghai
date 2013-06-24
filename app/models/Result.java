package models;

import com.avaje.ebean.annotation.CreatedTimestamp;
import play.db.ebean.Model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Version;
import java.util.Date;

@Entity
public class Result extends Model {

    @Id
    public Long id;

    public int school_year;

    public int level;

    public String subject;


    public int answer;

    @CreatedTimestamp
    public Date createDate;

    @Version
    public Date updateDate;

}
