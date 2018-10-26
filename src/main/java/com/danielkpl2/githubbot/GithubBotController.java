package com.danielkpl2.githubbot;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Controller
public class GithubBotController {
    @Value("${oauth}")
    private String oauth;
    @Value("${repository}")
    private String repository;

    @RequestMapping(value="/", method=RequestMethod.POST)
    public ResponseEntity<String> githubEvent(@RequestBody Map<String, Object> payload){

        String action = (String) payload.get("action");
        if(action.equals("created")) {
            Map<String, Object> comment = (Map<String, Object>) payload.get("comment");
            Map<String, Object> issue = (Map<String, Object>) payload.get("issue");

            System.out.printf("issue %s comment: %s\n", issue.get("number"), comment.get("body"));
            String commentBody = (String) comment.get("body");

            if (commentBody.startsWith("@bot say-hello")) {
                return new ResponseEntity<String>(makeComment("hello world", (int)issue.get("number")));
            }
            return new ResponseEntity<>(HttpStatus.ACCEPTED);
        }else return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    private HttpStatus makeComment(String comment, int pullRequestNo){
        RestTemplate restTemplate = new RestTemplate();
        String requestBody = "{\"body\":\"" + comment + "\"}";
        String url = "https://api.github.com/repos/" +
                repository +
                "/issues/" +
                pullRequestNo +
                "/comments";

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "token " + oauth);
        HttpEntity<String> entity = new HttpEntity<>(requestBody, headers);

        ResponseEntity<String> responseEntity = restTemplate.postForEntity(
                url,
                entity,
                String.class);

        return responseEntity.getStatusCode();
    }
}
