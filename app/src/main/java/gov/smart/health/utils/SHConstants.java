package gov.smart.health.utils;

/**
 * Created by laoniu on 2017/10/25.
 */

public class SHConstants {
    public static boolean isDebug = true;
    public static String BaseUrl8889 = "http://182.92.128.240:8889/api";
    public static String BaseUrl8888 = "http://182.92.128.240:8888/api";
    public static String SigninSystemMobile = SHConstants.BaseUrl8889 + "/login/signinSystemMobile";
    public static String RecordShareSave = SHConstants.BaseUrl8889 + "/recordShare/save";
    public static String EssayRetrieveMobile = SHConstants.BaseUrl8888 + "/essay/retrieveMobile";
    public static String RecordShareRetrieveMobile = SHConstants.BaseUrl8889 + "/recordShare/retrieveMobile";
    public static String PersonFollowSave = SHConstants.BaseUrl8889 + "/personFollow/save";


    public static String LoginUserCode = "user_code";
    public static String LoginUserPassword = "user_password";

    public static String HeaderContentType = "Content-Type";
    public static String HeaderAccept = "Accept";
    public static String HeaderContentTypeValue = "application/json";

    //commom
    public static String CommonStart = "start";
    public static String CommonLength = "length";
    public static String CommonOrderColumnName = "orderColumnName";
    public static String CommonOrderDir = "orderDir";
    public static String CommonPkType = "pk_type";
    public static String CommonTitle = "title";
    //login
    public static String LoginUserPersonName = "person_name";
    public static String LoginUserPkPerson = "pk_person";

    //person flow
    public static String PersonFlowPersonName = "follow_person_name";
    public static String PersonFlowPkPerson = "follow_pk_person";
    public static String PersonFlowModelKey = "follow_model_key";

    //find share
    public static String FindSharePersonName = "person_name";
    public static String FindSharePkPerson = "pk_person";
    public static String FindShareRecordShareCode = "record_share_code";
    public static String FindShareRecordShareDesc = "record_share_desc";
    public static String FindShareRecordShareName = "record_share_name";

    //essay list
    public static String EssayStart = "0";
    public static String EssayLength = "10";
    public static String EssayOrderColumnName = "ts";
    public static String EssayOrderDir = "desc";
    public static String EssayType32 = "32";
    public static String EssayType33 = "33";

}
