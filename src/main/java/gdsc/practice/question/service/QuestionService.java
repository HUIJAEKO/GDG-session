package gdsc.practice.question.service;

import gdsc.practice.answer.domain.Answer;
import gdsc.practice.answer.dto.AnswerResponse;
import gdsc.practice.answer.exception.BusinessException;
import gdsc.practice.answer.exception.ErrorCode;
import gdsc.practice.question.domain.Question;
import gdsc.practice.question.dto.QuestionRequest;
import gdsc.practice.question.dto.QuestionResponse;
import gdsc.practice.question.dto.SearchDto;
import gdsc.practice.question.repository.QuestionQueryRepository;
import gdsc.practice.question.repository.QuestionRepository;
import gdsc.practice.user.domain.User;
import gdsc.practice.user.dto.UserInfo;
import gdsc.practice.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class QuestionService {
    private final QuestionRepository questionRepository;
    private final UserRepository userRepository;
    private final QuestionQueryRepository queryRepository;

    // TODO : 페이징

    // 2. 질문 저장
    public Long saveQuestion(UserInfo userInfo, QuestionRequest questionRequest){
        User author = userRepository.findById(userInfo.getId())
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND_USER_EXCEPTION));
        Question question = new Question(questionRequest.subject(), questionRequest.content(), author);
        Question savedQuestion = questionRepository.save(question);
        return savedQuestion.getId();
    }

    // 3. 질문 수정
    public Long modifyQuestion(
            UserInfo userInfo,
            QuestionRequest questionRequest,
            Long questionId
    ){
        User author = userRepository.findById(userInfo.getId())
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND_USER_EXCEPTION));
        Question question = questionRepository.findById(questionId)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_EXIST_QUESTION_EXCEPTION));
        question.update(questionRequest.subject(), questionRequest.content());
        questionRepository.save(question);
        return question.getId();

    }

    // 4. 질문 삭제
    public Long deleteQuestion(
            UserInfo userInfo,
            Long questionId
    ) {
        User author = userRepository.findById(userInfo.getId())
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND_USER_EXCEPTION));
        return questionRepository.findById(questionId)
                .map(question ->{
                    questionRepository.delete(question);
                    return question.getId();
                })
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_EXIST_QUESTION_EXCEPTION));
    }

    public QuestionResponse getQuestion(Long questionId) {
        Question question = questionRepository.findById(questionId)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_EXIST_QUESTION_EXCEPTION));

        List<Answer> answers = question.getAnswers();
        List<AnswerResponse> answerResponses = AnswerResponse.toResponses(answers);

        return QuestionResponse.builder()
                .subject(question.getSubject())
                .content(question.getContent())
                .authorId(question.getAuthor().getId())
                .author(question.getAuthor().getUsername())
                .answers(answerResponses)
                .modifiedDate(question.getModifiedDate())
                .build();
    }

    @Transactional
    public Slice<QuestionResponse> getQuestionPage(Pageable pageable, SearchDto searchDto){
        if(Objects.isNull(searchDto.subject())){
            return queryRepository.getQuestionResponses(pageable);
        }else{
            return queryRepository.getQuestionResponses(pageable, searchDto);
        }
    }
}
