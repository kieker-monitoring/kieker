package kieker.live.demo;

import org.dussan.vaadin.dcharts.DCharts;
import org.dussan.vaadin.dcharts.data.DataSeries;
import org.dussan.vaadin.dcharts.metadata.renderers.SeriesRenderers;
import org.dussan.vaadin.dcharts.options.Options;
import org.dussan.vaadin.dcharts.options.SeriesDefaults;

import com.google.gwt.user.client.Random;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.ui.Button;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Button.ClickEvent;

@SuppressWarnings("serial")
public class MethodResponsetimeView extends Panel implements View {

	final DCharts chart = new DCharts();
	
	public MethodResponsetimeView(){
		final VerticalLayout layout = new VerticalLayout();
		layout.setMargin(true);
		setContent(layout);
		Button button = new Button("Main");
		button.addClickListener(new Button.ClickListener() {
			public void buttonClick(ClickEvent event) {
				KiekerLiveDemoUI.navigateToMain();
			}
		});
		layout.addComponent(button);
		
		Button button2 = new Button("new Data");
		button2.addClickListener(new Button.ClickListener() {
			public void buttonClick(ClickEvent event) {
				chart.setDataSeries(
						new DataSeries()
							.add( 7, 9, 1, 4, 6, 8, 2, 5, 11))
						.show();
			}
		});
		layout.addComponent(button2);

		chart.setEnableChartDataClickEvent(true);
		chart.setEnableChartDataRightClickEvent(true);

		
		SeriesDefaults seriesDefaults = new SeriesDefaults()
		.setRenderer(SeriesRenderers.PIE);

	Options options = new Options()
		.setSeriesDefaults(seriesDefaults);
	
	chart.setOptions(options);
	
		chart.setDataSeries(
			new DataSeries()
				.add(3, 7, 9, 1, 4, 6, 8, 2, 5))
			.show();
		layout.addComponent(chart);		
	}
	
	@Override
	public void enter(ViewChangeEvent event) {
		
	}

}
