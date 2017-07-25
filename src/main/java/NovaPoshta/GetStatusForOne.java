package NovaPoshta;

import java.io.IOException;
import java.net.URISyntaxException;

import Interface.MainController;
import Logic.ERROR;
import Logic.Logic;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class GetStatusForOne {
	public Logic logic;
	private MainController controller;
	public ERROR error;

	public GetStatusForOne(MainController controller) {
		this.controller = controller;

	}

	// обработчик собитій для отримання методу по відповіді з нової пошти по
	// накладній
	public void WhatGetStatuNpForOneTTn(String forvard, String Status00) {
		try{
		switch (Status00) {
		case "Відправлення отримано": // Відправлення отримано
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
			ifChangeOfAddress(forvard);
			break;
		// Відмова
		case "Відмова від отримання":
		case "Одержувач відмовився від отримання відправлення":
			ifRefusing(forvard);
			break;
		// Прибув у відділення
		case "Прибув у відділення":
			ifArrivedInTtheDivision(forvard);
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
			controller.regusngstatus.setText("дивись на сайті ");
			controller.regusngstatus.setVisible(true);
			break;
		}
		}catch (NullPointerException e){
			MainController.appendUsingFileWriter(
					controller.getNamefoldertosavexls().getText().toString() + "/" + "Звіт1.txt",
					MainController.data()
							+ "Сталась помилка при // отриманні статусу по одній накладній можливо такої наклданої не існує"
							+ "\r\n");
			error.ERROR();
		}
	}

	// Відправлення передано до огляду отримувачу
	private void ifWatching(String forvard) {
		controller.numberttnpost.setText(ParsingResult.GetNumbernTTNNP(forvard));
		controller.ifststusrefusing.setText("Посилка на відділенні");
		controller.statusttn.setVisible(false);
		controller.numberttn.setVisible(true);
		controller.numberttnpost.setVisible(true);
		controller.statuspost.setVisible(false);
		controller.dnivnaposhtipost.setVisible(false);
		controller.dnivnaposhti.setVisible(false);
		controller.ifststusrefusing.setVisible(true);
		controller.regusngstatus.setVisible(false);
		controller.regusngstatus.setText("Отримувач переглядає посилку");
	}

	// Відправлення отримано
	private void ifSendingReceived(String forvard) {
		controller.numberttnpost.setText(ParsingResult.GetNumbernTTNNP(forvard));
		controller.ifststusrefusing.setText("Посилку Забрали");
		controller.statusttn.setVisible(false);
		controller.numberttn.setVisible(true);
		controller.numberttnpost.setVisible(true);
		controller.statuspost.setVisible(false);
		controller.dnivnaposhtipost.setVisible(false);
		controller.dnivnaposhti.setVisible(false);
		controller.ifststusrefusing.setVisible(true);
		controller.regusngstatus.setVisible(false);
	}

	// Відправлення отримано очікуйте смс
	private void ifSendingReceivedSms(String forvard) {
		controller.numberttnpost.setText(ParsingResult.GetNumbernTTNNP(forvard));
		controller.ifststusrefusing.setText("Посилку Забрали " + ParsingResult.GetDataToTaken(forvard));
		controller.statusttn.setVisible(false);
		controller.numberttn.setVisible(true);
		controller.numberttnpost.setVisible(true);
		controller.statuspost.setVisible(false);
		controller.dnivnaposhtipost.setVisible(false);
		controller.dnivnaposhti.setVisible(false);
		controller.ifststusrefusing.setVisible(true);
		controller.regusngstatus.setVisible(true);
		controller.regusngstatus.setText("Очікуйте СМС");
	}

	// Відправлення отримано. Грошовий переказ видано одержувачу
	private void ifSendingReceivedCach(String forvard) {
		controller.numberttnpost.setText(ParsingResult.GetNumbernTTNNP(forvard));
		controller.ifststusrefusing.setText("Посилку Забрали");
		controller.statusttn.setVisible(false);
		controller.numberttn.setVisible(true);
		controller.numberttnpost.setVisible(true);
		controller.statuspost.setVisible(false);
		controller.dnivnaposhtipost.setVisible(false);
		controller.dnivnaposhti.setVisible(false);
		controller.ifststusrefusing.setVisible(true);
		controller.regusngstatus.setVisible(false);
		controller.regusngstatus.setVisible(true);
		controller.regusngstatus.setText("Гроші видано");
	}

	// Відмова
	private void ifRefusing(String forvard) {
		if (ParsingResult.GetLastCreatedOnTheBasisNumber(forvard) == null) {
			controller.numberttnpost.setText(ParsingResult.GetNumbernTTNNP(forvard));
			controller.ifststusrefusing.setText("Повернення не замовлено ");
			controller.regusngstatus.setText("Відмова ");
			controller.statusttn.setVisible(true);
			controller.numberttn.setVisible(true);
			controller.numberttnpost.setVisible(true);
			controller.statuspost.setVisible(true);
			controller.dnivnaposhtipost.setVisible(false);
			controller.dnivnaposhti.setVisible(false);
			controller.ifststusrefusing.setVisible(true);
			controller.regusngstatus.setVisible(false);
			controller.regusngstatus.setVisible(true);
		} else {
			DataModel.setNumber(ParsingResult.GetNumbernTTNNP(forvard));
			String forvard1 = ParsingResult.GetLastCreatedOnTheBasisNumber(forvard);
			String forvard2 = null;
			try {
				forvard2 = NovaPoshta.Xml(forvard1, controller.DefaultPhoneNumberSender.getText().toString());
			} catch (URISyntaxException | IOException e) {
				MainController.appendUsingFileWriter(
						controller.getNamefoldertosavexls().getText().toString() + "/" + "Звіт1.txt",
						MainController.data() + "Сталась помилка при // Відмова" + "\r\n");
				error.ERROR();
			}
			controller.numberttnpost.setText(ParsingResult.GetNumbernTTNNP(forvard));
			controller.ifststusrefusing.setText(ParsingResult.GetStatus(forvard2));
			controller.regusngstatus.setText("Відмова ");
			controller.statuspost.setText("Поверення");
			controller.statuspost.setVisible(true);
			controller.statusttn.setVisible(true);
			controller.numberttn.setVisible(true);
			controller.numberttnpost.setVisible(true);
			controller.dnivnaposhtipost.setVisible(false);
			controller.dnivnaposhti.setVisible(false);
			controller.ifststusrefusing.setVisible(true);
			controller.regusngstatus.setVisible(true);
		}
	}

	// Відправлення прямує
	private void ifGoWay(String forvard) {
		controller.numberttnpost.setText(ParsingResult.GetNumbernTTNNP(forvard));
		controller.statuspost.setText("Прямує на відділення");
		controller.statuspost.setVisible(true);
		controller.statusttn.setVisible(true);
		controller.numberttn.setVisible(true);
		controller.numberttnpost.setVisible(true);
		controller.dnivnaposhtipost.setVisible(false);
		controller.dnivnaposhti.setVisible(false);
		controller.dnivnaposhtipost.setText(null);
		controller.ifststusrefusing.setVisible(false);
		controller.regusngstatus.setVisible(false);
	}

	// Прибув у відділення
	private void ifArrivedInTtheDivision(String forvard) {
		controller.numberttnpost.setText(ParsingResult.GetNumbernTTNNP(forvard));
		controller.ifststusrefusing.setText(ParsingResult.GetStatus(forvard));
		controller.statuspost.setText("на відділенні");
		controller.statuspost.setVisible(true);
		controller.statusttn.setVisible(true);
		controller.numberttn.setVisible(true);
		controller.numberttnpost.setVisible(true);
		controller.dnivnaposhtipost.setVisible(true);
		controller.dnivnaposhti.setVisible(true);
		controller.ifststusrefusing.setVisible(true);
		controller.regusngstatus.setVisible(false);
		if (Integer.valueOf(ParsingResult.RahunokDnivIfNotTaken(forvard, controller)) > 3) {
			controller.dnivnaposhtipost.setTextFill(Color.RED);
			controller.dnivnaposhtipost.setFont(Font.font("Verdana", FontWeight.BOLD, 14));
			controller.dnivnaposhtipost.setText(ParsingResult.RahunokDnivIfNotTaken(forvard, controller));
		} else {
			controller.dnivnaposhtipost.setTextFill(Color.BLACK);
			controller.dnivnaposhtipost.setText(ParsingResult.RahunokDnivIfNotTaken(forvard, controller));
		}
	}

	// Змінено адресу
	private void ifChangeOfAddress(String forvard) {
		String forvard2 = null;
		try {
			forvard2 = NovaPoshta.Xml(controller.npttn.getText().toString(),
					controller.DefaultPhoneNumberSender.getText().toString());
		} catch (URISyntaxException | IOException e) {
			MainController.appendUsingFileWriter(
					controller.getNamefoldertosavexls().getText().toString() + "/" + "Звіт1.txt",
					MainController.data() + "Сталась помилка при // Змінено адресу" + "\r\n");
			error.ERROR();
		}
		String forvard3 = ParsingResult.GetLastCreatedOnTheBasisNumber(forvard2);
		String phone = ParsingResult.GetPhoneNumberRepicientTTNNP(forvard2);
		String forvard5 = null;
		try {
			forvard5 = NovaPoshta.Xml(forvard3, phone);
		} catch (URISyntaxException | IOException e) {
			MainController.appendUsingFileWriter(
					controller.getNamefoldertosavexls().getText().toString() + "/" + "Звіт1.txt",
					MainController.data() + "Сталась помилка при // Змінено адресу" + "\r\n");
			error.ERROR();
		}
		String Status00 = ParsingResult.GetStatusTTNNP(forvard5);
		WhatGetStatuNpForOneTTn(forvard5, Status00);

	}
}
