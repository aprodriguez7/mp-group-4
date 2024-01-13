module mp.ims.mpgroup4 {
    //For Database implementation
    requires java.sql;
    //For GUI Implementation
    requires javafx.graphics;
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;

    //Miscellaneous Implementations

    opens mp.ims.mpgroup4 to javafx.fxml;
    opens mp.ims.mpgroup4.controllers to javafx.fxml;
    //exports mp.ims.mpgroup4;
    exports primary;
    exports mp.ims.mpgroup4.controllers;
}