package com.wevel.wevel_server.domain.openai;

import com.wevel.wevel_server.domain.openai.dto.ChatGPTRequest;
import com.wevel.wevel_server.domain.openai.dto.ChatGPTResponse;
import com.wevel.wevel_server.domain.openai.dto.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/api/openai")
public class OpenAIController {

    @Value("${openai.model}")
    private String model;

    @Value("${openai.api.url}")
    private String apiURL;

    @Autowired
    private RestTemplate restTemplate;

    @GetMapping("/chat")
    public String chat(@RequestBody String prompt){
        ChatGPTRequest request = new ChatGPTRequest(model, prompt);
        request.getMessages().add(new Message("system", "You are a helpful assistant to analyze the purchase items, quantity, and amount from the receipt and output it in JSON format AND translate Korean"));

        ChatGPTResponse chatGPTResponse = restTemplate.postForObject(apiURL, request, ChatGPTResponse.class);

        String messageContent = chatGPTResponse.getChoices().get(0).getMessage().getContent();

        return messageContent;
    }
}