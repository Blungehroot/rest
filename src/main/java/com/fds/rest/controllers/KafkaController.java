package com.fds.rest.controllers;

import com.fds.rest.model.User;
import com.fds.rest.services.impl.ProducerServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api/v1/kafka")
public class KafkaController {
    private final ProducerServiceImpl producer;

    @Autowired
    KafkaController(ProducerServiceImpl producer) {
        this.producer = producer;
    }

    @PostMapping(value = "/publish")
    public void sendMessageToKafkaTopic(@RequestParam("message") String message) {
        this.producer.sendMessage(message);
    }
}
