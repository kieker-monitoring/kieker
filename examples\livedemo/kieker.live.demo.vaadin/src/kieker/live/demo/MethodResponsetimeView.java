package kieker.live.demo;

import java.util.Arrays;
import java.util.LinkedList;

import org.dussan.vaadin.dcharts.DCharts;
import org.dussan.vaadin.dcharts.data.DataSeries;
import org.dussan.vaadin.dcharts.metadata.renderers.SeriesRenderers;
import org.dussan.vaadin.dcharts.options.Options;
import org.dussan.vaadin.dcharts.options.SeriesDefaults;

import java.util.Random;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.ui.Button;
import com.vaadin.ui.CustomLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Panel;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.VerticalLayout;

public class MethodResponsetimeView extends Panel implements View {

	private static final long serialVersionUID = -2313861106966236721L;
	final DCharts chart = new DCharts();
	LinkedList<Integer> list = new LinkedList<Integer>();
	LinkedList<Integer> list2 = new LinkedList<Integer>();
	
	public MethodResponsetimeView(){
		CustomLayout layout = new CustomLayout("methodResponsetimeView");
		setContent(layout);
		
		SeriesDefaults seriesDefaults = new SeriesDefaults().setRenderer(SeriesRenderers.LINE);
		Options options = new Options().setSeriesDefaults(seriesDefaults);
		chart.setOptions(options);
		
		initLists();
		DataSeries dataSeries = new DataSeries();
		dataSeries.add(list.toArray());
		dataSeries.add(list2.toArray());
		chart.setDataSeries(dataSeries).show();
		
		Button button = new Button("Main");
		button.addClickListener(new Button.ClickListener() {
			private static final long serialVersionUID = 1351548194600430027L;

			public void buttonClick(ClickEvent event) {
				KiekerLiveDemoUI.navigateToMain();
			}
		});
		
		Button button2 = new Button("new Data");
		button2.addClickListener(new Button.ClickListener() {
			private static final long serialVersionUID = -6517719076274910305L;

			public void buttonClick(ClickEvent event) {
				newData();
				DataSeries dataSeries = new DataSeries();
				dataSeries.add(list.toArray());
				dataSeries.add(list2.toArray());
				chart.setDataSeries(dataSeries).show();
			}
		});
		HorizontalLayout hlayout = new HorizontalLayout();
		hlayout.addComponent(button);
		hlayout.addComponent(button2);
		VerticalLayout vlayout = new VerticalLayout();
		vlayout.addComponent(hlayout);
		vlayout.addComponent(chart);
		
		layout.addComponent(vlayout, "main");
	}
	
	private void initLists(){
		list.addAll(Arrays.asList(0,1,2,3,4,5,6,7,8));
		list2.addAll(Arrays.asList(29,28,27,26,25,24,23,22,21));
	}
	
	public void newData(){
		Integer i = new Random().nextInt(30);
		Integer j = new Random().nextInt(30);
		list.remove();
		list2.remove();
		list.add(i);
		list2.add(j);
	}
	
	@Override
	public void enter(ViewChangeEvent event) {
		
	}

}
