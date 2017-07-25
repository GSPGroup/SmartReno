package sms.api;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import Interface.MainController;
import Logic.ERROR;
import Logic.Logic;

public class RequestBuilder {

	private MainController controller;
	public ERROR error;

	public RequestBuilder(MainController controller) {
		this.controller = controller;
	}

	String URL;

	public RequestBuilder(String URL) {
		this.URL = URL;
	}

	public String doXMLQuery(String xml) {
		StringBuilder responseString = new StringBuilder();

		Map<String, String> params = new HashMap<String, String>();
		params.put("XML", xml);
		try {
			Connector.sendPostRequest(this.URL, params);
			String[] response = Connector.readMultipleLinesRespone();
			for (String line : response) {
				responseString.append(line);
			}
		} catch (IOException ex) {
			MainController.appendUsingFileWriter(
					controller.getNamefoldertosavexls().getText().toString() + "/" + "Звіт1.txt",
					MainController.data() + "Сталась помилка при конекті до сервера з відправки смс" + "\r\n");
			error.ERROR();
		}
		Connector.disconnect();
		return responseString.toString();
	}
}
