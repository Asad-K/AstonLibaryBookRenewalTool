import libary.renewal.tool.Book;
import libary.renewal.tool.Browser;

import java.io.IOException;
import java.util.ArrayList;

public class Main {

	public static void main(String... args) throws IOException {
		Browser browser = new Browser("your user strudent number here", "Your pass here in base64 here");
		ArrayList<Book> books = browser.getItems();
		for (Book b : books) {
			System.out.println(b);
			
			if (b.getDaysLeft() <= 3) {
				System.out.println("Renewing Book");
				browser.renewBook(b);
			}
		}
	}

}
