package com.edu.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.edu.util.EmailUtil;
import com.edu.util.IOUtil;

public class MainController extends HttpServlet {

	/**
	 * 序列化
	 */
	private static final long serialVersionUID = 1L;
	private static final Logger log = LoggerFactory.getLogger(MainController.class);

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		doPost(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String method = req.getParameter("method");// 模式

		switch (method) {
		case "upload":// 上传文件
			Map<String, String> uploadFile = uploadFile(req);
			returnData(JSON.toJSONString(uploadFile), resp);
			break;
		case "sender":// 操作时间 开始手动发送文件
			Map<String, String> senderEmail = senderEmail();
			returnData(JSON.toJSONString(senderEmail), resp);
			break;
		}

	}

	/**
	 * 
	 * @Title: uploadFile
	 * @Description: 文件上传
	 * @author 黄官易
	 * @param request
	 * @return
	 * @return Map<String,String>
	 * @date 2018年8月13日
	 * @version 1.0
	 */
	@SuppressWarnings("unchecked")
	public Map<String, String> uploadFile(HttpServletRequest request) {
		try {
			Map<String, Object> multipartData = IOUtil.getMultipartData(request);
			// 获得文件保存路径
			String path = this.getServletContext().getRealPath("/upload/");

			// 获得文件流
			List<InputStream> list = (List<InputStream>) multipartData.get("stream");
			// 获得自定的年月日
			Map<String, Object> formField = (Map<String, Object>) multipartData.get("formField");

			String fileDate = formField.get("file_date") + "";
			String[] dateStr = fileDate.split("-");

			// 获得文件名称
			String fileName = dateStr[0] + "年" + dateStr[1] + "月" + dateStr[2] + "日开发组黄官易日报.pdf";

			File file = new File(path + fileName);
			if (file.exists()) {
				HashMap<String, String> map = new HashMap<String, String>();
				map.put("code", "400");
				map.put("msg", "当天日报已上传");
				return map;
			} else if (list.isEmpty()) {
				// 不存在流
				HashMap<String, String> map = new HashMap<String, String>();
				map.put("code", "500");
				map.put("msg", "文件未上传");
				return map;
			} else {
				InputStream in = list.get(0);

				FileOutputStream out = new FileOutputStream(path + fileName);
				// 创建一个缓冲区
				byte buffer[] = new byte[5120];
				// 判断输入流中的数据是否已经读完的标识
				int len = 0;
				// 循环将输入流读入到缓冲区当中，(len=in.read(buffer))>0就表示in里面还有数据
				while ((len = in.read(buffer)) > 0) {
					// 使用FileOutputStream输出流将缓冲区的数据写入到指定的目录(savePath + "\\" + filename)当中
					out.write(buffer, 0, len);
				}
				in.close();
				out.close();
			}
			HashMap<String, String> map = new HashMap<String, String>();
			map.put("code", "200");
			map.put("msg", "文件上传成功");
			return map;
		} catch (Exception e) {
			log.error("上传服务异常,异常原因:【" + e.toString() + "】");
			HashMap<String, String> map = new HashMap<String, String>();
			map.put("code", "500");
			map.put("msg", "文件上传异常");
			return map;
		}
	}

	public Map<String, String> senderEmail() {
		EmailUtil.sendEmail();
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("code", "200");
		map.put("msg", "邮件发送成功");
		return map;
	}

	/**
	 * 
	 * @Title: returnData
	 * @Description: 统一返回路径
	 * @param json
	 * @param response
	 * @throws IOException
	 * @author: MR.H
	 * @return: void
	 *
	 */
	private void returnData(String json, HttpServletResponse response) throws IOException {
		response.setHeader("Access-Control-Allow-Origin", "*");
		response.setHeader("Content-type", "text/html;charset=UTF-8");
		response.setCharacterEncoding("utf-8");
		response.getWriter().print(json);
	}
}
