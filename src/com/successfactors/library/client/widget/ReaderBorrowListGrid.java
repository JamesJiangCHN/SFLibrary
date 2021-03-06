package com.successfactors.library.client.widget;

import static com.successfactors.library.client.SFLibrary.borrowService;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.smartgwt.client.data.Record;
import com.smartgwt.client.types.ListGridFieldType;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.grid.events.CellDoubleClickEvent;
import com.smartgwt.client.widgets.grid.events.CellDoubleClickHandler;
import com.successfactors.library.client.SFLibrary;
import com.successfactors.library.client.datasource.SLBorrowDS;
import com.successfactors.library.client.helper.RPCCall;
import com.successfactors.library.shared.BorrowStatusType;
import com.successfactors.library.shared.model.BorrowPage;
import com.successfactors.library.shared.model.SLBorrow;

public class ReaderBorrowListGrid extends ListGrid {

	public static final int DEFAULT_RECORDS_EACH_PAGE = 16;
	public static final int DEFAULT_IMG_HEIGHT = 40;
	public static final int DEFAULT_IMG_WIDTH = 28;

	public static final int DEFAULT_CELL_HEIGHT = 42;
	
	private Refreshable jumpBar;
	
	private SLBorrowDS slBorrowDS = new SLBorrowDS();
	private int pageNowNum = 1;
	private int pageTotalNum = 1;
	
	public ReaderBorrowListGrid(Refreshable jumpbar) {
		super();
		jumpBar = jumpbar;
		
		GWT.log("初始化: ReaderBorrowListGrid");
		
		this.setShowAllRecords(true);
		this.setSortField("bookClass");
		this.setCellHeight(DEFAULT_CELL_HEIGHT);
		
		ListGridField bookPicUrlField = new ListGridField("bookPicUrl", "封面", 60);
		//bookPicUrlField.setImageURLPrefix("/images/upload/");
		bookPicUrlField.setType(ListGridFieldType.IMAGE);
		bookPicUrlField.setImageHeight(DEFAULT_IMG_HEIGHT);
		bookPicUrlField.setImageWidth(DEFAULT_IMG_WIDTH);

		ListGridField borrowIdField = new ListGridField("borrowId", "借阅编号", 100);
		ListGridField bookNameField = new ListGridField("bookName", "书名");
		ListGridField bookISBNField = new ListGridField("bookISBN", "ISBN");
		ListGridField userNameField = new ListGridField("userName", "借书人");
		
		ListGridField borrowDateField = new ListGridField("borrowDate", "借书日期");
		ListGridField shouldReturnDateField = new ListGridField("shouldReturnDate", "应还日期");
		
		ListGridField inStoreField = new ListGridField("inStore", "需要领取");
		ListGridField overdueField = new ListGridField("overdue", "是否超期");
		ListGridField statusField = new ListGridField("status", "状态");
		
		this.setFields(
				bookPicUrlField,
				borrowIdField,
				bookNameField,
				bookISBNField,
				userNameField,
				borrowDateField,
				shouldReturnDateField,
				inStoreField,
				overdueField,
				statusField
				);
		
		updateDS(DEFAULT_RECORDS_EACH_PAGE, 1);
		this.setDataSource(slBorrowDS);
		this.setAutoFetchData(true);
		
		bind();
		
	}
	
	private void bind() {
		this.addCellDoubleClickHandler(new CellDoubleClickHandler() {
			
			@Override
			public void onCellDoubleClick(CellDoubleClickEvent event) {
				BorrowDisplayWindow borrowDisplayWindow = new BorrowDisplayWindow(getSelectedRecord());
				borrowDisplayWindow.show();
			}
		});
	}
	
	private void updateDS(final int itemsPerPage, final int pageNum) {
		new RPCCall<BorrowPage>() {
			@Override
			public void onFailure(Throwable caught) {
				SC.say("通信失败，请检查您的网络连接！");
			}
			@Override
			public void onSuccess(BorrowPage result) {
				if (result == null) {
					SC.say("暂无资料。。。囧rz");
					return;
				}
				for (SLBorrow borrow : result.getTheBorrows()) {
					slBorrowDS.addData(borrow.getRecord());
				}
				pageNowNum = result.getPageNum();
				pageTotalNum = result.getTotalPageNum();
				jumpBar.refreshView(pageNowNum, pageTotalNum);
			}
			@Override
			protected void callService(AsyncCallback<BorrowPage> cb) {
				borrowService.getBorrowList(BorrowStatusType.NOW, SFLibrary.get().getNowUser().getUserEmail(), itemsPerPage, pageNum, cb);
			}
		}.retry(3);
	}

	public void doNextPage() {
		
		if (pageNowNum >= pageTotalNum) {
			SC.say("已到最后一页！");
			return;
		}
		
		for (Record record : this.getRecords()) {
			slBorrowDS.removeData(record);
		}
		
			new RPCCall<BorrowPage>() {
				@Override
				public void onFailure(Throwable caught) {
					SC.say("通信失败，请检查您的网络连接！");
				}
				@Override
				public void onSuccess(BorrowPage result) {
					
					if (result == null) {
						SC.say("暂无资料。。。囧rz");
						return;
					}
					for (SLBorrow borrow : result.getTheBorrows()) {
						slBorrowDS.addData(borrow.getRecord());
					}
					pageNowNum = result.getPageNum();
					pageTotalNum = result.getTotalPageNum();
					jumpBar.refreshView(pageNowNum, pageTotalNum);
				}
				@Override
				protected void callService(AsyncCallback<BorrowPage> cb) {
					borrowService.getBorrowList(BorrowStatusType.NOW, SFLibrary.get().getNowUser().getUserEmail(), DEFAULT_RECORDS_EACH_PAGE, pageNowNum+1, cb);
				}
			}.retry(3);
		}

	public void doPrePage() {

		if (pageNowNum <= 1) {
			SC.say("已到第一页！");
			return;
		}
		
		for (Record record : this.getRecords()) {
			slBorrowDS.removeData(record);
		}
			
			new RPCCall<BorrowPage>() {
				@Override
				public void onFailure(Throwable caught) {
					SC.say("通信失败，请检查您的网络连接！");
				}
				@Override
				public void onSuccess(BorrowPage result) {
					
					if (result == null) {
						SC.say("暂无资料。。。囧rz");
						return;
					}
					for (SLBorrow borrow : result.getTheBorrows()) {
						slBorrowDS.addData(borrow.getRecord());
					}
					pageNowNum = result.getPageNum();
					pageTotalNum = result.getTotalPageNum();
					jumpBar.refreshView(pageNowNum, pageTotalNum);
				}
				@Override
				protected void callService(AsyncCallback<BorrowPage> cb) {
					borrowService.getBorrowList(BorrowStatusType.NOW, SFLibrary.get().getNowUser().getUserEmail(), DEFAULT_RECORDS_EACH_PAGE, pageNowNum-1, cb);
				}
			}.retry(3);
		}
	
}
