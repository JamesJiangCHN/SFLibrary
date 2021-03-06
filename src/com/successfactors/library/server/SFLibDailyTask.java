package com.successfactors.library.server;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimerTask;

import javax.servlet.ServletContext;

import com.successfactors.library.server.dao.SLBookDao;
import com.successfactors.library.server.dao.SLBorrowDao;
import com.successfactors.library.server.dao.SLOrderDao;
import com.successfactors.library.server.dao.SLUserDao;
import com.successfactors.library.shared.BorrowStatusType;
import com.successfactors.library.shared.SLEmailUtil;
import com.successfactors.library.shared.model.SLBorrow;

public class SFLibDailyTask extends TimerTask {

	// 每天0点执行
	private static final int C_SCHEDULE_HOUR = 0;
	
	private static boolean isRunning = false;
	private ServletContext context = null;
	
	private SLEmailUtil emailUtil = new SLEmailUtil();
	private SLBorrowDao borrowDao = SLBorrowDao.getDao();
	private SLBookDao bookDao = SLBookDao.getDao();
	private SLUserDao userDao = SLUserDao.getDao();
	private SLOrderDao orderDao = SLOrderDao.getDao();

	public SFLibDailyTask(ServletContext context) {
		this.context = context;
	}

	@Override
	public void run() {
		Calendar cal = Calendar.getInstance();
		if (!isRunning) {
			if (C_SCHEDULE_HOUR == cal.get(Calendar.HOUR_OF_DAY)) {
				isRunning = true;
				doTask();
				isRunning = false;
			}
		} else {
			context.log("上一次任务执行还未结束");
		}
	}

	// 执行任务
	public void doTask() {
		context.log("开始执行定时任务");
		
		// 检查书籍超期情况
		List<SLBorrow> nowBorrowList = borrowDao.searchBorrowList(BorrowStatusType.NOW, Integer.MAX_VALUE, 1).getTheBorrows();
		if (nowBorrowList == null) {
			return;
		}
		
		//context.log("共有： "+nowBorrowList.size()+" 个当前借阅记录");
		for (SLBorrow slBorrow : nowBorrowList) {
			if (slBorrow.getShouldReturnDate().before(new Date())) {
				
				slBorrow.setStatus("已超期");
				slBorrow.setOverdue(true);
				borrowDao.update(slBorrow);
				
				slBorrow.setTheBook(bookDao.queryByISBN(slBorrow.getBookISBN()));
				slBorrow.setTheUser(userDao.getSLUserByEmail(slBorrow
						.getUserEmail()));
				
				if (orderDao.getEarlistOrder(slBorrow.getBookISBN()) != null) {
					emailUtil.sendOverdueEmail(slBorrow);
				}
				context.log("设置借阅ID： "+slBorrow.getBorrowId()+" 为已超期");
//				System.out.println("设置借阅ID： "+slBorrow.getBorrowId()+" 为已超期");
			}
		}

		context.log("定时任务执行结束");
	}

}
