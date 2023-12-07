package io.mountblue.redditclone.controller;

import io.mountblue.redditclone.entity.Post;
import io.mountblue.redditclone.service.PostService;
import io.mountblue.redditclone.service.VoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class VoteController {
    private final VoteService voteService;
    private final PostService postService;

    @Autowired
    VoteController(VoteService voteService, PostService postService){
        this.voteService = voteService;
        this.postService = postService;
    }

    @PostMapping("/processPostVote")
    public String processVoteCountOnPost(@RequestParam("vote") Integer vote,
                                         @RequestParam("postId") Integer postId,
                                         @RequestParam("endPoint") String endPoint,
                                         @RequestParam(name = "sort", defaultValue = "Top") String sort,
                                         @AuthenticationPrincipal UserDetails userDetails) {
        String username = userDetails.getUsername();
        int voteCountChange = voteService.getChangeInVoteCount(username,postId,vote );

        postService.updatePost(postId, voteCountChange);

        return "redirect:" + endPoint + "?sort=" + sort;
    }
}
