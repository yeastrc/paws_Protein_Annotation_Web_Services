package org.yeastrc.paws.base.utils;

import org.apache.commons.lang.StringUtils;

/**
 *
 *
 */
public class ProcessYesNoFlag {

	/**
	 * return true if param == "yes" ignore case, false if param == "no" ignore case, else throw IllegalArgumentException.
	 * @param yesNoFlag
	 * @return
	 */
	public static boolean processYesNoFlag( String yesNoFlag )  {

		if ( StringUtils.isEmpty( yesNoFlag ) ) {

			throw new IllegalArgumentException( "'yesNoFlag' cannot be empty." );
		}

		if ( yesNoFlag.equalsIgnoreCase( "yes" ) ) {
			return true;
		}

		if ( yesNoFlag.equalsIgnoreCase( "no" ) ) {
			return false;
		}

		throw new IllegalArgumentException( "'yesNoFlag' must be 'yes' or 'no' ( ignoring case )." );
	}
}
