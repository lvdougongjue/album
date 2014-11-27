package cn.duke.common.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ExcelUtil {
	// 私有化构造函数
	private ExcelUtil() {
	}

	public static final String ERROR_EXT_NAME = "错误的扩展名";

	/**
	 * @author mengweifeng
	 * @param filePath
	 * @param sheets
	 *            key=sheet页名称 value=需要写入的sheet页的内容(string数组的形式)，String[]第一行为标题
	 * @throws IOException
	 * @throws IOException
	 * @since 2012-11-28
	 */
	public static void create03ExcelFile(String filePath, Map<String, List<String[]>> sheets) throws IOException {
		File excelFile = new File(filePath);
		create03ExcelFile(excelFile, sheets);
	}

	/**
	 * @author mengweifeng
	 * @param excelFile
	 * @param sheets
	 *            key = sheet页名称 value =
	 *            需要写入的sheet页的内容(string数组的形式)，String[]第一行为标题
	 * @throws IOException
	 * @throws IOException
	 * @since 2012-11-28
	 */
	public static void create03ExcelFile(File excelFile, Map<String, List<String[]>> sheets) throws IOException {
		Workbook wb = new HSSFWorkbook();
		if (!excelFile.getAbsolutePath().endsWith(".xls")) {
			throw new IOException(ERROR_EXT_NAME);
		}
		createExcelFile(wb, excelFile, sheets);
	}

	/**
	 * 创建07格式的excel文件
	 * 
	 * @author mengweifeng
	 * @param filePath
	 * @param sheets
	 * @throws IOException
	 * @since 2013-5-8
	 */
	public static void create07ExcelFile(String filePath, Map<String, List<String[]>> sheets) throws IOException {
		File excelFile = new File(filePath);
		create07ExcelFile(excelFile, sheets);
	}

	/**
	 * 创建07格式的excel文件
	 * 
	 * @author mengweifeng
	 * @param excelFile
	 * @param sheets
	 * @throws IOException
	 * @since 2013-5-8
	 */
	public static void create07ExcelFile(File excelFile, Map<String, List<String[]>> sheets) throws IOException {
		Workbook wb = new XSSFWorkbook();
		if (!excelFile.getAbsolutePath().endsWith(".xlsx")) {
			throw new IOException(ERROR_EXT_NAME);
		}
		createExcelFile(wb, excelFile, sheets);
	}

	private static void createExcelFile(Workbook wb, File excelFile, Map<String, List<String[]>> sheets) throws IOException {
		for (Entry<String, List<String[]>> entry : sheets.entrySet()) {
			String sheetName = entry.getKey();
			Sheet sheet = wb.createSheet(sheetName);
			List<String[]> sheetValues = entry.getValue();
			for (int i = 0; i < sheetValues.size(); i++) {
				String[] values = sheetValues.get(i);
				Row row = sheet.createRow(i);
				for (int j = 0; j < values.length; j++) {
					Cell cell = row.createCell(j);
					String value = values[j];
					try {
						Double numberValue = Double.parseDouble(value);
						cell.setCellValue(numberValue);
					} catch (NumberFormatException e) {
						cell.setCellValue(values[j]);
					}
				}
			}
		}
		OutputStream os = new FileOutputStream(excelFile);
		wb.write(os);
	}

	@SuppressWarnings("resource")
	public static Workbook getWorkbook(File file) throws Exception {
		Workbook wb = null;
		InputStream is = new FileInputStream(file);
		String fileName = file.getName();
		String extendName = FileHelp.getExtendName(fileName);
		if ("xls".equals(extendName)) {
			// 2003格式
			wb = new HSSFWorkbook(is);
		} else if ("xlsx".equals(extendName)) {
			// 2007格式
			wb = new XSSFWorkbook(is);
		} else {
			// 不是excel文件
			throw new Exception("不是EXCLE文件");
		}
		return wb;
	}

	public static void main(String[] args) throws Exception {
		Workbook wb = getWorkbook(new File("d:/abc.xlsx"));
		Sheet sheet = wb.getSheetAt(0);
		Row row = sheet.getRow(0);
		System.out.println(sheet.getLastRowNum());
		System.out.println(row.getLastCellNum());
	}

	/**
	 * 获取单元格的内容
	 * 
	 * @author mengweifeng
	 * @param cell
	 * @return
	 * @since 2013-2-20
	 */
	public static String getCellValue(Cell cell) {
		if (cell == null) {
			return "";
		}
		String value = "";
		switch (cell.getCellType()) {
		case HSSFCell.CELL_TYPE_STRING:
			value = cell.getStringCellValue();
			break;
		case HSSFCell.CELL_TYPE_NUMERIC:

			if (HSSFDateUtil.isCellDateFormatted(cell)) {
				Date date = cell.getDateCellValue();
				if (date != null) {
					value = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(date);
				}
			} else {
				value = new DecimalFormat("0").format(cell.getNumericCellValue());
			}
			break;
		case HSSFCell.CELL_TYPE_FORMULA:
			// 导入时如果为公式生成的数据则无值
			if (!cell.getStringCellValue().equals("")) {
				value = cell.getStringCellValue();
			} else {
				value = cell.getNumericCellValue() + "";
			}
			break;
		case HSSFCell.CELL_TYPE_BLANK:
			break;
		case HSSFCell.CELL_TYPE_ERROR:
			value = "";
			break;
		case HSSFCell.CELL_TYPE_BOOLEAN:
			value = (cell.getBooleanCellValue() == true ? "Y" : "N");
			break;
		default:
			value = "";
		}
		value = rightTrim(value);
		return value;
	}

	/**
	 * 
	 * 去掉字符串右边的空格
	 * 
	 * @param str
	 *            要处理的字符串
	 * 
	 * @return 处理后的字符串
	 */

	private static String rightTrim(String str) {
		if (str == null) {
			return "";
		}
		str = str.trim().replaceAll("\r", "").replaceAll("\r\n", "").replaceAll("\n", "");
		int length = str.length();
		for (int i = length - 1; i >= 0; i--) {
			if (str.charAt(i) != 0x20 && str.charAt(i) != (char) 160) {
				break;
			}
			length--;
		}
		return str.substring(0, length);
	}
}
