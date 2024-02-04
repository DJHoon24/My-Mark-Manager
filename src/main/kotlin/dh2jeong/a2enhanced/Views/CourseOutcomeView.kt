package dh2jeong.a2enhanced.Views

import dh2jeong.a2enhanced.Model.Model
import javafx.beans.InvalidationListener
import javafx.beans.Observable
import javafx.collections.FXCollections
import javafx.event.EventHandler
import javafx.geometry.Insets
import javafx.geometry.Pos
import javafx.scene.Group
import javafx.scene.control.CheckBox
import javafx.scene.input.MouseEvent
import javafx.scene.layout.BorderPane
import javafx.scene.layout.Pane
import javafx.scene.layout.VBox
import javafx.scene.paint.Color
import javafx.scene.shape.Arc
import javafx.scene.shape.ArcType
import javafx.scene.shape.Circle
import javafx.scene.text.Font
import javafx.scene.text.Text
import java.lang.Double.min

class CourseOutcomeView(private val model: Model) : BorderPane(), InvalidationListener {
    private var isShowAllCourses = false
    private val showAllCoursesCheckbox = CheckBox("Include Missing Courses").apply {
        onAction = EventHandler {
            isShowAllCourses = !isShowAllCourses
            invalidated(null)
        }
        padding = Insets(5.0)
    }
    private val bottomBar = VBox(showAllCoursesCheckbox).apply {
        alignment = Pos.CENTER
    }
    private var tempCourseListView = FXCollections.observableArrayList(model.CourseListView.value)
    private val group = Group()
    private val viewPane = Pane(group).apply {
        heightProperty().addListener { _ -> drawGraph(this.width, this.height) }
        widthProperty().addListener { _ -> drawGraph(this.width, this.height) }
    }
    private var totalPassedCourses = 0
    private var totalCourses = 0
    private val courseListWD = MutableList<String>(0) { "" }
    private val courseListFailedCourses = MutableList<String>(0) { "" }
    private val courseListGoldCourses = MutableList<String>(0) { "" }
    private val courseListSilverCourses = MutableList<String>(0) { "" }
    private val courseListGreenCourses = MutableList<String>(0) { "" }
    private val courseListBlueCourses = MutableList<String>(0) { "" }

    init {
        model.CourseListView.addListener(this)
        center = viewPane
        bottom = bottomBar
        invalidated(null)
    }

    private fun drawGraph(width: Double, height: Double) {
        group.children.clear()
        var graphTotalCourses = totalCourses
        var startingAngle = 0.0
        val missingCourses = 40 - totalPassedCourses
        if (isShowAllCourses) {
            graphTotalCourses = totalCourses + missingCourses
        }
        val circleRadius = min((.45 * width), (.45 * height))
        val coursesString = Text((width / 2) - circleRadius, (height / 2) - circleRadius, "")
        if (graphTotalCourses != 0) {
            group.children.add(Circle(width / 2, height / 2, circleRadius).apply {
                stroke = Color.BLACK
            })
            if (courseListWD.isNotEmpty()) {
                var wdCoursesAngle = 360.0 * (courseListWD.size.toDouble() / graphTotalCourses)
                group.children.add(
                    Arc(
                        width / 2,
                        height / 2,
                        circleRadius,
                        circleRadius,
                        startingAngle,
                        wdCoursesAngle
                    ).apply {
                        type = ArcType.ROUND
                        fill = Color.DARKSLATEGRAY
                        strokeWidth = 0.0
                        onMouseEntered = courseStringEventHandler(courseListWD, coursesString)
                        onMouseExited = clearCourseStringEventHandler(coursesString)
                    })
                startingAngle += wdCoursesAngle
            }
            if (courseListFailedCourses.isNotEmpty()) {
                var failedCoursesAngle = 360.0 * (courseListFailedCourses.size.toDouble() / graphTotalCourses)
                group.children.add(
                    Arc(
                        width / 2,
                        height / 2,
                        circleRadius,
                        circleRadius,
                        startingAngle,
                        failedCoursesAngle
                    ).apply {
                        type = ArcType.ROUND
                        fill = Color.LIGHTCORAL
                        strokeWidth = 0.0
                        onMouseEntered = courseStringEventHandler(courseListFailedCourses, coursesString)
                        onMouseExited = clearCourseStringEventHandler(coursesString)
                    })
                startingAngle += failedCoursesAngle
            }
            if (courseListBlueCourses.isNotEmpty()) {
                var blueCoursesAngle = 360.0 * (courseListBlueCourses.size.toDouble() / graphTotalCourses)
                group.children.add(
                    Arc(
                        width / 2,
                        height / 2,
                        circleRadius,
                        circleRadius,
                        startingAngle,
                        blueCoursesAngle
                    ).apply {
                        type = ArcType.ROUND
                        fill = Color.LIGHTBLUE
                        strokeWidth = 0.0
                        onMouseEntered = courseStringEventHandler(courseListBlueCourses, coursesString)
                        onMouseExited = clearCourseStringEventHandler(coursesString)
                    })
                startingAngle += blueCoursesAngle
            }
            if (courseListGreenCourses.isNotEmpty()) {
                var greenCoursesAngle = 360.0 * (courseListGreenCourses.size.toDouble() / graphTotalCourses)
                group.children.add(
                    Arc(
                        width / 2,
                        height / 2,
                        circleRadius,
                        circleRadius,
                        startingAngle,
                        greenCoursesAngle
                    ).apply {
                        type = ArcType.ROUND
                        fill = Color.LIGHTGREEN
                        strokeWidth = 0.0
                        onMouseEntered = courseStringEventHandler(courseListGreenCourses, coursesString)
                        onMouseExited = clearCourseStringEventHandler(coursesString)
                    })
                startingAngle += greenCoursesAngle
            }
            if (courseListSilverCourses.isNotEmpty()) {
                var silverCoursesAngle = 360.0 * (courseListSilverCourses.size.toDouble() / graphTotalCourses)
                group.children.add(
                    Arc(
                        width / 2,
                        height / 2,
                        circleRadius,
                        circleRadius,
                        startingAngle,
                        silverCoursesAngle
                    ).apply {
                        type = ArcType.ROUND
                        fill = Color.SILVER
                        strokeWidth = 0.0
                        onMouseEntered = courseStringEventHandler(courseListSilverCourses, coursesString)
                        onMouseExited = clearCourseStringEventHandler(coursesString)
                    })
                startingAngle += silverCoursesAngle
            }
            if (courseListGoldCourses.isNotEmpty()) {
                var goldCoursesAngle = 360.0 * (courseListGoldCourses.size.toDouble() / graphTotalCourses)
                group.children.add(
                    Arc(
                        width / 2,
                        height / 2,
                        circleRadius,
                        circleRadius,
                        startingAngle,
                        goldCoursesAngle
                    ).apply {
                        type = ArcType.ROUND
                        fill = Color.GOLD
                        strokeWidth = 0.0
                        onMouseEntered = courseStringEventHandler(courseListGoldCourses, coursesString)
                        onMouseExited = clearCourseStringEventHandler(coursesString)
                    })
                startingAngle += goldCoursesAngle
            }
            if (isShowAllCourses) {
                var missingCoursesAngle = 360.0 * (missingCourses.toDouble() / graphTotalCourses)
                group.children.add(
                    Arc(
                        width / 2,
                        height / 2,
                        circleRadius,
                        circleRadius,
                        startingAngle,
                        missingCoursesAngle
                    ).apply {
                        type = ArcType.ROUND
                        fill = Color.WHITE
                        strokeWidth = 0.0
                    })
                startingAngle += missingCoursesAngle
            }
        }
        group.children.add(coursesString)
    }

    private fun computeGroups() {
        courseListWD.clear()
        courseListFailedCourses.clear()
        courseListBlueCourses.clear()
        courseListGreenCourses.clear()
        courseListSilverCourses.clear()
        courseListGoldCourses.clear()
        totalPassedCourses = 0
        totalCourses = 0
        tempCourseListView.forEach {
            val grade = it.getIntGrade()
            val courseCode = it.getCourseCode()
            if (grade == -1) {
                courseListWD.add(courseCode)
            } else if (grade < 50) {
                courseListFailedCourses.add(courseCode)
            } else if (grade < 60) {
                courseListBlueCourses.add(courseCode)
                totalPassedCourses += 1
            } else if (grade < 91) {
                courseListGreenCourses.add(courseCode)
                totalPassedCourses += 1
            } else if (grade < 96) {
                courseListSilverCourses.add(courseCode)
                totalPassedCourses += 1
            } else {
                courseListGoldCourses.add(courseCode)
                totalPassedCourses += 1
            }
            totalCourses += 1
        }
    }

    private fun courseStringEventHandler(courseList : List<String>, coursesString : Text) : EventHandler<MouseEvent> {
        return EventHandler {
            var newString = ""
            courseList.forEach {
                newString += it + "\n"
            }
            coursesString.text = newString
        }
    }

    private fun clearCourseStringEventHandler(coursesString : Text) : EventHandler<MouseEvent> {
        return EventHandler {
            coursesString.text = ""
        }
    }

    override fun invalidated(observable: Observable?) {
        tempCourseListView = FXCollections.observableArrayList(model.CourseListView.value)
        computeGroups()
        drawGraph(viewPane.width, viewPane.height)
    }
}