package jp.co.techtone.nsm.common;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class StrDateConverter {

	/**
	 * String型の日付をDate型に変換するメソッド
	 */
	public Date strDateConvert(String conDate){

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
		Date resultDate = new Date();
		try {
			resultDate = sdf.parse(conDate);
		} catch (ParseException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}
		return resultDate;
	}

	/**
	 * Date型の日付をString型に変換するメソッド
	 */
	public static String dateStrConvert(Date conDate){

		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		String strResultDate = sdf.format(conDate);

		return strResultDate;
	}

	/**
	 * ログに出力するフォーマット形式で現在時刻を返すメソッド
	 */
	public static String todayDateReturn(){

		Calendar calendar = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");

		return sdf.format(calendar.getTime());
	}

	/**
	 * 受け取った株価の種類を検索用に変換するメソッド
	 */
	public String searchConverter(String kind) {

		String resultKind = kind;
		String open = "始値";
		String high = "高値";
		String low = "低値";
		String close = "終値";

		if (kind.equals(open)) {
			resultKind = "open_rate";
		} else if (kind.equals(high)) {
			resultKind = "high_price";
		} else if (kind.equals(low)) {
			resultKind = "low_price";
		} else if (kind.equals(close)) {
			resultKind = "close_rate";
		}

		return resultKind;
	}

}
