package bupt.androidsipchat.sip.sipchat.entities;

import java.util.Date;

/*
 * Created by Maou on 2017/7/4.
 */
public class User extends Entity {

    static final char SEX_MALE = 'm';
    static final char SEX_FEMALE = 'f';
    static final char SEX_UNKNOWN = 'u';

    private String userName = null;
    private char sex = SEX_UNKNOWN;
    private Date birthday = null;
    private String address = null;
    private String sculptureURL = null;

    // todo
}
