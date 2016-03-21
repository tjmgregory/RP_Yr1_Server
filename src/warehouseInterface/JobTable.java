package warehouseInterface;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import routeExecution.RouteExecution;
import Objects.Job;
import Objects.Sendable.SingleTask;
import jobInput.JobProcessor;

import java.awt.*;
import java.util.Random;

public class JobTable
{
	private static JTable activeJobs;
	private static DefaultTableModel tableModel;
	private static JPanel panel;

	public static JPanel draw(RouteExecution routeExec)
	{
		panel = new JPanel(new BorderLayout());
		tableModel = new DefaultTableModel(new String[] {"Job ID", "Reward", "Robot", "Status"}, 0);
		activeJobs = Display.createTable(tableModel);

		//JButton addJob = new JButton("Add a job");
		//addJob.addActionListener(e -> new AddJob());

		JPopupMenu popupMenu = new JPopupMenu();
		JMenuItem viewInfo = new JMenuItem("Information");
		viewInfo.addActionListener(e -> viewJobInfo((int) activeJobs.getValueAt(activeJobs.getSelectedRow(), 0)));
		JMenuItem cancelJob = new JMenuItem("Cancel this job");
		cancelJob.addActionListener(e -> cancelJob((int) activeJobs.getValueAt(activeJobs.getSelectedRow(), 0), (String) activeJobs.getValueAt(activeJobs.getSelectedRow(), 2), routeExec));
		popupMenu.add(viewInfo);
		popupMenu.add(cancelJob);
		activeJobs.setComponentPopupMenu(popupMenu);
		activeJobs.getColumnModel().getColumn(0).setMaxWidth(60);
		activeJobs.getColumnModel().getColumn(0).setPreferredWidth(60);
		activeJobs.getColumnModel().getColumn(1).setMaxWidth(60);
		activeJobs.getColumnModel().getColumn(1).setPreferredWidth(60);
		activeJobs.getColumnModel().getColumn(2).setMaxWidth(100);
		activeJobs.getColumnModel().getColumn(2).setPreferredWidth(100);

		panel.add(new JScrollPane(activeJobs), BorderLayout.CENTER);
		//panel.add(addJob, BorderLayout.SOUTH);
		return panel;
	}

	public static void addJob(int job, String reward, String robot)
	{
		tableModel.addRow(new Object[] { job, reward, robot, "Running task"});
	}

	private static void cancelJob(int jobID, String robot, RouteExecution routeExec)
	{
		tableModel.removeRow(activeJobs.getSelectedRow());
		RobotTable.updateStatus(robot, "Ready");
		JobProcessor.getJob(jobID).cancel();
		routeExec.initVariables(robot);
		JOptionPane.showMessageDialog(panel, "Job " + jobID + " cancelled.");
	}

	private static void viewJobInfo(int jobID)
	{
		Job job = JobProcessor.getJob(jobID);
		String tasks = "";
		for(SingleTask task : job.getTasks())
			if(!task.getItemID().equals("dropOff"))
				tasks += "(" + task.getItemID() + ", " + task.getQuantity() + ") ";
		JOptionPane.showMessageDialog(panel, "Job ID: " + jobID + "\nTasks: " + tasks + "\nCancellation probability: " + job.getCancellationProb() + "\nTotal weight: " + job.getTotalWeight());
	}
	
	public static void updateStatus(int jobID, String status)
	{
		for(int i = 0; i < tableModel.getRowCount(); i++)
		{
			if((int)(tableModel.getValueAt(i, 0)) == jobID)
			{
				tableModel.setValueAt(status, i, 3);
				break;
			}
		}
	}
}
