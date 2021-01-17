package okay.web;

import com.google.gson.Gson;
import okay.model.Project;
import okay.model.ProjectEmail;
import okay.model.ProjectPhone;
import okay.service.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;

@Controller
public class ProjectController {

    Gson gson = new Gson();

    @Autowired
    ProjectService projectService;

    @GetMapping(value="/project/create")
    public String index(ModelMap modelMap){
        return projectService.create(modelMap);
    }

    @GetMapping(value="/project/{id}")
    public String index(ModelMap modelMap,
                        @PathVariable Long id){
        return projectService.index(id, modelMap);
    }

    @PostMapping(value="/project/save")
    protected String save(@ModelAttribute("project") Project project,
                          RedirectAttributes redirect){
        return projectService.save(project, redirect);
    }

    @PostMapping(value="/project/validate")
    protected @ResponseBody String save(@RequestParam(name="uri", required=true) String uri){
        return gson.toJson(projectService.validate(uri));
    }

    @GetMapping(value="/admin/project/list")
    public String getProjects(ModelMap modelMap){
        return projectService.getProjects(modelMap);
    }

    @GetMapping(value="/project/overview")
    public String getOverview(ModelMap modelMap){
        return projectService.getOverview(modelMap);
    }

    @GetMapping(value="/project/edit/{id}")
    public String getEdit(ModelMap modelMap,
                              @PathVariable Long id){
        return projectService.getEdit(id, modelMap);
    }

    @PostMapping(value="/project/update")
    protected String update(@ModelAttribute("project") Project project,
                            RedirectAttributes redirect){
        return projectService.update(project, redirect);
    }

    @PostMapping(value="/project/delete/{id}")
    protected String delete(@PathVariable Long id,
                            RedirectAttributes redirect){
        return projectService.delete(id, redirect);
    }

    @GetMapping(value="/project/phone/add/{id}")
    public String addPhone(@PathVariable Long id,
                           ModelMap modelMap){
        return projectService.addPhone(id, modelMap);
    }

    @PostMapping(value="/project/phone/add/{id}")
    protected String addPhone(@PathVariable Long id,
                          @ModelAttribute("projectPhone") ProjectPhone projectPhone,
                          HttpServletRequest request,
                          RedirectAttributes redirect){
        return projectService.addPhone(id, projectPhone, request, redirect);
    }

    @PostMapping(value="/project/phone/delete/{id}")
    protected String deletePhone(@PathVariable Long id,
                          HttpServletRequest request,
                          RedirectAttributes redirect){
        return projectService.deletePhone(id, request, redirect);
    }

    @GetMapping(value="/project/email/add/{id}")
    public String addEmail(@PathVariable Long id,
                           ModelMap modelMap){
        return projectService.addEmail(id, modelMap);
    }

    @PostMapping(value="/project/email/add/{id}")
    protected String addEmail(@PathVariable Long id,
                          @ModelAttribute("projectEmail") ProjectEmail projectEmail,
                          HttpServletRequest request,
                          RedirectAttributes redirect){
        return projectService.addEmail(id, projectEmail, request, redirect);
    }

    @PostMapping(value="/project/email/delete/{id}")
    protected String deleteEmail(@PathVariable Long id,
                          HttpServletRequest request,
                          RedirectAttributes redirect){
        return projectService.deleteEmail(id, request, redirect);
    }

}
