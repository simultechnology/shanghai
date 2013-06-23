package controllers;

import models.Room;
import play.db.ebean.Model;
import play.db.ebean.Model.Finder;
import play.mvc.Controller;
import play.mvc.Result;
import views.xml.rooms.*;

import java.util.List;

public class Rooms extends Controller {

    public static Result setup() {

        Room room1 = new Room();
        room1.room_number = 1;
        room1.status = true;
        room1.save();

        Room room2 = new Room();
        room2.room_number = 2;
        room2.status = true;
        room2.save();

        return ok("OK");
    }

    public static Result check() {

        Finder<Long, Room> finder = new Finder<Long, Room>(Long.class,
                    Room.class);
        List<Room> rooms = finder.all();

        return ok(check.render(rooms));
    }

}
