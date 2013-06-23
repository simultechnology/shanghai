package models;

import play.db.ebean.Model;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Room extends Model {

    @Id
    public int room_number;

    public boolean status;


}
