package gdsc.practice.question.repository;

import com.querydsl.core.types.ConstructorExpression;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import gdsc.practice.answer.dto.AnswerResponse;
import gdsc.practice.question.domain.Question;
import gdsc.practice.question.dto.QuestionResponse;
import gdsc.practice.question.dto.SearchDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

import static gdsc.practice.question.domain.QQuestion.question;

@Slf4j
@Repository
@RequiredArgsConstructor
public class QuestionQueryRepository {

    private final JPAQueryFactory queryFactory;

    // 검색어 없는 경우의 조회
    public Slice<QuestionResponse> getQuestionResponses(Pageable pageable) {

        // Question 엔티티 목록 조회
        List<Question> questions = queryFactory // QueryDSL 사용
                .selectFrom(question)
                .orderBy(question.modifiedDate.desc())
                .offset(pageable.getOffset()) // 페이지 시작 위치
                .limit(pageable.getPageSize() + 1) // 페이지 크기 + 1개 조회 (다음 페이지 존재 여부 확인용)
                .fetch(); // 쿼리 실행 및 결과 반환

        // Question 엔티티를 QuestionResponse DTO로 변환
        List<QuestionResponse> result = questions.stream()
                .map(q -> new QuestionResponse(
                        q.getSubject(),
                        q.getSubject(),
                        q.getAuthor().getId(),
                        q.getAuthor().getUsername(),
                        AnswerResponse.toResponses(q.getAnswers()),
                        q.getModifiedDate()))
                .collect(Collectors.toList());
        return getSlicedResponse(pageable, result); // 페이징 처리된 결과 반환
    }

    // 검색어 있는 경우의 조회
    public Slice<QuestionResponse> getQuestionResponses(
            Pageable pageable,
            SearchDto searchDto
    ) {

        // Question 엔티티 목록 조회 (검색 조건 포함)
        List<Question> questions = queryFactory
                .selectFrom(question)
                .where(question.subject.contains(searchDto.subject()))  // 제목에 검색어 포함
                .orderBy(question.modifiedDate.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize() + 1)
                .fetch();
        List<QuestionResponse> result = questions.stream()
                .map(q -> new QuestionResponse(
                        q.getSubject(),
                        q.getSubject(),
                        q.getAuthor().getId(),
                        q.getAuthor().getUsername(),
                        AnswerResponse.toResponses(q.getAnswers()),
                        q.getModifiedDate()))
                .collect(Collectors.toList());
        return getSlicedResponse(pageable, result);
    }

    // 페이징 처리된 응답 생성
    private SliceImpl<QuestionResponse> getSlicedResponse(
            Pageable pageable,
            List<QuestionResponse> responses
    ) {
        int pageSize = pageable.getPageSize(); // 페이지 크기
        boolean hasNext = responses.size() > pageSize; // 다음 페이지 존재 여부 확인

        // 실제 페이지 크기만큼 데이터 잘라내기
        List<QuestionResponse> slicedResponses = hasNext ? responses.subList(0, pageSize) : responses;

        // 페이징 처리된 결과 반환
        return new SliceImpl<>(slicedResponses, pageable, hasNext);
    }

    private ConstructorExpression<QuestionResponse> getQuestionResponseConstructorExpression() {
        return Projections.constructor(
                QuestionResponse.class,
                question.subject,
                question.content,
                question.author.id,
                question.author.username,
                question.answers,
                question.modifiedDate
        );
    }
}
