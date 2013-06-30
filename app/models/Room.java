package models;

import play.db.ebean.Model;

import javax.persistence.*;

@Entity
public class Room extends Model {

    @Id
    public long room_number;

    public boolean status;

    @Column(unique=true)
    @JoinColumn(name="entry_id")
    public long entry_id;

}
