package sms.api;

import Interface.MainController;
import Logic.Hyper;
import javafx.scene.paint.Color;

public class OperatorPhoneRecipient {
	private MainController controller;

	public OperatorPhoneRecipient(MainController controller) {
		this.controller = controller;
	}

	// метод для перевірки який мобільний оператор бувв ведений
	public void WhatOperatorrecipient(MainController mainController) {
		String textmasage = mainController.messagetext.getText().toString();
		String phonenomber = mainController.telephonereceiver.getText().toString();
		String phone = "000";
		if (!phonenomber.isEmpty()) {
			if (phonenomber.length() > 11 || phonenomber.length() < 15) {
				if (phonenomber.length() < 14 & phonenomber.indexOf("+380") != -1) {
					phone = phonenomber.substring(phonenomber.indexOf("+380") + 3, 6);
				} else if (phonenomber.length() < 13 & phonenomber.indexOf("380") != -1) {
					phone = phonenomber.substring(phonenomber.indexOf("380") + 2, 5);
				} else {
					phone = "999";
				}
			}
			switch (phone) {
			case "050":
			case "095":
			case "099":
				OperatorVodafone(mainController, textmasage);
				break;
			case "063":
			case "073":
			case "093":
				OperatorLifecell(mainController, textmasage);
				break;
			case "067":
			case "068":
			case "096":
			case "097":
			case "098":
				OperatorKyivstar(mainController, textmasage);
				break;
			case "999":
				mainController.phonemasage.setText("НЕВІРИНИЙ ФОРМАТ");
				mainController.phonemasage.setTextFill(Color.RED);
				mainController.sendsmsadvertising.setDisable(true);
				break;
			case "000":
				mainController.phonemasage.setText("вкажіть номер");
				mainController.phonemasage.setTextFill(Color.RED);
				mainController.sendsmsadvertising.setDisable(true);
				break;
			default:
				mainController.phonemasage.setText("НЕВІДОМИЙ оператор");
				mainController.phonemasage.setTextFill(Color.RED);
				mainController.sendsmsadvertising.setDisable(true);
				break;
			}
			mainController.initialize();
		}
		mainController.initialize();
	}

	// якщо мобільний оператор київстар
	private void OperatorKyivstar(MainController mainController, String textmasage) {
		mainController.phonemasage.setText("Київстар");
		mainController.phonemasage.setTextFill(Color.GREEN);
		if (!textmasage.isEmpty()) {
			if (mainController.smart.isSelected() == true || mainController.reno.isSelected() == true
					|| mainController.smartreno.isSelected() == true) {
				if (mainController.singlesms.isSelected() == true) {
					mainController.sendsmsadvertising.setDisable(false);
				}
			}
		}
	}

	// якщо мобільний оператор ВОДАФОН
	private void OperatorVodafone(MainController mainController, String textmasage) {
		mainController.phonemasage.setText("Vodafone");
		mainController.phonemasage.setTextFill(Color.GREEN);
		if (!textmasage.isEmpty()) {
			if (mainController.smart.isSelected() == true || mainController.reno.isSelected() == true
					|| mainController.smartreno.isSelected() == true) {
				if (mainController.singlesms.isSelected() == true) {
					mainController.sendsmsadvertising.setDisable(false);
				}
			}
		}
	}

	// якщо мобільний оператор лайфсел
	private void OperatorLifecell(MainController mainController, String textmasage) {
		mainController.phonemasage.setText("lifecell");
		mainController.phonemasage.setTextFill(Color.GREEN);
		if (!textmasage.isEmpty()) {
			if (mainController.smart.isSelected() == true || mainController.reno.isSelected() == true
					|| mainController.smartreno.isSelected() == true) {
				if (mainController.singlesms.isSelected() == true) {
					mainController.sendsmsadvertising.setDisable(false);
				}
			}
		}
	}
}
