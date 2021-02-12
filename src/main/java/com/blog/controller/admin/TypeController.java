package com.blog.controller.admin;


import com.blog.model.Type;
import com.blog.service.ITypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin")
public class TypeController {

    @Autowired
    ITypeService iTypeService;


    @GetMapping("/types")
    //Pageable選domin的
    //一頁10筆; 以Id做排序 ; direction並以降冪的方式呈現
    public String types(@PageableDefault(size = 3,sort = {"id"},direction = Sort.Direction.DESC) Pageable pageable,
                        Model model){

        //是Page物件
        model.addAttribute("page",iTypeService.listType(pageable));


        return "admin/types";
    }

    /**
     * 返回新增"分類"頁面
     * @return
     */
    @GetMapping("/types/input")
    public String input(){

        return "admin/types-input";
    }

    @PostMapping("/types")
    public String post(Type type , RedirectAttributes attributes){

        Type typeSaved = iTypeService.saveType(type);

        if (typeSaved == null){
            //操作消息顯示RedirectAttributes
            attributes.addFlashAttribute("message","操作失敗！");
        }
        else {
            attributes.addFlashAttribute("message","操作成功!!");
        }
        //重定向 -> 因為沒有經過樓上的types去查詢拿不到物件所以必須重定向
        return "redirect:/admin/types";
    }











}
