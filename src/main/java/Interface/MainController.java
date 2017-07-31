package Interface;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import Logic.Buttons;
import Logic.ERROR;
import Logic.Hyper;
import Logic.Logic;
import Logic.RadioButtons;
import Logic.ThreadsSendSMS;
import NovaPoshta.GetStatusForList;
import NovaPoshta.GetStatusForOne;
import NovaPoshta.ParsingResult;
import NovaPoshta.СreateExcel;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Tab;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Line;
import sms.api.API;
import sms.api.OperatorPhoneRecipient;

public class MainController {

	public Logic logic;
	public Hyper hyperlink;
	public OperatorPhoneRecipient phonerecipient;
	public RadioButtons radiobutton;
	public Buttons buttons;
	public GetStatusForOne getstatusforone;
	public API api;
	public ThreadsSendSMS threads;
	public GetStatusForList getstatusforlist;
	public СreateExcel createexcel;
	public ERROR error;
	private ParsingResult parsinresult;

	public MainController() {
		this.parsinresult = new ParsingResult(this);
		this.hyperlink = new Hyper(this);
		this.logic = new Logic(this);
		this.phonerecipient = new OperatorPhoneRecipient(this);
		this.radiobutton = new RadioButtons(this);
		this.buttons = new Buttons(this);
		this.getstatusforone = new GetStatusForOne(this);
		this.api = new API(this);
		this.threads = new ThreadsSendSMS(this);
		this.getstatusforlist = new GetStatusForList(this);
		this.createexcel = new СreateExcel(this);
	}

	//////////////////
	@FXML
	public Line line1, line2, line3, line4, line5, line6, line7, line8, line9;

	// вкладка реклами
	// // вкладка нової пошти
	// // вкладка інтайму
	// // вкладка відправки смс по накладних
	@FXML
	public Tab advertising, np, it, senderblok;
	///////////////////////////////////////////////////////
	// Виберіть папку по замовчуванюю для вибору файлу з накладними
	// // Виберіть Пакпку для Збереження ексель файлів з звітом по накладним
	// // кнопака на вспилваючому вікні
	// // отрмати звіт по накладним нової пошти
	// // відкрити файл з накладними нової пошти
	// // отрмати звіт по накладним інтайму
	// // відкрити файл з накладними інтайму
	// // відправити смс звіт по накладним
	// // відкрити файл з накладними для звіту
	// // перевірити стан рахунку
	// // відкрити файл з номерами телефонами для відправки рекламних смс
	// // відправити рекламіні смс
	// // кнопка підтвердження відправки
	// // кнопка скасукати відправлення
	// // очитстити поле для накладної нової пошти
	@FXML
	public Button BrowsDefaultPacageToFindfileTTNNP, BrowsFolderToSaveXLSNP, accespaneYES, getstatusnp,
			browsgetstatusnp, getstatusit, browsgetstatusit, SendSMS, browsfilefromsms, gethowmanymoney,
			browsfilefromsmsadvertising, sendsmsadvertising, yes, no, clearnp, cleartextandphone,
			reloadmessagetextandtelephonereceiver, browsfoldertosavexls, reloadttn, clearTTN, cancel;
	///////////////////////////////////////////////////////
	// для списку нової пошти
	// // для однієї нової пошти
	// // для однієї інтайм
	// // для списку інтайм
	// // хто відправник smartio смс
	// // хто відпраник renomax смс
	// // хто відпраник smartreno смс
	// // одиночна відправка смс
	// // масова розсилка смс
	// // масова розсилка смс
	// // масова розсилка смс
	@FXML
	public RadioButton manynp, foronenp, foroneit, manyit, smartio, renomax, smartreno, singlesms, massmailing, smart,
			reno, newmanynp;
	///////////////////////////////////////////////////////
	// шлях до дефолтної папки для вибору файлу з номерами накладних нової пошти
	// // скільки смс
	// // якшо помилка формати нуомера
	// // якшо текст повідомлення не введений.
	// // текст повідомлення на вспливаючому вікні
	// // якщо було змінено адресу доставки
	// // поле зі звітом по накладній для нової пошти
	// // шлях папки куда будуть зберігатись ексель файли з звітом по накладних
	// // повіддомлення на вспиливаючому вікні
	// // поле з написамо "днів на новій пошті "
	// // поле з написамо "статус"
	// // поле з написамо "номер накладної"
	// // поле статус з накладною
	// // поле результат номеру накладної
	// // поле результат днів на пошті
	// // поле результат фактичний статус
	// // статус подый
	// // інформація по одній накладній для нової пошти
	// // інформація по одній накладній для ін-тайму
	// // назва файлу зі списком накладних нової пошти
	// // скільки накладних в списну нової пошти
	// // назва файлу зі списком накладних інтайму
	// // скільки накладних в списну інтайму
	// // скільки накладних в списку для відправки смс нової пошти
	// // назва файлу зі списком накладних нової пошти для відправки смс
	// // скільки нарахунку
	// // скільки нарахунку
	// // чи достатньо коштів для відправки всіх смс з списку
	// // чи достатньо коштів для відправки всіх смс з списку для реклами
	// // назва файлу з номерами для відправки рекламних повідомленнь
	// // скільки смс повідомлень буде відправлено рекламного характеру.
	// // якшо список номерів порожній
	// // якшо список номерів порожній для вкладки нвыдправки смс
	// // якшо список номерів порожній для новоъ пошти
	// // якшо список номерів порожній для ынтайму
	@FXML
	public Label namedefaultpacagetofindfilettnnp, poststatusnp, regusngstatus, doyouagree, textisempty, phonemasage,
			howsms, iffileisemptyit, iffileisemptynp, iffileisemptysendsms, iffileisempty, howmanysmsadvertising,
			namefilesmsadvertising, whethersufficientfundsfromadvertising, whethersufficientfundsfrom, howmanymoney1,
			howmanymoney, namefilesms, howmanysms, howmanyttnit, namefilettnit, howmanyttnnp, namefilettnnp,
			infoforonit, infoforonnp, status, statuspost, dnivnaposhtipost, numberttnpost, ifststusrefusing, numberttn,
			statusttn, dnivnaposhti;
	@FXML
	public Label accespanelabel;
	@FXML
	public Label namefoldertosavexls;
	///////////////////////////////////////////////////////
	// поле для вводу номеру накладної нової пошти
	// поле для вводу номеру накладної інтайму
	// // текст рекламного повідомленя
	// // номер телефону для відправки смс
	@FXML
	public TextField npttn, intimettn, messagetext, telephonereceiver, DefaultPhoneNumberSender;
	///////////////////////////////////////////////////////
	// гіпер силка шоб поповнити смс
	// гіперсилка на сайт нової пошти
	// гіперсилка на сатй інтайму
	// гіперсилка на сатй гіт розробників
	// вспливаюче вікно
	// вспливаюче вікно всюда
	@FXML
	public Hyperlink myatompark, novaposhta, intime, autor, smsreport;
	@FXML
	public Pane accespane, paneagree;
	// прогрес бар
	@FXML
	public ProgressBar progeress;
	@FXML
	public ProgressIndicator runindicator;

	public ProgressBar getProgeress() {
		return progeress;
	}

	public Button getGetstatusnp() {
		return getstatusnp;
	}

	public RadioButton getSmartreno() {
		return smartreno;
	}

	public RadioButton getSmart() {
		return smart;
	}

	public RadioButton getReno() {
		return reno;
	}

	public Label getPhonemasage() {
		return phonemasage;
	}

	public OperatorPhoneRecipient getPhonerecipient() {
		return phonerecipient;
	}

	public RadioButton getSmartio() {
		return smartio;
	}

	public RadioButton getRenomax() {
		return renomax;
	}

	public void setNamefoldertosavexls(Label namefoldertosavexls) {
		this.namefoldertosavexls = namefoldertosavexls;
	}

	public Label getAccespanelabel() {
		return accespanelabel;
	}

	public void setAccespanelabel(Label accespanelabel) {
		this.accespanelabel = accespanelabel;
	}

	public ProgressIndicator getRunindicator() {
		return runindicator;
	}

	public Label getNamefoldertosavexls() {
		return namefoldertosavexls;
	}

	public void initialize() {

		//
		// if (Browsfilefromsms == null) {
		// SendSMS.setDisable(true);
		// smartio.setDisable(true);
		// renomax.setDisable(true);
		// }
		// if (massmailing.isSelected() == true) {
		// if (browsfilefromsmsadvertising == null) {
		// sendsmsadvertising.setDisable(true);
		// } else {
		// sendsmsadvertising.setDisable(false);
		// }
		// }
		//
		// if (progeress.getProgress() == 1.0) {
		// progeress.setStyle("-fx-accent: green;");
		// } else {
		// progeress.setStyle("-fx-accent: red;");
		// }
		// logic.HowSmsNeedToSend(this);
		// logic.TESTPANEadvertising(this);
		// if (npttn.getText().toString().length() == 14) {
		// getstatusnp.setDisable(false);
		// iffileisemptynp.setText("");
		// } else if (npttn.getText().toString().length() == 0) {
		// iffileisemptynp.setText("Введіть ТТН");
		// } else {
		// iffileisemptynp.setVisible(true);
		// iffileisemptynp.setText("невірний формат");
		// getstatusnp.setDisable(true);
		// }
	}

	//////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// файл зі списком накладних з нової пошти
	// файл зі списком накладних з нової пошти для відправки смс
	// файл зі списком номерів для відправки рекламних смс
	public File Browsgetstatusnp;
	public File Browsfilefromsms;
	public File Browsflefromsmsadvertising;

	// гіперсилка на сайт інтайму
	@FXML
	private void InTime(ActionEvent event) {
		hyperlink.HyperLinkInTime();
	}

	// гіперсилка на сайт нової пошти
	@FXML
	private void NovaPoshta(ActionEvent event) {
		hyperlink.HyperLinkNovaPoshta();
	}

	// гіперсилка поповнення рахунку
	@FXML
	private void MyAtompark(ActionEvent event) {
		hyperlink.HyperLinkMyAtompark();
	}

	// гіперсилка на гіт розробників
	@FXML
	private void Autor(ActionEvent event) {
		hyperlink.HyperlinkAutor();
	}

	// рідіобатон одиноча відправка рекламних смс
	@FXML
	private void SingleSms(ActionEvent event) {
		radiobutton.RadioButtonSingleSms();
	}

	// рідіобатон масова відправка рекламних смс
	@FXML
	private void MassMailing(ActionEvent event) {
		radiobutton.RadioButtonMassMailing();
	}

	// рідіобатон для поодинокиої інформації нова пошта
	@FXML
	private void ForOneNP(ActionEvent event) {
		radiobutton.RadioButtonForOneNP();
	}

	// рідіобатон для списку нової пошти
	@FXML
	private void ManyNP(ActionEvent event) {
		radiobutton.RadioButtonManyNP();
	}

	// рідіобатон імя відправника Smartio
	@FXML
	private void Smartio(ActionEvent event) {
		radiobutton.RadioButtonSmartio();
	}

	// рідіобатон імя відправника Renomax
	@FXML
	private void Renomax(ActionEvent event) {
		radiobutton.RadioButtonRenomax();
	}

	// рідіобатон імя відправника SmartReno
	@FXML
	private void SmartReno(ActionEvent event) {
		radiobutton.RadioButtonSmartReno();
	}

	// рідіобатон імя відправника Smart
	@FXML
	private void Smart(ActionEvent event) {
		radiobutton.RadioButtonSmart();
	}

	// рідіобатон імя відправника Reno
	@FXML
	private void Reno(ActionEvent event) {
		radiobutton.RadioButonReno();
	}

	// радіобатон для НОВОГО СПИСКУ наклдадних нової пошти
	@FXML
	private void NewManyNP(ActionEvent event) {
		radiobutton.RadioButtonNewManyNP();
	}

	// кнопка відправити смс по накладним
	@FXML
	private void SendSMS(ActionEvent event) {
		runindicator.setProgress(runindicator.INDETERMINATE_PROGRESS);
		progeress.setProgress(0.0);
		progeress.setStyle("-fx-accent: red;");
		Buttons m = new Buttons(this);
		m.start();
	}

	// кнопка отримати статус по накладним нової пошти
	@FXML
	private void GetStatusNP(ActionEvent event) {
		runindicator.setProgress(runindicator.INDETERMINATE_PROGRESS);
		if (foronenp.isSelected()) {
			progeress.setProgress(0.0);
			buttons.ButtonGetStatusNP(this, radiobutton);
			progeress.setProgress(1.0);
			runindicator.setProgress(1.0);
		} else {

			progeress.setProgress(0.0);
			progeress.setStyle("-fx-accent: red;");
			GetStatusForList StatusForList = new GetStatusForList(this);
			StatusForList.start();
			StatusForList.setName("StatusForList");
		}
	}

	// кнопка відправити смс рекламного характеру
	@FXML
	private void SendSMSAdvertising(ActionEvent event) {
		buttons.ButtonsSendSMSAdvertising(this);
	}

	// відправити після попередження
	@FXML
	private void Yes(ActionEvent event) {
		buttons.ButtonsYes(this);
	}

	// Скасувати відправку
	@FXML
	private void No(ActionEvent event) {
		buttons.ButtonNo(this);
	}

	// кнопка очстити поле для телефону і тексту смс
	@FXML
	private void ClearTextandPhone(ActionEvent event) {
		buttons.ButtonClearTextandPhone(this);
	}

	// кнопка очстити поле для накладної новаої пошти
	@FXML
	private void ClearTTN(ActionEvent event) {
		buttons.ButtonClearTTN(this);
	}

	// кнопка перевірити стан рахунку
	@FXML
	private void GetHowManyMoney(ActionEvent event) {
		buttons.ButtonGetHowManyMoney(this);
	}

	// кнопка перевірки правильнсоті введеної ТТН
	@FXML
	private void ReloadTtn(ActionEvent event) {
		buttons.ButtonReloadTtn();
	}

	// кнопка перевірки правильнсоті введеної полz для телефону і тексту смс
	@FXML
	private void ReloadTtnmessagetextandtelephonereceiver(ActionEvent event) {
		buttons.ButtonReloadTtnmessagetextandtelephonereceiver(this);
	}

	// Копка вмбору дефолтної папки для відкриття файлу з накладними
	@FXML
	private void BrowsDefaultPacageToFindfileTTNNP(ActionEvent event) {
		buttons.ButtonBrowsDefaultPacageToFindfileTTNNP(this);
	}

	// Копка вибору папки для змережання файлів ексель з нвітом по накладним
	// нової пошти
	@FXML
	private void BrowsFolderToSaveXLSNP(ActionEvent event) {
		buttons.ButtonBrowsFolderToSaveXLSNP(this);
	}

	// кнопка відкрити файл з списком накладних нової пошти
	@FXML
	private void BrowsGetStatusNP(ActionEvent event) {
		howmanyttnnp.setText(null);
		initialize();
		browsgetstatusnp.setDisable(true);
		buttons.ButtonBrowsGetStatusNP(this);
		browsgetstatusnp.setDisable(false);

	}

	// кнопка відкрити файл зі списком накладних для відправки смс Нової пошти
	@FXML
	private void BrowsFileFromSms(ActionEvent event) {
		howmanysms.setText(null);
		initialize();
		browsfilefromsms.setDisable(true);
		buttons.ButtonBrowsFileFromSms(this);
		browsfilefromsms.setDisable(false);
	}

	// кнопка відкрити файл з номерами телефоні для відправки рекламних смс
	@FXML
	private void BrowsFleFromSmsAdvertising(ActionEvent event) {
		buttons.ButtonBrowsFleFromSmsAdvertising(this);
	}

	// оновлювати вкладку нова пошта при руху мишкою
	@FXML
	public void InicializeTabNovaPoshta(MouseEvent mouseEvent) {
		// if ((new File(namefoldertosavexls.getText().toString() +
		// "/накладні.txt")).exists()) {
		// manynp.setDisable(false);
		// } else {
		// howmanyttnnp.setText(null);
		// manynp.setDisable(true);
		// newmanynp.setSelected(true);
		// namefilettnnp.setText("Стандартний файл відсутній");
		// namefilettnnp.setTextFill(Color.RED);
		// browsgetstatusnp.setDisable(false);
		// }
		// initialize();
	}

	// оновлювати вкладку відправки смс
	@FXML
	public void InicializeTabsenderblok(MouseEvent mouseEvent) {
		if (netIsAvailable() == true) {
		} else {
			np.setDisable(true);
			senderblok.setDisable(true);
			advertising.setDisable(true);
			accespane.setDisable(false);
			accespane.setVisible(true);
			accespanelabel.setText("Підключення інетренету відсутнє");
		}
	}

	// // оноволювати всю форму при русі мишкою
	@FXML
	public void Refresh(MouseEvent mouseEvent) {
		if (netIsAvailable() == true) {
			np.setDisable(false);
			senderblok.setDisable(false);
			advertising.setDisable(false);
			initialize();
		}
	}

	// тест підключення інтернету
	public static boolean netIsAvailable() {
		try {
			final URL url = new URL("http://www.google.com");
			final URLConnection conn = url.openConnection();
			conn.connect();
			return true;
		} catch (MalformedURLException e) {
			throw new RuntimeException(e);

		} catch (IOException e) {
			return false;
		}
	}

	// логіка закрити вспливаюче вікно
	@FXML
	private void Close(ActionEvent event) {
		((Node) (event.getSource())).getScene().getWindow().hide();

	}

	// кнопка зупинити виконання програми
	@FXML
	private void SMSReport(ActionEvent event) {
		hyperlink.SMSReport();
	}

	// прочитати кожну строчку в вибраному файлі з накладними для нової пошти
	public List<String> ReadFileForTTN(String filetxt, List<String> lines) {
		try {
			lines = Files.readAllLines(Paths.get(filetxt), StandardCharsets.UTF_8);
		} catch (IOException e) {
			appendUsingFileWriter(namefoldertosavexls.getText().toString() + "/" + "Звіт1.txt",
					data() + "Сталась помилка при // прочитати кожну строчку в вибраному файлі з накладними для нової пошти"
							+ "\r\n");
			error.ERROR();
		}
		return lines;
	}

	public List<String> readUsingBufferedReader(String fileName, Charset cs, List<String> lines) throws IOException {
		BufferedReader reader = new BufferedReader(new FileReader(fileName));
		String line;
		while ((line = reader.readLine()) != null) {
			lines.add(line);
		}
		reader.close();
		return lines;
	}

	// записати в текстовий файл
	public static void appendUsingFileWriter(String filePath, String text) {
		File file = new File(filePath);
		OutputStreamWriter fr = null;
		try {
			fr = new OutputStreamWriter(new FileOutputStream(file, true), "UTF-8");
			fr.write(text);
		} catch (IOException e) {

		} finally {
			try {
				fr.close();
			} catch (IOException e) {
			}
		}
	}

	// отримати сьогоднішнб дату
	public static String data() {
		Date currentDate = new Date();
		SimpleDateFormat dateFormat = null;
		dateFormat = new SimpleDateFormat("MMMM");
		Calendar c = Calendar.getInstance();
		int year = c.get(Calendar.YEAR);
		int month = c.get(Calendar.MONTH) + 1;
		int day = c.get(Calendar.DAY_OF_MONTH);
		String Data = "(" + day + "." + month + "." + year + ")-";
		return Data;
	}
}