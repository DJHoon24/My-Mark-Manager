package dh2jeong.a2enhanced.Controllers

import dh2jeong.a2enhanced.Model.Model
import dh2jeong.a2enhanced.Views.CourseView
import javafx.beans.InvalidationListener
import javafx.beans.Observable
import javafx.event.EventHandler
import javafx.scene.control.Button

class EditCourseButton(private val model: Model, private val courseView: CourseView) : Button("Update"),
    InvalidationListener {
    private val courseInputForm = courseView.getCourseInputForm()
    private val gradeTextArea = courseInputForm.getGradeTextArea()
    private val termDropdown = courseInputForm.getTermDropdown()

    init {
        courseInputForm.ValidInput.addListener(this)
        courseInputForm.InputChanged.addListener(this)
        onAction = EventHandler {
            model.editCourse(
                courseView,
                termDropdown.selectionModel.selectedItem,
                gradeTextArea.text
            )
        }
        isDisable = true
        invalidated(null)
    }

    override fun invalidated(observable: Observable?) {
        if (observable == courseInputForm.ValidInput) {
            isDisable = !(courseInputForm.ValidInput.value && courseInputForm.InputChanged.value)
        } else if (observable == courseInputForm.InputChanged) {
            isDisable = !(courseInputForm.ValidInput.value && courseInputForm.InputChanged.value)
        }
    }
}