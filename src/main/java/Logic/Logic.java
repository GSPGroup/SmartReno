package Logic;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import Interface.MainController;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import sms.api.API;
import sms.api.RequestBuilder;

public class Logic extends Thread {
	public Buttons buttons;
	private MainController controller;
	public ERROR error;

	public Logic(MainController controller) {
		this.controller = controller;
		this.error = new ERROR(controller);
	}

	@Override
	public void run() {
		AgreeSend();

	}

	// підрахунок скікльки потрібно відправити смс
	public void HowSmsNeedToSend() {
		String textmasage = controller.messagetext.getText().toString();
		int result = (int) Math.ceil((double) textmasage.length() / 69);
		if (!textmasage.isEmpty()) {
			if (textmasage.length() > 69) {
				controller.howsms.setText(String.valueOf(result + " SMS"));
				controller.howsms.setTextFill(Color.RED);
			} else if (textmasage.length() < 70 | textmasage.length() < 1) {
				if (!textmasage.isEmpty()) {
					if (controller.smart.isSelected() == true || controller.reno.isSelected() == true
							|| controller.smartreno.isSelected() == true) {
						if (controller.singlesms.isSelected() == true) {
							if ((controller.telephonereceiver.getText().toString().length() == 12
									& !controller.messagetext.getText().toString().isEmpty()) != false) {
								controller.sendsmsadvertising.setDisable(false);
							} else {
								controller.sendsmsadvertising.setDisable(true);
							}
						}
					}
				}
				controller.textisempty.setText("");
				controller.howsms.setText("1 SMS");
				controller.howsms.setTextFill(Color.GREEN);
			}
		} else {
			controller.textisempty.setText("текст повідомленя порожній");
			controller.textisempty.setTextFill(Color.RED);
			controller.sendsmsadvertising.setDisable(true);
		}
	}

	// який файл був вибраий, і чи підходить він для тої чи іншиї операції
	public void WhatFileChoose(List<String> lines, File filenave) {
		lines = controller.ReadFileForTTN(filenave.toString(), lines);
		int max = 0;
		int min = 100;
		for (String line : lines) {
			if (line.length() > max) {
				max = line.length();
			}
			if (line.length() < min) {
				min = line.length();
			}
		}
		ChekWhaFileChooseTofilefromsms(filenave, max, min);
		ChekWhaFileChooseToflefromsmsadvertising(filenave, max, min);
	}

	// який файл був вибраий, і чи підходить він для тої чи іншиї операції
	public void WhatFileChooseFromNP(List<String> lines, File filenave) {
		lines = controller.ReadFileForTTN(filenave.toString(), lines);
		int max = 0;
		int min = 100;
		for (String line : lines) {
			if (line.length() > max) {
				max = line.length();
			}
			if (line.length() < min) {
				min = line.length();
			}
		}
		ChekWhaFileChooseToGetStatusNp(filenave, max, min);
	}

	// перевірка чи підходить вибраний файли для відправки смс з звітом по
	// накладнимм
	private void ChekWhaFileChooseTofilefromsms(File filenave, int max, int min) {
		if (filenave.equals(controller.Browsfilefromsms)) {
			if (!(max == 16 && min == 16)) {
				controller.SendSMS.setDisable(true);
				controller.howmanysms.setText("0");
			} else {
				controller.howmanysms.setText(String.valueOf(controller.logic.SummRow()));
			}
		}
	}

	// перевірка чи підходить вибраний файли для відправки рекламних смс по
	// номерам телефонів
	private void ChekWhaFileChooseToflefromsmsadvertising(File filenave, int max, int min) {
		if (filenave.equals(controller.Browsflefromsmsadvertising)) {
			if (!(max == 12 && min == 12)) {
				controller.sendsmsadvertising.setDisable(true);
				controller.howmanysmsadvertising.setText("0");
			} else {
				controller.howmanysmsadvertising.setText(String.valueOf(controller.logic.SummRow()));
			}
		}
	}

	// перевірка чи підходить вибраний файли для отримання статусу по накладним
	// НовоїПошти
	private void ChekWhaFileChooseToGetStatusNp(File filenave, int max, int min) {
		if (filenave.equals(controller.Browsgetstatusnp)) {
			if (!(max == 16 && min == 16)) {
				controller.howmanyttnnp.setText("0");
				controller.getstatusnp.setDisable(true);
			} else {
				List<String> filechose = null;
				filechose = controller.ReadFileForTTN(controller.Browsgetstatusnp.toString(), filechose);
				String fileName = controller.getNamefoldertosavexls().getText().toString() + "/накладні.txt";
				List<String> file = null;
				if ((new File(fileName)).exists()) {
					file = controller.ReadFileForTTN(
							controller.getNamefoldertosavexls().getText().toString() + "/накладні.txt", file);
					for (String line : filechose) {
						if (!file.toString().contains(line)) {
							MainController.appendUsingFileWriter(
									controller.getNamefoldertosavexls().getText().toString() + "/накладні.txt",
									line + "\r\n");
						}
					}
				} else {
					for (String line : filechose) {
						MainController.appendUsingFileWriter(
								controller.getNamefoldertosavexls().getText().toString() + "/накладні.txt",
								line + "\r\n");
					}
				}
//				controller.howmanyttnnp.setText(null);
//				controller.howmanyttnnp.setText(String.valueOf(controller.logic.SummRowforNP()));

			}
		}
	}

	// погодження на відправку рекламних повідомлень
	public void AgreeSend() {
		String login = "pro100-pr2@ukr.net";
		String password = "pro100pr1";
		String URL = "http://api.atompark.com/members/sms/xml.php";
		RequestBuilder Request = new RequestBuilder(URL);
		API ApiSms = new API(Request, login, password);
		String whatsend1 = null;
		if (controller.singlesms.isSelected()) {
			ChooseRadioButtonSingleSms(ApiSms, whatsend1);
		} else if (controller.massmailing.isSelected()) {
			ChooseRadioButtonmassmailing(ApiSms, whatsend1);
		}
	}

	// якщо вибрана масова розсилка Рекламних смс
	private void ChooseRadioButtonmassmailing(API ApiSms, String whatsend1) {
		String phonenomber = null;
		String textmasage;
		int smssend = 0;
		int smsned = 0;
		List<String> lines = null;
		lines = controller.ReadFileForTTN(controller.Browsflefromsmsadvertising.toString(), lines);
		for (String line : lines) {
			phonenomber = line.substring(0, 12);
			whatsend1 = WhooNameSender(whatsend1);
			textmasage = controller.messagetext.getText().toString();
			String Status = API.StatusSms(ApiSms, whatsend1, phonenomber, textmasage,phonenomber);
			if (Status.equals("1")) {
				smssend++;
			}
			smsned++;
			MainController.appendUsingFileWriter("/Desktop/Звіт1.txt",
					MainController.data() + whatsend1 + "  " + textmasage + "  " + phonenomber + "\r\n");
		}
		DialogWindowHowManySMSSend(phonenomber, smssend, lines);
	}

	// діалогов вікно зі звітом сікльки повідомлень було відправлено.
	private void DialogWindowHowManySMSSend(String phonenomber, int smssend, List<String> lines) {
		final int howsmsneed = lines.size();
		final int howsmssend = smssend;
		final String phone = phonenomber;
		if (smssend > 0) {
			Platform.runLater(new Runnable() {
				@Override
				public void run() {
					Alert alert = new Alert(AlertType.INFORMATION);
					alert.setTitle("Виконано");
					alert.setHeaderText("відправлено " + howsmssend + "/" + howsmsneed);
					alert.setContentText("повідомлення відправлено");
					alert.showAndWait();
					controller.runindicator.setProgress(1.0);
					controller.getProgeress().setProgress(1.0);
				}
			});
		} else {
			Platform.runLater(new Runnable() {
				@Override
				public void run() {
					Alert alert = new Alert(AlertType.ERROR);
					alert.setTitle(" НЕ Виконано");
					alert.setHeaderText("ПОВІДМЛЕННЯ НЕ БУЛИ ВІДПРАВЛЕНІ");
					alert.showAndWait();
					controller.runindicator.setProgress(1.0);
					controller.getProgeress().setProgress(1.0);
				}
			});
		}
	}

	// якщо вибрана одиночна розсилка Рекламних смс
	private void ChooseRadioButtonSingleSms(API ApiSms, String whatsend1) {
		String phonenomber;
		String textmasage;
		int smssend = 0;
		int smsned = 0;
		textmasage = controller.messagetext.getText().toString();
		phonenomber = controller.telephonereceiver.getText().toString();
		whatsend1 = WhooNameSender(whatsend1);
		textmasage = controller.messagetext.getText().toString();
		String Status = API.StatusSms(ApiSms, whatsend1, phonenomber, textmasage,phonenomber);
		if (Status.equals("1")) {
			smssend++;
		}
		smsned++;
		MainController.appendUsingFileWriter("/Desktop/Звіт1.txt",
				MainController.data() + whatsend1 + "  " + textmasage + "  " + phonenomber + "\r\n");
		MainController.appendUsingFileWriter("/Desktop/Звіт1.txt",
				MainController.data() + "Відправило СМС: " + smssend + "/" + smsned + "\r\n");
		DialogWindwOfSendOneSMS(phonenomber, smssend);
	}

	// яке альфа імя було вибрано для відправки
	private String WhooNameSender(String whatsend1) {
		if (controller.smart.isSelected() == true) {
			whatsend1 = "Smartio";
		} else if (controller.reno.isSelected() == true) {
			whatsend1 = "Renomax";
		} else if (controller.smartreno.isSelected() == true) {
			whatsend1 = "smart reno";
		}
		return whatsend1;
	}

	// діалогове вікно з повідомлення про чи було відправлено повідомлення
	private void DialogWindwOfSendOneSMS(String phonenomber, int smssend) {
		final String phone = phonenomber;
		if (smssend > 0) {
			Platform.runLater(new Runnable() {
				@Override
				public void run() {
					Alert alert = new Alert(AlertType.INFORMATION);
					alert.setTitle("Виконано");
					alert.setHeaderText("абоненту: " + phone);
					alert.setContentText("повідомлення відправлено");
					alert.showAndWait();
					controller.runindicator.setProgress(1.0);
					controller.getProgeress().setProgress(1.0);
				}
			});
		} else {
			Platform.runLater(new Runnable() {
				@Override
				public void run() {
					Alert alert = new Alert(AlertType.ERROR);
					alert.setTitle(" НЕ Виконано");
					alert.setHeaderText("абоненту: " + phone);
					alert.setContentText("повідомлення НЕ відправлено");
					alert.showAndWait();
					controller.runindicator.setProgress(1.0);
					controller.getProgeress().setProgress(1.0);
				}
			});
		}
	}

	// перевірка чи файл який вибраний для відправки смс відповідає потребам
	public void ChekWhatFileBrowsFfromSMS() {
		if (controller.Browsfilefromsms != null) {
			if (controller.Browsfilefromsms == null) {
				controller.namefilesms.setText("файл вибраний не вірно");
				controller.howmanysms.setText("Жодної");
				controller.smartio.setDisable(true);
				controller.renomax.setDisable(true);
				controller.SendSMS.setDisable(true);
			} else {
				int name = controller.Browsfilefromsms.toString().indexOf(".txt");
				int name1 = controller.Browsfilefromsms.toString().lastIndexOf("\\") + 1;
				controller.namefilesms.setText(controller.Browsfilefromsms.toString().substring(name1, name));
				controller.namefilesms.setTextFill(Color.GREEN);
				controller.howmanysms.setText(String.valueOf(SummRow()));
				int how = controller.howmanysms.toString().lastIndexOf("'") + 1;
				if (controller.howmanysms.toString().substring(39, how - 1).equals("0")) {
					controller.howmanysms.setTextFill(Color.RED);
					controller.iffileisemptysendsms.setText("список порожній");
					controller.iffileisemptysendsms.setTextFill(Color.RED);
					controller.smartio.setDisable(true);
					controller.renomax.setDisable(true);
					controller.SendSMS.setDisable(true);
				} else {
					double rah = Double.parseDouble(controller.api.Chekrahunky(controller));
					double symasms = (double) controller.logic.SummRow() * 0.26;
					if (symasms > rah) {
						controller.whethersufficientfundsfrom.setText("Недостатньокоштів на рахунку");
						error.YouDontHaveMoneyToSendAllSMS();
						controller.whethersufficientfundsfrom.setTextFill(Color.RED);
						controller.smartio.setDisable(true);
						controller.renomax.setDisable(true);
					} else {
						controller.whethersufficientfundsfrom.setText("коштів достатньо для відправки всіх СМС");
						controller.whethersufficientfundsfrom.setTextFill(Color.GREEN);
						controller.smartio.setDisable(false);
						controller.renomax.setDisable(false);
						controller.iffileisemptysendsms.setText("");
						controller.howmanysms.setTextFill(Color.GREEN);
					}
				}
			}
			List<String> lines = null;
			WhatFileChoose(lines, controller.Browsfilefromsms);
		} else {
			controller.namefilesms.setText("Файл не вибрано");
			controller.namefilesms.setTextFill(Color.RED);
			controller.howmanysms.setText("");
			controller.iffileisemptysendsms.setText("");

		}
		controller.initialize();
	}

	// відкрити діалогове вікно з фільтром тільки на файли типу ТХТ
	public FileChooser Openfiledialog() {
		FileChooser fileChooser = new FileChooser();
		ExtensionFilter extFilterTxT = new ExtensionFilter("TxT files (*.txt)", "*.TXT");
		fileChooser.getExtensionFilters().addAll(extFilterTxT);
		return fileChooser;
	}

	// порахувати скікльки накладних або номерів телефонів в вибраному файлі
	public int SummRow() {
		int i = 0;
		BufferedReader bufferedReader = null;
		String FindFileTxt = null;
		if (controller.Browsgetstatusnp != null) {
			FindFileTxt = controller.Browsgetstatusnp.toString();
		} else if (controller.Browsfilefromsms != null) {
			FindFileTxt = controller.Browsfilefromsms.toString();
		} else if (controller.Browsflefromsmsadvertising != null) {
			FindFileTxt = controller.Browsflefromsmsadvertising.toString();
		}
		if (FindFileTxt == null) {
			String textmasage = controller.messagetext.getText().toString();
			int result = (int) Math.ceil((double) textmasage.length() / 69);
			return result;
		} else {
			FileReader fileReader = null;
			try {
				fileReader = new FileReader(FindFileTxt);
			} catch (FileNotFoundException e2) {
				MainController.appendUsingFileWriter(
						controller.getNamefoldertosavexls().getText().toString() + "/" + "Звіт1.txt",
						MainController.data()
								+ "Сталась помилка при // порахувати скікльки накладних або номерів телефонів в вибраному файлі"
								+ "\r\n");
				error.ERROR();
			}
			bufferedReader = new BufferedReader(fileReader);
			try {
				while (bufferedReader.readLine() != null)
					i++;
			} catch (IOException e1) {
				MainController.appendUsingFileWriter(
						controller.getNamefoldertosavexls().getText().toString() + "/" + "Звіт1.txt",
						MainController.data()
								+ "Сталась помилка при // порахувати скікльки накладних або номерів телефонів в вибраному файлі"
								+ "\r\n");
				error.ERROR();
			}
			try {
				bufferedReader.close();
			} catch (IOException e) {
				MainController.appendUsingFileWriter(
						controller.getNamefoldertosavexls().getText().toString() + "/" + "Звіт1.txt",
						MainController.data()
								+ "Сталась помилка при // порахувати скікльки накладних або номерів телефонів в вибраному файлі"
								+ "\r\n");
				error.ERROR();
			}
			controller.initialize();
			return i;
		}
	}

	// порахувати скікльки накладних або номерів телефонів в вибраному файлі
	public int SummRowforNP() {
		int i = 0;
		List<String> lines = null;
		try {
			String fileName = controller.getNamefoldertosavexls().getText().toString() + "/накладні.txt";
			if ((new File(fileName)).exists()) {
				lines = Files.readAllLines(
						Paths.get(controller.getNamefoldertosavexls().getText().toString() + "/накладні.txt"),
						StandardCharsets.UTF_8);
			} else {
				String FindFileTxt = null;
				if (controller.Browsgetstatusnp != null) {
					FindFileTxt = controller.Browsgetstatusnp.toString();
					lines = Files.readAllLines(Paths.get(FindFileTxt), StandardCharsets.UTF_8);
				}
			}

		} catch (IOException e) {
			error.ERROR();
		}
		if (lines.size() != 0) {
			controller.getstatusnp.setDisable(false);
		}
		return lines.size();
	}

	// перевірка чи файл який вибраний для відправки рекламним смс відповідає
	// потребам
	public void ChekWhatFileBrowsFfromAdvertising() {
		if (controller.Browsflefromsmsadvertising != null) {
			if (controller.Browsflefromsmsadvertising == null) {
				controller.namefilesmsadvertising.setText("файл вибраний не вірно");
				controller.howmanysmsadvertising.setText("Жодної");
				controller.sendsmsadvertising.setDisable(true);
			} else {
				int name = controller.Browsflefromsmsadvertising.toString().indexOf(".txt");
				int name1 = controller.Browsflefromsmsadvertising.toString().lastIndexOf("\\") + 1;
				controller.namefilesmsadvertising
						.setText(controller.Browsflefromsmsadvertising.toString().substring(name1, name));
				controller.namefilesmsadvertising.setTextFill(Color.GREEN);
				controller.howmanysmsadvertising.setText(String.valueOf(SummRow()));
				int how = controller.howmanysmsadvertising.toString().lastIndexOf("'") + 1;
				if (controller.howmanysmsadvertising.toString().substring(50, how - 1).equals("0")) {
					controller.howmanysmsadvertising.setTextFill(Color.RED);
					controller.iffileisempty.setText("список порожній");
					controller.iffileisempty.setTextFill(Color.RED);
					controller.sendsmsadvertising.setDisable(true);
				} else {
					if (controller.massmailing.isSelected() == true) {
						if (controller.messagetext.getText().toString().length() > 0) {
							double rah = Double.parseDouble(controller.api.Chekrahunky(controller));
							double symasms = (double) SummRow() * 0.26;
							if (symasms > rah) {
								controller.whethersufficientfundsfromadvertising
										.setText("Недостатньокоштів на рахунку");
								controller.whethersufficientfundsfromadvertising.setTextFill(Color.RED);
								controller.sendsmsadvertising.setDisable(true);
							} else {
								controller.whethersufficientfundsfromadvertising
										.setText("коштів достатньо для відправки всіх СМС");
								controller.whethersufficientfundsfromadvertising.setTextFill(Color.GREEN);

								if ((controller.telephonereceiver.getText().toString().length() == 12
										& !controller.messagetext.getText().toString().isEmpty()) != false) {
									if (controller.Browsflefromsmsadvertising != null) {
										controller.sendsmsadvertising.setDisable(false);
									} else {
										controller.sendsmsadvertising.setDisable(true);
									}
								} else {
									controller.sendsmsadvertising.setDisable(true);
								}
								controller.iffileisempty.setText("");
								controller.howmanysmsadvertising.setTextFill(Color.GREEN);
							}
						}
					}
					List<String> lines = null;
					WhatFileChoose(lines, controller.Browsflefromsmsadvertising);
				}
			}
		} else {
			controller.namefilesmsadvertising.setText("Файл не вибрано");
			controller.namefilesmsadvertising.setTextFill(Color.RED);
			controller.howmanysmsadvertising.setText("");
			controller.iffileisempty.setText("");
		}
		controller.initialize();
	}

	// перевірка файла який був вибраний для отримання статусу по накладним
	// нової пошти
	public void ChekWhatFileBrowsFfromGetStatusNP() {
		if (controller.Browsgetstatusnp != null) {
			if (controller.Browsgetstatusnp == null) {
				controller.namefilettnnp.setText("файл вибраний не вірно");
				controller.howmanyttnnp.setText("0");
				controller.getstatusnp.setDisable(true);
			} else {
				int name = controller.Browsgetstatusnp.toString().indexOf(".txt");
				int name1 = controller.Browsgetstatusnp.toString().lastIndexOf("\\") + 1;
				controller.namefilettnnp.setText(controller.Browsgetstatusnp.toString().substring(name1, name));
				controller.namefilettnnp.setTextFill(Color.GREEN);
//				controller.howmanyttnnp.setText(null);
//				controller.howmanyttnnp.setText(String.valueOf(controller.logic.SummRowforNP()));
				int how = controller.howmanyttnnp.toString().lastIndexOf("'") + 1;
				if (controller.howmanyttnnp.toString().substring(41, how - 1).equals("0")) {
					controller.howmanyttnnp.setTextFill(Color.RED);
					controller.iffileisemptynp.setText("список порожній");
					controller.iffileisemptynp.setTextFill(Color.RED);
					controller.getstatusnp.setDisable(true);
				} else {
					controller.getstatusnp.setDisable(false);
					controller.iffileisemptynp.setText("");
					controller.howmanyttnnp.setTextFill(Color.GREEN);
				}
				List<String> lines = null;
				WhatFileChooseFromNP(lines, controller.Browsgetstatusnp);
			}
		} else {
			controller.namefilettnnp.setText("Файл не вибрано");
			controller.namefilettnnp.setTextFill(Color.RED);
			controller.howmanyttnnp.setText(null);
			controller.iffileisemptynp.setText("");
		}
		controller.initialize();
	}

	public void TESTPANEadvertising() {
		if (controller.singlesms.isSelected() == true) {
			if ((controller.telephonereceiver.getText().toString().length() == 12
					& !controller.messagetext.getText().toString().isEmpty()) != false) {
				controller.sendsmsadvertising.setDisable(false);
			} else {
				controller.sendsmsadvertising.setDisable(true);
			}
		} else if (controller.massmailing.isSelected() == true) {
			if (!controller.messagetext.getText().toString().isEmpty()) {
				if (controller.Browsflefromsmsadvertising != null) {
					controller.sendsmsadvertising.setDisable(false);
				} else {
					controller.sendsmsadvertising.setDisable(true);
				}
			} else {
				controller.sendsmsadvertising.setDisable(true);
			}
		} else {
			controller.sendsmsadvertising.setDisable(true);
		}
	}

}
