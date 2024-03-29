package models;

import com.avaje.ebean.annotation.CreatedTimestamp;
import play.db.ebean.Model;

import javax.persistence.*;
import java.util.Date;

@Entity
public class RoomQueue extends Model {

    @Id
    public long entry_id;

    @ManyToOne
    @JoinColumn(name="school_id")
    public School school;

    public int school_year;

    public String group_name;

    public int level;

    public int room_number;

    @CreatedTimestamp
    public Date createDate;

    @Version
    public Date updateDate;
}
