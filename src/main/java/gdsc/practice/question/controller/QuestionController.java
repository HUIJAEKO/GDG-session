package gdsc.practice.question.controller;

import gdsc.practice.config.argumentresolver.Login;
import gdsc.practice.question.dto.QuestionRequest;
import gdsc.practice.question.dto.QuestionResponse;
import gdsc.practice.question.dto.SearchDto;
import gdsc.practice.question.service.QuestionService;
import gdsc.practice.user.dto.UserInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/question")
public class QuestionController {
    private final QuestionService questionService;

    // 2. 질문 등록
    @PostMapping
    public ResponseEntity<Long> createQuestion(
            @Login UserInfo userInfo,
            @RequestBody QuestionRequest questionRequest
    ){
        Long questionId = questionService.saveQuestion(userInfo, questionRequest);
        return ResponseEntity.ok(questionId);
    }

    // 3. 질문 수정
    @PatchMapping("/{questionId}")
    public ResponseEntity<Long> modifyQuestion(
            @Login UserInfo userInfo,
            @PathVariable Long questionId,
            @RequestBody QuestionRequest questionRequest
    ) {
        Long updatedQuestionId = questionService.modifyQuestion(userInfo, questionRequest, questionId);
        return ResponseEntity.ok(updatedQuestionId);
    }

    // 4. 질문 삭제
    @DeleteMapping("/{questionId}")
    public ResponseEntity<Long> deleteQuestion(
            @Login UserInfo userInfo,
            @PathVariable Long questionId
    ) {
        Long deletedQuestionId = questionService.deleteQuestion(userInfo, questionId);
        return ResponseEntity.ok(deletedQuestionId);
    }

    @GetMapping
    public ResponseEntity<Slice<QuestionResponse>> getQuestionPage(
            @PageableDefault final Pageable pageable,
            @RequestParam SearchDto searchDto
    ) {
        Slice<QuestionResponse> questionPage = questionService.getQuestionPage(pageable, searchDto);
        return ResponseEntity.ok(questionPage);
    }
}
