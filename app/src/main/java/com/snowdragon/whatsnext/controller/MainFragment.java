package com.snowdragon.whatsnext.controller;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.snowdragon.whatsnext.model.Task;
import com.snowdragon.whatsnext.model.TaskList;

import java.util.List;

public class MainFragment extends Fragment {

    private static final String TAG = "MainFragment";
    private RecyclerView mTaskRecyclerView;
    private TaskAdaptor mTaskAdaptor;

    /*
     * Inflation of MainFragment.
     */
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        mTaskRecyclerView = view.findViewById(R.id.task_recycler_view);
        mTaskRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        mTaskRecyclerView.setAdapter(new TaskAdaptor(TaskList.get().getTasks()));
        return view;
    }

    private class TaskHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener {

        private TextView mTaskName;
        private TextView mTaskDeadline;
        private Task mTask;

        public TaskHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.list_item_task, parent, false));
            itemView.setOnClickListener(this);
            mTaskName = itemView.findViewById(R.id.task_name_textview);
            mTaskDeadline = itemView.findViewById(R.id.task_deadline_textview);
        }

        public void bind(Task task) {
            mTask = task;
            mTaskName.setText(task.getName());
//            mTaskDeadline.setText(task.getDeadline().toString());
            mTaskDeadline.setText(DateFormat.format("dd/MM/yyyy",task.getDeadline().getTime()));
        }

        /*
         * Overriding the the onClick method for the ViewHolder.
         *
         * <p>
         * Program the onCLick to replace the MainFragment with the DetailFragment
         * of the selected Task.
         * </p>
         */
        @Override
        public void onClick(View v) {
            DetailFragment detailFragment = DetailFragment.newInstance(mTask);
            getActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, detailFragment, "TaskDetailFragment")
                    .addToBackStack(null)
                    .commit();
        }
    }

    private class TaskAdaptor extends RecyclerView.Adapter<TaskHolder> {

        private List<Task> mTasks;

        public TaskAdaptor(List<Task> tasks) {
            mTasks = tasks;
        }

        @NonNull
        @Override
        public TaskHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            return new TaskHolder(layoutInflater, parent);
        }

        // To be kept efficient for faster loading as you scroll
        @Override
        public void onBindViewHolder(@NonNull TaskHolder holder, int position) {
            Task task = mTasks.get(position);
            holder.bind(task);
        }

        @Override
        public int getItemCount() {
            return mTasks.size();
        }
    }
}