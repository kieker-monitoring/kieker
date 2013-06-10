package livedemo.entities;

import org.primefaces.model.chart.CartesianChartModel;

/**
 * @author Bjoern Weissenfels
 */
public class Model{
	
	private CartesianChartModel firstModel;
	private CartesianChartModel secondModel;
	private String name;
	
	public Model(CartesianChartModel model, String name){
		this.firstModel = model;
		this.name = name;
	}
	
	public Model(CartesianChartModel firstModel, CartesianChartModel secondModel, String name){
		this.firstModel = firstModel;
		this.secondModel = secondModel;
		this.name = name;
	}
	
	public CartesianChartModel getModel(){
		return this.firstModel;
	}
	
	public void setModel(CartesianChartModel model){
		this.firstModel = model;
	}
	
	public CartesianChartModel getSecondModel(){
		return this.secondModel;
	}
	
	public void setSecondModel(CartesianChartModel model){
		this.secondModel = model;
	}
	
	public String getName(){
		return this.name;
	}
	
	public void setName(String name){
		this.name = name;
	}

}
