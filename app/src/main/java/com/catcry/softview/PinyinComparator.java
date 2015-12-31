package com.catcry.softview;

import java.util.Comparator;

/**
 * 
 * @author xiaanming
 *
 */
public class PinyinComparator implements Comparator<SortModel> {

	public int compare(SortModel o1, SortModel o2) {
			return o1.getSortLetters().compareTo(o2.getSortLetters());
	}

}
