package com.edu.listen;

import java.util.Calendar;
import java.util.Date;
import java.util.Timer;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.edu.task.Task;

public class SchedulingJobsListener implements ServletContextListener {
	private Timer timer = null;
	private static Logger log = LoggerFactory.getLogger(SchedulingJobsListener.class);

	@Override
	public void contextInitialized(ServletContextEvent sce) {

		try {
			timer = new Timer(true);
			sce.getServletContext().log("【初始化----调度任务】");
			// 默认30秒执行一次
			Calendar calendar = Calendar.getInstance();

			calendar.set(Calendar.HOUR_OF_DAY, 12);
			calendar.set(Calendar.MINUTE, 30);
			calendar.set(Calendar.SECOND, 0);

			Date time = calendar.getTime();

			if (time.before(new Date())) {
				time = this.addDay(time, 1);
			}
			timer.schedule(new Task(), time, 24 * 60 * 60 * 1000);
		} catch (Exception e) {
			log.error("调度任务监听类异常,异常原因:" + e.toString());
		}
	}

	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		sce.getServletContext().log("【销毁----调度任务】");
		timer.cancel();
	}

	private Date addDay(Date date, int day) {
		Calendar instance = Calendar.getInstance();
		instance.setTime(date);
		instance.add(Calendar.DAY_OF_MONTH, day);
		return instance.getTime();
	}

}
