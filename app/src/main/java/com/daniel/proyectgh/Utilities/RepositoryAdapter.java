package com.daniel.proyectgh.Utilities;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.daniel.proyectgh.R;

import java.util.List;

/**
 * Created by daniel on 27/12/17.
 */

public class RepositoryAdapter extends RecyclerView.Adapter<RepositoryAdapter.GuardHolder> {
    private List<RepositoryObject> repositoryObjects;

    class GuardHolder extends RecyclerView.ViewHolder {
        TextView name;

        GuardHolder(View view) {
            super(view);
            name = view.findViewById(R.id.row_repoo_name);
        }
    }

    public RepositoryAdapter(List<RepositoryObject> repositoryObjects) {
        this.repositoryObjects = repositoryObjects;
    }

    @Override
    public GuardHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.repositories_row, parent, false);

        return new GuardHolder(itemView);
    }

    @Override
    public void onBindViewHolder(GuardHolder holder, int position) {
        RepositoryObject repoObject = repositoryObjects.get(position);

        String name = repoObject.getRepoName();


        holder.name.setText(name);
    }

    @Override
    public int getItemCount() {
        return repositoryObjects.size();
    }
}
