package pl.testhc.Utility;

import org.bukkit.ChatColor;

public class ChatUtility {
	public static String McColor(String text)
	{
		return ChatColor.translateAlternateColorCodes('&', text);
	}
}
