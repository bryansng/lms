package ie.ucd.lms.service.admin;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Common {
	public static final int PAGINATION_ROWS = 15;
	public static final int QUICK_SEARCH_ROWS = 5;
	public static final int DAYS_TILL_EXPIRED = 4;
	public static final int DAYS_TILL_RETURNED = 7;
	public static final int DAYS_TO_RENEW = 3;
	public static final int DAYS_TO_LOAN = 7;

	public static DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
	public static DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

	public static String nowDate = LocalDateTime.now().format(dateFormatter);
	public static String nowPlus3Date = LocalDateTime.now().plusDays(3).format(dateFormatter);
	public static String DEFAULT_EXPIRED_ON = LocalDateTime.now().plusDays(DAYS_TILL_EXPIRED).format(dateFormatter);

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
			val = new BigDecimal(str);
		} catch (NumberFormatException e) {
			val = BigDecimal.valueOf(0.0);
		}
		return val;
	}

	public static LocalDateTime convertStringDateToDateTime(String date) {
		return LocalDate.parse(date).atStartOfDay();
	}

	public static LocalDateTime convertStringToDateTime(String dateTime) {
		return LocalDateTime.parse(dateTime);
	}

	public static Integer convertStringToInteger(String str) {
		return Integer.valueOf(str);
	}

	// https://stackoverflow.com/questions/22463062/how-to-parse-format-dates-with-localdatetime-java-8
	public static LocalDateTime toLocalDateTime(String strDateTime) {
		LocalDateTime dateTime = LocalDateTime.parse(strDateTime, dateTimeFormatter);
		return dateTime;
	}

	public static String getStringCurrentLocalDate() {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		return LocalDateTime.now().format(formatter);
	}

	public static LocalDateTime getLowerBoundOfDate(String fromDate) {
		if (fromDate.isEmpty()) {
			// return LocalDateTime.parse(LocalDateTime.MIN.toString(), dateTimeFormatter);
			// return LocalDateTime.MIN;	// does not work for some reason.
			return convertStringDateToDateTime("-9999-01-01");
		}
		return convertStringDateToDateTime(fromDate);
	}

	public static LocalDateTime getUpperBoundOfDate(String toDate) {
		if (toDate.isEmpty()) {
			// return LocalDateTime.parse(LocalDateTime.MAX.toString(), dateTimeFormatter);
			// return LocalDateTime.MAX;	// does not work for some reason.
			return convertStringDateToDateTime("9999-01-01");
		}
		return convertStringDateToDateTime(toDate).plusHours(23).plusMinutes(59).plusSeconds(59);
	}

	// String(daysToLoan) -> String(LocalDate.now() + daysToLoan)
	public static String getStringNowPlusDays(String daysToLoan) {
		return LocalDate.now().plusDays(Long.parseLong(daysToLoan)).format(dateFormatter);
	}
}