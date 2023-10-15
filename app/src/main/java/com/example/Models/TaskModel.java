package com.example.Models;

public class TaskModel {
     int id;
     String name;
     String description;
     String deadline;
    String categorname;
    String periorityname;


    public TaskModel() {
    }

    public TaskModel(int id, String name, String description, String deadline, String categorname, String periorityname) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.deadline = deadline;
        this.categorname = categorname;
        this.periorityname = periorityname;
    }

    public String getCategorname() {
        return categorname;
    }

    public void setCategorname(String categorname) {
        this.categorname = categorname;
    }

    public String getPeriorityname() {
        return periorityname;
    }

    public void setPeriorityname(String periorityname) {
        this.periorityname = periorityname;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDeadline() {
        return deadline;
    }

    public void setDeadline(String deadline) {
        this.deadline = deadline;
    }
}
