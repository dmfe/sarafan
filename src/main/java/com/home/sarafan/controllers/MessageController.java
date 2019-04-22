package com.home.sarafan.controllers;

import com.home.sarafan.exceptions.ObjectNotFoundException;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

@RestController
@RequestMapping("message")
public class MessageController {

    private final AtomicInteger count = new AtomicInteger(4);

    private final List<Map<String, String>> messages = Collections.synchronizedList(
            new ArrayList<Map<String, String>>() {{
                add(new HashMap<String, String>() {{
                    put("id", "1");
                    put("text", "First message");
                }});

                add(new HashMap<String, String>() {{
                    put("id", "2");
                    put("text", "Second message");
                }});

                add(new HashMap<String, String>() {{
                    put("id", "3");
                    put("text", "Third message");
                }});
            }});

    @GetMapping
    public List<Map<String, String>> listMessages() {
        return messages;
    }

    @GetMapping("{id}")
    public Map<String, String> getMessage(@PathVariable String id) {
        return getMessageById(id);
    }

    @PostMapping
    public Map<String, String> createMessage(@RequestBody Map<String, String> message) {
        message.put("id", String.valueOf(count.getAndIncrement()));

        messages.add(message);

        return message;
    }

    @PutMapping("{id}")
    public Map<String, String> updateMessage(
            @PathVariable String id,
            @RequestBody Map<String, String> message) {

        Map<String, String> messageFromDb = getMessageById(id);
        messageFromDb.putAll(message);
        messageFromDb.put("id", id);

        return messageFromDb;
    }

    @DeleteMapping("{id}")
    public void deleteMessage(@PathVariable String id) {

        Map<String, String> message = getMessageById(id);
        messages.remove(message);
    }

    private Map<String, String> getMessageById(String id) {
        return messages.stream()
                .filter(message -> message.get("id").equals(id))
                .findFirst().orElseThrow(() -> new ObjectNotFoundException("Unable to found object with id: " + id));
    }
}