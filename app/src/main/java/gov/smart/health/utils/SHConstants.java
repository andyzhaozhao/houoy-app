package gov.smart.health.utils;

/**
 * Created by laoniu on 2017/10/25.
 */

public class SHConstants {
    public static boolean isDebug = true;
    public static String BaseUrl8889 = "http://182.92.128.240:8889/api";
    public static String SigninSystemMobile = SHConstants.BaseUrl8889 + "/login/signinSystemMobile";
    public static String RecordShareSave = SHConstants.BaseUrl8889 + "/recordShare/save";


    public static String LoginUserCode = "user_code";
    public static String LoginUserPassword = "user_password";

    public static String HeaderContentType = "Content-Type";
    public static String HeaderAccept = "Accept";
    public static String HeaderContentTypeValue = "application/json";

    //login
    public static String LoginUserPersonName = "person_name";
    public static String LoginUserPkPerson = "pk_person";

    //find share
    public static String FindSharePersonName = "person_name";
    public static String FindSharePkPerson = "pk_person";
    public static String FindShareRecordShareCode = "record_share_code";
    public static String FindShareRecordShareDesc = "record_share_desc";
    public static String FindShareRecordShareName = "record_share_name";

}
