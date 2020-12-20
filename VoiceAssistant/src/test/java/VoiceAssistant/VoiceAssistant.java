package VoiceAssistant;

import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class VoiceAssistant {

    public static void googleSearch(String search) throws IOException {
        search = search.replaceAll(" ", "+");
		try {
			if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
                Desktop.getDesktop().browse(new URI("https://www.google.com/search?q=" + search));
            }
		} catch(URISyntaxException e) {
		    System.out.println(e);
		}
    }
    
    public static void closeChrome(String search) throws IOException {
    	Runtime.getRuntime().exec("cmd /c start cmd.exe /K \"taskkill /F /IM chrome.exe && taskkill /F /IM cmd.exe\"");
    }

}
