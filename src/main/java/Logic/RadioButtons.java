package Logic;

import java.io.File;
import java.util.List;

import Interface.MainController;
import NovaPoshta.NovaPoshta;
import javafx.scene.paint.Color;

public class RadioButtons {
	private MainController controller;
	private NovaPoshta novaposhta;
	public ERROR error;

	public RadioButtons(MainController controller) {
		this.controller = controller;
	}

	// рідіобатон одиноча відправка рекламних смс
	public void RadioButtonSingleSms() {
		controller.namefilesmsadvertising.setText("Одиночна відправка СМС");
		controller.browsfilefromsmsadvertising.setDisable(true);
		controller.messagetext.setDisable(false);
		controller.telephonereceiver.setDisable(false);
		double rah = Double.parseDouble(controller.api.Chekrahunky(controller));
		double symasms = 0;
		symasms = (double) controller.logic.SummRow() * 0.26;
		if (controller.telephonereceiver.getText().toString().length() == 12
				& controller.messagetext.getText().toString().isEmpty()) {
			controller.smart.setDisable(true);
			controller.reno.setDisable(true);
			controller.smartreno.setDisable(true);
		} else {
			WhatSummSMS(rah, symasms);
		}
		controller.initialize();
	}

	// яка сука смс чи достатня для відправки смс
	private void WhatSummSMS(double rah, double symasms) {
		if (symasms > rah) {
			controller.whethersufficientfundsfromadvertising.setText("Недостатньокоштів на рахунку ");
			error.YouDontHaveMoneyToSendAllSMS();
			controller.whethersufficientfundsfromadvertising.setTextFill(Color.RED);
			controller.smart.setDisable(true);
			controller.reno.setDisable(true);
			controller.smartreno.setDisable(true);
			controller.smart.setSelected(true);
			controller.reno.setSelected(true);
			controller.smartreno.setSelected(true);
		} else {
			controller.whethersufficientfundsfromadvertising.setText("коштів достатньо для відправки СМС");
			controller.whethersufficientfundsfromadvertising.setTextFill(Color.GREEN);
			controller.smart.setDisable(false);
			controller.reno.setDisable(false);
			controller.smartreno.setDisable(false);
			controller.smart.setSelected(false);
			controller.reno.setSelected(false);
			controller.smartreno.setSelected(false);
		}
	}

	// рідіобатон масова відправка рекламних смс
	public void RadioButtonMassMailing() {
		controller.browsfilefromsmsadvertising.setDisable(false);
		controller.messagetext.setDisable(false);
		controller.telephonereceiver.setDisable(true);
		double rah = Double.parseDouble(controller.api.Chekrahunky(controller));
		double symasms = 0;
		symasms = (double) controller.logic.SummRow() * 0.26;
		WhatSummSMSForMassMiling(rah, symasms);
		controller.initialize();
	}

	// сума смс для масової відправки смс
	private void WhatSummSMSForMassMiling(double rah, double symasms) {
		if (symasms > rah) {
			controller.whethersufficientfundsfromadvertising.setText("Недостатньокоштів на рахунку ");
			error.YouDontHaveMoneyToSendAllSMS();
			controller.whethersufficientfundsfromadvertising.setTextFill(Color.RED);
			controller.smart.setDisable(false);
			controller.reno.setDisable(false);
			controller.smart.setSelected(true);
			controller.reno.setSelected(true);
			controller.smartreno.setSelected(false);
		} else {
			controller.whethersufficientfundsfromadvertising.setText("коштів достатньо для відправки СМС");
			controller.whethersufficientfundsfromadvertising.setTextFill(Color.GREEN);
			controller.smart.setDisable(true);
			controller.reno.setDisable(true);
			controller.smart.setSelected(false);
			controller.reno.setSelected(false);
			controller.smartreno.setSelected(true);
		}
	}

	// рідіобатон для поодинокиої інформації нова пошта
	public void RadioButtonForOneNP() {
		controller.namefilettnnp.setText(null);
		controller.howmanyttnnp.setText(null);
		controller.browsgetstatusnp.setDisable(true);
		controller.npttn.setDisable(false);
		controller.reloadttn.setDisable(false);
		controller.clearTTN.setDisable(false);
		controller.status.setText("для однієї накладної");
		IsTheCorrectFormatNumberTTN();
		controller.initialize();
	}

	// перевірка чи правильний формат номера експрес наклданої
	private void IsTheCorrectFormatNumberTTN() {
		if (controller.npttn.getText().toString().length() == 14) {
			controller.getstatusnp.setDisable(false);
			controller.iffileisemptynp.setText("");
		} else {
			controller.iffileisemptynp.setVisible(true);
			controller.iffileisemptynp.setText("невірний формат");
			controller.getstatusnp.setDisable(true);
		}
	}

	// рідіобатон для списку нової пошти
	public void RadioButtonManyNP() {
		controller.browsgetstatusnp.setDisable(true);
		controller.npttn.setDisable(true);
		controller.status.setText("для СТАНДРАТНОГО списку накладних");
		controller.npttn.setText("");
		List<String> list = null;
		String fileName = controller.getNamefoldertosavexls().getText().toString() + "/накладні.txt";
		if ((new File(fileName)).exists()) {
			controller.namefilettnnp.setText("вибраний файл за замовчуванням");
			controller.namefilettnnp.setTextFill(Color.GREEN);
			list = controller.ReadFileForTTN(fileName, list);
			controller.howmanyttnnp.setText(String.valueOf(list.size()));
			controller.getstatusnp.setDisable(false);
			IfFileforGetManyStatusNP(list);
		} else {
			controller.howmanyttnnp.setText(null);
			controller.manynp.setDisable(true);
			controller.newmanynp.setSelected(true);
			controller.status.setText("для НОВОГО списку накладних");
			controller.browsgetstatusnp.setDisable(false);

		}
		WhatRadioButtonSelected();
		controller.initialize();
	}

	// перевірка чи вибрайниф файл не є порожнім для отримання статусу по
	// накладних
	private void IfFileforGetManyStatusNP(List<String> list) {
		if (list.size() != 0) {
			controller.getstatusnp.setDisable(false);
		} else {
			controller.getstatusnp.setDisable(false);
		}
	}

	// радіобатон для НОВОГО СПИСКУ наклдадних нової пошти
	public void RadioButtonNewManyNP() {
		WhatRadioButtonSelected();
		controller.namefilettnnp.setText("виберіть файл з списком накладних");
		controller.namefilettnnp.setTextFill(Color.RED);
		controller.howmanyttnnp.setText(null);
		controller.iffileisemptynp.setText("");
		controller.browsgetstatusnp.setDisable(false);
		controller.getstatusnp.setDisable(true);
		controller.npttn.setDisable(true);
		controller.status.setText("для НОВОГО списку накладних");
		controller.npttn.setText(" ");
		controller.initialize();

	}

	// рідіобатон імя відправника Smartio
	public void RadioButtonSmartio() {
		NovaPoshta.WhatNameSender = "Smartio";
		controller.SendSMS.setDisable(false);
		controller.status.setText("Smartio");
		controller.initialize();
	}

	// рідіобатон імя відправника Renomax
	public void RadioButtonRenomax() {
		NovaPoshta.WhatNameSender = "Renomax";
		controller.SendSMS.setDisable(false);
		controller.status.setText("Renomax");
		controller.initialize();
	}

	// рідіобатон імя відправника Reno
	public void RadioButonReno() {
		if (controller.reno.isSelected() == true) {
			if (controller.singlesms.isSelected() == true) {
				if (controller.messagetext.getText().toString().length() > 0
						&& controller.telephonereceiver.getText().toString().length() > 0) {
					controller.sendsmsadvertising.setDisable(false);
				}
			}
		}
		controller.status.setText("Renomax");
		controller.initialize();
	}

	// рідіобатон імя відправника Smart
	public void RadioButtonSmart() {
		if (controller.smart.isSelected() == true) {
			if (controller.singlesms.isSelected() == true) {
				if (!controller.messagetext.getText().toString().isEmpty()
						&& controller.telephonereceiver.getText().toString().length() == 12) {
					controller.sendsmsadvertising.setDisable(false);
				}
			}
		}
		controller.status.setText("Smartio");
		controller.initialize();
	}

	// рідіобатон імя відправника SmartReno
	public void RadioButtonSmartReno() {
		if (controller.smartreno.isSelected() == true) {
			if (controller.singlesms.isSelected() == true) {
				if (controller.messagetext.getText().toString().length() > 0
						&& controller.telephonereceiver.getText().toString().length() > 0) {
					controller.sendsmsadvertising.setDisable(false);
				}
			}
		}
		controller.status.setText("SmartReno");
		controller.initialize();
	}

	// рідіобатон імя відправника SmartReno
	public void WhatRadioButtonSelected() {
		if (controller.manynp.isSelected() == true || controller.newmanynp.isSelected() == true) {
			controller.reloadttn.setDisable(true);
			controller.clearTTN.setDisable(true);
			controller.dnivnaposhti.setVisible(false);
			controller.numberttn.setVisible(false);
			controller.statusttn.setVisible(false);
			controller.ifststusrefusing.setVisible(false);
			controller.dnivnaposhtipost.setVisible(false);
			controller.statuspost.setVisible(false);
			controller.numberttnpost.setVisible(false);
			controller.regusngstatus.setVisible(false);
			controller.line1.setVisible(false);
			controller.line2.setVisible(false);
			controller.line3.setVisible(false);
			controller.line4.setVisible(false);
			controller.line5.setVisible(false);
			controller.line6.setVisible(false);
			controller.line7.setVisible(false);
			controller.line8.setVisible(false);
			controller.line9.setVisible(false);
		} else if (controller.foronenp.isSelected() == true) {
			controller.line1.setVisible(true);
			controller.line2.setVisible(true);
			controller.line3.setVisible(true);
			controller.line4.setVisible(true);
			controller.line5.setVisible(true);
			controller.line6.setVisible(true);
			controller.line7.setVisible(true);
			controller.line8.setVisible(true);
			controller.line9.setVisible(true);
		}
	}
}
