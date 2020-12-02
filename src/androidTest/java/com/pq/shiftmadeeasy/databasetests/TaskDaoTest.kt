package com.pq.shiftmadeeasy.databasetests

import androidx.room.Room
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.pq.shiftmadeeasy.localdatabase.AppDatabase
import com.pq.shiftmadeeasy.localdatabase.task.Task
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.util.*

@RunWith(AndroidJUnit4::class)
class TaskDaoTest {

    private lateinit var appDatabase: AppDatabase

    @Before
    fun initDb(){
        appDatabase = Room.inMemoryDatabaseBuilder(
            InstrumentationRegistry.getInstrumentation().context,
            AppDatabase::class.java
        ).build() }

    @After
    fun closeDb(){
        appDatabase.close()
    }

    @Test
    fun insertTaskTest(){
        val task = Task(
            taskDate = Calendar.getInstance().timeInMillis.toString(),
            taskFromTime =
        )

    }
}