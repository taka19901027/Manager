package jp.co.techtone.nsm.controller;

import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import jp.co.techtone.nsm.DAO.NikkeiDateDAOImpl;
import jp.co.techtone.nsm.DTO.UserInfoDTO;
import jp.co.techtone.nsm.Exception.NSMFileManagementException;
import jp.co.techtone.nsm.common.NSMHolidayChecker;
import jp.co.techtone.nsm.common.StrDateConverter;
import jp.co.techtone.nsm.entity.NikkeiDateEntity;
import jp.co.techtone.nsm.facade.NsmControllerFacade;
import jp.co.techtone.nsm.form.AverageSearchForm;
import jp.co.techtone.nsm.form.DateExportForm;
import jp.co.techtone.nsm.form.LoginForm;
import jp.co.techtone.nsm.form.PriceInsertForm;
import jp.co.techtone.nsm.form.PriceSearchForm;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * アプリ全体のコントローラークラス
 */
@Controller
public class NsmController extends AbstractNsmController {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Autowired
	private Properties applicationProperties;

	@Autowired
	NikkeiDateDAOImpl nikkeiDateDAOImpl;

	NsmControllerFacade nsmControllerFacade = new NsmControllerFacade();

	NSMHolidayChecker nsmHolidayChecker = new NSMHolidayChecker();

	StrDateConverter strDateConverter = new StrDateConverter();

	UserInfoDTO userInfoDTO = UserInfoDTO.getInstance();

	Calendar todayCalendar = Calendar.getInstance();

	Date todayDate = todayCalendar.getTime();

	/**
	 * GETメソッド URLでアクセスした時に最初にログインページを表示
	 */
	@RequestMapping(value = "/LoginMenu", method = RequestMethod.GET)
	public String loginStart(Model model) {

		LoginForm lf = new LoginForm();
		model.addAttribute("loginForm", lf);
		model.addAttribute("title", applicationProperties.getProperty("LoginMenu.title"));
		model.addAttribute("message",applicationProperties.getProperty("LoginMenu.message"));
		return "LoginMenu";

	}

	/**
	 * ログインメニューで「ログイン」ボタンを押したときのPOSTメソッド
	 * @throws NSMFileManagementException
	 */
	@RequestMapping(value = "/LoginMenu", method = RequestMethod.POST, params = "login")
	public String loginCheck(@ModelAttribute LoginForm lf, Model model) throws NSMFileManagementException {

		List<Map<String, Object>> userList = jdbcTemplate
				.queryForList("select * from user_table");

		// ユーザーIDとパスワードが正しいか判定
		// 正しい場合はtitleとユーザー名をセットしトップメニューに移動する
		if (nsmControllerFacade.checkLoginDataFacade(lf.getUSER_ID(), lf.getPASSWORD(), userList)) {

			//ログ出力用のファイルを生成
			if(nsmControllerFacade.createLogFileFacade(lf.getUSER_ID(), todayDate)){

				//シングルトンクラスにユーザーIDをセットする
				userInfoDTO.setUSER_NAME(lf.getUSER_ID());
				model.addAttribute("title", userInfoDTO.getUSER_NAME()
						+ applicationProperties.getProperty("TopMenu.welcommessage"));
				model.addAttribute("message", applicationProperties.getProperty("TopMenu.choicemessage"));
				return "TopMenu";

			//ファイル管理処理で例外が発生していた場合は例外ページに移動する
			}else{
				model.addAttribute("subject", applicationProperties.getProperty("NSMFileManagementException.subject"));
				model.addAttribute("message", applicationProperties.getProperty("NSMFileManagementException.message"));
				return "Exception/NSMFileManagementException";
			}
			// 正しくない場合はエラーメッセージを送信し、再度ログイン画面へ
		} else {
			model.addAttribute("title", applicationProperties.getProperty("LoginMenu.error_title"));
			model.addAttribute("message", applicationProperties.getProperty("LoginMenu.error_message"));
			return "LoginMenu";
		}
	}

	/**
	 * トップメニューで「データを入力する」を押したときのPOSTメソッド
	 * 日経株価入力ページ(PriceInsert.jsp)にレンダリングする
	 */
	@RequestMapping(value = "/LoginMenu", method = RequestMethod.POST, params = "insert_data")
	public String changeInsertPage(Model model) {

		// Viewに渡すプルダウンリストを作成するメソッド呼び出し
		Map DayPriceMap = new HashMap();
		DayPriceMap = getDayPriceMap();

		PriceInsertForm priceInsertForm = new PriceInsertForm();

		model.addAttribute("PriceInsertForm", priceInsertForm);
		model.addAttribute("title", applicationProperties.getProperty("PriceInsert.title"));
		model.addAttribute("message", applicationProperties.getProperty("PriceInsert.message"));
		model.addAttribute("YearList", DayPriceMap.get("YearList"));
		model.addAttribute("MonthList", DayPriceMap.get("MonthList"));
		model.addAttribute("DayList", DayPriceMap.get("DayList"));
		model.addAttribute("PriceSearchList", DayPriceMap.get("PriceSearchList"));


		return "PriceInsert/PriceInsert";

	}

	/**
	 * トップメニューで「過去の日経株価を見る」を押したときのPOSTメソッド
	 * 日経株価検索ページ(PriceSearch.jsp)にレンダリングする
	 */
	@RequestMapping(value = "/LoginMenu", method = RequestMethod.POST, params = "past_price_search")
	public String changeSearchPage(Model model) {

		// Viewに渡すプルダウンリストを作成するメソッド呼び出し
		Map DayPriceMap = new HashMap();
		DayPriceMap = getDayPriceMap();

		// 画面表示用の本日のデータ取得
		int year = todayCalendar.get(todayCalendar.YEAR);
		int month = todayCalendar.get(todayCalendar.MONTH);
		month++;
		int day = todayCalendar.get(todayCalendar.DATE);
		String strTodayDate = year + "年" + month + "月" + day + "日";

		PriceSearchForm psf = new PriceSearchForm();
		model.addAttribute("PriceSearchForm", psf);
		model.addAttribute("YearList", DayPriceMap.get("YearList"));
		model.addAttribute("MonthList", DayPriceMap.get("MonthList"));
		model.addAttribute("DayList", DayPriceMap.get("DayList"));
		model.addAttribute("PriceSearchList", DayPriceMap.get("PriceSearchList"));

		// 本日の曜日を調べ平日なら初期値として表示する本日の株価始値の値を取得
		// 本日が土日祝日かどうかのチェック
		if (nsmHolidayChecker.isHoliday(todayDate)
				|| (nsmHolidayChecker.isSundayOrSaturday(todayDate))) {

			model.addAttribute("message", "本日" + strTodayDate + "は土日祝の為、休場です。");

			return "PriceSearch/PriceSearch2";

		} else {

			SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
			String todaySearchDate = sdf.format(todayDate);
			List todaySearchList = nikkeiDateDAOImpl.findByDate(todaySearchDate);

			//未入力によりデータが取得できなかった時はエラーメッセージを返す
			if(todaySearchList.size() == 0){

				model.addAttribute("message", applicationProperties.getProperty("PriceSearch.todayPriceError"));

				return "PriceSearch/PriceSearch2";

			//取得できたデータを表示する
			}else{

				double todayPrice = ((NikkeiDateEntity) todaySearchList.get(0)).getOpen_rate();

				model.addAttribute("year", year);
				model.addAttribute("month", month);
				model.addAttribute("day", day);
				model.addAttribute("price_search", "始値");
				model.addAttribute("rate", todayPrice);

				return "PriceSearch/PriceSearch";
			}
		}
	}

	/**
	 * トップメニューで「日付を指定して平均を見る」を押したときのPOSTメソッド
	 * 平均検索ページ(AverageSearchTop.jsp)にレンダリングする
	 */
	@RequestMapping(value = "/LoginMenu", method = RequestMethod.POST, params = "average_price_search")
	public String changeAveragePage(Model model) {

		// Viewに渡すプルダウンリストを作成するメソッド呼び出し
		Map DayPriceMap = new HashMap();
		DayPriceMap = getDayPriceMap();

		AverageSearchForm asf = new AverageSearchForm();
		model.addAttribute("AverageSearchForm", asf);
		model.addAttribute("title", applicationProperties.getProperty("AverageSearchTop.title"));
		model.addAttribute("startDate", applicationProperties.getProperty("AverageSearchTop.startDate"));
		model.addAttribute("endDate", applicationProperties.getProperty("AverageSearchTop.endDate"));
		model.addAttribute("rateKind", applicationProperties.getProperty("AverageSearchTop.rateKind"));
		model.addAttribute("YearList", DayPriceMap.get("YearList"));
		model.addAttribute("MonthList", DayPriceMap.get("MonthList"));
		model.addAttribute("DayList", DayPriceMap.get("DayList"));
		model.addAttribute("PriceSearchList", DayPriceMap.get("PriceSearchList"));

		return "AverageSearch/AverageSearchTop";
	}

	/**
	 * トップメニューで「選択したデータを出力する」を押したときのPOSTメソッド
	 * 株価出力ページ(DateExport.jsp)にレンダリングする
	 */
	@RequestMapping(value = "/LoginMenu", method = RequestMethod.POST, params = "price_data_export")
	public String changeExportPage(Model model) {

		// Viewに渡すプルダウンリストを作成するメソッド呼び出し
		Map DayPriceMap = new HashMap();
		DayPriceMap = getDayPriceMap();

		DateExportForm def = new DateExportForm();
		model.addAttribute("DateExportForm", def);
		model.addAttribute("title", applicationProperties.getProperty("DateExport.title"));
		model.addAttribute("startDate", applicationProperties.getProperty("DateExport.start"));
		model.addAttribute("endDate", applicationProperties.getProperty("DateExport.end"));
		model.addAttribute("YearList", DayPriceMap.get("YearList"));
		model.addAttribute("MonthList", DayPriceMap.get("MonthList"));
		model.addAttribute("DayList", DayPriceMap.get("DayList"));

		return "DateExport/DateExport";
	}

	/**
	 * トップメニューで「ログインメニューに戻る」を押したときのPOSTメソッド
	 * ログインページに戻る
	 */
	@RequestMapping(value = "/LoginMenu", method = RequestMethod.POST, params = "return_loginpage")
	public String returnLogain(Model model) {

		String JspName = this.loginStart(model);

		return JspName;
	}

	/**
	 * データを入力するページ「入力する」ボタンのPOSTメソッド
	 * 指定された日付が平日の場合、resultPriceSearch.jspにレンダリングする
	 * 土日祝の場合はresultPriceSearch2.jspにレンダリングする
	 *
	 * @throws ParseException
	 * @throws java.text.ParseException
	 * @throws UnsupportedEncodingException
	 */
	@RequestMapping(value = "/LoginMenu", method = RequestMethod.POST, params = "Insert_Price")
	public String priceInsert(@ModelAttribute PriceInsertForm priceInsertForm,
			Model model) throws ParseException, UnsupportedEncodingException {

		//入力された値に未入力や不適切な文字列が存在しないかチェック
		if(nsmControllerFacade.CheckPriceInsertFacade(priceInsertForm)){

			// 検索条件に指定されたパラメータから日付を作成する
			String year = priceInsertForm.getYEAR();
			String month = priceInsertForm.getMONTH();
			String day = priceInsertForm.getDAY();
			String strInsertDate = year + "/" + month + "/" + day;
			Date insertDate = strDateConverter.strDateConvert(strInsertDate);

			//選択された日付が土日祝の場合は入力させない
			if (nsmHolidayChecker.isHoliday(insertDate)
					|| (nsmHolidayChecker.isSundayOrSaturday(insertDate))) {

				model.addAttribute("resultView", year + "年" + month + "月" + day + "日は土日祝のため、入力できません");
				return "PriceInsert/PriceInsertResultInsert";

			}else{

				NikkeiDateEntity insertEntity = nsmControllerFacade.createInsertEntityFacade(priceInsertForm);

				//選択したデータを入力
				if(nikkeiDateDAOImpl.addEntity(insertEntity)){
					model.addAttribute("resultView", applicationProperties.getProperty("PriceInsert.InsertSuccess"));
					return "PriceInsert/PriceInsertResultInsert";
				//選択した日付に値がすでに入力されている場合は入力できない
				}else{
					model.addAttribute("resultView", applicationProperties.getProperty("PriceInsert.InsertDuplicationError"));
					return "PriceInsert/PriceInsertResultInsert";
				}
			}
		}else{
			model.addAttribute("resultView", applicationProperties.getProperty("PriceInsert.NullParseError"));
			return "PriceInsert/PriceInsertResultInsert";
		}
	}

	/**
	 * データを入力するページ「削除する」ボタンのPOSTメソッド
	 * 指定した日付のデータが見つからなかった場合はエラーメッセージを返す
	 * カラム個々の削除はできず、行毎削除する
	 *
	 * @throws ParseException
	 * @throws java.text.ParseException
	 * @throws UnsupportedEncodingException
	 */
	@RequestMapping(value = "/LoginMenu", method = RequestMethod.POST, params = "Delete_Price")
	public String priceDelete(@ModelAttribute PriceInsertForm priceInsertForm,
			Model model) throws ParseException, UnsupportedEncodingException {

		//日経平均株価入力項目に値が入っていないかチェック
		if(nsmControllerFacade.CheckPriceDeleteFacade(priceInsertForm)){

			NikkeiDateEntity deleteTmpEntity = nsmControllerFacade.createDeleteEntityFacade(priceInsertForm);
			String checkOnlyDeleteDate = nsmControllerFacade.createCheckOnlyDateFacade(priceInsertForm);
			List<NikkeiDateEntity> deleteList = nikkeiDateDAOImpl.getAll();

			//削除を実行し、削除が成功した場合成功メッセージを返す
			if(nsmControllerFacade.deleteEntityCheckFacade(checkOnlyDeleteDate, deleteList)){

				nikkeiDateDAOImpl.removeEntity(deleteTmpEntity.getDay_date());
				model.addAttribute("resultView", applicationProperties.getProperty("PriceDelete.DeleteSucess"));
				return "PriceInsert/PriceInsertResultInsert";

			//削除するデータが見つからない場合、エラーメッセージを返す
			}else{
				model.addAttribute("resultView", applicationProperties.getProperty("PriceDelete.NoFindDataError"));
				return "PriceInsert/PriceInsertResultInsert";
			}

		}else{
			model.addAttribute("resultView", applicationProperties.getProperty("PriceDelete.ExtraWordError"));
			return "PriceInsert/PriceInsertResultInsert";
		}
	}

	/**
	 * データを入力するページ「データ入力ページに戻る」ボタンのPOSTメソッド
	 * PriceInsert.jspにレンダリングする
	 */
	@RequestMapping(value = "/LoginMenu", method = RequestMethod.POST, params = "return_insert_page")
	public String returnPriceInsert(Model model) throws ParseException, UnsupportedEncodingException {

		// Viewに渡すプルダウンリストを作成するメソッド呼び出し
		Map DayPriceMap = new HashMap();
		DayPriceMap = getDayPriceMap();

		PriceInsertForm priceInsertForm = new PriceInsertForm();

		model.addAttribute("PriceInsertForm", priceInsertForm);
		model.addAttribute("title", applicationProperties.getProperty("PriceInsert.title"));
		model.addAttribute("message", applicationProperties.getProperty("PriceInsert.message"));
		model.addAttribute("YearList", DayPriceMap.get("YearList"));
		model.addAttribute("MonthList", DayPriceMap.get("MonthList"));
		model.addAttribute("DayList", DayPriceMap.get("DayList"));
		model.addAttribute("PriceSearchList", DayPriceMap.get("PriceSearchList"));

		return "PriceInsert/PriceInsert";

	}

	/**
	 * 過去の日経株価検索ページ「検索する」ボタンのPOSTメソッド
	 *
	 * @throws ParseException
	 * @throws java.text.ParseException
	 * @throws UnsupportedEncodingException
	 */
	@RequestMapping(value = "/LoginMenu", method = RequestMethod.POST, params = "Price_Search")
	public String searchValue(@ModelAttribute PriceSearchForm priceSearchForm,
			Model model) throws ParseException, UnsupportedEncodingException {

		// 検索条件に指定されたパラメータから日付を作成する
		String year = priceSearchForm.getYEAR();
		String month = priceSearchForm.getMONTH();
		String day = priceSearchForm.getDAY();
		String strSearchDate = year + "/" + month + "/" + day;
		String strSearchDate2 = year + "-" + month + "-" + day;

		Date searchDate = strDateConverter.strDateConvert(strSearchDate);

		// Viewに渡すプルダウンリストを作成するメソッド呼び出し
		Map DayPriceMap = new HashMap();
		DayPriceMap = getDayPriceMap();

		PriceSearchForm psf = new PriceSearchForm();
		model.addAttribute("PriceSearchForm", psf);
		model.addAttribute("YearList", DayPriceMap.get("YearList"));
		model.addAttribute("MonthList", DayPriceMap.get("MonthList"));
		model.addAttribute("DayList", DayPriceMap.get("DayList"));
		model.addAttribute("PriceSearchList",DayPriceMap.get("PriceSearchList"));

		// 指定された曜日を調べ平日なら選択された株価の種類の値を表示する
		// 土日祝の場合は取得しない
		if (nsmHolidayChecker.isHoliday(searchDate)
				|| (nsmHolidayChecker.isSundayOrSaturday(searchDate))) {

			model.addAttribute("message", year + "年" + month + "月" + day
					+ "日は土日祝の為、休場です。");

			return "PriceSearch/resultPriceSearch2";

		} else {

			List checkSearchList = nikkeiDateDAOImpl.getAll();

			//選択された日付のデータが存在しなければ、エラーメッセージを返す
			if(nsmControllerFacade.checkSearchDateFacade(strSearchDate2, checkSearchList)){

			String searchKind = new String(priceSearchForm.getPRICE_SEARCH()
					.getBytes("ISO-8859-1"), "UTF-8");
			String SEARCH_KIND = strDateConverter.searchConverter(searchKind);
			double searchPrice = getSearchKindPrice(strSearchDate, SEARCH_KIND);

			model.addAttribute("year", year);
			model.addAttribute("month", month);
			model.addAttribute("day", day);
			model.addAttribute("price_search", searchKind);
			model.addAttribute("rate", searchPrice);

			return "PriceSearch/resultPriceSearch";

			}else{

				model.addAttribute("message", year + "年" + month + "月" + day
						+ "日はデータが入力されていません。");

				return "PriceSearch/resultPriceSearch2";
			}
		}
	}

	/**
	 * 日付を指定して平均を見るページ「平均を算出する」ボタンのPOSTメソッド
	 * 指定された範囲の値を平均し、verageSearchResult.jspにレンダリングする
	 *
	 * @throws UnsupportedEncodingException
	 */
	@RequestMapping(value = "/LoginMenu", method = RequestMethod.POST, params = "average_price_culculate")
	public String culcurateAverage(@ModelAttribute AverageSearchForm averageSearchForm, Model model) throws UnsupportedEncodingException{

		// 検索条件に指定されたパラメータから日付を作成する
		String startYear = averageSearchForm.getStartYEAR();
		String startMonth = averageSearchForm.getStartMONTH();
		String startDay = averageSearchForm.getStartDAY();
		String endYear = averageSearchForm.getEndYEAR();
		String endMonth = averageSearchForm.getEndMONTH();
		String endDay = averageSearchForm.getEndDAY();
		String searchPriceKind = new String(averageSearchForm.getPRICE_SEARCH()
				.getBytes("ISO-8859-1"), "UTF-8");

		// Viewに渡す変数を作成
		AverageSearchForm asf = new AverageSearchForm();
		Map DayPriceMap = new HashMap();
		DayPriceMap = getDayPriceMap();

		int eqStartDay = Integer.parseInt(startYear + startMonth + startDay);
		int eqEndDay = Integer.parseInt(endYear + endMonth + endDay);

		model.addAttribute("AverageSearchForm", asf);
		model.addAttribute("title", applicationProperties.getProperty("AverageSearchTop.title"));
		model.addAttribute("startDate", applicationProperties.getProperty("AverageSearchTop.startDate"));
		model.addAttribute("endDate", applicationProperties.getProperty("AverageSearchTop.endDate"));
		model.addAttribute("rateKind", applicationProperties.getProperty("AverageSearchTop.rateKind"));
		model.addAttribute("YearList", DayPriceMap.get("YearList"));
		model.addAttribute("MonthList", DayPriceMap.get("MonthList"));
		model.addAttribute("DayList", DayPriceMap.get("DayList"));
		model.addAttribute("PriceSearchList", DayPriceMap.get("PriceSearchList"));

		if(eqStartDay <= eqEndDay){

			//作成した日付データを加工し、引数に設定してリストを取得する
			String startDate = startYear + "/" + startMonth + "/" + startDay;
			String endDate = endYear + "/" + endMonth + "/" + endDay;
			List<NikkeiDateEntity> aveList = nikkeiDateDAOImpl.findAverage(startDate, endDate);

			//AlthemicException対策用if文
			//Listのサイズが0かどうか確認
			if(aveList.size() != 0){
				String searchKind = strDateConverter.searchConverter(searchPriceKind);

				//平均算出用メソッドを呼び出し、結果取得
				int resultDate = nsmControllerFacade.listAverageResolverFacade(aveList, searchKind);

				model.addAttribute("startYear", startYear);
				model.addAttribute("startMonth", startMonth);
				model.addAttribute("startDay", startDay);
				model.addAttribute("endYear", endYear);
				model.addAttribute("endMonth", endMonth);
				model.addAttribute("endDay", endDay);
				model.addAttribute("search_kind", searchPriceKind);
				model.addAttribute("resultDate", resultDate);

				return "AverageSearch/AverageSearchResult";

			//平均算出用リストが0の場合はエラーメッセージを送信する
			}else{

				model.addAttribute("DateChoiceError", applicationProperties.getProperty("AverageSearchTop.AlthemicError"));

				return "AverageSearch/AverageSearchTop";
			}
		}

		//日付の指定がおかしい場合、エラーメッセージを出力し、
		//AverageSearchResult.jspにリダイレクトする
		else{

			model.addAttribute("DateChoiceError", applicationProperties.getProperty("AverageSearchTop.DateChoiceError"));

			return "AverageSearch/AverageSearchTop";

		}
	}

	/**
	 * 日付を指定して平均を見るページ「選択した日付で出力」ボタンのPOSTメソッド
	 * 指定された範囲の値を平均し、verageSearchResult.jspにレンダリングする
	 *
	 * @throws UnsupportedEncodingException
	 */
	@RequestMapping(value = "/LoginMenu", method = RequestMethod.POST, params = "date_export")
	public String culcurateAverage(@ModelAttribute DateExportForm dateExportForm, Model model){

		// 検索条件に指定されたパラメータから日付を作成する
		String startYear = dateExportForm.getStartYEAR();
		String startMonth = dateExportForm.getStartMONTH();
		String startDay = dateExportForm.getStartDAY();
		String endYear = dateExportForm.getEndYEAR();
		String endMonth = dateExportForm.getEndMONTH();
		String endDay = dateExportForm.getEndDAY();

		//Viewに渡す変数を作成
		DateExportForm def = new DateExportForm();
		Map DayPriceMap = new HashMap();
		DayPriceMap = getDayPriceMap();

		int eqStartDay = Integer.parseInt(startYear + startMonth + startDay);
		int eqEndDay = Integer.parseInt(endYear + endMonth + endDay);

		if(eqStartDay <= eqEndDay){

			//作成した日付データを加工し、引数に設定してリストを取得する
			String startDate = startYear + "/" + startMonth + "/" + startDay;
			String endDate = endYear + "/" + endMonth + "/" + endDay;
			List<NikkeiDateEntity> exportTmpList = nikkeiDateDAOImpl.findAverage(startDate, endDate);

			//AlthemicException対策用if文
			//Listのサイズが0かどうか確認
			if(exportTmpList.size() != 0){

				//ListをJSP出力用へ変換
				List<List<String>> exportList = nsmControllerFacade.getExportListFacade(exportTmpList);

				//ヘッダーを取得しDateExportResultへ出力
				List<String> headerList = nsmControllerFacade.getHeaderListFacade();
				model.addAttribute("headerList", headerList);
				model.addAttribute("exportList", exportList);
				model.addAttribute("message", applicationProperties.getProperty("DateExport.resultmessage"));

				return "DateExport/DateExportResult";

			//出力リストが0の場合はエラーメッセージを送信する
			}else{

				model.addAttribute("DateExportForm", def);
				model.addAttribute("title", applicationProperties.getProperty("DateExport.title"));
				model.addAttribute("startDate", applicationProperties.getProperty("DateExport.start"));
				model.addAttribute("endDate", applicationProperties.getProperty("DateExport.end"));
				model.addAttribute("YearList", DayPriceMap.get("YearList"));
				model.addAttribute("MonthList", DayPriceMap.get("MonthList"));
				model.addAttribute("DayList", DayPriceMap.get("DayList"));

				model.addAttribute("DateChoiceError", applicationProperties.getProperty("DateExport.AlthemicError"));

				return "DateExport/DateExport";
			}
		}

		//日付の指定がおかしい場合、エラーメッセージを出力し、
		//DateExportにリダイレクトする
		else{

			model.addAttribute("DateExportForm", def);
			model.addAttribute("title", applicationProperties.getProperty("DateExport.title"));
			model.addAttribute("startDate", applicationProperties.getProperty("DateExport.start"));
			model.addAttribute("endDate", applicationProperties.getProperty("DateExport.end"));
			model.addAttribute("YearList", DayPriceMap.get("YearList"));
			model.addAttribute("MonthList", DayPriceMap.get("MonthList"));
			model.addAttribute("DayList", DayPriceMap.get("DayList"));

			model.addAttribute("DateChoiceError", applicationProperties.getProperty("DateExport.DateChoiceError"));

			return "DateExport/DateExport";

		}
	}

	/**
	 * 選択したデータを出力するページの「データ出力ページに戻る」ボタンのPOSTメソッド
	 * DateExport.jspへレンダリングする
	 *
	 * @throws UnsupportedEncodingException
	 */
	@RequestMapping(value = "/LoginMenu", method = RequestMethod.POST, params = "return_export_menu")
	public String dateExportTop(Model model){

		// Viewに渡すプルダウンリストを作成するメソッド呼び出し
		Map DayPriceMap = new HashMap();
		DayPriceMap = getDayPriceMap();

		DateExportForm def = new DateExportForm();
		model.addAttribute("DateExportForm", def);
		model.addAttribute("title", applicationProperties.getProperty("DateExport.title"));
		model.addAttribute("startDate", applicationProperties.getProperty("DateExport.start"));
		model.addAttribute("endDate", applicationProperties.getProperty("DateExport.end"));
		model.addAttribute("YearList", DayPriceMap.get("YearList"));
		model.addAttribute("MonthList", DayPriceMap.get("MonthList"));
		model.addAttribute("DayList", DayPriceMap.get("DayList"));

		return "DateExport/DateExport";

	}

	/**
	 * 「トップメニューに戻る」ボタンのPOSTメソッド
	 * TopMenu.jspへレンダリングする
	 *
	 * @throws UnsupportedEncodingException
	 */
	@RequestMapping(value = "/LoginMenu", method = RequestMethod.POST, params = "return_top_menu")
	public String culcurateAverage(Model model){

		//記憶しているログインしたユーザーIDを渡してトップメニューへ戻る
		model.addAttribute("title", userInfoDTO.getUSER_NAME() + applicationProperties.getProperty("TopMenu.welcommessage"));
		model.addAttribute("message", applicationProperties.getProperty("TopMenu.choicemessage"));

		return "TopMenu";

	}

	/**
	 * 「ログアウト」ボタンのPOSTメソッド
	 * LogoutPage.jspへレンダリングする
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/LoginMenu", method = RequestMethod.POST, params = "logout")
	public String logoutAction(Model model){

		//ログアウトする

		if(nsmControllerFacade.logoutActionFacade()){

			model.addAttribute("title", applicationProperties.getProperty("LogoutPage.title"));
			model.addAttribute("message", applicationProperties.getProperty("LogoutPage.message"));

			return "LogoutPage";

			//ファイル管理処理で例外が発生していた場合は例外ページに移動する

		}else{
			model.addAttribute("subject", applicationProperties.getProperty("NSMFileManagementException.subject"));
			model.addAttribute("message", applicationProperties.getProperty("NSMFileManagementException.message"));
			return "Exception/NSMFileManagementException";
		}
	}

	/**
	 * 過去の株価検索で使用するプルダウンリストを作成するメソッド
	 */
	private Map getDayPriceMap() {

		// 戻り値用のマップを作成
		Map<String, List<String>> resultMap = new HashMap<String, List<String>>();

		List<Map<String, Object>> yearList = jdbcTemplate
				.queryForList("select year from nikkei_year_mst");
		List<Map<String, Object>> monthList = jdbcTemplate
				.queryForList("select * from nikkei_month_mst");
		List<Map<String, Object>> dayList = jdbcTemplate
				.queryForList("select * from nikkei_day_mst");
		List<Map<String, Object>> priceSearchList = jdbcTemplate
				.queryForList("select * from nikkei_price_kind_mst");

		List YearList = new ArrayList();
		List MonthList = new ArrayList();
		List DayList = new ArrayList();
		List PriceSearchList = new ArrayList();

		for (int i = 0; i < yearList.size(); i++) {
			YearList.add(yearList.get(i).get("year"));
		}

		for (int i = 0; i < monthList.size(); i++) {
			MonthList.add(monthList.get(i).get("month"));
		}

		for (int i = 0; i < dayList.size(); i++) {
			DayList.add(dayList.get(i).get("day"));
		}

		for (int i = 0; i < priceSearchList.size(); i++) {
			PriceSearchList.add(priceSearchList.get(i).get("price_kind"));
		}

		resultMap.put("YearList", YearList);
		resultMap.put("MonthList", MonthList);
		resultMap.put("DayList", DayList);
		resultMap.put("PriceSearchList", PriceSearchList);

		return resultMap;
	}

	private double getSearchKindPrice(String searchDate, String searchKind) {

		double resultSearchKindPrice = 0;

		List<NikkeiDateEntity> searchdateList = nikkeiDateDAOImpl
				.findByDate(searchDate);

		if (searchKind.equals("open_rate")) {

			resultSearchKindPrice = searchdateList.get(0).getOpen_rate();

		} else if (searchKind.equals("high_price")) {

			resultSearchKindPrice = searchdateList.get(0).getHigh_price();

		} else if (searchKind.equals("low_price")) {

			resultSearchKindPrice = searchdateList.get(0).getLow_price();

		} else if (searchKind.equals("close_rate")) {

			resultSearchKindPrice = searchdateList.get(0).getClose_rate();

		}

		return resultSearchKindPrice;
	}

}
