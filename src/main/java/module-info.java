module dh2jeong.a2enhanced {
    requires javafx.controls;
    requires javafx.fxml;
                requires kotlin.stdlib;
    
                            
    opens dh2jeong.a2enhanced to javafx.fxml;
    exports dh2jeong.a2enhanced;
}