package exam.jsb_webshop_t2406e.exception;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

// tham khảo:
// https://www.bezkoder.com/spring-boot-restcontrolleradvice/
// https://github.com/bezkoder/spring-boot-restcontrolleradvice
// Kiểm soát lỗi ApiControllerExceptionHandler
@RestControllerAdvice
public class RestControllerExceptionHandler 
{

    // Xử lý lỗi 400: Bad Request
    // https://www.baeldung.com/spring-boot-bean-validation
    // Độ ưu tiên còn cao hơn lỗi 500 khi chạy hàm create()
    // Hàm này dùng để xử lý ngoại lệ (exception) khi
    // dữ liệu đầu vào không vượt qua được yêu cầu kiểm duyệt
    // (data validation constraints)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, String> handleValidationExceptions(
            MethodArgumentNotValidException ex, WebRequest request) {

        // Danh sách các lỗi
        // Mỗi lỗi sẽ có cấu trúc ánh xạ:
        // Tên Trường => Lỗi liên quan
        Map<String, String> errors = new HashMap<>();

        // Thông tin ban đầu, hết sức chung chung
        // về Http Exception
        errors.put("status code", HttpStatus.BAD_REQUEST.toString());
        errors.put("timestamp", (new Date()).toString());
        errors.put("description", request.getDescription(false));

        // Bóc tách từng lỗi chi tiết trong Exception
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });

        // errors.put("description", request.getDescription(false));
        return errors;

    }

    // Xử lý lỗi 404: (Resource) Not Found
    // https://www.bezkoder.com/spring-boot-restcontrolleradvice/
    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    public ErrorMessage resourceNotFoundException(ResourceNotFoundException ex, WebRequest request) {
        ErrorMessage message = new ErrorMessage(
                HttpStatus.NOT_FOUND.value(),
                new Date(),
                ex.getMessage(),
                request.getDescription(false));

        return message;
    }

    // Xử lý lỗi 500: Internal Server Error
    // https://www.bezkoder.com/spring-boot-restcontrolleradvice/
    // Bắt lỗi với thứ tự ưu tiên cao hơn @Valid của jakarta.validation.Valid
    // hàm này hơi khó để bóc tách các error message riêng cho
    // từng trường thông tin.
    // Đây là Exception chung chung, không liên quan gì đến
    // Validation Exception
    @ExceptionHandler(Exception.class)
    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorMessage globalExceptionHandler(Exception ex, WebRequest request) {
        ErrorMessage message = new ErrorMessage(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                new Date(),
                ex.getMessage(),
                request.getDescription(false));

        return message;
    }

}// end class
