package com.example.Models;

public class TaskCategoryModel {
    private int  task1_id;
    private int cat1_id;
    private int cat_task_id;

    public TaskCategoryModel(int task1_id, int cat1_id, int cat_task_id) {
        this.task1_id = task1_id;
        this.cat1_id = cat1_id;
        this.cat_task_id = cat_task_id;
    }

    public int getTask1_id() {
        return task1_id;
    }

    public void setTask1_id(int task1_id) {
        this.task1_id = task1_id;
    }

    public int getCat1_id() {
        return cat1_id;
    }

    public void setCat1_id(int cat1_id) {
        this.cat1_id = cat1_id;
    }

    public int getCat_task_id() {
        return cat_task_id;
    }

    public void setCat_task_id(int cat_task_id) {
        this.cat_task_id = cat_task_id;
    }
}
