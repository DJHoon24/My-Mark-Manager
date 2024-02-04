package dh2jeong.a2enhanced.Views

import javafx.beans.property.ReadOnlyBooleanProperty
import javafx.beans.property.ReadOnlyBooleanWrapper
import javafx.collections.FXCollections
import javafx.geometry.Insets
import javafx.geometry.Pos
import javafx.scene.control.CheckBox
import javafx.scene.control.ComboBox
import javafx.scene.control.TextField
import javafx.scene.layout.HBox
import javafx.scene.layout.Priority

class CourseInputForm(courseCode: String, term: String, grade: String) : HBox() {
    private val courseCode = courseCode
    private val term = term
    private val grade = grade
    private val courseCodeTextArea = TextField(courseCode).apply {
        prefWidth = 90.0
        alignment = Pos.CENTER_LEFT
        HBox.setMargin(this, Insets(5.0))
        if (courseCode.isNotBlank()) {
            isEditable = false
        }
    } // Required and not editable
    private val termOptions = FXCollections.observableArrayList("F20", "W21", "S21", "F21", "W22", "S22", "F22", "W23")
    private val termDropdown = ComboBox(termOptions).apply {
        HBox.setMargin(this, Insets(5.0))
        if (term.isNotBlank()) {
            selectionModel.select(term)
        }
    }
    private val gradeTextArea = TextField(grade).apply {
        prefWidth = 40.0
        alignment = Pos.CENTER
        HBox.setMargin(this, Insets(5.0))
    }
    private val inputChanged = ReadOnlyBooleanWrapper(false)
    val InputChanged: ReadOnlyBooleanProperty = inputChanged.readOnlyProperty
    private val validInput = ReadOnlyBooleanWrapper(false)
    val ValidInput: ReadOnlyBooleanProperty = validInput.readOnlyProperty

    init {
        this.children.addAll(courseCodeTextArea, termDropdown, gradeTextArea)
        courseCodeTextArea.textProperty().addListener { _, _, _ -> inputChanged.value = isInputChanged() }
        gradeTextArea.textProperty().addListener { _, _, _ -> inputChanged.value = isInputChanged() }
        termDropdown.valueProperty().addListener { _, _, _ -> inputChanged.value = isInputChanged() }
        courseCodeTextArea.textProperty().addListener { _, _, _ -> validInput.value = isValidInput() }
        gradeTextArea.textProperty().addListener { _, _, _ -> validInput.value = isValidInput() }
        termDropdown.valueProperty().addListener { _, _, _ -> validInput.value = isValidInput() }
    }

    fun getGradeTextArea(): TextField {
        return this.gradeTextArea
    }

    fun getCourseCodeTextArea(): TextField {
        return this.courseCodeTextArea
    }

    fun getTermDropdown(): ComboBox<String> {
        return this.termDropdown
    }

    private fun isInputChanged(): Boolean {
        return (courseCodeTextArea.textProperty().get() != courseCode) || (gradeTextArea.textProperty()
            .get() != grade) || (termDropdown.valueProperty()
            .get() != null && termDropdown.valueProperty().get() != term)
    }

    private fun isValidInput(): Boolean {
        val integerGrade = gradeTextArea.text.toIntOrNull()
        return (courseCodeTextArea.text.isNotBlank() && !termDropdown.selectionModel.selectedItem.isNullOrEmpty() &&
                (gradeTextArea.text == "WD" || (integerGrade != null && integerGrade <= 100 && integerGrade >= 0)))
    }
}