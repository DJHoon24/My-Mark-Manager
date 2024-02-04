package dh2jeong.a2enhanced.View

import dh2jeong.a2enhanced.Model.Model
import javafx.beans.InvalidationListener
import javafx.beans.Observable
import javafx.collections.FXCollections
import javafx.event.EventHandler
import javafx.scene.Group
import javafx.scene.input.MouseEvent
import javafx.scene.layout.Pane
import javafx.scene.paint.Color
import javafx.scene.shape.Line
import javafx.scene.shape.Rectangle
import javafx.scene.text.Text

class DegreeProgressView(private val model: Model) : Pane(), InvalidationListener {
    private val group = Group()
    private var tempCourseListView = FXCollections.observableArrayList(model.CourseListView.value)
    private val totalRequiredCSCredits = 11.0
    private val totalRequiredMathCredits = 4.0
    private val totalRequiredOtherCredits = 5.0
    private val totalRequiredCredits = 20.0
    private var csCredits = 0.0
    private var mathCredits = 0.0
    private var otherCredits = 0.0
    private var credits = 0.0
    private var courseListCS = MutableList<String>(0){""}
    private var courseListMath = MutableList<String>(0){""}
    private var courseListOther = MutableList<String>(0){""}
    private var courseListAllPassed = MutableList<String>(0){""}

    init {
        model.CourseListView.addListener(this)
        children.add(group)
        heightProperty().addListener { _ -> drawGraph(width, height) }
        widthProperty().addListener { _ -> drawGraph(width, height) }
        invalidated(null)
    }

    private fun drawGraph(width: Double, height: Double) {
        group.children.clear()
        val widthLineLength = width * .7
        val widthOffset = (width - widthLineLength) / 2
        val verticalLineWidth = widthLineLength / 4
        val heightLineLength = height * .8
        val heightOffset = (height - heightLineLength) / 2
        val rectangleHeight = heightLineLength / 11
        val coursesString = Text(widthOffset + (4 * verticalLineWidth) + 5.0, heightOffset, "")
        group.children.add(coursesString)
        group.children.addAll(
            Rectangle(
                widthOffset,
                heightOffset + (2 * rectangleHeight),
                (widthLineLength * (csCredits / totalRequiredCredits)),
                rectangleHeight
            ).apply {
                fill = Color.YELLOW
                onMouseEntered = courseStringEventHandler(courseListCS, coursesString)
                onMouseExited = clearCourseStringEventHandler(coursesString)
            },
            Rectangle(
                widthOffset + (widthLineLength * (csCredits / totalRequiredCredits)),
                heightOffset + (2 * rectangleHeight),
                (widthLineLength * ((totalRequiredCSCredits - csCredits) / totalRequiredCredits)),
                rectangleHeight
            ).apply {
                fill = Color.LIGHTYELLOW
            },
            Text(widthOffset - 60.0, (heightOffset + (2 * rectangleHeight) + (rectangleHeight / 2)), "CS"),
            Rectangle(
                widthOffset,
                heightOffset + (4 * rectangleHeight),
                (widthLineLength * (mathCredits / totalRequiredCredits)),
                rectangleHeight
            ).apply {
                fill = Color.DEEPPINK
                onMouseEntered = courseStringEventHandler(courseListMath, coursesString)
                onMouseExited = clearCourseStringEventHandler(coursesString)
            },
            Rectangle(
                widthOffset + (widthLineLength * (mathCredits / totalRequiredCredits)),
                heightOffset + (4 * rectangleHeight),
                (widthLineLength * ((totalRequiredMathCredits - mathCredits) / totalRequiredCredits)),
                rectangleHeight
            ).apply {
                fill = Color.LIGHTPINK
            },
            Text(widthOffset - 60.0, (heightOffset + (4 * rectangleHeight) + (rectangleHeight / 2)), "MATH"),
            Rectangle(
                widthOffset,
                heightOffset + (6 * rectangleHeight),
                (widthLineLength * (otherCredits / totalRequiredCredits)),
                rectangleHeight
            ).apply {
                fill = Color.GREY
                onMouseEntered = courseStringEventHandler(courseListOther, coursesString)
                onMouseExited = clearCourseStringEventHandler(coursesString)
            },
            Rectangle(
                widthOffset + (widthLineLength * (otherCredits / totalRequiredCredits)),
                heightOffset + (6 * rectangleHeight),
                (widthLineLength * ((totalRequiredOtherCredits - otherCredits) / totalRequiredCredits)),
                rectangleHeight
            ).apply {
                fill = Color.LIGHTGREY
            },
            Text(widthOffset - 60.0, (heightOffset + (6 * rectangleHeight) + (rectangleHeight / 2)), "OTHER"),
            Rectangle(
                widthOffset,
                heightOffset + (8 * rectangleHeight),
                (widthLineLength * (credits / totalRequiredCredits)),
                rectangleHeight
            ).apply {
                fill = Color.GREEN
                onMouseEntered = courseStringEventHandler(courseListAllPassed, coursesString)
                onMouseExited = clearCourseStringEventHandler(coursesString)
            },
            Rectangle(
                widthOffset + (widthLineLength * (credits / totalRequiredCredits)),
                heightOffset + (8 * rectangleHeight),
                (widthLineLength * ((totalRequiredCredits - credits) / totalRequiredCredits)),
                rectangleHeight
            ).apply {
                fill = Color.LIGHTGREEN
            },
            Text(widthOffset - 60.0, (heightOffset + (8 * rectangleHeight) + (rectangleHeight / 2)), "TOTAL")
        )
        for (i in 0..4) {
            group.children.addAll(
                Line(
                    (widthOffset + (i * verticalLineWidth)),
                    heightOffset,
                    (widthOffset + (i * verticalLineWidth)),
                    (height - heightOffset)
                ),
                Text((widthOffset + (i * verticalLineWidth) - 10.0), (height - heightOffset + 15.0), "${5.0 * i}")
            )
        }
    }

    private fun courseStringEventHandler(courseList : List<String>, coursesString : Text) : EventHandler<MouseEvent> {
        return EventHandler {
            var newString = "Courses:\n"
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

    private fun computeTotals() {
        csCredits = 0.0
        mathCredits = 0.0
        otherCredits = 0.0
        credits = 0.0
        courseListCS.clear()
        courseListMath.clear()
        courseListOther.clear()
        courseListAllPassed.clear()
        tempCourseListView.forEach {
            if (it.getIntGrade() >= 50) {
                if (it.getCourseCode().startsWith("CS")) {
                    csCredits += 0.5
                    courseListCS.add(it.getCourseCode())
                } else if (it.getCourseCode().startsWith("MATH") ||
                    it.getCourseCode().startsWith("STAT") || it.getCourseCode().startsWith("CO")
                ) {
                    courseListMath.add(it.getCourseCode())
                    mathCredits += 0.5
                } else {
                    courseListOther.add(it.getCourseCode())
                    otherCredits += 0.5
                }
                courseListAllPassed.add(it.getCourseCode())
                credits += 0.5
            }
        }
    }

    override fun invalidated(observable: Observable?) {
        tempCourseListView = FXCollections.observableArrayList(model.CourseListView.value)
        computeTotals()
        drawGraph(width, height)
    }
}