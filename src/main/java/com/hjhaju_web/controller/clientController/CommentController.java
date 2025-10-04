package com.hjhaju_web.controller.clientController;

import com.hjhaju_web.model.Comment;
import com.hjhaju_web.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
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
    public Comment addComment(@DestinationVariable String comicId, CommentPayload payload, Authentication authentication) {
        System.out.println("Message received for /comic/" + comicId + "/comment");
        if (authentication == null || !authentication.isAuthenticated()) {
            System.out.println("User not authenticated for comicId: " + comicId);
            throw new IllegalStateException("User not authenticated");
        }
        System.out.println("Received comment for comicId: " + comicId + ", content: " + payload.getContent() + ", user: " + authentication.getName());
        String userEmail = authentication.getName();
        return userService.addComment(comicId, payload.getContent(), userEmail);
    }

    public static class CommentPayload {
        private String content;

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }
    }
}