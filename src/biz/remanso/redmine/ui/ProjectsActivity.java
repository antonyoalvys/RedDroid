package biz.remanso.redmine.ui;

import java.util.ArrayList;
import java.util.List;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.widget.ListAdapter;
import biz.remanso.redmine.api.RedmineApiManager;
import biz.remanso.redmine.api.beans.Project;
import biz.remanso.redmine.util.CustomListItem;
import biz.remanso.redmine.util.ListItemAdapter;

public class ProjectsActivity extends ListActivity {

	private List<Project> projects;

	String host;
	String username;
	String key;
	Context context;
	
	String projectSelected;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_projects);

		Intent intent = getIntent();
		host = intent.getStringExtra("host");
		username = intent.getStringExtra("username");
		key = intent.getStringExtra("key");
		context = this;

		new ConexaoThread().start();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_projects, menu);
		return true;
	}

	class ConexaoThread extends Thread {
		boolean logged = false;
		Handler h = new Handler();

		public void run() {

			RedmineApiManager mgr = new RedmineApiManager(host, key);

			try {
				projects = mgr.getProjects(key);

				List<CustomListItem> items = new ArrayList<CustomListItem>();

				for (Project p : projects) {
					items.add(new CustomListItem(Integer.parseInt(p.getId()), p
							.getName()));
				}

				final ListAdapter adapter = new ListItemAdapter(context, items);

				h.post(new Runnable() {

					@Override
					public void run() {
						ProjectsActivity.this.setListAdapter(adapter);
					}
				});

			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}

}
