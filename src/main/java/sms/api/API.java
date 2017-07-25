package sms.api;

import java.util.Map;

import Interface.MainController;
import NovaPoshta.ParsingResult;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class API {
	private static RequestBuilder reqBuilder;
	private String login;
	private String password;

	private MainController controller;

	public API(MainController controller) {
		this.controller = controller;
	}

	public API(RequestBuilder reqBuilder, String login, String password) {
		API.reqBuilder = reqBuilder;
		this.login = login;
		this.password = password;
	}

	// отримати статус по смс
	public static String StatusSms(API ApiSms, String whatsend1, String phonenomber, String textmasage, String TTN) {
		String ApiSmsforvard = ApiSms.sendSms(whatsend1, textmasage, phonenomber, TTN);
		int Status1 = ApiSmsforvard.indexOf("<status>") + 8;
		int Status2 = ApiSmsforvard.indexOf("</status>");
		String Status = ApiSmsforvard.substring(Status1, Status2);
		return Status;
	}

	// отрмати статус по відпраці смс
	public static String GetStatusSendSms(String forvard) {
		String login = "pro100-pr2@ukr.net";
		String password = "pro100pr1";
		String URL = "http://api.atompark.com/members/sms/xml.php";
		RequestBuilder Request = new RequestBuilder(URL);
		API ApiSms = new API(Request, login, password);
		String ApiSmsforvard = ApiSms.getStatus(forvard);
		if (ApiSmsforvard.equals("<?xml version=\"1.0\" encoding=\"UTF-8\"?><deliveryreport></deliveryreport>")) {
			return ApiSmsforvard = "СМС не відправлено";
		} else {
			int Status1 = ApiSmsforvard.indexOf("status=\"") + 8;
			int Status2 = ApiSmsforvard.indexOf("\" amount=\"");

			String Status = ApiSmsforvard.substring(Status1, Status2);
			return Status;
		}
	}

	public String getStatus(String msgId) {
		String request = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
		request = request.concat("<SMS><operations><operation>GETSTATUS</operation></operations>");
		request = request.concat("<authentification>");
		request = request.concat("<username>" + this.login + "</username>");
		request = request.concat("<password>" + this.password + "</password>");
		request = request.concat("</authentification>");
		request = request.concat("<statistics>");
		request = request.concat("<messageid>" + msgId + "</messageid>");
		request = request.concat("</statistics>");
		request = request.concat("</SMS>");
		return reqBuilder.doXMLQuery(request);
	}

	public String getPrice(String text, Map<String, String> phones) {
		String request = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
		request = request.concat("<SMS>");
		request = request.concat("<operations>  ");
		request = request.concat("<operation>GETPRICE</operation>");
		request = request.concat("</operations> ");
		request = request.concat("<authentification>");
		request = request.concat("<username>" + this.login + "</username>");
		request = request.concat("<password>" + this.password + "</password>");
		request = request.concat("</authentification>");
		request = request.concat("<message>");
		request = request.concat("<sender>SMS</sender>");
		request = request.concat("<text>" + text + "</text>");
		request = request.concat("</message>");
		request = request.concat("<numbers>");
		for (Map.Entry entry : phones.entrySet()) {
			request = request.concat("<number messageID=\"" + entry.getKey() + "\">" + entry.getValue() + "</number>");
		}
		request = request.concat("</numbers>");
		request = request.concat("</SMS>");
		return reqBuilder.doXMLQuery(request);
	}

	public static String getBalance(RequestBuilder url, String login, String password) {
		String request = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
		request = request.concat("<SMS>");
		request = request.concat("<operations>");
		request = request.concat("<operation>BALANCE</operation>");
		request = request.concat("</operations>");
		request = request.concat("<authentification>");
		request = request.concat("<username>" + login + "</username>");
		request = request.concat("<password>" + password + "</password>");
		request = request.concat("</authentification> ");
		request = request.concat("</SMS>");
		return reqBuilder.doXMLQuery(request);
	}

	// метод для відправки смс
	public String sendSms(String sender, String text, String phone, String TTN) {
		String request = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
		request = request.concat("<SMS>");
		request = request.concat("<operations>");
		request = request.concat("<operation>SEND</operation>");
		request = request.concat("</operations>");
		request = request.concat("<authentification>");
		request = request.concat("<username>" + this.login + "</username>");
		request = request.concat("<password>" + this.password + "</password>");
		request = request.concat("</authentification>");
		request = request.concat("<message>");
		request = request.concat("<sender>" + sender + "</sender>");
		request = request.concat("<text>" + text + "</text>");
		request = request.concat("</message>");
		request = request.concat("<numbers>");
		request = request.concat("<number");
		request = request.concat(" messageID=\"" + TTN + "\"");
		request = request.concat(">");
		request = request.concat(phone);
		request = request.concat("</number>");
		request = request.concat("</numbers>");
		request = request.concat("</SMS>");
		return reqBuilder.doXMLQuery(request);
	}

	// метод для отрмання скільки на рахунку.
	public String Chekrahunky(MainController mainController) {
		String URL = "http://atompark.com/members/sms/xml.php";
		String login = "pro100-pr2@ukr.net";
		String password = "pro100pr1";
		RequestBuilder Request = new RequestBuilder(URL);
		API ApiSms = new API(Request, login, password);
		String Balance = sms.api.API.getBalance(Request, login, password);
		int Balance1 = Balance.indexOf("<amount>") + 8;
		int Balance2 = Balance.indexOf("</amount>");
		mainController.howmanymoney1.setText(Balance.substring(Balance1, Balance2));
		mainController.howmanymoney.setText(Balance.substring(Balance1, Balance2));
		if (Double.valueOf(Balance.substring(Balance1, Balance2)) < 5.0) {
			Platform.runLater(new Runnable() {
				@Override
				public void run() {
					Alert alert = new Alert(AlertType.INFORMATION);
					alert.setTitle("ПОПОВНИ РАХУНОК");
					alert.setHeaderText("Коштів можливо буде не достатьньо для наступної операції");
					alert.showAndWait();
				}
			});
		}
		return Balance = Balance.substring(Balance1, Balance2);
	}

}
