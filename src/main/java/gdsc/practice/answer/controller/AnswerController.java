package gdsc.practice.answer.controller;

import gdsc.practice.answer.dto.AnswerRequest;
import gdsc.practice.answer.service.AnswerService;
import gdsc.practice.config.argumentresolver.Login;
import gdsc.practice.user.dto.UserInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/answer")
public class AnswerController {
    private final AnswerService answerService;

    @PostMapping
    public ResponseEntity<Long> createAnswer(
            @Login UserInfo userInfo,
            @RequestBody AnswerRequest answerRequest
    ){
        Long answerId = answerService.saveAnswer(userInfo, answerRequest);
        return ResponseEntity.ok(answerId);
    }

    @PatchMapping("/{answerId}")
    public ResponseEntity<Long> modifyAnswer(
            @PathVariable Long answerId,
            @Login UserInfo userInfo,
            @RequestBody AnswerRequest answerRequest
    ){
        Long updatedAnswerId = answerService.modifyAnswer(userInfo, answerRequest, answerId);
        return ResponseEntity.ok(updatedAnswerId);
    }

    @DeleteMapping("/{answerId}")
    public ResponseEntity<Long> deleteAnswer(
            @Login UserInfo userInfo,
            @PathVariable Long answerId
    ){
        Long deletedAnswerId = answerService.deleteAnswer(userInfo, answerId);
        return ResponseEntity.ok(deletedAnswerId);
    }
}
