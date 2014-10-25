package jp.co.techtone.nsm.logic;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import jp.co.techtone.nsm.DTO.UserInfoDTO;
import jp.co.techtone.nsm.Exception.NSMFileManagementException;
import jp.co.techtone.nsm.common.StrDateConverter;
import jp.co.techtone.nsm.entity.NikkeiDateEntity;
import jp.co.techtone.nsm.form.PriceInsertForm;

public class NsmControllerLogic {

	private static final String LOG_FILE_NAME = "c:\\NikkeiSearchManager\\";

	private static File USER_LOG_FILE_NAME;

	/**
	 * ユーザーがログインした際にオペレーションのトレースログを作成するメソッド
	 * 日毎に作成されるため、1日2回以上ログインした場合は新規作成されず追記される。
	 * @throws IOException
	 */
	public boolean createLogFile(String User, Date todayDate){

		//戻り値用の変数を定義
		boolean createFileResult = true;
		int returnCodeCheck;

		//ログファイル生成用のFileクラスの作成
		String tmpLogNameDate = StrDateConverter.dateStrConvert(todayDate);
		String logName = this.LOG_FILE_NAME + tmpLogNameDate + "_" + User + ".txt";
		boolean isFileChecker = false;
		File logFile = new File(logName);

		//ユーザーDTOにファイル情報をセットする
		UserInfoDTO.userInfoDTO.setUSER_FILE(logFile);

		if(logFile.exists()){

			isFileChecker = true;
			returnCodeCheck = this.writeLoginSuccessLog(logFile, isFileChecker);

		}else{

			//ログファイルを作成
			try{

				logFile.createNewFile();
				returnCodeCheck = this.writeLoginSuccessLog(logFile, isFileChecker);

			}catch(IOException e){
				createFileResult = false;
				return createFileResult;
			}
		}

		//書き込みに失敗した時もfalseを返す
		if(returnCodeCheck == -1){
			createFileResult = false;
		}

		return createFileResult;
	}

	/**
	 * ログイン成功のログ書き出しメソッド
	 * @param logFile
	 * @param isFileChecker
	 * @throws NSMFileManagementException
	 */
	private int writeLoginSuccessLog(File logFile, boolean isFileChecker){

		String writeTodayDate = StrDateConverter.todayDateReturn();

		int returnCode;

		if(isFileChecker){

			try{
				//ログイン成功の書き込み動作、既存のファイルに追記
				 FileWriter fileWriter = new FileWriter(logFile, true);
				 fileWriter.write(writeTodayDate + ";ログインに成功しました\r\n");
				 fileWriter.close();
				 returnCode = 0;
			}catch(IOException e){
				returnCode = -1;
				return returnCode;
			}

		}else{
			try{
				//ログイン成功の書き込み動作、新規ファイルに書き込み
				 FileWriter fileWriter = new FileWriter(logFile);
				 fileWriter.write(writeTodayDate + ";ログインに成功しました\r\n");
				 fileWriter.close();
				 returnCode = 0;
			}catch(IOException e){
				returnCode = -1;
				return returnCode;
			}
		}

		return returnCode;
	}

	/**
	 * ログアウト時にログを出力するメソッド
	 * @param todayDate
	 * @return
	 */
	public boolean logoutAction(){

		boolean logoutResult = true;

		try{

			String writeTodayDate = StrDateConverter.todayDateReturn();

			FileWriter fileWriter = new FileWriter(UserInfoDTO.userInfoDTO.getUSER_FILE(), true);
			fileWriter.write(writeTodayDate + ";ログアウトに成功しました\r\n");
			fileWriter.close();

		}catch(IOException e){
			logoutResult = false;
			return logoutResult;
		}

		return logoutResult;
	}

	/**
	 * データの入力ページ「入力する」ボタンのチェックメソッド 4つの値のうち、1つでも未入力があればfalseを返す。
	 *
	 * @param priceInsertForm
	 * @return
	 */
	public boolean CheckPriceInsert(PriceInsertForm priceInsertForm) {

		boolean resultCheck = true;

		if (priceInsertForm.getOPEN_INSERT().equals("")
				|| priceInsertForm.getHIGH_INSERT().equals("")
				|| priceInsertForm.getLOW_INSERT().equals("")
				|| priceInsertForm.getCLOSE_INSERT().equals("")) {

			resultCheck = false;

		}

		try {

			Double.parseDouble(priceInsertForm.getOPEN_INSERT());
			Double.parseDouble(priceInsertForm.getHIGH_INSERT());
			Double.parseDouble(priceInsertForm.getLOW_INSERT());
			Double.parseDouble(priceInsertForm.getCLOSE_INSERT());

		} catch (NumberFormatException e) {

			resultCheck = false;
			return resultCheck;

		}

		return resultCheck;
	}

	/**
	 * データの入力ページ「入力する」ボタンで受け取ったパラメータから DBに入力するNikkeiDateEntityを作成するメソッド
	 *
	 * @param priceInsertForm
	 * @return
	 */
	public NikkeiDateEntity createInsertEntity(PriceInsertForm priceInsertForm) {

		NikkeiDateEntity insertEntity = new NikkeiDateEntity();

		// DBに入力するエンティティを作成する
		insertEntity.setDay_date(priceInsertForm.getYEAR() + "/"
				+ priceInsertForm.getMONTH() + "/" + priceInsertForm.getDAY());
		insertEntity.setOpen_rate(Double.parseDouble(priceInsertForm
				.getOPEN_INSERT()));
		insertEntity.setHigh_price(Double.parseDouble(priceInsertForm
				.getHIGH_INSERT()));
		insertEntity.setLow_price(Double.parseDouble(priceInsertForm
				.getLOW_INSERT()));
		insertEntity.setClose_rate(Double.parseDouble(priceInsertForm
				.getCLOSE_INSERT()));

		return insertEntity;
	}

	public boolean CheckPriceDelete(PriceInsertForm priceInsertForm) {

		boolean resultCheck = false;

		if (priceInsertForm.getOPEN_INSERT().equals("")
				&& priceInsertForm.getHIGH_INSERT().equals("")
				&& priceInsertForm.getLOW_INSERT().equals("")
				&& priceInsertForm.getCLOSE_INSERT().equals("")) {

			resultCheck = true;

		}

		return resultCheck;
	}

	/**
	 * 引数のフォームから削除対象のデータエンティティを作成するメソッド
	 * @param priceInsertForm
	 * @return
	 */
	public NikkeiDateEntity createDeleteEntity(PriceInsertForm priceInsertForm) {

		NikkeiDateEntity deleteEntity = new NikkeiDateEntity();

		deleteEntity.setDay_date(priceInsertForm.getYEAR() + "/"
				+ priceInsertForm.getMONTH() + "/" + priceInsertForm.getDAY());

		return deleteEntity;
	}

	/**
	 * データ出力用のヘッダーを取得するメソッド
	 * @return
	 */
	public List<String> getHeaderList(){

		List<String> headerList = new ArrayList<String>();

		headerList.add("日付");
		headerList.add("始値");
		headerList.add("高値");
		headerList.add("低値");
		headerList.add("終値");

		return headerList;
	}
}
