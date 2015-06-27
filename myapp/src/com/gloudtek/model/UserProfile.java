package com.gloudtek.model;

/**
 * Created by wu on 6/27/15.
 */

/*
userName	String	用户名
userNickname	String	昵称
userBirthday	String	生日	格式为：“YYYY-MM-HH”
userGender	String	性别	‘M’-男，‘F’-女，‘O’-其他
userHeight	Integer	身高	单位 厘米
userWeight	Integer	体重	单位 公斤
createTime	String	加入时间	用户账号创建的日期，
格式为：“YYYY-MM-HH”
footLen	Integer	步长	单位cm
userFace	String	用户头像	用户头像在服务器上的地址
activeDeviceID	String	激活设备ID	当前和APP绑定的设备ID
stepTarget	Integer	计步目标	用户一天的计步目标
*/
    //refs server return

public class UserProfile {
  public String userName;
    public String userNickname;
    public String userBirthday;
    public String userGender;
    public Integer userHeight;
    public Integer userWeight;
    public String createTime;
    public Integer footLen;
    public String userFace;
    public String activeDeviceID;
    public Integer stepTarget;
}
