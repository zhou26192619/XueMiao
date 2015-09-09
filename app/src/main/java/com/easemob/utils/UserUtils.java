package com.easemob.utils;

import android.content.Context;
import android.text.TextUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.easemob.applib.controller.HXSDKHelper;
import com.easemob.domain.User;
import com.easemob.helper.HMHXSDKHelper;
import com.squareup.picasso.Picasso;

import cc.xuemiao.R;

public class UserUtils {
    /**
     * 根据username获取相应user，由于demo没有真实的用户数据，这里给的模拟的数据；
     *
     * @param username
     * @return
     */
    public static User getUserInfo(String username) {
        User user = ((HMHXSDKHelper) HXSDKHelper.getInstance()).getContactList().get(username);
        if (user == null) {
            user = new User(username);
        }

        if (user != null) {
            //demo没有这些数据，临时填充
            if (TextUtils.isEmpty(user.getNick()))
                user.setNick(username);
        }
        return user;
    }

    /**
     * 设置用户头像
     *
     * @param username
     */
    public static void setUserAvatar(Context context, String username, ImageView imageView) {
        User user = getUserInfo(username);
        if (user != null && user.getAvatar() != null) {
            Picasso.with(context).load(user.getAvatar()).placeholder(R.mipmap.default_icon_s).into(imageView);
        } else {
            Picasso.with(context).load(R.mipmap.default_icon_s).into(imageView);
        }
    }

    /**
     * 设置当前用户头像
     */
    public static void setCurrentUserAvatar(Context context, ImageView imageView) {
        User user = ((HMHXSDKHelper) HXSDKHelper.getInstance()).getUserProfileManager().getCurrentUserInfo();
        if (user != null && user.getAvatar() != null) {
            Picasso.with(context).load(user.getAvatar()).placeholder(R.mipmap.default_icon_s).into(imageView);
        } else {
            Picasso.with(context).load(R.mipmap.default_icon_s).into(imageView);
        }
    }

    /**
     * 设置用户昵称
     */
    public static void setUserNick(String username, TextView textView) {
        User user = getUserInfo(username);
        if (user != null) {
            textView.setText(user.getNick());
        } else {
            textView.setText(username);
        }
    }

    /**
     * 设置当前用户昵称
     */
    public static void setCurrentUserNick(TextView textView) {
        User user = ((HMHXSDKHelper) HXSDKHelper.getInstance()).getUserProfileManager().getCurrentUserInfo();
        if (textView != null) {
            textView.setText(user.getNick());
        }
    }

    /**
     * 保存或更新某个用户
     *
     * @param newUser
     */
    public static void saveUserInfo(User newUser) {
        if (newUser == null || newUser.getUsername() == null) {
            return;
        }
        ((HMHXSDKHelper) HXSDKHelper.getInstance()).saveContact(newUser);
    }

}
