package ie.ucd.lms.service.admin;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Common {
	public static int PAGINATION_ROWS = 10;

	public static Long convertStringToLong(String str) {
		Long val;
		try {
			val = Long.valueOf(str);
		} catch (NumberFormatException e) {
			val = Long.valueOf(0);
		}
		return val;
	}

	public static BigDecimal convertStringToBigDecimal(String str) {
		BigDecimal val;
		try {
			val = BigDecimal.valueOf(convertStringToLong(str));
		} catch (NumberFormatException e) {
			val = BigDecimal.valueOf(0.0);
		}
		return val;
	}

	public static LocalDateTime convertStringDateToDateTime(String date) {
		return LocalDate.parse(date).atStartOfDay();
	}

	public static Integer convertStringToInteger(String str) {
		return Integer.valueOf(str);
	}

	// https://stackoverflow.com/questions/22463062/how-to-parse-format-dates-with-localdatetime-java-8
	public static LocalDateTime toLocalDateTime(String strDateTime) {
		// DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		LocalDateTime dateTime = LocalDateTime.parse(strDateTime, formatter);
		return dateTime;
	}
}