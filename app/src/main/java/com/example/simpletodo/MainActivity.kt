package com.example.simpletodo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import org.apache.commons.io.FileUtils
import java.io.File
import java.io.IOException
import java.nio.charset.Charset

class MainActivity : AppCompatActivity() {

    var listOfTasks = mutableListOf<String>()
    lateinit var adapter: TaskItemAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val onLongClickListener = object : TaskItemAdapter.OnLongClickListener {
            override fun onItemLongClicked(position: Int) {
                // 1. Remove item from the list
                listOfTasks.removeAt(position)
                // 2. Notify the adapter that the data set has changed
                adapter.notifyDataSetChanged()

                saveItems()
            }

        }

//        findViewById<Button>(R.id.button).setOnClickListener {
//            Log.i("Testing", "User clicked on button")
//        }

//        listOfTasks.add("Do Laundry")
//        listOfTasks.add("Go for a walk")

        loadItems()

        // look up recyclerview in layout
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        // create adapter passing in the sample user data
        adapter = TaskItemAdapter(listOfTasks, onLongClickListener)
        // Attach the adapter to the recyclerview to populate items
        recyclerView.adapter = adapter
        // Set layout manager to position the items
        recyclerView.layoutManager = LinearLayoutManager(this)

        // set up the button and input field, so that the user can enter a task and add it to the list

        val inputTextField = findViewById<EditText>(R.id.addTaskField)

        // get a reference to the button
        // and then set an OnClickListener
        findViewById<Button>(R.id.button).setOnClickListener {
            // 1. Grab the text the user has inputted into @id/addTaskField
            val userInputtedTask = inputTextField.text.toString()

            // notify the adapter that our data has been updated
            adapter.notifyItemInserted(listOfTasks.size)

            // 2. Add the string to our list of tasks: listOfTasks
            listOfTasks.add(userInputtedTask)
            // 3. Reset text field
            inputTextField.setText("")

            saveItems()
        }
    }

    // save the data that the user has inputted
    // by writing and reading from a file

    // get the file we need
    fun getDataFile() : File {

        // every line is going to represent a specific task in the list of tasks
        return File(filesDir, "data.txt")
    }

    // load the items by reading every line in the file
    fun loadItems() {
        try {
            listOfTasks = FileUtils.readLines(getDataFile(), Charset.defaultCharset())
        } catch (ioException : IOException) {
            ioException.printStackTrace()
        }
    }

    // save our items by writing them into our data file
    fun saveItems() {
        try {
            FileUtils.writeLines(getDataFile(), listOfTasks)
        } catch (ioException : IOException) {
            ioException.printStackTrace()
        }
    }
}