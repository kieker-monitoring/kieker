package kieker.live.demo;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.ui.Button;
import com.vaadin.ui.CustomLayout;
import com.vaadin.ui.Panel;

public class RecordView extends Panel implements View {

	private static final long serialVersionUID = -2628616342439990109L;

	public RecordView() {
		CustomLayout layout = new CustomLayout("recordView");
		
		final Button button = new Button("Hallo, Welt!");
		button.addClickListener(new Button.ClickListener() {

			private static final long serialVersionUID = -303825246905314764L;

			@Override
			public void buttonClick(final com.vaadin.ui.Button.ClickEvent event) {
				KiekerLiveDemoUI.navigateToMain();
			}
		});
		layout.addComponent(button, "main");
		setContent(layout);
	}

	@Override
	public void enter(final ViewChangeEvent event) {}

}
