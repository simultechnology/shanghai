package controllers;

import com.avaje.ebean.Query;
import models.Entry;
import play.db.ebean.Model;
import play.mvc.Result;
import play.mvc.Controller;
import views.html.entries.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

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
}
