package models;

import play.db.ebean.Model;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class School extends Model {

    @Id
    public long school_id;

    public String school_name;
}
