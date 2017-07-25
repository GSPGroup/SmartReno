package NovaPoshta;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

public class NovaPoshta {

	// конектитись до бази нової пошти і дістати інформацію по накладній
	static String URIBuilder(String XML, HttpClient httpclient)
			throws URISyntaxException, UnsupportedEncodingException, IOException, ClientProtocolException {
		URIBuilder builder = new URIBuilder("http://testapi.novaposhta.ua/v2.0/en/documentsTracking/xml/");
		URI uri = builder.build();
		HttpPost request = new HttpPost(uri);
		request.setHeader("Content-Type", "text/xml");
		StringEntity reqEntity = new StringEntity(XML);
		request.setEntity(reqEntity);
		HttpResponse response = httpclient.execute(request);
		HttpEntity entity = response.getEntity();
		String text = EntityUtils.toString(entity, "UTF-8");
		return text;
	}

	// XML запит для нової пошти
	public static String Xml(String TTN,String Phone)
			throws UnsupportedEncodingException, ClientProtocolException, URISyntaxException, IOException {
		String request = "<root>";
		request = request.concat("<apiKey>48025e386e7bbcf99e2bd5d45f0a6adf</apiKey>");
		request = request.concat("<calledMethod>getStatusDocuments</calledMethod>");
		request = request.concat("<methodProperties>");
		request = request.concat("<Documents>");
		request = request.concat("<item>");
		request = request.concat("<DocumentNumber>" + TTN + "</DocumentNumber>");
		request = request.concat("<Phone>"+Phone+"</Phone>");
		request = request.concat("</item>");
		request = request.concat("</Documents>");
		request = request.concat("</methodProperties>");
		request = request.concat("<modelName>TrackingDocument</modelName>");
		request = request.concat("</root>");
		return URIBuilder(request, HttpClients.createDefault());
	}

	public static String WhatNameSender;
	public static String SendingReceived = "Відправлення отримано";
	public static String SendingReceivedCach = "Відправлення отримано. Грошовий переказ видано одержувачу.";
	public static String SendingReceivedSms = "Відправлення отримано. Протягом доби відправник отримає SMS-повідомлення про надходження грошового переказу та зможете отримати його в касі відділення «Нова пошта».";
	public static String ChangeOfAddress = "Змінено адресу";
	public static String Refusing = "Відмова від отримання";
	public static String ArrivedInTtheDivision = "Прибув у відділення";
	public static String IfRefusingStatus = "Одержувач відмовився від отримання відправлення";
	public static String GoBack = "Відділення №1: вул. Тичини, 2а";
	public static  String xmlanswer = "{ " + "\"" + "statusCode" + "\"" + ":";

}
