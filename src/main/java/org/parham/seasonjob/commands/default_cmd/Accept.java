package org.parham.seasonjob.commands.default_cmd;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.parham.seasonjob.commands.SeasonCommand;
import org.parham.seasonjob.data.member.MemberManager;
import org.parham.seasonjob.data.sender.Messages;

import java.util.ArrayList;
import java.util.List;

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
        return "";
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
    public void execute(CommandSender sender, Command cmd, String label, String[] args) {
        Player player = (Player) sender;
        if (!Invite.invites.containsKey(player.getUniqueId())) {
            player.sendMessage(Messages.getMessage("doesnt-have-invite-accept"));
        } else if (MemberManager.contains(player.getUniqueId())) {
            player.sendMessage(Messages.getMessage("employed-accept"));
        } else {
            String job = Invite.invites.get(player.getUniqueId());

            MemberManager.add(player.getUniqueId(), job);
            player.sendMessage(Messages.getMessage("accepted-accept"));

            Invite.invites.remove(player.getUniqueId());
        }
    }

    @Override
    public List<String> getCompletions(String[] args) {
        return new ArrayList<>();
    }
}
