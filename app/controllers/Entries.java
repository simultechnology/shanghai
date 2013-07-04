package controllers;

import models.Entry;
import models.Room;
import play.data.DynamicForm;
import play.data.Form;
import play.db.ebean.Model.Finder;
import play.mvc.Result;
import play.mvc.Controller;
import views.html.entries.*;
import views.html.questions.end;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class Entries extends Controller {

    public static Result index() {

        Finder<Long, Entry> finder = new Finder<Long, Entry>(Long.class,
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

        List<Entry> entries = finder.where().eq("play_date", today).eq("selected", false).findList();

        return ok(index.render(entries));
    }

    public static Result add() {
        String[] params = {"room_number", "entry_id"};
        DynamicForm input = Form.form();
        input = input.bindFromRequest(params);
        Map<String, String> data = input.data();
        long room_number = Long.parseLong(data.get("room_number"));
        long entry_id = Long.parseLong(data.get("entry_id"));

        Finder<Long, Room> finder = new Finder<Long, Room>(Long.class,
                Room.class);
        Room room = finder.byId(room_number);
        if (!room.status) {
            return ok(end.render("現在、room" + room_number + "は使用中のためエントリー追加できません。"));
        }
        room.status = false;
        room.entry_id = entry_id;
        room.save();

        Finder<Long, Entry> entryFinder = new Finder<Long, Entry>(Long.class,
                Entry.class);
        Entry entry = entryFinder.byId(entry_id);
        entry.selected = true;
        entry.save();

        return redirect(routes.Entries.index());
    }


    public static Result stop(int room_number) {

        Finder<Long, Room> finder = new Finder<Long, Room>(Long.class,
                Room.class);
        Room room = finder.byId(Long.valueOf(room_number));
        room.status = true;
        room.save();

        Finder<Long, Entry> entryFinder = new Finder<Long, Entry>(Long.class,
                Entry.class);
        Entry entry = entryFinder.byId(room.entry_id);
        entry.selected = true;
        entry.save();

        return redirect(routes.Entries.index());
    }
}
