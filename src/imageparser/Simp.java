package imageparser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Simp {
    public static void main(String[] args) {
        try {
            String str = readPageFromUrl("https://www.youtube.com/c/rukremov");
            System.out.println(str);
        }
        catch (IOException e) {
            System.out.println("Ошибка");
        }
        catch (InterruptedException e) {
            System.out.println("Ошибка");
        }
    }

    public static String readPageFromUrl(String strURL) throws IOException, InterruptedException {
        URL pURL = new URL(strURL);

        URLConnection urlCon = (HttpURLConnection) pURL.openConnection();
        urlCon.setConnectTimeout(30000000);
        urlCon.setReadTimeout(30000000);
        urlCon.setRequestProperty("User-Agent", "Mozilla");

        BufferedReader in = new BufferedReader(new InputStreamReader(urlCon.getInputStream()));
        StringBuilder result = new StringBuilder();
        String readLine;
        readLine = in.readLine();
        while (readLine != null) {
            result.append(readLine);

            readLine = in.readLine();
        }
        in.close();
        return result.toString();
    }
}
