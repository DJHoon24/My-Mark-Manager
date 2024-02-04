package dh2jeong.a2enhanced

import dh2jeong.a2enhanced.Model.Model
import dh2jeong.a2enhanced.Views.ApplicationToolbar
import dh2jeong.a2enhanced.Views.StatusBar
import dh2jeong.a2enhanced.Views.VisualTabsPane
import javafx.application.Application
import javafx.scene.Scene
import javafx.scene.layout.BorderPane
import javafx.stage.Stage

class MainApplication : Application() {
    override fun start(stage: Stage) {
        val model = Model()
        val toolBar = ApplicationToolbar(model)
        val status = StatusBar(model)
        val visualTabsPane = VisualTabsPane(model)
        val root = BorderPane()
        root.left = toolBar
        root.bottom = status
        root.center = visualTabsPane
        stage.apply {
            title = "CS349 - A2 My Mark Visualization (Enhanced Version) - dh2jeong"
            scene = Scene(root, 900.0, 450.0)
        }.show()
    }

}