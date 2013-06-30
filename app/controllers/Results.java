package controllers;

import play.data.DynamicForm;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import views.html.results.*;

public class Results extends Controller {

    public static Result complete() {
        String[] params = {"result_id", "score"};
        DynamicForm input = Form.form();
        input = input.bindFromRequest(params);
        Map<String, String> data = input.data();
        long result_id = Long.parseLong(data.get("result_id"));
        int score = Integer.parseInt(data.get("score"));
        List list = new ArrayList<Map>();
        for (int i = 1; i <= 20; i++) {
            String subject = data.get("answer" + i + "_subject");
            int status = Integer.parseInt(data.get("answer" + i + "_status"));
            int time = Integer.parseInt(data.get("answer" + i + "_time"));
            Map map = new HashMap();
            map.put("subject", subject);
            map.put("status", status);
            map.put("time", time);
            list.add(map);
        }



        return ok("OK");
    }

    public static Result test() {

        return ok(test.render());
    }
}
