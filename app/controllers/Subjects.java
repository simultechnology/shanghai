package controllers;

import com.avaje.ebean.Ebean;
import com.avaje.ebean.SqlRow;
import models.School;
import models.Subject;
import org.codehaus.jackson.node.ObjectNode;
import play.data.DynamicForm;
import play.data.Form;
import play.db.ebean.Model;
import play.db.ebean.Model.Finder;
import play.mvc.BodyParser;
import play.mvc.Controller;
import play.mvc.Result;
import views.html.subjects.*;
import play.libs.Json;
import play.libs.Json.*;

import java.util.*;

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


    public static Result update() {
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

    public static List<Subject> select() {
        Finder<String, Subject> subjectFinder =
                new Model.Finder<String, Subject>(String.class, Subject.class);
        List<Subject> subjects = subjectFinder.all();
        List<Subject> newSubjects = new ArrayList<>();
        List<Subject> restSubject = new ArrayList<>();
        for (Subject s : subjects) {
            if (s.priority) {
                newSubjects.add(s);
            }
            else {
                restSubject.add(s);
            }
        }

        Map<String, Integer> validSubjects = getValidSubjectCodes();
        Set<String> validSubjectCodes = validSubjects.keySet();

        // 優先フラグ無しの科目数
        int restCnt = 5 - newSubjects.size();

        for (int i = 0; i < restCnt;) {
            Random r = new Random();
            int idx = r.nextInt(restSubject.size());
            Subject subject = restSubject.remove(idx);
            if (validSubjectCodes.contains(subject.subject_code)) {
                newSubjects.add(subject);
                i++;
            }
        }
        return newSubjects;
    }

    @BodyParser.Of(play.mvc.BodyParser.Json.class)
    public static Result check() {
        ObjectNode result = Json.newObject();

        Map<String, Integer> subjectCountMap = getValidSubjectCodes();

        Set<String> subject_codes = subjectCountMap.keySet();
        // 4問以上、問題が存在する科目数が5科目未満のためエラー
        if (subject_codes.size() < 5) {
            result.put("status", "NG");
            result.put("message", "出題可能な科目が不足しています。");
            return badRequest(result);
        }

        Finder<String, Subject> subjectFinder =
                new Model.Finder<String, Subject>(String.class, Subject.class);
        List<Subject> subjects = subjectFinder.where().eq("priority", true).findList();
        for (Subject s : subjects) {
            if (!subjectCountMap.keySet().contains(s.subject_code)) {
                // 優先フラグ有り科目の問題数が4問未満のためエラー
                result.put("status", "NG");
                result.put("message", "優先科目の問題数が不足しています。");
                return badRequest(result);
            }
        }
        result.put("status", "OK");
        result.put("message", "OK");
        return ok(result);
    }

    private static Map<String, Integer> getValidSubjectCodes() {
        String sql = "select subject_code, count from "
                   + "(select subject_code, count(subject_code) count "
                   + "from question group by subject_code ) sub "
                   + "where count >= 4";

        List<SqlRow> sqlRows = Ebean.createSqlQuery(sql).findList();

        Map<String, Integer> subjectCountMap = new HashMap<>();
        for (SqlRow r : sqlRows) {
            String subject_code = r.getString("subject_code");
            Integer count = r.getInteger("count");
            subjectCountMap.put(subject_code, count);
            System.out.printf("%s : %d件\n", subject_code, count);
        }
        return subjectCountMap;
    }
}
