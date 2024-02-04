package dh2jeong.a2enhanced.Controllers

import dh2jeong.a2enhanced.Model.Model
import dh2jeong.a2enhanced.Views.CourseView
import javafx.beans.InvalidationListener
import javafx.beans.Observable
import javafx.event.EventHandler
import javafx.scene.control.Button

class DeleteCourseButton(private val model: Model, private val courseView: CourseView) : Button("Delete"),
    InvalidationListener {
    private var isDelete = true
    private val courseInputForm = courseView.getCourseInputForm()
    private val gradeTextArea = courseInputForm.getGradeTextArea()
    private val termDropdown = courseInputForm.getTermDropdown()

    init {
        courseInputForm.InputChanged.addListener(this)
        onAction = EventHandler {
            if (isDelete) {
                model.removeCourse(courseView)
            } else {
                termDropdown.selectionModel.select(courseView.getTerm())
                gradeTextArea.text = courseView.getGrade()
            }
        }
        invalidated(null)
    }

    override fun invalidated(observable: Observable?) {
        if (observable == courseInputForm.InputChanged) {
            if (courseInputForm.InputChanged.value) {
                isDelete = false
                text = "Undo"
            } else {
                isDelete = true
                text = "Delete"
            }
        }
    }
}