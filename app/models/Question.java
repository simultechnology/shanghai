package models;

import play.db.ebean.Model;
import com.avaje.ebean.annotation.CreatedTimestamp;

import javax.persistence.*;
import java.util.Date;

@Entity
public class Question extends Model {

    @Id
    public Long question_id;

    public int school_year;

    public int level;

    @ManyToOne
    @JoinColumn(name="subject_code")
    public Subject subject;

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

    public int answer_type;

    @CreatedTimestamp
    public Date createDate;

    @Version
    public Date updateDate;
}
