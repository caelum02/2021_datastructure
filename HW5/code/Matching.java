import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.SQLOutput;

public class Matching
{
	private static PatternDB patternDB = new PatternDB();

	public static void main(String[] args)
	{
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

		while (true)
		{
			try
			{
				String input = br.readLine();
				if (input.compareTo("QUIT") == 0)
					break;

				command(input);
			}
			catch (IOException e)
			{
				System.out.println("입력이 잘못되었습니다. 오류 : " + e.toString());
			}
		}
	}

	private static void command(String input) throws IOException{
		// TODO : 아래 문장을 삭제하고 구현해라.
		char commandType = input.charAt(0);

		if (commandType == '<') {
			patternDB = new PatternDB();
			saveFile(input.substring(2));
		}

		else if (commandType == '@') {
			print(Integer.parseInt(input.substring(2)));
		}

		else if (commandType == '?') {
			search(input.substring(2));
		}
	}

	private static void saveFile(String path) throws IOException {
		String content = Files.readString(Path.of(path));
		String[] strings = content.split("\n");

		for (int i=0; i<strings.length; i++) {
			String s = strings[i]; int len = s.length();
			for(int j=0; j<len-5; j++)
				patternDB.save(new Pattern(s.substring(j, j+6)), new Location(i+1, j+1));
		}
	}

	private static void print(int index) {
		patternDB.printSlot(index);
	}

	private static void search(String pattern) {
		LinkedList<Location> locations = patternDB.search(pattern);
		if (locations.isEmpty()) System.out.println("(0, 0)");
		else System.out.println(locations);
	}
}
