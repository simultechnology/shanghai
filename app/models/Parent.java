package models;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.*;

import play.db.ebean.Model;

import com.avaje.ebean.annotation.CreatedTimestamp;
import com.avaje.ebean.validation.NotNull;

@Entity
public class Parent extends Model {

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "parent")
    public List<Child> children = new ArrayList<Child>();

    @Id
    public Long id;

    @NotNull
    public String name;

    @CreatedTimestamp
    public Date createDate;

    @Version
    public Date updateDate;

    public String toString() {
        return "Parent [id=" + id + ", name=" + name + ", createDate="
                + createDate + ", updateDate=" + updateDate + "]";
    }
}
