package NovaPoshta;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import Interface.MainController;
import Logic.Logic;

public class ParsingResult {
	private MainController controller;
	public Logic logic;

	public ParsingResult(MainController controller) {
		this.controller = controller;

	}

	// статуси які обробляються
	public static String SendingReceived = "Відправлення отримано";
	public static String SendingReceivedCach = "Відправлення отримано. Грошовий переказ видано одержувачу.";
	public static String SendingReceivedSms = "Відправлення отримано. Протягом доби відправник отримає SMS-повідомлення про надходження грошового переказу та зможете отримати його в касі відділення «Нова пошта».";
	public static String ChangeOfAddress = "Змінено адресу";
	public static String Refusing = "Відмова від отримання";
	public static String ArrivedInTtheDivision = "Прибув у відділення";
	public static String IfRefusingStatus = " Одержувач відмовився від отримання відправлення";
	public static String GoBack = "м. Зимна Вода Відділення №1: вул. Тичини, 2а";
	public static String GoWay = "Відправлення прямує";
	public static String Watching = "Відправлення передано до огляду отримувачу";

	// отримати статус накладної
	public static String GetStatusTTNNP(String forvard) {
		int Status1 = forvard.indexOf("<Status>") + 8;
		int Status2 = forvard.indexOf("</Status>");
		String forvard1 = forvard.substring(Status1, Status2);
		if (forvard1.length() > 14) {
			if (forvard1.substring(0, 19).equals(GoWay)) {
				return forvard1 = GoWay;
			}
		}
		return forvard1;
	}

	// отримати статус накладної
	public static String GetStatus(String forvard) {
		int Status1 = forvard.indexOf("<Status>") + 8;
		int Status2 = forvard.indexOf("</Status>");
		String forvard1 = forvard.substring(Status1, Status2);
		return forvard1;
	}

	// витягує повну адресу доставки
	public static String fullRecipientAddress(String forvard) {
		if (GetWarehouseRecipient(forvard) == null) {
			return GetCityRecipient(forvard);
		} else {
			return GetCityRecipient(forvard) + " " + GetWarehouseRecipient(forvard);
		}
	}

	// отрмати адресу доставки
	public static String GetWarehouseRecipientTTNNP(String forvard) {
		int WarehouseRecipient1 = forvard.indexOf("<WarehouseRecipient>") + 20;
		int WarehouseRecipient2 = forvard.indexOf("</WarehouseRecipient>");
		forvard = forvard.substring(WarehouseRecipient1, WarehouseRecipient2);
		return forvard;
	}

	// отримати прогнозовану дату доставки
	public static String GetscheduleddeliverydateTTNNP(String forvard) {
		int scheduleddeliverydate1 = forvard.indexOf("<ScheduledDeliveryDate>") + 23;
		int scheduleddeliverydate2 = forvard.indexOf("</ScheduledDeliveryDate>");
		forvard = forvard.substring(scheduleddeliverydate1, scheduleddeliverydate2);
		return forvard;
	}

	// отримати внутрішній номер накладної при наявності
	public static String GetLastCreatedOnTheBasisNumber(String forvard) {
		int Refusing1 = forvard.indexOf("<LastCreatedOnTheBasisNumber>") + 29;
		int Refusing2 = forvard.indexOf("</LastCreatedOnTheBasisNumber>");
		forvard = forvard.substring(Refusing1, Refusing2);
		return forvard;
	}

	// отримати номер накладної при зміні
	public static String GetNumbernTTNNP(String forvard) {
		int Number1 = forvard.indexOf("<Number>") + 8;
		int Number2 = forvard.indexOf("</Number>");
		forvard = forvard.substring(Number1, Number2);
		return forvard;
	}

	// дата відправки
	public static String GetDateCreated(String forvard) {
		int DateCreated1 = forvard.indexOf("<DateCreated>") + 13;
		int DateCreated2 = forvard.indexOf("</DateCreated>");
		forvard = forvard.substring(DateCreated1, DateCreated2);
		return forvard;
	}

	// дата коли забрали
	public static String GetDataToTaken(String forvard) {
		int RecipientDateTime1 = forvard.indexOf("<RecipientDateTime>") + 19;
		int RecipientDateTime2 = forvard.indexOf("</RecipientDateTime>");
		String forvard0 = forvard.substring(RecipientDateTime1, RecipientDateTime2);
		if (forvard0.length() == 0) {
			forvard0 = null;
		}
		return forvard0;
	}

	// ФИО одержувача
	public static String GetRecipientFullNameEW(String forvard) {
		int RecipientFullNameEW1 = forvard.indexOf("<RecipientFullNameEW>") + 21;
		int RecipientFullNameEW2 = forvard.indexOf("</RecipientFullNameEW>");
		String forvard1 = null;
		if (RecipientFullNameEW2 == -1) {
			int SenderFullNameEW1 = forvard.indexOf("<SenderFullNameEW>") + 18;
			int SenderFullNameEW2 = forvard.indexOf("</SenderFullNameEW>");
			forvard1 = forvard.substring(SenderFullNameEW1, SenderFullNameEW2);
		} else {
			forvard1 = forvard.substring(RecipientFullNameEW1, RecipientFullNameEW2);
		}
		return forvard1;
	}

	// номер телефону одержувача
	public static String GetPhoneRecipient(String forvard) {
		int PhoneRecipient1 = forvard.indexOf("<PhoneRecipient>") + 16;
		int PhoneRecipient2 = forvard.indexOf("</PhoneRecipient>");
		forvard = forvard.substring(PhoneRecipient1, PhoneRecipient2);
		return forvard;
	}

	// адресу доставки відділення
	public static String GetWarehouseRecipient(String forvard) {
		int WarehouseRecipient1 = forvard.indexOf("<WarehouseRecipient>") + 20;
		int WarehouseRecipient2 = forvard.indexOf("</WarehouseRecipient>");
		forvard = forvard.substring(WarehouseRecipient1, WarehouseRecipient2);
		return forvard;
	}

	// місто доставки
	public static String GetCityRecipient(String forvard) {
		int CityRecipient1 = forvard.indexOf("<CityRecipient>") + 15;
		int CityRecipient2 = forvard.indexOf("</CityRecipient>");
		forvard = "м. " + forvard.substring(CityRecipient1, CityRecipient2);
		return forvard;
	}

	// номер одержувача
	public static String GetRecipientAddress(String forvard) {
		int RecipientAddress1 = forvard.indexOf("<RecipientAddress>") + 21;
		int RecipientAddress2 = forvard.indexOf("</RecipientAddress>");
		forvard = forvard.substring(RecipientAddress1, RecipientAddress2);
		return forvard;
	}

	// отримати номер накладної при зміні
	public static String GetPhoneNumberRepicientTTNNP(String forvard) {
		int PhoneNumber1 = forvard.indexOf("<PhoneRecipient>") + 16;
		int PhoneNumber2 = forvard.indexOf("</PhoneRecipient>");
		String forvard1 = null;
		if (PhoneNumber2 - PhoneNumber1 != 12) {
			int PhoneNumber3 = forvard.indexOf("<PhoneSender>") + 13;
			int PhoneNumber4 = forvard.indexOf("</PhoneSender>");
			forvard1 = forvard.substring(PhoneNumber3, PhoneNumber4);
		} else {
			forvard1 = forvard.substring(PhoneNumber1, PhoneNumber2);
		}
		return forvard1;
	}

	// рахунок днів з дня прогнозованої доставик
	public static String RahunokDnivIfNotTaken(String forvard, MainController controller) {
		int ScheduledDeliveryDate1 = forvard.indexOf("<ScheduledDeliveryDate>") + 23;
		int ScheduledDeliveryDate2 = forvard.indexOf("</ScheduledDeliveryDate>");
		Date currentDate = new Date();
		SimpleDateFormat dateFormat = null;
		dateFormat = new SimpleDateFormat("MMMM");
		Calendar c = Calendar.getInstance();
		int year = c.get(Calendar.YEAR);
		int month = c.get(Calendar.MONTH) + 1;
		int day = c.get(Calendar.DAY_OF_MONTH);
		String Data = day + "." + month + "." + year;
		String d2 = Data;
		String d1 = forvard.substring(ScheduledDeliveryDate1, ScheduledDeliveryDate2);
		SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy");
		Date date1 = null;
		Date date2 = null;
		try {
			date1 = format.parse(d1.replaceAll("-", "."));
		} catch (ParseException e) {
			MainController.appendUsingFileWriter(
					controller.getNamefoldertosavexls().getText().toString() + "/" + "Звіт1.txt",
					MainController.data() + "Сталась помилка при  рахунок днів з дня прогнозованої доставик" + "\r\n");
		}
		try {
			date2 = format.parse(d2);
		} catch (ParseException e) {
			MainController.appendUsingFileWriter(
					controller.getNamefoldertosavexls().getText().toString() + "/" + "Звіт1.txt",
					MainController.data() + "Сталась помилка при  рахунок днів з дня прогнозованої доставик" + "\r\n");
		}
		long difference = date2.getTime() - date1.getTime();
		long days = difference / (24 * 60 * 60 * 1000);
		if (days < 0) {
			days = 0;
			return String.valueOf(Math.abs(days));
		} else if (days == 0) {
			days = 1;
			return String.valueOf(Math.abs(days));
		}
		return String.valueOf(Math.abs(days));
	}

	public static void GoNull() {
		DataModel.Number = null;// номер накладної
		DataModel.PhoneRecipient = null;// номер телефону одержувача
		DataModel.RecipientFullNameEW = null;// фио одержувача
		DataModel.RecipientAddress = null;// адреса доставки
		DataModel.Status = null;// статуc відправки
		DataModel.DateCreated = null;// дата відправки
		DataModel.RecipientDateTime = null;// дата коли людина забраа посилку
		DataModel.LastCreatedOnTheBasisNumber = null;// номер накладної для
														// отримання інформації
														// якщо адресу доставки
														// було змінено
		DataModel.WarehouseRecipient = null;// номер відділення і адреса
											// отримувача
		DataModel.CityRecipient = null;// місто отримувача
		DataModel.ScheduledDeliveryDate = null;// прогнозована дата доставки
		DataModel.RiznucjaDat = null;// скільки днів посилка знаходиться на відділення з дня ПРГНОЗОВАНОЇ доставки
		DataModel.StatusSMS=null;	 //статус смс
	}
									 

}
