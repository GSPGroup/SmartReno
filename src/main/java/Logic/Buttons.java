package Logic;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.swing.JFileChooser;

import Interface.MainController;
import NovaPoshta.GetStatusForList;
import NovaPoshta.NovaPoshta;
import NovaPoshta.ParsingResult;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import sms.api.API;
import sms.api.OperatorPhoneRecipient;
import sms.api.RequestBuilder;

public class Buttons extends Thread {
	public API api;
	public ERROR error;
	public Logic logic;
	public RadioButtons radiobutton;
	public MainController controller;
	private OperatorPhoneRecipient phonerecipient;
	private GetStatusForList getstatusforlist;
	private ParsingResult parsinresult;

	@Override
	public void run() {
		controller.SendSMS.setDisable(true);
		controller.browsfilefromsms.setDisable(true);
		ButtonSendSMS();
		controller.SendSMS.setDisable(false);
		controller.browsfilefromsms.setDisable(false);
	}

	public Buttons(MainController controller) {
		this.controller = controller;
		this.phonerecipient = new OperatorPhoneRecipient(controller);
		this.getstatusforlist = new GetStatusForList(controller);
		this.radiobutton = new RadioButtons(controller);
		this.error = new ERROR(controller);
	}

	// кнопка OK на вспливаючому вікні
	public void ButtonAccespaneYes(MainController mainController) {
		mainController.initialize();
	}

	// кнопка OK на вспливаючому вікні
	public void ButtonAccespaneNo(MainController mainController) {
		mainController.initialize();
	}

	// кнопка відправити смс по накладним
	public void ButtonSendSMS() {
		String TTN = null;
		String whatsend1 = null;
		List<String> lines = null;
		int smssend = 0;
		int duble = 0;
		lines = controller.ReadFileForTTN(controller.Browsfilefromsms.toString(), lines);
		for (String line : lines) {
			TTN = line.substring(0, 14);
			List<String> textFromFile = null;
			if ((new File(controller.getNamefoldertosavexls().getText().toString() + "/Звіт1.txt")).exists()) {
				textFromFile = controller.ReadFileForTTN(
						controller.getNamefoldertosavexls().getText().toString() + "/Звіт1.txt", textFromFile);
				String fileName = controller.getNamefoldertosavexls().getText().toString() + "/накладні.txt";
				if ((new File(fileName)).exists()) {
					if (!textFromFile.toString().contains("Ваш товар отправлен. ТТН№" + TTN)) {
						smssend = SendSMStoGetPhoneNumberToNP(TTN, whatsend1, lines, smssend);
					} else {
						MainController.appendUsingFileWriter(
								controller.getNamefoldertosavexls().getText().toString() + "/" + "Звіт1.txt",
								MainController.data() + "SMS з номером накладної: " + TTN
										+ " НЕ було відправлено, бо смс було відправлено раніше" + "\r\n");
						duble++;
					}
				} else {
					smssend = SendSMStoGetPhoneNumberToNP(TTN, whatsend1, lines, smssend);
				}
			} else {
				smssend = SendSMStoGetPhoneNumberToNP(TTN, whatsend1, lines, smssend);
			}
		}
		error.ReportOfSendSMS(this, lines, smssend, duble);
	}

	///
	// метод відправки смс попередньо діставши номер телефоу з нової пошти
	private int SendSMStoGetPhoneNumberToNP(String TTN, String whatsend1, List<String> lines, int smssend) {
		WriteTTNtoFileAfterSendSMS(TTN);
		WriteTTNtoFileAfterSendSMSInFIle(TTN);
		String forvard = null;
		forvard = TryToGetTtn(TTN, forvard);
		try {
			TimeUnit.SECONDS.sleep(1);
		} catch (InterruptedException e) {
			error.WindowOfERRORtosendSMS();
		}
		String phonenumberrepicient = ParsingResult.GetPhoneNumberRepicientTTNNP(forvard);
		String URL = "http://api.atompark.com/members/sms/xml.php";
		String login = "pro100-pr2@ukr.net";
		String password = "pro100pr1";
		RequestBuilder Request = new RequestBuilder(URL);
		if (controller.getSmartio().isSelected() == true) {
			whatsend1 = "Smartio";
		} else if (controller.getRenomax().isSelected() == true) {
			whatsend1 = "Renomax";
		}
		String textsms = "Интернет-Маназин " + "\"" + whatsend1 + "\"" + ". Ваш товар отправлен. ТТН№" + TTN;
		API ApiSms = new API(Request, login, password);
		String Status = ApiSms.sendSms(whatsend1, textsms, phonenumberrepicient, phonenumberrepicient);
		int Status1 = Status.indexOf("<status>") + 8;
		int Status2 = Status.indexOf("</status>");
		String Status0 = Status.substring(Status1, Status2);
		if (Status0.equals("1")) {
			smssend++;
		}
		controller.getProgeress().setProgress((double) smssend / (double) lines.size());
		controller.initialize();
		if (controller.getProgeress().getProgress() == 1.0) {
			controller.getProgeress().setStyle("-fx-accent: green;");
			controller.initialize();
		}
		MainController.appendUsingFileWriter(
				controller.getNamefoldertosavexls().getText().toString() + "/" + "Звіт1.txt",
				MainController.data() + whatsend1 + "  " + textsms + "  " + phonenumberrepicient + "\r\n");

		return smssend;
	}

	// записати номер накладної в файл після того як було відправленно смс
	private void WriteTTNtoFileAfterSendSMS(String TTN) {
		List<String> list = null;
		String fileName = controller.getNamefoldertosavexls().getText().toString() + "/накладні.txt";
		if ((new File(fileName)).exists()) {
			list = controller.ReadFileForTTN(fileName, list);
			if (!list.toString().contains(TTN)) {
				MainController.appendUsingFileWriter(fileName, TTN + ";1" + "\r\n");

			}
		} else {
			MainController.appendUsingFileWriter(fileName, TTN + ";1" + "\r\n");

		}
	}

	// записати номер накладної в файл після того як було відправленно смс
	private void WriteTTNtoFileAfterSendSMSInFIle(String TTN) {
		Date currentDate = new Date();
		SimpleDateFormat dateFormat = null;
		dateFormat = new SimpleDateFormat("MMMM");
		Calendar c = Calendar.getInstance();
		int year = c.get(Calendar.YEAR);
		int month = c.get(Calendar.MONTH) + 1;
		int day = c.get(Calendar.DAY_OF_MONTH);
		String name = "Накладні за " + String.valueOf(day) + "." + String.valueOf(month) + "." + String.valueOf(year)
				+ ".txt";
		String folderName = dateFormat.format(currentDate);
		File folder = new File(controller.getNamefoldertosavexls().getText().toString() + "/" + folderName);
		folder.mkdir();
		String folderNamettn = "Папка з накладними";
		File folderttn = new File(folder + "\\" + folderNamettn);
		folderttn.mkdir();
		String folder1 = folderName + "\\" + folderNamettn;
		String locationfile = folder1 + "\\" + name;
		List<String> list = null;
		String fileName = controller.getNamefoldertosavexls().getText().toString() + "\\" + locationfile;
		if ((new File(fileName)).exists()) {
			list = controller.ReadFileForTTN(fileName, list);
			if (!list.toString().contains(TTN)) {
				MainController.appendUsingFileWriter(
						controller.getNamefoldertosavexls().getText().toString() + "\\" + locationfile,
						TTN + ";1" + "\r\n");
			}
		} else {
			MainController.appendUsingFileWriter(
					controller.getNamefoldertosavexls().getText().toString() + "\\" + locationfile,
					TTN + ";1" + "\r\n");
		}

	}

	// отримуєм номер телефону по номеру накладної поки не отримаєм
	private String TryToGetTtn(String TTN, String forvard) {
		do {
			try {
				forvard = NovaPoshta.Xml(TTN, controller.DefaultPhoneNumberSender.getText().toString());
			} catch (URISyntaxException | IOException e) {
				MainController.appendUsingFileWriter(
						controller.namefoldertosavexls.getText().toString() + "/" + "Звіт1.txt", MainController.data()
								+ "Сталась помилка при 	// кнопка відправити смс по накладним" + "\r\n");
				error.ERROR();
			}
			if (forvard.contains(NovaPoshta.xmlanswer)) {
				try {
					TimeUnit.SECONDS.sleep(20);
				} catch (InterruptedException e) {
					MainController.appendUsingFileWriter(
							controller.getNamefoldertosavexls().getText().toString() + "/" + "Звіт1.txt",
							MainController.data() + "Сталась помилка при 	// кнопка відправити смс по накладним"
									+ "\r\n");
					error.ERROR();
				}
			}
		} while (forvard.contains(NovaPoshta.xmlanswer));
		return forvard;
	}

	// кнопка отримати статус по накладним нової пошти
	public void ButtonGetStatusNP(MainController mainController, RadioButtons radiobutton) {
		if (mainController.foronenp.isSelected() == true) {
			String forvard = null;
			do {
				try {
					forvard = NovaPoshta.Xml(mainController.npttn.getText().toString(),
							controller.DefaultPhoneNumberSender.getText().toString());
				} catch (URISyntaxException | IOException e1) {
					MainController.appendUsingFileWriter(
							controller.getNamefoldertosavexls().getText().toString() + "/" + "Звіт1.txt",
							MainController.data()
									+ "Сталась помилка при // кнопка отримати статус по накладним нової пошти"
									+ "\r\n");
					error.ERROR();
				}
				if (forvard.contains(NovaPoshta.xmlanswer)) {
					try {
						TimeUnit.SECONDS.sleep(20);
					} catch (InterruptedException e) {
						MainController.appendUsingFileWriter(
								controller.getNamefoldertosavexls().getText().toString() + "/" + "Звіт1.txt",
								MainController.data()
										+ "Сталась помилка при // кнопка отримати статус по накладним нової пошти"
										+ "\r\n");
						error.ERROR();
					}
				}
			} while (forvard.contains(NovaPoshta.xmlanswer));
			String Status00 = null;
			try {
				Status00 = parsinresult.GetStatusTTNNP(forvard);
			} catch (StringIndexOutOfBoundsException e) {
				error.ERROR();
			}
			mainController.getstatusforone.WhatGetStatuNpForOneTTn(forvard, Status00);
		} else if (mainController.manynp.isSelected() == true) {
			getstatusforlist.GetStatusForListWriteToExcel();
		}
		mainController.status.setText("Готово");
		radiobutton.WhatRadioButtonSelected();
		mainController.initialize();
		MainController.appendUsingFileWriter(
				controller.getNamefoldertosavexls().getText().toString() + "/" + "Звіт1.txt",
				MainController.data() + "звіт по накладній " + mainController.npttn.getText().toString() + "\r\n");
	}

	// кнопка відправити смс рекламного характеру
	public void ButtonsSendSMSAdvertising(MainController mainController) {
		String textmasage = mainController.messagetext.getText().toString();
		int result = (int) Math.ceil((double) textmasage.length() / 69);
		if (result > 1) {
			mainController.paneagree.setDisable(false);
			mainController.paneagree.setVisible(true);
			mainController.doyouagree.setText("Вміст тексту повідомлення перевишує 1 SMS выдправити ?!");
		} else {
			Logic logic = new Logic(mainController);
			logic.start();
			mainController.paneagree.setDisable(true);
			mainController.paneagree.setVisible(false);
		}
		mainController.initialize();
	}

	// відправити після попередження
	public void ButtonsYes(MainController mainController) {
		Logic logic = new Logic(mainController);
		logic.start();
		mainController.paneagree.setDisable(true);
		mainController.paneagree.setVisible(false);
		mainController.status.setText("Відправленно");
		mainController.initialize();
	}

	// Скасувати відправку
	public void ButtonNo(MainController mainController) {
		mainController.paneagree.setDisable(true);
		mainController.paneagree.setVisible(false);
		mainController.initialize();
	}

	// кнопка очстити поле для накладної новаої пошти
	public void ButtonClearTTN(MainController mainController) {
		mainController.npttn.setText("");
		mainController.initialize();
	}

	// кнопка очстити поле для телефону і тексту смс
	public void ButtonClearTextandPhone(MainController mainController) {
		mainController.telephonereceiver.setText("");
		mainController.messagetext.setText("");
		mainController.phonemasage.setText("вкажіть номер");
		mainController.phonemasage.setTextFill(Color.RED);
		mainController.sendsmsadvertising.setDisable(true);
		mainController.initialize();
	}

	// кнопка перевірити стан рахунку
	public void ButtonGetHowManyMoney(MainController mainController) {
		mainController.progeress.setProgress(0.0);
		mainController.api.Chekrahunky(mainController);
		mainController.progeress.setProgress(1.0);
		mainController.initialize();
	}

	// кнопка перевірки правильнсоті введеної ТТН
	public void ButtonReloadTtn() {
		if (controller.npttn.getText().toString().length() == 14) {
			controller.getstatusnp.setDisable(false);
			controller.iffileisemptynp.setText("");
		} else {
			controller.iffileisemptynp.setVisible(true);
			controller.iffileisemptynp.setText("невірний формат");
			controller.getGetstatusnp().setDisable(true);
		}
		controller.initialize();
	}

	// кнопка перевірки правильнсоті введеної ТТН
	public void ButtonReloadTtnmessagetextandtelephonereceiver(MainController mainController) {
		if (mainController.telephonereceiver.getText().toString().isEmpty()) {
			mainController.phonemasage.setText("вкажіть номер");
			mainController.phonemasage.setTextFill(Color.RED);
			mainController.sendsmsadvertising.setDisable(true);
		}
		phonerecipient.WhatOperatorrecipient(mainController);
		if ((mainController.telephonereceiver.getText().toString().length() == 12
				& !mainController.messagetext.getText().toString().isEmpty()) != false) {
			mainController.sendsmsadvertising.setDisable(false);
		} else {
			mainController.sendsmsadvertising.setDisable(true);
		}
		mainController.initialize();
	}

	// Копка вибору папки для змережання файлів ексель з нвітом по накладним
	// нової пошти
	public void ButtonBrowsFolderToSaveXLSNP(MainController mainController) {
		JFileChooser f = new JFileChooser();
		f.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		f.showSaveDialog(null);
		mainController.getNamefoldertosavexls().setText(String.valueOf(f.getSelectedFile()));
		mainController.initialize();
	}

	// Копка вмбору дефолтної папки для відкриття файлу з накладними
	public void ButtonBrowsDefaultPacageToFindfileTTNNP(MainController mainController) {
		JFileChooser f = new JFileChooser();
		f.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		f.showSaveDialog(null);
		mainController.namedefaultpacagetofindfilettnnp.setText(String.valueOf(f.getSelectedFile()));
		mainController.initialize();
	}

	// кнопка відкрити файл з списком накладних нової пошти
	public void ButtonBrowsGetStatusNP(MainController mainController) {
		FileChooser fileChooser = mainController.logic.Openfiledialog();
		mainController.Browsgetstatusnp = fileChooser.showOpenDialog(null);
		mainController.logic.ChekWhatFileBrowsFfromGetStatusNP();
		mainController.initialize();
	}

	// кнопка відкрити файл зі списком накладних для відправки смс Нової пошти
	public void ButtonBrowsFileFromSms(MainController mainController) {
		FileChooser fileChooser = mainController.logic.Openfiledialog();
		mainController.Browsfilefromsms = fileChooser.showOpenDialog(null);
		mainController.logic.ChekWhatFileBrowsFfromSMS();
		mainController.initialize();
	}

	// кнопка відкрити файл з номерами телефоні для відправки рекламних смс
	public void ButtonBrowsFleFromSmsAdvertising(MainController mainController) {
		FileChooser fileChooser = mainController.logic.Openfiledialog();
		mainController.Browsflefromsmsadvertising = fileChooser.showOpenDialog(null);
		mainController.logic.ChekWhatFileBrowsFfromAdvertising();
		mainController.initialize();
	}

}
