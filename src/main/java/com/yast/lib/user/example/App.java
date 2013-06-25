package com.yast.lib.user.example;

import java.util.ArrayList;

import com.yast.lib.user.Yast;
import com.yast.lib.user.YastDataObject;
import com.yast.lib.user.YastFolder;
import com.yast.lib.user.YastProject;
import com.yast.lib.user.YastRecord;
import com.yast.lib.user.YastRecordWork;
import com.yast.lib.user.YastUserInfo;
import com.yast.lib.user.exceptions.YastLibApiException;
import com.yast.lib.user.exceptions.YastLibBadResponseException;
import com.yast.lib.user.exceptions.YastLibNotLoggedInException;

/**
 * Hello Yast!
 * 
 */
public class App {
	public static void main(String[] args) throws YastLibBadResponseException,
			YastLibApiException {
		
		Yast yast;

		String username = "USERNAME";
		String password = "PASSWORD";

		// Login
		yast = new Yast();
		yast.login(username, password);

		// Fetch user info
		YastUserInfo info = yast.getUserInfo();
		App.log("Logged in as: " + info.getName() + " (" + info.getId() + ")");

		// Fetch projects
		ArrayList<YastProject> projects = yast.getProjects();
		ArrayList<YastFolder> folders = yast.getFolders();
		App.log("Fetched " + projects.size() + " projects and "
				+ folders.size() + " folders");

		// Add a project
		YastProject project = new YastProject("Test project");
		project.setInternalId(555); // local id, can be used to keep track of
									// objects not already synced with Yast.com
		App.log("Project local id: " + project.getInternalId() + " remote id: "
				+ project.getId());
		yast.add(project);
		App.log("Project local id: " + project.getInternalId() + " remote id: "
				+ project.getId()); // The remote id will be updated

		// Change the project
		project.setName("Changed proejct name");
		yast.change(project);

		// Add a record
		int startTime = (int) (System.currentTimeMillis() / 1000 - 3600);
		int endTime = (int) (System.currentTimeMillis() / 1000 - 0);
		String comment = "Doing homework";
		boolean isRunning = false;

		YastRecordWork record = new YastRecordWork();
		record.setStartTime(startTime);
		record.setEndTime(endTime);
		record.setComment(comment);
		record.setProject(project);
		record.setRunning(isRunning);

		yast.add(record);
		App.log("Record local id: " + record.getInternalId() + " remote id: "
				+ record.getId()); // The remote id will be updated
		App.log("Record duration is: "
				+ (record.getEndTime() - record.getStartTime()));

		// Change the record
		record.setStartTime((int) (System.currentTimeMillis() / 1000 - 6000));
		yast.change(record);
		App.log("Record duration is: "
				+ (record.getEndTime() - record.getStartTime()));

		// Get all records for the last week
		ArrayList<YastRecord> recordsLastWeek = yast.getRecords(
				(int) (System.currentTimeMillis() / 1000 - 3600 * 24 * 7),
				(int) (System.currentTimeMillis() / 1000));
		App.log("Fetched " + recordsLastWeek.size()
				+ " records for the last week");

		// Get all records for the last week, for a specific project
		int from = (int) (System.currentTimeMillis() / 1000 - 3600 * 24 * 7);
		int to = (int) (System.currentTimeMillis() / 1000);

		ArrayList<Integer> projectIds = new ArrayList<Integer>();
		projectIds.add(project.getId());

		ArrayList<YastRecord> recordsProject = yast.getRecords(null, null,
				projectIds, from, to);
		App.log("Fetched " + recordsProject.size()
				+ " records for the last week for project id "
				+ project.getId());

		// Get a specific record
		ArrayList<Integer> ids = new ArrayList<Integer>();
		ids.add(record.getId());

		ArrayList<YastRecord> recordsSingle = yast.getRecords(null, ids, null,
				from, to);
		App.log("Fetched record " + recordsSingle.get(0).getId()
				+ " from server");

		// Delete entries
		yast.delete(record);
		yast.delete(project);
	}

	public static void log(String s) {
		System.out.println(s);
	}
}
