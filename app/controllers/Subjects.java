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
import java.util.Set;

public class Subjects extends Controller {

    public static Result index() {

        Finder<Long, Subject> finder =
                new Model.Finder<Long, Subject>(Long.class, Subject.class);
        List<Subject> subjects = finder.where()
                                       .orderBy("createDate DESC")
                                       .findList();

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
        String[] params = {"subject_codes", "priorities"};
        DynamicForm input = Form.form();
        input = input.bindFromRequest(params);
        Map<String,String> data = input.data();

        Set<String> keys = data.keySet();

        if (keys.size() > 0) {
            Finder<String, Subject> finder =
                    new Model.Finder<String, Subject>(String.class, Subject.class);

            for (String key : keys) {
                Subject subject = finder.byId(data.get(key));
                if (key.contains("subject_codes")) {
                    if (subject != null) {
                        subject.delete();
                    }
                }
                else if (key.contains("hidden_priorities")) {
                    String priorityKey = key.replace("hidden_", "");
                    // 優先フラグがONのままで変化のない場合
                    // キーを削除する
                    if (keys.contains(priorityKey)) {
                        //keys.remove(priorityKey);
                    }
                    else {
                        String id = data.get(priorityKey);
                        if (id == null) {
                            if (subject != null) {
                                subject.priority = false;
                                subject.save();
                            }
                        }
                    }
                }
                else if (key.startsWith("priorities")) {
                    if (subject != null) {
                        subject.priority = true;
                        subject.save();
                    }
                }
            }
        }

        return redirect("/subjects");
    }
}
