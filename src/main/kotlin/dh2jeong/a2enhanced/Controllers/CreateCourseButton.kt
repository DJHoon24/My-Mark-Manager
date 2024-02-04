package dh2jeong.a2enhanced.Controllers

import dh2jeong.a2enhanced.Model.Model
import dh2jeong.a2enhanced.Views.CourseInputForm
import javafx.beans.InvalidationListener
import javafx.beans.Observable
import javafx.event.EventHandler
import javafx.scene.control.Button

class CreateCourseButton(private val model: Model, private val courseInputForm: CourseInputForm) : Button("Create"),
    InvalidationListener {
    private val gradeTextArea = courseInputForm.getGradeTextArea()
    private val courseCodeTextArea = courseInputForm.getCourseCodeTextArea()
    private val termDropdown = courseInputForm.getTermDropdown()

    init {
        courseInputForm.ValidInput.addListener(this)
        onAction = EventHandler {
            if (!isCourseInList(courseCodeTextArea.text)) {
                model.addCourse(
                    courseCodeTextArea.text,
                    termDropdown.selectionModel.selectedItem,
                    gradeTextArea.text
                )
                courseCodeTextArea.clear()
                termDropdown.selectionModel.clearSelection()
                gradeTextArea.clear()
            }
        }
        isDisable = true
        invalidated(null)
    }

    override fun invalidated(observable: Observable?) {
        if (observable == courseInputForm.ValidInput) {
            isDisable = !courseInputForm.ValidInput.value
        }
    }

    private fun isCourseInList(courseCode: String): Boolean {
        val currentCourses = model.CourseListView.get()
        currentCourses.forEach {
            if (it.getCourseCode() == courseCode) {
                return true
            }
        }
        return false
    }

}