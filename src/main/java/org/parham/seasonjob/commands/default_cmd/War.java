package org.parham.seasonjob.commands.default_cmd;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.parham.seasonjob.commands.SeasonCommand;
import org.parham.seasonjob.data.job.Job;
import org.parham.seasonjob.data.job.JobManager;
import org.parham.seasonjob.data.sender.Messages;

import java.util.List;

public class War implements SeasonCommand {
    // war <start/stop> <job1> <job2>
    @Override
    public boolean hasPermission() {
        return true;
    }

    @Override
    public boolean isPlayerOnly() {
        return false;
    }

    @Override
    public String getPermission() {
        return "seasonjob.war";
    }

    @Override
    public int getNeedArguments() {
        return 3;
    }

    @Override
    public String getUsage() {
        return Messages.getMessage("usage-war");
    }

    @Override
    public void execute(CommandSender sender, Command cmd, String label, String[] args, boolean isLeader, boolean hasAccess) {
        Job job1 = JobManager.getJob(args[1]);
        Job job2 = JobManager.getJob(args[2]);
        String action = args[0].toLowerCase();

        if (!action.equals("start") && !action.equals("stop")) {
            sender.sendMessage(getUsage());
        } else if (job1 == null) {
            sender.sendMessage(Messages.getMessage("1st-job-not-found-war"));
        } else if (job2 == null) {
            sender.sendMessage(Messages.getMessage("2nd-job-not-found-war"));
        } else if ((isLeader && !hasAccess) || (!isLeader && hasPermission() && !sender.hasPermission(getPermission() + "." + action + "." + job1.getName().toLowerCase()))) {
            sender.sendMessage(Messages.getMessage("deny-permission-war"));
        } else if (job1.getName().equals(job2.getName())) {
            sender.sendMessage(Messages.getMessage("self-war"));
        } else if (job1.getWars().contains(job2.getName()) && action.equals("start")) {
            sender.sendMessage(Messages.getMessage("was-in-war"));
        } else if (!job1.getWars().contains(job2.getName()) && action.equals("stop")) {
            sender.sendMessage(Messages.getMessage("was-not-in-war"));
        } else {
            executeInit(sender, action, job1, job2);
        }
    }

    private void executeInit(CommandSender sender, String action, Job job1, Job job2) {
        if (action.equals("start")) {
            startWar(sender, job1, job2);
        } else if (action.equals("stop")) {
            stopWar(sender, job1, job2);
        }
    }

    private void startWar(CommandSender sender, Job job1, Job job2) {
        if (job1.getPeaces().contains(job2.getName())) {
            sender.sendMessage(Messages.getMessage("peace-error-war"));
        } else {
            job1.addWar(job2);
            job2.addWar(job1);

            sender.sendMessage(Messages.getMessage("start-war"));
        }
    }

    private void stopWar(CommandSender sender, Job job1, Job job2) {
        job1.removeWar(job2);
        job2.removeWar(job1);

        sender.sendMessage(Messages.getMessage("stop-war"));
    }

    @Override
    public List<String> getCompletions(CommandSender sender, String[] args) {
        if (args.length == 2) {
            return List.of("start", "stop");
        } else if (args.length == 3) {
            return JobManager.getJobsList().stream().toList();
        } else if (args.length == 4) {
            return JobManager.getJobsList().stream().toList();
        }  else {
            return List.of("");
        }
    }
}
