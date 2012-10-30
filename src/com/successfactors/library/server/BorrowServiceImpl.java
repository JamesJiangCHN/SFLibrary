package com.successfactors.library.server;

import java.util.Calendar;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.successfactors.library.client.service.BorrowService;
import com.successfactors.library.server.dao.MysqlBookDao;
import com.successfactors.library.server.dao.MysqlBorrowDao;
import com.successfactors.library.server.dao.SLUserDao;
import com.successfactors.library.shared.BorrowSearchType;
import com.successfactors.library.shared.BorrowStatusType;
import com.successfactors.library.shared.model.BorrowPage;
import com.successfactors.library.shared.model.SLBook;
import com.successfactors.library.shared.model.SLBorrow;
import com.successfactors.library.shared.model.SLUser;




@SuppressWarnings("serial")
public class BorrowServiceImpl extends RemoteServiceServlet implements BorrowService {

	private MysqlBorrowDao borrowDao= new MysqlBorrowDao();
	
	private MysqlBookDao bookDao = new MysqlBookDao();
	
	private SLUserDao userDao = new SLUserDao();
	
	private final static String USER_SESSION_KEY = "SF_LIB_USER"; 
	
	public MysqlBorrowDao getMysqlBorrowDao(){
		return this.borrowDao;
	}
	
	public MysqlBookDao getMysqlBookDao(){
		return this.bookDao;
	}
	
	public SLUserDao getMysqlUserDao(){
		return this.userDao;
	}
	
	/**
	 * 测试服务器连通
	 * */
	@Override
	public String helloServer(String strHello) {
		return "Hello " + strHello;
	}

	@Override
	public boolean borrowBook(String bookISBN) {
		HttpServletRequest request = null;
		HttpSession session = null;
		SLUser slUser = null;
		request = getThreadLocalRequest();
		if(request != null){
			session = request.getSession();
		}
		if(session != null){
			slUser = (SLUser) session.getAttribute(USER_SESSION_KEY);
		}
		SLBook slBook = bookDao.queryByISBN(bookISBN);
		if(slBook.getBookAvailableQuantity() > 0){
			slBook.setBookAvailableQuantity(slBook.getBookAvailableQuantity() - 1);
			SLBorrow slBorrow = new SLBorrow();
			
			Calendar c = Calendar.getInstance(); 
			Date borrowDate = c.getTime();
			c.add(Calendar.DAY_OF_MONTH, 15); 
			Date shouldReturnDate = c.getTime();
			if(slUser != null){
				slBorrow.setUserEmail(slUser.getUserEmail());
			}
			slBorrow.setBookISBN(bookISBN);
			slBorrow.setBorrowDate(borrowDate);
			slBorrow.setShouldReturnDate(shouldReturnDate);
			slBorrow.setInStore(false);
			slBorrow.setOverdue(false);
			slBorrow.setStatus("未归还");
			borrowDao.save(slBorrow);
			return true;
		}else{
			return false;
		}
	}

	@Override
	public boolean returnBook(int borrowId) {
		SLBorrow slBorrow = borrowDao.getBorrowById(borrowId);
		//
		slBorrow.setStatus("已归还");
		borrowDao.update(slBorrow);
		//
		SLBook slBook = slBorrow.getTheBook();
		slBook.setBookAvailableQuantity(slBook.getBookAvailableQuantity()+1);
		new BookServiceImpl().updateBook(slBook);
		return true;
	}

	@Override
	public boolean lostBook(int borrowId) {
		SLBorrow slBorrow = borrowDao.getBorrowById(borrowId);
		slBorrow.setStatus("已丢失");
		SLBook slBook = new BookServiceImpl().getBookByISBN(slBorrow.getBookISBN());
		slBook.setBookTotalQuantity(slBook.getBookAvailableQuantity() - 1);
		return true;
	}

	@Override
	public SLBorrow getBorrowInfo(int borrowId) {
		SLBorrow slBorrow = borrowDao.getBorrowById(borrowId);
		SLBook slBook =bookDao.queryByISBN(slBorrow.getBookISBN());
		SLUser slUser = userDao.getSLUserByEmail(slBorrow.getUserEmail());
		slBorrow.setTheBook(slBook);
		slBorrow.setTheUser(slUser);
		return slBorrow;
	}


	/**
	 * 获取某种状态下的，某用户的所有借阅信息
	 * @param statusType 借阅状态
	 * @param userEmail 用户邮箱地址，注：当userEmail==null时，对所有用户
	 * @param itemsPerPage
	 * @param pageNum
	 */
	@Override
	public BorrowPage getBorrowList(BorrowStatusType statusType,
			String userEmail, int itemsPerPage, int pageNum) {
		List<SLBorrow> result = borrowDao.searchBorrowList(statusType,itemsPerPage,pageNum);

		BorrowPage page = new BorrowPage(itemsPerPage, pageNum);
		
		for (int i = 0;i < 10;i++) {
			SLBorrow slBorrow = result.get(i);
			slBorrow.setTheBook(bookDao.queryByISBN(slBorrow.getBookISBN()));
			slBorrow.setTheUser(userDao.getSLUserByEmail(slBorrow.getUserEmail()));
			result.set(i, slBorrow);
		}
		page.setTheBorrows((ArrayList<SLBorrow>)result);
		page.setTotalPageNum(pageNum);
		return page;
	}

	/**
	 * 在某种状态下的借阅信息中，所有用户中，搜索
	 * @param statusType 借阅状态
	 * @param searchType 搜索类型
	 * @param searchValue 关键词
	 * @param itemsPerPage
	 * @param pageNum
	 * */
	@Override
	public BorrowPage searchBorrowList(BorrowStatusType statusType,
			BorrowSearchType searchType, String searchValue, int itemsPerPage,
			int pageNum) {

		List<SLBorrow> result = borrowDao.searchBorrowList(statusType,searchType,
		searchValue,itemsPerPage,pageNum);

		BorrowPage page = new BorrowPage(itemsPerPage, pageNum);

		for (int i = 0;i < 10;i++) {
			SLBorrow slBorrow = result.get(i);
			slBorrow.setTheBook(bookDao.queryByISBN(slBorrow.getBookISBN()));
			slBorrow.setTheUser(userDao.getSLUserByEmail(slBorrow.getUserEmail()));
			result.set(i, slBorrow);
		}
		page.setTheBorrows((ArrayList<SLBorrow>)result);
		page.setTotalPageNum(pageNum);
		return page;
	}

	/**
	 * 获取所有超期借阅记录
	 * */
	@Override
	public ArrayList<SLBorrow> getOverdueBorrowList() {
		BorrowStatusType statusType = BorrowStatusType.parse("已超期");
		List<SLBorrow> result = borrowDao.searchBorrowList(statusType);


		for (int i = 0;i < 10;i++) {
			SLBorrow slBorrow = result.get(i);
			slBorrow.setTheBook(bookDao.queryByISBN(slBorrow.getBookISBN()));
			slBorrow.setTheUser(userDao.getSLUserByEmail(slBorrow.getUserEmail()));
			result.set(i, slBorrow);
		}
		return (ArrayList<SLBorrow>)result;
	}

}
