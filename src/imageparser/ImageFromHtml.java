package imageparser;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class ImageFromHtml {

    String pathTo;

    public ImageFromHtml() {

    }

    public ImageFromHtml(String pathTo) {
        this.pathTo = pathTo;
    }

    public String readPageFromUrl(String strURL) throws IOException, InterruptedException {
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
            Iterator<String> iterator = getImage(readLine).iterator();
            while (iterator.hasNext()) {
                downloadFromULR("example", iterator.next());
            }
            readLine = in.readLine();
        }
        in.close();
        return result.toString();
    }

    private void downloadFromULR(String nameDir, String path) {
        try {
            // создаем объект URL по пути
            URL url = new URL(path);
            // получаем чтение с URL
            InputStream in = new BufferedInputStream(url.openStream());
            // инициаризируем объект чтения байтов
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            // создаем буфер
            byte[] buf = new byte[1024];
            int n = 0;
            // читаем с InputStream, записываем в буфер, и затем записываем в bytearray
            while (-1 != (n = in.read(buf))) {
                out.write(buf, 0, n);
            }
            // закрываем
            out.close();
            in.close();
            // получаем с bytearray байты и передаем в массив байтов
            byte[] response = out.toByteArray();
            new File(nameDir + "/").mkdir(); // создание папки
            // создаем объект
            FileOutputStream fos = new FileOutputStream(nameDir + "/" + path.split("/")[path.split("/").length - 1]);
            // записываем в файл полученные байты
            fos.write(response);
            fos.close();
            System.out.println("Скачано - " + path);
        } catch (Exception e) {
            System.out.println("Не удалось скачать - " + path);
        }
    }

    private Set<String> getImage(String url) {
        Set<String> images = new HashSet<String>();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < url.length() - 5; i++) {
            sb.setLength(0);
            if (url.charAt(i) == '\"' && url.charAt(i + 1) == 'u' && url.charAt(i + 2) == 'r' && url.charAt(i + 3) == 'l' && url.charAt(i + 4) == '\"') {
                for (int j = i + 7; j < url.length() - 3; j++) {
                    if (url.charAt(j) == 'j' && url.charAt(j + 1) == 'p' && url.charAt(j + 2) == 'g') {
                        for (int k = i + 7; k < j - 1; k++) {
                            sb.append(url.charAt(k));
                        }
                        i = j + 3;
                        sb.append(".jpg");
                        images.add(sb.toString());
                        break;
                    } else if (url.charAt(j) == '\"' && (url.charAt(j + 1) == ',' || url.charAt(j + 1) == '}')) {
                        i = j + 2;
                        break;
                    }
                }
            }
        }
        return images;
    }

    public static void main(String[] argc) {
        try {
            new ImageFromHtml().readPageFromUrl("https://www.youtube.com/c/rukremov");
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
