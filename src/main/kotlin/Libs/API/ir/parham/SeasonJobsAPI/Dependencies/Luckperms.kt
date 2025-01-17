package Libs.API.ir.parham.SeasonJobsAPI.Dependencies

import ir.parham.SeasonJobsAPI.Actions.Job
import net.luckperms.api.LuckPermsProvider
import net.luckperms.api.model.group.Group
import net.luckperms.api.model.user.User
import net.luckperms.api.node.Node
import org.bukkit.Bukkit
import java.util.UUID

class Luckperms {
    fun isEnable() : Boolean
    {
        if (Bukkit.getPluginManager().getPlugin("LuckPerms") != null)
        {
            return true
        }
        return false
    }
    fun createGroup(name: String) {
        if (!isEnable())
        {return}
        else
        {
            if (!LuckPermsProvider.get().groupManager.isLoaded(name))
            {
                LuckPermsProvider.get().groupManager.createAndLoadGroup(name)
            }
        }
    }
    fun deleteGroup(name: String) {
        if (!isEnable())
        {return}
        else
        {
            if (LuckPermsProvider.get().groupManager.isLoaded(name))
            {
                val group : Group? = LuckPermsProvider.get().groupManager.getGroup(name)

                if (group != null) {
                    LuckPermsProvider.get().groupManager.deleteGroup(group)
                }
            }
        }
    }
    fun addRank(uuid: UUID, name : String)
    {
        if (!isEnable())
        {return}
        else
        {
            if (LuckPermsProvider.get().groupManager.isLoaded(name)) {
                if (LuckPermsProvider.get().userManager.isLoaded(uuid)) {
                    val user: User = LuckPermsProvider.get().userManager.getUser(uuid)!!
                    user.data().add(Node.builder("group.${name}").build())

                    LuckPermsProvider.get().userManager.saveUser(user)
                }
            }
        }
    }
    fun removeRank(uuid: UUID, name : String)
    {
        if (!isEnable())
        {return}
        else
        {
            if (LuckPermsProvider.get().groupManager.isLoaded(name)) {
                if (LuckPermsProvider.get().userManager.isLoaded(uuid)) {
                    val user: User = LuckPermsProvider.get().userManager.getUser(uuid)!!
                    user.data().remove(Node.builder("group.${name}").build())

                    LuckPermsProvider.get().userManager.saveUser(user)
                }
            }
        }
    }
}