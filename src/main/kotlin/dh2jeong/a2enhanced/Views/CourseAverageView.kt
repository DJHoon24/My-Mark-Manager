package dh2jeong.a2enhanced.Views

import dh2jeong.a2enhanced.Model.Model
import javafx.beans.InvalidationListener
import javafx.beans.Observable
import javafx.collections.FXCollections
import javafx.scene.canvas.Canvas
import javafx.scene.layout.Pane
import javafx.scene.paint.Color

class CourseAverageView(private val model: Model) : Pane(), InvalidationListener {
    private val canvas = Canvas().apply {
        heightProperty().addListener { _ -> drawGraph(width, height) }
        widthProperty().addListener { _ -> drawGraph(width, height) }
    }
    private val termList = listOf("F20", "W21", "S21", "F21", "W22", "S22", "F22", "W23")
    private val termSortOrder =
        mapOf("F20" to 0, "W21" to 1, "S21" to 2, "F21" to 3, "W22" to 4, "S22" to 5, "F22" to 6, "W23" to 7)
    private var tempCourseListView = FXCollections.observableArrayList(model.CourseListView.value)
    private var termCourseAverages = MutableList(8) { -1 }
    private var minTermIndex = 99
    private var maxTermIndex = -1


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
        val heightLineLength = height * .8
        val heightOffset = (height - heightLineLength) / 2
        val splitHeight = heightLineLength / 10
        val widthLineLength = width * .8
        val widthOffset = (width - widthLineLength) / 2
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
        val tempTermList = mutableListOf<String>()
        for (i in minTermIndex..maxTermIndex) {
            tempTermList.add(termList[i])
        }
        val totalTerms = (maxTermIndex - minTermIndex) + 1
        val termsWidth = widthLineLength / (totalTerms + 1)
        val averageCoordinateList = MutableList<Pair<Double, Double>>(0) { Pair(0.0, 0.0) }
        for ((i, termName) in tempTermList.withIndex()) {
            graphicsContext.strokeText(
                termName,
                (widthOffset + (termsWidth * (i + 1))),
                ((heightOffset + (splitHeight * 10)) + 15)
            )
            val currentTermAverage = termCourseAverages[termSortOrder[termName]!!]
            if (currentTermAverage != -1) {
                val averageX = (widthOffset + (termsWidth * (i + 1)) + 5.0)
                val averageY = (heightOffset + (heightLineLength * (1.0 - (currentTermAverage / 100.0))) - 5.0)
                averageCoordinateList.add(Pair(averageX, averageY))
                graphicsContext.fill = getCircleColor(currentTermAverage)
                graphicsContext.fillOval(
                    averageX,
                    averageY,
                    10.0,
                    10.0
                )
            }
        }
        var prevX = 0.0
        var prevY = 0.0
        if (averageCoordinateList.isNotEmpty()) {
            prevX = averageCoordinateList[0].first
            prevY = averageCoordinateList[0].second
        }
        for (i in 1 until averageCoordinateList.size) {
            var currX = averageCoordinateList[i].first
            var currY = averageCoordinateList[i].second
            graphicsContext.strokeLine(prevX + 5.0, prevY + 5.0, currX + 5.0, currY + 5.0)
            prevX = currX
            prevY = currY
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

    private fun computeAverages() {
        minTermIndex = 99
        maxTermIndex = -1
        val termCourseCounts = MutableList(8) { 0 }
        val termCourseTotalGrades = MutableList(8) { 0 }
        termCourseAverages = MutableList(8) { -1 }
        tempCourseListView.forEach {
            val index = termSortOrder[it.getTerm()]!!
            if (index > maxTermIndex) {
                maxTermIndex = index
            }
            if (index < minTermIndex) {
                minTermIndex = index
            }
            val courseGrade = it.getIntGrade()
            if (courseGrade != -1) {
                termCourseTotalGrades[index] += courseGrade
                termCourseCounts[index] += 1
            }
        }
        for ((i, grade) in termCourseTotalGrades.withIndex()) {
            if (termCourseCounts[i] > 0) {
                termCourseAverages[i] = (grade / termCourseCounts[i])
            }
        }
    }

    override fun invalidated(observable: Observable?) {
        tempCourseListView = FXCollections.observableArrayList(model.CourseListView.value)
        computeAverages()
        drawGraph(canvas.width, canvas.height)
    }
}