package dh2jeong.a2enhanced.Views

import dh2jeong.a2enhanced.Model.Model
import javafx.beans.InvalidationListener
import javafx.beans.Observable
import javafx.collections.FXCollections
import javafx.scene.canvas.Canvas
import javafx.scene.canvas.GraphicsContext
import javafx.scene.layout.Pane
import javafx.scene.paint.Color
import kotlin.math.pow

class IncrementalAverageView(private val model: Model) : Pane(), InvalidationListener {
    private val canvas = Canvas().apply {
        heightProperty().addListener { _ -> drawGraph(width, height) }
        widthProperty().addListener { _ -> drawGraph(width, height) }
    }
    private val termList = listOf("F20", "W21", "S21", "F21", "W22", "S22", "F22", "W23")
    private val termSortOrder =
        mapOf("F20" to 0, "W21" to 1, "S21" to 2, "F21" to 3, "W22" to 4, "S22" to 5, "F22" to 6, "W23" to 7)
    private var tempCourseListView = FXCollections.observableArrayList(model.CourseListView.value)
    private var heightLineLength = height * .8
    private var heightOffset = (height - heightLineLength) / 2
    private var splitHeight = heightLineLength / 10
    private var widthLineLength = width * .8
    private var widthOffset = (width - widthLineLength) / 2

    init {
        model.CourseListView.addListener(this)
        this.children.add(canvas)
        canvas.widthProperty().bind(this.widthProperty())
        canvas.heightProperty().bind(this.heightProperty())
        invalidated(null)
    }

    private fun drawGraph(width: Double, height: Double) {
        val graphicsContext = canvas.graphicsContext2D
        graphicsContext.clearRect(0.0, 0.0, width, height)
        heightLineLength = height * .8
        heightOffset = (height - heightLineLength) / 2
        splitHeight = heightLineLength / 10
        widthLineLength = width * .8
        widthOffset = (width - widthLineLength) / 2
        for (i in 1..10) { // Draw Horizontal lines
            graphicsContext.stroke = Color.LIGHTGRAY
            graphicsContext.strokeLine(
                widthOffset,
                (heightOffset + (splitHeight * (i - 1))),
                (width - widthOffset),
                (heightOffset + (splitHeight * (i - 1)))
            )
            graphicsContext.stroke = Color.BLACK
            graphicsContext.strokeText("${i * 10}", (widthOffset - 20), (heightOffset + (splitHeight * (10 - i))))
        }
        graphicsContext.strokeLine(
            widthOffset,
            heightOffset,
            widthOffset,
            (heightOffset + (splitHeight * 10))
        )
        graphicsContext.strokeLine(
            widthOffset,
            (heightOffset + (splitHeight * 10)),
            (width - widthOffset),
            (heightOffset + (splitHeight * 10))
        )
        var minTermIndex = 99
        var maxTermIndex = -1
        var currentTerm = ""
        if (!tempCourseListView.isEmpty()) {
            minTermIndex = termSortOrder[tempCourseListView[0].getTerm()]!!
            maxTermIndex = termSortOrder[tempCourseListView[tempCourseListView.lastIndex].getTerm()]!!
            currentTerm = tempCourseListView[0].getTerm()
        }
        val tempTermList = mutableListOf<String>()
        val tempTermSortOrder = mutableMapOf<String, Int>()
        for (i in minTermIndex..maxTermIndex) {
            tempTermList.add(termList[i])
            tempTermSortOrder[termList[i]] = i - minTermIndex
        }
        val totalTerms = (maxTermIndex - minTermIndex) + 1
        val termsWidth = widthLineLength / (totalTerms + 1)
        for ((i, termName) in tempTermList.withIndex()) {
            graphicsContext.strokeText(
                termName,
                (widthOffset + (termsWidth * (i + 1))),
                ((heightOffset + (splitHeight * 10)) + 15)
            )
        }
        val cumulativeCourseList = mutableListOf<CourseView>()
        var totalGrade = 0
        var totalCoursesTaken = 0
        var previousTotalTaken = 0
        tempCourseListView.forEach {
            if (currentTerm != it.getTerm()) {
                if (totalCoursesTaken != previousTotalTaken) {
                    drawTerm(
                        graphicsContext,
                        currentTerm,
                        cumulativeCourseList,
                        totalGrade,
                        totalCoursesTaken,
                        termsWidth,
                        tempTermSortOrder,
                    )
                }
                currentTerm = it.getTerm()
                previousTotalTaken = totalCoursesTaken
            }
            if (it.getIntGrade() != -1) {
                totalGrade += it.getIntGrade()
                totalCoursesTaken += 1
                cumulativeCourseList.add(it)
            }
        }
        if (totalCoursesTaken != previousTotalTaken) {
            drawTerm(
                graphicsContext,
                currentTerm,
                cumulativeCourseList,
                totalGrade,
                totalCoursesTaken,
                termsWidth,
                tempTermSortOrder,
            )
        }
    }

    private fun drawTerm(
        graphicsContext: GraphicsContext,
        currentTerm: String,
        cumulativeCourseList: List<CourseView>,
        totalGrade: Int,
        totalCoursesTaken: Int,
        termsWidth: Double,
        tempTermSortOrder: Map<String, Int>,
    ) {
        cumulativeCourseList.forEach { currentCourse ->
            val grade = currentCourse.getIntGrade()
            graphicsContext.stroke = getCircleColor(grade)
            graphicsContext.strokeOval(
                (widthOffset + (termsWidth * (tempTermSortOrder[currentTerm]!! + 1)) + 5.0),
                (heightOffset + (heightLineLength * (1.0 - (grade / 100.0))) - 5.0),
                10.0,
                10.0
            )
        }
        val cumulativeAverage = totalGrade / totalCoursesTaken
        graphicsContext.fill = getCircleColor(cumulativeAverage)
        graphicsContext.fillOval(
            (widthOffset + (termsWidth * (tempTermSortOrder[currentTerm]!! + 1)) + 5.0),
            (heightOffset + (heightLineLength * (1.0 - (cumulativeAverage / 100.0))) - 5.0),
            10.0,
            10.0
        )
        graphicsContext.stroke = Color.BLACK
        graphicsContext.strokeOval(
            (widthOffset + (termsWidth * (tempTermSortOrder[currentTerm]!! + 1)) + 5.0),
            (heightOffset + (heightLineLength * (1.0 - (cumulativeAverage / 100.0))) - 5.0),
            10.0,
            10.0
        )
        var sumOfStandardDeviation = 0.0
        cumulativeCourseList.forEach { currentCourse ->
            val grade = currentCourse.getIntGrade()
            sumOfStandardDeviation += (grade - cumulativeAverage).toDouble().pow(2.0)
        }
        val standardDeviation = (sumOfStandardDeviation / cumulativeCourseList.size).pow(0.5)
        if (standardDeviation != 0.0) {
            graphicsContext.strokeLine(
                (widthOffset + (termsWidth * (tempTermSortOrder[currentTerm]!! + 1)) + 10.0),
                (heightOffset + (heightLineLength * (1.0 - (cumulativeAverage / 100.0))) - 5.0),
                (widthOffset + (termsWidth * (tempTermSortOrder[currentTerm]!! + 1)) + 10.0),
                (heightOffset + (heightLineLength * (1.0 - (cumulativeAverage / 100.0))) - (heightLineLength * (standardDeviation / 100.0)))
            )
            graphicsContext.strokeLine(
                (widthOffset + (termsWidth * (tempTermSortOrder[currentTerm]!! + 1)) + 10.0),
                (heightOffset + (heightLineLength * (1.0 - (cumulativeAverage / 100.0))) + 5.0),
                (widthOffset + (termsWidth * (tempTermSortOrder[currentTerm]!! + 1)) + 10.0),
                (heightOffset + (heightLineLength * (1.0 - (cumulativeAverage / 100.0))) + (heightLineLength * (standardDeviation / 100.0)))
            )
            graphicsContext.strokeLine(
                (widthOffset + (termsWidth * (tempTermSortOrder[currentTerm]!! + 1)) + 5.0),
                (heightOffset + (heightLineLength * (1.0 - (cumulativeAverage / 100.0))) + (heightLineLength * (standardDeviation / 100.0))),
                (widthOffset + (termsWidth * (tempTermSortOrder[currentTerm]!! + 1)) + 15.0),
                (heightOffset + (heightLineLength * (1.0 - (cumulativeAverage / 100.0))) + (heightLineLength * (standardDeviation / 100.0)))
            )
            graphicsContext.strokeLine(
                (widthOffset + (termsWidth * (tempTermSortOrder[currentTerm]!! + 1)) + 5.0),
                (heightOffset + (heightLineLength * (1.0 - (cumulativeAverage / 100.0))) - (heightLineLength * (standardDeviation / 100.0))),
                (widthOffset + (termsWidth * (tempTermSortOrder[currentTerm]!! + 1)) + 15.0),
                (heightOffset + (heightLineLength * (1.0 - (cumulativeAverage / 100.0))) - (heightLineLength * (standardDeviation / 100.0)))
            )
        }
    }

    private fun getCircleColor(grade: Int): Color {
        if (grade < 50) {
            return Color.LIGHTCORAL
        } else if (grade < 60) {
            return Color.LIGHTBLUE
        } else if (grade < 91) {
            return Color.LIGHTGREEN
        } else if (grade < 96) {
            return Color.SILVER
        }
        return Color.GOLD
    }

    override fun invalidated(observable: Observable?) {
        tempCourseListView = FXCollections.observableArrayList(model.CourseListView.value)
        tempCourseListView.sortWith(compareBy { termSortOrder[it.getTerm()] })
        drawGraph(canvas.width, canvas.height)
    }
}