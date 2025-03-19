package exam.jsb_webshop_t2406e.error;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;

// Xử lý lỗi chung chung, không phân loại

/**
 * @author Le Quang Huy
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

