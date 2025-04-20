package ir.parham.seasonJob.Commands.Default;

import Libs.API.ir.parham.SeasonJobsAPI.Commands;
import Libs.API.ir.parham.SeasonJobsAPI.Senders.Message;
import ir.parham.SeasonJobsAPI.Actions.Job;
import ir.parham.SeasonJobsAPI.Actions.Member;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.UUID;

public class SaveAll implements Commands
{

    @Override
    public void runner(@NotNull CommandSender sender, @NotNull String[] args) {
        Message message = new Message();

        if (sender.hasPermission("seasonjobs.save-all"))
        {
            sender.sendMessage(message.get(Bukkit.getOfflinePlayer(UUID.randomUUID()), "save-all-process"));
            new Member().saveAll();
            new Job().saveAll();
            sender.sendMessage(message.get(Bukkit.getOfflinePlayer(UUID.randomUUID()), "save-all-finished"));
        }
        else
        {
            // deny perm
            sender.sendMessage(message.get(Bukkit.getOfflinePlayer(UUID.randomUUID()), "denyPerm"));
        }
    }

    @Override
    public @NotNull List<String> completer(@NotNull CommandSender sender, @NotNull String[] args) {
        return List.of();
    }

    @Override
    public void remover(@NotNull OfflinePlayer target) {

    }

    @Override
    public void adder(@NotNull OfflinePlayer target) {

    }

    @Override
    public void setter(@NotNull OfflinePlayer target) {

    }
}
