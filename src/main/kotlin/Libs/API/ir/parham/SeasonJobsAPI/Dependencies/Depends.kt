package Libs.API.ir.parham.SeasonJobsAPI.Dependencies

import Libs.API.ir.parham.SeasonJobsAPI.Senders.Logger

class Depends {
    fun check()
    {
        val logger = Logger()
        if (Luckperms().isEnable())
        {
            logger.log("&aLuckPerms found! dependency hook activated.")
        }
        else
        {
            logger.log("&eYou may need to install &aLuckPerms &eto take full avantage of the plugin features.")
        }
        logger.log("&fLuckPerms avantages are listed below:")
        logger.log("&f&l* &7Change player ranks in game.")
        logger.log("&f&l* &7Use luckperms ranks for jobs.")

        if (PlaceholderAPI().isEnable())
        {
            logger.log("&aPlaceholderAPI found! dependency hook activated.")
        }
        else
        {
            logger.log("&eYou may need to install &aPlaceholderAPI &eto take full avantage of the plugin features.")
        }
        logger.log("&fPlaceholderAPI avantages are listed below:")
        logger.log("&f&l* &7Use plugin placeholders in game.")
        logger.log("&f&l* &7See data in game and tab, holograms and scoreboards.")
    }
}