package com.daniel.proyectgh.Utilities;

/**
 * Created by daniel on 28/12/17.
 */

public class IssuesObject {
    private String issueTitle;
    private String issueCreatedBy;
    private String issueCreatedAt;

    public IssuesObject(){}

    public String getIssueTitle() {
        return issueTitle;
    }

    public IssuesObject setIssueTitle(String issue_title) {
        this.issueTitle = issue_title;

        return this;
    }

    public String getIssueCreatedBy() {
        return issueCreatedBy;
    }

    public IssuesObject setIssueCreatedBy(String issue_created_by) {
        this.issueCreatedBy = issue_created_by;

        return this;
    }

    public String getIssueCreatedAt() {
        return issueCreatedAt;
    }

    public IssuesObject setIssueCreatedBAt(String issue_created_at) {
        this.issueCreatedAt = issue_created_at;

        return this;
    }
}
