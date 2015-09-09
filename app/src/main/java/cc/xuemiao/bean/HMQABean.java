package cc.xuemiao.bean;

import java.io.Serializable;
import java.util.List;

/**
 * 问答对象
 *
 * @author loar
 */
public class HMQABean implements Serializable {
    private static final long serialVersionUID = 1L;
    private String id;
    private String questionContent;
    private String answerContent;
    private HMParentBean parent;
    private HMUserBean account;
    private HMCourseBean course;
    private HMCampaignBean activity;
    private String status;
    private String createDate;
    private String replyId;
    private List<HMAnswerBean> answers;

    public HMCampaignBean getActivity() {
        return activity;
    }

    public void setActivity(HMCampaignBean activity) {
        this.activity = activity;
    }

    public List<HMAnswerBean> getAnswers() {
        return answers;
    }

    public void setAnswers(List<HMAnswerBean> answers) {
        this.answers = answers;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getQuestionContent() {
        return questionContent;
    }

    public void setQuestionContent(String questionContent) {
        this.questionContent = questionContent;
    }

    public String getAnswerContent() {
        return answerContent;
    }

    public void setAnswerContent(String answerContent) {
        this.answerContent = answerContent;
    }

    public HMParentBean getParent() {
        return parent;
    }

    public void setParent(HMParentBean parent) {
        this.parent = parent;
    }

    public HMUserBean getAccount() {
        return account;
    }

    public void setAccount(HMUserBean account) {
        this.account = account;
    }

    public HMCourseBean getCourse() {
        return course;
    }

    public void setCourse(HMCourseBean course) {
        this.course = course;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public String getReplyId() {
        return replyId;
    }

    public void setReplyId(String replyId) {
        this.replyId = replyId;
    }

    public static class HMAnswerBean implements Serializable {
        private static final long serialVersionUID = 1L;
        private String id;
        private HMUserBean account;
        private String answerContent;
        private String createDate;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public HMUserBean getAccount() {
            return account;
        }

        public void setAccount(HMUserBean account) {
            this.account = account;
        }

        public String getAnswerContent() {
            return answerContent;
        }

        public void setAnswerContent(String answerContent) {
            this.answerContent = answerContent;
        }

        public String getCreateDate() {
            return createDate;
        }

        public void setCreateDate(String createDate) {
            this.createDate = createDate;
        }
    }
}
