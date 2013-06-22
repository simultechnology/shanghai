package controllers;

import play.data.DynamicForm;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;
import views.html.name_result;
import views.html.top;
import views.html.type_result;

public class Uranai extends Controller {

    public static Result showTop() {
        return ok(top.render());
    }

    public static Result showNameResult() {
        String[] params = {"name"};
        DynamicForm input = Form.form();
        input = input.bindFromRequest(params);
        String name = input.data().get("name");

        if (name == null || name.length() == 0) {
            return badRequest("名前を入力して下さい。");
        }
        return ok(name_result.render(name));
    }

    public static Result showType(int type) {
        String[] params = {"name"};
        DynamicForm input = Form.form();
        input = input.bindFromRequest(params);
        String name = input.data().get("name");

        if (type == 1) {
            return ok(type_result.render(name, "恋愛運"));
        }
        else if (type == 2) {
            return ok(type_result.render(name, "金運"));
        }
        else {
            return notFound("/type/" + type + "存在しません。");
        }
    }
}
