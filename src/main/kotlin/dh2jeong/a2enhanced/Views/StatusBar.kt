package dh2jeong.a2enhanced.Views

import dh2jeong.a2enhanced.Model.Model
import javafx.beans.InvalidationListener
import javafx.beans.Observable
import javafx.collections.FXCollections
import javafx.geometry.Insets
import javafx.geometry.Orientation
import javafx.scene.control.Label
import javafx.scene.control.Separator
import javafx.scene.layout.Border
import javafx.scene.layout.BorderStroke
import javafx.scene.layout.BorderStrokeStyle
import javafx.scene.layout.HBox
import javafx.scene.paint.Color
import java.text.DecimalFormat
import java.math.RoundingMode

class StatusBar(private val model: Model) : HBox(), InvalidationListener {
    private var tempCourseListView = FXCollections.observableArrayList(model.CourseListView.value)
    private val averageLabel = Label("Course Average: 0.0").apply {
        padding = Insets(5.0, 5.0, 5.0, 15.0)
    }
    private val coursesTakenLabel = Label("Courses Taken: 0").apply {
        padding = Insets(5.0)
    }
    private val coursesFailedLabel = Label("Courses Failed: 0").apply {
        padding = Insets(5.0)
    }
    private val coursesWDLabel = Label("Courses WD'ed: 0").apply {
        padding = Insets(5.0)
    }

    init {
        model.CourseListView.addListener(this)
        this.children.addAll(
            averageLabel,
            Separator(Orientation.VERTICAL),
            coursesTakenLabel,
            Separator(Orientation.VERTICAL),
            coursesFailedLabel,
            Separator(Orientation.VERTICAL),
            coursesWDLabel
        )
        this.apply {
            border = (Border(BorderStroke(Color.GREY, BorderStrokeStyle.SOLID, null, null)))
        }
        invalidated(null)
    }

    private fun computeValues() {
        var sumOfCourses = 0.0
        var average = 0.0
        var coursesTaken = 0
        var coursesFailed = 0
        var coursesWD = 0
        val df = DecimalFormat("#.#").apply {
            roundingMode = RoundingMode.CEILING
        }
        tempCourseListView.forEach {
            if (it.getIntGrade() == -1) {
                coursesWD += 1
            } else {
                coursesTaken += 1
                sumOfCourses += it.getIntGrade().toDouble()
                if (it.getIntGrade() < 50) {
                    coursesFailed += 1
                }
            }
        }
        if (coursesTaken > 0) {
            average = sumOfCourses / coursesTaken
        }
        val roundedAverage = df.format(average)
        averageLabel.text = "Course Average: $roundedAverage"
        coursesTakenLabel.text = "Courses Taken: $coursesTaken"
        coursesFailedLabel.text = "Courses Failed: $coursesFailed"
        coursesWDLabel.text = "Courses WD'ed: $coursesWD"
    }

    override fun invalidated(observable: Observable?) {
        tempCourseListView = FXCollections.observableArrayList(model.CourseListView.value)
        computeValues()
    }
}