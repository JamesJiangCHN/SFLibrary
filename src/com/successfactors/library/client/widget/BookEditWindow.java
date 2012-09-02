package com.successfactors.library.client.widget;

import java.util.Date;

import com.smartgwt.client.data.Record;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.TitleOrientation;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.Img;
import com.smartgwt.client.widgets.Window;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.DateItem;
import com.smartgwt.client.widgets.form.fields.SelectItem;
import com.smartgwt.client.widgets.form.fields.StaticTextItem;
import com.smartgwt.client.widgets.form.fields.TextAreaItem;
import com.smartgwt.client.widgets.form.fields.TextItem;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.VLayout;
import com.successfactors.library.client.datasource.SLBookDS;
import com.successfactors.library.shared.model.SLBook;

public class BookEditWindow  extends Window {

	private static final String WINDOW_WIDTH = "620px";
	private static final String WINDOW_HEIGHT = "360px";
	private static final int IMG_HEIGHT = 165;
	private static final int IMG_WIDTH = 116;
	
	private SLBook theBook;

	private DynamicForm bookForm1;
	private DynamicForm bookForm2;
	private DynamicForm bookForm3;
	private IButton submitButton;
	private IButton newButton;
	private IButton uploadPicButton;
	
	public BookEditWindow() {
		super();
		theBook = new SLBook();
		initNewWindow();
	}
	
	public BookEditWindow(SLBook slBook) {
		super();
		theBook = slBook;
		initEditWindow();
	}
	
	public BookEditWindow(Record slBookRc) {
		super();
		theBook = SLBook.parse(slBookRc);
		initEditWindow();
	}
	
	private void initEditWindow() {
		
		String strBookName = theBook.getBookName();
		
		this.setAutoSize(true);
		this.setTitle("《"+strBookName+"》"+"修改信息");
		this.setCanDragReposition(true);
		this.setCanDragResize(false);
		this.setAutoCenter(true);
		this.setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
		
		VLayout vLayout;
		HLayout hLayout;
		HLayout buttonLayout;
		
		vLayout = new VLayout();
		vLayout.setWidth(WINDOW_WIDTH);
		vLayout.setHeight(WINDOW_HEIGHT);
		vLayout.setBorder("2px solid #7598C7");
		vLayout.setMargin(12);
		vLayout.setPadding(14);
		
		hLayout = new HLayout();
		hLayout.setWidth(WINDOW_WIDTH);

		//HLayout ---------------------------------------------------------------------------------------
		String strBookPicUrl = theBook.getBookPicUrl();
		VLayout imgVLayout = new VLayout();
		imgVLayout.setWidth(IMG_WIDTH);
		Img bookPicUrlItem = new Img("/images/upload/"+strBookPicUrl, IMG_WIDTH, IMG_HEIGHT);
		
		uploadPicButton = new IButton("上传封面");
		uploadPicButton.setIcon("actions/plus.png");
		uploadPicButton.setWidth(IMG_WIDTH);
		
		imgVLayout.setMembers(bookPicUrlItem, uploadPicButton);
		imgVLayout.setMembersMargin(10);
		
		//Form 1-----------------------------------------------------------------------------------------
		SLBookDS theDataSource = new SLBookDS();
		Record theRecord = theBook.getRecord();
		theDataSource.addData(theRecord);
		
		bookForm1 = new DynamicForm();
		bookForm1.setDataSource(theDataSource);
		bookForm1.setNumCols(4);
		bookForm1.setWidth("*");
		bookForm1.setColWidths(100, "*", 100, "*");
		bookForm1.setCellPadding(5);
		
		TextItem bookNameItem = new TextItem("bookName", "书名");
		bookNameItem.setColSpan(4);
		bookNameItem.setWidth("100%");
		bookNameItem.setTitleStyle("alex_bookdisplaywindow_form_text_title");
		bookNameItem.setTextBoxStyle("alex_bookdisplaywindow_form_text_content");
		
		TextItem bookAuthorItem = new TextItem("bookAuthor", "作者");
		bookAuthorItem.setColSpan(4);
		bookAuthorItem.setWidth("100%");
		bookAuthorItem.setTitleStyle("alex_bookdisplaywindow_form_text_title");
		bookAuthorItem.setTextBoxStyle("alex_bookdisplaywindow_form_text_content");
		
		TextItem bookISBNItem = new TextItem("bookISBN", "ISBN");
		bookISBNItem.setColSpan(4);
		bookISBNItem.setWidth("100%");
		bookISBNItem.setTitleStyle("alex_bookdisplaywindow_form_text_title");
		bookISBNItem.setTextBoxStyle("alex_bookdisplaywindow_form_text_content");
		
		TextItem bookPublisherItem = new TextItem("bookPublisher", "出版社");
		bookPublisherItem.setTitleStyle("alex_bookdisplaywindow_form_text_title");
		bookPublisherItem.setTextBoxStyle("alex_bookdisplaywindow_form_text_content");
		
		DateItem bookPublishDateItem = new DateItem("bookPublishDate", "出版日期");
		bookPublishDateItem.setTitleStyle("alex_bookdisplaywindow_form_text_title");
		bookPublishDateItem.setTextBoxStyle("alex_bookdisplaywindow_form_text_content");
		bookPublishDateItem.setUseTextField(true);  
		
		SelectItem bookClassItem = new SelectItem("bookClass", "类别");
		bookClassItem.setTitleStyle("alex_bookdisplaywindow_form_text_title");
		bookClassItem.setTextBoxStyle("alex_bookdisplaywindow_form_text_content");
		bookClassItem.setValueMap(
				"计算机/网络",
				"小说/文学",
				"哲学/文化",
				"经济/管理",
				"政治/军事",
				"法律",
				"历史",
				"其他"
				);
		bookClassItem.setDefaultToFirstOption(true);
		
		SelectItem bookLanguageItem = new SelectItem("bookLanguage", "语言");
		bookLanguageItem.setTitleStyle("alex_bookdisplaywindow_form_text_title");
		bookLanguageItem.setTextBoxStyle("alex_bookdisplaywindow_form_text_content");
		bookLanguageItem.setValueMap("中文", "英语", "法语", "德语", "日语", "俄语", "韩语");
		bookLanguageItem.setDefaultToFirstOption(true);
		
		TextItem bookContributorItem = new TextItem("bookContributor", "贡献者");
		bookContributorItem.setTitleStyle("alex_bookdisplaywindow_form_text_title");
		bookContributorItem.setTextBoxStyle("alex_bookdisplaywindow_form_text_content");
		
		TextItem bookPriceItem = new TextItem("bookPrice", "价格");
		bookPriceItem.setTitleStyle("alex_bookdisplaywindow_form_text_title");
		bookPriceItem.setTextBoxStyle("alex_bookdisplaywindow_form_text_content");
		
		bookForm1.setFields(
				bookNameItem, 
				bookAuthorItem, 
				bookISBNItem,
				bookPublisherItem, 
				bookPublishDateItem,
				bookClassItem,
				bookLanguageItem,
				bookContributorItem,
				bookPriceItem);
				
		bookForm1.selectRecord(theRecord);
		bookForm1.fetchData();
		
		//Form 2-----------------------------------------------------------------------------------------
		bookForm2 = new DynamicForm();
		bookForm2.setDataSource(theDataSource);
		bookForm2.setNumCols(3);
		bookForm2.setWidth(WINDOW_WIDTH);
		bookForm2.setColWidths("*","*","*");
		bookForm2.setCellPadding(3);
		bookForm2.setCellBorder(1);
		bookForm2.setTitleOrientation(TitleOrientation.TOP);

		StaticTextItem bookTotalQuantityItemTitle = new StaticTextItem("bookTotalQuantityTitle", "");
		bookTotalQuantityItemTitle.setTextBoxStyle("alex_bookdisplaywindow_form_header");
		bookTotalQuantityItemTitle.setShowTitle(false);

		StaticTextItem bookInStoreQuantityitemTitle = new StaticTextItem("bookInStoreQuantityTitle", "");
		bookInStoreQuantityitemTitle.setTextBoxStyle("alex_bookdisplaywindow_form_header");
		bookInStoreQuantityitemTitle.setShowTitle(false);

		StaticTextItem bookAvailableQuantityItemTitle = new StaticTextItem("bookAvailableQuantityTitle", "");
		bookAvailableQuantityItemTitle.setTextBoxStyle("alex_bookdisplaywindow_form_header");
		bookAvailableQuantityItemTitle.setShowTitle(false);
		
		TextItem bookTotalQuantityItem = new TextItem("bookTotalQuantity", "");
		bookTotalQuantityItem.setTextBoxStyle("alex_bookdisplaywindow_form_text_content");
		bookTotalQuantityItem.setShowTitle(false);

		TextItem bookInStoreQuantityitem = new TextItem("bookInStoreQuantity", "");
		bookInStoreQuantityitem.setTextBoxStyle("alex_bookdisplaywindow_form_text_content");
		bookInStoreQuantityitem.setShowTitle(false);

		TextItem bookAvailableQuantityItem = new TextItem("bookAvailableQuantity", "");
		bookAvailableQuantityItem.setTextBoxStyle("alex_bookdisplaywindow_form_text_content");
		bookAvailableQuantityItem.setShowTitle(false);
		
		bookForm2.setFields(
				bookTotalQuantityItemTitle,
				bookInStoreQuantityitemTitle,
				bookAvailableQuantityItemTitle,
				bookTotalQuantityItem,
				bookInStoreQuantityitem,
				bookAvailableQuantityItem
				);
		
		bookForm2.selectRecord(theRecord);
		bookForm2.fetchData();
		
		//Form 3-----------------------------------------------------------------------------------------
		bookForm3 = new DynamicForm();
		bookForm3.setDataSource(theDataSource);
		bookForm3.setWidth(WINDOW_WIDTH);
		bookForm3.setCellPadding(3);
		//bookForm2.setNumCols(2);
		bookForm3.setTitleOrientation(TitleOrientation.TOP);

		StaticTextItem bookIntroItemTitle = new StaticTextItem("bookIntroTitle", "");
		bookIntroItemTitle.setTextBoxStyle("alex_bookdisplaywindow_form_text_title");
		bookIntroItemTitle.setShowTitle(false);

		TextAreaItem bookIntroItem = new TextAreaItem("bookIntro", "");
		bookIntroItem.setTextBoxStyle("alex_bookdisplaywindow_form_intro_content");
		bookIntroItem.setShowTitle(false);
		bookIntroItem.setColSpan(2);
		bookIntroItem.setWidth("100%");

		bookForm3.setFields(bookIntroItemTitle, bookIntroItem);
		
		bookForm3.selectRecord(theRecord);
		bookForm3.fetchData();
		
		//buttonLayout --------------------------------------------------------------------------------------
		buttonLayout = new HLayout();
		submitButton = new IButton("提交修改");
		submitButton.setIcon("actions/approve.png");
		buttonLayout.setMembers(submitButton);
		buttonLayout.setAlign(Alignment.RIGHT);
		
		hLayout.setMembers(imgVLayout, bookForm1);
		vLayout.setMembers(hLayout, bookForm2, bookForm3, buttonLayout);
		vLayout.setMembersMargin(20);

		this.addItem(vLayout);
		
		bind();
	}
	
	private void initNewWindow() {
		
		this.setAutoSize(true);
		this.setTitle("新书入库");
		this.setCanDragReposition(true);
		this.setCanDragResize(false);
		this.setAutoCenter(true);
		this.setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
		
		VLayout vLayout;
		HLayout hLayout;
		HLayout buttonLayout;
		
		vLayout = new VLayout();
		vLayout.setWidth(WINDOW_WIDTH);
		vLayout.setHeight(WINDOW_HEIGHT);
		vLayout.setBorder("2px solid #7598C7");
		vLayout.setMargin(12);
		vLayout.setPadding(14);
		
		hLayout = new HLayout();
		hLayout.setWidth(WINDOW_WIDTH);

		//HLayout ---------------------------------------------------------------------------------------
		String strBookPicUrl = "nopic.jpg";
		VLayout imgVLayout = new VLayout();
		imgVLayout.setWidth(IMG_WIDTH);
		Img bookPicUrlItem = new Img("/images/upload/"+strBookPicUrl, IMG_WIDTH, IMG_HEIGHT);
		
		uploadPicButton = new IButton("上传封面");
		uploadPicButton.setIcon("actions/plus.png");
		uploadPicButton.setWidth(IMG_WIDTH);
		
		imgVLayout.setMembers(bookPicUrlItem, uploadPicButton);
		imgVLayout.setMembersMargin(10);
		
		//Form 1-----------------------------------------------------------------------------------------
		
		bookForm1 = new DynamicForm();
		bookForm1.setNumCols(4);
		bookForm1.setWidth("*");
		bookForm1.setColWidths(100, "*", 100, "*");
		bookForm1.setCellPadding(5);
		
		TextItem bookNameItem = new TextItem("bookName", "书名");
		bookNameItem.setColSpan(4);
		bookNameItem.setWidth("100%");
		bookNameItem.setTitleStyle("alex_bookdisplaywindow_form_text_title");
		bookNameItem.setTextBoxStyle("alex_bookdisplaywindow_form_text_content");
		
		TextItem bookAuthorItem = new TextItem("bookAuthor", "作者");
		bookAuthorItem.setColSpan(4);
		bookAuthorItem.setWidth("100%");
		bookAuthorItem.setTitleStyle("alex_bookdisplaywindow_form_text_title");
		bookAuthorItem.setTextBoxStyle("alex_bookdisplaywindow_form_text_content");
		
		TextItem bookISBNItem = new TextItem("bookISBN", "ISBN");
		bookISBNItem.setColSpan(4);
		bookISBNItem.setWidth("100%");
		bookISBNItem.setTitleStyle("alex_bookdisplaywindow_form_text_title");
		bookISBNItem.setTextBoxStyle("alex_bookdisplaywindow_form_text_content");
		
		TextItem bookPublisherItem = new TextItem("bookPublisher", "出版社");
		bookPublisherItem.setTitleStyle("alex_bookdisplaywindow_form_text_title");
		bookPublisherItem.setTextBoxStyle("alex_bookdisplaywindow_form_text_content");
		
		DateItem bookPublishDateItem = new DateItem("bookPublishDate", "出版日期");
		bookPublishDateItem.setTitleStyle("alex_bookdisplaywindow_form_text_title");
		bookPublishDateItem.setTextBoxStyle("alex_bookdisplaywindow_form_text_content");
		bookPublishDateItem.setUseTextField(true);  
		bookPublishDateItem.setDefaultChooserDate(new Date());
		
		SelectItem bookClassItem = new SelectItem("bookClass", "类别");
		bookClassItem.setTitleStyle("alex_bookdisplaywindow_form_text_title");
		bookClassItem.setTextBoxStyle("alex_bookdisplaywindow_form_text_content");
		bookClassItem.setValueMap(
				"计算机/网络",
				"小说/文学",
				"哲学/文化",
				"经济/管理",
				"政治/军事",
				"法律",
				"历史",
				"其他"
				);
		bookClassItem.setDefaultToFirstOption(true);
		
		SelectItem bookLanguageItem = new SelectItem("bookLanguage", "语言");
		bookLanguageItem.setTitleStyle("alex_bookdisplaywindow_form_text_title");
		bookLanguageItem.setTextBoxStyle("alex_bookdisplaywindow_form_text_content");
		bookLanguageItem.setValueMap("中文", "英语", "法语", "德语", "日语", "俄语", "韩语");
		bookLanguageItem.setDefaultToFirstOption(true);
		
		TextItem bookContributorItem = new TextItem("bookContributor", "贡献者");
		bookContributorItem.setTitleStyle("alex_bookdisplaywindow_form_text_title");
		bookContributorItem.setTextBoxStyle("alex_bookdisplaywindow_form_text_content");
		
		TextItem bookPriceItem = new TextItem("bookPrice", "价格");
		bookPriceItem.setTitleStyle("alex_bookdisplaywindow_form_text_title");
		bookPriceItem.setTextBoxStyle("alex_bookdisplaywindow_form_text_content");
		
		bookForm1.setFields(
				bookNameItem, 
				bookAuthorItem, 
				bookISBNItem,
				bookPublisherItem, 
				bookPublishDateItem,
				bookClassItem,
				bookLanguageItem,
				bookContributorItem,
				bookPriceItem);
				
		//Form 2-----------------------------------------------------------------------------------------
		bookForm2 = new DynamicForm();
		bookForm2.setNumCols(3);
		bookForm2.setWidth(WINDOW_WIDTH);
		bookForm2.setColWidths("*","*","*");
		bookForm2.setCellPadding(3);
		bookForm2.setCellBorder(1);
		bookForm2.setTitleOrientation(TitleOrientation.TOP);

		StaticTextItem bookTotalQuantityItemTitle = new StaticTextItem("bookTotalQuantityTitle", "");
		bookTotalQuantityItemTitle.setTextBoxStyle("alex_bookdisplaywindow_form_header");
		bookTotalQuantityItemTitle.setShowTitle(false);

		StaticTextItem bookInStoreQuantityitemTitle = new StaticTextItem("bookInStoreQuantityTitle", "");
		bookInStoreQuantityitemTitle.setTextBoxStyle("alex_bookdisplaywindow_form_header");
		bookInStoreQuantityitemTitle.setShowTitle(false);

		StaticTextItem bookAvailableQuantityItemTitle = new StaticTextItem("bookAvailableQuantityTitle", "");
		bookAvailableQuantityItemTitle.setTextBoxStyle("alex_bookdisplaywindow_form_header");
		bookAvailableQuantityItemTitle.setShowTitle(false);
		
		TextItem bookTotalQuantityItem = new TextItem("bookTotalQuantity", "");
		bookTotalQuantityItem.setTextBoxStyle("alex_bookdisplaywindow_form_text_content");
		bookTotalQuantityItem.setShowTitle(false);

		TextItem bookInStoreQuantityitem = new TextItem("bookInStoreQuantity", "");
		bookInStoreQuantityitem.setTextBoxStyle("alex_bookdisplaywindow_form_text_content");
		bookInStoreQuantityitem.setShowTitle(false);

		TextItem bookAvailableQuantityItem = new TextItem("bookAvailableQuantity", "");
		bookAvailableQuantityItem.setTextBoxStyle("alex_bookdisplaywindow_form_text_content");
		bookAvailableQuantityItem.setShowTitle(false);
		
		bookForm2.setFields(
				bookTotalQuantityItemTitle,
				bookInStoreQuantityitemTitle,
				bookAvailableQuantityItemTitle,
				bookTotalQuantityItem,
				bookInStoreQuantityitem,
				bookAvailableQuantityItem
				);
		
		//Form 3-----------------------------------------------------------------------------------------
		bookForm3 = new DynamicForm();
		bookForm3.setWidth(WINDOW_WIDTH);
		bookForm3.setCellPadding(3);
		//bookForm2.setNumCols(2);
		bookForm3.setTitleOrientation(TitleOrientation.TOP);

		StaticTextItem bookIntroItemTitle = new StaticTextItem("bookIntroTitle", "");
		bookIntroItemTitle.setTextBoxStyle("alex_bookdisplaywindow_form_text_title");
		bookIntroItemTitle.setShowTitle(false);

		TextAreaItem bookIntroItem = new TextAreaItem("bookIntro", "");
		bookIntroItem.setTextBoxStyle("alex_bookdisplaywindow_form_intro_content");
		bookIntroItem.setShowTitle(false);
		bookIntroItem.setColSpan(2);
		bookIntroItem.setWidth("100%");

		bookForm3.setFields(bookIntroItemTitle, bookIntroItem);
		
		//buttonLayout --------------------------------------------------------------------------------------
		buttonLayout = new HLayout();
		newButton = new IButton("添加图书");
		newButton.setIcon("actions/approve.png");
		buttonLayout.setMembers(newButton);
		buttonLayout.setAlign(Alignment.RIGHT);
		
		hLayout.setMembers(imgVLayout, bookForm1);
		vLayout.setMembers(hLayout, bookForm2, bookForm3, buttonLayout);
		vLayout.setMembersMargin(20);
		

		bookForm1.setValue("bookPublishDate", new Date());
		bookForm1.setValue("bookClass", "计算机/网络");
		bookForm1.setValue("bookLanguage", "中文");
		bookForm1.setValue("bookPrice", "0.00");
		
		bookForm2.setValue("bookTotalQuantity", "0");
		bookForm2.setValue("bookInStoreQuantity", "0");
		bookForm2.setValue("bookAvailableQuantity", "0");
		
		bookForm2.setValue("bookTotalQuantityTitle", "总数");
		bookForm2.setValue("bookInStoreQuantityTitle", "库中数量");
		bookForm2.setValue("bookAvailableQuantityTitle", "可借数量");
		bookForm3.setValue("bookIntroTitle", "简介：");
		
		this.addItem(vLayout);
		
		bind();
	}
	
	private void bind() {

		if (submitButton != null)
		submitButton.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				// TODO 前端：提交图书信息按钮事件
				updateBookInfo();
				SC.say("提交修改");
			}
		});
		if (newButton != null)
		newButton.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				// TODO 前端：提交新图书信息按钮事件
				updateBookInfo();
				SC.say("添加图书");
			}
		});
		if (uploadPicButton != null)
		uploadPicButton.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				// TODO 前端：提交图书信息按钮事件
				SC.say("上传图片");
			}
		});
	}
	
	private void updateBookInfo() {
		
		theBook.setBookName(bookForm1.getValueAsString("bookName"));
		theBook.setBookAuthor(bookForm1.getValueAsString("bookAuthor"));
		theBook.setBookISBN(bookForm1.getValueAsString("bookISBN"));
		theBook.setBookPublisher(bookForm1.getValueAsString("bookPublisher"));
		theBook.setBookPublishDate((Date)bookForm1.getValue("bookPublishDate"));
		theBook.setBookLanguage(bookForm1.getValueAsString("bookLanguage"));
		theBook.setBookPrice(Double.parseDouble((bookForm1.getValueAsString("bookPrice"))));
		theBook.setBookClass(bookForm1.getValueAsString("bookClass"));
		theBook.setBookContributor(bookForm1.getValueAsString("bookContributor"));
		theBook.setBookIntro(bookForm3.getValueAsString("bookIntro"));
		theBook.setBookTotalQuantity(Integer.parseInt(bookForm2.getValueAsString("bookTotalQuantity")));
		theBook.setBookInStoreQuantity(Integer.parseInt(bookForm2.getValueAsString("bookInStoreQuantity")));
		theBook.setBookAvailableQuantity(Integer.parseInt(bookForm2.getValueAsString("bookAvailableQuantity")));
		
	}
	
}