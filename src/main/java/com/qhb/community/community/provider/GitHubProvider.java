package com.qhb.community.community.provider;

import com.alibaba.fastjson.JSON;
import com.qhb.community.community.dao.AccessTokenDTO;
import com.qhb.community.community.dao.GitHubUser;
import okhttp3.*;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class GitHubProvider {
    public String getAccessToken(AccessTokenDTO accessTokenDTO){
       MediaType mediaType = MediaType.get("application/json; charset=utf-8");

        OkHttpClient client = new OkHttpClient();

        RequestBody body = RequestBody.create(mediaType,JSON.toJSONString(accessTokenDTO));
        Request request = new Request.Builder()
                .url("https://github.com/login/oauth/access_token")
                .post(body)
                .build();
        try (Response response = client.newCall(request).execute()) {
         String string= response.body().string();
           String token=string.split("&")[0].split("=")[1];
           return token;
            } catch (Exception e) {
            e.printStackTrace();
            }
        return null;
    }
    public GitHubUser getUser(String accessToken){
        OkHttpClient client=new OkHttpClient();
        Request request=new Request.Builder().
                url("https://api.github.com/user?access_token="+accessToken).build();
        try {
            Response response=client.newCall(request).execute();
         String string=response.body().string();
            GitHubUser gitHubUser = JSON.parseObject(string, GitHubUser.class);//将String Json类对象自动转换为java类对象不用手动去解析
        return gitHubUser;
        } catch (IOException e) {
            return null;
        }
    }
}
