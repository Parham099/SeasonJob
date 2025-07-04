package org.parham.seasonjob.commands.default_cmd;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.parham.seasonjob.commands.SeasonCommand;
import org.parham.seasonjob.data.member.MemberManager;
import org.parham.seasonjob.data.sender.Messages;

import java.util.*;

public class Accept implements SeasonCommand {
    @Override
    public boolean hasPermission() {
        return false;
    }

    @Override
    public boolean isPlayerOnly() {
        return true;
    }

    @Override
    public String getPermission() {
        return "seasonjob.accept";
    }

    @Override
    public int getNeedArguments() {
        return 0;
    }

    @Override
    public String getUsage() {
        return "";
    }

    @Override
    public void execute(CommandSender sender, Command cmd, String label, String[] args, boolean isLeader, boolean hasAccess) {
        Player player = (Player) sender;
        if (!Invite.invites.containsKey(player.getUniqueId())) {
            player.sendMessage(Messages.getMessage("doesnt-have-invite-accept"));
        } else if (MemberManager.contains(player.getUniqueId())) {
            player.sendMessage(Messages.getMessage("employed-accept"));
        } else {
            Map<UUID, ArrayList<String>> invites = Invite.invites;

            String job;

            if (args.length > 0 && invites.get(player.getUniqueId()).contains(args[0])) {
                job = args[0];
            } else {
                job = invites.get(player.getUniqueId()).get(invites.get(player.getUniqueId()).size() - 1);
            }

            MemberManager.add(player.getUniqueId(), job);
            player.sendMessage(Messages.getMessage("accepted-accept"));

            Invite.invites.remove(player.getUniqueId());
        }
    }

    @Override
    public List<String> getCompletions(CommandSender sender, String[] args) {
        if (sender instanceof Player) {
            return Invite.invites.get(((Player) sender).getUniqueId());
        } else {
            return Collections.emptyList();
        }
    }
}
