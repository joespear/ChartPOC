package chartdemo;

import java.util.Map;

/**
 * A pojo to represent a chart with some data.
 * 
 * @author Spear
 *
 */
public class Chart {
	private String id;
	private String name;
	private Map<String,Double> data;
	private ChartType type;
	private String feature;
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}

	public Map<String,Double> getData() {
		return data;
	}

	public void setData(Map<String,Double> data) {
		this.data = data;
	}
	
	public ChartType getType() {
		return type;
	}

	public void setType(ChartType chartType) {
		this.type = chartType;
	}

	public String getFeature() {
		return feature;
	}

	public void setFeature(String feature) {
		this.feature = feature;
	}

	public enum ChartType {
		PIE, COLUMN, BAR, LIST
	}
	
}
