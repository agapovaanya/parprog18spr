package check;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.StringTokenizer;

public class Check {
    
    static class Scanner {
        BufferedReader br;
        StringTokenizer st;
        boolean end = false;

        boolean hasNext() { 
        	return !end;
        }
        Scanner(File f) {
            try {
                br = new BufferedReader(new FileReader(f));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }

        String next() {
            while (st == null || !st.hasMoreTokens()) {
                try {
                    st = new StringTokenizer(br.readLine());
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (NullPointerException e) {
                	end = true;
                	return "";
                }
            }
            return st.nextToken();
        }
    }

	public static void main(String... args) {
		
		Scanner f = new Scanner(new File("example.txt"));
		List<String> file = new ArrayList<String>();
		while (f.hasNext()) {
			file.add(f.next());
		}
		Random r = new Random();
		int length = r.nextInt(file.size()) + 1;
		
		String answer = "";
		for (int i = 0; i < length; i++) {
			int id = r.nextInt(file.size());
			answer += file.get(id);
		}
		System.out.println(answer);

	}

}