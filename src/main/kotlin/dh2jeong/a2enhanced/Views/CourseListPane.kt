package dh2jeong.a2enhanced.Views

import dh2jeong.a2enhanced.Model.Model
import javafx.beans.InvalidationListener
import javafx.beans.Observable
import javafx.collections.FXCollections
import javafx.collections.ObservableList
import javafx.geometry.Insets
import javafx.scene.control.ScrollPane
import javafx.scene.layout.*
import javafx.scene.paint.Color

class CourseListPane(private val model: Model) : VBox(), InvalidationListener {
    private val courseVBox = VBox().apply {
        maxWidth = Double.MAX_VALUE
        maxHeight = Double.MAX_VALUE
    }
    private val courseScroll = ScrollPane(courseVBox).apply {
        isFitToWidth = false
        isFitToHeight = false
        background = Background(BackgroundFill(Color.TRANSPARENT, null, null))
    }
    private var tempCourseListView = FXCollections.observableArrayList(model.CourseListView.value)

    init {
        model.CourseListView.addListener(this)
        model.SortByString.addListener(this)
        model.FilterString.addListener(this)
        invalidated(null)
        this.apply {
            VBox.setVgrow(courseVBox, Priority.ALWAYS)
        }
        this.children.add(courseScroll)
    }

    private fun filterCourses() {
        val filterString = model.FilterString.value
        if (filterString == "CS Courses") {
            tempCourseListView =
                FXCollections.observableArrayList(tempCourseListView.filter { it.getCourseCode().startsWith("CS") })
        }
        if (filterString == "Math Courses") {
            tempCourseListView =
                FXCollections.observableArrayList(tempCourseListView.filter {
                    (it.getCourseCode().startsWith("MATH") || it.getCourseCode()
                        .startsWith("STAT") || it.getCourseCode()
                        .startsWith("CO"))
                })
        }
        if (filterString == "Other") {
            tempCourseListView =
                FXCollections.observableArrayList(tempCourseListView.filter {
                    !(it.getCourseCode().startsWith("MATH") || it.getCourseCode()
                        .startsWith("STAT") || it.getCourseCode()
                        .startsWith("CO") || it.getCourseCode().startsWith("CS"))
                })
        }
    }

    private fun sortCourses() {
        val sortByString = model.SortByString.value
        val termSortOrder =
            mapOf("F20" to 1, "W21" to 2, "S21" to 3, "F21" to 4, "W22" to 5, "S22" to 6, "F22" to 7, "W23" to 8)
        if (sortByString == "Course Code") {
            tempCourseListView.sortWith(compareBy { it.getCourseCode() })
        }
        if (sortByString == "Term") {
            tempCourseListView.sortWith(compareBy { termSortOrder[it.getTerm()] })
        }
        if (sortByString == "Grade (asc)") {
            tempCourseListView.sortWith(compareBy { it.getIntGrade() })
        }
        if (sortByString == "Grade (desc)") {
            tempCourseListView.sortWith(compareByDescending { it.getIntGrade() })
        }
    }

    override fun invalidated(observable: Observable?) {
        courseVBox.children.clear()
        tempCourseListView = FXCollections.observableArrayList(model.CourseListView.value)
        sortCourses()
        tempCourseListView.forEach {
            courseVBox.children.add(it).apply {
                HBox.setHgrow(it, Priority.ALWAYS)
            }
            VBox.setMargin(it, Insets(5.0, 5.0, 5.0, 10.0))
        }
    }

}