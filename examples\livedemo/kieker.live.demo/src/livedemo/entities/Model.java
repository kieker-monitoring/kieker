package livedemo.entities;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Bjoern Weissenfels
 */
public class Model<T>{
	
	private T model;
	private String name;
	private List<String> ids;
	
	public Model(T model, String name){
		this.model = model;
		this.name = name;
		this.ids = new ArrayList<String>();
	}
	
	public T getModel(){
		return this.model;
	}
	
	public void setModel(T model){
		this.model = model;
	}
	
	public String getName(){
		return this.name;
	}
	
	public void setName(String name){
		this.name = name;
	}

	public List<String> getIds() {
		return ids;
	}

	public void setIds(List<String> ids) {
		this.ids = ids;
	}
	
	public void addId(String id){
		this.ids.add(id);
	}

}
