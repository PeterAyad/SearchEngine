import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Scanner;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;


public class Indexer {
    private static ArrayList<String> listOfWords;
    private static ArrayList<String> listOfWordsP;
    private static int countOfWords;
    private static String content;
    private static int tCountOfWords;
    public static void main(String[] args) throws SQLException, IOException {
        tCountOfWords=0;
        IndexerDB.open();
        Scanner s = new Scanner(System.in);
        System.out.println("Start over indexing ? enter y if yes");
        String startOver=s.nextLine();
        if(startOver.equals("y")){
            IndexerDB.startOver();
        }
        s.close();
        System.out.println("Started Indexing");
        String URL;
        int count=0;
        long startTime = System.currentTimeMillis();
        while ((URL = IndexerDB.getNonIndexedURL()) != null) {
            System.out.println("Started Parsing "+URL);
            parsePAGE(URL);
            System.out.println("Finished Parsing "+URL);
            if (countOfWords > 0) {
                System.out.printf("started indexing page %d%n",++count);
                LinkedHashMap<String, Double> TF = IndexerDB.calcTF(listOfWords,listOfWordsP,countOfWords);
                IndexerDB.indexWords(TF, URL);
            }
            IndexerDB.updateURL(URL);
            System.out.printf("finished indexing page %d%n", count);
        }
        IndexerDB.clean();
        long endTime = System.currentTimeMillis();
        System.out.printf("Finished Indexing %d words at %d %n",tCountOfWords,endTime-startTime);
        IndexerDB.close();
    }
    private static void parsePAGE(String url) throws IOException {
        try {
            countOfWords = 0;
            content = Extract.escapeMetaCharacters(IndexerDB.getDocument(url));
            if(content.isEmpty()){
                return;
            }
            listOfWords = Extract.splitSentence(content);
            listOfWordsP=listOfWords;
            listOfWords = Extract.removeStoppingWords(listOfWords);
            countOfWords += listOfWords.size();
            tCountOfWords += countOfWords;
        }catch(Exception e){
            System.out.println("error occurred in parse page");
        }
    }
}