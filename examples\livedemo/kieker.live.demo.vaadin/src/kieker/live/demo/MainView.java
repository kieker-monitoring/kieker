package kieker.live.demo;

import kieker.live.demo.KiekerLiveDemoUI;

import com.vaadin.event.MouseEvents;
import com.vaadin.event.MouseEvents.ClickEvent;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.ThemeResource;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Image;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;

@SuppressWarnings("serial")
public class MainView extends Panel implements View {

	public MainView() {
		final HorizontalLayout hLayout1 = new HorizontalLayout();
		final HorizontalLayout hLayout2 = new HorizontalLayout();
		final HorizontalLayout hLayout3 = new HorizontalLayout();

		// Create the embedded images
		final Image imgJPetStore = new Image(null, new ThemeResource("img/petstore.png"));
		final Image imgRecordList = new Image(null, new ThemeResource("img/recordList.png"));
		final Image imgMethodResponseTimes = new Image(null, new ThemeResource("img/methodResponseTimes.png"));
		final Image imgSystemModel = new Image(null, new ThemeResource("img/systemModel.png"));
		final Image imgCPUMemSwap = new Image(null, new ThemeResource("img/cpuMemSwap.png"));

		// A little bit style for the images
		imgJPetStore.addStyleName("embedded");
		imgRecordList.addStyleName("embedded");
		imgMethodResponseTimes.addStyleName("embedded");
		imgSystemModel.addStyleName("embedded");
		imgCPUMemSwap.addStyleName("embedded");

		imgRecordList.addClickListener(new MouseEvents.ClickListener() {

			@Override
			public void click(final ClickEvent event) {
				KiekerLiveDemoUI.navigateToRecordList();
			}
		});
		
		imgMethodResponseTimes.addClickListener(new MouseEvents.ClickListener() {

			@Override
			public void click(final ClickEvent event) {
				KiekerLiveDemoUI.navigateToMethodResponsetime();
			}
		});


		// Add the images to the layout
		hLayout1.addComponent(imgJPetStore);

		hLayout2.addComponent(imgRecordList);
		hLayout2.addComponent(imgMethodResponseTimes);

		hLayout3.addComponent(imgSystemModel);
		hLayout3.addComponent(imgCPUMemSwap);

		hLayout1.setSizeFull();
		hLayout2.setSizeFull();
		hLayout3.setSizeFull();

		hLayout1.addStyleName("centered");
		hLayout2.addStyleName("centered");
		hLayout3.addStyleName("centered");

		final VerticalLayout vLayout = new VerticalLayout(hLayout1, hLayout2, hLayout3);
		vLayout.setSizeFull();
		setContent(vLayout);
	}

	@Override
	public void enter(final ViewChangeEvent event) {}

}
