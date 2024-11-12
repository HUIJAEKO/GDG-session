package gdsc.practice.question.domain;

import gdsc.practice.answer.domain.Answer;
import gdsc.practice.user.domain.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Table(name = "questions")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Question {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String subject;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id")
    private User anthor;

    @OneToMany(mappedBy = "question")
    private List<Answer> answers = new ArrayList<>();

    public Question(String subject, String content, User anthor) {
        this.subject = subject;
        this.content = content;
        this.anthor = anthor;
    }

    public void update(
            String subject,
            String content
    ) {
        this.subject = subject;
        this.content = content;
    }

    public void addAnswer(Answer answer){
        answers.add(answer);
    }
}
