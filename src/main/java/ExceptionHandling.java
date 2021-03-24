import java.io.FileWriter;
import java.io.IOException;

public class ExceptionHandling {

    private static String start = "\n{\n" +
            "   \"type\":\"error\",\n" +
            "   \"message\":";
    private static String end = "\n}\n";

    private static final String fileNotFound = start + "\"не найден файл запроса\"" + end;

    private static final String invalidDateFormat = start + "\"неверный формат даты\"" + end;

    private static final String wrongRequest = start + "\"задан неверный критерий запроса\"" + end;

    private static final String wrongMapping = start + "\"неверный синтаксис запроса\"" + end;

    private static final String emptyParameter = start + "\"в запросе не указан обязательный параметр\"" + end;

    private static final String SQLException = start + "\"не удалось подключиться к базе данных проверьте параметры подключения и доступность базы данных\"" + end;

    private static final String operationTypeException = start + "\"неверный тип операции\"" + end;


    public static void response(String s, String outputPath) {
        try (FileWriter writer = new FileWriter(outputPath, false)) {
            writer.write(s);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String getFileNotFound() {
        return fileNotFound;
    }

    public static String getInvalidDateFormat() {
        return invalidDateFormat;
    }

    public static String getWrongRequest() {
        return wrongRequest;
    }

    public static String getWrongMapping() {
        return wrongMapping;
    }

    public static String getEmptyParameter() {
        return emptyParameter;
    }

    public static String getSQLException() {
        return SQLException;
    }

    public static String getOperationTypeException() {
        return operationTypeException;
    }
}
