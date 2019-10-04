package com.qhb.community.community.controller;

import com.qhb.community.community.dao.AccessTokenDTO;
import com.qhb.community.community.dao.GitHubUser;
import com.qhb.community.community.mapper.UserMapper;
import com.qhb.community.community.model.User;
import com.qhb.community.community.provider.GitHubProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

/**
 * 用户登录控制层 获取github用户信息
 */
@Controller
public class AuthorizeController {
    @Autowired
    private GitHubProvider gitHubProvider;
    @Autowired
    private UserMapper userMapper;
    @Value("${github.client_id}")
    private String clientId;
    @Value("${github.client_secret}")
    private String clientSecret;
    @Value("${github.redirect_uri}")
    private  String redirectUri;
    @GetMapping("callback")
    public String callback(@RequestParam(name = "code") String code,
                           @RequestParam(name = "state") String state,
                           HttpServletResponse response){
        AccessTokenDTO accessTokenDTO=new AccessTokenDTO();
        accessTokenDTO.setClient_id(clientId);
        accessTokenDTO.setClient_secret(clientSecret);
        accessTokenDTO.setCode(code);
        accessTokenDTO.setRedirect_uri(redirectUri);
        accessTokenDTO.setState(state);
        String accessToken = gitHubProvider.getAccessToken(accessTokenDTO);
        GitHubUser gitHubUser = gitHubProvider.getUser(accessToken);
       if (gitHubUser!=null){
           User user=new User();
           user.setName(gitHubUser.getName());
           String token=UUID.randomUUID().toString();
           user.setToken(token);
           user.setAccountId(String.valueOf(gitHubUser.getId()));
           user.setGmtCreate(System.currentTimeMillis());
           user.setGmtModified(user.getGmtCreate());
           user.setAvatarUrl(gitHubUser.getAvatar_url());
           userMapper.insert(user);
           response.addCookie(new Cookie("token",token));

           return "redirect:/";
       }else {
           //失败 重新登录
           return "redirect:/";
       }

    }
}
