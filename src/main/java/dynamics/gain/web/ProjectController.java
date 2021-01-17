package dynamics.gain.web;

import com.google.gson.Gson;
import dynamics.gain.model.Project;
import dynamics.gain.service.ProjectService;
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

}
