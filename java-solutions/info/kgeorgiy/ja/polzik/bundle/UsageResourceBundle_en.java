package info.kgeorgiy.ja.polzik.bundle;

import java.util.ListResourceBundle;
// :NOTE: * Использование ListResourceBundle вместо PropertyResourceBundle

public class UsageResourceBundle_en extends ListResourceBundle {
    private final Object[][] CONTENTS = {
            {"Current dir", "Current directory"},
            {"Err", "Error"},
            {"No file", "File doesn't find"},
            {"I/O", "I/O Exception"},
            {"Move to new directory", "Successfully moved to new directory"},
            {"File", "File"},
            {"Directory", "Directory"},
            {"deleted", "deleted"},
            {"deleted (she)", "deleted"},
            {"created", "created"},
            {"created (she)", "created"},
            {"File/dir is exists", "File/directory is already exists"},
            {"File contents", "File contents"},
            {"End of file", "End of file"},
            {"Expected", "Expected"},
            {"Actual", "Actual"},
            {"command not exist", "Unknown command"},
            {"Unexpected exception occurred", "Unexpected exception occurred"}
    };

    @Override
    protected Object[][] getContents() {
        return CONTENTS;
    }
}
