package libary.renewal.tool;

import org.joda.time.DateTime;
import org.jsoup.Connection.Method;
import org.jsoup.Connection.Response;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Map;


public class Browser {

	private static final String baseUrl = "https://library.aston.ac.uk/";
	private static final String userAgent = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/73.0.3683.86 Safari/537.36";
	private String userName;
	private String password;
	private Map<String, String> cookies;


	public Browser(String userName, String B64password) throws IOException {
		this.userName = userName;
		this.password = new String(Base64.getDecoder().decode(B64password));
		this.login();

	}

	private void login() throws IOException {
		Response res = Jsoup.connect(baseUrl + "patroninfo~S9")
				.userAgent(userAgent)
				.data("extpatid", userName)
				.data("extpatpw", password)
				.data("submit.x", "42")
				.data("submit.y", "16")
				.data("code", "")
				.data("pin", "")
				.method(Method.POST)
				.execute();
		this.cookies = res.cookies();

	}

	public ArrayList<Book> getItems() throws IOException {
		Document doc = Jsoup.connect(baseUrl + "patroninfo~S9/1091346/items")
				.userAgent(userAgent)
				.cookies(this.cookies)
				.get();

		ArrayList<Book> books = new ArrayList<>();
		Elements es = doc.select("tr[class=patFuncEntry]");
		for (Element e : es) {
			books.add(this.parseBook(e));
		}
		return books;

	}

	private Book parseBook(Element htmlBlock) {
		String renewCode = htmlBlock.selectFirst("td[class=patFuncMark]").selectFirst("input").attr("value");
		String title = htmlBlock.selectFirst("td[class=patFuncTitle]").text();
		String barcode = htmlBlock.selectFirst("td[class=patFuncBarcode]").text().trim();
		String[] tokens = htmlBlock.selectFirst("td[class=patFuncStatus]").text().split(" ");

		String[] dateTokens = tokens[1].split("-");
		DateTime dueDate = new DateTime(Integer.parseInt("20" + dateTokens[2]), Integer.parseInt(dateTokens[1]), Integer.parseInt(dateTokens[0]), 0, 0);

		int timesRenewed = Integer.parseInt(tokens[3]);

		String code = htmlBlock.selectFirst("td[class=patFuncCallNo]").text();

		return new Book(title, code, barcode, renewCode, dueDate, timesRenewed);

	}

	public void renewBook(Book book) throws IOException {
		Jsoup.connect(baseUrl + "patroninfo~S9/1091346/items")
				.userAgent(userAgent)
				.data("currentsortorder", "current_duedate")
				.data("renew0", book.getRenewCode())
				.data("renewsome", "YES")
				.cookies(this.cookies)
				.post();

		book.incrementTimesRenewed();

	}

}
