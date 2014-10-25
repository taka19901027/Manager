package jp.co.techtone.nsm.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="user_table")
public class UserDataEntity {

	@Id
	@Column(length=20)
	private String user_id;

	@Column(length=8, nullable=false)
	private String user_password;

	@Column(nullable=false)
	private String user_permission;

	public UserDataEntity(){
	}

	public UserDataEntity(String user_id, String user_password, String user_permission){
		this();
		this.user_id = user_id;
		this.user_password = user_password;
		this.user_permission = user_permission;
	}

	public String getUser_id() {
		return user_id;
	}

	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}

	public String getUser_password() {
		return user_password;
	}

	public void setUser_password(String user_password) {
		this.user_password = user_password;
	}

	public String getUser_permission() {
		return user_permission;
	}

	public void setUser_permission(String user_permission) {
		this.user_permission = user_permission;
	}


}
