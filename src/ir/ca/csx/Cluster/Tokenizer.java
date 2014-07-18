package ir.ca.csx.Cluster;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Pattern;


public class Tokenizer {

	//Check in whole file
    private Pattern tag = Pattern.compile("<.*>");
    private Pattern puncPattern = Pattern.compile("[,|\\-|\\\\|=|\\\"|@|{|}|#|%|+|~|\\*|;|:|\\[|\\]|?|/|\\(|\\)|\\|]");
    private Pattern fullStopPattern = Pattern.compile("[\\.]");
    
    //Check in each token
    private Pattern exclaPattern = Pattern.compile("!$");
    private Pattern aposPattern = Pattern.compile("'s$");
    private Pattern pluralPattern = Pattern.compile("[']+");
    
    private Pattern nonAlphaPattern = Pattern.compile("[^a-zA-Z]+"); //including numbers
    
    private Set<String> stopWords = new HashSet<String>();
    
    public String[] getTokens(String str) throws IOException {
    	
    	parseStopWords("D:\\CrawlerForIR\\stopwords");
    	
    	str = tag.matcher(str).replaceAll("");
		str = puncPattern.matcher(str).replaceAll(" ");
		str = fullStopPattern.matcher(str).replaceAll("");
		
        String strArr[] = str.split("\\s+");

        ArrayList<String> tokens = new ArrayList<String>();
        for (String t : strArr) {
        	t = exclaPattern.matcher(t).replaceAll("");
        	t = aposPattern.matcher(t).replaceAll("");   
        	t = pluralPattern.matcher(t).replaceAll("");
            
        	if (nonAlphaPattern.matcher(t).find()) {
                continue;
            }            
            if(t.length() > 1)
            {
            	if(!stopWords.contains(t)){
            		tokens.add(t.toLowerCase());
            	}
            }
        }
        
        return tokens.toArray(new String[tokens.size()]);
    }
    
    private void parseStopWords(String path) throws IOException
	{
		BufferedReader br = new BufferedReader(new FileReader(path));
		
        String line;
        while ((line = br.readLine()) != null) {
            stopWords.add(line.trim().toLowerCase());
        }
	}
}
