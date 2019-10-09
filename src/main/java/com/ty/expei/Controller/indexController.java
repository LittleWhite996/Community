package com.ty.expei.Controller;

import com.ty.expei.mapper.UserMapper;
import com.ty.expei.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;


@Controller
public class indexController {
    @Autowired
    private UserMapper userMapper;

    @GetMapping("/")
    public String index(HttpServletRequest request){
        Cookie[] cookies =request.getCookies();
        for(Cookie cookie:cookies){
            if (cookie.getName().equals("token")){
                String token=cookie.getValue();
                User user=userMapper.findbytoken(token);
                if(user!=null){
                    request.getSession().setAttribute("user",user);
                }
                break;
            }


        }



        return "index";
    }
}
