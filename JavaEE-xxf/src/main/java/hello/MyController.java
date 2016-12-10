package hello;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.client.RestTemplate;
import org.springframework.beans.factory.annotation.Autowired;


@Controller
public class MyController {
	
    @Autowired
    private MessageRepository messageRepository;
    
    @Autowired
    private UserRepository userRepository;
    
    RestTemplate restTemplate = new RestTemplate();

    @RequestMapping(value = "index", method = RequestMethod.GET)
    String getAllMessages(Model model) {
    	Iterable<Message> messages = messageRepository.findAll();
    	model.addAttribute("messages", messages);
    	return "index";
    }
    
    @RequestMapping(value ="login", method = RequestMethod.GET)
    public String login() {
        return "login";
    }
    
    @RequestMapping(value ="login", method = RequestMethod.POST)
    public String loginSubmit(HttpServletRequest request, Model model) {
        String username = request.getParameter("name");
        String password = request.getParameter("password");
        User user = userRepository.findByUsername(username);
        if(user!=null){
            if(user.getPassword().equals(password)){
                    //return "profile";
            		return "loggedIn?userName="+username;
            }else{
                    return "login";
            }
        }
        return "login";
    }
    
    @RequestMapping(value ="loggedIn", method = RequestMethod.POST)
    public String getUserMessages(HttpServletRequest request, Model model) {
    	String userName = request.getParameter("userName");
    	Iterable<Message> messagesFromUser = messageRepository.findByFromUser(userName);
    	//Iterable<Message> messagesToUser = messageRepository.findByToUser(userName);
    	model.addAttribute("username", userName);
    	model.addAttribute("messages", messagesFromUser);
    	return "loggedIn";
    }
    
    @RequestMapping(value ="postMessage", method = RequestMethod.POST)
    public String postMessage(HttpServletRequest request, Model model) {
    	String userName = request.getParameter("userName");
    	String message = request.getParameter("message");
    	if(message!=""){
    		messageRepository.save(newMessage(userName, message));
    	}
    	Iterable<Message> messagesFromUser = messageRepository.findByFromUser(userName);
    	model.addAttribute("username", userName);
    	model.addAttribute("messages", messagesFromUser);
    	return "loggedIn";
    }
    
    public Message newMessage(String userName, String message){
    	DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Date date = new Date();
        ArrayList<String> users = new ArrayList<String>();
        String[] firstSplit = message.split("@");
        System.out.println(firstSplit.length);
        for(int i=1; i<firstSplit.length; i++){
        	users.add("@"+firstSplit[i].split(" ")[0]+" ");
        	System.out.println(firstSplit[0]);
        }  
        message = firstSplit[firstSplit.length-1].split(" ", 2)[1];
        return new Message(userName, message, users, dateFormat.format(date), "dark");
    }
    /*@RequestMapping(value ="loggedIn", method = RequestMethod.POST)
    public String submitMessage(HttpServletRequest request, Model model) {
    	String message = request.getParameter("message");
    	//User user = userRepository.findByUsername(username);
    	
    	return "loggedIn";
    }*/
    
   /* @RequestMapping(value = "loggedIn", method = RequestMethod.GET)
    String getUserMessages(Model model) {
    	Iterable<Message> messagesFromUser = messageRepository.findByFromUser(user);
    	Iterable<Message> messagesToUser = messageRepository.findByToUser(user);
    	model.addAttribute("messages", messagesFromUser);
    	model.addAttribute("messages", messagesToUser);
    	return "loggedIn";
    }*/
}
