package chu.engine;

import java.util.HashMap;
import java.util.Scanner;

import org.newdawn.slick.openal.Audio;
import org.newdawn.slick.openal.AudioLoader;
import org.newdawn.slick.util.ResourceLoader;

import chu.engine.anim.BitmapFont;

public class Resources {
	
	private static HashMap<String, Audio> audio;
	private static HashMap<String, BitmapFont> bitmapFonts;
	
	static {
		audio = new HashMap<String, Audio>();
		bitmapFonts = new HashMap<String, BitmapFont>();
	}
	
	public static BitmapFont getBitmapFont(String name) {
		return bitmapFonts.get(name);
	}
	
	public static Audio getAudio(String name) {
		Audio a = audio.get(name);
		if(a == null) {
//			System.err.println("Warn: " + name + " not explicitly defined");
			try{
				Audio b = AudioLoader.getAudio("WAV",
						ResourceLoader.getResourceAsStream("res/sfx/"+name+".wav"));
				audio.put(name, b);
				return b;
			} catch (Exception e){
				return null;
			}
		} else {
			return a;
		}
	}
	
	public static void loadBitmapFonts() {
		Scanner in = new Scanner(ResourceLoader.getResourceAsStream("res/fonts/fonts.txt"));
		while(in.hasNextLine()) {
			String line = in.nextLine();
			if(line.startsWith("#"))
				continue;
			if(line.startsWith("define")) {
				String name = line.split(":")[1];
				String texName = in.nextLine();
				char[] chars = in.nextLine().toCharArray();
				int height = Integer.parseInt(in.nextLine());
				int spacing = Integer.parseInt(in.nextLine());
				char[] widths = in.nextLine().toCharArray();
				
				BitmapFont font = new BitmapFont(texName);
				font.setHeight(height);
				font.setSpacing(spacing);
				int pos = 0;
				for(int i=0; i<chars.length; i++) {
					int width = Integer.parseInt(widths[i]+"");
					font.put(chars[i], pos, width);
					pos += width;
				}
				bitmapFonts.put(name, font);
				System.out.println(name+"(bitmap font) loaded");
			}
		}
		in.close();
	}
	
}
