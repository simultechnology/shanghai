package controllers;

import com.avaje.ebean.ExpressionList;
import models.Entry;
import models.Question;
import models.Room;
import org.apache.commons.csv.CSVUtils;
import play.data.DynamicForm;
import play.data.Form;
import play.db.ebean.Model;
import play.db.ebean.Model.Finder;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Http.*;
import play.mvc.Result;
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
                return ok(end.render("ファイルを正常に取り込みました。"));
            } catch (Exception e) {
                return ok(end.render("ファイルを正常に取り込む異ができませんでした。"));
            } finally {
                if (br != null) {
                    try {
                        br.close();
                    } catch (IOException e) {
                    }
                }
            }
        } else {
            flash("error", "Missing file");
            return redirect(routes.Questions.file());
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

        Finder<Long, Entry> entryFinder = new Model.Finder<Long, Entry>(Long.class,
                Entry.class);
        Entry entry = entryFinder.byId(selectedRoom.entry_id);

        models.Result result = new models.Result();
        result.school_year = entry.school_year;
        result.school = entry.school;
        result.group_name = entry.group_name;
        result.save();
        long result_id = result.result_id;

        List<Question> questionList = null;
        try {
            questionList = getQuestionList();
        } catch (Exception e) {
            return ok(questions_not_available.render());
        }
        return ok(questions.render(questionList, result_id));
    }

    public static Result listSample() {

        List<Question> questionList = null;
        try {
            questionList = getQuestionList();
        } catch (Exception e) {
            return ok(questions_not_available.render());
        }
        return ok(questions.render(questionList, Long.valueOf(99999999)));
    }

    private static List<Question> getQuestionList() throws Exception {
        Finder<Long, Question> finder = new Finder<Long, Question>(Long.class,
                Question.class);
        List<Object> questionIds = finder.findIds();
        if (questionIds.size() == 0) {
            throw new Exception();
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
        return expressionList.findList();
    }
}
