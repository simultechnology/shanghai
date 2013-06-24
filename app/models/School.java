package models;

import play.db.ebean.Model;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class School extends Model {

    @Id
    public long id;

    public String name;
}
