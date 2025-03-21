package exam.jsb_webshop_t2406e.error;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;

// Xử lý lỗi chung chung, không phân loại

/**
 * @author Fpt Aptech T2406E
 * 
 * MvcErrorController này chẳng có tác dụng gì, khi trong dự án có
 * RestControllerAdvice
   public class RestControllerExceptionHandler.

   Tức là MvcErrorController rules bị viết đè bởi RestControllerExceptionHandler. rules
   https://stackoverflow.com/questions/43325685/spring-different-exception-handler-for-restcontroller-and-controller

   2025.03.20: đã tìm ra cách khắc phục:
   RestControllerAdvice(annotations = RestController.class)
    public class RestControllerExceptionHandler 

    Viết mã như thế thì sẽ loại bỏ ảnh hưởng của RestControllerExceptionHandler đối với các lỗi
    phát sinh bởi MvcController (view thymeleaf html controller)

    Vai trò của ErrorController là kiểm soát thông báo lỗi trả về cho người dùng
    trình duyệt web. Trang báo lỗi sẽ được trang hoàng đẹp mắt, thân thiện
    trước khi nó thực sự đến tay người dùng.
 */

@Controller
public class MvcErrorController implements ErrorController 
{

    // @RequestMapping("/error")
    // public String handleError() {
    // // bắt lỗi chung chung, đơn giản, không phân loại
    // //do something like logging
    // return "error.html";
    // }

    // Ghi chú này phải khớp với bên app.properties
    // server.error.path=/error
    @RequestMapping("/error")
    public String handleError(HttpServletRequest request) 
    {
        // Đối tượng chứa lỗi:
        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);

        // Phân tích kỹ chi tiết bên trong đối tượng lỗi
        // Phân loại lỗi để có thông báo phù hợp
        if (status != null) 
        {
            Integer statusCode = Integer.valueOf(status.toString());

            // Lỗi không hợp lệ, ví dụ: dữ liệu nhập không đúng
            if (statusCode == HttpStatus.BAD_REQUEST.value()) 
            {
                return "error-400.html";
            } 
            else    // Lỗi không tìm thấy trang
            if (statusCode == HttpStatus.NOT_FOUND.value()) 
            {
                return "error-404.html";
            } 
            else // Lỗi nội bộ máy chủ, ví dụ: Foreign Key Constraint failed
            if (statusCode == HttpStatus.INTERNAL_SERVER_ERROR.value()) 
            {
                return "error-500.html";
            }
        }

        // Lỗi chung chung, mơ hồ, không phân loại
        return "error.html";
    }

}

