package com.danielkpl2.githubbot;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = GithubBotApplication.class)
public class GithubBotTest {
    private MockMvc mockMvc;
    @Autowired
    private WebApplicationContext wac;

    private String validComment = "{\"action\": \"created\", \"issue\": {\"number\":1}, \"comment\":{\"body\":\"hello\"}}";
    private String botComment = "{\"action\": \"created\", \"issue\": {\"number\":1}, \"comment\":{\"body\":\"@bot say-hello\"}}";
    private String invalidComment = "{\"action\": \"edited\", \"issue\": {\"number\":1}, \"comment\":{\"body\":\"hello\"}}";

    @Before
    public void setup(){
        mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();

    }

    private ResultActions makePost(String content) throws Exception {
        return mockMvc.perform(post("/").content(content).contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON));
    }

    @Test
    public void normalComment() throws Exception {
        makePost(validComment).andExpect(status().isAccepted());
    }

    @Test
    public void commandComment() throws Exception {
        makePost(botComment).andExpect(status().isCreated());
    }

    @Test
    public void invalidContent() throws Exception {
        makePost(invalidComment).andExpect(status().isBadRequest());
    }
}
