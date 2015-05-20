package com.podio.manager.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Collections;
import java.util.List;

import com.podio.manager.R;
import com.podio.manager.model.ItemListRow;
import com.podio.manager.model.Organization;
import com.tonicartos.superslim.LayoutManager;
import com.tonicartos.superslim.LinearSLM;

public class ManagerListAdapter extends RecyclerView.Adapter<ManagerListAdapter.MyViewHolder> {
    List<ItemListRow> data = Collections.emptyList();
    private static int VIEW_TYPE_HEADER = 0x02;
    private static int VIEW_TYPE_CONTENT = 0x03;

    public ManagerListAdapter(List<ItemListRow> data) {
        this.data = data;
    }

    public void updateList(List<ItemListRow> updated){
        data.clear();
        data.addAll(updated);
        notifyDataSetChanged();
    }

    public void updateListOrg(List<Organization> organizationList){
        data.clear();
        int spaceCount = 0;
        int sectionFirstPosition;

        for (int headerCount = 0; headerCount < organizationList.size(); headerCount++) {
            sectionFirstPosition = (spaceCount + headerCount);
            organizationList.get(headerCount).setSectionFirstPosition(sectionFirstPosition);
            data.add(organizationList.get(headerCount));
            for (int spaceItem = 0; spaceItem < organizationList.get(headerCount).getWorkspaceList().size(); spaceItem++) {
                organizationList.get(headerCount).getWorkspaceList().get(spaceItem).setSectionFirstPosition(sectionFirstPosition);
                data.add(organizationList.get(headerCount).getWorkspaceList().get(spaceItem));
                spaceCount++;
            }
        }
        notifyDataSetChanged();
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view;
        if (viewType == VIEW_TYPE_HEADER) {
            view = inflater.inflate(R.layout.header_item, parent, false);
        } else {
            view = inflater.inflate(R.layout.content_item, parent, false);
        }
        return new MyViewHolder(view);
    }

    @Override
    public int getItemViewType(int position) {
        return data.get(position) instanceof Organization ? VIEW_TYPE_HEADER : VIEW_TYPE_CONTENT;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        ItemListRow currentItem = data.get(position);

        final View itemView = holder.itemView;

        final LayoutManager.LayoutParams params = (LayoutManager.LayoutParams) itemView.getLayoutParams();
        params.setSlm(LinearSLM.ID);
//        params.headerEndMarginIsAuto = false;
//        params.headerStartMarginIsAuto = false;

        holder.title.setText("" + currentItem.getName());
        params.setFirstPosition(currentItem.getSectionFirstPosition());
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView title;

        public MyViewHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.listText);
        }
    }
}