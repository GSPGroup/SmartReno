package NovaPoshta;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import Interface.MainController;
import Logic.ERROR;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import sms.api.API;

public class GetStatusForList extends Thread {
	public СreateExcel createexcel;
	public ERROR error;
	public API api;
	public MainController controller;
	private ParsingResult parsinresult;

	public GetStatusForList(MainController controller) {
		this.controller = controller;
	}


	@Override
	public void run() {
		controller.browsgetstatusnp.setDisable(true);
		controller.getstatusnp.setDisable(true);
		GetStatusForListWriteToExcel();
		controller.browsgetstatusnp.setDisable(false);
		controller.getstatusnp.setDisable(false);
	}

	private List<String> lines = null;

	// основний метод
	public void GetStatusForListWriteToExcel() {
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
				forvard = GetXMLAnswer(TTN, forvard);
				if (forvard.contains(NovaPoshta.xmlanswer)) {
					try {
						TimeUnit.SECONDS.sleep(20);
					} catch (InterruptedException e) {
						MainController.appendUsingFileWriter(
								controller.getNamefoldertosavexls().getText().toString() + "/" + "Звіт1.txt",
								MainController.data()
										+ "Сталась помилка при // отриманні статусу по списку накладних (затримка в часі)"
										+ "\r\n");
						error.ErrorWindow();
					}
				}
			} while ((forvard.contains(NovaPoshta.xmlanswer) && MainController.netIsAvailable()) == true);
			if (forvard.contains("<success>true</success>")) {
				String Status00 = ParsingResult.GetStatusTTNNP(forvard);
				if (ParsingResult.GetSuccessTTNNP(forvard).equals("true")) {
					rowNum = WhatGetStatuNpForlist(forvard, Status00, rowNum, sheet, workbook, TTN);
					num++;
				}
				controller.getProgeress().setProgress((double) num / lines.size());
				if (controller.getProgeress().getProgress() == 1.0) {
					controller.getProgeress().setStyle("-fx-accent: green;");
					controller.initialize();
				}
			} else {
				num--;
			}
		}
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
		WhatWriteToFileafterFinishGetStatusNP(num, lines);
	}

	private String GetXMLAnswer(String TTN, String forvard) {
		try {
			forvard = NovaPoshta.Xml(TTN, controller.DefaultPhoneNumberSender.getText().toString());
		} catch (URISyntaxException | IOException e) {
			MainController.appendUsingFileWriter(
					controller.getNamefoldertosavexls().getText().toString() + "/" + "Звіт1.txt",
					MainController.data() + "Сталась помилка при // отриманні статусу по списку накладних" + "\r\n");
			error.ErrorWindow();
		}
		return forvard;
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

	// якщо було замовлено поверненя товару
	private void IfRefusingIsTrue(String forvard) {
		DataModel.setNumber(ParsingResult.GetNumbernTTNNP(forvard));
		String forvard1 = ParsingResult.GetLastCreatedOnTheBasisNumber(forvard);
		String forvard2 = null;
		forvard2 = GetXMLinTheBasicNumber(forvard1, forvard2);
		DataModel.setRecipientFullNameEW(ParsingResult.GetRecipientFullNameEW(forvard));
		DataModel.setPhoneRecipient(ParsingResult.GetPhoneNumberRepicientTTNNP(forvard2));
		DataModel.setDateCreated(ParsingResult.GetDateCreated(forvard));
		DataModel.setRiznucjaDat("Відмова");
		DataModel.setRecipientAddress(ParsingResult.fullRecipientAddress(forvard2));
		IfTTngoHome(forvard2);
		if (!DataModel.getStatus().equals("Поверення Відправлення отримано")) {
			WriteToNewListTTN(ParsingResult.GetNumbernTTNNP(forvard));
		}
		DataModel.setRecipientDateTime(null);
		DataModel.setScheduledDeliveryDate(ParsingResult.GetscheduleddeliverydateTTNNP(forvard2));
	}

	// якщо була відмова івд товару і поверненя не було замовлено
	private void IfRefusingIsNotTrue(String forvard) {
		DataModel.setStatus(ParsingResult.GetStatus(forvard) + " Повернення не замовлено ");
		DataModel.setNumber(ParsingResult.GetNumbernTTNNP(forvard));
		DataModel.setRiznucjaDat("Відмова");
		WriteToNewListTTN(ParsingResult.GetNumbernTTNNP(forvard));
		DataModel.setRecipientDateTime(ParsingResult.GetDataToTaken(forvard));
		DataModel.setScheduledDeliveryDate(ParsingResult.GetscheduleddeliverydateTTNNP(forvard));
		DataModel.setDateCreated(ParsingResult.GetDateCreated(forvard));
		DataModel.setRecipientAddress(ParsingResult.fullRecipientAddress(forvard));
		DataModel.setPhoneRecipient(ParsingResult.GetPhoneNumberRepicientTTNNP(forvard));
		DataModel.setRecipientFullNameEW(ParsingResult.GetRecipientFullNameEW(forvard));
	}

	// Змінено адресу
	private int ifChangeOfAddress(String forvard, int rowNum, HSSFSheet sheet, HSSFWorkbook workbook, String TTN) {
		String forvard2 = null;
		forvard2 = GetXMLIfshangeAdress(TTN, forvard2);
		String forvard3 = ParsingResult.GetLastCreatedOnTheBasisNumber(forvard2);
		String phone = ParsingResult.GetPhoneNumberRepicientTTNNP(forvard2);
		String forvard5 = null;
		forvard5 = GetXMLOfNewPhone(forvard3, phone, forvard5);
		DataModel.setStatus("Змінено адресу " + ParsingResult.GetStatus(forvard5));
		DataModel.setNumber(ParsingResult.GetNumbernTTNNP(forvard));
		IfHuamanGetTTn(forvard, forvard2);
		TakeOrRefusing(forvard, forvard5);
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
		DataModel.setRecipientDateTime(ParsingResult.GetDataToTaken(forvard));
		DataModel.setScheduledDeliveryDate(ParsingResult.GetscheduleddeliverydateTTNNP(forvard));
		DataModel.setDateCreated(ParsingResult.GetDateCreated(forvard));
		DataModel.setRecipientAddress(ParsingResult.fullRecipientAddress(forvard));
		DataModel.setPhoneRecipient(ParsingResult.GetPhoneNumberRepicientTTNNP(forvard));
		DataModel.setRecipientFullNameEW(ParsingResult.GetRecipientFullNameEW(forvard));
	}

	// після переадресаії товар був отриманий чи була відмова
	private void TakeOrRefusing(String forvard, String forvard5) {
		if (ParsingResult.GetStatus(forvard5).equals(ParsingResult.SendingReceived)
				| ParsingResult.GetStatus(forvard5).equals(ParsingResult.SendingReceivedCach)
				| ParsingResult.GetStatus(forvard5).equals(ParsingResult.SendingReceivedSms)) {
			DataModel.setRiznucjaDat("Посилку Забрали");
		} else if (ParsingResult.GetStatus(forvard5).equals(ParsingResult.Refusing)
				| ParsingResult.GetStatus(forvard5).equals(ParsingResult.IfRefusingStatus)) {
			DataModel.setRiznucjaDat("Відмова");
			WriteToNewListTTN(ParsingResult.GetNumbernTTNNP(forvard));
		}
	}

	// посилку забрали чи не забрали ?
	private void IfHuamanGetTTn(String forvard, String forvard2) {
		if (ParsingResult.GetDataToTaken(forvard2) == null) {
			DataModel.setRiznucjaDat(ParsingResult.RahunokDnivIfNotTaken(forvard2, controller));

			WriteToNewListTTN(ParsingResult.GetNumbernTTNNP(forvard));
		} else {
			DataModel.setRiznucjaDat("Посилку Забрали");
			DataModel.setRecipientDateTime(ParsingResult.GetDataToTaken(forvard2));
		}
	}

	// отримати повну відповідь ХМЛ за допомоги НОВОГО номеру телефону
	private String GetXMLOfNewPhone(String forvard3, String phone, String forvard5) {
		try {
			forvard5 = NovaPoshta.Xml(forvard3, phone);
		} catch (URISyntaxException | IOException e) {
			MainController.appendUsingFileWriter(
					controller.getNamefoldertosavexls().getText().toString() + "/" + "Звіт1.txt",
					MainController.data() + "Сталась помилка при // для спмску метод Змінено адресу" + "\r\n");
			error.ErrorWindow();
		}
		return forvard5;
	}

	// отримати ХМЛ відповідь по внутрішньому номері телефіну накладної
	private String GetXMLIfshangeAdress(String TTN, String forvard2) {
		try {
			forvard2 = NovaPoshta.Xml(TTN, controller.DefaultPhoneNumberSender.getText().toString());

		} catch (URISyntaxException | IOException e1) {
			MainController.appendUsingFileWriter(
					controller.getNamefoldertosavexls().getText().toString() + "/" + "Звіт1.txt",
					MainController.data() + "Сталась помилка при // для спмску метод Змінено адресу" + "\r\n");
			error.ErrorWindow();
		}
		return forvard2;
	}

	// якщо посилка відправляється назад
	private void IfTTngoHome(String forvard2) {
		if (ParsingResult.fullRecipientAddress(forvard2).equals(ParsingResult.GoBack)) {
			DataModel.setStatus("Поверення " + ParsingResult.GetStatus(forvard2));
		} else {
			DataModel.setStatus("Поверення НЕ замовлено " + ParsingResult.GetStatus(forvard2));
		}
	}

	// отримати відповідь хмл з внітрішнім номером наклданої
	private String GetXMLinTheBasicNumber(String forvard1, String forvard2) {
		try {
			forvard2 = NovaPoshta.Xml(forvard1, controller.DefaultPhoneNumberSender.getText().toString());
		} catch (URISyntaxException | IOException e) {
			MainController.appendUsingFileWriter(
					controller.getNamefoldertosavexls().getText().toString() + "/" + "Звіт1.txt",
					MainController.data() + "Сталась помилка при // для спмску метод ВІДМОВА" + "\r\n");
			error.ErrorWindow();
		}
		return forvard2;
	}

	// Відмова
	private void ifRefusing(String forvard, int rowNum, HSSFSheet sheet, HSSFWorkbook workbook) {
		if (ParsingResult.GetLastCreatedOnTheBasisNumber(forvard).equals(" ")) {
			IfRefusingIsNotTrue(forvard);
		} else {
			IfRefusingIsTrue(forvard);
		}
	}
}
