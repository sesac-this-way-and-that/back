package com.twat.detalks.question.service;

import com.twat.detalks.answer.dto.AnswerDto;
import com.twat.detalks.answer.entity.AnswerEntity;
import com.twat.detalks.answer.repository.AnswerRepositroy;
import com.twat.detalks.mypage.dto.QuestionListDto;
import com.twat.detalks.question.QuestionSpecification;
import com.twat.detalks.question.dto.MemberQuestionDto;
import com.twat.detalks.question.dto.QuestionDto;
import com.twat.detalks.question.entity.QuestionEntity;
import com.twat.detalks.question.repository.QuestionRepository;
import com.twat.detalks.tag.entity.QuestionTagEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class QuestionSearchService {
    private final QuestionRepository questionRepository;
    private final AnswerRepositroy answerRepository;

    public Page<QuestionDto> searchQuestions(String title, String content, String tag, String name,
                                             int pageNo,
                                             int pageSize,
                                             String sortBy) {
        // 기본 정렬 : 최신순, 그 외 경우에 투표순
        // Sort sort = "voteCount".equals(sortBy) ? Sort.by(Sort.Direction.DESC, "voteCount") : Sort.by(Sort.Direction.DESC, sortBy);
        Sort sort = Sort.by(Sort.Direction.DESC, sortBy);
        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);

        Specification<QuestionEntity> spec = Specification.where(null);

        if (title != null && !title.isEmpty()) {
            spec = spec.and(QuestionSpecification.hasTitle(title));
        }
        if (content != null && !content.isEmpty()) {
            spec = spec.and(QuestionSpecification.hasContent(content));
        }
        if (tag != null && !tag.isEmpty()) {
            spec = spec.and(QuestionSpecification.hasTag(tag));
        }
        if(name != null && !name.isEmpty()){
            spec = spec.and(QuestionSpecification.hasName(name));
        }

        Page<QuestionEntity> page = questionRepository.findAll(spec, pageable);
        return page.map(this::convertToDto);
    }

    private QuestionDto convertToDto(QuestionEntity question) {
        List<AnswerDto> answerDtos = answerRepository.findByQuestions_QuestionId(question.getQuestionId())
                .stream()
                .map(this::convertAnswerToDto)
                .collect(Collectors.toList());

        List<String> tagNames = question.getQuestionTagList().stream()
                .map(QuestionTagEntity::getTags)
                .map(tag -> tag.getTagName())
                .collect(Collectors.toList());

        return QuestionDto.builder()
                .questionId(question.getQuestionId())
                .questionTitle(question.getQuestionTitle())
                .questionContent(question.getQuestionContent())
                .createdAt(question.getCreatedAt())
                .modifiedAt(question.getModifiedAt())
                .viewCount(question.getViewCount())
                .voteCount(question.getVoteCount())
                .questionState(question.getQuestionState())
                .isSolved(question.getIsSolved())
                .author(new MemberQuestionDto(question.getMembers().getMemberIdx(), question.getMembers().getMemberName(), question.getMembers().getMemberImg(), question.getMembers().getMemberRep()))
                .answerList(answerDtos)
                .tagNameList(tagNames)
                .build();
    }

    private AnswerDto convertAnswerToDto(AnswerEntity answer) {
        return AnswerDto.builder()
                .answerId(answer.getAnswerId())
                .answerContent(answer.getAnswerContent())
                .createdAt(answer.getCreatedAt())
                .isSelected(answer.getIsSelected())
                .author(new MemberQuestionDto(answer.getMembers().getMemberIdx(), answer.getMembers().getMemberName(), answer.getMembers().getMemberImg(), answer.getMembers().getMemberRep()))
                .build();
    }
}
