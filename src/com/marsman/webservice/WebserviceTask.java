package com.marsman.webservice;

import java.security.PublicKey;

import android.R.integer;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;

public class WebserviceTask extends AsyncTask<String, Integer, Object> {

	private Context mContext;
	private Handler mHandler;

	private String[] params;
	private int type;

	public WebserviceTask(Context context) {
		this.mContext = context;
	}

	public WebserviceTask(Context context, Handler handler) {
		this.mContext = context;
		this.mHandler = handler;
	}

	public void sendExecute(String[] params, int type) {
		this.params = params;
		this.type = type;
		this.execute(params);

	}

	public void sendExecuteNo(String[] params, int type) {
		this.params = params;
		this.type = type;
		this.execute(params);
	}

	@Override
	protected Object doInBackground(String... arg0) {
		// TODO Auto-generated method stub
		return doBackWork(params, type);
	}

	@Override
	protected void onPostExecute(Object result) {
		super.onPostExecute(result);
		if (null == result) {
			return;
		}

		Message message = new Message();
		message.what = type;
		message.obj = result;
		mHandler.sendMessage(message);
	}

	private Object doBackWork(String[] params, int type) {

		Object result = null;
		switch (type) {
		case 0:

			break;

		default:
			break;
		}
		return result;
	}

}
