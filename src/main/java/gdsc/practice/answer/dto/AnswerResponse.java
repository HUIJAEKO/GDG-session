package gdsc.practice.answer.dto;

import gdsc.practice.answer.domain.Answer;
import lombok.Builder;

import java.util.List;

@Builder
public record AnswerResponse(String author, Long authorId, String content) {
    public static List<AnswerResponse> toResponses(List<Answer> answers) {
        return answers.stream().map(answer -> AnswerResponse.builder()
                        .content(answer.getContent())
                        .authorId(answer.getAuthor().getId())
                        .author(answer.getAuthor().getUsername())
                        .build())
                .toList();
    }
}
