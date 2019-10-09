package com.ty.expei.provider;

import com.alibaba.fastjson.JSON;
import com.ty.expei.dto.AccessTokenDto;
import com.ty.expei.dto.GitHubUser;
import okhttp3.*;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

@Component
public class GitHubprovider {
    public String getaccesstoken(AccessTokenDto accessTokenDto){
            MediaType mediaTypee = MediaType.get("application/json; charset=utf-8");
            OkHttpClient client = new OkHttpClient();

            RequestBody body = RequestBody.create(mediaTypee,JSON.toJSONString(accessTokenDto));
            System.out.println(JSON.toJSONString(accessTokenDto));
            Request request = new Request.Builder()
                    .url("https://github.com/login/oauth/access_token")
                    .post(body)
                    .build();
            try (Response response = client.newCall(request).execute()) {
                String string= response.body().string();
                String [] split=string.split("&");
                String tokstring=split[0];
                String token=tokstring.split("=")[1];
                System.out.println(string);
               // System.out.println(token);
                return token;
            } catch (IOException e) {
                e.printStackTrace();

            }
          return "123";

    }
    public GitHubUser getuser(String accesstoken)  {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url("https://api.github.com/user?access_token="+accesstoken)
                .build();
        System.out.println(request.url());
        try {
            Response response = client.newCall(request).execute();
            String string=response.body().string();
            System.out.println(string);
            GitHubUser gitHubUser=JSON.parseObject(string,GitHubUser.class);
            System.out.println(gitHubUser.getId());
            return gitHubUser;
        } catch (IOException e) {
            e.printStackTrace();
        }
      return null;

    }

    }

