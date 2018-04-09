package gui;

import javax.swing.text.BadLocationException;
import javax.swing.text.AttributeSet;
import javax.swing.text.DocumentFilter;

public class IntFilter extends DocumentFilter
{
	private int intCount = 0;
	private int decimalCount = 0;
	private boolean comma = false;
	private String stringa = "";

    public void insertString(DocumentFilter.FilterBypass fb, int offset,
                             String string, AttributeSet attr)
            throws BadLocationException {
        StringBuffer buffer = new StringBuffer(string);
    	if (!comma && Character.isDigit(buffer.charAt(0)))
    	{
    		intCount++;
        	stringa+=buffer.charAt(0);
    	}
    	else if (comma && Character.isDigit(buffer.charAt(0))) 
    	{
    		decimalCount++;
        	stringa+=buffer.charAt(0);
    	}
    	if (!comma && intCount > 2 && Character.isDigit(buffer.charAt(0)))
        {
        	intCount--;
        	buffer.deleteCharAt(0);
        	stringa = stringa.substring(0, stringa.length()-1);
        }
    	else if (decimalCount > 2 && Character.isDigit(buffer.charAt(0)))
        {
        	decimalCount--;
        	buffer.deleteCharAt(0);
        	stringa = stringa.substring(0, stringa.length()-1);
        }
    	else if (comma && (buffer.charAt(0) == '.' || buffer.charAt(0) == ','))
		{
        	buffer.deleteCharAt(0);
		}
        for (int i = buffer.length() - 1; i >= 0; i--) {
            char ch = buffer.charAt(i);
            if (ch == '.' || ch ==',' && !comma)
            {
            	comma = true;
            	stringa+=ch;
            }
            else if (!Character.isDigit(ch)) 
            {
                buffer.deleteCharAt(i);
            }
        }
        super.insertString(fb, offset, buffer.toString(), attr);
    }

    public void replace(DocumentFilter.FilterBypass fb,
                        int offset, int length, String string, AttributeSet attr) throws BadLocationException
    {
        if (length > 0) fb.remove(offset, length);
        insertString(fb, offset, string, attr);
    }
    
    @Override
    public void remove(FilterBypass fb, int offset, int length) throws BadLocationException
    {
//    	char[] arr = stringa.toCharArray();
//    	for (char c : arr)
//    	{
//    		comma = false;
//	    	if (c == '.' || c == ',') 
//	    	{
//	    		comma = true;
//	    		break;
//	    	}
//    	}
    	if (stringa.charAt(stringa.length()-1) == ',' || stringa.charAt(stringa.length()-1) == '.')
    	{
    		comma = false;
    	}
    		
    	
    	if (!comma) intCount--;
    	else decimalCount--;
    	
    	if (decimalCount<0) decimalCount = 0;
    	if (intCount<0)	intCount = 0;
    	stringa = stringa.substring(0, stringa.length()-1);

    	System.out.println(stringa);
    	
    	super.remove(fb, offset, length);
    }
}

