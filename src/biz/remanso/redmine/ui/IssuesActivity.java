package biz.remanso.redmine.ui;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpException;
import org.json.JSONException;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.widget.ListAdapter;
import biz.remanso.redmine.api.RedmineApiManager;
import biz.remanso.redmine.api.beans.Issue;
import biz.remanso.redmine.util.CustomListItem;
import biz.remanso.redmine.util.ListItemAdapter;

public class IssuesActivity extends ListActivity {

	private String host;
	private String username;
	private String key;
	private String projectId;

	List<Issue> issues;

	Context context;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_issues);

		Intent intent = getIntent();
		host = intent.getStringExtra("host");
		username = intent.getStringExtra("username");
		key = intent.getStringExtra("key");
		projectId = intent.getStringExtra("projectId");
		context = this;

		new ConexaoThread().start();

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_issues, menu);
		return true;
	}

	class ConexaoThread extends Thread {
		boolean logged = false;
		Handler h = new Handler();

		public void run() {

			RedmineApiManager mgr = new RedmineApiManager(host, key);

			try {
				issues = mgr.getIssues(key, projectId);
			} catch (HttpException e) {
				e.printStackTrace();
			} catch (JSONException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}

			List<CustomListItem> items = new ArrayList<CustomListItem>();

			for (Issue i : issues) {
				items.add(new CustomListItem(Integer.parseInt(i.getId()), i
						.getSubject()));
			}

			final ListAdapter adapter = new ListItemAdapter(context, items);

			h.post(new Runnable() {

				@Override
				public void run() {
					IssuesActivity.this.setListAdapter(adapter);
				}
			});

		}

	}

}
