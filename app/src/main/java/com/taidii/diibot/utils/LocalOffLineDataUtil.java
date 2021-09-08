package com.taidii.diibot.utils;

import com.taidii.diibot.entity.school.HealthCheckCacheDate;
import com.taidii.diibot.entity.school.StaffPictureCacheData;
import com.taidii.diibot.entity.school.StaffSignCacheData;
import com.taidii.diibot.entity.school.StudentPictureCacheData;
import com.taidii.diibot.entity.school.StudentSignCacheData;

import java.util.List;

public class LocalOffLineDataUtil {

    public static List<StudentSignCacheData> getStudentSignCacheList(){
        return OrmDBHelper.getInstance().getQueryAll(StudentSignCacheData.class);//学生考勤
    }

    public static List<StaffSignCacheData> getStaffSignCacheList(){
        return OrmDBHelper.getInstance().getQueryAll(StaffSignCacheData.class);//老师考勤
    }

    public static List<StudentPictureCacheData> getsStudentPictureCacheList(){
        return OrmDBHelper.getInstance().getQueryAll(StudentPictureCacheData.class);//学生图片
    }

    public static List<StaffPictureCacheData> getStaffPictureCacheList(){
        return OrmDBHelper.getInstance().getQueryAll(StaffPictureCacheData.class);//老师图片
    }

    public static List<HealthCheckCacheDate> getHealthCheckCacheList(){
        return OrmDBHelper.getInstance().getQueryAll(HealthCheckCacheDate.class);//体测
    }

    public static boolean isExistOffLineData(){
        if (getStudentSignCacheList().size()==0 && getStaffSignCacheList().size()==0&&
            getsStudentPictureCacheList().size()==0 && getStaffPictureCacheList().size()==0
            &&getHealthCheckCacheList().size()==0) {
            return false;
        }else {
            return true;
        }
    }

}
