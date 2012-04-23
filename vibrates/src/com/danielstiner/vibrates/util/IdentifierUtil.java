package com.danielstiner.vibrates.util;

import java.util.Locale;

import roboguice.util.Ln;

import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber.PhoneNumber;

public class IdentifierUtil {

	public static String phoneNumberToInternational(String number) {

		try {
			PhoneNumber num = PhoneNumberUtil.getInstance()
					.parseAndKeepRawInput(number,
							Locale.getDefault().getCountry());

			// Put number in general international format
			String fnum = PhoneNumberUtil.getInstance()
					.formatOutOfCountryCallingNumber(num, "");

			return fnum;

		} catch (NumberParseException e) {
			Ln.e(e, "Could not convert phone number to international format.");
		}

		return null;
	}
}
