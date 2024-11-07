package gdsc.practice.question.domain;

import gdsc.practice.user.domain.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

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
}
