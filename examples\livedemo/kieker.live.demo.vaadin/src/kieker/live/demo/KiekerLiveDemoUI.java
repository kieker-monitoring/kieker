package kieker.live.demo;

import javax.servlet.annotation.WebServlet;

import kieker.live.demo.RecordView;
import kieker.live.demo.MainView;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.Title;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.annotations.Widgetset;
import com.vaadin.navigator.Navigator;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.UI;

@SuppressWarnings("serial")
@Theme("kieker_live_demo_vaadin")
@Title("Kieker Live Demo #Vaadin")
@Widgetset(value = "kieker.live.demo.widgetset.Kieker_live_demo_vaadinWidgetset")
public class KiekerLiveDemoUI extends UI {

	private static Navigator navigator;

	@WebServlet(value = "/*", asyncSupported = true)
	@VaadinServletConfiguration(productionMode = false, ui = KiekerLiveDemoUI.class, widgetset = "kieker.live.demo.widgetset.Kieker_live_demo_vaadinWidgetset")
	public static class YourServlet extends VaadinServlet {
	}
	
	@Override
	protected void init(final VaadinRequest request) {
		KiekerLiveDemoUI.navigator = new Navigator(this, this);

		KiekerLiveDemoUI.navigator.addView("main", new MainView());
		KiekerLiveDemoUI.navigator.addView("recordList", new RecordView());
		KiekerLiveDemoUI.navigator.addView("methodResponsetime", new MethodResponsetimeView());

		KiekerLiveDemoUI.navigateToMain();
	}

	public static void navigateToMain() {
		KiekerLiveDemoUI.navigator.navigateTo("main");
	}

	public static void navigateToRecordList() {
		KiekerLiveDemoUI.navigator.navigateTo("recordList");
	}
	
	public static void navigateToMethodResponsetime() {
		KiekerLiveDemoUI.navigator.navigateTo("methodResponsetime");
	}

}