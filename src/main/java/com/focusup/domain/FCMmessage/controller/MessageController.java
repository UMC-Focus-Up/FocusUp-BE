package com.focusup.domain.FCMmessage.controller;


import com.focusup.domain.FCMmessage.dto.RequestDto;
import com.focusup.domain.FCMmessage.service.FirebaseCloudMessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/fcm")
public class MessageController {
    private final FirebaseCloudMessageService firebaseCloudMessageService;

    @PostMapping("/")
    public ResponseEntity pushMessage(@RequestBody RequestDto requestDTO) throws IOException {
        System.out.println(requestDTO.getTargetToken() + " "
                +requestDTO.getTitle() + " " + requestDTO.getBody());

        firebaseCloudMessageService.sendMessageTo(
                requestDTO.getTargetToken(),
                requestDTO.getTitle(),
                requestDTO.getBody());
        return ResponseEntity.ok().build();
    }
}
