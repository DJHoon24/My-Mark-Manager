package dh2jeong.a2enhanced.Model

import dh2jeong.a2enhanced.Views.CourseView
import javafx.beans.property.ReadOnlyListProperty
import javafx.beans.property.ReadOnlyListWrapper
import javafx.beans.property.ReadOnlyStringProperty
import javafx.beans.property.ReadOnlyStringWrapper
import javafx.collections.FXCollections

class Model {
    private var filterString = ReadOnlyStringWrapper("All Courses")
    val FilterString: ReadOnlyStringProperty = filterString.readOnlyProperty
    private var sortByString = ReadOnlyStringWrapper("Term")
    val SortByString: ReadOnlyStringProperty = sortByString.readOnlyProperty
    private var courseViewList = ReadOnlyListWrapper(FXCollections.observableList(mutableListOf<CourseView>()))
    val CourseListView: ReadOnlyListProperty<CourseView> = courseViewList.readOnlyProperty

    fun setFilterString(string1: String) {
        filterString.value = string1
    }

    fun setSortByString(string1: String) {
        sortByString.value = string1
    }

    fun addCourse(courseCode: String, term: String, grade: String) {
        courseViewList.value.add(CourseView(this, courseCode, term, grade))
    }

    fun editCourse(course: CourseView, term: String, grade: String) {
        courseViewList.value.add(CourseView(this, course.getCourseCode(), term, grade))
        courseViewList.value.remove(course)
    }

    fun removeCourse(course: CourseView) {
        courseViewList.value.remove(course)
    }
}