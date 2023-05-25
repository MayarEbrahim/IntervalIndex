import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.HashMap;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Scanner;

class Posting {
    public Posting next;
    public int docId;
    public int dtf;

    public Posting() {
        next = null;
        docId = 0;
        dtf = 1;
    }
}

class DictEntry {
    public int doc_freq;
    public int term_freq;
    public Posting pList;

    public DictEntry() {
        doc_freq = 0;
        term_freq = 0;
        pList = null;
    }
}
public class InvertedIndex {
    private HashMap<String, DictEntry> index;

    public InvertedIndex() {
        index = new HashMap<>();
    }

    public void buildIndex(String[] filenames) {
        for (String filename : filenames) {
            try {
                File file = new File(filename);
                BufferedReader reader = new BufferedReader(new FileReader(file));
                String line;
                int docId = Integer.parseInt(file.getName().split("\\.")[0]);
                while ((line = reader.readLine()) != null) {
                    String[] words = line.split("\\s+");
                    for (String word : words) {
                        if (!index.containsKey(word)) {
                            index.put(word, new DictEntry());
                        }
                        DictEntry entry = index.get(word);
                        entry.term_freq++;
                        if (entry.pList == null || entry.pList.docId != docId) {
                            Posting posting = new Posting();
                            posting.docId = docId;
                            entry.doc_freq++;
                            posting.next = entry.pList;
                            entry.pList = posting;
                        } else {
                            entry.pList.dtf++;
                        }
                    }
                }
                reader.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void search(String word) {
        if (index.containsKey(word)) {
            DictEntry entry = index.get(word);
            System.out.println("Term frequency: " + entry.term_freq);
            System.out.println("Document frequency: " + entry.doc_freq);
            System.out.println("Posting list:");
            Posting posting = entry.pList;
            while (posting != null) {
                System.out.println(posting.docId + ": " + posting.dtf);
                posting = posting.next;
            }
        } else {
            System.out.println("Term not found.");
        }
    }

    public static void main(String[] args) {
        String[] filenames = {"1.txt", "2.txt", "3.txt", "4.txt", "5.txt",
                "6.txt", "7.txt", "8.txt", "9.txt", "10.txt"};
        InvertedIndex index = new InvertedIndex();
        index.buildIndex(filenames);
        Scanner sc=new Scanner(System.in);
        System.out.println("Enter the word:");
        String inputWord=sc.nextLine();
        index.search(inputWord);
    }
}
