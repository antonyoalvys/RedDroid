package biz.remanso.redmine.ui;

import java.util.HashMap;
import java.util.Map;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import biz.remanso.redmine.api.RedmineApiManager;

public class LoginActivity extends Activity implements OnClickListener {

	private EditText usernameEdit;
	private EditText passwordEdit;
	private EditText hostEdit;
	
	Map<String, String> credentials = new HashMap<String, String>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);

		usernameEdit = (EditText) findViewById(R.id.etUsername);
		passwordEdit = (EditText) findViewById(R.id.etPassword);
		hostEdit = (EditText) findViewById(R.id.etHost);
		Button confirmButton = (Button) findViewById(R.id.btnConfirm);
		confirmButton.setOnClickListener(this);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_login, menu);
		return true;
	}

	@Override
	public void onClick(View v) {
		credentials = new HashMap<String, String>();
		credentials.put("username", usernameEdit.getText().toString());
		credentials.put("password", passwordEdit.getText().toString());
		credentials.put("host", hostEdit.getText().toString());
		
		LoginTask task = new LoginTask(); 
		task.execute(credentials);
		 
	}

	public class LoginTask extends
			AsyncTask<Map<String, String>, Void, Integer> {
		protected ProgressDialog dialog;

		protected void onPreExecute() {
			dialog = ProgressDialog.show(LoginActivity.this, "",
					"Loading. Please wait...", true);
		}

		@Override
		protected Integer doInBackground(Map<String, String>... params) {
			String username = params[0].get("username");
			String password = params[0].get("password");
			String host = params[0].get("host");
			String api_key;
			
			RedmineApiManager mgr = new RedmineApiManager(username, password,
					host);
			try {
				api_key = mgr.getApiKey();
				
				params[0].remove(password);
				params[0].put("key", api_key);
			} catch (Exception e) {
				e.printStackTrace();
				return -1;
			}

			return 1;
		}

		protected void onPostExecute(Integer result) {
			Intent intent = new Intent(LoginActivity.this,
					ProjectsActivity.class);
			intent.putExtra("username", credentials.get("username"));
			intent.putExtra("host", credentials.get("host"));
			intent.putExtra("key", credentials.get("key"));
			setResult(RESULT_OK, intent);
			dialog.dismiss();
			startActivity(intent);
			finish();

		}

	}

}
