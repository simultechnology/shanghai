package controllers;

import com.avaje.ebean.ExpressionList;
import models.Entry;
import models.Question;
import models.Room;
import models.Subject;
import org.apache.commons.csv.CSVUtils;
import play.data.DynamicForm;
import play.data.Form;
import play.db.ebean.Model;
import play.db.ebean.Model.Finder;
import play.mvc.Controller;
import play.mvc.Http.*;
import play.mvc.Result;
import views.html.questions.*;
import views.xml.questions.*;

import java.io.*;
import java.util.*;

public class Questions extends Controller {

    private static HashMap<Integer, HashMap<Long, List<Question>>> cachedQuestionList = new HashMap();

    public static Result index() {

        return ok(index.render());
    }

    public static int getRoomNumberByResultId(long result_id) {
        Set<Integer> room_numbers = cachedQuestionList.keySet();
        for (Integer room_number :room_numbers) {
            HashMap<Long, List<Question>> questionsMap = cachedQuestionList.get(room_number);
            List<Question> questionList = questionsMap.get(result_id);
            if (questionList != null) {
                return room_number;
            }
        }
        return -1;
    }

    public static void deleteCacheByRoomNumber(int room_number) {
        HashMap<Long, List<Question>> questionsMap = cachedQuestionList.get(room_number);
        Set<Long> keys = questionsMap.keySet();
        for (Long key : keys) {
            List<Question> questions = questionsMap.get(key);
            questions.clear();
            questions = null;
        }
    }

    public static Result insert() {
        String[] params = {"school_year", "level", "subject_code", "content",
                           "choice1", "choice2","choice3","choice4", "answer"};
        DynamicForm input = Form.form();
        input = input.bindFromRequest(params);
        Map<String, String> data = input.data();
        int school_year = Integer.parseInt(data.get("school_year"));
        int level = Integer.parseInt(data.get("level"));
        String subject_code = data.get("subject_code");

        Finder<String, Subject> finder =
                new Finder<String, Subject>(String.class, Subject.class);
        Subject subject = finder.byId(subject_code);

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
                        String subject_code = val[3];

                        Finder<String, Subject> finder =
                                new Finder<String, Subject>(String.class, Subject.class);
                        Subject subject = finder.byId(subject_code);

                        question.subject = subject;
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

        HashMap<Long, List<Question>> cachedMap = cachedQuestionList.get(room_number);
        if (cachedMap != null) {
            Set<Long> key = cachedMap.keySet();
            for (long id : key) {
                List<Question> list = cachedMap.get(id);
                if (list == null || list.size() == 0) {
                    break;
                }
                return ok(questions.render(list, id));
            }
        }

        Finder<Long, Room> roomFinder = new Model.Finder<Long, Room>(Long.class,
                Room.class);
        Room selectedRoom = roomFinder.byId(Long.valueOf(room_number));
//        if (!selectedRoom.status) {
//            return ok(questions_not_available.render());
//        }
//        selectedRoom.status = false;
//        selectedRoom.save();

        Finder<Long, Entry> entryFinder = new Model.Finder<Long, Entry>(Long.class,
                Entry.class);
        Entry entry = entryFinder.byId(selectedRoom.entry_id);

        if (entry == null) {
            return ok(questions_not_available.render());
        }

        models.Result result = new models.Result();
        result.school_year = entry.school_year;
        result.school = entry.school;
        result.group_name = entry.group_name;
        result.save();
        Long result_id = result.result_id;

        List<Question> questionList = null;
        try {
            questionList = getQuestionList();
        } catch (Exception e) {
            return ok(questions_not_available.render());
        }

        HashMap<Long, List<Question>> map = new HashMap<>();
        map.put(result_id, questionList);
        cachedQuestionList.put(room_number, map);

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
