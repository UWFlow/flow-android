package com.uwflow.flow_android.util;

/**
 * Created by jasperfung on 3/4/14.
 */
public class StringHelper {
    public static String capitalize(String string) {
	char[] chars = string.toLowerCase().toCharArray();
	boolean found = false;
	for (int i = 0; i < chars.length; i++) {
	    if (!found && Character.isLetter(chars[i])) {
		chars[i] = Character.toUpperCase(chars[i]);
		found = true;
	    } else if (Character.isWhitespace(chars[i]) || chars[i]=='.' || chars[i]=='\'') {
		found = false;
	    }
	}
	return String.valueOf(chars);
    }
}
