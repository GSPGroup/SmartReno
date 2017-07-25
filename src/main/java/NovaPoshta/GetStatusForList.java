package NovaPoshta;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.swing.GroupLayout.Alignment;

import org.apache.poi.ddf.EscherColorRef.SysIndexSource;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import Interface.MainController;
import Logic.ERROR;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import sms.api.API;

public class GetStatusForList extends Thread {
	public СreateExcel createexcel;
	public ERROR error;
	public API api;
	private MainController controller;

	public GetStatusForList(MainController controller) {
		this.controller = controller;
		this.api = api;
	}

	@Override
	public void run() {
		controller.browsgetstatusnp.setDisable(true);
		controller.getstatusnp.setDisable(true);
		GetStatusForListWriteToExcel();
		controller.browsgetstatusnp.setDisable(false);
		controller.getstatusnp.setDisable(false);
	}

	// основний метод
	public void GetStatusForListWriteToExcel() {

		List<String> lines = null;
		if (controller.manynp.isSelected() == true) {
			String fileName = controller.getNamefoldertosavexls().getText().toString() + "/накладні.txt";
			if ((new File(fileName)).exists()) {
				lines = controller.ReadFileForTTN(
						controller.getNamefoldertosavexls().getText().toString() + "/накладні.txt", lines);
				new File(controller.getNamefoldertosavexls().getText().toString() + "/накладні.txt").delete();
				MethodToGetStatusNp(lines);
			} else {
				Platform.runLater(new Runnable() {
					@Override
					public void run() {
						Alert alert = new Alert(AlertType.ERROR);
						alert.setTitle("НЕ Виконано");
						alert.setHeaderText("Дефолтний файл з накладними відсутній");
						alert.showAndWait();
						controller.runindicator.setProgress(1.0);
						controller.getProgeress().setProgress(1.0);
					}
				});
			}

		} else if (controller.newmanynp.isSelected() == true) {
			lines = controller.ReadFileForTTN(controller.Browsgetstatusnp.toString(), lines);
			if ((new File(controller.getNamefoldertosavexls().getText().toString() + "/накладні.txt")).exists()) {
				new File(controller.getNamefoldertosavexls().getText().toString() + "/накладні.txt").delete();
			}
			MethodToGetStatusNp(lines);
		}
		controller.initialize();
	}

	// основний метод для отримання статусу по накладним
	private void MethodToGetStatusNp(List<String> lines) {
		HSSFWorkbook workbook = new HSSFWorkbook();
		HSSFSheet sheet = workbook.createSheet("Просто лист");
		int rowNum = 0;
		int num = 0;
		СreateExcel.propusustovpciv(sheet, rowNum);
		String TTN = null;
		for (String line : lines) {
			TTN = line.substring(0, 14);
			String forvard = null;
			do {
				try {
					forvard = NovaPoshta.Xml(TTN, controller.DefaultPhoneNumberSender.getText().toString());
				} catch (URISyntaxException | IOException e) {
					MainController.appendUsingFileWriter(
							controller.getNamefoldertosavexls().getText().toString() + "/" + "Звіт1.txt",
							MainController.data() + "Сталась помилка при // отриманні статусу по списку накладних"
									+ "\r\n");
					ErrorWindow();
				}
				if (forvard.contains(NovaPoshta.xmlanswer)) {
					try {
						TimeUnit.SECONDS.sleep(20);
					} catch (InterruptedException e) {
						MainController.appendUsingFileWriter(
								controller.getNamefoldertosavexls().getText().toString() + "/" + "Звіт1.txt",
								MainController.data()
										+ "Сталась помилка при // отриманні статусу по списку накладних (затримка в часі)"
										+ "\r\n");
						ErrorWindow();
					}
				}
			} while (forvard.contains(NovaPoshta.xmlanswer) && MainController.netIsAvailable() == true);
			String Status00 = ParsingResult.GetStatusTTNNP(forvard);
			rowNum = WhatGetStatuNpForlist(forvard, Status00, rowNum, sheet, workbook, TTN);
			num++;
			controller.getProgeress().setProgress((double) num / lines.size());
			if (controller.getProgeress().getProgress() == 1.0) {
				controller.getProgeress().setStyle("-fx-accent: green;");
				controller.initialize();
			}
		}
		WindowMassageGetStatusIsFisnish(num, lines);
		WhatWriteToFileafterFinishGetStatusNP(num, lines);
	}

	// вспилваюче вікно зі звітом що зробило по отриманню статусу зі списку по
	// накладним
	private void WindowMassageGetStatusIsFisnish(int num, List<String> lines) {
		final int m = num;
		final int l = lines.size();
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				Alert alert = new Alert(AlertType.INFORMATION);
				alert.setTitle("Виконано");
				alert.setHeaderText("Для списку накладних ");
				alert.setContentText(" Обробило " + m + "/" + l);
				alert.showAndWait();
				controller.runindicator.setProgress(1.0);
				controller.getProgeress().setProgress(1.0);
			}
		});
	}

	// шо записати в файл звіт після того як було отримано список по накладним
	private void WhatWriteToFileafterFinishGetStatusNP(int num, List<String> lines) {
		if (controller.manynp.isSelected() == true) {
			MainController.appendUsingFileWriter(
					controller.getNamefoldertosavexls().getText().toString() + "/" + "Звіт1.txt",
					MainController.data() + "отимання статусу для СТАНДАРТНОГО СПИСКУ.  Обробило: " + num + "/"
							+ lines.size() + "\r\n");
		} else if (controller.newmanynp.isSelected() == true) {
			MainController.appendUsingFileWriter(
					controller.getNamefoldertosavexls().getText().toString() + "/" + "Звіт1.txt",
					MainController.data() + "Файл: " + controller.Browsgetstatusnp.toString() + " Обробило " + num + "/"
							+ lines.size() + "\r\n");
		}
	}

	private void ErrorWindow() {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				Alert alert = new Alert(AlertType.ERROR);
				alert.setTitle(" НЕ Виконано");
				alert.setHeaderText("СТАЛАСЯ ПОМИЛКА ПРИ ВИКОНАННІ ");
				alert.showAndWait();
				controller.runindicator.setProgress(1.0);
				controller.getProgeress().setProgress(1.0);
			}
		});
	}

	// обработчик собитій для отримання методу по відповіді з нової пошти по
	// накладній
	public int WhatGetStatuNpForlist(String forvard, String Status00, int rowNum, HSSFSheet sheet,
			HSSFWorkbook workbook, String TTN) {
		switch (Status00) {
		// Відправлення отримано
		case "Відправлення отримано":
			ifSendingReceived(forvard);
			break;
		// Відправлення отримано. Грошовий переказ видано одержувачу
		case "Відправлення отримано. Грошовий переказ видано одержувачу.":
			ifSendingReceivedCach(forvard);
			break;
		// Відправлення отримано очікуйте смс
		case "Відправлення отримано. Протягом доби відправник отримає SMS-повідомлення про надходження грошового переказу та зможете отримати його в касі відділення «Нова пошта».":
			ifSendingReceivedSms(forvard);
			break;
		// Змінено адресу
		case "Змінено адресу":
			ifChangeOfAddress(forvard, rowNum, sheet, workbook, TTN);
			break;
		// Відмова
		case "Відмова від отримання":
		case "Одержувач відмовився від отримання відправлення":
			ifRefusing(forvard, rowNum, sheet, workbook);
			break;
		// Відправлення прямує
		case "Відправлення прямує":
			ifGoWay(forvard);
			break;
		// Відправлення передано до огляду отримувачу
		case "Відправлення передано до огляду отримувачу":
			ifWatching(forvard);
			break;
		default:
			// Прибув у відділення
			ifArrivedInTtheDivision(forvard);
			break;
		}
		// записати всьо шо приїхїало в ексель
		DataModel.setStatusSMS(API.GetStatusSendSms(ParsingResult.GetPhoneNumberRepicientTTNNP(forvard)));
		rowNum = GoToExcel(forvard, rowNum, sheet, workbook, controller);
		return rowNum;
	}

	// записати номер накладної в список для повторного отримання статусу
	private void WriteToNewListTTN(String TTN) {
		String fileName = controller.getNamefoldertosavexls().getText().toString() + "/накладні.txt";
		if ((new File(fileName)).exists()) {
			List<String> file = null;
			file = controller.ReadFileForTTN(controller.getNamefoldertosavexls().getText().toString() + "/накладні.txt",
					file);
			if (!file.toString().contains(TTN)) {
				MainController.appendUsingFileWriter(
						controller.getNamefoldertosavexls().getText().toString() + "/накладні.txt",
						TTN + ";1" + "\r\n");
			}
		} else {
			MainController.appendUsingFileWriter(
					controller.getNamefoldertosavexls().getText().toString() + "/накладні.txt", TTN + ";1" + "\r\n");
		}
	}

	// метод записати всьо шо приїхїало в ексель
	private int GoToExcel(String forvard, int rowNum, HSSFSheet sheet, HSSFWorkbook workbook,
			MainController controller) {
		List<DataModel> dataModels = new ArrayList<>();
		dataModels.add(new DataModel());
		List<DataModel> dataList = dataModels;
		rowNum = СreateExcel.zabutudanumu(sheet, dataList, rowNum, forvard);
		СreateExcel.TryToCreate(workbook, controller, error);
		ParsingResult.GoNull();
		return rowNum;
	}

	// Відмова
	private void ifRefusing(String forvard, int rowNum, HSSFSheet sheet, HSSFWorkbook workbook) {
		if (ParsingResult.GetLastCreatedOnTheBasisNumber(forvard) == null) {
			DataModel.setStatus(ParsingResult.GetStatus(forvard) + " Повернення не замовлено ");
			DataModel.setNumber(ParsingResult.GetNumbernTTNNP(forvard));
			DataModel.setRiznucjaDat("Відмова");
			// DataModel.setStatusSMS(API.GetStatusSendSms(ParsingResult.GetPhoneNumberRepicientTTNNP(forvard)));
			WriteToNewListTTN(ParsingResult.GetNumbernTTNNP(forvard));
			DataModel.setRecipientDateTime(ParsingResult.GetDataToTaken(forvard));
			DataModel.setScheduledDeliveryDate(ParsingResult.GetscheduleddeliverydateTTNNP(forvard));
			DataModel.setDateCreated(ParsingResult.GetDateCreated(forvard));
			DataModel.setRecipientAddress(ParsingResult.fullRecipientAddress(forvard));
			DataModel.setPhoneRecipient(ParsingResult.GetPhoneNumberRepicientTTNNP(forvard));
			DataModel.setRecipientFullNameEW(ParsingResult.GetRecipientFullNameEW(forvard));
		} else {
			DataModel.setNumber(ParsingResult.GetNumbernTTNNP(forvard));
			String forvard1 = ParsingResult.GetLastCreatedOnTheBasisNumber(forvard);
			String forvard2 = null;
			try {
				forvard2 = NovaPoshta.Xml(forvard1, controller.DefaultPhoneNumberSender.getText().toString());
			} catch (URISyntaxException | IOException e) {
				MainController.appendUsingFileWriter(
						controller.getNamefoldertosavexls().getText().toString() + "/" + "Звіт1.txt",
						MainController.data() + "Сталась помилка при // для спмску метод ВІДМОВА" + "\r\n");
				ErrorWindow();
			}
			DataModel.setRecipientFullNameEW(ParsingResult.GetRecipientFullNameEW(forvard));
			DataModel.setPhoneRecipient(ParsingResult.GetPhoneNumberRepicientTTNNP(forvard2));
			DataModel.setDateCreated(ParsingResult.GetDateCreated(forvard2));
			DataModel.setRiznucjaDat("Відмова");
			// DataModel.setStatusSMS(API.GetStatusSendSms(ParsingResult.GetPhoneNumberRepicientTTNNP(forvard)));
			DataModel.setRecipientAddress(ParsingResult.fullRecipientAddress(forvard2));
			if (ParsingResult.fullRecipientAddress(forvard2).equals(ParsingResult.GoBack)) {
				DataModel.setStatus("Поверення " + ParsingResult.GetStatus(forvard2));
			} else {
				DataModel.setStatus("Поверення НЕ замовлено " + ParsingResult.GetStatus(forvard2));
			}
			if (!DataModel.getStatus().equals("Поверення Відправлення отримано")) {
				WriteToNewListTTN(ParsingResult.GetNumbernTTNNP(forvard));
			}
			DataModel.setRecipientDateTime(null);
			DataModel.setScheduledDeliveryDate(ParsingResult.GetscheduleddeliverydateTTNNP(forvard2));
		}
	}

	// Змінено адресу
	private int ifChangeOfAddress(String forvard, int rowNum, HSSFSheet sheet, HSSFWorkbook workbook, String TTN) {

		String forvard2 = null;
		try {
			forvard2 = NovaPoshta.Xml(TTN, controller.DefaultPhoneNumberSender.getText().toString());

		} catch (URISyntaxException | IOException e1) {
			MainController.appendUsingFileWriter(
					controller.getNamefoldertosavexls().getText().toString() + "/" + "Звіт1.txt",
					MainController.data() + "Сталась помилка при // для спмску метод Змінено адресу" + "\r\n");
			ErrorWindow();
		}
		String forvard3 = ParsingResult.GetLastCreatedOnTheBasisNumber(forvard2);
		String phone = ParsingResult.GetPhoneNumberRepicientTTNNP(forvard2);
		String forvard5 = null;
		try {
			forvard5 = NovaPoshta.Xml(forvard3, phone);
		} catch (URISyntaxException | IOException e) {
			MainController.appendUsingFileWriter(
					controller.getNamefoldertosavexls().getText().toString() + "/" + "Звіт1.txt",
					MainController.data() + "Сталась помилка при // для спмску метод Змінено адресу" + "\r\n");
			ErrorWindow();
		}
		DataModel.setStatus("Змінено адресу " + ParsingResult.GetStatus(forvard5));
		DataModel.setNumber(ParsingResult.GetNumbernTTNNP(forvard));
		if (ParsingResult.GetDataToTaken(forvard2) == null) {
			DataModel.setRiznucjaDat(ParsingResult.RahunokDnivIfNotTaken(forvard2, controller));

			WriteToNewListTTN(ParsingResult.GetNumbernTTNNP(forvard));
		} else {
			DataModel.setRiznucjaDat("Посилку Забрали");
			DataModel.setRecipientDateTime(ParsingResult.GetDataToTaken(forvard2));
		}
		if (ParsingResult.GetStatus(forvard5).equals(ParsingResult.SendingReceived)
				| ParsingResult.GetStatus(forvard5).equals(ParsingResult.SendingReceivedCach)
				| ParsingResult.GetStatus(forvard5).equals(ParsingResult.SendingReceivedSms)) {
			DataModel.setRiznucjaDat("Посилку Забрали");
		} else if (ParsingResult.GetStatus(forvard5).equals(ParsingResult.Refusing)
				| ParsingResult.GetStatus(forvard5).equals(ParsingResult.IfRefusingStatus)) {
			DataModel.setRiznucjaDat("Відмова");
			WriteToNewListTTN(ParsingResult.GetNumbernTTNNP(forvard));
		}
		DataModel.setScheduledDeliveryDate(ParsingResult.GetscheduleddeliverydateTTNNP(forvard));
		DataModel.setDateCreated(ParsingResult.GetDateCreated(forvard));
		DataModel.setRecipientAddress(ParsingResult.fullRecipientAddress(forvard5));
		DataModel.setPhoneRecipient(ParsingResult.GetPhoneNumberRepicientTTNNP(forvard));
		DataModel.setRecipientFullNameEW(ParsingResult.GetRecipientFullNameEW(forvard));
		return rowNum;

	}

	// Прибув у відділення
	private void ifArrivedInTtheDivision(String forvard) {
		DataModel.setStatus(ParsingResult.GetStatus(forvard));
		DataModel.setNumber(ParsingResult.GetNumbernTTNNP(forvard));
		DataModel.setRiznucjaDat(ParsingResult.RahunokDnivIfNotTaken(forvard, controller));
		// DataModel.setStatusSMS(API.GetStatusSendSms(ParsingResult.GetPhoneNumberRepicientTTNNP(forvard)));
		WriteToNewListTTN(ParsingResult.GetNumbernTTNNP(forvard));
		DataModel.setRecipientDateTime(ParsingResult.GetDataToTaken(forvard));
		DataModel.setScheduledDeliveryDate(ParsingResult.GetscheduleddeliverydateTTNNP(forvard));
		DataModel.setDateCreated(ParsingResult.GetDateCreated(forvard));
		DataModel.setRecipientAddress(ParsingResult.fullRecipientAddress(forvard));
		DataModel.setPhoneRecipient(ParsingResult.GetPhoneNumberRepicientTTNNP(forvard));
		DataModel.setRecipientFullNameEW(ParsingResult.GetRecipientFullNameEW(forvard));
	}

	// Відправлення отримано. Грошовий переказ видано одержувачу
	private void ifSendingReceivedCach(String forvard) {
		DataModel.setStatus("Відправлення отримано. Гроші видано ");
		DataModel.setNumber(ParsingResult.GetNumbernTTNNP(forvard));
		DataModel.setRiznucjaDat("Посилку Забрали");
		// DataModel.setStatusSMS(API.GetStatusSendSms(ParsingResult.GetPhoneNumberRepicientTTNNP(forvard)));
		DataModel.setRecipientDateTime(ParsingResult.GetDataToTaken(forvard));
		DataModel.setScheduledDeliveryDate(ParsingResult.GetscheduleddeliverydateTTNNP(forvard));
		DataModel.setDateCreated(ParsingResult.GetDateCreated(forvard));
		DataModel.setRecipientAddress(ParsingResult.fullRecipientAddress(forvard));
		DataModel.setPhoneRecipient(ParsingResult.GetPhoneNumberRepicientTTNNP(forvard));
		DataModel.setRecipientFullNameEW(ParsingResult.GetRecipientFullNameEW(forvard));
	}

	// Відправлення прямує
	private void ifGoWay(String forvard) {
		DataModel.setStatus(ParsingResult.GetStatus(forvard));
		DataModel.setNumber(ParsingResult.GetNumbernTTNNP(forvard));
		DataModel.setRiznucjaDat(null);
		// DataModel.setStatusSMS(API.GetStatusSendSms(ParsingResult.GetPhoneNumberRepicientTTNNP(forvard)));
		WriteToNewListTTN(ParsingResult.GetNumbernTTNNP(forvard));
		DataModel.setRecipientDateTime(ParsingResult.GetDataToTaken(forvard));
		DataModel.setScheduledDeliveryDate(ParsingResult.GetscheduleddeliverydateTTNNP(forvard));
		DataModel.setDateCreated(ParsingResult.GetDateCreated(forvard));
		DataModel.setRecipientAddress(ParsingResult.fullRecipientAddress(forvard));
		DataModel.setPhoneRecipient(ParsingResult.GetPhoneNumberRepicientTTNNP(forvard));
		DataModel.setRecipientFullNameEW(ParsingResult.GetRecipientFullNameEW(forvard));
	}

	// Відправлення отримано
	private void ifSendingReceived(String forvard) {
		DataModel.setStatus("Відправлення отримано");
		DataModel.setNumber(ParsingResult.GetNumbernTTNNP(forvard));
		DataModel.setRiznucjaDat("Посилку Забрали");
		// DataModel.setStatusSMS(API.GetStatusSendSms(ParsingResult.GetPhoneNumberRepicientTTNNP(forvard)));
		DataModel.setRecipientDateTime(ParsingResult.GetDataToTaken(forvard));
		DataModel.setScheduledDeliveryDate(ParsingResult.GetscheduleddeliverydateTTNNP(forvard));
		DataModel.setDateCreated(ParsingResult.GetDateCreated(forvard));
		DataModel.setRecipientAddress(ParsingResult.fullRecipientAddress(forvard));
		DataModel.setPhoneRecipient(ParsingResult.GetPhoneNumberRepicientTTNNP(forvard));
		DataModel.setRecipientFullNameEW(ParsingResult.GetRecipientFullNameEW(forvard));
	}

	// Відправлення передано до огляду отримувачу
	private void ifWatching(String forvard) {
		DataModel.setStatus("Отримувач переглядає посилку");
		DataModel.setNumber(ParsingResult.GetNumbernTTNNP(forvard));
		DataModel.setRiznucjaDat(null);
		// DataModel.setStatusSMS(API.GetStatusSendSms(ParsingResult.GetPhoneNumberRepicientTTNNP(forvard)));
		WriteToNewListTTN(ParsingResult.GetNumbernTTNNP(forvard));
		DataModel.setRecipientDateTime(null);
		DataModel.setScheduledDeliveryDate(ParsingResult.GetscheduleddeliverydateTTNNP(forvard));
		DataModel.setDateCreated(ParsingResult.GetDateCreated(forvard));
		DataModel.setRecipientAddress(ParsingResult.fullRecipientAddress(forvard));
		DataModel.setPhoneRecipient(ParsingResult.GetPhoneNumberRepicientTTNNP(forvard));
		DataModel.setRecipientFullNameEW(ParsingResult.GetRecipientFullNameEW(forvard));
	}

	// Відправлення отримано очікуйте смс
	private void ifSendingReceivedSms(String forvard) {
		DataModel.setStatus("Відправлення отримано Очікуйте СМС Забрали: " + ParsingResult.GetDataToTaken(forvard));
		DataModel.setNumber(ParsingResult.GetNumbernTTNNP(forvard));
		DataModel.setRiznucjaDat("Посилку Забрали");
		// DataModel.setStatusSMS(API.GetStatusSendSms(ParsingResult.GetPhoneNumberRepicientTTNNP(forvard)));
		DataModel.setRecipientDateTime(ParsingResult.GetDataToTaken(forvard));
		DataModel.setScheduledDeliveryDate(ParsingResult.GetscheduleddeliverydateTTNNP(forvard));
		DataModel.setDateCreated(ParsingResult.GetDateCreated(forvard));
		DataModel.setRecipientAddress(ParsingResult.fullRecipientAddress(forvard));
		DataModel.setPhoneRecipient(ParsingResult.GetPhoneNumberRepicientTTNNP(forvard));
		DataModel.setRecipientFullNameEW(ParsingResult.GetRecipientFullNameEW(forvard));
	}

}
