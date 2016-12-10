package hello;

import java.util.ArrayList;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Message {
    
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Long id;
    private String fromUser;
    private String message;
    private ArrayList<String> toUsers;
    private String tag;
    private String date;
    
    protected Message() {}
    
    public Message(String fromUser, String message, ArrayList<String> toUsers, String date, String tag){
        this.fromUser = fromUser;
        this.message = message;
        this.toUsers = toUsers;
        this.date = date;
        this.tag = tag;
        /*DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date date = new Date();
        System.out.println(dateFormat.format(date)); //2016/11/16 12:08:43*/
    }
    
    public void setFromUser(String fromUser) {
        this.fromUser = fromUser;
    }
    
    public String getFromUser(){
        return fromUser;
    }
    
    public ArrayList<String> getToUsers() {
        return toUsers;
    }
    
    public void setToUsers(ArrayList<String> toUsers) {
        this.toUsers = toUsers;
    }
    
    
    public Long getId() {
        return id;
    }
    
    public String getMessage(){
        return message;
    }
    
    public String getTag(){
        return tag;
    }
    
    public String getDate(){
        return date;
    }
    
}