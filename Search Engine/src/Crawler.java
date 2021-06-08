import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class Crawler implements Runnable {
    public static final ArrayList<String> URLs = new ArrayList<>();
    public static final HashMap<String, Integer> counts = new HashMap<>();
    public static Integer counter = 0;
    String current;

    public static int countChar(String str, char c) {
        int count = 0;
        for (int i = 0; i < str.length(); i++) {
            if (str.charAt(i) == c)
                count++;
        }
        return count;
    }

    public String normalizeURL(String url) {
        URI temp = URI.create(url).normalize();

        StringBuilder normalizedURL = new StringBuilder();
        if (temp.getScheme() != null && !temp.getScheme().startsWith("http"))
            return "https://www.google.com/";
        if (temp.getScheme() != null)
            normalizedURL = new StringBuilder(temp.getScheme().toLowerCase() + "://");

        if (temp.getHost() != null)
            normalizedURL.append(temp.getHost().toLowerCase());

        String path = temp.getPath();
        if (path != null) {
            if (path.endsWith("/index.html"))
                path = path.replace("/index.html", "/");
            if (path.startsWith("/index") || path.startsWith("/default") || path.equals(""))
                path = "/";
            normalizedURL.append(path);
        }

        if (temp.getQuery() != null && !temp.getQuery().isEmpty())
            normalizedURL.append("?").append(temp.getQuery().toLowerCase());

        return normalizedURL.toString().replaceAll("^(http|https)://[0-9][0-9][0-9].*\r*", "/");
    }

    public void AddURL(String URL) {
        Connection conn = DBManager.getDBConnection();
        assert conn != null;
        URL = normalizeURL(URL);
        if (countChar(URL, '/') > 3)
            return;
        synchronized (counts) {
            URL x = null;
            try {
                x = new URL(current);
            } catch (MalformedURLException e) {
                return;
            }
            String host = x.getHost();
            counts.putIfAbsent(host, 0);
            int temp = counts.get(host);
            if (temp > 50)
                return;
            else
                counts.replace(host, temp + 1);
        }
        try {
            ResultSet result =
                    conn.createStatement().executeQuery("Select * FROM searchengine.urls where URL = \"" + URL + "\"");
            if (!result.next()) {
                conn.createStatement().executeUpdate("INSERT IGNORE INTO searchengine.urls (`URL`) VALUES" +
                        " ('" + URL + "');");
//                System.out.println(URL);
                synchronized (URLs) {
                    URLs.add(URL);
                }
            }
        } catch (SQLException e) {
            System.out.println("Select * FROM searchengine.urls where URL = \"" + URL + "\"");
            System.out.println("INSERT  IGNORE  INTO searchengine.urls (`URL`) VALUES" +
                    " ('" + URL + "');");
        }
    }

    public void UpdateDate(String URL) throws SQLException {
        Connection conn = DBManager.getDBConnection();
        assert conn != null;
        conn.createStatement().executeUpdate("UPDATE searchengine.urls SET crawldate = current_date() WHERE url = '" + URL + "';");
    }

    public void UpdateDefaultDate(String URL) throws SQLException {
        Connection conn = DBManager.getDBConnection();
        assert conn != null;
        conn.createStatement().executeUpdate("UPDATE searchengine.urls SET crawldate = '2001-01-01' WHERE url = '" + URL + "';");
    }

    public static boolean isRobotSafe(String link) throws IOException {
        URL url = new URL(link);
        link = url.getPath();

        String robotlink = url.getProtocol() + "://" + url.getHost() + "/robots.txt";
        Document doc = Jsoup.connect(robotlink)
                .followRedirects(false)
                .userAgent("Mozilla/5.0 (Windows; U; WindowsNT 5.1; en-US; rv1.8.1.6) Gecko/20070725 Firefox/2.0.0.6")
                .referrer("http://www.google.com")
                .get();
        String[] data = doc.wholeText().split("\\r?\\n");

        HashMap<String, Boolean> robotSafe = new HashMap<>();
        boolean begin = false;
        for (String inputLine : data) {
            inputLine = inputLine.toLowerCase();
            if (inputLine.contains("user-agent")) {
                if (inputLine.matches(".*\\s+\\*\\s*$")) {
                    begin = true;
                } else if (begin) {
                    begin = false;
                }
            } else if (begin) {
                if (inputLine.contains("disallow")) {
                    inputLine = inputLine.replace("disallow:", "")
                            .replace(" ", "")
                            .replace("*", ".*");
                    robotSafe.put(inputLine + ".*", false);
                }
            }
        }
        for (String i : robotSafe.keySet()) {
            if (link.matches(i)) {
                return false;
            }
        }
        return true;
    }

    void AddContent(Document doc, String url) throws SQLException {
        Connection conn_searchEngine = DBManager.getDBConnection();
        Connection conn_content = DBManager.getDBConnectionC();
        assert conn_searchEngine != null;
        assert conn_content != null;
        String title = doc.title();
        title = Extract.escapeMetaCharacters(title);
//        System.out.println(title);
        if (title.isEmpty()) {
            title = url;
        }
        conn_searchEngine.createStatement().executeUpdate("UPDATE searchengine.urls SET titles = '"
                + title + "' WHERE url = '" + url + "';");
        conn_content.createStatement().executeUpdate("INSERT IGNORE INTO content.urlcontent (`url`,`content`) " +
                "VALUES ('" + url + "','" + doc.wholeText() + "');");
    }

    @Override
    public void run() {
        while (true) {
            synchronized (URLs) {
                if (!URLs.isEmpty()) {
                    current = URLs.get(0);
                    URLs.remove(current);
                } else
                    continue;
            }
//            System.out.println("inside : " + current);
            try {
                if (!isRobotSafe(current)) {
                    UpdateDefaultDate(current);
                } else {
                    Document doc = Jsoup.connect(current)
                            .userAgent("Mozilla/5.0 (Windows; U; WindowsNT 5.1; en-US; rv1.8.1.6) Gecko/20070725 Firefox/2.0.0.6")
                            .referrer("http://www.google.com")
                            .get();
                    Elements elements = doc.select("a[href]");
                    for (Element link : elements) {
                        AddURL(link.attr("abs:href"));
                    }
                    AddContent(doc, current);
                    UpdateDate(current);
                    synchronized (counter) {
                        counter++;
                        System.out.println("Counter =  " + counter);
                        if(Thread.currentThread().getName().equals(String.valueOf(0)))
                        {
                            Writer wr = new FileWriter("counter.txt");
                            wr.write( String.valueOf(counter) );
                            wr.close();
                        }
                        if (counter > 5000) {
                            return;
                        }
                    }
                }
            } catch (Exception e) {
//                System.out.println("Malformed URL: " + current);
                try {
                    UpdateDefaultDate(current);
                } catch (SQLException throwable) {
                    System.out.println(throwable.getMessage());
                }
            }
        }
    }

    public Crawler() {
        synchronized (URLs) {
            if (URLs.isEmpty()) {
                try {
                    Connection conn = DBManager.getDBConnection();
                    assert conn != null;
                    ResultSet result = conn.createStatement().executeQuery("SELECT * FROM searchengine.urls where crawldate is NULL;");
                    while (result.next()) {
                        URLs.add(result.getString("URL"));
                    }
                    result.close();
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
            }
        }
    }

    public static void main(String[] args) {
        Scanner keyboard = new Scanner(System.in);
        System.out.print("Enter number of threads: ");
        try {
            Scanner scanner = new Scanner(new File("counter.txt"));
            if (scanner.hasNextInt())
                Crawler.counter = scanner.nextInt();
        } catch (Exception ignored) {

        }
        int x = keyboard.nextInt();
        Thread[] threads = new Thread[x];
        int i = 0;
        for (Thread t : threads) {
            t = new Thread(new Crawler());
            t.setName(String.valueOf(i++));
            t.start();
        }
    }
}
