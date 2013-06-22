package controllers;

import play.mvc.Controller;
import play.mvc.Result;
import views.html.welcome.index;

public class Welcome extends Controller {

    public static Result index() {

       return ok(index.render());
    }
}
