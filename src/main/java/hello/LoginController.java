package hello;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import javax.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.beans.factory.annotation.Autowired;



@Controller
public class LoginController {
    @Autowired
    private UserRepository repository;
    
    @RequestMapping(value ="/login", method = RequestMethod.POST)
    public String loginSubmit(HttpServletRequest request, Model model) {
        String username = request.getParameter("name");
        String password = request.getParameter("password");
        User user = repository.findByUsername(username);
        if(user!=null){
            if(user.getPassword().equals(password)){
                    return "profile";
            }else{
                    return "login";
            }
        }
        return "login";
    }
    
    @RequestMapping("/login")
    public String login() {
        return "login";
    }

    
}