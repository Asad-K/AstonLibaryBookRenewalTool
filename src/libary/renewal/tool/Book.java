package libary.renewal.tool;

import org.joda.time.DateTime;
import org.joda.time.Duration;
import org.joda.time.Instant;


public class Book {
	private String title;
	private String renewCode;
	private String code;
	private String barcodeNum;
	private int timesRenewed;
	private int daysLeft;

	public Book(String title, String code, String barcodeNum, String renewCode,DateTime dueDate, int timesRenewed) {
		this.title = title;
		this.code = code;
		this.barcodeNum = barcodeNum;
		this.timesRenewed = timesRenewed;
		this.renewCode = renewCode;
		this.daysLeft = (new Duration(new Instant(), dueDate)).toStandardDays().getDays();

	}

	public void incrementTimesRenewed(){
		this.timesRenewed++;
	}

	public int getDaysLeft(){
		return this.daysLeft;
	}

	public String getRenewCode() {
		return renewCode;
	}

	@Override
	public String toString() {
		return "Title: " + this.title
				+ " | Times Renewed: "  + this.timesRenewed
				+ " | Days Left: " + this.daysLeft
				+ " | Barcode: " + this.barcodeNum
				+ " | Book Code: " + this.code;
	}
}
