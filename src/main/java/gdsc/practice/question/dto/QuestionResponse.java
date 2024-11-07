package gdsc.practice.question.dto;

public record QuestionResponse(
        String subject,
        String content,
        Long authorId,
        String author
) {
}
