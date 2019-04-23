package com.home.sarafan.controllers;

import com.fasterxml.jackson.annotation.JsonView;
import com.home.sarafan.domain.Message;
import com.home.sarafan.domain.Views;
import com.home.sarafan.repositories.MessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("message")
@RequiredArgsConstructor
public class MessageController {

    private final MessageRepository messageRepository;

    @GetMapping
    @JsonView(Views.IdName.class)
    public List<Message> listMessages() {
        return messageRepository.findAll();
    }

    @GetMapping("{id}")
    @JsonView(Views.FullMessage.class)
    public Message getMessage(@PathVariable("id") Message message) {
        return message;
    }

    @PostMapping
    public Message createMessage(@RequestBody Message message) {
        message.setCreationDate(LocalDateTime.now());
        return messageRepository.save(message);
    }

    @PutMapping("{id}")
    public Message updateMessage(@PathVariable("id") Message messageFromDb, @RequestBody Message message) {

        BeanUtils.copyProperties(message, messageFromDb, "id");

        return messageRepository.save(messageFromDb);
    }

    @DeleteMapping("{id}")
    public void deleteMessage(@PathVariable("id") Message message) {
        messageRepository.delete(message);
    }
}
