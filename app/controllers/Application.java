package controllers;

import com.avaje.ebean.Ebean;
import com.avaje.ebean.ExpressionList;
import com.avaje.ebean.TxRunnable;
import models.Child;
import models.Parent;
import play.db.ebean.Model;
import play.db.ebean.Model.Finder;
import play.mvc.*;
import views.html.index;

import java.util.List;

public class Application extends Controller {

    public static Result index() {
        // 現在のParentを全検索して表示
        Finder<Long, Parent> finder = new Finder<Long, Parent>(Long.class,
                Parent.class);
        List<Parent> parents = finder.all();

        return ok(index.render(parents));
    }


    public static Result index2() {

        try {

            Ebean.execute(new TxRunnable() {
                @Override
                public void run() {
                    //To change body of implemented methods use File | Settings | File Templates.
                    Model.Finder<Long, Parent> finder = new Model.Finder<Long, Parent>(Long.class, Parent.class);
                    List<Parent> parents = finder.all();

                    for (Parent parent : parents) {
                        for (Child child : parent.children) {
                            child.delete();
                        }
                    }
                    throw new RuntimeException();
                }
            });
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return ok("data is not deleted!");
    }

    public static Result index_org() {
        insertFamily("石川たかつぐ", "小太郎", "次郎");
        insertFamily("やまだ太一", "数一", "数二");
        insertFamily("足立六郎", "一郎", "次郎");

        Model.Finder<Long, Parent> finder = new Model.Finder<>(Long.class, Parent.class);

        // parent1 の取得（条件を指定した検索）
//        ExpressionList<Parent> query = finder.where().like("name", "%足立%");
//        List<Parent> parent1List =query.findList();
//        // 1番目の名前を更新します
//        for (Parent parent1 : parent1List) {
//            //parent1.name = "新宿次郎";
//            //parent1.update();
//            //parent1.delete();
//        }
        // parent2 の取得（Idからの検索。Idは保存したときに振られたId）
        //Parent parent2 =finder.byId(new Long(2));
        // 2番目を削除します
        //parent2.delete();
        //List<Parent> parents = finder.all();


        Model.Finder<Long, Parent> finder2 = new Model.Finder<Long, Parent>(Long.class, Parent.class);
        List<Parent> parents2 = finder2.all();
        StringBuilder msg = new StringBuilder();
        for (Parent parent : parents2) {
            msg.append(parent.toString()).append("¥n");
            for (Child child : parent.children) {
                msg.append(" ").append(child.toString()).append("¥n");
            }
        }

        return ok(msg.toString());
    }

    private static void insertFamily(String name, String... args) {
        Parent parent1 = new Parent();
        parent1.name = name;

        for (String childName : args) {
            // 子1の追加
            Child child = new Child();
            child.name = name + childName;
            parent1.children.add(child);
        }
        parent1.save();
    }

}
