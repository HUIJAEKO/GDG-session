package gdsc.practice.question.service;

import gdsc.practice.question.domain.Question;
import gdsc.practice.question.dto.QuestionRequest;
import gdsc.practice.question.repository.QuestionRepository;
import gdsc.practice.user.domain.User;
import gdsc.practice.user.dto.UserInfo;
import gdsc.practice.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class QuestionService {
    private final QuestionRepository questionRepository;
    private final UserRepository userRepository;

    // TODO : 페이징

    // 2. 질문 저장
    public Long saveQuestion(UserInfo userInfo, QuestionRequest questionRequest){
        User author = userRepository.findById(userInfo.getId())
                .orElseThrow(() -> new RuntimeException("존재하지 않은 회원입니다."));
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
                .orElseThrow(() -> new RuntimeException("존재하지 않는 회원입니다."));
        Question question = questionRepository.findById(questionId)
                .orElseThrow(() -> new RuntimeException("존재하지 않는 질문입니다."));
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
                .orElseThrow(() -> new RuntimeException("회원을 찾을 수 없습니다."));
        return questionRepository.findById(questionId)
                .map(question ->{
                    questionRepository.delete(question);
                    return question.getId();
                })
                .orElseThrow(() -> new RuntimeException("존재하지 않는 질문입니다."));
    }

}
