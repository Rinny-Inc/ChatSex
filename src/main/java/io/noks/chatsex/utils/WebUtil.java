package io.noks.chatsex.utils;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;

import org.bukkit.plugin.java.JavaPlugin;

import io.noks.chatsex.interfaces.WebCallback;

// SATHONAY DID THIS
public abstract class WebUtil {
	
	public void getResponse(JavaPlugin plugin, String urlString, WebCallback callback) {
	    plugin.getServer().getScheduler().runTaskAsynchronously(plugin, () -> {
	        try {
	            URI uri = new URI(urlString);
	            HttpURLConnection connection = (HttpURLConnection) uri.toURL().openConnection();
	            connection.setRequestMethod("GET");

	            try (BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
	                StringBuilder response = new StringBuilder();
	                String line;
	                while ((line = reader.readLine()) != null) {
	                    response.append(line);
	                }
	                callback.callback(response.toString());
	            }
	        } catch (Exception ex) {
	            ex.printStackTrace();
	        }
	    });
	}
}