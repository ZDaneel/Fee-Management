package com.usts.feeback.controller;

import com.usts.feeback.pojo.Comment;
import com.usts.feeback.pojo.Fee;
import com.usts.feeback.service.CommentService;
import com.usts.feeback.utils.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author leenadz
 * @since 2022-12-10 20:47
 */
@Api(tags = "评论信息接口")
@RestController
@RequestMapping("/comment")
public class CommentController {
    @Resource
    private CommentService commentService;

    @ApiOperation(value = "查询开支id所对应的未关闭评论列表, 查询参数为名称")
    @GetMapping(value = {"/open-comments/{feeId}/{title}", "/open-comments/{feeId}"})
    public Result<List<Comment>> queryOpenComments(@PathVariable("feeId") Integer feeId,
                                                   @PathVariable(value = "title", required = false) String title) {
        return Result.success(commentService.queryOpenParentComments(feeId, title));
    }

    @ApiOperation(value = "查询开支id所对应的已关闭评论列表, 查询参数为名称")
    @GetMapping(value = {"/closed-comments/{feeId}/{title}", "/closed-comments/{feeId}"})
    public Result<List<Comment>> queryClosedFees(@PathVariable("feeId") Integer feeId,
                                             @PathVariable(value = "title", required = false) String title) {
        return Result.success(commentService.queryClosedParentComments(feeId, title));
    }

    @ApiOperation(value = "新增父级评论")
    @PostMapping("/parent-comment")
    public Result<Boolean> insertParentComment(@RequestBody Comment comment) {
        return commentService.insertParentComment(comment);
    }

    @ApiOperation(value = "查询评论id所对应的评论和回复")
    @GetMapping("/parent-comment/{commentId}")
    public Result<Comment> queryParentComment(@PathVariable("commentId") Integer commentId) {
        return Result.success(commentService.queryParentComment(commentId));
    }

    @ApiOperation(value = "新增子级评论(回复)")
    @PostMapping("/child-comment")
    public Result<Boolean> insertChildComment(@RequestBody Comment comment) {
        return commentService.insertChildComment(comment);
    }

    @ApiOperation(value = "确认该条质疑")
    @GetMapping("/confirm/{commentId}")
    public Result<Boolean> confirmComment(@PathVariable("commentId") Integer commentId) {
        return commentService.confirmComment(commentId);
    }

}
