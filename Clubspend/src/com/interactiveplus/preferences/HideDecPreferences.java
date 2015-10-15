/**
 * 
 */
package com.interactiveplus.preferences;

/**
 * @author Michael Angelo
 *
 */
public class HideDecPreferences extends BasePreferences {
	// Sync preferences
  
    private static final String CHECKED_HD= "checked_hd";
    private static final String SEARCH_SELECTED= "search_selected";
    // Sync preferences default
    private static final int DEFAULT_SEARCH_SELECTED = 0;
    private static final boolean DEFAULT_CHECKED_HD = true;

    public static void setSearchSelected(int searchSelected) {
    	editor.putInt(SEARCH_SELECTED,searchSelected);
    }
    
    public static int getSearchSelected() {
    	return settings.getInt(SEARCH_SELECTED,DEFAULT_SEARCH_SELECTED);
    }
    
    
    public static void setChecked(boolean checked) {
    	editor.putBoolean(CHECKED_HD,checked);
    }
    
    public static boolean getChecked() {
    	return settings.getBoolean(CHECKED_HD,DEFAULT_CHECKED_HD);
    }
}
