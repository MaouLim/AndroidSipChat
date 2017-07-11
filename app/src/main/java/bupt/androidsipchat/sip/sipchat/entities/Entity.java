package bupt.androidsipchat.sip.sipchat.entities;

/*
 * Created by Maou on 2017/7/4.
 */
public class Entity {

    private String id = null;

    Entity() {
        this.id = "";
    }

    Entity(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
