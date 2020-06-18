package com.cdph.example.jsonparser.net;

import android.content.Context;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import java.util.HashMap;
import java.util.Map;

public class VolleyRequest 
{
	private OnResponseListener listener;
	private Context context;
    private String endPoint = "", baseUrl = "";

	private VolleyRequest()
	{}

	private VolleyRequest(Context context, String baseUrl)
	{
		this.baseUrl = baseUrl;
		this.context = context;
	}

	public static synchronized VolleyRequest newRequest(Context context, String baseUrl)
	{
		return (new VolleyRequest(context, baseUrl));
	}

	public VolleyRequest setEndPoint(String endPoint)
	{
		this.endPoint = endPoint;
		return this;
	}

	public VolleyRequest addListener(OnResponseListener listener)
	{
		this.listener = listener;
		return this;
	}

	public void sendPostRequest(final HashMap<String, Object> data)
	{
		Response.Listener<String> _response_ = new Response.Listener<String>() {
			@Override
			public void onResponse(String response)
			{
				if(listener != null)
					listener.onResponse(response);
			}
		};

		Response.ErrorListener _error_ = new Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error)
			{
				if(listener != null)
					listener.onResponse(error.getMessage());
			}
		};

		StringRequest request = new StringRequest(Request.Method.POST, baseUrl + endPoint, _response_, _error_) {
			@Override
			protected Map<String, String> getParams()
			{
				Map<String, String> params = new HashMap<>();

				for(Map.Entry entry : data.entrySet())
					params.put(String.valueOf(entry.getKey()), String.valueOf(entry.getValue()));

				return params;
			}
		};

		RequestQueue queue = Volley.newRequestQueue(context);
		queue.add(request);
	}
	
	public void sendGetRequest(final HashMap<String, Object> data)
	{
		Response.Listener<String> _response_ = new Response.Listener<String>() {
			@Override
			public void onResponse(String response)
			{
				if(listener != null)
					listener.onResponse(response);
			}
		};

		Response.ErrorListener _error_ = new Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error)
			{
				if(listener != null)
					listener.onResponse(error.getMessage());
			}
		};

		StringRequest request = new StringRequest(Request.Method.GET, baseUrl + endPoint, _response_, _error_) {
			@Override
			protected Map<String, String> getParams()
			{
				Map<String, String> params = new HashMap<>();

				for(Map.Entry entry : data.entrySet())
					params.put(String.valueOf(entry.getKey()), String.valueOf(entry.getValue()));

				return params;
			}
		};

		RequestQueue queue = Volley.newRequestQueue(context);
		queue.add(request);
	}
	
	public void readJsonFromWeb()
	{
		Response.Listener<String> _response_ = new Response.Listener<String>() {
			@Override
			public void onResponse(String response)
			{
				if(listener != null)
					listener.onResponse(response);
			}
		};

		Response.ErrorListener _error_ = new Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error)
			{
				if(listener != null)
					listener.onResponse(error.getMessage());
			}
		};

		StringRequest request = new StringRequest(
			Request.Method.GET, 
			baseUrl + endPoint, 
			_response_, 
			_error_
		);

		RequestQueue queue = Volley.newRequestQueue(context);
		queue.add(request);
	}

	public interface OnResponseListener
	{
		public void onResponse(String response);
	}
}
