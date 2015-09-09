package cc.xuemiao.utils;


import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import android.net.ParseException;

public class HMTools {
	
	public static String getAge(String date) throws Exception { 
		Date birthDay = null;
		try  
		{  
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");  
			birthDay = sdf.parse(date);  
		}  
		catch (ParseException e)  
		{  
		    System.out.println(e.getMessage());  
		}  

        Calendar cal = Calendar.getInstance(); 

        if (cal.before(birthDay)) { 
            throw new IllegalArgumentException( 
                "The birthDay is before Now.It's unbelievable!"); 
        } 

        int yearNow = cal.get(Calendar.YEAR); 
        int monthNow = cal.get(Calendar.MONTH)+1; 
        int dayOfMonthNow = cal.get(Calendar.DAY_OF_MONTH); 
        
        cal.setTime(birthDay); 
        int yearBirth = cal.get(Calendar.YEAR); 
        int monthBirth = cal.get(Calendar.MONTH); 
        int dayOfMonthBirth = cal.get(Calendar.DAY_OF_MONTH); 

        int age = yearNow - yearBirth; 

        if (monthNow <= monthBirth) { 
            if (monthNow == monthBirth) { 
                //monthNow==monthBirth 
                if (dayOfMonthNow < dayOfMonthBirth) { 
                    age--; 
                } 
            } else { 
                //monthNow>monthBirth 
                age--; 
            } 
        } 

        return age +""; 
    }
}
