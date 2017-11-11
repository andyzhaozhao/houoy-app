package gov.smart.health.utils;

/**
 * Created by laoniu on 2017/10/25.
 */

public class SHConstants {
    public static boolean isDebug = true;
    public static String BaseUrl8889 = "http://182.92.128.240:8889/api";
    public static String BaseUrl8888 = "http://182.92.128.240:8888/api";
    public static String BaseUrlPhoto = "http://47.94.6.120/person/";

    public static String SigninSystemMobile = SHConstants.BaseUrl8889 + "/login/signinSystemMobile";
    public static String PersonSave = SHConstants.BaseUrl8889 + "/person/save";
    public static String ForgetPassword = BaseUrl8889+"/login/forgetPassword";

    public static String RecordShareSave = SHConstants.BaseUrl8889 + "/recordShare/save";
    public static String EssayRetrieveMobile = SHConstants.BaseUrl8888 + "/essay/retrieveMobile";
    public static String RecordShareRetrieveMobile = SHConstants.BaseUrl8889 + "/recordShare/retrieveMobile";
    public static String PersonFollowSave = SHConstants.BaseUrl8889 + "/personFollow/save";
    public static String PersonFollowRetrieve = BaseUrl8889+"/personFollow/retrieveMobile";
    public static String RecordVRSportRetrieve = BaseUrl8889+"/recordVRSport/retrieveMobile";
    public static String PersonRetrieve = BaseUrl8889+"/person/retrieveMobile";
    public static String FolderVideoRetrieve = BaseUrl8888+"/folderVideo/retrieveMobile";
    public static String VideoRetrieve = BaseUrl8888+"/video/retrieveMobile";

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
    public static String CommonOrderDir_Desc = "desc";
    public static String CommonOrderDir_Asc = "asc";
    public static String CommonUser_PK = "pk_person";
    public static String CommonPkType = "pk_type";
    public static String CommonTitle = "title";
    //Setting
    public static String SettingPersonModelKey = "PersonModelKey";

    //login
    public static String LoginUserPersonName = "person_name";
    public static String LoginUserPkPerson = "pk_person";
    //register
    public static String Register_Password = "password";
    public static String Register_Person_Code = "person_code";
    public static String Register_Person_name = "person_name";
    public static String Register_Mobile = "mobile";
    public static String Register_Email = "email";

    //reset pwd
    public static String Password_Email = "email";
    public static String Password_User_PK = "pk_person";

    //person flow
    public static String PersonFlowPersonName = "follow_person_name";
    public static String PersonFlowPkPerson = "follow_pk_person";
    public static String PersonFlowModelKey = "follow_model_key";

    //attention flow
    public static String PersonAttentionModelKey = "attention_model_key";
    public static String PersonFollow_List_OrderColumnName_Value = "person_name";
    //recordVRSport list parameter.
    public static String RecordVRSport_List_OrderColumnName_Value = "ts";

    //video list parameter.
    public static String Video_List_OrderColumnName_Value = "video_name";
    public static String Video_Floder_Key = "folder_key";
    public static String Video_Floder_Pk_Folder = "pk_folder";
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
