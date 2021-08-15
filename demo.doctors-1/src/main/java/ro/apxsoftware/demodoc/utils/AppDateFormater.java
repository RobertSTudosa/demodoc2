package ro.apxsoftware.demodoc.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Pattern;

import org.springframework.stereotype.Service;

@Service
public class AppDateFormater {
	

	private static Pattern DATE_PATTERN = Pattern.compile(
		      "^(?:(?:1[6-9]|[2-9]\\d)?\\d{2})(?:(?:(\\/|-|\\.)(?:0?[13578]|1[02])\\1(?:31))|(?:(\\/|-|\\.)(?:0?[13-9]|1[0-2])\\2(?:29|30)))$|"
		      + "^(?:(?:(?:1[6-9]|[2-9]\\d)?(?:0[48]|[2468][048]|[13579][26])|(?:(?:16|[2468][048]|[3579][26])00)))(\\/|-|\\.)0?2\\3(?:29)$|"
		      + "^(?:(?:1[6-9]|[2-9]\\d)?\\d{2})(\\/|-|\\.)(?:(?:0?[1-9])|(?:1[0-2]))\\4(?:0?[1-9]|1\\d|2[0-8])$");
	

	
	
	
	//	Matcher matcher = DATE_PATTERN.matcher(s);
	//	if (matcher.find()) {
	//	  System.out.println("data gasita: " + matcher.group(1));
	//	}
	
	
	
	public AppDateFormater() {
		
	}
	
	
	public static boolean isNumeric(String str) {
		  return str.matches("-?\\d+(\\.\\d+)?");  //match a number with optional '-' and decimal.
		}
	
	
	
	public int checkIfDateOrNumber(String s) {
		int flag = 0;
		if (s.contains("\\") || (s.contains("/") || s.contains("-")) ) {
			flag = 1;
		}
		
		if(AppDateFormater.isNumeric(s)) {
			flag = 2;
		}
		
		return flag;
	}
	
	
	
	public String formatDate(String s) {

		int flag = 0;
		
		if (s.contains("\\")) {
			flag = 1;
		}

		if (s.contains("/")) {
			flag = 2;

		}

		if (s.contains("-")) {
			flag = 3;
		}

		System.out.println("formating a date");
		Date date1;

		switch (flag) {

		case 0: {
			break;
		}

		case 1: {
			try {
				date1 = new SimpleDateFormat("dd\\MM\\yyyy").parse(s);
				s = new SimpleDateFormat("yyyy-MM-dd").format(date1);
				System.out.println("date with backslash " + s);
				break;
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		case 2: {
			try {
				date1 = new SimpleDateFormat("dd/MM/yyyy").parse(s);
				s = new SimpleDateFormat("yyyy-MM-dd").format(date1);
				System.out.println("date with forward slash " + s);
				break;
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

		case 3: {
			try {
				date1 = new SimpleDateFormat("dd-MM-yyyy").parse(s);
				s = new SimpleDateFormat("yyyy-MM-dd").format(date1);
				System.out.println("date with dash " + s);
				break;
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		}

		return s;

	}


}
