package com.usts.feeback.utils;

import com.usts.feeback.dto.StudentDTO;
import com.usts.feeback.pojo.Student;

/**
 * @author leenadz
 * @since 2022-12-08 16:06
 */
public class StudentHolder {
    private static final ThreadLocal<StudentDTO> TL = new ThreadLocal<>();

    public static void saveStudent(StudentDTO student){
        TL.set(student);
    }

    public static StudentDTO getStudent(){
        return TL.get();
    }

    public static void removeStudent(){
        TL.remove();
    }
}
