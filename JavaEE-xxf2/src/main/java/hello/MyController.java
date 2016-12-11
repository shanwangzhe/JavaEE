package hello;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;

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
    
    @RequestMapping(value ="/login", method = RequestMethod.POST)
    public String loginSubmit(HttpServletRequest request, Model model) {
    	String action = request.getParameter("action");
    	if(action.equals("login")){
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
    	}else if(action.equals("signIn")){
    		String username = request.getParameter("name");
	        String password = request.getParameter("password");
	        User user = userRepository.findByUsername(username);
	        if(user==null){
	        	user = new User(username, password);
	        	userRepository.save(user);
	        	return "loggedIn?userName="+username;
	        }
    	}
        return "login";
    }
    
    @RequestMapping(value ="loggedIn", method = RequestMethod.POST)
    public String getUserMessages(HttpServletRequest request, Model model) {
    	String userName = request.getParameter("userName");
    	model.addAttribute("username", userName);
    	model.addAttribute("messages", containsUser(userName));
    	return "loggedIn";
    }
    
    public ArrayList<Message> containsUser(String userName){
    	ArrayList<Message> messagesFound = new ArrayList<Message>();
    	Iterable<Message> allMessages = messageRepository.findAll();
    	Iterator<Message> it = allMessages.iterator();
    	while(it.hasNext()) {
    		Message m = it.next();
    		ArrayList<String> toUserFound = m.getToUsers();
    		if(toUserFound.contains("@"+userName) || m.getFromUser().equals(userName)){
    			messagesFound.add(m);
    		}
        }
    	return messagesFound;
    }
    
    @RequestMapping(value ="postMessage", method = RequestMethod.POST)
    public String postMessage(HttpServletRequest request, Model model) {
    	String userName = request.getParameter("userName");
    	String message = request.getParameter("message");
    	if(message!=""){
    		messageRepository.save(formatMessage(userName, message));
    	}
    	model.addAttribute("username", userName);
    	model.addAttribute("messages", containsUser(userName));
    	return "loggedIn";
    }
    
    public Message formatMessage(String userName, String message){
    	DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Date date = new Date();
        ArrayList<String> toUsers = new ArrayList<String>();
        ArrayList<String> hashTags = new ArrayList<String>();
        
        //recherche destinataires
        if(message.contains("@")){
        	String[] firstSplit = message.split("@");
            for(int i=1; i<firstSplit.length; i++){
            	toUsers.add("@"+firstSplit[i].split(" ")[0]);
            }  
            message = firstSplit[firstSplit.length-1].split(" ", 2)[1];
        }
        
        //recherche hashTags
        if(message.contains("#")){
        	String[] firstSplit = message.split("#");
        	for(int i=1; i<firstSplit.length; i++){
        		hashTags.add("#"+firstSplit[i].split(" ")[0]);
            }
        }
        
        return new Message(userName, message, toUsers, dateFormat.format(date), hashTags);
    }
    
    
    @RequestMapping(value ="/profile", method = RequestMethod.POST)
    public String searchProfile(HttpServletRequest request, Model model) {
    	String userName = request.getParameter("username");
    	String refactorUserName = userName;
    	if(userName.charAt(0)=='@'){
    		refactorUserName = userName.substring(1);
    	}

    	User user = userRepository.findByUsername(refactorUserName);
        if(user!=null){
        	model.addAttribute("username", user.getUsername());
        	model.addAttribute("email", user.getEmail());
        	model.addAttribute("messages", containsUser(refactorUserName));
        	return "profile";
    	}
    	return "profile_not_found";
    }
    
    @RequestMapping(value ="/hashTag", method = RequestMethod.POST)
    public String getHashTagReferences(HttpServletRequest request, Model model) {
    	String hashTag = request.getParameter("hashTag");
    	Iterable<Message> allMessages = messageRepository.findAll();
    	ArrayList<Message> messagesWithAcutalHashtag = new ArrayList<Message>();
    	Iterator<Message> it = allMessages.iterator();
    	while(it.hasNext()) {
    		Message m = it.next();
    		ArrayList<String> hashTagsFound = m.getHashTags();
    		if(hashTagsFound.contains(hashTag)){
    			messagesWithAcutalHashtag.add(m);
    		}
        } 
    	model.addAttribute("hashTag", hashTag);
    	model.addAttribute("messages", messagesWithAcutalHashtag);
    	return "hashTag";
    }
    
    @RequestMapping(value ="/edit_profile", method = RequestMethod.POST)
    public String editProfile(HttpServletRequest request, Model model) {
    	String username = request.getParameter("username");
        User user = userRepository.findByUsername(username);
        if(user!=null){
        	model.addAttribute("username", username);
        	model.addAttribute("email", user.getEmail());
        	model.addAttribute("facebook", user.getFacebook());
        	model.addAttribute("twitter", user.getTwitter());
        	model.addAttribute("linkedIn", user.getLinkedIn());
        	return "edit_profile";
        }
        return "profile_not_found";
    }
    
    @RequestMapping(value ="/save_edit_profile", method = RequestMethod.POST)
    public String saveEditProfile(HttpServletRequest request, Model model) {
    	String username = request.getParameter("username");
        String email = request.getParameter("email");
        String facebook = request.getParameter("facebook");
        String twitter = request.getParameter("twitter");
        String linkedIn = request.getParameter("linkedIn");
        User user = userRepository.findByUsername(username);
        if(user!=null){
        	user.setEmail(email);
        	user.setFacebook(facebook);
        	user.setTwitter(twitter);
        	user.setLinkedIn(linkedIn);
        	userRepository.save(user);
        }
        return "loggedIn?userName="+username;
    }
    
    
    
}
