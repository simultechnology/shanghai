package controllers;

import com.avaje.ebean.ExpressionList;
import models.Question;
import models.Room;
import org.apache.commons.csv.CSVUtils;
import play.data.DynamicForm;
import play.data.Form;
import play.db.ebean.Model;
import play.db.ebean.Model.Finder;
import play.mvc.Controller;
import play.mvc.Http.*;
import play.mvc.Result;
import views.html.name_result;
import views.html.type_result;
import views.html.questions.*;
import views.xml.questions.*;

import java.io.*;
import java.util.*;

public class Questions extends Controller {

    public static Result index() {

        return ok(index.render());
    }

    public static Result insert() {
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

        Question question = new Question();
        question.school_year = school_year;
        question.level = level;
        question.subject = subject;
        question.content = content;
        question.choice1 = choice1;
        question.choice2 = choice2;
        question.choice3 = choice3;
        question.choice4 = choice4;
        question.answer = answer;
        question.save();

        return ok("OK");
    }

    public static Result file() {

        return ok(file.render());
    }

    public static Result upload() {
        MultipartFormData body = request().body().asMultipartFormData();
        MultipartFormData.FilePart csv = body.getFile("csv");
        if (csv != null) {
            String fileName = csv.getFilename();
            String contentType = csv.getContentType();
            File file = csv.getFile();

            BufferedReader br = null;
            try {
                br = new BufferedReader(new FileReader(file));
                StringBuilder sb = new StringBuilder();
                String line = br.readLine();
                String[] columns = line.split(",");

                while (line != null) {
                    line = br.readLine();

                    if (line != null) {
                        String[] val = CSVUtils.parseLine(line.toString());

                        if (val[1] == null || val[1].length() == 0) {
                            continue;
                        }

                        Question question = new Question();
                        question.school_year = Integer.parseInt(val[1]);
                        question.level = Integer.parseInt(val[2]);
                        question.subject = val[3];
                        question.content = val[4];
                        question.choice1 = val[5];
                        question.choice2 = val[6];
                        question.choice3 = val[7];
                        question.choice4 = val[8];
                        question.answer = Integer.parseInt(val[9]);
                        question.save();
                    }
                }
                return ok("OK");
            } catch (Exception e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            } finally {
                if (br != null) {
                    try {
                        br.close();
                    } catch (IOException e) {
                    }
                }
            }
            return ok("File upload failed");
        } else {
            flash("error", "Missing file");
            return redirect(routes.Application.index());
        }
    }

    public static Result list(int room_number) {

        Finder<Long, Room> roomFinder = new Model.Finder<Long, Room>(Long.class,
                Room.class);
        Room selectedRoom = roomFinder.byId(Long.valueOf(room_number));
        if (!selectedRoom.status) {
            return ok(questions_not_available.render());
        }
        selectedRoom.status = false;
        selectedRoom.save();

        Finder<Long, Question> finder = new Model.Finder<Long, Question>(Long.class,
                Question.class);
        List<Object> questionIds = finder.findIds();
        if (questionIds.size() == 0) {
            return ok(questions_not_available.render());
        }

        Set<Long> randomIds = new HashSet<Long>();

        int questionsTotalNum = questionIds.size();
        while (randomIds.size() < 20) {
            Random rand = new Random();
            int idx = rand.nextInt(questionsTotalNum);
            randomIds.add((Long) questionIds.get(idx));
        }

        List<Long> randomIdList = new ArrayList<Long>();
        randomIdList.addAll(randomIds);

        ExpressionList<Question> expressionList = finder.where().idIn(randomIdList);
        List<Question> questionList = expressionList.findList();

        return ok(questions.render(questionList));
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
