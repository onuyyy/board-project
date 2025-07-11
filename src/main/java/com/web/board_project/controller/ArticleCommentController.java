package com.web.board_project.controller;

import com.web.board_project.dto.UserAccountDto;
import com.web.board_project.dto.request.ArticleCommentRequest;
import com.web.board_project.service.ArticleCommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequiredArgsConstructor
@RequestMapping("/comments")
@Controller
public class ArticleCommentController {

    private final ArticleCommentService articleCommentService;

    @PostMapping("/new")
    public String postNewArticleComment(ArticleCommentRequest articleCommentRequest){

        // todo : 인증 정보를 넣어줘야 한다.
        articleCommentService.saveArticleComment(articleCommentRequest.toDto(UserAccountDto.of(
                "onuy","1234","onuy@email.com","onuy","memo")));

        // 댓글을 작성한 게시글에 머물러야 하기 때문에 식별자 필요
        return "redirect:/articles/" + articleCommentRequest.articleId();
    }

    // 삭제
    // form 태그는 get과 post밖에 없다
    @PostMapping("/{commentId}/delete")
    public String deleteArticleComment(@PathVariable Long commentId, Long articleId){

        articleCommentService.deleteArticleComment(commentId);
        // 어느 게시글에 댓글인지 알아야 한다
        // 댓글 삭제하면 목록으로 가는 게 아니라 댓글을 삭제한 게시글에 머물러 있어야 하기 때문에 식별자를 넘겨 받아야 한다
        return "redirect:/articles/" + articleId;
    }
}
