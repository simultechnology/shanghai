package controllers;

import com.avaje.ebean.Query;
import models.Entry;
import models.RoomQueue;
import models.School;
import play.data.DynamicForm;
import play.data.Form;
import play.db.ebean.Model;
import play.mvc.Result;
import play.mvc.Controller;
import views.html.entries.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class Entries extends Controller {

    public static Result index() {

        Model.Finder<Long, Entry> finder = new Model.Finder<Long, Entry>(Long.class,
                Entry.class);

        Calendar c = new GregorianCalendar();
        Date today = null;
        try {
            today = new SimpleDateFormat("yyyy/MM/dd").parse(String.format("%d/%d/%d",
                    c.get(Calendar.YEAR),
                    c.get(Calendar.MONTH) + 1,
                    c.get(Calendar.DATE)));
        } catch (ParseException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

        List<Entry> entries = finder.where().eq("play_date", today).findList();

        return ok(index.render(entries));
    }

    public static Result add() {
        String[] params = {"school_id", "school_year", "group_name", "level",
                "room_number"};
        DynamicForm input = Form.form();
        input = input.bindFromRequest(params);
        Map<String, String> data = input.data();
        long school_id = Long.parseLong(data.get("school_id"));
        int school_year = Integer.parseInt(data.get("school_year"));
        String group_name = data.get("group_name");
        int level = Integer.parseInt(data.get("level"));
        int room_number = Integer.parseInt(data.get("room_number"));
        long entry_id = Long.parseLong(data.get("entry_id"));

        Model.Finder<Long, School> finder = new Model.Finder<Long, School>(Long.class,
                School.class);
        School school = finder.byId(school_id);

        RoomQueue roomQueue = new RoomQueue();
        roomQueue.entry_id = entry_id;
        roomQueue.school = school;
        roomQueue.school_year = school_year;
        roomQueue.level = level;
        roomQueue.group_name = group_name;
        roomQueue.room_number = room_number;
        roomQueue.save();

        return ok("");
    }
}
