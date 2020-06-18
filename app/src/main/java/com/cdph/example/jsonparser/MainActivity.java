package com.cdph.example.jsonparser;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ClipboardManager;
import android.content.ClipData;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;

import com.cdph.example.jsonparser.net.VolleyRequest;

public class MainActivity extends Activity implements AdapterView.OnItemClickListener
{
	private static final String JSON_URL = "https://raw.githubusercontent.com/fate0/proxylist/master/proxy.list";
	private List<String> listOfHosts = new ArrayList<>();
	private String json;
	private ListView list;
	
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
		
		list = (ListView) findViewById(R.id.mainListView);
		list.setOnItemClickListener(this);
		readJsonFromWebAndDisplay();
    }
	
	@Override
	public void onItemClick(AdapterView<?> adapter, View view, int position, long id)
	{
		displayInfoOfHost(listOfHosts.get(position));
	}
	
	private void readJsonFromWebAndDisplay()
	{
		final ProgressDialog pd = new ProgressDialog(this);
		pd.setMessage("Fetching data...");
		pd.setCancelable(false);
		pd.setCanceledOnTouchOutside(false);
		pd.setIndeterminate(true);
		pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		pd.show();
		
		VolleyRequest.newRequest(this, JSON_URL)
			.addListener(new VolleyRequest.OnResponseListener() {
				@Override
				public void onResponse(String jsonResponse)
				{
					pd.dismiss();
					
					json = jsonResponse;
					loadHostsToList(jsonResponse);
					displayListOfHosts(listOfHosts);
				}
			})
			.readJsonFromWeb();
	}
	
	private void loadHostsToList(String json)
	{
		try {
			json = fixJson(json);
			
			JSONArray jarr = new JSONArray(json);
			for(int i = 0; i < jarr.length(); i++)
			{
				if(jarr.getJSONObject(i) == null)
					continue;
				
				JSONObject jobj = jarr.getJSONObject(i);
				listOfHosts.add(jobj.getString("host"));
			}
		} catch(Exception e) {
			e.printStackTrace();
			Log.e(MainActivity.class.getSimpleName() + " - List Hosts -", e.getMessage());
		}
	}
	
	private void displayInfoOfHost(String hostQuery)
	{
		try {
			JSONArray jarr = new JSONArray(fixJson(json));
			for(int i = 0; i < jarr.length(); i++)
			{
				JSONObject jobj = jarr.getJSONObject(i);
				String _host_ = jobj.getString("host");
				
				if(!_host_.toLowerCase().equals(hostQuery.toLowerCase()))
					continue;
					
				JSONArray _exp_addr_ = jobj.getJSONArray("export_address");
				String _type_ = jobj.getString("type");
				String _from_ = jobj.getString("from");
				String _country_ = jobj.getString("country");
				double _response_time_ = jobj.getDouble("response_time");
				String _anonymity_ = jobj.getString("anonymity");
				
				String info = "Country: %s\nFrom: %s\nType: %s\nResponse Time: %02f\nAnonimity: %s\nHost: %s\nExport Address: %s";
				String addr = "";
				
				for(int x = 0; x < _exp_addr_.length(); x++)
					addr += _exp_addr_.getString(x) + ",";
				addr = addr.substring(0, addr.length()-1);
				
				AlertDialog dlg = new AlertDialog.Builder(this).create();
				dlg.setCancelable(true);
				dlg.setCanceledOnTouchOutside(false);
				dlg.setMessage(String.format(info, _country_, _from_, _type_, _response_time_, _anonymity_, _host_, addr));
				dlg.requestWindowFeature(Window.FEATURE_NO_TITLE);
				dlg.show();
				break;
			}
		} catch(Exception e) {
			e.printStackTrace();
			Log.e(MainActivity.class.getSimpleName() + " - DisplayInfo -", e.getMessage());
		}
	}
	
	private void displayListOfHosts(List<String> hosts)
	{
		ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, listOfHosts);
		list.setAdapter(adapter);
	}
	
	private String fixJson(String json)
	{
		String[] split = json.split("[}]");
		String ret = "";
		
		for(int i = 0; i < split.length; i++)
			ret += split[i] + "},";
		
		ret = ret.substring(0, ret.length()-3);
		ret = "[" + ret + "]";
		return ret;
	}
}
