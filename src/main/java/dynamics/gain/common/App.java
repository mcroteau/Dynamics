package dynamics.gain.common;

import dynamics.gain.service.LightService;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.validator.routines.EmailValidator;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class App {

	private static final Logger log = Logger.getLogger(App.class);

	@Autowired
	private static ApplicationContext applicationContext;

	public static int getRandomNumber(int max){
		Random r = new Random();
		return r.nextInt(max);
	}


	public static boolean containsSpecialCharacters(String str) {
		Pattern p = Pattern.compile("[^A-Za-z0-9]", Pattern.CASE_INSENSITIVE);
		Matcher m = p.matcher(str);
		boolean b = m.find();

		if(b)return true;
		return false;
	}

	public static String getGenericFileName(CommonsMultipartFile file){

		FileItem fileItem = file.getFileItem();
		String originalName = fileItem.getName();

		String fileName = getRandomString(9);
		String extension = getExtension(originalName);

		return fileName + "." +  extension;
	}


	private static String getExtension(final String path) {
		String result = null;
		if (path != null) {
			result = "";
			if (path.lastIndexOf('.') != -1) {
				result = path.substring(path.lastIndexOf('.'));
				if (result.startsWith(".")) {
					result = result.substring(1);
				}
			}
		}
		return result;
	}


	public static String getRandomString(int n) {
		String CHARS = "AaBbCcDdEeFfGgHhIiJjKkLlMmNnOoPpQqRrSsTtUuVvWwXxYyZz1234567890";
		StringBuilder uuid = new StringBuilder();
		Random rnd = new Random();
		while (uuid.length() < n) {
			int index = (int) (rnd.nextFloat() * CHARS.length());
			uuid.append(CHARS.charAt(index));
		}
		return uuid.toString();
	}


	public static boolean isValidMailbox(String str){
		EmailValidator validator = EmailValidator.getInstance();
		return validator.isValid(str);
	}
		

	public static String getApplicationPath(){
		try {
			Resource propResource = applicationContext.getResource(".");
			return propResource.getURI().getPath().toString();
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}


	public static long getDate() {
		Calendar cal = Calendar.getInstance();
		long date = getSimpleDateFormatted(cal);
		return date;
	}

	public static long getToday(){
		Calendar cal = Calendar.getInstance();
		DateFormat df = new SimpleDateFormat(Constants.ZERO_TIME_FORMAT);
		String date = df.format(cal.getTime());
		log.info("get today > " + date);
		return Long.parseLong(date);
	}

	public static long getYesterday(int day) {
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DAY_OF_MONTH, -day);
		App.clearTime(cal);

		long date = getSimpleDateFormatted(cal);
		return date;
	}

	public static Calendar clearTime(Calendar cal){
		cal.clear(Calendar.HOUR_OF_DAY);
		cal.clear(Calendar.AM_PM);
		cal.clear(Calendar.MINUTE);
		cal.clear(Calendar.SECOND);
		cal.clear(Calendar.MILLISECOND);
		return cal;
	}

	private static long getSimpleDateFormatted(Calendar cal) {
		DateFormat df = new SimpleDateFormat(Constants.DATE_SEARCH_FORMAT);
		String dateStr = df.format(cal.getTime());
		long date = Long.parseLong(dateStr);
		return date;
	}


	public static String getGraphDate(int day) {
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DAY_OF_MONTH, -day);
		DateFormat df = new SimpleDateFormat(Constants.DATE_GRAPH_FORMAT);
		String dateStr = df.format(cal.getTime());
		return dateStr;
	}

	public static double round(double value, int places) {
		if (places < 0) throw new IllegalArgumentException();

		BigDecimal bd = BigDecimal.valueOf(value);
		bd = bd.setScale(places, RoundingMode.HALF_UP);
		return bd.doubleValue();
	}

	public static String getActualDate(long date) {
		try {
			SimpleDateFormat format = new SimpleDateFormat(Constants.DATE_SEARCH_FORMAT);
			Date actualDate = format.parse(Long.toString(date));

			Calendar cal = Calendar.getInstance();
			cal.setTime(actualDate);

			DateFormat df = new SimpleDateFormat(Constants.DATE_ACTUAL_FORMAT);
			String actualFormatted = df.format(cal.getTime());

			return actualFormatted;

		}catch (Exception e){}
		return "";
	}

}