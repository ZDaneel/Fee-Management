package com.usts.feeback.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.usts.feeback.dto.StudentDTO;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

import static com.usts.feeback.utils.Constants.SESSION_STUDENT_DTO;

/**
 * 登陆拦截器
 * @author leenadz
 * @since 2022-12-08 16:09
 */
public class LoginInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        HttpSession session = request.getSession();
        StudentDTO studentDTO = (StudentDTO) session.getAttribute(SESSION_STUDENT_DTO);
        if (studentDTO == null) {
            falseResult(response);
            return false;
        }
        StudentHolder.saveStudent(studentDTO);
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        StudentHolder.removeStudent();
    }

    public void falseResult(HttpServletResponse response) throws IOException {
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json; charset=utf-8");
        Result<String> resultBody = Result.error(ExceptionCodeEnum.NEED_LOGIN, "请先登陆!");
        ObjectMapper objectMapper = new ObjectMapper();
        response.getWriter().println(objectMapper.writeValueAsString(resultBody));
    }
}
