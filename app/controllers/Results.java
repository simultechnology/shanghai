package controllers;

import play.data.DynamicForm;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;

import java.util.Map;

public class Results extends Controller {

    public static Result complete() {
        String[] params = {"school_year", "level", "subject", "content",
                "choice1", "choice2","choice3","choice4", "answer"};
        DynamicForm input = Form.form();
        input = input.bindFromRequest(params);
        Map<String, String> data = input.data();
        int school_year = Integer.parseInt(data.get("school_year"));
        int level = Integer.parseInt(data.get("level"));
        String subject = data.get("subject");
        String content = data.get("content");
        String choice1 = data.get("choice1");
        String choice2 = data.get("choice2");
        String choice3 = data.get("choice3");
        String choice4 = data.get("choice4");
        int answer = Integer.parseInt(data.get("answer"));

//        Question question = new Question();
//        question.school_year = school_year;
//        question.level = level;
//        question.subject = subject;
//        question.content = content;
//        question.choice1 = choice1;
//        question.choice2 = choice2;
//        question.choice3 = choice3;
//        question.choice4 = choice4;
//        question.answer = answer;
//        question.save();

        return ok("OK");
    }
}
