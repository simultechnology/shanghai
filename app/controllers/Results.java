package controllers;

import com.avaje.ebean.Ebean;
import com.avaje.ebean.SqlRow;
import models.Question;
import models.Room;
import models.Score;
import models.ScorePK;
import play.data.DynamicForm;
import play.data.Form;
import play.db.ebean.Model.Finder;
import play.mvc.Controller;
import play.mvc.Result;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Date;

import views.html.results.*;
import views.xml.results.*;

public class Results extends Controller {

    public static Result complete() {
        String[] params = {"result_id", "score"};
        DynamicForm input = Form.form();
        input = input.bindFromRequest(params);
        Map<String, String> data = input.data();
        long result_id = Long.parseLong(data.get("result_id"));
        int score = Integer.parseInt(data.get("score"));

        Finder<Long, models.Result> finder =
                new Finder<Long, models.Result>(Long.class, models.Result.class);
        models.Result result = finder.byId(result_id);
        result.score = score;
        result.save();

        Map<String, Map> subjectCodeMap = new HashMap<String, Map>();
        for (int i = 1; i <= 20; i++) {
            String subject_code = data.get("answer" + i + "_subject_code");
            int status = Integer.parseInt(data.get("answer" + i + "_status"));
            int time = Integer.parseInt(data.get("answer" + i + "_time"));
            Map<String, Integer> map = subjectCodeMap.get(subject_code);
            if (map == null) {
                map = new HashMap();
                map.put("correct_number", 0);
                map.put("mistake_number", 0);
                map.put("time_over_number", 0);
                map.put("total_number", 0);
                map.put("time", 0);
            }
            if (status == 1) {
                map = updateMapContent(map, "correct_number", 1);
            }
            else if (status == 2) {
                map = updateMapContent(map, "mistake_number", 1);
            }
            else if (status == 3) {
                map = updateMapContent(map, "time_over_number", 1);
            }
            map = updateMapContent(map, "total_number", 1);
            map = updateMapContent(map, "time", time);
            subjectCodeMap.put(subject_code, map);
        }

        Set<String> subject_codes = subjectCodeMap.keySet();
        for (String subject_code : subject_codes) {
            ScorePK pk = new ScorePK();
            pk.result_id = result_id;
            pk.subject_code = subject_code;
            Score sc = new Score();
            sc.scorePK = pk;
            Map<String, Integer> map = subjectCodeMap.get(subject_code);
            sc.correct_number = map.get("correct_number");
            sc.mistake_number = map.get("mistake_number");
            sc.time = map.get("time_over_number");
            sc.total_number = map.get("total_number");
            sc.time = map.get("time");
            sc.save();
        }

        int room_number = Questions.getRoomNumberByResultId(result_id);
        Finder<Long, Room> roomFinder =
                new Finder<Long, Room>(Long.class, Room.class);
        Room room = roomFinder.byId(Long.valueOf(room_number));
        room.status = true;
        room.save();

        // キャッシュしている問題リストの削除
        Questions.deleteCacheByRoomNumber(room_number);

        return ok(results.render(true));
    }

    private static Map<String, Integer> updateMapContent(
            Map<String, Integer> map, String key, int updateNumber) {

        int content = map.get(key);
        content += updateNumber;
        map.put(key, content);
        return map;
    }

    public static Result log() {

        // Prepare a chunked text stream
        Chunks<String> chunks = new StringChunks() {

            // Called when the stream is ready
            public void onReady(Chunks.Out<String> out) {
                registerOutChannelSomewhere(out);
            }
        };

        String strDate =
                new SimpleDateFormat("yyyyMMddhhmmssSSS").format(new Date());
        String fileName = String.format("log%s.csv", strDate);
        response().setContentType("application/x-download");
        response().setHeader("Content-disposition",
                String.format("attachment; filename=%s", fileName));

        return ok(chunks);
    }

    public static void registerOutChannelSomewhere(Chunks.Out<String> out) {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddhhmmss");

        String sql =
                "select school_name, school_year, group_name, subject_name, " +
                "level, score, correct_number, mistake_number, " +
                "time_over_number, total_number, time, s.update_date update_date " +
                "from result r, score s, school, subject " +
                "where r.result_id = s.result_id " +
                "and r.school_id = school.school_id " +
                "and s.subject_code = subject.subject_code";

        List<SqlRow> sqlRows = Ebean.createSqlQuery(sql).findList();

        for (SqlRow r : sqlRows) {
            StringBuilder sb = new StringBuilder();
            sb.append(r.getString("school_name"));
            sb.append(",");
            sb.append(r.getInteger("school_year"));
            sb.append(",");
            sb.append(r.getString("group_name"));
            sb.append(",");
            sb.append(r.getString("subject_name"));
            sb.append(",");
            sb.append(r.getInteger("level"));
            sb.append(",");
            sb.append(r.getInteger("score"));
            sb.append(",");
            sb.append(r.getInteger("correct_number"));
            sb.append(",");
            sb.append(r.getInteger("mistake_number"));
            sb.append(",");
            sb.append(r.getInteger("time_over_number"));
            sb.append(",");
            sb.append(r.getInteger("total_number"));
            sb.append(",");
            sb.append(r.getInteger("time"));
            sb.append(",");
            sb.append(sdf.format(r.getDate("update_date")));
            sb.append("\n");
            out.write(sb.toString());
        }
        out.close();
    }


    public static Result test() {

        return ok(test.render());
    }
}
