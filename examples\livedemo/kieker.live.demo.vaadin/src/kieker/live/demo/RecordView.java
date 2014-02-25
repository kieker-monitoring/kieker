package kieker.live.demo;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.ui.Button;
import com.vaadin.ui.Panel;

@SuppressWarnings("serial")
public class RecordView extends Panel implements View {

	public RecordView() {
		final Button button = new Button("Hallo, Welt!");
		button.addClickListener(new Button.ClickListener() {

			@Override
			public void buttonClick(final com.vaadin.ui.Button.ClickEvent event) {
				KiekerLiveDemoUI.navigateToMain();
			}
		});
		setContent(button);
	}

	@Override
	public void enter(final ViewChangeEvent event) {}

}
