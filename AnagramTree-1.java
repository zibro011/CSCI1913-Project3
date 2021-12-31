//
//  WORDS. An iterator that reads lower case words from a text file.
//
//    James Moen
//    19 Apr 17
//

import java.io.FileReader;   //  Read Unicode chars from a file.
import java.io.IOException;  //  In case there's IO trouble.

//  WORDS. Iterator. Read words, represented as STRINGs, from a text file. Each
//  word is the longest possible contiguous series of alphabetic ASCII CHARs.

class Words
{
  private int           ch;      //  Last CHAR from READER, as an INT.
  private FileReader    reader;  //  Read CHARs from here.
  private StringBuilder word;    //  Last word read from READER.

//  Constructor. Initialize an instance of WORDS, so it reads words from a file
//  whose pathname is PATH. Throw an exception if we can't open PATH.

  public Words(String path)
  {
    try
    {
      reader = new FileReader(path);
      ch = reader.read();
    }
    catch (IOException ignore)
    {
      throw new IllegalArgumentException("Cannot open '" + path + "'.");
    }
  }

//  HAS NEXT. Try to read a WORD from READER, converting it to lower case as we
//  go. Test if we were successful.

  public boolean hasNext()
  {
    word = new StringBuilder();
    while (ch > 0 && ! isAlphabetic((char) ch))
    {
      read();
    }
    while (ch > 0 && isAlphabetic((char) ch))
    {
      word.append(toLower((char) ch));
      read();
    }
    return word.length() > 0;
  }

//  IS ALPHABETIC. Test if CH is an ASCII letter.

  private boolean isAlphabetic(char ch)
  {
    return 'a' <= ch && ch <= 'z' || 'A' <= ch && ch <= 'Z';
  }

//  NEXT. If HAS NEXT is true, then return a WORD read from READER as a STRING.
//  Otherwise, return an undefined STRING.

  public String next()
  {
    return word.toString();
  }

//  READ. Read the next CHAR from READER. Set CH to the CHAR, represented as an
//  INT. If there are no more CHARs to be read from READER, then set CH to -1.

  private void read()
  {
    try
    {
      ch = reader.read();
    }
    catch (IOException ignore)
    {
      ch = -1;
    }
  }

//  TO LOWER. Return the lower case ASCII letter which corresponds to the ASCII
//  letter CH.

  private char toLower(char ch)
  {
    if ('a' <= ch && ch <= 'z')
    {
      return ch;
    }
    else
    {
      return (char) (ch - 'A' + 'a');
    }
  }

//  MAIN. For testing. Open a text file whose pathname is the 0th argument from
//  the command line. Read words from the file, and print them one per line.

  public static void main(String [] args)
  {
    Words words = new Words(args[0]);
    while (words.hasNext())
    {
      System.out.println("'" + words.next() + "'");
    }
  }
}

public class AnagramTree
{
  private TreeNode root;

  private class TreeNode
  {
    private byte summary[];
    private WordNode words;
    private TreeNode left;
    private TreeNode right;

    private TreeNode(byte summary[], WordNode words, TreeNode left, TreeNode right)
    {
      this.summary = summary;
      this.words = words;
      this.left = left;
      this.right = right;
     }
  }
  private class WordNode
  {
    private String word;
    private WordNode next;
    private WordNode(String word, WordNode next)
    {
      this.word = word;
      this.next = next;
    }
  }
  public AnagramTree()
  {
    root = new TreeNode(stringToSummary(""), null, null, null);
  }
  public void add(String word)
  {
    TreeNode current = root;
    byte summary[] = stringToSummary(word);
    while (true)
    {
      int temp = compareSummaries(summary, current.summary);
      if (temp < 0)
      {
        if (current.left == null)
        {
          current.left = new TreeNode(summary, new WordNode(word, null), null, null);
          return;
        }
        else
        {
          current = current.left;
        }
      }
      else if (temp > 0)
      {
        if (current.right == null)
        {
          current.right = new TreeNode(summary, new WordNode(word, null), null, null);
          return;
        }
        else
        {
          current = current.right;
        }
      }
      else
      {
        WordNode t = current.words;
        if (t.word.equals(word))
        {
          return;
        }
        while (t.next != null)
        {
          t = t.next;
          if (t.word.equals(word))
          {
            return;
          }
        }
        t.next = new WordNode(word, null);
        return;
      }
    }
  }
  public void anagrams()
  {
    traverse(root.right);
  }
public void traverse(TreeNode n)
{
  if (n == null)
  {
    return;
  }
  if (n.words.next != null)
  {
    WordNode w = n.words;
    System.out.print("\n");
    while (w != null)
    {
      System.out.print(" "+ w.word);
      w = w.next;
    }
  }
  traverse(n.left);
  traverse(n.right);
}
private int compareSummaries(byte[] left, byte[] right)
{
  for (int i = 0; i < left.length; i++)
  {
    if (left[i] != right[i])
    {
      return left[i] - right[i];
    }
    else
    {
      continue;
    }
  }
return 0;
}
private byte[] stringToSummary(String word)
{
  byte summary[] = new byte[26];
  for (int c = 0; c < word.length(); c++)
  {
    summary[word.charAt(c) - 'a']++;
  }
  return summary;
}
}

public class Anagrammer
{
  public static void main(String[] args)
  {
    AnagramTree anagram = new AnagramTree();
    Words words = new Words("warAndPeace.txt");
    while (words.hasNext())
    {
      anagram.add(words.next());
    }
    anagram.anagrams();
    System.out.println("\n");
  }
}
