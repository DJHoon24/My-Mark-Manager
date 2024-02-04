package dh2jeong.a2enhanced.Views

import dh2jeong.a2enhanced.Model.Model
import dh2jeong.a2enhanced.View.DegreeProgressView
import javafx.scene.control.*

class VisualTabsPane(private val model: Model) : TabPane() {
    private val averageByTerm = Tab("Average by Term", CourseAverageView(model)).apply {
        isClosable = false
        style = "-fx-font-size: 12;"
    }
    private val degreeProgress = Tab("Progress towards Degree", DegreeProgressView(model)).apply {
        isClosable = false
        style = "-fx-font-size: 12;"
    }
    private val courseOutcomes = Tab("Course Outcomes", CourseOutcomeView(model)).apply {
        isClosable = false
        style = "-fx-font-size: 12;"
    }
    private val incrementalAverage = Tab("Incremental Average", IncrementalAverageView(model)).apply {
        isClosable = false
        style = "-fx-font-size: 12;"
    }

    init {
        this.tabs.addAll(averageByTerm, degreeProgress, courseOutcomes, incrementalAverage)
    }
}