package gov.smart.health.utils;

/**
 * Created by laoniu on 2017/10/25.
 */

public class SHConstants {
    public static boolean isDebug = true;
    public static String BaseUrl8889 = "http://182.92.128.240:8889/api";
    public static String BaseUrl8888 = "http://182.92.128.240:8888/api";
    public static String BaseUrlPhoto = "http://47.94.6.120/person/";

    public static String SigninSystemMobile = SHConstants.BaseUrl8889 + "/login/signinMobile";
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
    public static String RecordVRSportSave = BaseUrl8889+"/recordVRSport/save";
    public static String RecordVRSportDetailsave = BaseUrl8889+"/recordVRSportDetail/save";
    public static String PersonUploadMobile = BaseUrl8889+"/person/uploadMobile";

    //dowmnload
    public static String Download_Base_Link = "http://47.94.6.120/video/";
    public static String Download_Download = "SHDownloads";
    public static String Download_Temp = "SHTemp";

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

    //Setting
    public static String SettingPersonImage = "image";

    //splash
    public static String IsShowSplash = "IsShowSplash";
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
    public static String ShowAttentionModelKey = "show_attention_model_key";
    public static String PersonAttentionModelKey = "attention_model_key";
    public static String PersonFollow_List_OrderColumnName_Value = "person_name";
    //recordVRSport list parameter.
    public static String RecordVRSport_List_OrderColumnName_Value = "ts";

    //video list parameter.
    public static String Video_List_OrderColumnName_Value = "video_name";
    public static String Video_Floder_Key = "folder_key";
    public static String Video_Floder_Pk_Folder = "pk_folder";
    public static String Video_ModelKey = "video_model_key";


    //recordVR sport save parameter
    public static String Record_VRSport_Save_calorie = "calorie";
    public static String Record_VRSport_Save_heart_rate = "heart_rate";
    public static String Record_VRSport_Save_heart_rate_max = "heart_rate_max";

    public static String Record_VRSport_Save_Pk_Person = "pk_person";
    public static String Record_VRSport_Save_Person_Name = "person_name";

    public static String Record_VRSport_Save_Pk_Place = "pk_place";
    public static String Record_VRSport_Save_Place_Name = "place_name";

    public static String Record_VRSport_Save_Pk_Video = "pk_video";
    public static String Record_VRSport_Save_Video_Name = "video_name";

    public static String Record_VRSport_Save_Record_Sport_Code = "record_sport_code";
    public static String Record_VRSport_Save_Record_Sport_name = "record_sport_name";

    public static String Record_VRSport_Save_Time_End = "time_end";
    public static String Record_VRSport_Save_Time_Length = "time_length";
    public static String Record_VRSport_Save_Time_Start = "time_start";

    //recordVR sport detail save parameter
    public static String Record_VRSportDetailSave_calorie = "calorie";
    public static String Record_VRSportDetailSave_heart = "heart";
    public static String Record_VRSportDetailSave_length = "length";

    public static String Record_VRSportDetailSave_Pk_Person = "pk_person";
    public static String Record_VRSportDetailSave_Person_Name = "person_name";

    public static String Record_VRSportDetailSave_Pk_Place = "pk_place";
    public static String Record_VRSportDetailSave_Place_Name = "place_name";

    public static String Record_VRSportDetailSave_Pk_Video = "pk_video";
    public static String Record_VRSportDetailSave_Video_Name = "video_name";

    public static String Record_VRSportDetailSave_Record_SportDetailCode = "sport_detail_code";
    public static String Record_VRSportDetailSave_Record_SportDetailname = "sport_detail_name";

    public static String Record_VRSportDetailSave_Timestamp = "timestamp";

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

    public static String VideoLength = "VideoLength_";
}
