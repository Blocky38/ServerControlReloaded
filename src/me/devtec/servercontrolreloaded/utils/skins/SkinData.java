package me.devtec.servercontrolreloaded.utils.skins;

import java.util.HashMap;
import java.util.UUID;

import javax.annotation.Nonnull;

import me.devtec.theapi.utils.json.Json;

public class SkinData {
	
	public String skinName;
	
	public UUID uuid;
	@Nonnull
	public String value;
	@Nonnull
	public String signature;
	
	public String url;
	public boolean slim;
	
	public long lastUpdate = System.currentTimeMillis()/1000;
	
	public boolean isFinite() {
		return value != null && signature != null;
	}
	
	public String toString() {
		HashMap<String, String> data = new HashMap<>();
		data.put("uuid", uuid.toString());
		data.put("texture.name", skinName);
		data.put("texture.value", value);
		data.put("texture.signature", signature);
		data.put("texture.url", url);
		data.put("texture.slim", slim+"");
		data.put("texture.lastUpdate", lastUpdate+"");
		return Json.writer().simpleWrite(data);
	}
}
