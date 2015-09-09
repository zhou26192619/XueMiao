package cc.xuemiao.utils;

import android.widget.ImageView;

import cc.xuemiao.R;


public class GenderUtil {
    public static String GENDER_MALE = "m";
    public static String GENDER_FEMALE = "f";
    public static int MALE = R.mipmap.gender_male;
    public static int FEMALE = R.mipmap.gender_female;

    public static void setGenderImage(String gender, ImageView iv) {
        if (GENDER_MALE.equals(gender)) {
            iv.setImageResource(MALE);
        } else {
            iv.setImageResource(FEMALE);
        }
    }

}
