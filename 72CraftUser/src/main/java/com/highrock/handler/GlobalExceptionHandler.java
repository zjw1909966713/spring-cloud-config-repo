package com.highrock.handler;

import com.highrock.util.BaseJsonRst;
import com.highrock.util.HRContants;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

/**
 * 全局统一异常处理
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 应用到所有@RequestMapping注解方法，在其执行之前初始化数据绑定器
     * @param binder
     */
    @InitBinder
    public void initBinder(WebDataBinder binder) {}

    /**
     * 把值绑定到Model中，使全局@RequestMapping可以获取到该值
     * @param model
     */
    @ModelAttribute
    public void addAttributes(Model model) {
        model.addAttribute("author", "Magical Sam");
    }

    /**
     * 全局异常捕捉处理
     * @param ex
     * @return
     */
    @ResponseBody
    @ExceptionHandler(value = Exception.class)
    public BaseJsonRst errorHandler(Exception ex) {
       BaseJsonRst ret=new BaseJsonRst();
       ret.setSuccess(false);
       ret.setCode(HRContants.CODE404);
       ret.setMessage(ex.getMessage());
        return ret;
    }


}
