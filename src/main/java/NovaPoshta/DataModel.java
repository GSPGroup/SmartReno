package NovaPoshta;

public class DataModel {

	public static String Number;
	// номер накладної
	public static String PhoneRecipient;
	// номер телефону одержувача
	public static String RecipientFullNameEW;
	// фио одержувача
	public static String RecipientAddress;
	// адреса доставки
	public static String Status;
	// статуc відправки
	public static String DateCreated;
	// дата відправки
	public static String RecipientDateTime;
	// дата коли людина забраа посилку
	public static String LastCreatedOnTheBasisNumber;
	// номер накладної для отримання інформації якщо адресу доставки було
	// змінено
	public static String WarehouseRecipient;
	// номер відділення і адреса отримувача
	public static String CityRecipient;
	// місто отримувача
	public static String ScheduledDeliveryDate;
	// прогнозована дата доставки
	public static String RiznucjaDat;
	// скільки днів посилка знаходиться на відділення з дня ПРГНОЗОВАНОЇ
	// доставки
	public static String StatusSMS;
	 

	public static  String getStatusSMS() {
		return StatusSMS;
	}

	public static  void setStatusSMS(String statusSMS) {
		StatusSMS = statusSMS;
	}

	public String getNumber() {
		return Number;
	}

	public static void setNumber(String number) {
		Number = number;
	}

	public String getPhoneRecipient() {
		return PhoneRecipient;
	}

	public static void setPhoneRecipient(String phoneRecipient) {
		PhoneRecipient = phoneRecipient;
	}

	public String getRecipientFullNameEW() {
		return RecipientFullNameEW;
	}

	public static void setRecipientFullNameEW(String recipientFullNameEW) {
		RecipientFullNameEW = recipientFullNameEW;
	}

	public String getRecipientAddress() {
		return RecipientAddress;
	}

	public static void setRecipientAddress(String recipientAddress) {
		RecipientAddress = recipientAddress;
	}

	public static   String getStatus() {
		return Status;
	}

	public  static void setStatus(String status) {
		Status = status;
	}

	public String getDateCreated() {
		return DateCreated;
	}

	public static void setDateCreated(String dateCreated) {
		DateCreated = dateCreated;
	}

	public String getRecipientDateTime() {
		return RecipientDateTime;
	}

	public static void setRecipientDateTime(String recipientDateTime) {
		RecipientDateTime = recipientDateTime;
	}

	public String getLastCreatedOnTheBasisNumber() {
		return LastCreatedOnTheBasisNumber;
	}

	public static void setLastCreatedOnTheBasisNumber(String lastCreatedOnTheBasisNumber) {
		LastCreatedOnTheBasisNumber = lastCreatedOnTheBasisNumber;
	}

	public String getWarehouseRecipient() {
		return WarehouseRecipient;
	}

	public static void setWarehouseRecipient(String warehouseRecipient) {
		WarehouseRecipient = warehouseRecipient;
	}

	public String getCityRecipient() {
		return CityRecipient;
	}

	public static void setCityRecipient(String cityRecipient) {
		CityRecipient = cityRecipient;
	}

	public String getScheduledDeliveryDate() {
		return ScheduledDeliveryDate;
	}

	public static void setScheduledDeliveryDate(String scheduledDeliveryDate) {
		ScheduledDeliveryDate = scheduledDeliveryDate;
	}

	public String getRiznucjaDat() {
		return RiznucjaDat;
	}

	public static void setRiznucjaDat(String riznucjaDat) {
		RiznucjaDat = riznucjaDat;
	}

}