package jp.co.techtone.nsm.logic;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import jp.co.techtone.nsm.entity.NikkeiDateEntity;
import jp.co.techtone.nsm.form.PriceInsertForm;

public class NsmDBLogic {

	/**
	 * ログインID、パスワードがデータベースに登録されているかを確認するメソッド
	 */
	public boolean checkLoginData(String user_id, String password, List user_list) {

		boolean result = false;

		Iterator it = user_list.iterator();
		while (it.hasNext()) {
			Map<String, Object> USER = ((Map<String, Object>) it.next());
			if (USER.containsValue(user_id)) {
				if (USER.containsValue(password)) {
					result = true;
				}
			}
		}
		return result;
	}

	/**
	 * 平均算出用メソッド
	 * @param averageRateList
	 * @param searchKind
	 * @return
	 */
	public int listAverageResolver(List<NikkeiDateEntity> averageRateList, String searchKind){

		int sumInt = 0;
		int listSize = averageRateList.size();
		int resultInt = 0;

		if(searchKind.equals("open_rate")){

			for(int i=0; i < listSize; i++){

				String str = Double.toString(averageRateList.get(i).getOpen_rate());

				//取り出した値が整数か小数かを判定
				//小数なら小数点以下を切り捨て整数へ変換して合計変数へ加算
				if(!(str.indexOf("\\.") == -1)){
					String[] divisionNumber = str.split("\\.");
					sumInt += Integer.parseInt(divisionNumber[0]);
				}else{
					sumInt += (int)averageRateList.get(i).getOpen_rate();
				}
			}

		}else if(searchKind.equals("high_price")){

			for(int i=0; i < listSize; i++){

				String str = Double.toString(averageRateList.get(i).getHigh_price());

				//取り出した値が整数か小数かを判定
				//小数なら小数点以下を切り捨て整数へ変換して合計変数へ加算
				if(!(str.indexOf("\\.") == -1)){
					String[] divisionNumber = str.split("\\.");
					sumInt += Integer.parseInt(divisionNumber[0]);
				}else{
					sumInt += (int)averageRateList.get(i).getHigh_price();
				}
			}

		}else if(searchKind.equals("low_price")){

			for(int i=0; i < listSize; i++){

				String str = Double.toString(averageRateList.get(i).getLow_price());

				//取り出した値が整数か小数かを判定
				//小数なら小数点以下を切り捨て整数へ変換して合計変数へ加算
				if(!(str.indexOf("\\.") == -1)){
					String[] divisionNumber = str.split("\\.");
					sumInt += Integer.parseInt(divisionNumber[0]);
				}else{
					sumInt += (int)averageRateList.get(i).getLow_price();
				}
			}

		}else if(searchKind.equals("close_rate")){

			for(int i=0; i < listSize; i++){

				String str = Double.toString(averageRateList.get(i).getClose_rate());

				//取り出した値が整数か小数かを判定
				//小数なら小数点以下を切り捨て整数へ変換して合計変数へ加算
				if(!(str.indexOf("\\.") == -1)){
					String[] divisionNumber = str.split("\\.");
					sumInt += Integer.parseInt(divisionNumber[0]);
				}else{
					sumInt += (int)averageRateList.get(i).getClose_rate();
				}
			}
		}

		double averageDouble = sumInt / listSize;
		String averageResultStr = Double.toString(averageDouble);

		if(!(averageResultStr.indexOf("\\.") == -1)){
			String[] averageStr = averageResultStr.split("\\.");
			resultInt = Integer.parseInt(averageStr[0]);
		}else{
			resultInt = (int) averageDouble;
		}

		return resultInt;

	}

	/**
	 * 削除に選択された日付がDBに存在するかチェックするメソッド
	 * ADD「検索する」ボタンの照合にも使用する
	 * @param deleteEntity
	 * @param deleteCheckList
	 * @return
	 */
	public boolean deleteEntityCheck(String checkOnlyDeleteDate, List<NikkeiDateEntity> deleteCheckList){

		boolean deleteResultCheck = false;
		int checkListSize = deleteCheckList.size();

		for(int i=0; i < checkListSize; i++){
			if(checkOnlyDeleteDate.equals(deleteCheckList.get(i).getDay_date())){
				deleteResultCheck = true;
			}
		}
		return deleteResultCheck;
	}

	/**
	 * DBとの照合用にエンティティのデータを変換するメソッド
	 * @param priceInsertForm
	 * @return
	 */
	public String createCheckOnlyDate(PriceInsertForm priceInsertForm){

		String resultCheckOnlyDate = priceInsertForm.getYEAR() + "-" + priceInsertForm.getMONTH() + "-" + priceInsertForm.getDAY();

		return resultCheckOnlyDate;
	}

	/**
	 * エンティティが入ったリストをJSP出力用に変換するメソッド
	 * @param exportList
	 * @return
	 */
	public List<List<String>> getExportList(List<NikkeiDateEntity> exportList){

		List<List<String>> exportResultList = new ArrayList();

		int exportListSize = exportList.size();

		for(int i=0; i < exportListSize; i++){
			List tmpList = new ArrayList();
			tmpList.add(exportList.get(i).getDay_date());
			tmpList.add(exportList.get(i).getOpen_rate());
			tmpList.add(exportList.get(i).getHigh_price());
			tmpList.add(exportList.get(i).getLow_price());
			tmpList.add(exportList.get(i).getClose_rate());
			exportResultList.add(tmpList);
		}

		return exportResultList;

	}

}
