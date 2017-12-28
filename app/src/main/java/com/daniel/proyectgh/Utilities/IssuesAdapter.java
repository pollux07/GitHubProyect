package com.daniel.proyectgh.Utilities;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.daniel.proyectgh.R;

import java.util.List;

/**
 * Created by daniel on 28/12/17.
 */

public class IssuesAdapter extends RecyclerView.Adapter<IssuesAdapter.GuardHolder>{
    private List<IssuesObject> issuesObjects;

    class GuardHolder extends RecyclerView.ViewHolder {
        TextView issueTitle;
        TextView issueCreatedBy;
        TextView issueCreatedAt;

        GuardHolder(View view) {
            super(view);
            issueTitle = view.findViewById(R.id.tv_issue_title);
            issueCreatedBy = view.findViewById(R.id.tv_created_by);
            issueCreatedAt = view.findViewById(R.id.tv_created_at);
        }
    }

    public IssuesAdapter(List<IssuesObject> issuesObjects) {
        this.issuesObjects = issuesObjects;
    }

    @Override
    public IssuesAdapter.GuardHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.issues_row, parent, false);

        return new IssuesAdapter.GuardHolder(itemView);
    }

    @Override
    public void onBindViewHolder(IssuesAdapter.GuardHolder holder, int position) {
        IssuesObject issueObject = issuesObjects.get(position);

        String issueTitle = issueObject.getIssueTitle();
        String issueCreatedBy = issueObject.getIssueCreatedBy();
        String issueCreatedAt = issueObject.getIssueCreatedAt();


        holder.issueTitle.setText(issueTitle);
        holder.issueCreatedBy.append(issueCreatedBy);
        if (issueCreatedAt != null) {
            holder.issueCreatedAt.append(issueCreatedAt);
        } else {
            holder.issueCreatedAt.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return issuesObjects.size();
    }
}
