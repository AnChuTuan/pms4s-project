package com.pms4st.pms.controller;

import com.pms4st.pms.entity.*;
import com.pms4st.pms.exception.ResourceNotFoundException;
import com.pms4st.pms.service.AppService; 
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/projects") // Base URL for projects
public class ProjectController {

    private static final Logger log = LoggerFactory.getLogger(ProjectController.class);
    @Autowired private AppService appService;

    // === Project Views ===
    @GetMapping
    public String listProjects(Model model, Principal principal) {
        if(principal==null) return "redirect:/login";
        model.addAttribute("projects", appService.findProjectsForUser(principal.getName()));
        return "project-list";
    }

    @GetMapping("/new")
    public String showCreateProjectForm(Model model) {
        model.addAttribute("project", new Project());
        return "project-form";
    }

    @GetMapping("/{projectId}")
    public String viewProject(@PathVariable Long projectId, Model model, Principal principal, RedirectAttributes ra) {
        if (principal == null) {
            log.warn("Attempt to view project detail without authentication.");
            return "redirect:/login";
        }
        String username = principal.getName();
        try {
            // Get project & verify user access
            Project project = appService.findProjectByIdForUser(projectId, username);

            // Get related data using service methods
            List<Task> tasks = appService.findTasksByProjectId(projectId);
            List<Comment> projectComments = appService.findCommentsForProject(projectId); 
            List<User> potentialMembers = appService.findPotentialMembers(projectId);

            // Add attributes to the model for the Thymeleaf template
            model.addAttribute("project", project);
            model.addAttribute("tasks", tasks);
            model.addAttribute("projectComments", projectComments);
            model.addAttribute("projectMembers", project.getMembers()); 
            model.addAttribute("potentialMembers", potentialMembers);
            model.addAttribute("newComment", new Comment()); 

            return "project-detail"; 

        } catch (ResourceNotFoundException | AccessDeniedException e) {
            log.warn("Access denied or project not found for ID {} by user {}: {}", projectId, username, e.getMessage());
            ra.addFlashAttribute("errorMessage", e.getMessage()); 
            return "redirect:/projects"; 
        } catch (Exception e) {
            // Catch unexpected errors
            log.error("Error viewing project ID {} by user {}: {}", projectId, username, e.getMessage(), e); // Log stack trace
            ra.addFlashAttribute("errorMessage", "An unexpected error occurred loading project details.");
            return "redirect:/projects";
        }
    }

    @GetMapping("/{projectId}/edit")
    public String showEditProjectForm(@PathVariable Long projectId, Model model, Principal principal, RedirectAttributes ra) {
         if(principal==null) return "redirect:/login";
        try {
            model.addAttribute("project", appService.findProjectByIdForUser(projectId, principal.getName()));
            return "project-form";
        } catch (Exception e) {
            ra.addFlashAttribute("errorMessage", "Could not load project for edit: " + e.getMessage());
            return "redirect:/projects";
        }
    }

    // === Project Actions ===
    @PostMapping("/save")
    public String saveProject(@ModelAttribute("project") Project project, BindingResult result, // Basic binding
                             Principal principal, RedirectAttributes ra, Model model) {
         if(principal==null) return "redirect:/login";
        // Manual basic validation
        if(project.getName()==null || project.getName().isBlank()){ result.rejectValue("name","","Name required"); }
        if(result.hasErrors()){ return "project-form"; }

        try {
            appService.saveProject(project, principal.getName());
            ra.addFlashAttribute("successMessage", "Project saved!");
            return "redirect:/projects";
        } catch (Exception e) {
             model.addAttribute("errorMessage", "Save failed: "+e.getMessage());
             return "project-form";
        }
    }

     @PostMapping("/{projectId}/delete")
    public String deleteProject(@PathVariable Long projectId, Principal principal, RedirectAttributes ra) {
        if(principal==null) return "redirect:/login";
        try {
            appService.deleteProject(projectId, principal.getName());
            ra.addFlashAttribute("successMessage", "Project deleted.");
        } catch (Exception e) {
             ra.addFlashAttribute("errorMessage", "Delete failed: "+e.getMessage());
        }
        return "redirect:/projects";
    }

    // --- Project Member Actions ---
    @PostMapping("/{projectId}/members/add")
    public String addMember(@PathVariable Long projectId, @RequestParam Long userId, Principal principal, RedirectAttributes ra) {
         if(principal==null) return "redirect:/login";
        try {
            appService.addMemberToProject(projectId, userId, principal.getName());
            ra.addFlashAttribute("successMessage", "Member added.");
        }
        catch (Exception e) {
            log.warn("Error adding member {} to project {}: {}", userId, projectId, e.getMessage());
            ra.addFlashAttribute("errorMessage", "Failed to add member: "+e.getMessage());
        }
        return "redirect:/projects/" + projectId;
    }

    @PostMapping("/{projectId}/members/remove")
    public String removeMember(@PathVariable Long projectId, @RequestParam Long userId, Principal principal, RedirectAttributes ra) {
         if(principal==null) return "redirect:/login";
        try {
            appService.removeMemberFromProject(projectId, userId, principal.getName());
            ra.addFlashAttribute("successMessage", "Member removed.");
        }
        catch (Exception e) {
            log.warn("Error removing member {} from project {}: {}", userId, projectId, e.getMessage());
            ra.addFlashAttribute("errorMessage", "Failed to remove member: "+e.getMessage());
        }
        return "redirect:/projects/" + projectId;
    }

    // --- Project Comment Actions ---
    @PostMapping("/{projectId}/comments")
    public String addProjectComment(@PathVariable Long projectId, @RequestParam String content, Principal principal, RedirectAttributes ra) {
        if(principal==null) return "redirect:/login";
        if(content == null || content.isBlank()){
             ra.addFlashAttribute("errorMessage", "Comment cannot be empty.");
             return "redirect:/projects/"+projectId;
        }
        try {
            appService.addComment(content, projectId, null, principal.getName()); 
        } catch (Exception e) {
             log.warn("Error adding project comment to project {}: {}", projectId, e.getMessage());
             ra.addFlashAttribute("errorMessage", "Failed to add comment: "+e.getMessage());
        }
        return "redirect:/projects/" + projectId;
    }

    // === Task Actions (within ProjectController) ===

    @GetMapping("/{projectId}/tasks/new")
    public String showCreateTaskForm(@PathVariable Long projectId, Model model, Principal principal, RedirectAttributes ra) {
        if(principal==null) return "redirect:/login";
        try {
            Project project = appService.findProjectByIdForUser(projectId, principal.getName());
            Task task = new Task(); task.setProject(project);
            model.addAttribute("task", task);
            model.addAttribute("projectId", projectId);
            model.addAttribute("members", project.getMembers());
            return "task-form";
        } catch (Exception e) {
            ra.addFlashAttribute("errorMessage", "Cannot create task: "+e.getMessage());
            return "redirect:/projects/"+projectId;
        }
    }

    @GetMapping("/{projectId}/tasks/{taskId}/edit")
    public String showEditTaskForm(@PathVariable Long projectId, @PathVariable Long taskId, Model model, Principal principal, RedirectAttributes ra) {
         if(principal==null) return "redirect:/login";
         try {
             Task task = appService.findTaskByIdForUser(taskId, principal.getName()); // Checks access via project
             if (!task.getProject().getId().equals(projectId)) throw new ResourceNotFoundException("Task not in project");
             model.addAttribute("task", task);
             model.addAttribute("projectId", projectId);
             model.addAttribute("members", task.getProject().getMembers());
             return "task-form";
         } catch (Exception e) {
             ra.addFlashAttribute("errorMessage", "Cannot edit task: "+e.getMessage());
             return "redirect:/projects/"+projectId;
         }
    }

     @PostMapping("/{projectId}/tasks/save")
    public String saveTask(@PathVariable Long projectId, @ModelAttribute("task") Task task, BindingResult result, // Basic binding
                           Principal principal, RedirectAttributes ra, Model model) {
         if(principal==null) return "redirect:/login";
         // Manual validation
         if(task.getName()==null || task.getName().isBlank()){ result.rejectValue("name", "", "Task name required"); }


         if(result.hasErrors()){
              try{ // Repopulate model for form redisplay
                 Project p = appService.findProjectByIdForUser(projectId, principal.getName());
                 model.addAttribute("projectId", projectId);
                 model.addAttribute("members", p.getMembers());
              } catch(Exception e){ return "redirect:/projects/"+projectId;}
              return "task-form";
         }

         try {
             if (task.getId() == null) { // Create
                 appService.saveTask(task, projectId, principal.getName());
                 ra.addFlashAttribute("successMessage", "Task created!");
             } else { // Update
                  Task existing = appService.findTaskById(task.getId());
                  if(!existing.getProject().getId().equals(projectId)) throw new AccessDeniedException("Project mismatch");
                 appService.updateTask(task, task.getId(), principal.getName());
                 ra.addFlashAttribute("successMessage", "Task updated!");
             }
             return "redirect:/projects/" + projectId;
         } catch (Exception e) {
              ra.addFlashAttribute("errorMessage", "Failed to save task: "+e.getMessage());
              return "redirect:/projects/" + projectId; // Redirect on error
         }
    }

    @PostMapping("/{projectId}/tasks/{taskId}/delete")
    public String deleteTask(@PathVariable Long projectId, @PathVariable Long taskId, Principal principal, RedirectAttributes ra) {
        if(principal==null) return "redirect:/login";
        try {
             Task task = appService.findTaskById(taskId);
             if (!task.getProject().getId().equals(projectId)) throw new AccessDeniedException("Task project mismatch");
            appService.deleteTask(taskId, principal.getName()); // Service checks access
            ra.addFlashAttribute("successMessage", "Task deleted.");
        } catch (Exception e) {
             ra.addFlashAttribute("errorMessage", "Failed to delete task: "+e.getMessage());
        }
        return "redirect:/projects/" + projectId;
    }

     // --- Task Comment Actions ---
     @PostMapping("/{projectId}/tasks/{taskId}/comments")
     public String addTaskComment(@PathVariable Long projectId, @PathVariable Long taskId, @RequestParam String content, Principal principal, RedirectAttributes ra) {
         if(principal==null) return "redirect:/login";
         if(content == null || content.isBlank()){ ra.addFlashAttribute("errorMessage", "Comment empty."); return "redirect:/projects/"+projectId;}
         try {
             Task task = appService.findTaskById(taskId);
             if (!task.getProject().getId().equals(projectId)) throw new AccessDeniedException("Task project mismatch");

             appService.addComment(content, null, taskId, principal.getName()); 
         } catch (Exception e) {
             ra.addFlashAttribute("errorMessage", "Failed to add task comment: "+e.getMessage());
         }
         return "redirect:/projects/" + projectId;
     }
}