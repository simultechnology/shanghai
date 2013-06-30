package controllers;

import models.School;
import models.Subject;
import play.data.DynamicForm;
import play.data.Form;
import play.db.ebean.Model;
import play.db.ebean.Model.Finder;
import play.mvc.Controller;
import play.mvc.Result;
import views.html.subjects.*;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public class Subjects extends Controller {

    public static Result index() {

        Finder<Long, Subject> finder =
                new Model.Finder<Long, Subject>(Long.class, Subject.class);
        List<Subject> subjects = finder.all();

        return ok(index.render("", "", subjects));
    }

    public static Result master() {
        String[] params = {"subject_code", "subject_name"};
        DynamicForm input = Form.form();
        input = input.bindFromRequest(params);
        String subject_code = input.data().get("subject_code");
        String subject_name = input.data().get("subject_name");

        String msg1 = null;
        if (subject_code == null || subject_code.length() == 0) {
            msg1 = "科目コードを入力して下さい。";
        }

        String msg2 = null;
        if (subject_name == null || subject_name.length() == 0) {
            msg2 = "科目名を入力して下さい。";
        }

        if (msg1 != null || msg2 != null) {
            ok(index.render(msg1, msg2, null));
        }

        Subject subject = new Subject();
        subject.subject_code= subject_code;
        subject.subject_name= subject_name;
        subject.save();

        return redirect("/subjects");
    }


    public static Result delete() {
        String[] params = {"subject_codes"};
        DynamicForm input = Form.form();
        input = input.bindFromRequest(params);
        Map<String,String> data = input.data();

        Collection<String> values = data.values();
        if (values.size() > 0) {
            Finder<String, Subject> finder =
                    new Model.Finder<String, Subject>(String.class, Subject.class);
            for (String subject_code : values) {
                Subject subject = finder.byId(subject_code);
                subject.delete();
            }
        }
        return redirect("/subjects");
    }
}
