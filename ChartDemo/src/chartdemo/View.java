package chartdemo;

import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Path;
import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.browser.BrowserFunction;
import org.eclipse.swt.browser.ProgressAdapter;
import org.eclipse.swt.browser.ProgressEvent;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.ui.part.ViewPart;

import chartdemo.Chart.ChartType;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class View extends ViewPart {
	public static final String ID = "ChartDemo.view";

	private Browser browser;
	private BrowserFunction browserFunction;
	
	public void createPartControl(Composite parent) {
		try {
			final URL url = FileLocator.find(Activator.getDefault().getBundle(), new Path("/resources/index.html"), Collections.emptyMap());
			final URL urlFile = FileLocator.toFileURL(url);
			
			// Create some mock chart objects.
			List<Chart> charts = getMockData();
			
			this.browser = new Browser(parent, SWT.BORDER);	
			this.browser.setUrl(urlFile.toString());
			this.browser.addProgressListener(new ProgressAdapter() {
				@Override
				public void completed(ProgressEvent event) {
					// Build a JSON representation of the model and fire it off to the browser.
					for (Chart chart : charts) {
						String json = buildChartJson(chart);
						System.out.println(json);
						browser.execute(String.format("addTile(%s)", json));
					}
				}
			});
			this.browserFunction = new BrowserFunction(browser, "callBack") {
				@Override
				public Object function(Object[] arguments) {
					MessageBox msgBox = new MessageBox(parent.getShell());
					msgBox.setMessage(String.format("You clicked on %s in the %s widget", arguments[0] /* segment name */, arguments[1] /* chart ID */));
					msgBox.open();
					return super.function(arguments);
				}
			};
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Generate some dummy chart data.
	 * 
	 * @return
	 */
	private List<Chart> getMockData() {
		List<Chart> charts = new ArrayList<>();
		
		Chart chart = new Chart();
		chart.setName("By Currency");
		chart.setId("ByCurrency");
		chart.setFeature("Currency");
		chart.setType(ChartType.PIE);
		Map<String,Double> data = new HashMap<>();
		data.put("GBP", 40.0);
		data.put("USD", 32.0);
		data.put("AUD", 13.0);
		data.put("CHF", 10.0);
		data.put("EUR", 5.0);
		chart.setData(data);
		charts.add(chart);
		
		Chart chart2 = new Chart();
		chart2.setName("By Record Type");
		chart2.setId("ByRecordType");
		chart2.setFeature("Record Type");
		chart2.setType(ChartType.COLUMN);
		Map<String,Double> data2 = new HashMap<>();
		data2.put("OTEM", 40.0);
		data2.put("CASH/CHQ", 35.0);
		data2.put("ARM", 15.0);
		data2.put("DC", 10.0);
		chart2.setData(data2);
		charts.add(chart2);
		
		Chart chart3 = new Chart();
		chart3.setName("By Customer");
		chart3.setId("ByCustomer");
		chart3.setFeature("Customer Name");
		chart3.setType(ChartType.BAR);
		Map<String,Double> data3 = new HashMap<>();
		data3.put("Favoride", 50.0);
		data3.put("Equivale GB", 20.0);
		data3.put("Sproink Global", 10.0);
		data3.put("Solaxo Pharmaceuticals", 20.0);
		chart3.setData(data3);
		charts.add(chart3);
		
		Chart chart4 = new Chart();
		chart4.setName("By Customer");
		chart4.setId("ByCustomer2");
		chart4.setFeature("Customer Name");
		chart4.setType(ChartType.LIST);
		Map<String,Double> data4 = new HashMap<>();
		data4.put("Favoride", 50.0);
		data4.put("Equivale GB", 20.0);
		data4.put("Sproink Global", 10.0);
		data4.put("Solaxo Pharmaceuticals", 20.0);
		chart4.setData(data4);
		charts.add(chart4);
				
		return charts;
	}
	
	private String buildChartJson(Chart chart) {
		ObjectMapper mapper = new ObjectMapper();
		try {
			return mapper.writeValueAsString(chart);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public void setFocus() {
		this.browser.setFocus();
	}
	
	@Override
	public void dispose() {
		this.browserFunction.dispose();
		this.browser.dispose();
		super.dispose();
	}
}