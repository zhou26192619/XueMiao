package cc.xuemiao.bean;

import com.lib_common.bean.UserBean;

public class HMUserBean extends UserBean {
	private static final long serialVersionUID = -5686943315696485570L;

	private HMParentBean parent;
	private String isModify;

	public HMParentBean getParent() {
		return parent;
	}

	public void setParent(HMParentBean parent) {
		this.parent = parent;
	}

	public String getIsModify() {
		return isModify;
	}

	public void setIsModify(String isModify) {
		this.isModify = isModify;
	}

}
