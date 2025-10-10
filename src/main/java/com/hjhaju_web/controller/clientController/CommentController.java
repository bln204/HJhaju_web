package com.hjhaju_web.controller.clientController;


import com.hjhaju_web.dto.CommentDTO;
import com.hjhaju_web.service.UserService;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;

@Controller
public class CommentController {
    private final UserService userService;

    public CommentController(UserService userService) {
        this.userService = userService;
    }

    @MessageMapping("/comic/{comicId}/comment")
    @SendTo("/topic/comic/{comicId}/comments")
    public CommentDTO addComment(@DestinationVariable String comicId, CommentPayload payload, Authentication authentication) {
        System.out.println("Message received for /comic/" + comicId + "/comment");
        try {
            if (authentication == null || !authentication.isAuthenticated()) {
                System.out.println("User not authenticated for comicId: " + comicId);
                throw new IllegalStateException("User not authenticated");
            }
            System.out.println("Received comment for comicId: " + comicId + ", content: " + payload.getContent() + ", user: " + authentication.getName() + ", parentId: " + payload.getParentId());
            String userEmail = authentication.getName();
            CommentDTO comment = userService.addComment(comicId, payload.getContent(), userEmail, payload.getParentId());
            System.out.println("Comment saved: " + comment.getId());
            return comment;
        } catch (Exception e) {
            System.err.println("Error processing comment for comicId: " + comicId + ", error: " + e.getMessage());
            throw e;
        }
    }

    public static class CommentPayload {
        private String content;
        private Long parentId; // Thêm trường parentId cho reply

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public Long getParentId() {
            return parentId;
        }

        public void setParentId(Long parentId) {
            this.parentId = parentId;
        }
    }
}