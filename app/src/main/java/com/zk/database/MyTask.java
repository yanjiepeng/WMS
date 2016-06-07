package com.zk.database;

import android.os.AsyncTask;

public class MyTask extends AsyncTask<String, Integer, String> {

	MyTaskInterface myTaskInterface;

	public MyTask(MyTaskInterface myTaskInterface) {
		this.myTaskInterface = myTaskInterface;
	}

	// onPreExecute
	@Override
	protected void onPreExecute() {
	}

	// doInBackground子线程执行
	@Override
	protected String doInBackground(String... params) {
		myTaskInterface.doBackGround();
		return null;
	}

	// onProgressUpdate 
	@Override
	protected void onProgressUpdate(Integer... progresses) {
	}

	// onPostExecute  UI
	@Override
	protected void onPostExecute(String result) {
		myTaskInterface.doUi();
	}

	// onCancelled取消
	@Override
	protected void onCancelled() {
	}
}
