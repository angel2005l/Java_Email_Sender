package com.edu.task;

import java.util.TimerTask;

import com.edu.util.EmailUtil;

public class Task extends TimerTask {

	@Override
	public void run() {
		EmailUtil.sendEmail();
	}

}
