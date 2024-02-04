package dh2jeong.a2enhanced.Views

import dh2jeong.a2enhanced.Controllers.CreateCourseButton
import dh2jeong.a2enhanced.Model.Model
import javafx.geometry.Insets
import javafx.scene.layout.Background
import javafx.scene.layout.BackgroundFill
import javafx.scene.layout.HBox
import javafx.scene.layout.Priority
import javafx.scene.paint.Color

class NewCourseToolbar(private val model: Model) : HBox() {
    private val courseInputForm = CourseInputForm("", "", "")
    private val createCourseButton = CreateCourseButton(model, courseInputForm).apply {
        setMargin(this, Insets(5.0, 10.0, 5.0, 75.0))
        minWidth = 60.0
    }

    init {
        this.children.addAll(
            courseInputForm,
            createCourseButton
        )
        setHgrow(courseInputForm, Priority.ALWAYS)
        this.apply {
            padding = Insets(5.0)
            background = Background(BackgroundFill(Color.LIGHTGRAY, null, null))
        }
    }
}