package livedemo.entities;

import org.primefaces.model.chart.CartesianChartModel;

public class Model{
	
	CartesianChartModel ccmodel;
	String name;
	
	public Model(CartesianChartModel model, String name){
		this.ccmodel = model;
		this.name = name;
	}
	
	public CartesianChartModel getModel(){
		return this.ccmodel;
	}
	
	public void setModel(CartesianChartModel model){
		this.ccmodel = model;
	}
	
	public String getName(){
		return this.name;
	}
	
	public void setName(String name){
		this.name = name;
	}

}
