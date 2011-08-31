package com.util;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Date;

//import sun.java2d.pipe.SpanShapeRenderer.Simple;

public class RegisterMark {
	public static String getRegister(String s) {
		String s1 = "", s2 = ""; // 初始化兩個字串
		SimpleDateFormat sd = new SimpleDateFormat("yyyy"); // 設定事件的格式化物件
		Date d = new Date(); // 初始化一個Date物件
		s += sd.format(d); // 將需要加密的字串與目前年份連結
		for (int i = 0; i < s.length(); i++) { // 迴圈字串的個數
			s1 += (int) s.charAt(i) * (i + 1); // 對字串中每個字元進行處理賦予s1字串
		}
		for (int i = 0; i < s1.length() / 5; i++) { // 根據s1字串進行處理取得字串s2

			if (i == s1.length() / 5 - 1)
				s2 += s1.substring(i * 5, (i + 1) * 5);
			else
				s2 += s1.substring(i * 5, (i + 1) * 5) + "-";
		}
		return s2; // 將s2傳回
	}

	public static String getMAC() {
		String macResult = null;
		String osName = System.getProperty("os.name"); // 取得作業系統的名稱
		StringBuffer systemPathBuff = new StringBuffer(""); // 產生範例StringBuffer物件
		if (osName.indexOf("Windows") > -1) {
			// Windows作業系統的cmd.exe的絕對路徑�
			systemPathBuff.append("c:\\WINDOWS\\system32\\cmd.exe");
		}
		if (osName.indexOf("NT") > -1) {
			// NT作業系統cmd.exe的絕對路徑
			systemPathBuff.append("c:\\WINDOWS\\command.com");
		}
		//
		Process pro = null;
		try {
			// 在cmd底下執行dir命令，並得到指令執行完畢後的輸出串流
			pro = Runtime.getRuntime().exec(
					systemPathBuff.toString() + " /c dir"); // 執行DOS指令
			InputStream is = pro.getInputStream();
			BufferedReader br = new BufferedReader(new InputStreamReader(is,
					"Big5"));
			// 讀取第一行
			String message = br.readLine();
			// String serNuResult = null;
			int index = -1;
			// 讀取下一行
			message = br.readLine();
			// 在cmd下面執行 ipconfig/all 指令並得到指令執行完畢後的輸出串流
			pro = Runtime.getRuntime().exec(
					systemPathBuff.toString() + " /c ipconfig/all"); // 執行取得MAC位址的DOS指令
			is = pro.getInputStream();
			br = new BufferedReader(new InputStreamReader(is, "Big5"));
			// 讀取第一行
			message = br.readLine();
			while (message != null) {
				if ((index = message.indexOf("實體位址")) > 0
						|| (index = message.indexOf("Phisycal Address")) > 0) { // 找到MAC位址那行
					macResult = message.substring(index + 36).trim(); // 將結果放置在macResult變數中
					break;
				}
				// 讀取下一行
				message = br.readLine();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return macResult; // 將mac位址傳回
	}

	public static void main(String[] args) {
		getRegister("EROWA");
	}
}
