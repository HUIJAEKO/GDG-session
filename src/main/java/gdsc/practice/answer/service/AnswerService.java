package gdsc.practice.answer.service;

import gdsc.practice.answer.domain.Answer;
import gdsc.practice.answer.dto.AnswerRequest;
import gdsc.practice.answer.repository.AnswerRepository;
import gdsc.practice.question.domain.Question;
import gdsc.practice.question.repository.QuestionRepository;
import gdsc.practice.user.domain.User;
import gdsc.practice.user.dto.UserInfo;
import gdsc.practice.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AnswerService {
    private final AnswerRepository answerRepository;
    private final UserRepository userRepository;
    private final QuestionRepository questionRepository;

    public Long saveAnswer(UserInfo userInfo, AnswerRequest answerRequest){
        User author = userRepository.findById(userInfo.getId())
                .orElseThrow(() -> new RuntimeException("존재하지 않는 회원입니다"));
        Question question = questionRepository.findById(answerRequest.questionId())
                .orElseThrow(() -> new RuntimeException("존재하지 않는 질문입니다."));
        Answer answer = new Answer(answerRequest.content(), author, question);
        Answer savedAnswer = answerRepository.save(answer);
        question.addAnswer(savedAnswer);
        return savedAnswer.getId();
    }

    public Long modifyAnswer(UserInfo userInfo, AnswerRequest answerRequest, Long answerId){
        User author = userRepository.findById(userInfo.getId())
                .orElseThrow(() -> new RuntimeException("존재하지 않는 회원입니다"));
        Answer answer = answerRepository.findById(answerId)
                .orElseThrow(() -> new RuntimeException("존재하지 않는 답변입니다."));
        answer.update(answerRequest.content());
        return answer.getId();
    }

    public Long deleteAnswer(UserInfo userInfo, Long answerId) {
        User author = userRepository.findById(userInfo.getId())
                .orElseThrow(() -> new RuntimeException("존재하지 않는 회원입니다"));
        return answerRepository.findById(answerId)
                .map(answer -> {
                    answerRepository.delete(answer);
                    return answer.getId();
                })
                .orElseThrow(() -> new RuntimeException("존재하지 않는 답변입니다"));
    }
}
