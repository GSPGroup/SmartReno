package NovaPoshta;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.util.CellRangeAddress;

import Interface.MainController;
import Logic.ERROR;
import Logic.Logic;

public class СreateExcel {
	private MainController controller;
	public Logic logic;

	public СreateExcel(MainController controller) {
		this.controller = controller;
	}

	// пропис стовпців в ексль файлі
	public static void propusustovpciv(HSSFSheet sheet, int rowNum) {
		Row row = sheet.createRow(rowNum);
		row.createCell(0).setCellValue("Статус");
		row.createCell(1).setCellValue("ТТН");
		row.createCell(2).setCellValue("Днів на пошті");
		row.createCell(3).setCellValue("SMS");
		row.createCell(4).setCellValue("Дата отримання");
		row.createCell(5).setCellValue("Дата прогнозованої доставки");
		row.createCell(6).setCellValue("Дата отправки");
		row.createCell(7).setCellValue("Адрес получателя");
		row.createCell(8).setCellValue("Телефон получателя");
		row.createCell(9).setCellValue("ФИО получателя");
	}

	// метод який забє створений ексль файл даними
	public static int zabutudanumu(HSSFSheet sheet, List<DataModel> dataList, int rowNum, String forvard) {
		for (DataModel dataModel : dataList) {
			Row row1 = sheet.createRow(++rowNum);
			row1.createCell(0).setCellValue(DataModel.getStatus());
			row1.createCell(1).setCellValue(dataModel.getNumber());
			row1.createCell(2).setCellValue(dataModel.getRiznucjaDat());
			row1.createCell(3).setCellValue(DataModel.getStatusSMS());
			row1.createCell(4).setCellValue(dataModel.getRecipientDateTime());
			row1.createCell(5).setCellValue(dataModel.getScheduledDeliveryDate());
			row1.createCell(6).setCellValue(dataModel.getDateCreated());
			row1.createCell(7).setCellValue(dataModel.getRecipientAddress());
			row1.createCell(8).setCellValue(dataModel.getPhoneRecipient());
			row1.createCell(9).setCellValue(dataModel.getRecipientFullNameEW());
			sheet.autoSizeColumn(0);
			sheet.autoSizeColumn(1);
			sheet.autoSizeColumn(2);
			sheet.autoSizeColumn(3);
			sheet.autoSizeColumn(4);
			sheet.autoSizeColumn(5);
			sheet.autoSizeColumn(6);
			sheet.autoSizeColumn(7);
			sheet.autoSizeColumn(8);
			sheet.autoSizeColumn(9);
			// Создаём фильтры на весь диапазон значений таблицы
			sheet.setAutoFilter(
					CellRangeAddress.valueOf("A" + String.valueOf(1) + ":J" + String.valueOf(dataList.size())));
		}
		return rowNum;
	}

	// створити папку де буде зберігатись файл екселю і файл ексель
	public static void TryToCreate(HSSFWorkbook workbook, MainController controller, ERROR error) {
		Date currentDate = new Date();
		SimpleDateFormat dateFormat = null;
		dateFormat = new SimpleDateFormat("MMMM");
		Calendar c = Calendar.getInstance();
		int year = c.get(Calendar.YEAR);
		int month = c.get(Calendar.MONTH) + 1;
		int day = c.get(Calendar.DAY_OF_MONTH);

		File folder = CreateFolderofMounth(currentDate, dateFormat, controller);
		File folder1 = CreateFolderofTTn(currentDate, dateFormat, controller, folder);
		CreateFileExcelToDay(workbook, year, month, day, folder, controller, folder1, error);
		File out1 = new File(folder1 + "/" + day + "." + month + "." + year + ".txt");
		try {
			out1.createNewFile();
		} catch (IOException e1) {
			MainController.appendUsingFileWriter(
					controller.getNamefoldertosavexls().getText().toString() + "/" + "Звіт1.txt",
					MainController.data()
							+ "Сталась помилка при // створити папку де буде зберігатись файл екселю і файл ексель"
							+ "\r\n");
			error.ERROR();
		}
	}

	// Створити файл ексель з сьогоднішньою датою
	private static void CreateFileExcelToDay(HSSFWorkbook workbook, int year, int month, int day, File folder,
			MainController controller, File folder1, ERROR error) {
		FileOutputStream out = null;

		try {
			out = new FileOutputStream(new File(folder + "/" + day + "." + month + "." + year + ".xls"));
		} catch (FileNotFoundException e) {
			MainController.appendUsingFileWriter(
					controller.getNamefoldertosavexls().getText().toString() + "/" + "Звіт1.txt",
					MainController.data()
							+ "Сталась помилка при // створити папку де буде зберігатись файл екселю і файл ексель"
							+ "\r\n");
			error.ERROR();
		}
		try {
			workbook.write(out);
		} catch (IOException e) {
			MainController.appendUsingFileWriter(
					controller.getNamefoldertosavexls().getText().toString() + "/" + "Звіт1.txt",
					MainController.data()
							+ "Сталась помилка при // створити папку де буде зберігатись файл екселю і файл ексель"
							+ "\r\n");
		}
	}

	// створити папку з назвою існуючого місяця
	private static File CreateFolderofMounth(Date currentDate, SimpleDateFormat dateFormat, MainController controller) {
		String folderName = dateFormat.format(currentDate);
		File folder = new File(controller.getNamefoldertosavexls().getText().toString() + "/" + folderName);
		folder.mkdir();
		return folder;
	}

	// створити папку з назвою існуючого місяця
	private static File CreateFolderofTTn(Date currentDate, SimpleDateFormat dateFormat, MainController controller,
			File folderName) {
		String folderNamettn = "Папка з накладними";
		File folder1 = new File(folderName + "/" + folderNamettn);
		folder1.mkdir();
		return folder1;
	}

}