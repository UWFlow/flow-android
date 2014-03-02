package utility;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by jasperfung on 3/2/14.
 */
public class DateHelper {
    private static final String seasons[] = {
	    "Winter", "Winter", "Spring", "Spring", "Summer", "Summer",
	    "Summer", "Summer", "Fall", "Fall", "Winter", "Winter"
    };
    private static final String shortMonth[] = {
	    "Jan", "Feb", "Mar", "Apr", "May", "Jun",
	    "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"
    };

    public static String getSeason( Date date ) {
	return seasons[ date.getMonth() ];
    }

    public static String getShortMonth( Date date ) {
	return shortMonth[ date.getMonth() ];
    }

    public static String getShortString(Date date) {
	StringBuilder sb = new StringBuilder();
	DateFormat df = new SimpleDateFormat("yy");
	sb.append(getShortMonth(date));
	sb.append(" ");
	sb.append(df.format(date));
	return sb.toString();
    }
}
