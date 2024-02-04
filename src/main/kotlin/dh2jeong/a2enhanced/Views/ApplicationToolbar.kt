package dh2jeong.a2enhanced.Views

import dh2jeong.a2enhanced.Model.Model
import javafx.geometry.Insets
import javafx.scene.control.Separator
import javafx.geometry.Orientation
import javafx.scene.layout.VBox

class ApplicationToolbar(private val model: Model) : VBox() {
    private val newCourseToolbar = NewCourseToolbar(model)
    private val courseList = CourseListPane(model)
    init {
        this.children.addAll(newCourseToolbar, Separator(Orientation.HORIZONTAL), courseList)
        setMargin(newCourseToolbar, Insets(10.0, 25.0, 10.0, 10.0))
    }

}