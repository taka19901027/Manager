package jp.co.techtone.nsm.facade;

import java.util.Date;
import java.util.List;

import jp.co.techtone.nsm.entity.NikkeiDateEntity;
import jp.co.techtone.nsm.form.PriceInsertForm;
import jp.co.techtone.nsm.logic.NsmControllerLogic;
import jp.co.techtone.nsm.logic.NsmDBLogic;


/**
 * NsmControllerからの処理呼び出しメソッド
 * 処理内容に応じてロジックに振り分ける
 * @author taka
 *
 */
public class NsmControllerFacade {

	NsmDBLogic nsmDBLogic = new NsmDBLogic();
	NsmControllerLogic nsmControllerLogic = new NsmControllerLogic();

	public boolean createLogFileFacade(String User, Date todayDate){

		boolean createFileResult = nsmControllerLogic.createLogFile(User, todayDate);

		return createFileResult;

	}

	public boolean logoutActionFacade(){

		boolean logoutActionResult = nsmControllerLogic.logoutAction();

		return logoutActionResult;

	}

	public boolean checkLoginDataFacade(String user_id, String password, List userList){

		boolean resultUserCheck = nsmDBLogic.checkLoginData(user_id, password, userList);

		return resultUserCheck;
	}

	public int listAverageResolverFacade(List<NikkeiDateEntity> averageRateList, String searchKind){

		int resultInt = 0;

		resultInt = nsmDBLogic.listAverageResolver(averageRateList, searchKind);

		return resultInt;

	}

	public boolean CheckPriceInsertFacade(PriceInsertForm priceInsertForm){

		boolean resultCheck = nsmControllerLogic.CheckPriceInsert(priceInsertForm);

		return resultCheck;
	}

	public NikkeiDateEntity createInsertEntityFacade(PriceInsertForm priceInsertForm){

		NikkeiDateEntity insertEntity = nsmControllerLogic.createInsertEntity(priceInsertForm);

		return insertEntity;
	}

	public boolean CheckPriceDeleteFacade(PriceInsertForm priceInsertForm){

		boolean resultCheck = nsmControllerLogic.CheckPriceDelete(priceInsertForm);

		return resultCheck;
	}

	public NikkeiDateEntity createDeleteEntityFacade(PriceInsertForm priceInsertForm){

		NikkeiDateEntity deleteEntity = nsmControllerLogic.createDeleteEntity(priceInsertForm);

		return deleteEntity;

	}

	public boolean deleteEntityCheckFacade(String checkOnlyDeleteDate,List<NikkeiDateEntity> deleteCheckList){

		boolean deleteCheckResult = nsmDBLogic.deleteEntityCheck(checkOnlyDeleteDate, deleteCheckList);

		return deleteCheckResult;

	}

	public String createCheckOnlyDateFacade(PriceInsertForm priceInsertForm){

		String resultCheckOnlyDate = nsmDBLogic.createCheckOnlyDate(priceInsertForm);

		return resultCheckOnlyDate;

	}

	public boolean checkSearchDateFacade(String strSearchDate2, List<NikkeiDateEntity> checkSearchList){

		boolean checkSearchDateResult = nsmDBLogic.deleteEntityCheck(strSearchDate2, checkSearchList);

		return checkSearchDateResult;
	}

	public List<String> getHeaderListFacade(){

		List<String> headerList = nsmControllerLogic.getHeaderList();

		return headerList;

	}

	public List<List<String>> getExportListFacade(List<NikkeiDateEntity> exportList){

		List<List<String>> exportResultList = nsmDBLogic.getExportList(exportList);

		return exportResultList;
	}

}
