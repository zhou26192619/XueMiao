package cc.xuemiao.bean;

import java.util.Date;

public class HMCourseCommentBean {
	private String				id;			//评论ID
	private HMCommentAccount	account;		//评论用户
	private String				content;		//评论内容
	private Date				commentTime;	//评论时间
												
	private Double				improve;		//提高
	private Double				like;			//喜爱
	private Double				care;			//关心
												
	public HMCommentAccount getAccount() {
		return account;
	}
	
	public void setAccount(HMCommentAccount account) {
		this.account = account;
	}
	
	public Double getCare() {
		return care;
	}
	
	public void setCare(Double care) {
		this.care = care;
	}
	
	public Date getCommentTime() {
		return commentTime;
	}
	
	public void setCommentTime(Date commentTime) {
		this.commentTime = commentTime;
	}
	
	public String getContent() {
		return content;
	}
	
	public void setContent(String content) {
		this.content = content;
	}
	
	public String getId() {
		return id;
	}
	
	public void setId(String id) {
		this.id = id;
	}
	
	public Double getImprove() {
		return improve;
	}
	
	public void setImprove(Double improve) {
		this.improve = improve;
	}
	
	public Double getLike() {
		return like;
	}
	
	public void setLike(Double like) {
		this.like = like;
	}
	
	public class HMCommentAccount {
		private String	accountName;
		private String	id;
		
		public String getAccountName() {
			return accountName;
		}
		
		public void setAccountName(String accountName) {
			this.accountName = accountName;
		}
		
		public String getId() {
			return id;
		}
		
		public void setId(String id) {
			this.id = id;
		}
		
	}
}
