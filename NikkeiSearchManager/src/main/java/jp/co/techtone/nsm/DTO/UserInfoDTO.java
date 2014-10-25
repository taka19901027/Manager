package jp.co.techtone.nsm.DTO;

import java.io.File;

/**
 * ユーザー情報を記憶するシングルトンクラス
 */
public class UserInfoDTO {

	//ログイン時にのみ生成されるシングルトンクラス
	public static UserInfoDTO userInfoDTO;

	//ユーザーID
	private String USER_NAME;

	//ユーザーの操作状況を記録するログのファイルクラス
	private File USER_FILE;

	private UserInfoDTO(){
	}

	/**
	 * ユーザー情報を記憶するDTOクラスを生成するメソッド
	 * シングルトンのため、ログインしたユーザー情報をセッションが切れるまで永続的に保存する
	 * @return
	 */
	public static UserInfoDTO getInstance(){
		if(userInfoDTO == null){
			userInfoDTO = new UserInfoDTO();
		}
		return userInfoDTO;
	}

	public String getUSER_NAME() {
		return USER_NAME;
	}

	public void setUSER_NAME(String uSER_NAME) {
		USER_NAME = uSER_NAME;
	}

	public File getUSER_FILE() {
		return USER_FILE;
	}

	public void setUSER_FILE(File uSER_FILE) {
		USER_FILE = uSER_FILE;
	}
}
