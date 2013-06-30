package models;

import com.avaje.ebean.annotation.CreatedTimestamp;
import play.db.ebean.Model;

import javax.persistence.*;
import java.util.Date;

@Entity
public class Result extends Model {

    @Id
    public Long result_id;

    @ManyToOne
    @JoinColumn(name="school_id")
    public School school;

    public int school_year;

    public int level;

    public String group_name;

    public int answer;

    @CreatedTimestamp
    public Date createDate;

    @Version
    public Date updateDate;

}
