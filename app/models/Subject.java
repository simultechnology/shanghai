package models;

import play.db.ebean.Model;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Subject extends Model {

    @Id
    public String subject_code;

    public String subject_name;
}
