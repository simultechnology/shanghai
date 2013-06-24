package controllers;

import play.mvc.Controller;
import play.mvc.Result;
import views.html.school.*;

public class Schools extends Controller {

    public static Result index() {

        return ok(index.render());
    }

    public static Result register() {

        return ok("OK");
    }

    public static Result master() {

        return ok("OK");
    }

    public static Result entry() {

        return ok("OK");
    }
}
