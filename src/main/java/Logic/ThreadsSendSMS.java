package Logic;

import Interface.MainController;

public class ThreadsSendSMS extends Thread {
	private MainController controller;
	public Buttons buttons;

	public ThreadsSendSMS(MainController controller) {
		this.controller = controller;
		this.buttons = new Buttons(controller);
	}
// запустити потік для відправки смс по накладним
	@Override
	public void run() {
			buttons.ButtonSendSMS();
	}
}
