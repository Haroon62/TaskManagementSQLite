package com.example.taskmanagementsqlite;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.Adapters.TaskAdapter;
import com.example.Models.TaskModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    FloatingActionButton actionButton;
    RecyclerView recyclerView;
    ArrayList<TaskModel> tasklist;
    DBHelper dbHelper;
    private EditText etLoginUsername, etLoginPassword;
    private Button btnLogin, btnSignup;

    private UserDAO userDAO;
    private int year, month, day, hour, minute;

    private boolean isMenuOpen = false;

    TaskAdapter taskAdapter;
    TaskModel taskModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        actionButton=findViewById(R.id.addnew);
        recyclerView=findViewById(R.id.recycler);
        ImageView fabItem1 = findViewById(R.id.fabItem1);
        ImageView fabItem2 = findViewById(R.id.fabItem2);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        dbHelper=new DBHelper(this);
        taskModel=new TaskModel();
        tasklist=new ArrayList<>();

        NotificationUtils.createNotificationChannel(this);
        tasklist=dbHelper.fetchData();

        for (TaskModel contact : tasklist) {
            Log.d("ContactList", "ID: " + contact.getId() + "  ,Name: " + contact.getName() + ", Phone: " + contact.getDescription()+"category: " + contact.getCategorname() + "  ,priority: " + contact.getPeriorityname());
        }

        year = Calendar.getInstance().get(Calendar.YEAR);
        month = Calendar.getInstance().get(Calendar.MONTH);
        day = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
        hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
        minute = Calendar.getInstance().get(Calendar.MINUTE);

        taskAdapter = new TaskAdapter(tasklist, this, new TaskAdapter.OnUpdateClickListener() {
            @Override
            public void onUpdateClick(int position) {
                showUpdateDialog(position);
            }
        }, new TaskAdapter.OnLongPressListener() {
            @Override
            public void onLongPress(int position) {

                TaskModel contactToDelete = tasklist.get(position);
                showDeleteConfirmationDialog(contactToDelete);
            }

    });
        recyclerView.setAdapter(taskAdapter);

        actionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isMenuOpen) {
                    showMenu(fabItem1, 0);
                    showMenu(fabItem2, 1);
                } else {
                    hideMenu(fabItem1, 0);
                    hideMenu(fabItem2, 1);
                }
                isMenuOpen = !isMenuOpen;
            }


        });


    }
    private void showAddContactDialog(int position) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.add_tasks, null);
        dialogBuilder.setView(dialogView);

        final EditText editTextName = dialogView.findViewById(R.id.editTextName);
        final EditText editTextDesc = dialogView.findViewById(R.id.editTextPhoneNumber);
        final EditText editTextdeadline = dialogView.findViewById(R.id.editTextdeadline);
        Spinner category = dialogView.findViewById(R.id.category);
        Spinner priority = dialogView.findViewById(R.id.priority);
        Button btnAdd = dialogView.findViewById(R.id.btnAdd);

        final AlertDialog alertDialog = dialogBuilder.create();
        List<String> existingCategories = dbHelper.getAllCategories();
        editTextdeadline.setText("");

        editTextdeadline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDateTimePickerDialog(editTextdeadline);
            }
        });
        // Update the EditText with the selected date and time

        ArrayAdapter<String> adaptercategory = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, existingCategories);
        adaptercategory.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        category.setAdapter(adaptercategory);

        List<String> existingPriorities = dbHelper.getAllPriorities();


        ArrayAdapter<String> adapterper = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, existingPriorities);
        adaptercategory.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        priority.setAdapter(adapterper);

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = editTextName.getText().toString();
                String descr = editTextDesc.getText().toString();
                String deadline = editTextdeadline.getText().toString();
                String priorityName = priority.getSelectedItem().toString();
                String categoryName = category.getSelectedItem().toString();


                if (!name.isEmpty() && !deadline.isEmpty()&&!descr.isEmpty()) {


                    TaskModel modelClass=new TaskModel(position,name,descr,deadline,categoryName,priorityName);

                    setNotification(modelClass);
//                    Log.d("MainActivity", "Category ID: " + categoryId);
//                    Log.d("MainActivity", "Priority ID: " + priorityId);
                    dbHelper.addTask(name,descr,deadline,categoryName,priorityName);
                    tasklist.add(modelClass);
                     taskAdapter.notifyDataSetChanged();
                   // Toast.makeText(MainActivity.this, "Added", Toast.LENGTH_SHORT).show();

                    // Close the dialog
                    alertDialog.dismiss();
                } else {
                    Toast.makeText(MainActivity.this, "Please enter data", Toast.LENGTH_SHORT).show();
                }
            }
        });

        alertDialog.show();
    }

    private void showAddCategoryDialog() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.categoryset, null);
        dialogBuilder.setView(dialogView);

        final EditText editTextName = dialogView.findViewById(R.id.editcategory);

        final EditText editTextPriority = dialogView.findViewById(R.id.editpriority);

        Button btnAdd = dialogView.findViewById(R.id.btnAddcat);

        final AlertDialog alertDialog = dialogBuilder.create();
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = editTextName.getText().toString();
                String per = editTextPriority.getText().toString();



                if (!name.isEmpty()) {


                    dbHelper.addCategories(name);
                    dbHelper.addPeriorities(per);
                    taskAdapter.notifyDataSetChanged();
                    Toast.makeText(MainActivity.this, "Added", Toast.LENGTH_SHORT).show();

                    // Close the dialog
                    alertDialog.dismiss();
                } else {
                    Toast.makeText(MainActivity.this, "Please enter data", Toast.LENGTH_SHORT).show();
                }
            }
        });

        alertDialog.show();
    }

    private void showDeleteConfirmationDialog( TaskModel contactToDelete) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Are you sure you want to delete this contact?");
        builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Delete the item from the list and notify the adapter
//                String id= String.valueOf(contactToDelete.getId());
//                Log.d("shdhd", id);
                dbHelper.deleteContact(contactToDelete.getId());
                tasklist.remove(contactToDelete);
                taskAdapter.notifyDataSetChanged();
                // Toast.makeText(MainActivity.this, "Deleted", Toast.LENGTH_SHORT).show();
            }
        });
        builder.setNegativeButton("Cancel", null);
        builder.show();
    }

    private void showUpdateDialog(int position) {
        // Inflate the custom dialog layout

        TaskModel existingContact = tasklist.get(position);

        View dialogView = LayoutInflater.from(this).inflate(R.layout.add_tasks, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(dialogView);

        final EditText editTextName = dialogView.findViewById(R.id.editTextName);
        final EditText editTextDesc = dialogView.findViewById(R.id.editTextPhoneNumber);
        final EditText editTextdeadline = dialogView.findViewById(R.id.editTextdeadline);
        Spinner category = dialogView.findViewById(R.id.category);
        Spinner priority = dialogView.findViewById(R.id.priority);
        Button updateButton = dialogView.findViewById(R.id.btnAdd);
        updateButton.setText("Update");


        // Assuming you have arrays.xml with category and priority arrays
        ArrayAdapter<CharSequence> categoryAdapter = ArrayAdapter.createFromResource(this, R.array.category, android.R.layout.simple_spinner_item);
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        category.setAdapter(categoryAdapter);

        ArrayAdapter<CharSequence> priorityAdapter = ArrayAdapter.createFromResource(this, R.array.priority, android.R.layout.simple_spinner_item);
        priorityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        priority.setAdapter(priorityAdapter);

        // Set the selected values from existingContact
        category.setSelection(categoryAdapter.getPosition(existingContact.getCategorname()));
        priority.setSelection(priorityAdapter.getPosition(existingContact.getPeriorityname()));


        editTextName.setText(existingContact.getName());
        editTextDesc.setText(existingContact.getDescription());
        editTextdeadline.setText(existingContact.getDeadline());



        AlertDialog alertDialog = builder.create();

        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newName = editTextName.getText().toString();
                String newdesc = editTextDesc.getText().toString();
                String newdead = editTextdeadline.getText().toString();
                String priorityName = priority.getSelectedItem().toString();
                String categoryName = category.getSelectedItem().toString();

                existingContact.setName(newName);
                existingContact.setDescription(newdesc);
                existingContact.setDeadline(newdead);
                existingContact.setPeriorityname(priorityName);
                existingContact.setCategorname(categoryName);

                dbHelper.updateData(existingContact.getId(),newName,newdesc,newdead,priorityName,categoryName);

                taskAdapter.notifyDataSetChanged();


                // Close the dialog
                alertDialog.dismiss();
            }
        });

        alertDialog.show();
    }

    private void showMenu(ImageView view, int index) {
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.show_fab);
        view.startAnimation(animation);
        view.setVisibility(View.VISIBLE);

        if (index==0){
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showAddContactDialog(taskModel.getId());
                }
            });
        }
        if (index==1){
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showAddCategoryDialog();
                }
            });
        }


    }

    private void hideMenu(ImageView view, int index) {
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.hide_fab);
        view.startAnimation(animation);
        view.setVisibility(View.INVISIBLE);
    }
    private void showDateTimePickerDialog(final EditText editText) {
        // Create a DatePickerDialog
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        MainActivity.this.year = year;
                        month = monthOfYear;
                        day = dayOfMonth;

                        // Create a TimePickerDialog after setting the date
                        TimePickerDialog timePickerDialog = new TimePickerDialog(
                                MainActivity.this,
                                new TimePickerDialog.OnTimeSetListener() {
                                    @Override
                                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                        MainActivity.this.hour = hourOfDay;
                                        MainActivity.this.minute = minute;

                                        // Update the EditText with the selected date and time
                                        Calendar selectedDateTime = Calendar.getInstance();
                                        selectedDateTime.set(year, month, day, hour, minute);
                                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());
                                        editText.setText(sdf.format(selectedDateTime.getTime()));
                                    }
                                },
                                hour,
                                minute,
                                DateFormat.is24HourFormat(MainActivity.this)
                        );
                        timePickerDialog.show();
                    }
                },
                year,
                month,
                day
        );
        datePickerDialog.show();
    }

    private void setNotification(TaskModel taskModel) {
        // Convert deadline string to Date object
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());
        try {
            Date deadlineDate = sdf.parse(taskModel.getDeadline());

            // Set up AlarmManager to trigger a notification at the specified deadline
            AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
            Intent intent = new Intent(this, NotificationReceiver.class);
            intent.putExtra("TASK_NAME", taskModel.getName());
            PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_IMMUTABLE);

            if (alarmManager != null) {
                alarmManager.set(AlarmManager.RTC_WAKEUP, deadlineDate.getTime(), pendingIntent);
            }

        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

}