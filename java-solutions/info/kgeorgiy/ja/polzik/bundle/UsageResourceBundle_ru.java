package info.kgeorgiy.ja.polzik.bundle;

import java.util.ListResourceBundle;

public class UsageResourceBundle_ru extends ListResourceBundle {
    private final Object[][] CONTENTS = {
            {"Current dir", "Текущая директория"},
            {"Err", "Ошибка"},
            {"No file", "Файл не обнаружен"},
            {"I/O", "Ошибка ввода/вывода"},
            {"Move to new directory", "Успешно переместились в новую директорию"},
            {"File", "Файл"},
            {"Directory", "Директория"},
            {"deleted", "удален"},
            {"deleted (she)", "удалена"},
            {"created", "создан"},
            {"created (she)", "создана"},
            {"File/dir is exists", "Файл/директория с таким именем уже создан"},
            {"File contents", "Содержимое файла"},
            {"End of file", "Конец файла"},
            {"Expected", "Ожидалось"},
            {"Actual", "Фактически"},
            {"command not exist", "Неизвестная команда"},
            {"Unexpected exception occurred", "Обнаружена неизвестная ошибка"}
    };

    @Override
    protected Object[][] getContents() {
        return CONTENTS;
    }
}
