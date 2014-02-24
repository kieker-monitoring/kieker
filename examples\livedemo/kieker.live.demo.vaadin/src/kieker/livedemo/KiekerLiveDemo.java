package kieker.livedemo;

import kieker.livedemo.view.main.MainView;

import com.vaadin.annotations.Theme;
import com.vaadin.navigator.Navigator;
import com.vaadin.server.VaadinRequest;
import com.vaadin.ui.UI;

@SuppressWarnings("serial")
@Theme("kieker_live_demo")
public class KiekerLiveDemo extends UI {

	private static Navigator navigator;

	@Override
	protected void init(final VaadinRequest request) {
		KiekerLiveDemo.navigator = new Navigator(this, this);

		KiekerLiveDemo.navigator.addView("main", new MainView());
		KiekerLiveDemo.navigator.addView("recordList", new RecordView());

		KiekerLiveDemo.navigateToMain();
	}

	public static void navigateToMain() {
		KiekerLiveDemo.navigator.navigateTo("main");
	}

	public static void navigateToRecordList() {
		KiekerLiveDemo.navigator.navigateTo("recordList");
	}

}
