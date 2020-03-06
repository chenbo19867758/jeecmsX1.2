package com.jeecms.common.util.office;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/*
* PDF转SWF工具
* @author tom
*
*/
public class PdfToSwfConverter {

	/**
	 *  PDF转SWF工具
	* @Title: convertPDF2SWF 
	* @param swftoolsHome 工具
	* @param sourcePath 来源路径
	* @param destPath 转换路径
	* @param fileName 名称
	* @throws IOException 异常
	 */
	public static int convertPDF2SWF(String swftoolsHome, String sourcePath, String destPath, String fileName)
			throws IOException {
		File dest = new File(destPath);
		if (!dest.exists()) {
			dest.mkdirs();
		}
		// 调用pdf2swf命令进行转换 没有中文文件不需要参数-s languagedir
		String command = swftoolsHome + " " + sourcePath + " -o  " 
				+ destPath + fileName + " -f -z -s flashversion=9  ";
		command = command.replace("\\", "/");
		Process process = Runtime.getRuntime().exec(command); // 调用外部程序
		final InputStream pInputStream = process.getInputStream();
		new Thread(new Runnable() {
			public void run() {
				BufferedReader br = new BufferedReader(new InputStreamReader(pInputStream));
				try {
					while (br.readLine() != null) {
						
					}
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}).start(); // 启动单独的线程来清空process.getInputStream()的缓冲区
		InputStream inputstream = process.getErrorStream();
		BufferedReader bufferReader = new BufferedReader(new InputStreamReader(inputstream));
		StringBuilder buf = new StringBuilder(); // 保存输出结果流
		String line = null;
		while ((line = bufferReader.readLine()) != null) {
			buf.append(line);
		}
		try {
			process.waitFor();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return process.exitValue();
	}
}
