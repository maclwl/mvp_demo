package com.taidii.diibot.app;

import android.text.TextUtils;

import com.taidii.diibot.BuildConfig;
import com.taidii.diibot.module.debug.DebugUrlModifyActivity;
import com.taidii.diibot.utils.SharePrefUtils;

public final class ApiContainer {

//    public static final String Base_Url_CN = "http://192.168.51.59:8200";
//    public static final String Base_Url_CN = "https://dev.taidii.cn";
    public static final String Base_Url_CN = "https://www.taidii.cn";
    public static final String Base_Url_EN = "https://v2.taidii.com";
    public static String API_HOST;

    static {
        final String debugApiHost = SharePrefUtils.getString
                (DebugUrlModifyActivity.DEBUG_URL_SAVE_KEY, "");
        /*debug模式并国内外模式切换处理*/
        if (!BuildConfig.DEBUG) {
            if (SharePrefUtils.getBoolean(Constant.IS_ABROAD,false)){
                API_HOST = Base_Url_EN;
            }else {
                API_HOST = Base_Url_CN;
            }
        } else {
            if (SharePrefUtils.getBoolean(Constant.IS_ABROAD,false)){
                API_HOST = TextUtils.isEmpty(debugApiHost) ? Base_Url_EN : debugApiHost;
            }else {
                API_HOST = TextUtils.isEmpty(debugApiHost) ? Base_Url_CN : debugApiHost;
            }
        }
    }

    public static final String GET_CENTER_TYPE= "/localapp/get_center_type/?username=%1$s&password=%2$s";

    public static final String GET_LOCAL_SCHOOL_DATA= "/localapp/v1/records/?username=%1$s&password=%2$s";

    public static final String STUDENT_SIGN_REQUEST= "/localapp/v1/attendance/student/add/?username=%1$s&password=%2$s&json=%3$s";

    public static final String STAFF_SIGN_REQUEST= "/localapp/v1/attendance/staff/add/?username=%1$s&password=%2$s&json=%3$s";

    public static final String GET_STUDENT_SIGN_HISTORY= "/localapp/v1/attendance/student/history/?username=%1$s&password=%2$s&studentId=%3$s&fromDate=%4$s&toDate=%5$s";

    public static final String DELETE_SIGN_HISTORY= "/localapp/v1/attendance/student/delete/?username=%1$s&password=%2$s&json=%3$s";

    public static final String STUDENT_PICTURE_INPUT= "/localapp/v1/photo/upload/?username=%1$s&password=%2$s";

    public static final String STAFF_PICTURE_INPUT= "/localapp/v1/teacher/photo/upload/?username=%1$s&password=%2$s";

    public static final String GET_DES_INFO= "/localapp/v1/qrcode/key/?username=%1$s&password=%2$s";

    public static final String GET_VISITOR_LIST= "/api/visitors/?username=%1$s&password=%2$s";

    public static final String GET_VISITOR_DETAIL= "/api/visitors/detail/?username=%1$s&password=%2$s";

    public static final String ADD_NEW_VISITOR= "/api/visitors/?username=%1$s&password=%2$s";

    public static final String EDIT_UPDATE_VISITOR= "/api/visitors/update_visitor/?username=%1$s&password=%2$s";

    public static final String EDIT_VISITOR= "/api/visitors/destroy/?username=%1$s&password=%2$s&visitor_id=%3$s";

    public static final String HEALTH_CHECK_REQUEST= "/localapp/v1/attendance/student/add/?username=%1$s&password=%2$s&json=%3$s";

    public static final String GET_LOCAL_ORGAN_DATA= "/localapp/enrichment/records/?username=%1$s&password=%2$s";

    public static final String GET_ORGAN_DETAIL= "/localapp/enrichment/student/info/?username=%1$s&password=%2$s&student_id=%3$s&is_child=%4$s";

    public static final String ORGAN_STUDENT_SIGN_REQUEST= "/localapp/enrichment/attendance/student/add/?username=%1$s&password=%2$s&json=%3$s&is_app=%4$s";

    public static final String ORGAN_PICTURE_INPUT= "/localapp/v1/enrichment/student/photo/upload/?username=%1$s&password=%2$s";

    public static final String GET_TOKEN= "/jwt-token-auth/";

    public static final String GET_CLOUD_FACE= "/api/attendance/attendance_device/sync_people/";

    public final static String SVC_UPLOAD_STUDENT_FACE = "/api/faceplusplus/student/photo/update-token/";

    public final static String SVC_GET_TEACHER_PROFILE = "/api/teacher/";

    public static final String SVC_GET_TOKEN_GUARDIAN = "/jwt-token-guardian/";

    public static final String SVC_UPLOAD_USER_AVATAR = "/api/guardian/profile/%1$s/avatar/";

    public final static String SVC_UPLOAD_AVATAR = "/api/account/%1$s/update_avatar/";


    public final static String CLASS_CLASS_STUDENT_LIST = "/api/k_student/sign_status/";
    public final static String CLASS_CLASS_LIST = "/api/k_student/klass_list/?username=%1$s&password=%2$s";
    public final static String CLASS_FOOD_LIST = "/api/recipev2/";
    public static final String GET_STUDENT_MEDICINE_LIST_NEW = "/api/medication/medication_request/teacher_batch_show_requests/";
    public static final String GET_MOUNT_PIC = "/api/infanttracking/photo/0/photo/?username=%1$s&password=%2$s";
    public final static String CLASS_MESSAGE_LIST = "/api/happeningsv2/";
    public final static String CLASS_WEEKPLAN_LIST = "/api/weekly_plan/get_weekly_plan/";

}
