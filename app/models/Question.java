package models;

import play.db.ebean.Model;
import com.avaje.ebean.annotation.CreatedTimestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Version;
import java.util.Date;

@Entity
public class Question extends Model {

    @Id
    public Long id;

    public int school_year;

    public int level;

    public String subject;

    @Column(columnDefinition = "TEXT")
    public String content;

    @Column(columnDefinition = "TEXT")
    public String choice1;

    @Column(columnDefinition = "TEXT")
    public String choice2;

    @Column(columnDefinition = "TEXT")
    public String choice3;

    @Column(columnDefinition = "TEXT")
    public String choice4;

    public int answer;

    @CreatedTimestamp
    public Date createDate;

    @Version
    public Date updateDate;
}
