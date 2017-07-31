package Logic;

import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import Interface.MainController;

public class Hyper {
	private MainController controller;
	public ERROR error;

	public Hyper(MainController controller) {
		this.controller = controller;
	}

	// гіперсилка на гіт розробників
	public void HyperlinkAutor() {
		String Autor = "https://github.com/GSPGroup";
		try {
			Desktop.getDesktop().browse(new URI(Autor));
		} catch (IOException | URISyntaxException e) {
			MainController.appendUsingFileWriter(
					controller.getNamefoldertosavexls().getText().toString() + "/" + "Звіт1.txt",
					MainController.data() + "Сталась помилка при // гіперсилка на гіт розробників" + "\r\n");
			error.EthernetERROR();
		}
		controller.initialize();
	}

	// гіперсилка поповнення рахунку
	public void HyperLinkMyAtompark() {
		String MyAtompark = "https://myatompark.com/sms/billing/";
		try {
			Desktop.getDesktop().browse(new URI(MyAtompark));
		} catch (IOException | URISyntaxException e) {
			MainController.appendUsingFileWriter(
					controller.getNamefoldertosavexls().getText().toString() + "/" + "Звіт1.txt",
					MainController.data() + "Сталась помилка при // гіперсилка поповнення рахунку" + "\r\n");
			error.EthernetERROR();
		}
		controller.initialize();
	}

	// гіперсилка на сайт нової пошти
	public void HyperLinkNovaPoshta() {
		String NovaPoshta = "https://novaposhta.ua";
		try {
			Desktop.getDesktop().browse(new URI(NovaPoshta));
		} catch (IOException | URISyntaxException e) {
			MainController.appendUsingFileWriter(
					controller.getNamefoldertosavexls().getText().toString() + "/" + "Звіт1.txt",
					MainController.data() + "Сталась помилка при // гіперсилка на сайт нової пошти" + "\r\n");
			error.EthernetERROR();
		}
		controller.initialize();
	}

	// гіперсилка на сайт інтайму
	public void HyperLinkInTime() {
		String InTime = "https://intime.ua/ua";
		try {
			Desktop.getDesktop().browse(new URI(InTime));
		} catch (IOException | URISyntaxException e) {
			MainController.appendUsingFileWriter(
					controller.getNamefoldertosavexls().getText().toString() + "/" + "Звіт1.txt",
					MainController.data() + "Сталась помилка при // гіперсилка на сайт інтайму" + "\r\n");
			error.EthernetERROR();
		}
		controller.initialize();
	}

	public void SMSReport() {
		String InTime = "https://myatompark.com/sms/stat/";
		try {
			Desktop.getDesktop().browse(new URI(InTime));
		} catch (IOException | URISyntaxException e) {
			MainController.appendUsingFileWriter(
					controller.getNamefoldertosavexls().getText().toString() + "/" + "Звіт1.txt",
					MainController.data() + "Сталась помилка при // гіперсилка на сайт інтайму" + "\r\n");
			error.EthernetERROR();
		}
		controller.initialize();
	}
}
