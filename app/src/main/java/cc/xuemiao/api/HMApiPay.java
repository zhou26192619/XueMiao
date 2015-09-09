package cc.xuemiao.api;

import com.loopj.android.http.RequestParams;

import cc.xuemiao.ui.HMBaseAct;

/**
 * 支付相关api
 * 
 * @author loar
 * 
 */
public class HMApiPay extends HMApi {
	/**
	 * 银联支付渠道
	 */
	public static final String CHANNEL_UPMP = "upmp";
	/**
	 * 微信支付渠道
	 */
	public static final String CHANNEL_WECHAT = "wx";
	/**
	 * 支付支付渠道
	 */
	public static final String CHANNEL_ALIPAY = "alipay";
	/**
	 * 百度支付渠道
	 */
	public static final String CHANNEL_BFB = "bfb";
	/**
	 * 京东支付渠道
	 */
	public static final String CHANNEL_JDPAY_WAP = "jdpay_wap";


	public static String PAY_CHARGE = HOST +"webapi/pay/charge";

	public static HMApiPay getInstance(){
		return  new HMApiPay();
	}

	/**
	 * 
	 * @param act
	 * @param params
	 */
	public  void postPayCharge(HMBaseAct act, RequestParams params) {
		postRequest(act, PAY_CHARGE, params);
	}
}
