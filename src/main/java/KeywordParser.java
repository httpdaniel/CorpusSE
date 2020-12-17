import java.io.*;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class KeywordParser {
    public static void main(String Args[]) throws IOException {
        ArrayList<String> topicsList = new ArrayList<>();
        BufferedReader bufferedReader = null;
        BufferedWriter bufferedWriter = null;

        String fileName = "corpus/topics";
        String keywordFileName = "corpus/keywords";
        try {
            bufferedReader = new BufferedReader(new FileReader(fileName));
            bufferedWriter = new BufferedWriter(new FileWriter(keywordFileName));


        } catch (Exception e) {
            System.out.println("The path to the Query File  maybe wrong, please check again.");
            System.exit(0);
        }
        int j = 1;

        String line = bufferedReader.readLine();
        String keywords = "";
        Integer i = 1;
        while (line != null) {
            if(line.startsWith(("<title>"))){
                keywords += line.substring(7) + " ";
                line = bufferedReader.readLine();
                System.out.println(i);
                i++;
            }
            line = bufferedReader.readLine();
        }
        keywords = keywords.replace(",","");
        keywords = keywords.replace("'","");
        String regex = "\\s+";
        //Compiling the regular expression
        Pattern pattern = Pattern.compile(regex);
        //Retrieving the matcher object
        Matcher matcher = pattern.matcher(keywords.trim());
        //Replacing all space characters with single space
        keywords = matcher.replaceAll(",");
        bufferedWriter.write(keywords);
        bufferedReader.close();
        bufferedWriter.close();


    }
}
