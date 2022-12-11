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
@Api(tags = "评论信息")
@RestController
@RequestMapping("/comment")
public class CommentController {
    // TODO 新增评论时判断是否已经确认

    @Resource
    private CommentService commentService;

    @ApiOperation(value = "开支id所对应的未关闭评论列表")
    @GetMapping("/open-comments/{feeId}")
    public Result<List<Comment>> queryOpenComments(@PathVariable("feeId") Integer feeId) {
        return Result.success(commentService.queryOpenParentComments(feeId));
    }

    @ApiOperation(value = "新增父级评论")
    @PostMapping("/parent-comment")
    public Result<Boolean> insertParentComment(@RequestBody Comment comment) {
        return commentService.insertParentComment(comment);
    }

}
