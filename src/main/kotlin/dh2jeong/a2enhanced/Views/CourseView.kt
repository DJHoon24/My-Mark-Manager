package dh2jeong.a2enhanced.Views

import dh2jeong.a2enhanced.Controllers.DeleteCourseButton
import dh2jeong.a2enhanced.Controllers.EditCourseButton
import dh2jeong.a2enhanced.Model.Model
import javafx.geometry.Insets
import javafx.scene.layout.Background
import javafx.scene.layout.BackgroundFill
import javafx.scene.layout.HBox
import javafx.scene.layout.Priority
import javafx.scene.paint.Color

class CourseView(model: Model, courseCode: String, term: String, grade: String) : HBox() {
    private val courseCode = courseCode
    private val term = term
    private val grade = grade
    private val courseInputForm = CourseInputForm(courseCode, term, grade).apply {
        HBox.setHgrow(this, Priority.ALWAYS)
    }
    private val editButton = EditCourseButton(model, this).apply {
        HBox.setMargin(this, Insets(5.0))
        minWidth = 60.0
    }
    private val deleteButton = DeleteCourseButton(model, this).apply {
        HBox.setMargin(this, Insets(5.0))
        minWidth = 60.0
    }

    init {
        this.children.addAll(courseInputForm, editButton, deleteButton)
        this.apply {
            padding = Insets(5.0, 10.0, 5.0, 5.0)
        }
        this.setBackground()
    }

    fun getCourseCode(): String {
        return courseCode
    }

    fun getTerm(): String {
        return term
    }

    fun getGrade(): String {
        return grade
    }

    fun getIntGrade(): Int {
        if (grade == "WD") {
            return -1
        }
        return grade.toInt()
    }

    private fun setBackground() {
        val currentGrade = this.getIntGrade()
        if (currentGrade == -1) {
            this.background = Background(BackgroundFill(Color.DARKSLATEGRAY, null, null))
        } else if (currentGrade < 50) {
            this.background = Background(BackgroundFill(Color.LIGHTCORAL, null, null))
        } else if (currentGrade < 60) {
            this.background = Background(BackgroundFill(Color.LIGHTBLUE, null, null))
        } else if (currentGrade < 91) {
            this.background = Background(BackgroundFill(Color.LIGHTGREEN, null, null))
        } else if (currentGrade < 96) {
            this.background = Background(BackgroundFill(Color.SILVER, null, null))
        } else {
            this.background = Background(BackgroundFill(Color.GOLD, null, null))
        }
    }

    fun getCourseInputForm(): CourseInputForm {
        return this.courseInputForm
    }

    fun printCourse() {
        println(courseCode)
        println(term)
        println(grade)
    }
}