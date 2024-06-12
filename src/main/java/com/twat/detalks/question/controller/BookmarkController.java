package com.twat.detalks.question.controller;

import com.twat.detalks.member.dto.ResDto;
import com.twat.detalks.oauth2.dto.CustomUserDetail;
import com.twat.detalks.question.dto.BookmarkedQuestionDto;
import com.twat.detalks.question.dto.QuestionDto;
import com.twat.detalks.question.entity.BookmarkEntity;
import com.twat.detalks.question.service.BookmarkService;
import com.twat.detalks.question.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/bookmarks")
public class BookmarkController {

    @Autowired
    private BookmarkService bookmarkService;

    @Autowired
    private QuestionService questionService;

    // 북마크 추가
    // POST /api/bookmarks/{questionId}
    @PostMapping("/{questionId}")
    public ResponseEntity<ResDto> addBookmark(
            @PathVariable Long questionId,
            @AuthenticationPrincipal CustomUserDetail user) {
        if (user != null) {
            Long memberIdx = Long.valueOf(user.getUserIdx());
            bookmarkService.addBookmark(memberIdx, questionId);
            // return ResponseEntity.ok().build();
            return ResponseEntity.ok().body(
                    ResDto.builder()
                            .msg("북마크 추가 성공")
                            .result(true)
                            .status("200")
                            .build());
        } else {
            return ResponseEntity.status(401).body(
                    ResDto.builder()
                    .msg("로그인을 해주세요.")
                    .result(false)
                    .status("401")
                    .build());
        }
    }

    // 북마크 삭제
    // DELETE /api/bookmarks/{questionId}
    @DeleteMapping("/{questionId}")
    public ResponseEntity<ResDto> removeBookmark(
            @PathVariable Long questionId,
            @AuthenticationPrincipal CustomUserDetail user) {
        if (user != null) {
            Long memberIdx = Long.valueOf(user.getUserIdx());
            bookmarkService.removeBookmark(memberIdx, questionId);
            return ResponseEntity.ok().body(
                    ResDto.builder()
                            .msg("북마크 삭제 성공")
                            .result(true)
                            .status("200")
                            .build());
        } else {
            return ResponseEntity.status(401).body(
                    ResDto.builder()
                            .msg("로그인을 해주세요.")
                            .result(false)
                            .status("401")
                            .build());
        }
    }

    // 북마크 리스트 조회
    // GET /api/bookmarks
    @GetMapping
    public ResponseEntity<List<QuestionDto>> getBookmarks(@AuthenticationPrincipal CustomUserDetail user) {
        if (user != null) {
            Long memberIdx = Long.valueOf(user.getUserIdx());
            List<BookmarkEntity> bookmarks = bookmarkService.getBookmarksByMember(memberIdx);
            List<QuestionDto> bookmarkedQuestions = bookmarks.stream()
                    .map(bookmark -> questionService.convertToDTO(bookmark.getQuestion(), bookmark.getBookmarkState()))
                    .collect(Collectors.toList());
            return ResponseEntity.ok(bookmarkedQuestions);
        } else {
            return ResponseEntity.status(401).build();  // Unauthorized
        }
    }
}
