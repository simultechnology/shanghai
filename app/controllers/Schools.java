package controllers;

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

import java.util.List;

public class Schools extends Controller {

    public static Result index() {

        return ok(index.render());
    }

    public static Result register() {

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
