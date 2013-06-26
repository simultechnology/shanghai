package controllers;

import com.google.common.base.Strings;
import models.Entry;
import models.School;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.node.ObjectNode;
import play.data.DynamicForm;
import play.data.Form;
import play.db.ebean.Model;
import play.db.ebean.Model.Finder;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import views.html.school.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class Schools extends Controller {

    public static Result index() {

        return ok(index.render());
    }

    public static Result register() {

        String[] params = {"school_id", "play_date", "school_year",
                "group_name", "level"};
        DynamicForm input = Form.form();
        input = input.bindFromRequest(params);
        Map<String, String> data = input.data();
        long school_id = Long.parseLong(data.get("school_id"));
        Date play_date = null;
        try {
            play_date = new SimpleDateFormat("yyyy/MM/dd").parse(data.get("play_date"));
        } catch (ParseException e) {
            return badRequest();
        }

        Finder<Long, School> finder = new Finder<Long, School>(Long.class,
                School.class);
        School school = finder.byId(school_id);

        for (int i = 1; i <=4; i++) {
            String school_year = data.get("school_year_" + i);
            String group_name = data.get("group_name_" + i);
            String level = data.get("level_" + i);
            if (Strings.isNullOrEmpty(school_year) ||
                Strings.isNullOrEmpty(group_name) ||
                Strings.isNullOrEmpty(level)) {
                continue;
            }
            Entry entry = new Entry();
            entry.school = school;
            entry.play_date = play_date;
            entry.school_year = Integer.parseInt(school_year);
            entry.group_name = group_name;
            entry.level = Integer.parseInt(level);
            entry.save();
        }

        return ok("OK");
    }

    public static Result master() {
        String[] params = {"name"};
        DynamicForm input = Form.form();
        input = input.bindFromRequest(params);
        String name = input.data().get("name");

        if (name == null || name.length() == 0) {
            return ok(entry.render("学校名を入力して下さい。"));
        }

        School school = new School();
        school.name = name;
        school.save();

        return ok("OK");
    }

    public static Result entry() {

        return ok(entry.render(""));
    }

    public static Result list(String name) {

        Finder<Long, School> finder = new Finder<Long, School>(Long.class,
                School.class);

        List<School> schools;
        if (name == null || name.length() == 0) {
            schools = finder.all();
        }
        else {
            schools = finder.where().like("name", "%" + name + "%").findList();
        }

        ObjectNode result = Json.newObject();
        if(schools == null) {
            result.put("status", "NG");
            return badRequest(result);
        } else {
            JsonNode node = Json.toJson(schools);
            result.put("status", "OK");
            result.put("schools", node);
            return ok(result);
        }
    }

    public static Result all() {

        Finder<Long, School> finder = new Finder<Long, School>(Long.class,
                School.class);

        List<School> schools = finder.all();
        ObjectNode result = Json.newObject();
        if(schools == null) {
            result.put("status", "NG");
            return badRequest(result);
        } else {
            JsonNode node = Json.toJson(schools);
            result.put("status", "OK");
            result.put("schools", node);
            return ok(result);
        }
    }
}
