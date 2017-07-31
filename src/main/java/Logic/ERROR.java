package Logic;

import java.util.List;

import Interface.MainController;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class ERROR {

	private MainController controller;

	public ERROR(MainController controller) {
		this.controller = controller;
	}

	// СТАЛАСЯ ПОМИЛКА ПРИ ВІДПРВЦІ
	public void WindowOfERRORtosendSMS() {
		MainController.appendUsingFileWriter(
				controller.getNamefoldertosavexls().getText().toString() + "/" + "Звіт1.txt",
				MainController.data() + "Сталась помилка при // кнопка відправити смс по накладним" + "\r\n");
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				Alert alert = new Alert(AlertType.ERROR);
				alert.setTitle("SMS НЕ ВІДПРАВЛЕНІ");
				alert.setHeaderText("СТАЛАСЯ ПОМИЛКА ПРИ ВІДПРВЦІ");
				alert.showAndWait();
				controller.runindicator.setProgress(1.0);
				controller.getProgeress().setProgress(1.0);
			}
		});
	}

	// звіт по відправленим смс
	public void ReportOfSendSMS(Buttons buttons, List<String> lines, int smssend, int duble) {
		MainController.appendUsingFileWriter(
				buttons.controller.getNamefoldertosavexls().getText().toString() + "/" + "Звіт1.txt",
				"СМС було відправленно: " + smssend + "\r\n");
		final int d = duble;
		final int m = smssend;
		final int l = lines.size();
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				Alert alert = new Alert(AlertType.INFORMATION);
				alert.setTitle("Виконано");
				alert.setHeaderText("SMS відправлено");
				alert.setContentText(" відправило SMS: " + m + "/" + l + "\r\n"
						+ "НЕВІДПРАВИЛО ЧЕРЕЗ ТЕ ЩО БУЛИ ВІДПРАВЛЕНІ РАНІШЕ:" + d);
				alert.showAndWait();
				controller.runindicator.setProgress(1.0);
				controller.getProgeress().setProgress(1.0);
			}
		});
	}

	public void ERROR() {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				Alert alert = new Alert(AlertType.ERROR);
				alert.setTitle("СТАЛАСЯ ПОМИЛКА");
				alert.setHeaderText("СТАЛАСЬ ЯКАСЬ ФІГНЯ, ЧИТАЙ ФАЙЛ ЗВІТ.тхт");
				alert.showAndWait();
				controller.runindicator.setProgress(1.0);
				controller.getProgeress().setProgress(1.0);
			}
		});
	}

	// Проблеми З підключенням До мережі
	public void EthernetERROR() {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				Alert alert = new Alert(AlertType.ERROR);
				alert.setTitle("СТАЛАСЯ ПОМИЛКА");
				alert.setHeaderText("Проблеми З підключенням До мережі");
				alert.showAndWait();
				controller.runindicator.setProgress(1.0);
				controller.getProgeress().setProgress(1.0);
			}
		});
	}

	// КОШтів на рахунку не достатньо для відправки всіх SMS
	void YouDontHaveMoneyToSendAllSMS() {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				Alert alert = new Alert(AlertType.INFORMATION);
				alert.setTitle("ПОПОВНИ РАХУНОК");
				alert.setHeaderText("КОШтів на рахунку не достатньо для відправки всіх SMS");
				alert.showAndWait();
			}
		});
	}

	// СТАЛАСЯ ПОМИЛКА ПРИ ВИКОНАННІ
	public void ErrorWindow( ) {
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

}
