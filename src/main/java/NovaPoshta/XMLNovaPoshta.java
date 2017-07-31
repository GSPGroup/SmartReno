package NovaPoshta;

public class XMLNovaPoshta {

	private String succes;
	private String status;
	private String recipientaddress;
	private String warehouserecipient;
	private String scheduleddeliverydate;
	private String lastcreatedonthebasisnumber;
	private String number;
	private String datecreated;
	private String recipientdatetime;
	private String recipientfullname;
	private String phonerecipient;
	private String cityrecipient;
	private String phonenumberrepicient;
	private String howdaysinpost;

	public XMLNovaPoshta(String succes, String status, String recipientaddress, String warehouserecipient,
			String scheduleddeliverydate, String lastcreatedonthebasisnumber, String number, String datecreated,
			String recipientdatetime, String recipientfullname, String phonerecipient, String cityrecipient,
			String phonenumberrepicient, String howdaysinpost) {
		super();
		this.succes = succes;
		this.status = status;
		this.recipientaddress = recipientaddress;
		this.warehouserecipient = warehouserecipient;
		this.scheduleddeliverydate = scheduleddeliverydate;
		this.lastcreatedonthebasisnumber = lastcreatedonthebasisnumber;
		this.number = number;
		this.datecreated = datecreated;
		this.recipientdatetime = recipientdatetime;
		this.recipientfullname = recipientfullname;
		this.phonerecipient = phonerecipient;
		this.cityrecipient = cityrecipient;
		this.phonenumberrepicient = phonenumberrepicient;
		this.howdaysinpost = howdaysinpost;
	}

	public String getSucces() {
		return succes;
	}

	public void setSucces(String succes) {
		this.succes = succes;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getRecipientaddress() {
		return recipientaddress;
	}

	public void setRecipientaddress(String recipientaddress) {
		this.recipientaddress = recipientaddress;
	}

	public String getWarehouserecipient() {
		return warehouserecipient;
	}

	public void setWarehouserecipient(String warehouserecipient) {
		this.warehouserecipient = warehouserecipient;
	}

	public String getScheduleddeliverydate() {
		return scheduleddeliverydate;
	}

	public void setScheduleddeliverydate(String scheduleddeliverydate) {
		this.scheduleddeliverydate = scheduleddeliverydate;
	}

	public String getLastcreatedonthebasisnumber() {
		return lastcreatedonthebasisnumber;
	}

	public void setLastcreatedonthebasisnumber(String lastcreatedonthebasisnumber) {
		this.lastcreatedonthebasisnumber = lastcreatedonthebasisnumber;
	}

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public String getDatecreated() {
		return datecreated;
	}

	public void setDatecreated(String datecreated) {
		this.datecreated = datecreated;
	}

	public String getRecipientdatetime() {
		return recipientdatetime;
	}

	public void setRecipientdatetime(String recipientdatetime) {
		this.recipientdatetime = recipientdatetime;
	}

	public String getRecipientfullname() {
		return recipientfullname;
	}

	public void setRecipientfullname(String recipientfullname) {
		this.recipientfullname = recipientfullname;
	}

	public String getPhonerecipient() {
		return phonerecipient;
	}

	public void setPhonerecipient(String phonerecipient) {
		this.phonerecipient = phonerecipient;
	}

	public String getCityrecipient() {
		return cityrecipient;
	}

	public void setCityrecipient(String cityrecipient) {
		this.cityrecipient = cityrecipient;
	}

	public String getPhonenumberrepicient() {
		return phonenumberrepicient;
	}

	public void setPhonenumberrepicient(String phonenumberrepicient) {
		this.phonenumberrepicient = phonenumberrepicient;
	}

	public String getHowdaysinpost() {
		return howdaysinpost;
	}

	public void setHowdaysinpost(String howdaysinpost) {
		this.howdaysinpost = howdaysinpost;
	}

}
