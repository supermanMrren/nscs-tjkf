package com.boco.nscs.entity.sys;

import java.io.Serializable;
import java.util.Date;


/**
 * 用户信息表
 * 
 * @author nscs
 * @email nscs@boco.com.cn
 * @date 2018-07-10 15:10:20
 */
public class UserInfo implements Serializable {
	private static final long serialVersionUID = 1L;
	
	//id
	private Integer id;
	//帐号
	private String userId;
	//姓名
	private String userName;
	//性别
	private String sex;
	//邮箱地址
	private String mail;
	//电话
	private String phone;
	//状态：1:激活，2:锁定，3:删除
	private Integer state;
	//部门
	private String deptId;
	//地市
	private String cityId;
	//上次登录时间
	private Date lastLoginTime;
	//描述
	private String remarks;

	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getId() {
		return id;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getUserName() {
		return userName;
	}
	public void setSex(String sex) {
		this.sex = sex;
	}
	public String getSex() {
		return sex;
	}
	public void setMail(String mail) {
		this.mail = mail;
	}
	public String getMail() {
		return mail;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getPhone() {
		return phone;
	}
	public void setState(Integer state) {
		this.state = state;
	}
	public Integer getState() {
		return state;
	}
	public void setDeptId(String deptId) {
		this.deptId = deptId;
	}
	public String getDeptId() {
		return deptId;
	}
	public void setCityId(String cityId) {
		this.cityId = cityId;
	}
	public String getCityId() {
		return cityId;
	}
	public void setLastLoginTime(Date lastLoginTime) {
		this.lastLoginTime = lastLoginTime;
	}
	public Date getLastLoginTime() {
		return lastLoginTime;
	}
	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
	public String getRemarks() {
		return remarks;
	}
}
