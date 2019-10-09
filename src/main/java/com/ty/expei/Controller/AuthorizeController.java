package com.ty.expei.Controller;

import com.ty.expei.dto.AccessTokenDto;
import com.ty.expei.dto.GitHubUser;
import com.ty.expei.mapper.UserMapper;
import com.ty.expei.model.User;
import com.ty.expei.provider.GitHubprovider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;


@Controller
public class AuthorizeController {
    @Autowired
    private GitHubprovider gitHubprovider;

    @Value("${github.client_id}")
    private String client_id;

    @Value("${github.client_secret}")
    private String client_secret;

    @Value("${github.redirect_url}")
    private String redirect_url;
    @Autowired
    private UserMapper userMapper;

    @GetMapping("/callback")
    public String callback(@RequestParam(name = "code") String code,
                           @RequestParam(name="state") String state,
                                   HttpServletRequest request,
                           HttpServletResponse response
    ){
        AccessTokenDto accessTokenDto=new AccessTokenDto();
        accessTokenDto.setClient_id(client_id);
        accessTokenDto.setClient_secret(client_secret);
        accessTokenDto.setCode(code);
        accessTokenDto.setRedirect_url(redirect_url);
        accessTokenDto.setState(state);
        String access=gitHubprovider.getaccesstoken(accessTokenDto);
        System.out.println(access);
        GitHubUser gitHubUser=gitHubprovider.getuser(access);
        if(gitHubUser!=null){
            User user=new User();
            String token=UUID.randomUUID().toString();
            user.setToken(token);
            user.setName(gitHubUser.getName());
            user.setAccount_id(String.valueOf(gitHubUser.getId()));
            user.setGmt_create(System.currentTimeMillis());
            user.setGmt_modified(user.getGmt_create());
            response.addCookie(new Cookie("token",token));
            userMapper.insert(user);

            return "redirect:/";
        }
        else {
            return "redirect:/";
        }
    }

}
