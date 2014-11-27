package cn.duke.common.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

public class FileHelp {
	private static Log log = LogFactory.getLog(FileHelp.class);

	public static String getExtendName(String fileName) {
		int l = fileName.lastIndexOf(".");
		return fileName.substring(l + 1, fileName.length());
	}

	public static boolean sava(String base, String fileName, CommonsMultipartFile commonsMultipartFile) {

		boolean flag = false;
		File dirs = new File(base);
		dirs.mkdirs();

		// 这里的file就是前台页面的name
		if (commonsMultipartFile.isEmpty()) {
			return false;
		}

		// 获取路径，生成完整的文件路径,当然要先创建upload文件夹
		File uploadFile = new File(base + fileName);
		try {
			// 上传
			FileCopyUtils.copy(commonsMultipartFile.getBytes(), uploadFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
		flag = true;
		return flag;
	}

	public static void downloadFile(HttpServletRequest request, HttpServletResponse response, String fileName, String downLoadPath) throws FileNotFoundException {
		FileInputStream is = new FileInputStream(downLoadPath);
		downloadFile(request, response, fileName, is);
	}

	/**
	 * 下载文件
	 * 
	 * @param request
	 * @param response
	 * @param fileName
	 *            下载的文件名
	 * @param downLoadPath
	 *            文件的路径
	 * @throws IOException
	 */
	public static void downloadFile(HttpServletRequest request, HttpServletResponse response, String fileName, InputStream is) {
		BufferedInputStream bis = null;
		BufferedOutputStream bos = null;
		try {
			response.setContentType("text/html;charset=UTF-8");
			request.setCharacterEncoding("UTF-8");
			// long fileLength = downloadFile.length();
			// response.setHeader("Content-Length", String.valueOf(fileLength));
			response.setContentType("application/x-msdownload;");
			response.setHeader("Content-disposition", "attachment; filename=" + new String((fileName).getBytes("GBK"), "ISO8859-1"));
			bis = new BufferedInputStream(is);
			bos = new BufferedOutputStream(response.getOutputStream());
			byte[] buff = new byte[2048];
			int bytesRead;
			while (-1 != (bytesRead = bis.read(buff, 0, buff.length))) {
				bos.write(buff, 0, bytesRead);
			}
			bos.flush();
		} catch (UnsupportedEncodingException e) {
			log.error("设置编码时错误", e);
		} catch (FileNotFoundException e) {
			log.error("下载文件未找到", e);
		} catch (Exception e) {
			log.error("下载文件时错误", e);
		} finally {
			try {
				if (bis != null) {
					bis.close();
				}
				if (bos != null) {
					bos.close();
				}
			} catch (IOException e) {
				log.error("关闭文件流时出错", e);
			}
		}
	}
}
